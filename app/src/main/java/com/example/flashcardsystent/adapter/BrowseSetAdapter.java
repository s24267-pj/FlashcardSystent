package com.example.flashcardsystent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.data.Deck;

import java.util.List;

/**
 * Adapter used in browse mode to display available decks.
 * Clicking a deck allows the user to browse its flashcards.
 */
public class BrowseSetAdapter extends RecyclerView.Adapter<BrowseSetAdapter.DeckViewHolder> {

    /**
     * Callback interface for deck click events.
     */
    public interface OnDeckClickListener {
        /**
         * Called when a deck is clicked.
         * @param deck the selected deck
         */
        void onDeckClick(Deck deck);
    }

    /** List of decks to display */
    private final List<Deck> decks;
    /** Listener for item click events */
    private final OnDeckClickListener listener;

    /**
     * Constructs the adapter.
     * @param decks list of decks
     * @param listener click listener for decks
     */
    public BrowseSetAdapter(List<Deck> decks, OnDeckClickListener listener) {
        this.decks = decks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DeckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deck, parent, false);
        return new DeckViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeckViewHolder holder, int position) {
        Deck deck = decks.get(position);
        holder.deckName.setText(deck.name);
        holder.itemView.setOnClickListener(v -> listener.onDeckClick(deck));
    }

    @Override
    public int getItemCount() {
        return decks.size();
    }

    /**
     * ViewHolder representing a single deck item.
     */
    static class DeckViewHolder extends RecyclerView.ViewHolder {
        /** TextView displaying the deck name */
        final TextView deckName;

        /**
         * Constructs the view holder.
         * @param itemView the view representing the item layout
         */
        public DeckViewHolder(@NonNull View itemView) {
            super(itemView);
            deckName = itemView.findViewById(R.id.deck_name);
        }
    }
}
