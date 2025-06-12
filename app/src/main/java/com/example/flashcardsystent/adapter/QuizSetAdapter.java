package com.example.flashcardsystent.ui.quiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.flashcardsystent.data.Deck;
import java.util.List;

public class QuizSetAdapter extends RecyclerView.Adapter<QuizSetAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Deck deck);
    }

    private List<Deck> sets;
    private OnItemClickListener listener;

    public QuizSetAdapter(List<Deck> sets, OnItemClickListener listener) {
        this.sets = sets;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Deck deck = sets.get(position);
        ((TextView) holder.itemView).setText(deck.name);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(deck));
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
