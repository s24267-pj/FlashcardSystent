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

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.data.Deck;
import com.example.flashcardsystent.viewmodel.DeckViewModel;

public class DeckDetailFragment extends Fragment {

    private DeckViewModel deckViewModel;
    private Deck currentDeck;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deck_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView textName = view.findViewById(R.id.text_deck_name);

        Bundle args = getArguments();
        if (args != null) {
            int id = args.getInt("deckId");
            String name = args.getString("deckName");
            textName.setText(name);

            currentDeck = new Deck(name);
            currentDeck.id = id;
        }

        deckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);
    }
}
