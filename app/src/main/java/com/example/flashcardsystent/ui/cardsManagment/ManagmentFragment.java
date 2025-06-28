// Pakiet zawierający fragmenty do zarządzania zestawami fiszek
package com.example.flashcardsystent.ui.cardsManagment;

// Importy klas potrzebnych do działania fragmentu
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.adapter.DeckAdapter;
import com.example.flashcardsystent.data.Deck;
import com.example.flashcardsystent.viewmodel.DeckViewModel;

/**
 * Fragment wyświetlający wszystkie zestawy fiszek.
 * Umożliwia kliknięcie, długie kliknięcie (zmiana nazwy/usunięcie),
 * oraz dodawanie nowych zestawów.
 */
public class ManagmentFragment extends Fragment {

    // ViewModel do zarządzania danymi zestawów (Deck) — pośrednik między bazą danych a UI
    private DeckViewModel deckViewModel;

    // Adapter RecyclerView do wyświetlania listy zestawów fiszek
    private DeckAdapter adapter;

    /**
     * Metoda tworzy i zwraca widok dla tego fragmentu (interfejs użytkownika)
     * @param inflater obiekt używany do przekształcenia XML w obiekty View
     * @param container kontener, w którym osadzany jest fragment
     * @param savedInstanceState stan zapisany (jeśli wcześniej istniał)
     * @return gotowy widok do wyświetlenia
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Tworzenie widoku na podstawie layoutu XML fragment_cards_managment.xml
        View root = inflater.inflate(R.layout.fragment_cards_managment, container, false);

        // Inicjalizacja ViewModelu — uzyskujemy dostęp do danych zestawów
        deckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);

        // Pobieramy RecyclerView z layoutu (lista zestawów)
        RecyclerView recyclerView = root.findViewById(R.id.deck_list);

        // Tworzymy adapter i definiujemy co się stanie po kliknięciu/długim kliknięciu zestawu
        adapter = new DeckAdapter(new DeckAdapter.OnDeckClickListener() {

            // Kliknięcie zestawu — przejście do szczegółów zestawu (lista fiszek)
            @Override
            public void onDeckClick(Deck deck) {
                // Tworzymy paczkę danych do przekazania do kolejnego fragmentu
                Bundle bundle = new Bundle();
                bundle.putInt("deckId", deck.id); // przekazujemy ID zestawu
                bundle.putString("deckName", deck.name); // przekazujemy nazwę zestawu

                // Przechodzimy do fragmentu szczegółów zestawu
                Navigation.findNavController(requireView())
                        .navigate(R.id.action_navigation_dashboard_to_deckDetailFragment, bundle);
            }

            // Długie kliknięcie zestawu — pokaż dialog z opcją zmiany nazwy lub usunięcia
            @Override
            public void onDeckLongClick(Deck deck) {
                new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        .setTitle(deck.name) // tytuł dialogu to nazwa zestawu
                        .setItems(new CharSequence[]{
                                getString(R.string.rename), // opcja zmiany nazwy
                                getString(R.string.delete_deck) // opcja usunięcia
                        }, (dialog, which) -> {
                            if (which == 0) {
                                // Jeśli użytkownik wybrał „Zmień nazwę”
                                showRenameDialog(deck);
                            } else {
                                // Jeśli wybrano „Usuń”, pytamy o potwierdzenie
                                new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                                        .setTitle(getString(R.string.delete_deck))
                                        .setMessage(getString(R.string.confirm_delete_deck, deck.name))
                                        .setPositiveButton(R.string.yes, (d, w) -> {
                                            deckViewModel.delete(deck); // usuwamy z bazy
                                            Toast.makeText(requireContext(),
                                                    R.string.deck_deleted,
                                                    Toast.LENGTH_SHORT).show();
                                        })
                                        .setNegativeButton(R.string.no, null)
                                        .show();
                            }
                        })
                        .show();
            }
        });

        // Ustawiamy adapter do RecyclerView — teraz będzie wyświetlał listę zestawów
        recyclerView.setAdapter(adapter);

        // Obserwujemy dane z ViewModelu — aktualizujemy adapter gdy dane się zmienią
        deckViewModel.getAllDecks().observe(getViewLifecycleOwner(), adapter::submitList);

        // Obsługa kliknięcia przycisku dodania nowego zestawu fiszek
        root.findViewById(R.id.button_add_deck)
                .setOnClickListener(v ->
                        Navigation.findNavController(v)
                                .navigate(R.id.action_navigation_dashboard_to_addDeckFragment)
                );

        return root;
    }

    /**
     * Pokazuje dialog umożliwiający zmianę nazwy zestawu
     * @param deck obiekt zestawu do zmiany nazwy
     */
    private void showRenameDialog(Deck deck) {
        // Tworzymy pole tekstowe do wpisania nowej nazwy
        EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT); // typ tekstowy
        input.setText(deck.name); // ustawiamy aktualną nazwę jako domyślną

        // Tworzymy i pokazujemy dialog
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle(R.string.rename_deck) // tytuł dialogu
                .setView(input) // dodajemy pole tekstowe
                .setPositiveButton(R.string.save, (dialog, which) -> {
                    // Po kliknięciu „Zapisz” — sprawdzamy czy nie jest puste
                    String newName = input.getText().toString().trim();
                    if (newName.isEmpty()) {
                        Toast.makeText(getContext(), R.string.name_cannot_be_empty, Toast.LENGTH_SHORT).show();
                    } else {
                        // Ustawiamy nową nazwę i aktualizujemy bazę danych
                        deck.name = newName;
                        deckViewModel.update(deck);
                        Toast.makeText(requireContext(),
                                getString(R.string.deck_renamed_to, newName),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, null) // przycisk „Anuluj”
                .show();
    }
}
