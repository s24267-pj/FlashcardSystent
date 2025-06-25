package com.example.flashcardsystent.adapter;
/**
 * Adapter used in browse mode to display available decks.
 * Clicking a deck allows the user to browse its flashcards.
 */
import android.view.LayoutInflater; // creates View objects from XML layouts
import android.view.View;   // basic building block for layouts
import android.view.ViewGroup;  // container for other views
import android.widget.TextView; // displays text labels

import androidx.annotation.NonNull; // marks parameters that cannot be null
import androidx.recyclerview.widget.RecyclerView;   // List component optimized for large datasets

import com.example.flashcardsystent.R;  // resource constants
import com.example.flashcardsystent.data.Deck;  // model representing a flashcard deck

import java.util.List;  // container for deck data

public class BrowseSetAdapter extends RecyclerView.Adapter<BrowseSetAdapter.DeckViewHolder> {

    /** Callback invoked when the user selects a deck from the list. */
    public interface OnDeckClickListener {
        void onDeckClick(Deck deck);
    }

    /** Decks displayed in the RecyclerView */
    private final List<Deck> decks;

    /** Listener notified of item clicks */
    private final OnDeckClickListener listener;

    public BrowseSetAdapter(List<Deck> decks, OnDeckClickListener listener) {
        this.decks = decks; // dataset to show
        this.listener = listener;   // save click handler
    }

    @NonNull
    @Override
    public DeckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item layout and create the holder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_deck, parent, false);
        return new DeckViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeckViewHolder holder, int position) {
        // Bind deck data and handle clicks
        Deck deck = decks.get(position);
        holder.deckName.setText(deck.name);
        holder.itemView.setOnClickListener(v -> listener.onDeckClick(deck));
    }

    @Override
    public int getItemCount() {
        return decks.size();    // number of decks to show
    }

    static class DeckViewHolder extends RecyclerView.ViewHolder {
        final TextView deckName;

        public DeckViewHolder(@NonNull View itemView) {
            super(itemView);
            deckName = itemView.findViewById(R.id.deck_name);   // label displaying deck name
        }
    }
}