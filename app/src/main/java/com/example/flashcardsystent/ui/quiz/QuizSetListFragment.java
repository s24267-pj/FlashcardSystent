package com.example.flashcardsystent.ui.quiz;

// Import klas Androida odpowiedzialnych za widok i cykl życia fragmentu
import android.os.Bundle; // Umożliwia przekazywanie danych między komponentami (np. fragmentami)
import android.view.LayoutInflater; // Umożliwia "nadmuchiwanie" widoku z XML-a do postaci obiektu View
import android.view.View; // Klasa bazowa dla wszystkich komponentów UI w Androidzie
import android.view.ViewGroup; // Kontener do przechowywania widoków w układzie

// Import klas Jetpack (AndroidX) związanych z fragmentami i nawigacją
import androidx.annotation.NonNull; // Adnotacja wskazująca, że argument/metoda nie może być null
import androidx.fragment.app.Fragment; // Bazowa klasa dla fragmentu ekranu
import androidx.navigation.Navigation; // Umożliwia nawigację pomiędzy fragmentami
import androidx.recyclerview.widget.LinearLayoutManager; // LayoutManager do RecyclerView - układ pionowy listy

// Import własnych klas aplikacji
import com.example.flashcardsystent.R; // Referencje do zasobów XML (layouty, napisy, kolory itp.)
import com.example.flashcardsystent.adapter.QuizSetAdapter; // Adapter do wyświetlania listy zestawów w trybie quizu
import com.example.flashcardsystent.data.AppDatabase; // Singleton bazy danych aplikacji
import com.example.flashcardsystent.data.Deck; // Klasa modelu reprezentująca zestaw fiszek
import com.example.flashcardsystent.data.DeckDao; // DAO do obsługi tabeli Deck w Room
import com.example.flashcardsystent.databinding.FragmentQuizSetListBinding; // ViewBinding do tego fragmentu

import java.util.List; // Lista kart
import java.util.concurrent.Executors; // Do uruchamiania zadań w tle (np. zapytań do bazy)

/**
 * Fragment wyświetlający listę dostępnych zestawów fiszek do trybu quizu.
 * Kliknięcie w zestaw rozpoczyna quiz wielokrotnego wyboru.
 */
public class QuizSetListFragment extends Fragment {

    // Obiekt ViewBinding zapewniający dostęp do widoków z layoutu fragmentu
    private FragmentQuizSetListBinding binding;

    // DAO do odczytu danych o zestawach z bazy danych
    private DeckDao deckDao;

    /**
     * Tworzy i zwraca drzewo widoków fragmentu oraz rozpoczyna wczytywanie danych z bazy.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Tworzymy ViewBinding - uzyskujemy dostęp do wszystkich widoków z XML-a
        binding = FragmentQuizSetListBinding.inflate(inflater, container, false);

        // Pobieramy DAO z instancji bazy danych
        AppDatabase db = AppDatabase.getInstance(requireContext());
        deckDao = db.deckDao();

        // Wczytujemy zestawy fiszek i wyświetlamy je
        loadDecks();

        // Zwracamy korzeń widoku tego fragmentu
        return binding.getRoot();
    }

    /**
     * Wczytuje zestawy z bazy w tle i aktualizuje RecyclerView na głównym wątku.
     */
    private void loadDecks() {
        // Tworzymy osobny wątek do odczytu z bazy (nie można tego robić w głównym wątku)
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Deck> decks = deckDao.getAll(); // Pobieramy wszystkie zestawy

            // Aktualizujemy UI (RecyclerView) na głównym wątku
            requireActivity().runOnUiThread(() -> {
                QuizSetAdapter adapter = new QuizSetAdapter(decks, deck -> {
                    // Po kliknięciu zestawu przechodzimy do quizu
                    Bundle bundle = new Bundle();
                    bundle.putInt("setId", deck.id);
                    Navigation.findNavController(binding.getRoot())
                            .navigate(R.id.action_quizSetListFragment_to_quizFragment, bundle);
                });

                // Ustawiamy układ listy (RecyclerView) i adapter
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                binding.recyclerView.setAdapter(adapter);
            });
        });
    }
}
