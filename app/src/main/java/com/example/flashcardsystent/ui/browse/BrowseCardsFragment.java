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
import com.example.flashcardsystent.data.Card;
import com.example.flashcardsystent.viewmodel.CardViewModel;
import com.example.flashcardsystent.adapter.BrowseCardAdapter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class BrowseCardsFragment extends Fragment {

    int deckId;
    CardViewModel cardViewModel;
    final Deque<Card> cardStack = new ArrayDeque<>();
    final List<Card> visibleCards = new ArrayList<>();
    private static final int MAX_VISIBLE = 4;

    private RecyclerView recyclerView;
    private Button finishButton;
    private BrowseCardAdapter adapter;

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browse_cards, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        deckId = (args != null) ? args.getInt("setId", -1) : -1;
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

        finishButton.setOnClickListener(v -> Navigation.findNavController(view)
                .navigate(R.id.action_browseCardsFragment_to_browseSummaryFragment));
    }

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
