package com.example.flashcardsystent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.data.Deck;

import java.util.ArrayList;
import java.util.List;

public class DeckAdapter extends RecyclerView.Adapter<DeckAdapter.DeckViewHolder> {

    private final OnDeckClickListener listener;
    private List<Deck> deckList = new ArrayList<>();

    public DeckAdapter(OnDeckClickListener listener) {
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
        Deck currentDeck = deckList.get(position);
        holder.textViewName.setText(currentDeck.name);

        holder.itemView.setOnClickListener(v ->
                listener.onDeckClick(currentDeck)
        );

        holder.itemView.setOnLongClickListener(v -> {
            listener.onDeckLongClick(currentDeck);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return deckList.size();
    }

    public void setDeckList(List<Deck> decks) {
        this.deckList = decks;
        notifyDataSetChanged();
    }

    public interface OnDeckClickListener {
        void onDeckClick(Deck deck);

        void onDeckLongClick(Deck deck);
    }

    static class DeckViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewName;

        public DeckViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.deck_name);
        }
    }
}
