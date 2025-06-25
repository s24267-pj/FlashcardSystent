package com.example.flashcardsystent.ui.cardsManagment;

/**
 * Bottom sheet dialog for adding multiple cards to a deck. Users can enter new
 * flashcards in a list and persist them all at once when finished.
 */

// Bundle stores saved state information for the fragment
import android.os.Bundle;
// LayoutInflater turns XML layout files into actual View objects
import android.view.LayoutInflater;
// Generic base class for any visual element on screen
import android.view.View;
// Container that can hold other views
import android.view.ViewGroup;

// Annotation definitions adding null-safety information
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
// Allows retrieving ViewModel instances scoped to the activity
import androidx.lifecycle.ViewModelProvider;
// Positions items in a vertical scrolling list within a RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager;
// View that efficiently displays large sets of data
import androidx.recyclerview.widget.RecyclerView;

// Reference to resource ids defined in XML
import com.example.flashcardsystent.R;
// RecyclerView adapter responsible for presenting editable card inputs
import com.example.flashcardsystent.adapter.CardInputAdapter;
// Data class representing a single flashcard
import com.example.flashcardsystent.data.Card;
// ViewModel used to insert cards into the database
import com.example.flashcardsystent.viewmodel.CardViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List; // collection of cards

public class AddCardsBottomSheet extends BottomSheetDialogFragment {
    // Name of the argument used to pass the deck id into the fragment
    private static final String ARG_DECK_ID = "deckId";
    // Id of the deck that the new cards should be inserted into
    private int deckId;
    // ViewModel shared with the activity for database operations
    private CardViewModel cardViewModel;
    // Adapter supplying rows of editable cards
    private CardInputAdapter adapter;

    /**
     * Factory method to create the dialog for a specific deck.
     */
    public static AddCardsBottomSheet newInstance(int deckId) {
        // Bundle up the deck id so it can be retrieved after recreation
        Bundle args = new Bundle();
        args.putInt(ARG_DECK_ID, deckId);

        // Create the fragment instance and attach the arguments
        AddCardsBottomSheet f = new AddCardsBottomSheet();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        // Always delegate to parent implementation first
        super.onCreate(savedState);

        // Retrieve the deck id passed in via arguments
        deckId = requireArguments().getInt(ARG_DECK_ID);

        // Obtain a ViewModel instance scoped to the parent activity so saved
        // cards will update shared UI components
        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle s) {
        // Inflate the layout for this bottom sheet
        View view = inf.inflate(R.layout.dialog_add_cards, c, false);

        // Locate the RecyclerView used to show editable card rows
        RecyclerView rv = view.findViewById(R.id.rv_card_inputs);

        // Display rows in a simple vertical list
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Create the adapter and provide the deck id so new cards know which
        // deck they belong to
        adapter = new CardInputAdapter(deckId);
        rv.setAdapter(adapter);

        // Save button commits any fully entered cards and then closes the dialog
        view.findViewById(R.id.button_save_cards)
                .setOnClickListener(v -> {
                    // Gather all cards with text on both sides
                    List<Card> toSave = adapter.getFilledCards();
                    for (Card card : toSave) {
                        // Insert the card through the ViewModel so it persists
                        cardViewModel.insert(card);
                    }
                    // Close the bottom sheet after saving
                    dismiss();
                });
        return view;
    }
}