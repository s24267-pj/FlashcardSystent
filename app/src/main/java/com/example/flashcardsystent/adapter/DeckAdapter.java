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

public class DeckAdapter extends ListAdapter<Deck, DeckAdapter.DeckViewHolder> {

    private final OnDeckClickListener listener;

    public DeckAdapter(OnDeckClickListener listener) {
        super(DIFF_CALLBACK);
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
        Deck currentDeck = getItem(position);
        holder.textViewName.setText(currentDeck.name);

        holder.itemView.setOnClickListener(v ->
                listener.onDeckClick(currentDeck)
        );

        holder.itemView.setOnLongClickListener(v -> {
            listener.onDeckLongClick(currentDeck);
            return true;
        });
    }

    public Deck getItemAt(int position) {
        return getItem(position);
    }

    public interface OnDeckClickListener {
        void onDeckClick(Deck deck);
        void onDeckLongClick(Deck deck);
    }

    static class DeckViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        private final TextView textViewName;

        public DeckViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.deck_name);
        }
    }

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
