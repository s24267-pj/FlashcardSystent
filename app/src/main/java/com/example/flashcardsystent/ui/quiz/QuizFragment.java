package com.example.flashcardsystent.ui.quiz;

/**
 * Fragment implementing a multiple choice quiz. Each line is commented to
 * highlight how questions are generated and results recorded.
 */

// Executor for running database inserts on a background thread
import java.util.concurrent.Executors;
// Bundle providing saved state
import android.os.Bundle;
// Logging utility
import android.util.Log;
// Inflates layout resources
import android.view.LayoutInflater;
// Base type for UI components
import android.view.View;
// Container that can host other views
import android.view.ViewGroup;
// Interactive button widget
import android.widget.Button;
// Displays the quiz question text
import android.widget.TextView;

// Singleton entry point to database
import com.example.flashcardsystent.data.AppDatabase;
// Entity storing a single quiz result
import com.example.flashcardsystent.data.QuizResult;


// Marks parameters that must not be null
import androidx.annotation.NonNull;
// Marks parameters that may be null
import androidx.annotation.Nullable;
// Access to color resources with tint support
import androidx.core.content.ContextCompat;
// Basic fragment class
import androidx.fragment.app.Fragment;
// Factory for creating ViewModel instances
import androidx.lifecycle.ViewModelProvider;
// Handles navigation actions
import androidx.navigation.Navigation;

// Resource identifiers
import com.example.flashcardsystent.R;
// Entity representing a flash card
import com.example.flashcardsystent.data.Card;
// ViewModel used to load cards
import com.example.flashcardsystent.viewmodel.CardViewModel;

// Mutable list for quiz options
import java.util.ArrayList;
// Utility for shuffling answer order
import java.util.Collections;
// Base list interface
import java.util.List;

public class QuizFragment extends Fragment {

    // Identifier of the deck being quizzed
    private int deckId;
    // ViewModel providing access to card data
    CardViewModel cardViewModel;

    // TextView displaying the current question
    private TextView textQuestion;
    // Buttons showing the answer options
    final Button[] answerButtons = new Button[4];
    // All cards loaded from the selected deck
    private List<Card> allCards = new ArrayList<>();
    // Current question index
    private int currentIndex = 0;
    // Count of correct answers so far
    private int correctCount = 0;
    // Card containing the correct answer for the current question
    private Card correctCard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the quiz layout containing question text and answer buttons
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Retrieve the deck id and log for debugging
        deckId = getArguments().getInt("setId", -1);
        Log.d("QUIZ", "deckId received: " + deckId);

        // Obtain shared ViewModel instance
        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);

        // Find views defined in the layout
        textQuestion = view.findViewById(R.id.text_question);
        answerButtons[0] = view.findViewById(R.id.button_answer_1);
        answerButtons[1] = view.findViewById(R.id.button_answer_2);
        answerButtons[2] = view.findViewById(R.id.button_answer_3);
        answerButtons[3] = view.findViewById(R.id.button_answer_4);

        // Load cards for the chosen deck and prepare the first question
        cardViewModel.getCardsByDeck(deckId).observe(getViewLifecycleOwner(), cards -> {
            allCards = cards;
            if (allCards.size() >= 4) {
                Collections.shuffle(allCards);
                loadNextQuestion();
            } else {
                textQuestion.setText(R.string.too_few_flashcards);
                for (Button btn : answerButtons) {
                    btn.setEnabled(false);
                }
            }
        });
    }

    private void loadNextQuestion() {
        if (!isAdded() || getView() == null) return; // Avoid crash if fragment not attached

        if (currentIndex >= allCards.size()) {
            // Save the quiz result to the database
            QuizResult result = new QuizResult(
                    deckId,
                    correctCount,
                    allCards.size(),
                    System.currentTimeMillis()
            );
            Executors.newSingleThreadExecutor().execute(() ->
                    AppDatabase.getInstance(requireContext())
                            .quizResultDao()
                            .insert(result)
            );

            // Navigate to the summary screen
            Bundle bundle = new Bundle();
            bundle.putInt("correctCount", correctCount);
            bundle.putInt("totalCount", allCards.size());

            Navigation.findNavController(getView())
                    .navigate(R.id.action_quizFragment_to_quizSummaryFragment, bundle);
            return;
        }



        // Set up the next question and answer options
        correctCard = allCards.get(currentIndex);
        textQuestion.setText(correctCard.front);

        List<String> options = new ArrayList<>();
        options.add(correctCard.back);
        for (Card card : allCards) {
            if (!card.back.equals(correctCard.back) && options.size() < 4) {
                options.add(card.back);
            }
        }
        Collections.shuffle(options);

        for (int i = 0; i < 4; i++) {
            Button btn = answerButtons[i];
            btn.setText(options.get(i));
            btn.setBackgroundColor(requireContext().getColor(android.R.color.holo_blue_light));
            btn.setOnClickListener(v -> {
                boolean isCorrect = btn.getText().equals(correctCard.back);
                if (isCorrect) {
                    btn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.quiz_button_correct));

                    correctCount++;
                } else {
                    btn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.quiz_button_wrong));
                }

                // Load the next question after a short delay
                btn.postDelayed(() -> {
                    currentIndex++;
                    loadNextQuestion();
                }, 800);
            });
        }
    }
}