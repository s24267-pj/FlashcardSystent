package com.example.flashcardsystent.ui.cardsManagment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.data.Deck;
import com.example.flashcardsystent.viewmodel.DeckViewModel;

/**
 * Fragment that allows the user to create a new flashcard deck.
 * Provides a text input and a save button that stores the new deck in the database.
 */
public class AddDeckFragment extends Fragment {

    /** ViewModel used to insert the deck into the database */
    private DeckViewModel deckViewModel;

    /**
     * Inflates the layout containing the input field and save button.
     *
     * @param inflater LayoutInflater used to inflate views
     * @param container Optional parent view
     * @param savedInstanceState Previous saved state
     * @return root view of the fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_deck, container, false);

        EditText nameInput = root.findViewById(R.id.input_deck_name);
        Button saveButton = root.findViewById(R.id.button_save_deck);

        deckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);

        saveButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(requireContext(), R.string.enter_deck_name, Toast.LENGTH_SHORT).show();
            } else {
                deckViewModel.insert(new Deck(name));
                Navigation.findNavController(v).popBackStack();
            }
        });

        return root;
    }
}
