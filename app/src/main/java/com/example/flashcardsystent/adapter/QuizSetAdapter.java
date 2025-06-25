package com.example.flashcardsystent.adapter;

/**
 * RecyclerView adapter displaying decks when selecting a quiz or learning set.
 */

import android.view.LayoutInflater; // inflate XML into views
import android.view.View;           // root for each list item
import android.view.ViewGroup;      // parent for item views
import android.widget.TextView;     // shows deck names
import androidx.annotation.NonNull; // support library annotation
import androidx.recyclerview.widget.RecyclerView; // efficient list component

import com.example.flashcardsystent.R;    // resource access
import com.example.flashcardsystent.data.Deck; // deck model
import java.util.List; // list of decks

public class QuizSetAdapter extends RecyclerView.Adapter<QuizSetAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Deck deck);
    }

    /** Data set to display */
    final List<Deck> sets;
    /** Click handler for selecting a deck */
    final OnItemClickListener listener;

    public QuizSetAdapter(List<Deck> sets, OnItemClickListener listener) {
        this.sets = sets;       // list of decks
        this.listener = listener; // callback for clicks
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deck, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Deck deck = sets.get(position); // deck for row
        TextView deckName = holder.itemView.findViewById(R.id.deck_name);
        deckName.setText(deck.name);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(deck));
    }

    @Override
    public int getItemCount() {
        return sets.size(); // total decks
    }

    /** ViewHolder for a single deck entry */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}