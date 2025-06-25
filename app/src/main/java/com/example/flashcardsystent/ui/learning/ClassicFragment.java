package com.example.flashcardsystent.ui.learning;

/**
 * Fragment implementing the classic flip-card learning mode. Detailed comments
 * explain the card flipping animation and user interactions.
 */

// Container for saved instance state
import android.os.Bundle;
// Inflates the layout resource file
import android.view.LayoutInflater;
// Base class of all visual widgets
import android.view.View;
// Layout container for other views
import android.view.ViewGroup;
// Widget representing a clickable button
import android.widget.Button;
// Text display element
import android.widget.TextView;
// Toast for small popup messages
import android.widget.Toast;

// Annotation for parameters that must not be null
import androidx.annotation.NonNull;
// Annotation for parameters that may be null
import androidx.annotation.Nullable;
// Basic fragment class
import androidx.fragment.app.Fragment;
// Factory for creating ViewModels
import androidx.lifecycle.ViewModelProvider;
// Utility class for performing navigation
import androidx.navigation.Navigation;

// Resource identifiers
import com.example.flashcardsystent.R;
import com.example.flashcardsystent.data.Card;
// ViewModel containing the learning logic
import com.example.flashcardsystent.viewmodel.ClassicViewModel;

public class ClassicFragment extends Fragment {

    // ViewModel controlling which card is shown
    private ClassicViewModel viewModel;

    // Views displaying the front and back of the card
    private TextView frontView;
    private TextView backView;
    private View cardContainer;
    // Tracks whether the front side is currently visible
    private boolean showingFront = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate layout containing card views and buttons
        return inflater.inflate(R.layout.fragment_learning, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtain references to UI elements
        frontView = view.findViewById(R.id.text_card_front);
        backView = view.findViewById(R.id.text_card_back);
        cardContainer = view.findViewById(R.id.card_container);

        // Buttons representing whether the user knows the card
        Button buttonKnow = view.findViewById(R.id.button_know);
        Button buttonDontKnow = view.findViewById(R.id.button_dont_know);

        // Retrieve the deck id passed as an argument
        int deckId = getArguments().getInt("deckId", -1);

        // Obtain the ViewModel and load cards for this deck
        viewModel = new ViewModelProvider(this).get(ClassicViewModel.class);
        viewModel.loadCards(deckId, getViewLifecycleOwner());

        // Observe the current card and update the UI whenever it changes
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
                    Navigation.findNavController(view).navigate(R.id.action_learningFragment_to_learningSummaryFragment, bundle);
                }
            } else {
                frontView.setText(card.front);
                backView.setText(card.back);

                frontView.setRotationY(0);
                backView.setRotationY(0);
                frontView.setVisibility(View.VISIBLE);
                backView.setVisibility(View.GONE);
                showingFront = true;

                // Flip the card when the container is tapped
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

        // Mark the card as known or unknown when buttons are pressed
        buttonKnow.setOnClickListener(v -> viewModel.onKnow());
        buttonDontKnow.setOnClickListener(v -> viewModel.onDontKnow());

        viewModel.getFinished().observe(getViewLifecycleOwner(), finished -> {
            if (Boolean.TRUE.equals(finished)) {
                viewModel.saveResult(deckId);
                Navigation.findNavController(view).navigate(R.id.learningSummaryFragment);
            }
        });
    }

    private void flipCard(View fromView, View toView) {
        // Animate the card flip using a rotation effect
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