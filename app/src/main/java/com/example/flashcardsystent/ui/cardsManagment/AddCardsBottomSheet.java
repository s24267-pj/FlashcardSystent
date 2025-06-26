package com.example.flashcardsystent.ui.cardsManagment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.adapter.CardInputAdapter;
import com.example.flashcardsystent.data.Card;
import com.example.flashcardsystent.viewmodel.CardViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

/**
 * Bottom sheet dialog fragment allowing the user to add multiple flashcards
 * to a specific deck using a scrollable list of editable rows.
 */
public class AddCardsBottomSheet extends BottomSheetDialogFragment {

    /** Key used for passing deck ID via arguments */
    private static final String ARG_DECK_ID = "deckId";

    /** The ID of the deck to which new cards will be added */
    private int deckId;

    /** Shared ViewModel used to insert cards into the database */
    private CardViewModel cardViewModel;

    /** Adapter displaying editable rows for card input */
    private CardInputAdapter adapter;

    /**
     * Static factory method to create a new instance for a specific deck.
     *
     * @param deckId ID of the deck the cards will belong to
     * @return a configured instance of the bottom sheet
     */
    public static AddCardsBottomSheet newInstance(int deckId) {
        Bundle args = new Bundle();
        args.putInt(ARG_DECK_ID, deckId);

        AddCardsBottomSheet fragment = new AddCardsBottomSheet();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Initializes shared ViewModel and retrieves the deck ID from arguments.
     *
     * @param savedState previous saved state, if any
     */
    @Override
    public void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        deckId = requireArguments().getInt(ARG_DECK_ID);
        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
    }

    /**
     * Inflates the layout for the bottom sheet and sets up the card input list.
     *
     * @param inf the LayoutInflater
     * @param c the parent container
     * @param s saved state, if any
     * @return the view to be displayed
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle s) {
        View view = inf.inflate(R.layout.dialog_add_cards, c, false);

        RecyclerView rv = view.findViewById(R.id.rv_card_inputs);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new CardInputAdapter(deckId);
        rv.setAdapter(adapter);

        view.findViewById(R.id.button_save_cards).setOnClickListener(v -> {
            List<Card> toSave = adapter.getFilledCards();
            for (Card card : toSave) {
                cardViewModel.insert(card);
            }
            dismiss();
        });

        return view;
    }
}
