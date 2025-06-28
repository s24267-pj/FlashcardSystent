// Pakiet, w którym znajduje się ten fragment — dotyczy zarządzania fiszkami
package com.example.flashcardsystent.ui.cardsManagment;

// Importy potrzebnych klas Androida i komponentów AndroidX
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
 * Fragment wyświetlany jako tzw. "bottom sheet" (dolny panel),
 * pozwalający użytkownikowi dodawać wiele fiszek naraz.
 * Każda fiszka to osobna edytowalna linia w przewijalnej liście.
 */
public class AddCardsBottomSheet extends BottomSheetDialogFragment {

    /** Klucz używany do przekazywania ID zestawu jako argument do fragmentu */
    private static final String ARG_DECK_ID = "deckId";

    /** ID zestawu, do którego zostaną przypisane nowe fiszki */
    private int deckId;

    /** Współdzielony ViewModel do zapisu danych do bazy */
    private CardViewModel cardViewModel;

    /** Adapter, który obsługuje listę edytowalnych fiszek */
    private CardInputAdapter adapter;

    /**
     * Fabryczna metoda statyczna tworząca nową instancję fragmentu dla danego zestawu fiszek.
     *
     * @param deckId ID zestawu fiszek, do którego mają zostać dodane nowe karty
     * @return skonfigurowany fragment `AddCardsBottomSheet`
     */
    public static AddCardsBottomSheet newInstance(int deckId) {
        Bundle args = new Bundle();                  // Tworzymy paczkę argumentów
        args.putInt(ARG_DECK_ID, deckId);            // Wkładamy ID zestawu

        AddCardsBottomSheet fragment = new AddCardsBottomSheet();
        fragment.setArguments(args);                 // Przypisujemy argumenty do fragmentu
        return fragment;
    }

    /**
     * Metoda wywoływana przy tworzeniu fragmentu.
     * Pobiera argumenty i inicjalizuje ViewModel.
     *
     * @param savedState zapisany stan (jeśli istniał)
     */
    @Override
    public void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        deckId = requireArguments().getInt(ARG_DECK_ID); // Pobieramy ID zestawu z argumentów
        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
    }

    /**
     * Tworzy widok fragmentu — ładuje layout i ustawia listę fiszek do edycji.
     *
     * @param inf obiekt LayoutInflater służący do "nadmuchania" layoutu XML
     * @param c kontener (rodzic), czyli widok nadrzędny
     * @param s zapisany stan (jeśli istniał)
     * @return widok, który zostanie wyświetlony jako bottom sheet
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle s) {
        // Wczytujemy layout z pliku XML: dialog_add_cards.xml
        View view = inf.inflate(R.layout.dialog_add_cards, c, false);

        // Ustawiamy RecyclerView — lista edytowalnych fiszek
        RecyclerView rv = view.findViewById(R.id.rv_card_inputs);
        rv.setLayoutManager(new LinearLayoutManager(requireContext())); // Ustawiamy liniowy układ

        // Tworzymy adapter z domyślnymi pustymi polami fiszek
        adapter = new CardInputAdapter(deckId);
        rv.setAdapter(adapter); // Przypisujemy adapter do RecyclerView

        // Obsługa przycisku "Zapisz" — zapisuje wypełnione fiszki do bazy danych
        view.findViewById(R.id.button_save_cards).setOnClickListener(v -> {
            List<Card> toSave = adapter.getFilledCards(); // Pobieramy tylko uzupełnione karty
            for (Card card : toSave) {
                cardViewModel.insert(card); // Wstawiamy każdą fiszkę do bazy przez ViewModel
            }
            dismiss(); // Zamykamy bottom sheet
        });

        return view; // Zwracamy gotowy widok do wyświetlenia
    }
}
