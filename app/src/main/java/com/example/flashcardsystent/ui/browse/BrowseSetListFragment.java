// Pakiet zawierający ten fragment – odpowiada za listę zestawów do przeglądania
package com.example.flashcardsystent.ui.browse;

// Importujemy wszystkie potrzebne klasy do obsługi fragmentu i widoków
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.viewmodel.DeckViewModel;
import com.example.flashcardsystent.adapter.BrowseSetAdapter;

/**
 * Fragment wyświetlający listę wszystkich dostępnych zestawów fiszek (decków) w trybie przeglądania.
 * Użytkownik może kliknąć zestaw, aby zobaczyć jego zawartość (czyli przejść do widoku przeglądania fiszek).
 */
public class BrowseSetListFragment extends Fragment {

    /** ViewModel dostarczający listę dostępnych zestawów (decków) z bazy danych */
    private DeckViewModel deckViewModel;

    /** RecyclerView, w którym będą wyświetlane zestawy fiszek */
    private RecyclerView recyclerView;

    /**
     * Tworzy widok fragmentu na podstawie pliku XML (fragment_browse_set_list.xml).
     * Wywoływana automatycznie przez Androida, gdy fragment ma być wyświetlony.
     *
     * @param inflater obiekt odpowiedzialny za przekształcenie XML na widoki Java
     * @param container opcjonalny kontener (rodzic) widoku
     * @param savedInstanceState stan fragmentu, jeśli był wcześniej zapisany
     * @return wygenerowany widok główny fragmentu
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Ładujemy layout fragmentu z pliku XML
        return inflater.inflate(R.layout.fragment_browse_set_list, container, false);
    }

    /**
     * Wywoływana po utworzeniu widoku. Tu ustawiamy RecyclerView i obserwujemy dane.
     *
     * @param view główny widok fragmentu (wczytany wcześniej w onCreateView)
     * @param savedInstanceState zapisany stan (jeśli dotyczy)
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Pobieramy referencję do RecyclerView z layoutu XML
        recyclerView = view.findViewById(R.id.browse_set_list);

        // Ustawiamy sposób wyświetlania – LinearLayoutManager oznacza listę pionową
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Tworzymy ViewModel, który zapewnia dostęp do danych o zestawach fiszek (Deck)
        deckViewModel = new ViewModelProvider(requireActivity()).get(DeckViewModel.class);

        // Obserwujemy listę wszystkich zestawów (LiveData) – fragment automatycznie zaktualizuje widok gdy dane się zmienią
        deckViewModel.getAllDecks().observe(getViewLifecycleOwner(), decks -> {
            // Tworzymy adapter do RecyclerView, przekazując listę zestawów i callback do obsługi kliknięcia
            BrowseSetAdapter adapter = new BrowseSetAdapter(decks, deck -> {
                // Po kliknięciu zestawu tworzymy "pakiet danych" (Bundle), w którym przekazujemy ID wybranego zestawu
                Bundle bundle = new Bundle();
                bundle.putInt("deckId", deck.id);

                // Przechodzimy do fragmentu przeglądania fiszek danego zestawu, przekazując bundle jako argument
                Navigation.findNavController(view)
                        .navigate(R.id.action_browseSetListFragment_to_browseCardsFragment, bundle);
            });

            // Przypisujemy adapter do RecyclerView
            recyclerView.setAdapter(adapter);
        });
    }
}
