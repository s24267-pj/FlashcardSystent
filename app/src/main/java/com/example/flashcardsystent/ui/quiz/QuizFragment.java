package com.example.flashcardsystent.ui.quiz;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.data.Card;
import com.example.flashcardsystent.viewmodel.CardViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizFragment extends Fragment {

    private int deckId;
    private CardViewModel cardViewModel;

    private TextView textQuestion;
    private Button[] answerButtons = new Button[4];
    private List<Card> allCards = new ArrayList<>();
    private int currentIndex = 0;
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

        deckId = getArguments().getInt("setId", -1); // używamy setId, bo tak nazwany jest argument
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
                textQuestion.setText("Za mało fiszek w zestawie (min. 4).");
                for (Button btn : answerButtons) {
                    btn.setEnabled(false);
                }
            }
        });
    }

    private void loadNextQuestion() {
        correctCard = allCards.get(currentIndex % allCards.size());
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
            btn.setOnClickListener(v -> {
                if (btn.getText().equals(correctCard.back)) {
                    btn.setBackgroundColor(0xFF81C784); // zielony
                } else {
                    btn.setBackgroundColor(0xFFE57373); // czerwony
                }

                btn.postDelayed(() -> {
                    resetButtonColors();
                    currentIndex++;
                    loadNextQuestion();
                }, 800);
            });
        }
    }

    private void resetButtonColors() {
        for (Button btn : answerButtons) {
            btn.setBackgroundColor(requireContext().getColor(android.R.color.holo_blue_light));
        }
    }
}
