// Pakiet odpowiadający za ekran edycji pojedynczej fiszki
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
 * Fragment umożliwiający edycję istniejącej fiszki.
 * Użytkownik może zmienić tekst z przodu i z tyłu karty i zapisać zmiany do bazy danych.
 */
public class EditCardFragment extends Fragment {

    /** ViewModel do uzyskiwania dostępu i aktualizacji danych karty */
    private CardViewModel cardViewModel;

    /** Pola tekstowe do edycji przodu i tyłu fiszki */
    private EditText inputFront, inputBack;

    /** Przycisk zapisujący zmiany */
    private Button btnSave;

    /** ID edytowanej fiszki (przekazywane jako argument) */
    private int cardId;

    /** Obiekt aktualnie załadowanej fiszki */
    private Card currentCard;

    /**
     * Tworzy i zwraca widok layoutu fragmentu.
     *
     * @param inflater obiekt do "nadmuchania" layoutu XML
     * @param container kontener rodzicielski
     * @param savedInstanceState poprzedni stan, jeśli istnieje
     * @return widok główny fragmentu
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_card, container, false);
    }

    /**
     * Metoda wywoływana po utworzeniu widoku — inicjalizuje formularz edycji
     * i ustawia logikę zapisu zmian.
     *
     * @param view główny widok fragmentu
     * @param savedInstanceState poprzedni stan, jeśli istnieje
     */
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Pobranie ID fiszki przekazanej jako argument
        Bundle args = getArguments();
        if (args != null) {
            cardId = args.getInt("cardId", -1);
        }

        // Powiązanie widoków z layoutu
        inputFront = view.findViewById(R.id.input_front);
        inputBack = view.findViewById(R.id.input_back);
        btnSave = view.findViewById(R.id.button_save_card);

        // Inicjalizacja ViewModelu
        cardViewModel = new ViewModelProvider(this).get(CardViewModel.class);

        // Obserwacja fiszki po jej ID — załadowanie jej do formularza
        cardViewModel.getCardById(cardId)
                .observe(getViewLifecycleOwner(), card -> {
                    if (card == null) return;
                    currentCard = card;
                    inputFront.setText(card.front);
                    inputBack.setText(card.back);
                });

        // Obsługa kliknięcia przycisku Zapisz
        btnSave.setOnClickListener(v -> {
            String newFront = inputFront.getText().toString().trim();
            String newBack = inputBack.getText().toString().trim();

            // Walidacja: oba pola muszą być wypełnione
            if (TextUtils.isEmpty(newFront) || TextUtils.isEmpty(newBack)) {
                Toast.makeText(requireContext(),
                        R.string.both_fields_required,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Zaktualizowanie pól w obiekcie fiszki i zapis do bazy
            currentCard.front = newFront;
            currentCard.back = newBack;
            cardViewModel.update(currentCard);

            // Powiadomienie i cofnięcie nawigacji
            Toast.makeText(requireContext(),
                    R.string.card_updated,
                    Toast.LENGTH_SHORT).show();
            Navigation.findNavController(v).navigateUp();
        });
    }
}
