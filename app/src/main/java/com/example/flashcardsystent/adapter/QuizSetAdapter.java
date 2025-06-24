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

public class QuizSetAdapter extends ListAdapter<Deck, QuizSetAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Deck deck);
    }

    private final OnItemClickListener listener;

    public QuizSetAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
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
        Deck deck = getItem(position);
        holder.deckName.setText(deck.name);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(deck));
    }

    public static class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        final TextView deckName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deckName = itemView.findViewById(R.id.deck_name);
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
