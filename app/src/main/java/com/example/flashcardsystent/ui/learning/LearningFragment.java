package com.example.flashcardsystent.ui.learning;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.viewmodel.LearningViewModel;

public class LearningFragment extends Fragment {

    private LearningViewModel viewModel;
    private TextView cardText;
    private Button buttonKnow, buttonDontKnow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_learning, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cardText = view.findViewById(R.id.text_card);
        buttonKnow = view.findViewById(R.id.button_know);
        buttonDontKnow = view.findViewById(R.id.button_dont_know);

        int deckId = getArguments().getInt("deckId", -1);

        viewModel = new ViewModelProvider(this).get(LearningViewModel.class);
        viewModel.loadCards(deckId);

        viewModel.getCurrentCard().observe(getViewLifecycleOwner(), card -> {
            if (card == null) {
                Navigation.findNavController(view).navigate(R.id.learningSummaryFragment);

            } else {
                cardText.setText(card.front);
                cardText.setOnClickListener(new View.OnClickListener() {
                    private boolean isFront = true;
                    @Override
                    public void onClick(View v) {
                        isFront = !isFront;
                        cardText.setText(isFront ? card.front : card.back);
                    }
                });
            }
        });

        buttonKnow.setOnClickListener(v -> viewModel.onKnow());
        buttonDontKnow.setOnClickListener(v -> viewModel.onDontKnow());
    }
}
