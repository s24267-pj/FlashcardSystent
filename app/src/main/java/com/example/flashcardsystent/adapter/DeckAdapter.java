package com.example.flashcardsystent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.data.Deck;

/**
 * Adapter showing user-created decks in management screens.
 * Supports click and long-click events for actions like rename or delete.
 */
public class DeckAdapter extends ListAdapter<Deck, DeckAdapter.DeckViewHolder> {

    /** Listener for click events on deck items */
    private final OnDeckClickListener listener;

    /**
     * Constructs a DeckAdapter with the specified click listener.
     * @param listener callback for deck click and long-click events
     */
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

    /**
     * Callback interface for deck click and long-click events.
     */
    public interface OnDeckClickListener {
        /**
         * Called when a deck is clicked.
         * @param deck the selected deck
         */
        void onDeckClick(Deck deck);

        /**
         * Called when a deck is long-clicked.
         * @param deck the selected deck
         */
        void onDeckLongClick(Deck deck);
    }

    /**
     * ViewHolder holding references to deck item views.
     */
    static class DeckViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        /** TextView displaying the deck name */
        private final TextView textViewName;

        /**
         * Constructs the view holder.
         * @param itemView the deck item view
         */
        public DeckViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.deck_name); // deck title label
        }
    }

    /**
     * DiffUtil callback to detect item changes efficiently.
     */
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
