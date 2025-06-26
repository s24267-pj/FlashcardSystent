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
import com.example.flashcardsystent.data.Card;
import com.example.flashcardsystent.viewmodel.ClassicViewModel;

/**
 * Fragment implementing the classic learning mode, where users flip flashcards
 * and mark whether they know the answer or not.
 */
public class ClassicFragment extends Fragment {

    /** ViewModel managing flashcard progression and learning stats */
    private ClassicViewModel viewModel;

    /** Views for showing the front and back of the flashcard */
    private TextView frontView;
    private TextView backView;

    /** Container that wraps the flashcard and handles flipping */
    private View cardContainer;

    /** Whether the front side is currently visible */
    private boolean showingFront = true;

    /**
     * Inflates the layout for the classic learning screen.
     *
     * @param inflater layout inflater
     * @param container optional parent container
     * @param savedInstanceState saved state
     * @return root view of the fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_learning, container, false);
    }

    /**
     * Initializes the flashcard display, sets up flip logic and button actions.
     *
     * @param view root view of the fragment
     * @param savedInstanceState saved state, if any
     */
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
                if (viewModel.getTotalCount() == 0) {
                    Toast.makeText(requireContext(), R.string.no_flashcards_in_deck, Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(view).navigateUp();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("totalCount", viewModel.getTotalCount());
                    Card hardest = viewModel.getMostDifficultCard();
                    if (hardest != null) {
                        bundle.putString("hardFront", hardest.front);
                        bundle.putString("hardBack", hardest.back);
                        bundle.putInt("hardCount", viewModel.getDontKnowCount(hardest.id));
                    }
                    int totalClicks = viewModel.getKnowClicks() + viewModel.getDontKnowClicks();
                    int rate = totalClicks > 0 ? (int) Math.round(100.0 * viewModel.getKnowClicks() / totalClicks) : 0;
                    bundle.putInt("successRate", rate);
                    Navigation.findNavController(view)
                            .navigate(R.id.action_learningFragment_to_learningSummaryFragment, bundle);
                }
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

        viewModel.getFinished().observe(getViewLifecycleOwner(), finished -> {
            if (Boolean.TRUE.equals(finished)) {
                viewModel.saveResult(deckId);
            }
        });
    }

    /**
     * Animates the card flip effect between two views.
     *
     * @param fromView the currently visible side
     * @param toView the side to be shown
     */
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
