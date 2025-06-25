package com.example.flashcardsystent.adapter;

/**
 * Adapter showing user created decks in management screens.
 */

import android.view.LayoutInflater; // inflates layout resources
import android.view.View;           // container view type
import android.view.ViewGroup;      // view group for list items
import android.widget.TextView;     // displays deck name

import androidx.annotation.NonNull;             // null-safety annotations
import androidx.recyclerview.widget.DiffUtil;   // calculates list diffs
import androidx.recyclerview.widget.ListAdapter; // adapter with diffutil support

import com.example.flashcardsystent.R;    // resource constants
import com.example.flashcardsystent.data.Deck; // deck entity

public class DeckAdapter extends ListAdapter<Deck, DeckAdapter.DeckViewHolder> {
    /** Listener for click events on deck items */
    private final OnDeckClickListener listener;

    public DeckAdapter(OnDeckClickListener listener) {
        super(DIFF_CALLBACK); // use diff util for efficient updates
        this.listener = listener;
    }

    @NonNull
    @Override
    public DeckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deck, parent, false);
        return new DeckViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DeckViewHolder holder, int position) {
        Deck currentDeck = getItem(position); // deck at this position
        holder.textViewName.setText(currentDeck.name);

        // forward click events to listener
        holder.itemView.setOnClickListener(v ->
                listener.onDeckClick(currentDeck)
        );

        // long click triggers context actions
        holder.itemView.setOnLongClickListener(v -> {
            listener.onDeckLongClick(currentDeck);
            return true;
        });
    }

    public Deck getItemAt(int position) {
        return getItem(position); // helper for swipe actions
    }

    /** Callback for deck click and long click events */
    public interface OnDeckClickListener {
        void onDeckClick(Deck deck);
        void onDeckLongClick(Deck deck);
    }

    /** Holds reference to deck item views */
    static class DeckViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        private final TextView textViewName;

        public DeckViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.deck_name); // deck title label
        }
    }

    // DiffUtil callback to detect item changes efficiently
    private static final DiffUtil.ItemCallback<Deck> DIFF_CALLBACK = new DiffUtil.ItemCallback<Deck>() {
        @Override
        public boolean areItemsTheSame(@NonNull Deck oldItem, @NonNull Deck newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Deck oldItem, @NonNull Deck newItem) {
            return oldItem.name.equals(newItem.name);
        }
    };
}