// Pakiet fragmentów związanych z zarządzaniem fiszkami
package com.example.flashcardsystent.ui.cardsManagment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.adapter.CardAdapter;
import com.example.flashcardsystent.data.Card;
import com.example.flashcardsystent.viewmodel.CardViewModel;

/**
 * Fragment wyświetlający wszystkie fiszki należące do konkretnego zestawu (decka).
 * Pozwala na przesunięcie fiszki w prawo, aby ją usunąć, oraz kliknięcie, aby edytować.
 */
public class DeckDetailFragment extends Fragment {

    /** ViewModel do zarządzania fiszkami */
    private CardViewModel cardViewModel;

    /** Adapter RecyclerView do wyświetlania listy fiszek */
    private CardAdapter cardAdapter;

    /** ID aktualnie przeglądanego zestawu */
    private int deckId;

    /**
     * Metoda wywoływana do utworzenia drzewa widoków dla fragmentu.
     *
     * @param inflater obiekt do "nadmuchania" layoutu XML
     * @param container kontener nadrzędny (np. FrameLayout)
     * @param savedInstanceState poprzedni stan (jeśli istniał)
     * @return korzeń widoku fragmentu
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deck_detail, container, false);
    }

    /**
     * Metoda wywoływana po utworzeniu widoku – inicjalizuje RecyclerView,
     * ViewModel, adapter, przeciąganie (swipe) i przycisk dodawania fiszek.
     *
     * @param view główny widok fragmentu
     * @param savedInstanceState poprzedni stan, jeśli istniał
     */
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Pobieramy argumenty przekazane do fragmentu (ID i nazwa zestawu)
        Bundle args = getArguments();
        if (args != null) {
            deckId = args.getInt("deckId"); // ID zestawu
            String deckName = args.getString("deckName", ""); // nazwa zestawu
            // Ustawiamy nazwę zestawu w widoku tekstowym
            ((TextView) view.findViewById(R.id.text_deck_name)).setText(deckName);
        }

        // Inicjalizacja ViewModelu dla fiszek
        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);

        // Inicjalizacja RecyclerView i jego układu
        RecyclerView rv = view.findViewById(R.id.rv_cards);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Inicjalizacja adaptera fiszek z obsługą kliknięcia (do edycji)
        cardAdapter = new CardAdapter(card -> {
            Bundle bundle = new Bundle();
            bundle.putInt("cardId", card.id);     // ID fiszki
            bundle.putInt("deckId", deckId);       // ID zestawu
            // Nawigacja do ekranu edycji fiszki
            Navigation.findNavController(view)
                    .navigate(R.id.action_deckDetailFragment_to_editCardFragment, bundle);
        });
        rv.setAdapter(cardAdapter);

        // Obserwujemy listę fiszek dla danego zestawu i aktualizujemy adapter
        cardViewModel.getCardsByDeck(deckId)
                .observe(getViewLifecycleOwner(), cardAdapter::submitList);

        // Logika przeciągnięcia fiszki w prawo (usuń)
        ItemTouchHelper.SimpleCallback swipeCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView rv,
                                          @NonNull RecyclerView.ViewHolder vh,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false; // przeciąganie pozycji nieobsługiwane
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder vh, int direction) {
                        int pos = vh.getAdapterPosition(); // pozycja przesuniętej fiszki
                        Card toDelete = cardAdapter.getItemAt(pos); // pobranie obiektu fiszki
                        cardViewModel.delete(toDelete); // usunięcie z bazy danych
                        Toast.makeText(requireContext(),
                                R.string.card_deleted,
                                Toast.LENGTH_SHORT).show(); // komunikat toast
                    }
                };

        // Przypisanie logiki swipe do RecyclerView
        new ItemTouchHelper(swipeCallback).attachToRecyclerView(rv);

        // Obsługa przycisku „Dodaj fiszki” – otwiera dolny panel
        view.findViewById(R.id.button_add_cards)
                .setOnClickListener(v ->
                        AddCardsBottomSheet
                                .newInstance(deckId)
                                .show(getChildFragmentManager(), "ADD_CARDS"));
    }
}
