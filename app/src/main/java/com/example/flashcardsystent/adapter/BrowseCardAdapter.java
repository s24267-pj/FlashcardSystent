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

    /** Interface to notify when a card is flipped (only to back side). */
    public interface OnCardFlipListener {
        void onCardFlipped(Card card);
    }

    private final List<Card> cards;
    private final OnCardFlipListener flipListener;

    /**
     * Constructor for the adapter.
     * @param cards list of cards to display
     * @param flipListener listener for card flip events
     */
    public BrowseCardAdapter(List<Card> cards, OnCardFlipListener flipListener) {
        this.cards = cards;
        this.flipListener = flipListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flashcard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Card card = cards.get(position);
        holder.bind(card);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    /**
     * ViewHolder class holding the UI and logic for a single flashcard.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;
        private boolean isFlipped = false;

        ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.flashcard_text);
        }

        /**
         * Binds the given card to the UI and sets up flipping behavior.
         * @param card flashcard data
         */
        void bind(Card card) {
            isFlipped = false;
            textView.setText(card.front);

            textView.setOnClickListener(v -> {
                isFlipped = !isFlipped;
                textView.setText(isFlipped ? card.back : card.front);
                if (isFlipped && flipListener != null) {
                    flipListener.onCardFlipped(card);
                }
            });
        }
    }
}
