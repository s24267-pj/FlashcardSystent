// Pakiet, w którym znajduje się ten fragment – dotyczy zarządzania zestawami fiszek
package com.example.flashcardsystent.ui.cardsManagment;

// Importujemy potrzebne klasy z Androida i AndroidX
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
 * Fragment umożliwiający użytkownikowi stworzenie nowego zestawu fiszek.
 * Zawiera pole tekstowe do wpisania nazwy oraz przycisk zapisujący nowy zestaw do bazy danych.
 */
public class AddDeckFragment extends Fragment {

    /** ViewModel służący do zapisu zestawu do bazy danych */
    private DeckViewModel deckViewModel;

    /**
     * Tworzy i zwraca widok fragmentu z polem tekstowym i przyciskiem zapisu.
     *
     * @param inflater obiekt LayoutInflater służący do załadowania layoutu XML
     * @param container kontener nadrzędny (np. FrameLayout)
     * @param savedInstanceState zapisany stan (jeśli istniał wcześniej)
     * @return korzeń widoku fragmentu
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Wczytujemy layout XML dla fragmentu (fragment_add_deck.xml)
        View root = inflater.inflate(R.layout.fragment_add_deck, container, false);

        // Pobieramy referencję do pola tekstowego, gdzie użytkownik wpisuje nazwę zestawu
        EditText nameInput = root.findViewById(R.id.input_deck_name);

        // Pobieramy referencję do przycisku zapisu
        Button saveButton = root.findViewById(R.id.button_save_deck);

        // Inicjalizujemy ViewModel dla tego fragmentu (do obsługi bazy danych Room)
        deckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);

        // Ustawiamy akcję wykonywaną po kliknięciu przycisku "Zapisz"
        saveButton.setOnClickListener(v -> {
            // Odczytujemy wpisaną nazwę i usuwamy zbędne spacje
            String name = nameInput.getText().toString().trim();

            // Sprawdzamy, czy pole nie jest puste
            if (TextUtils.isEmpty(name)) {
                // Jeśli jest puste, pokazujemy krótką wiadomość użytkownikowi
                Toast.makeText(requireContext(), R.string.enter_deck_name, Toast.LENGTH_SHORT).show();
            } else {
                // Jeśli nie jest puste — tworzymy nowy obiekt Deck i zapisujemy go do bazy
                deckViewModel.insert(new Deck(name));

                // Wychodzimy z aktualnego ekranu, cofając się w stosie nawigacji
                Navigation.findNavController(v).popBackStack();
            }
        });

        // Zwracamy cały widok, który zostanie wyświetlony na ekranie
        return root;
    }
}
