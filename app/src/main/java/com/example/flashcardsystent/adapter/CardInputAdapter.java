package com.example.flashcardsystent.adapter;

/**
 * RecyclerView adapter that displays a list of editable flashcard rows. As the
 * user fills out the last row, a new blank row is automatically appended so
 * they can continue entering cards without interruption.
 */

// Editable provides access to the text content that can be modified
import android.text.Editable;
// TextWatcher allows us to react to text changes in EditText fields
import android.text.TextWatcher;
// LayoutInflater turns XML layout files into View objects
import android.view.LayoutInflater;
// Generic base class representing a single UI element on screen
import android.view.View;
// ViewGroup is a container that can host multiple child views
import android.view.ViewGroup;
// EditText is a user editable text field
import android.widget.EditText;

// Annotation indicating a parameter cannot be null
import androidx.annotation.NonNull;
// ViewGroup that shows a scrolling list of elements
import androidx.recyclerview.widget.RecyclerView;

// Resource constants generated from XML files
import com.example.flashcardsystent.R;
// Data entity representing a flashcard
import com.example.flashcardsystent.data.Card;

// Resizable list implementation used to hold user input
import java.util.ArrayList;
// Base list interface type
import java.util.List;

public class CardInputAdapter extends RecyclerView.Adapter<CardInputAdapter.CardInputVH> {

    /** Cards being edited, one per visible row */
    private final List<Card> items = new ArrayList<>();
    /** Deck identifier used for newly created cards */
    private final int deckId;

    public CardInputAdapter(int deckId) {
        // Remember which deck newly created cards will belong to
        this.deckId = deckId;

        // Pre-populate the adapter with three empty card rows so the user
        // immediately has inputs available
        for (int i = 0; i < 3; i++) {
            // Each empty card uses the provided deck id but has no text yet
            items.add(new Card(deckId, "", ""));
        }
    }

    @NonNull
    @Override
    public CardInputVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate a new row using the item_card_input layout file
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_input, parent, false);

        // Wrap the resulting view in our custom ViewHolder class and return it
        return new CardInputVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CardInputVH holder, int position) {
        // Populate the EditText fields with any previously stored text for this
        // row. When the adapter is first created these will be empty strings.
        Card initialCard = items.get(position);
        holder.front.setText(initialCard.front);
        holder.back.setText(initialCard.back);

        // Watch for changes in the front field
        holder.front.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Determine which row triggered this callback
                int pos = holder.getAdapterPosition();
                // Ignore callbacks if the ViewHolder is no longer valid
                if (pos == RecyclerView.NO_POSITION) return;

                // Update the stored card object with the new text
                Card card = items.get(pos);
                card.front = holder.front.getText().toString().trim();

                // Potentially append a new empty row if this was the last item
                maybeAddNewRow(pos);
            }
        });
        // Watch for changes in the back field
        holder.back.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Determine which row triggered this callback
                int pos = holder.getAdapterPosition();
                if (pos == RecyclerView.NO_POSITION) return;

                // Update the corresponding card object with the typed text
                Card card = items.get(pos);
                card.back = holder.back.getText().toString().trim();

                // Append a new row when necessary
                maybeAddNewRow(pos);
            }
        });
    }


    @Override
    public int getItemCount() {
        // RecyclerView queries this method to know how many rows to display
        return items.size();
    }

    /**
     * Adds a new empty row when the last visible row contains text in both
     * fields. This lets the user continuously add cards without having to
     * manually press an "add" button.
     */
    private void maybeAddNewRow(int pos) {
        // Only attempt to add a new row if the changed item was the very last
        // row currently displayed
        if (pos == items.size() - 1) {
            Card last = items.get(pos);
            // If both sides of the card have text, append another blank card
            if (!last.front.isEmpty() && !last.back.isEmpty()) {
                items.add(new Card(deckId, "", ""));
                notifyItemInserted(items.size() - 1);
            }
        }
    }

    public List<Card> getFilledCards() {
        // Collect only the rows where both front and back text have been
        // provided by the user
        List<Card> out = new ArrayList<>();
        for (Card c : items) {
            if (!c.front.isEmpty() && !c.back.isEmpty()) {
                out.add(c);
            }
        }
        return out;
    }

    /** ViewHolder containing input fields for a single card */
    public static class CardInputVH extends RecyclerView.ViewHolder {
        // References to the EditText fields for front and back text
        final EditText front, back;

        CardInputVH(@NonNull View itemView) {
            super(itemView);
            // Look up the EditText widgets within the inflated row view
            front = itemView.findViewById(R.id.edit_front); // user input front
            back = itemView.findViewById(R.id.edit_back);  // user input back
        }
    }

    /**
     * Helper class with empty implementations so we only override needed methods.
     */
    private abstract static class SimpleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int st, int c, int a) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}