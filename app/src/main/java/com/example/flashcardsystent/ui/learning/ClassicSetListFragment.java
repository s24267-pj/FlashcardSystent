package com.example.flashcardsystent.ui.learning;

// Importujemy wszystkie potrzebne klasy
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.adapter.QuizSetAdapter;
import com.example.flashcardsystent.data.AppDatabase;
import com.example.flashcardsystent.data.Deck;
import com.example.flashcardsystent.data.DeckDao;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Fragment wyświetlający listę dostępnych zestawów fiszek do trybu klasycznego.
 * Kliknięcie w zestaw rozpoczyna sesję nauki z kartami.
 */
public class ClassicSetListFragment extends Fragment {

    // DAO służące do pobierania danych o zestawach z lokalnej bazy danych Room
    private DeckDao deckDao;

    /**
     * Metoda tworzy ("dmucha") widok layoutu XML i zwraca jego korzeń.
     * To tutaj ładowany jest plik fragment_classic_set_list.xml jako interfejs graficzny tego fragmentu.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Rozdmuchujemy layout XML na obiekt View
        return inflater.inflate(R.layout.fragment_classic_set_list, container, false);
    }

    /**
     * Metoda wywoływana po utworzeniu widoku. Tutaj konfigurujemy RecyclerView i pobieramy dane z bazy.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Znajdujemy RecyclerView (lista przewijana) w layoucie
        RecyclerView recycler = view.findViewById(R.id.recycler_classic_sets);

        // Ustawiamy sposób układania elementów w RecyclerView — w pionie, jeden pod drugim
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Pobieramy DAO (interfejs dostępu do bazy) z singletona AppDatabase
        deckDao = AppDatabase.getInstance(requireContext()).deckDao();

        // Uruchamiamy zadanie w osobnym wątku, by nie blokować głównego wątku aplikacji
        Executors.newSingleThreadExecutor().execute(() -> {
            // Pobieramy wszystkie zestawy fiszek z bazy danych
            List<Deck> decks = deckDao.getAll();

            // Po zakończeniu operacji na tle, wracamy na główny wątek, by zmodyfikować UI
            requireActivity().runOnUiThread(() -> {
                // Tworzymy adapter i przekazujemy listę zestawów oraz akcję po kliknięciu
                QuizSetAdapter adapter = new QuizSetAdapter(decks, deck -> {
                    // Tworzymy Bundle (dodatkowe dane do przekazania między fragmentami)
                    Bundle bundle = new Bundle();
                    bundle.putInt("deckId", deck.id); // Przekazujemy ID wybranego zestawu

                    // Przechodzimy do fragmentu nauki (ClassicFragment), przekazując bundle
                    Navigation.findNavController(view).navigate(R.id.learningFragment, bundle);
                });

                // Ustawiamy adapter w RecyclerView, co sprawia, że lista zaczyna działać i wyświetla dane
                recycler.setAdapter(adapter);
            });
        });
    }
}
