package com.example.flashcardsystent.adapter;

/**
 * RecyclerView adapter that displays flashcards.
 * Javadoc style comments describe the purpose of each component.
 */
import android.view.LayoutInflater; // creates view objects from XML
import android.view.View;   // basic building block for layouts
import android.view.ViewGroup;  // container that holds other views
import android.widget.TextView; // displays text in the list

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;   // calculates differences between lists
import androidx.recyclerview.widget.ListAdapter;    // adapter base class with diff support
import androidx.recyclerview.widget.RecyclerView;   // widget for efficient display of large datasets

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.data.Card;

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
        // inflate the layout for each item
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new CardVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CardVH holder, int position) {
        Card c = getItem(position); // retrieve card for position
        holder.front.setText(c.front);  // populate front text
        holder.back.setText(c.back);    // populate back text

        // forward click events to listener
        holder.itemView.setOnClickListener(v -> clickListener.onCardClick(c));
    }

    public Card getItemAt(int position) {
        return getItem(position);   // convenience method used by tests
    }

    /**
     * Callback for click events on cards.
     */
    public interface OnCardClickListener {
        void onCardClick(Card card);
    }

    /**
     * ViewHolder that holds references to the views for a single card item.
     */
    static class CardVH extends RecyclerView.ViewHolder {
        final TextView front, back; // text views showing front/back of the card

        CardVH(@NonNull View itemView) {
            super(itemView);    // initialize base class
            front = itemView.findViewById(R.id.text_front); // locate front text
            back = itemView.findViewById(R.id.text_back);   // locate back text
        }
    }

    // DiffUtil callback to optimize list updates
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
