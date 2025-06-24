package com.example.flashcardsystent.ui.learning;

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
import com.example.flashcardsystent.viewmodel.ClassicViewModel;

public class ClassicFragment extends Fragment {

    private ClassicViewModel viewModel;

    private TextView frontView;
    private TextView backView;
    private View cardContainer;
    private boolean showingFront = true;

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

        frontView = view.findViewById(R.id.text_card_front);
        backView = view.findViewById(R.id.text_card_back);
        cardContainer = view.findViewById(R.id.card_container);

        Button buttonKnow = view.findViewById(R.id.button_know);
        Button buttonDontKnow = view.findViewById(R.id.button_dont_know);

        int deckId = getArguments().getInt("deckId", -1);

        viewModel = new ViewModelProvider(this).get(ClassicViewModel.class);
        viewModel.loadCards(deckId, getViewLifecycleOwner());

        viewModel.getCurrentCard().observe(getViewLifecycleOwner(), card -> {
            if (card == null) {
                Toast.makeText(getContext(), "Brak fiszek w zestawie", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigateUp();
            } else {
                frontView.setText(card.front);
                backView.setText(card.back);

                frontView.setRotationY(0);
                backView.setRotationY(0);
                frontView.setVisibility(View.VISIBLE);
                backView.setVisibility(View.GONE);
                showingFront = true;

                cardContainer.setOnClickListener(v -> {
                    if (showingFront) {
                        flipCard(frontView, backView);
                    } else {
                        flipCard(backView, frontView);
                    }
                    showingFront = !showingFront;
                });
            }
        });

        buttonKnow.setOnClickListener(v -> viewModel.onKnow());
        buttonDontKnow.setOnClickListener(v -> viewModel.onDontKnow());
    }

    private void flipCard(View fromView, View toView) {
        fromView.animate()
                .rotationY(90)
                .setDuration(200)
                .withEndAction(() -> {
                    fromView.setVisibility(View.GONE);
                    toView.setRotationY(-90);
                    toView.setVisibility(View.VISIBLE);
                    toView.animate()
                            .rotationY(0)
                            .setDuration(200)
                            .start();
                }).start();
    }
}
