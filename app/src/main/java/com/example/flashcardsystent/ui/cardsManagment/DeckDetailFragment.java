package com.example.flashcardsystent.ui.cardsManagment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.adapter.CardAdapter;
import com.example.flashcardsystent.data.Deck;
import com.example.flashcardsystent.viewmodel.CardViewModel;

public class DeckDetailFragment extends Fragment {

    private CardViewModel cardViewModel;
    private CardAdapter cardAdapter;
    private int deckId;
    private Deck currentDeck;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deck_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            deckId = args.getInt("deckId");
            String deckName = args.getString("deckName", "");
            TextView textName = view.findViewById(R.id.text_deck_name);
            textName.setText(deckName);

            currentDeck = new Deck(deckName);
            currentDeck.id = deckId;
        }

        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);

        RecyclerView rv = view.findViewById(R.id.rv_cards);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        cardAdapter = new CardAdapter();
        rv.setAdapter(cardAdapter);

        cardViewModel.getCardsByDeck(deckId)
                .observe(getViewLifecycleOwner(), cards -> {
                    cardAdapter.setItems(cards);
                });

        view.findViewById(R.id.button_add_cards).setOnClickListener(v ->
                AddCardsBottomSheet.newInstance(deckId)
                        .show(getChildFragmentManager(), "ADD_CARDS")
        );
    }
}
