package com.example.flashcardsystent.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.data.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView adapter that displays a list of editable flashcard rows.
 * As the user fills out the last row, a new blank row is automatically
 * appended so they can continue entering cards without interruption.
 */
public class CardInputAdapter extends RecyclerView.Adapter<CardInputAdapter.CardInputVH> {

    /** Cards being edited, one per visible row */
    private final List<Card> items = new ArrayList<>();
    /** Deck identifier used for newly created cards */
    private final int deckId;

    /**
     * Constructs the adapter and pre-populates it with empty rows.
     * @param deckId ID of the deck the new cards belong to
     */
    public CardInputAdapter(int deckId) {
        this.deckId = deckId;
        for (int i = 0; i < 3; i++) {
            items.add(new Card(deckId, "", ""));
        }
    }

    @NonNull
    @Override
    public CardInputVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_input, parent, false);
        return new CardInputVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CardInputVH holder, int position) {
        Card initialCard = items.get(position);
        holder.front.setText(initialCard.front);
        holder.back.setText(initialCard.back);

        holder.front.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int pos = holder.getAdapterPosition();
                if (pos == RecyclerView.NO_POSITION) return;
                Card card = items.get(pos);
                card.front = holder.front.getText().toString().trim();
                maybeAddNewRow(pos);
            }
        });

        holder.back.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int pos = holder.getAdapterPosition();
                if (pos == RecyclerView.NO_POSITION) return;
                Card card = items.get(pos);
                card.back = holder.back.getText().toString().trim();
                maybeAddNewRow(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Adds a new empty row when the last visible row contains text in both fields.
     * @param pos index of the modified row
     */
    private void maybeAddNewRow(int pos) {
        if (pos == items.size() - 1) {
            Card last = items.get(pos);
            if (!last.front.isEmpty() && !last.back.isEmpty()) {
                items.add(new Card(deckId, "", ""));
                notifyItemInserted(items.size() - 1);
            }
        }
    }

    /**
     * Returns only the cards that contain text on both sides.
     * @return list of filled cards
     */
    public List<Card> getFilledCards() {
        List<Card> out = new ArrayList<>();
        for (Card c : items) {
            if (!c.front.isEmpty() && !c.back.isEmpty()) {
                out.add(c);
            }
        }
        return out;
    }

    /**
     * ViewHolder containing input fields for a single card.
     */
    public static class CardInputVH extends RecyclerView.ViewHolder {
        /** References to EditText fields for front and back */
        final EditText front, back;

        /**
         * Constructs the view holder.
         * @param itemView view containing card input layout
         */
        CardInputVH(@NonNull View itemView) {
            super(itemView);
            front = itemView.findViewById(R.id.edit_front);
            back = itemView.findViewById(R.id.edit_back);
        }
    }

    /**
     * Helper class with empty implementations so we override only necessary methods.
     */
    private abstract static class SimpleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
        @Override
        public void afterTextChanged(Editable s) {}
    }
}
