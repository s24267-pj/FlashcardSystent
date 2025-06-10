package com.example.flashcardsystent.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flashcardsystent.R;
import com.example.flashcardsystent.data.Card;
import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardVH> {
    private final List<Card> items = new ArrayList<>();

    @NonNull @Override
    public CardVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new CardVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CardVH holder, int position) {
        Card c = items.get(position);
        holder.front.setText(c.front);
        holder.back.setText(c.back);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Card> list) {
        items.clear();
        if (list != null) items.addAll(list);
        notifyDataSetChanged();
    }

    static class CardVH extends RecyclerView.ViewHolder {
        final TextView front, back;
        CardVH(@NonNull View itemView) {
            super(itemView);
            front = itemView.findViewById(R.id.text_front);
            back  = itemView.findViewById(R.id.text_back);
        }
    }
}
