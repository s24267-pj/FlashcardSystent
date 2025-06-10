package com.example.flashcardsystent.ui.cardsManagment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.adapter.CardInputAdapter;
import com.example.flashcardsystent.data.Card;
import com.example.flashcardsystent.viewmodel.CardViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class AddCardsBottomSheet extends BottomSheetDialogFragment {
    private static final String ARG_DECK_ID = "deckId";
    private int deckId;
    private CardViewModel cardViewModel;
    private CardInputAdapter adapter;

    public static AddCardsBottomSheet newInstance(int deckId) {
        Bundle args = new Bundle();
        args.putInt(ARG_DECK_ID, deckId);
        AddCardsBottomSheet f = new AddCardsBottomSheet();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        deckId = requireArguments().getInt(ARG_DECK_ID);
        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inf, ViewGroup c, Bundle s) {
        View view = inf.inflate(R.layout.dialog_add_cards, c, false);
        RecyclerView rv = view.findViewById(R.id.rv_card_inputs);

        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CardInputAdapter(deckId);
        rv.setAdapter(adapter);

        view.findViewById(R.id.button_save_cards)
                .setOnClickListener(v -> {
                    List<Card> toSave = adapter.getFilledCards();
                    for (Card card : toSave) {
                        cardViewModel.insert(card);
                    }
                    dismiss();
                });
        return view;
    }
}

