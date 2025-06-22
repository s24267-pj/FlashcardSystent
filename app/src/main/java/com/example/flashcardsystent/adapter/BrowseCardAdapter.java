package com.example.flashcardsystent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.data.Card;

import java.util.List;

/**
 * Adapter used in browse mode to display flashcards in a RecyclerView.
 * Each card can be flipped between front and back.
 */
public class BrowseCardAdapter extends RecyclerView.Adapter<BrowseCardAdapter.ViewHolder> {

    // Interface to notify when a card is flipped (only to back side)
    public interface OnCardFlipListener {
        void onCardFlipped(Card card);
    }

    private final List<Card> cards; // List of cards to display in the list
    private final OnCardFlipListener flipListener; // Optional listener for flipped cards

    // Constructor – assigns provided list of cards and listener
    public BrowseCardAdapter(List<Card> cards, OnCardFlipListener flipListener) {
        this.cards = cards;
        this.flipListener = flipListener;
    }

    // This method is called to create a new ViewHolder object for each item
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout defined in item_flashcard.xml
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flashcard, parent, false);
        return new ViewHolder(view);
    }

    // Called when the adapter needs to bind data to a ViewHolder at a given position
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the card at the given position and pass it to the ViewHolder
        Card card = cards.get(position);
        holder.bind(card);
    }

    // Returns the total number of items (cards) in the list
    @Override
    public int getItemCount() {
        return cards.size();
    }

    /**
     * ViewHolder class holds the UI and logic for a single card in the list.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView; // Text view showing front or back of the card
        private boolean isFlipped = false; // Tracks if the current card is showing the back

        // ViewHolder constructor gets reference to the TextView in the item layout
        ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.flashcard_text);
        }

        // Binds card data to the UI and sets up the click-to-flip logic
        void bind(Card card) {
            // Reset flip state when the card is shown
            isFlipped = false;
            textView.setText(card.front); // Initially show the front text

            // Set click listener to flip the card when tapped
            textView.setOnClickListener(v -> {
                // Toggle the flip state
                isFlipped = !isFlipped;

                // Change the displayed text depending on current state
                textView.setText(isFlipped ? card.back : card.front);

                // Notify external listener only when card is flipped to back
                if (isFlipped && flipListener != null) {
                    flipListener.onCardFlipped(card);
                }
            });
        }
    }
}
