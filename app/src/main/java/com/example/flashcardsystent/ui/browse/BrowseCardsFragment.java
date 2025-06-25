package com.example.flashcardsystent.ui.browse;

/**
 * Fragment allowing the user to swipe through cards one by one. Detailed
 * comments describe the deck loading logic and card swap mechanism used when
 * flipping through the stack.
 */

// Holds saved instance state for the fragment
import android.os.Bundle;
// Schedules work to run after a delay on the main thread
import android.os.Handler;
// Provides the main thread Looper instance
import android.os.Looper;
// Inflates XML layout resources
import android.view.LayoutInflater;
// Base class for UI elements
import android.view.View;
// Container for child views
import android.view.ViewGroup;
// Button widget for user interactions
import android.widget.Button;

// Marks parameters that cannot be null
import androidx.annotation.NonNull;
// Marks parameters that may be null
import androidx.annotation.Nullable;
// Fragment base class
import androidx.fragment.app.Fragment;
// Factory for obtaining ViewModel instances
import androidx.lifecycle.ViewModelProvider;
// Utility for navigating between fragments
import androidx.navigation.Navigation;
// Layout manager that arranges items in a grid
import androidx.recyclerview.widget.GridLayoutManager;
// RecyclerView for displaying swipeable cards
import androidx.recyclerview.widget.RecyclerView;

// Resource identifiers
import com.example.flashcardsystent.R;
// Entity representing a flashcard
import com.example.flashcardsystent.data.Card;
// ViewModel for loading card data
import com.example.flashcardsystent.viewmodel.CardViewModel;
// RecyclerView adapter that supports card flipping
import com.example.flashcardsystent.adapter.BrowseCardAdapter;

// Double-ended queue used to hold remaining cards
import java.util.ArrayDeque;
// Dynamic list used for visible cards
import java.util.ArrayList;
// Utility for shuffling cards randomly
import java.util.Collections;
// Interface representing a deque data structure
import java.util.Deque;
// List interface for generic collections
import java.util.List;

public class BrowseCardsFragment extends Fragment {

    // Identifier of the currently selected deck
    int deckId;
    // ViewModel retrieving cards from the database
    CardViewModel cardViewModel;
    // Remaining cards not currently visible
    final Deque<Card> cardStack = new ArrayDeque<>();
    // Cards currently displayed on screen
    final List<Card> visibleCards = new ArrayList<>();
    // Maximum number of visible cards at once
    private static final int MAX_VISIBLE = 4;

    // RecyclerView holding the card views
    private RecyclerView recyclerView;
    // Button that finishes the browsing session
    private Button finishButton;
    // Adapter presenting each card
    private BrowseCardAdapter adapter;

    // Handler for scheduling delayed card swaps
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for displaying cards
        return inflater.inflate(R.layout.fragment_browse_cards, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Determine which deck was selected in the previous screen
        deckId = getArguments().getInt("deckId", -1);
        // Set up view references
        recyclerView = view.findViewById(R.id.browse_recycler);
        finishButton = view.findViewById(R.id.button_finish_browse);

        // Obtain the ViewModel and load cards for this deck
        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
        cardViewModel.getCardsByDeck(deckId).observe(getViewLifecycleOwner(), cards -> {
            cardStack.clear();
            visibleCards.clear();

            // Randomize the card order for more variety
            Collections.shuffle(cards);
            cardStack.addAll(cards);

            // Fill the visible card list up to MAX_VISIBLE
            for (int i = 0; i < MAX_VISIBLE && !cardStack.isEmpty(); i++) {
                visibleCards.add(cardStack.pollFirst());
            }

            // Create adapter and attach to RecyclerView
            adapter = new BrowseCardAdapter(visibleCards, this::onCardFlipped);
            recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 1));
            recyclerView.setAdapter(adapter);

            // Reveal the finish button once data is ready
            finishButton.setVisibility(View.VISIBLE);
        });

        // End the browsing session and show summary
        finishButton.setOnClickListener(v -> Navigation.findNavController(view)
                .navigate(R.id.action_browseCardsFragment_to_browseSummaryFragment));
    }

    private void onCardFlipped(Card flippedCard) {
        // Wait a moment before swapping the flipped card to the back of the stack
        handler.postDelayed(() -> {
            if (!cardStack.isEmpty()) {
                int index = visibleCards.indexOf(flippedCard);
                if (index != -1) {
                    visibleCards.remove(index);
                    visibleCards.add(index, cardStack.pollFirst());
                    cardStack.offerLast(flippedCard);
                    adapter.notifyItemChanged(index);
                }
            }
        }, 3000);
    }
}