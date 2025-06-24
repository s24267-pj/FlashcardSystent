package com.example.flashcardsystent.ui.quiz;

import java.util.concurrent.Executors;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.flashcardsystent.data.AppDatabase;
import com.example.flashcardsystent.data.QuizResult;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.data.Card;
import com.example.flashcardsystent.viewmodel.CardViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizFragment extends Fragment {

    private int deckId;
    CardViewModel cardViewModel;

    private TextView textQuestion;
    final Button[] answerButtons = new Button[4];
    private List<Card> allCards = new ArrayList<>();
    private int currentIndex = 0;
    private int correctCount = 0;
    private Card correctCard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        deckId = getArguments().getInt("setId", -1);
        Log.d("QUIZ", "deckId received: " + deckId);

        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);

        textQuestion = view.findViewById(R.id.text_question);
        answerButtons[0] = view.findViewById(R.id.button_answer_1);
        answerButtons[1] = view.findViewById(R.id.button_answer_2);
        answerButtons[2] = view.findViewById(R.id.button_answer_3);
        answerButtons[3] = view.findViewById(R.id.button_answer_4);

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
        if (!isAdded() || getView() == null) return; // 👈 ochrona przed crashami

        if (currentIndex >= allCards.size()) {
            // Zapisz wynik do bazy
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

            // Przejdź do podsumowania
            Bundle bundle = new Bundle();
            bundle.putInt("correctCount", correctCount);
            bundle.putInt("totalCount", allCards.size());

            Navigation.findNavController(getView())
                    .navigate(R.id.action_quizFragment_to_quizSummaryFragment, bundle);
            return;
        }



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

                btn.postDelayed(() -> {
                    currentIndex++;
                    loadNextQuestion();
                }, 800);
            });
        }
    }
}
