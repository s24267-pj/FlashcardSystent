package com.example.flashcardsystent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.data.Card;

public class CardAdapter extends ListAdapter<Card, CardAdapter.CardVH> {

    private final OnCardClickListener clickListener;

    public CardAdapter(OnCardClickListener clickListener) {
        super(DIFF_CALLBACK);
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CardVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new CardVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CardVH holder, int position) {
        Card c = getItem(position);
        holder.front.setText(c.front);
        holder.back.setText(c.back);

        holder.itemView.setOnClickListener(v -> clickListener.onCardClick(c));
    }

    public Card getItemAt(int position) {
        return getItem(position);
    }

    public interface OnCardClickListener {
        void onCardClick(Card card);
    }

    static class CardVH extends RecyclerView.ViewHolder {
        final TextView front, back;

        CardVH(@NonNull View itemView) {
            super(itemView);
            front = itemView.findViewById(R.id.text_front);
            back = itemView.findViewById(R.id.text_back);
        }
    }

    private static final DiffUtil.ItemCallback<Card> DIFF_CALLBACK = new DiffUtil.ItemCallback<Card>() {
        @Override
        public boolean areItemsTheSame(@NonNull Card oldItem, @NonNull Card newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Card oldItem, @NonNull Card newItem) {
            return oldItem.front.equals(newItem.front) && oldItem.back.equals(newItem.back);
        }
    };
}
