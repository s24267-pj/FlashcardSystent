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

public class BrowseSetAdapter extends RecyclerView.Adapter<BrowseSetAdapter.DeckViewHolder> {

    public interface OnDeckClickListener {
        void onDeckClick(Deck deck);
    }

    private final List<Deck> decks;
    private final OnDeckClickListener listener;

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

    static class DeckViewHolder extends RecyclerView.ViewHolder {
        final TextView deckName;

        public DeckViewHolder(@NonNull View itemView) {
            super(itemView);
            deckName = itemView.findViewById(R.id.deck_name);
        }
    }
}