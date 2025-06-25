package com.example.flashcardsystent.ui.cardsManagment;

/**
 * Fragment that lets the user create a new deck. Inline comments explain how
 * the UI is wired up and how the deck is inserted into the database.
 */

// Object carrying saved state information for a fragment
import android.os.Bundle;
// Utilities for validating user input
import android.text.TextUtils;
// Used to inflate the layout resource into a View hierarchy
import android.view.LayoutInflater;
// Base class for widgets displayed on screen
import android.view.View;
// Container for other views
import android.view.ViewGroup;
// Pressable component for user actions
import android.widget.Button;
// Widget for text entry
import android.widget.EditText;
// Small popup message shown to the user
import android.widget.Toast;

// Annotation that a parameter must not be null
import androidx.annotation.NonNull;
// Annotation that a parameter may be null
import androidx.annotation.Nullable;
// Reusable portion of user interface
import androidx.fragment.app.Fragment;
// Helper to create ViewModel instances
import androidx.lifecycle.ViewModelProvider;
// Helper for navigating between fragments
import androidx.navigation.Navigation;

// Access to resource identifiers
import com.example.flashcardsystent.R;
// Entity representing a deck of cards
import com.example.flashcardsystent.data.Deck;
// ViewModel used for deck database operations
import com.example.flashcardsystent.viewmodel.DeckViewModel;

public class AddDeckFragment extends Fragment {

    // Shared ViewModel for performing database operations
    private DeckViewModel deckViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the XML layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_deck, container, false);

        // Grab references to UI elements
        EditText nameInput = root.findViewById(R.id.input_deck_name);
        Button saveButton = root.findViewById(R.id.button_save_deck);

        // Obtain the ViewModel instance for inserting decks
        deckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);

        // When the user taps save, validate and insert the new deck
        saveButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(requireContext(), R.string.enter_deck_name, Toast.LENGTH_SHORT).show();
            } else {
                deckViewModel.insert(new Deck(name));
                Navigation.findNavController(v).popBackStack();
            }
        });

        return root; // Return the fully configured view
    }
}