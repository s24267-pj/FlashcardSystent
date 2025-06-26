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
 * RecyclerView adapter displaying decks when selecting a quiz or learning set.
 */
public class QuizSetAdapter extends RecyclerView.Adapter<QuizSetAdapter.ViewHolder> {

    /**
     * Listener interface for deck selection events.
     */
    public interface OnItemClickListener {
        /**
         * Called when a deck is clicked.
         * @param deck the selected deck
         */
        void onItemClick(Deck deck);
    }

    /** Data set to display */
    final List<Deck> sets;
    /** Click handler for selecting a deck */
    final OnItemClickListener listener;

    /**
     * Constructs the adapter.
     * @param sets the list of decks to show
     * @param listener the click listener for deck selection
     */
    public QuizSetAdapter(List<Deck> sets, OnItemClickListener listener) {
        this.sets = sets;
        this.listener = listener;
    }

    /**
     * Creates a new ViewHolder for a deck item.
     * @param parent the parent ViewGroup
     * @param viewType the type of the view (not used here)
     * @return a new ViewHolder instance
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deck, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Binds data to a ViewHolder at the specified position.
     * @param holder the ViewHolder to bind data to
     * @param position the position of the item in the data set
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Deck deck = sets.get(position);
        TextView deckName = holder.itemView.findViewById(R.id.deck_name);
        deckName.setText(deck.name);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(deck));
    }

    /**
     * Returns the total number of items in the adapter.
     * @return the item count
     */
    @Override
    public int getItemCount() {
        return sets.size();
    }

    /**
     * ViewHolder for a single deck entry.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * Constructs a ViewHolder with the given itemView.
         * @param itemView the view representing a single deck
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
