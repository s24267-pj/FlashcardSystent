package com.example.flashcardsystent.ui.cardsManagment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.adapter.DeckAdapter;
import com.example.flashcardsystent.data.Deck;
import com.example.flashcardsystent.viewmodel.DeckViewModel;

public class ManagmentFragment extends Fragment {

    private DeckViewModel deckViewModel;
    private DeckAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cards_managment, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.deck_list);
        Button addButton = root.findViewById(R.id.button_add_deck);

        adapter = new DeckAdapter(deck -> {
            // TODO: Przejdź do ekranu szczegółów zestawu (edycja/usuwanie itd.)
        });

        recyclerView.setAdapter(adapter);

        deckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);
        deckViewModel.getAllDecks().observe(getViewLifecycleOwner(), adapter::setDeckList);

        addButton.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_managmentFragment_to_addDeckFragment)
        );

        return root;
    }
}
