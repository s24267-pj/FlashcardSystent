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
import com.example.flashcardsystent.data.Card;
import com.example.flashcardsystent.viewmodel.CardViewModel;

/**
 * Fragment for editing an existing flashcard.
 * Allows the user to update the front and back text and save changes to the database.
 */
public class EditCardFragment extends Fragment {

    /** ViewModel for accessing and updating card data */
    private CardViewModel cardViewModel;

    /** Input fields for editing front and back of the card */
    private EditText inputFront, inputBack;

    /** Button that triggers the save operation */
    private Button btnSave;

    /** ID of the card being edited */
    private int cardId;

    /** Currently loaded card */
    private Card currentCard;

    /**
     * Inflates the layout containing input fields and save button.
     *
     * @param inflater LayoutInflater to inflate views
     * @param container Optional parent view
     * @param savedInstanceState Previously saved state
     * @return root view of the fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_card, container, false);
    }

    /**
     * Initializes the form with the card’s current data and handles save logic.
     *
     * @param view the fragment’s root view
     * @param savedInstanceState previously saved state, if any
     */
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            cardId = args.getInt("cardId", -1);
        }

        inputFront = view.findViewById(R.id.input_front);
        inputBack = view.findViewById(R.id.input_back);
        btnSave = view.findViewById(R.id.button_save_card);

        cardViewModel = new ViewModelProvider(this).get(CardViewModel.class);
        cardViewModel.getCardById(cardId)
                .observe(getViewLifecycleOwner(), card -> {
                    if (card == null) return;
                    currentCard = card;
                    inputFront.setText(card.front);
                    inputBack.setText(card.back);
                });

        btnSave.setOnClickListener(v -> {
            String newFront = inputFront.getText().toString().trim();
            String newBack = inputBack.getText().toString().trim();
            if (TextUtils.isEmpty(newFront) || TextUtils.isEmpty(newBack)) {
                Toast.makeText(requireContext(),
                        R.string.both_fields_required,
                        Toast.LENGTH_SHORT).show();
                return;
            }
            currentCard.front = newFront;
            currentCard.back = newBack;
            cardViewModel.update(currentCard);
            Toast.makeText(requireContext(),
                    R.string.card_updated,
                    Toast.LENGTH_SHORT).show();
            Navigation.findNavController(v).navigateUp();
        });
    }
}
