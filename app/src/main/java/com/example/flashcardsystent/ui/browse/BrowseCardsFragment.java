package com.example.flashcardsystent.ui.browse;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.adapter.BrowseCardAdapter;
import com.example.flashcardsystent.data.Card;
import com.example.flashcardsystent.viewmodel.CardViewModel;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * Fragment that allows the user to swipe through flashcards one at a time in browse mode.
 * Displays a limited number of cards at once and swaps them dynamically when the user flips a card.
 */
public class BrowseCardsFragment extends Fragment {

    /** ViewModel used to load cards from the database */
    private CardViewModel cardViewModel;

    /** Queue of cards waiting to be shown */
    private final Deque<Card> cardStack = new ArrayDeque<>();

    /** List of currently visible cards */
    private final List<Card> visibleCards = new ArrayList<>();

    /** Maximum number of cards visible on screen at once */
    private static final int MAX_VISIBLE = 4;

    /** RecyclerView displaying flashcards */
    private RecyclerView recyclerView;

    /** Button that ends the browsing session */
    private Button finishButton;

    /** Adapter responsible for rendering cards */
    private BrowseCardAdapter adapter;

    /** Handler for scheduling card swaps with delay */
    private final Handler handler = new Handler(Looper.getMainLooper());

    /**
     * Inflates the layout for this fragment.
     *
     * @param inflater LayoutInflater used to inflate views
     * @param container Optional container for the fragment
     * @param savedInstanceState Previous state, if any
     * @return the root view for the fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browse_cards, container, false);
    }

    /**
     * Called after the view has been created. Sets up the card list, observers, and finish button.
     *
     * @param view the root view returned from onCreateView
     * @param savedInstanceState any saved state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        /** The ID of the deck currently being browsed */
        int deckId = getArguments().getInt("deckId", -1);
        recyclerView = view.findViewById(R.id.browse_recycler);
        finishButton = view.findViewById(R.id.button_finish_browse);

        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
        cardViewModel.getCardsByDeck(deckId).observe(getViewLifecycleOwner(), cards -> {
            cardStack.clear();
            visibleCards.clear();
            Collections.shuffle(cards);
            cardStack.addAll(cards);

            for (int i = 0; i < MAX_VISIBLE && !cardStack.isEmpty(); i++) {
                visibleCards.add(cardStack.pollFirst());
            }

            adapter = new BrowseCardAdapter(visibleCards, this::onCardFlipped);
            recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 1));
            recyclerView.setAdapter(adapter);
            finishButton.setVisibility(View.VISIBLE);
        });

        finishButton.setOnClickListener(v ->
                Navigation.findNavController(view)
                        .navigate(R.id.action_browseCardsFragment_to_browseSummaryFragment)
        );
    }

    /**
     * Handles logic when a card is flipped to the back side.
     * Swaps it with the next card from the stack after a short delay.
     *
     * @param flippedCard the card that was just flipped
     */
    private void onCardFlipped(Card flippedCard) {
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
