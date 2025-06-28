package com.example.flashcardsystent.ui.quiz;

// Import klas systemowych Androida
import android.os.Bundle; // Służy do przekazywania danych między komponentami (np. fragmentami)
import android.util.Log; // Umożliwia logowanie komunikatów do Logcat
import android.view.LayoutInflater; // Pozwala "nadmuchać" (utworzyć) widok z pliku XML
import android.view.View; // Bazowa klasa dla wszystkich elementów interfejsu użytkownika
import android.view.ViewGroup; // Klasa reprezentująca kontener widoków (np. layouty)
import android.widget.Button; // Klasa reprezentująca przyciski
import android.widget.TextView; // Klasa reprezentująca pole tekstowe

// Import adnotacji wskazujących na nullowalność
import androidx.annotation.NonNull; // Oznacza, że dany argument nie może być nullem
import androidx.annotation.Nullable; // Oznacza, że dany argument może być nullem

// Importy komponentów Android Jetpack
import androidx.core.content.ContextCompat; // Umożliwia dostęp do zasobów (np. kolorów) z kontekstu
import androidx.fragment.app.Fragment; // Bazowa klasa fragmentów
import androidx.lifecycle.ViewModelProvider; // Umożliwia tworzenie i udostępnianie ViewModeli
import androidx.navigation.Navigation; // Umożliwia nawigację między fragmentami

// Import zasobów i klas wewnętrznych aplikacji
import com.example.flashcardsystent.R; // Dostęp do zasobów (teksty, kolory, ID layoutów itp.)
import com.example.flashcardsystent.data.AppDatabase; // Główna klasa dostępu do bazy danych Room
import com.example.flashcardsystent.data.Card; // Klasa reprezentująca fiszkę
import com.example.flashcardsystent.data.QuizResult; // Klasa reprezentująca wynik quizu do zapisania
import com.example.flashcardsystent.viewmodel.CardViewModel; // ViewModel do zarządzania fiszkami

// Import klas Java
import java.util.ArrayList; // Lista dynamiczna (rozszerzalna)
import java.util.Collections; // Klasa narzędziowa do mieszania kolekcji itp.
import java.util.List; // Interfejs reprezentujący listę
import java.util.concurrent.Executors; // Umożliwia tworzenie i zarządzanie wątkami

/**
 * Fragment odpowiedzialny za tryb quizu wielokrotnego wyboru.
 * Pokazuje pytanie (przód fiszki) i cztery możliwe odpowiedzi.
 */
public class QuizFragment extends Fragment {

    // ID zestawu fiszek, który został wybrany do quizu
    private int deckId;

    // ViewModel do pobierania fiszek z bazy danych
    private CardViewModel cardViewModel;

    // Tekst pytania (przód karty)
    private TextView textQuestion;

    // Cztery przyciski do odpowiedzi
    private final Button[] answerButtons = new Button[4];

    // Lista wszystkich kart z wybranego zestawu
    private List<Card> allCards = new ArrayList<>();

    // Indeks aktualnego pytania
    private int currentIndex = 0;

    // Liczba poprawnych odpowiedzi
    private int correctCount = 0;

    // Aktualnie poprawna karta (poprawna odpowiedź)
    private Card correctCard;

    /**
     * Tworzy widok quizu na podstawie pliku XML fragment_quiz.xml
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    /**
     * Po utworzeniu widoku: inicjalizujemy widoki, ViewModel i ładujemy dane.
     */
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Pobieramy ID zestawu fiszek przekazany w argumencie o nazwie "setId"
        deckId = getArguments().getInt("setId", -1);
        Log.d("QUIZ", "deckId received: " + deckId);

        // Pobieramy ViewModel współdzielony z aktywnością
        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);

        // Znajdujemy elementy widoku
        textQuestion = view.findViewById(R.id.text_question);
        answerButtons[0] = view.findViewById(R.id.button_answer_1);
        answerButtons[1] = view.findViewById(R.id.button_answer_2);
        answerButtons[2] = view.findViewById(R.id.button_answer_3);
        answerButtons[3] = view.findViewById(R.id.button_answer_4);

        // Obserwujemy fiszki przypisane do danego zestawu
        cardViewModel.getCardsByDeck(deckId).observe(getViewLifecycleOwner(), cards -> {
            allCards = cards;
            if (allCards.size() >= 4) {
                Collections.shuffle(allCards); // mieszamy listę pytań
                loadNextQuestion(); // ładujemy pierwsze pytanie
            } else {
                textQuestion.setText(R.string.too_few_flashcards); // komunikat o zbyt małej liczbie fiszek
                for (Button btn : answerButtons) {
                    btn.setEnabled(false); // wyłączamy przyciski odpowiedzi
                }
            }
        });
    }

    /**
     * Ładuje kolejne pytanie i odpowiedzi do interfejsu użytkownika.
     * Jeśli nie ma już więcej pytań, zapisuje wynik i przechodzi do podsumowania.
     */
    private void loadNextQuestion() {
        if (!isAdded() || getView() == null) return; // zabezpieczenie przed błędami

        if (currentIndex >= allCards.size()) {
            // Liczymy błędne odpowiedzi
            int wrongCount = allCards.size() - correctCount;

            // Tworzymy obiekt QuizResult do zapisania
            QuizResult result = new QuizResult(
                    deckId,
                    correctCount,
                    wrongCount,
                    allCards.size(),
                    System.currentTimeMillis()
            );

            // Wstawiamy wynik quizu do bazy danych w osobnym wątku
            Executors.newSingleThreadExecutor().execute(() ->
                    AppDatabase.getInstance(requireContext())
                            .quizResultDao()
                            .insert(result)
            );

            // Przechodzimy do ekranu podsumowania quizu
            Bundle bundle = new Bundle();
            bundle.putInt("correctCount", correctCount);
            bundle.putInt("totalCount", allCards.size());

            Navigation.findNavController(getView())
                    .navigate(R.id.action_quizFragment_to_quizSummaryFragment, bundle);
            return;
        }

        // Pobieramy aktualne pytanie
        correctCard = allCards.get(currentIndex);
        textQuestion.setText(correctCard.front);

        // Tworzymy listę odpowiedzi, najpierw poprawna
        List<String> options = new ArrayList<>();
        options.add(correctCard.back);

        // Dodajemy inne błędne odpowiedzi (maks. 3 inne)
        for (Card card : allCards) {
            if (!card.back.equals(correctCard.back) && options.size() < 4) {
                options.add(card.back);
            }
        }
        Collections.shuffle(options); // mieszamy odpowiedzi

        // Przypisujemy tekst i logikę do przycisków
        for (int i = 0; i < 4; i++) {
            Button btn = answerButtons[i];
            btn.setText(options.get(i));
            btn.setBackgroundColor(requireContext().getColor(android.R.color.holo_blue_light));

            // Obsługa kliknięcia odpowiedzi
            btn.setOnClickListener(v -> {
                boolean isCorrect = btn.getText().equals(correctCard.back);
                btn.setBackgroundColor(ContextCompat.getColor(requireContext(),
                        isCorrect ? R.color.quiz_button_correct : R.color.quiz_button_wrong));
                if (isCorrect) correctCount++;

                // Przechodzimy do kolejnego pytania z opóźnieniem 800ms (animacja)
                btn.postDelayed(() -> {
                    currentIndex++;
                    loadNextQuestion();
                }, 800);
            });
        }
    }
}
