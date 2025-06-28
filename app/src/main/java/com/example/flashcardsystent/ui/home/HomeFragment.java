package com.example.flashcardsystent.ui.home;

// Importujemy klasy potrzebne do działania fragmentu
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.databinding.FragmentHomeBinding;
import com.example.flashcardsystent.viewmodel.DeckViewModel;

/**
 * Fragment reprezentujący ekran główny aplikacji.
 * Użytkownik może stąd przejść do jednego z trzech trybów nauki: klasycznego, quizowego lub przeglądania fiszek.
 */
public class HomeFragment extends Fragment {

    // Deklaracja zmiennej binding — to specjalny obiekt, który pozwala nam odwoływać się do widoków z layoutu XML bez używania findViewById
    private FragmentHomeBinding binding;

    // ViewModel to komponent architektury Androida, który przechowuje dane UI (w tym przypadku listę zestawów fiszek)
    private DeckViewModel deckViewModel;

    // Metoda onCreateView() tworzy widok fragmentu. To tutaj "dmuchamy" layout XML do obiektów Java
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Tworzymy obiekt bindingu na podstawie layoutu fragment_home.xml
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // Zwracamy główny widok tego fragmentu, czyli korzeń layoutu z XML
        return binding.getRoot();
    }

    // Metoda wywoływana tuż po utworzeniu widoku. Ustawiamy tutaj kliknięcia przycisków oraz ViewModel
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Tworzymy ViewModel, który ma dostęp do bazy danych z zestawami fiszek. Używamy requireActivity(), bo ViewModel ma działać między fragmentami
        deckViewModel = new ViewModelProvider(requireActivity()).get(DeckViewModel.class);

        // Obsługa kliknięcia przycisku "Tryb klasyczny"
        binding.buttonModeClassic.setOnClickListener(v ->
                // Pobieramy wszystkie zestawy fiszek z ViewModelu i obserwujemy wynik
                deckViewModel.getAllDecks().observe(getViewLifecycleOwner(), decks -> {
                    // Jeśli lista jest pusta lub null, pokazujemy komunikat Toast (krótkie powiadomienie na dole ekranu)
                    if (decks == null || decks.isEmpty()) {
                        Toast.makeText(getContext(), R.string.no_flashcard_sets, Toast.LENGTH_SHORT).show();
                    } else {
                        // Jeśli są jakieś zestawy, przechodzimy do ekranu wyboru zestawu dla trybu klasycznego
                        Navigation.findNavController(v).navigate(R.id.classicSetListFragment);
                    }
                })
        );

        // Obsługa kliknięcia przycisku "Tryb quizowy"
        binding.buttonQuizMode.setOnClickListener(v ->
                deckViewModel.getAllDecks().observe(getViewLifecycleOwner(), decks -> {
                    if (decks == null || decks.isEmpty()) {
                        Toast.makeText(getContext(), R.string.no_flashcard_sets, Toast.LENGTH_SHORT).show();
                    } else {
                        Navigation.findNavController(v).navigate(R.id.quizSetListFragment);
                    }
                })
        );

        // Obsługa kliknięcia przycisku "Przeglądanie fiszek"
        binding.buttonBrowseMode.setOnClickListener(v ->
                deckViewModel.getAllDecks().observe(getViewLifecycleOwner(), decks -> {
                    if (decks == null || decks.isEmpty()) {
                        Toast.makeText(getContext(), R.string.no_flashcard_sets, Toast.LENGTH_SHORT).show();
                    } else {
                        Navigation.findNavController(v).navigate(R.id.browseSetListFragment);
                    }
                })
        );
    }

    // Metoda onDestroyView() jest wywoływana, gdy fragment jest niszczony i nie potrzebuje już swojego widoku
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Zerujemy binding, żeby uniknąć wycieków pamięci (gdyby Android trzymał niepotrzebne referencje do widoków)
        binding = null;
    }
}
