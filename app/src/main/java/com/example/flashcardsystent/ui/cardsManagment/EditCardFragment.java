package com.example.flashcardsystent.ui.cardsManagment;

/**
 * Fragment used for editing an existing card. Comments have been added to
 * clarify how arguments are read, views are initialized and updates are saved.
 */

// Holds state for fragment recreation
import android.os.Bundle;
// Utility class for checking if strings are empty
import android.text.TextUtils;
// Converts XML layouts into View objects
import android.view.LayoutInflater;
// Basic building block for UI components
import android.view.View;
// Container view that holds children
import android.view.ViewGroup;
// Tappable button widget
import android.widget.Button;
// Text entry field widget
import android.widget.EditText;
// Short message popup
import android.widget.Toast;

// Indicates parameters that must not be null
import androidx.annotation.NonNull;
// Indicates parameters that may be null
import androidx.annotation.Nullable;
// Portion of UI within an activity
import androidx.fragment.app.Fragment;
// Factory for obtaining ViewModel instances
import androidx.lifecycle.ViewModelProvider;
// Simplifies fragment navigation
import androidx.navigation.Navigation;

// Resource identifiers used by the fragment
import com.example.flashcardsystent.R;
// Entity representing a flash card
import com.example.flashcardsystent.data.Card;
// ViewModel handling card database operations
import com.example.flashcardsystent.viewmodel.CardViewModel;

public class EditCardFragment extends Fragment {

    // ViewModel used to load and update the card
    private CardViewModel cardViewModel;
    // Text inputs for editing front and back
    private EditText inputFront, inputBack;
    // Save button
    Button btnSave;
    // Ids for the card and its deck
    int cardId;
    // Currently loaded card entity
    private Card currentCard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout file for editing a card
        return inflater.inflate(R.layout.fragment_edit_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Retrieve arguments specifying which card should be edited
        Bundle args = getArguments();
        if (args != null) {
            cardId = args.getInt("cardId", -1);
        }

        // Find views defined in the layout
        inputFront = view.findViewById(R.id.input_front);
        inputBack = view.findViewById(R.id.input_back);
        btnSave = view.findViewById(R.id.button_save_card);

        // Obtain the ViewModel and load the card from the database
        cardViewModel = new ViewModelProvider(this).get(CardViewModel.class);
        cardViewModel.getCardById(cardId)
                .observe(getViewLifecycleOwner(), card -> {
                    if (card == null) return;
                    currentCard = card;
                    inputFront.setText(card.front);
                    inputBack.setText(card.back);
                });

        // Validate input and save the updated card when the user taps the button
        btnSave.setOnClickListener(v -> {
            String newFront = inputFront.getText().toString().trim();
            String newBack = inputBack.getText().toString().trim();
            if (TextUtils.isEmpty(newFront) || TextUtils.isEmpty(newBack)) {
                Toast.makeText(requireContext(),
                        "Oba pola muszą być wypełnione",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            currentCard.front = newFront;
            currentCard.back = newBack;
            cardViewModel.update(currentCard);
            Toast.makeText(requireContext(),
                    "Fiszka zaktualizowana",
                    Toast.LENGTH_SHORT).show();
            Navigation.findNavController(v).navigateUp();
        });
    }
}