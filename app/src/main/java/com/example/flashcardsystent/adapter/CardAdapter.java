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

/**
 * RecyclerView adapter that displays flashcards.
 * Javadoc style comments describe the purpose of each component.
 */
public class CardAdapter extends ListAdapter<Card, CardAdapter.CardVH> {

    /** Listener for card click events */
    private final OnCardClickListener clickListener;

    /**
     * Creates the adapter.
     * @param clickListener callback invoked when a card is clicked
     */
    public CardAdapter(OnCardClickListener clickListener) {
        super(DIFF_CALLBACK);   // enable diffing for efficient updates
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CardVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new CardVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CardVH holder, int position) {
        Card c = getItem(position); // Retrieve card for position
        holder.front.setText(c.front);  // Populate front text
        holder.back.setText(c.back);    // Populate back text

        // Forward click events to listener
        holder.itemView.setOnClickListener(v -> clickListener.onCardClick(c));
    }

    /**
     * Returns the card at the specified adapter position.
     * @param position the index in the list
     * @return card at the given position
     */
    public Card getItemAt(int position) {
        return getItem(position);   // Convenience method used by tests
    }

    /**
     * Callback for click events on cards.
     */
    public interface OnCardClickListener {
        /**
         * Called when a card is clicked.
         * @param card the selected card
         */
        void onCardClick(Card card);
    }

    /**
     * ViewHolder that holds references to the views for a single card item.
     */
    static class CardVH extends RecyclerView.ViewHolder {
        /** TextViews showing front and back of the card */
        final TextView front, back;

        /**
         * Constructs the ViewHolder.
         * @param itemView view containing card layout
         */
        CardVH(@NonNull View itemView) {
            super(itemView);    // Initialize base class
            front = itemView.findViewById(R.id.text_front); // Locate front text
            back = itemView.findViewById(R.id.text_back);   // Locate back text
        }
    }

    /**
     * DiffUtil callback to optimize list updates.
     */
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
