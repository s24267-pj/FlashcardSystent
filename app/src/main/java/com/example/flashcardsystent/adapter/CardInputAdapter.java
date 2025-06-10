package com.example.flashcardsystent.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.data.Card;

import java.util.ArrayList;
import java.util.List;

public class CardInputAdapter extends RecyclerView.Adapter<CardInputAdapter.CardInputVH> {

    private final List<Card> items = new ArrayList<>();
    private final int deckId;

    public CardInputAdapter(int deckId) {
        this.deckId = deckId;
        for (int i = 0; i < 3; i++) {
            items.add(new Card(deckId, "", ""));
        }
    }

    @NonNull
    @Override
    public CardInputVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_input, parent, false);
        return new CardInputVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CardInputVH holder, int position) {
        Card initialCard = items.get(position);
        holder.front.setText(initialCard.front);
        holder.back.setText(initialCard.back);

        holder.front.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int pos = holder.getAdapterPosition();
                if (pos == RecyclerView.NO_POSITION) return;
                Card card = items.get(pos);
                card.front = holder.front.getText().toString().trim();
                maybeAddNewRow(pos);
            }
        });
        holder.back.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int pos = holder.getAdapterPosition();
                if (pos == RecyclerView.NO_POSITION) return;
                Card card = items.get(pos);
                card.back = holder.back.getText().toString().trim();
                maybeAddNewRow(pos);
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    private void maybeAddNewRow(int pos) {
        if (pos == items.size() - 1) {
            Card last = items.get(pos);
            if (!last.front.isEmpty() && !last.back.isEmpty()) {
                items.add(new Card(deckId, "", ""));
                notifyItemInserted(items.size() - 1);
            }
        }
    }

    public List<Card> getFilledCards() {
        List<Card> out = new ArrayList<>();
        for (Card c : items) {
            if (!c.front.isEmpty() && !c.back.isEmpty()) {
                out.add(c);
            }
        }
        return out;
    }

    public static class CardInputVH extends RecyclerView.ViewHolder {
        final EditText front, back;

        CardInputVH(@NonNull View itemView) {
            super(itemView);
            front = itemView.findViewById(R.id.edit_front);
            back = itemView.findViewById(R.id.edit_back);
        }
    }

    private abstract static class SimpleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int st, int c, int a) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}
