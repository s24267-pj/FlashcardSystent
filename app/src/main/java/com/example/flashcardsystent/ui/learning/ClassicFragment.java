package com.example.flashcardsystent.ui.learning;

// Importujemy klasy potrzebne do działania fragmentu
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.data.Card;
import com.example.flashcardsystent.viewmodel.ClassicViewModel;

/**
 * Fragment obsługujący klasyczny tryb nauki — użytkownik przegląda fiszki,
 * odsłania odpowiedzi i zaznacza, czy znał odpowiedź czy nie.
 */
public class ClassicFragment extends Fragment {

    // ViewModel zarządza logiką nauki, postępem użytkownika i statystykami
    private ClassicViewModel viewModel;

    // Tekstowe widoki pokazujące awers i rewers fiszki
    private TextView frontView;
    private TextView backView;

    // Główny kontener, który otacza fiszkę i reaguje na kliknięcia do przewracania
    private View cardContainer;

    // Zmienna określająca, czy aktualnie widzimy przód fiszki
    private boolean showingFront = true;

    // Metoda tworzy widok (layout) fragmentu na podstawie XML-a
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Ładujemy layout fragment_learning.xml i zwracamy jego główny widok
        return inflater.inflate(R.layout.fragment_learning, container, false);
    }

    // Metoda wywoływana po utworzeniu widoku — tutaj ustawiamy całą logikę
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Przypisujemy referencje do odpowiednich widoków na ekranie
        frontView = view.findViewById(R.id.text_card_front); // tekst przodu fiszki
        backView = view.findViewById(R.id.text_card_back);   // tekst tyłu fiszki
        cardContainer = view.findViewById(R.id.card_container); // klikany kontener fiszki
        Button buttonKnow = view.findViewById(R.id.button_know); // przycisk "znam"
        Button buttonDontKnow = view.findViewById(R.id.button_dont_know); // przycisk "nie znam"

        // Pobieramy ID zestawu fiszek przekazane jako argument do fragmentu
        int deckId = getArguments().getInt("deckId", -1);

        // Inicjalizujemy ViewModel (będzie pamiętał dane nawet po obrocie ekranu)
        viewModel = new ViewModelProvider(this).get(ClassicViewModel.class);

        // Ładujemy fiszki do nauki dla danego zestawu — obserwujemy ich zmiany
        viewModel.loadCards(deckId, getViewLifecycleOwner());

        // Obserwujemy aktualną fiszkę — kiedy się zmieni, uaktualniamy widok
        viewModel.getCurrentCard().observe(getViewLifecycleOwner(), card -> {
            if (card == null) { // Gdy nie ma już fiszek do pokazania
                if (viewModel.getTotalCount() == 0) {
                    // Jeśli zestaw był pusty, pokazujemy komunikat i cofamy do poprzedniego ekranu
                    Toast.makeText(requireContext(), R.string.no_flashcards_in_deck, Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view).navigateUp();
                } else {
                    // Jeśli zakończyliśmy naukę, przygotowujemy dane do podsumowania
                    Bundle bundle = new Bundle();
                    bundle.putInt("totalCount", viewModel.getTotalCount()); // liczba fiszek w sesji

                    Card hardest = viewModel.getMostDifficultCard(); // najtrudniejsza fiszka (najwięcej razy "nie znam")
                    if (hardest != null) {
                        bundle.putString("hardFront", hardest.front);
                        bundle.putString("hardBack", hardest.back);
                        bundle.putInt("hardCount", viewModel.getDontKnowCount(hardest.id));
                    }

                    // Obliczamy skuteczność (w %)
                    int totalClicks = viewModel.getKnowClicks() + viewModel.getDontKnowClicks();
                    int rate = totalClicks > 0 ? (int) Math.round(100.0 * viewModel.getKnowClicks() / totalClicks) : 0;
                    bundle.putInt("successRate", rate);

                    // Przechodzimy do ekranu podsumowania nauki
                    Navigation.findNavController(view)
                            .navigate(R.id.action_learningFragment_to_learningSummaryFragment, bundle);
                }
            } else {
                // Jeśli mamy fiszkę, wyświetlamy jej treść
                frontView.setText(card.front); // awers
                backView.setText(card.back);   // rewers

                // Ustawiamy rotację i widoczność widoków (pokazujemy przód)
                frontView.setRotationY(0);
                backView.setRotationY(0);
                frontView.setVisibility(View.VISIBLE);
                backView.setVisibility(View.GONE);
                showingFront = true;

                // Obsługa kliknięcia w kartę: przewraca ją
                cardContainer.setOnClickListener(v -> {
                    if (showingFront) {
                        flipCard(frontView, backView);
                    } else {
                        flipCard(backView, frontView);
                    }
                    showingFront = !showingFront; // przełączamy flagę
                });
            }
        });

        // Obsługa kliknięcia przycisku "znam" — informujemy ViewModel
        buttonKnow.setOnClickListener(v -> viewModel.onKnow());

        // Obsługa kliknięcia przycisku "nie znam" — informujemy ViewModel
        buttonDontKnow.setOnClickListener(v -> viewModel.onDontKnow());

        // Obserwujemy zakończenie nauki — gdy sesja się kończy, zapisujemy wynik
        viewModel.getFinished().observe(getViewLifecycleOwner(), finished -> {
            if (Boolean.TRUE.equals(finished)) {
                viewModel.saveResult(deckId); // zapisujemy statystyki w bazie
            }
        });
    }

    /**
     * Metoda animująca obrót karty (imitacja przewracania)
     * @param fromView aktualnie widoczna strona
     * @param toView strona, którą chcemy pokazać
     */
    private void flipCard(View fromView, View toView) {
        fromView.animate()
                .rotationY(90) // najpierw obrót do 90 stopni (strona "znika")
                .setDuration(200)
                .withEndAction(() -> {
                    // po zakończeniu animacji chowamy aktualny widok
                    fromView.setVisibility(View.GONE);
                    toView.setRotationY(-90); // przygotowujemy nowy widok od tyłu
                    toView.setVisibility(View.VISIBLE); // pokazujemy go
                    toView.animate()
                            .rotationY(0) // obracamy do przodu
                            .setDuration(200)
                            .start();
                }).start();
    }
}
