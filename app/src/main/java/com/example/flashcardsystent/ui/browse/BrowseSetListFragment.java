package com.example.flashcardsystent.ui.browse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.viewmodel.DeckViewModel;

public class BrowseSetListFragment extends Fragment {
    DeckViewModel deckViewModel;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browse_set_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.browse_set_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        deckViewModel = new ViewModelProvider(requireActivity()).get(DeckViewModel.class);
        deckViewModel.getAllDecks().observe(getViewLifecycleOwner(), decks -> {
            com.example.flashcardsystent.adapter.BrowseSetAdapter adapter = new com.example.flashcardsystent.adapter.BrowseSetAdapter(decks, deck -> {
                Bundle bundle = new Bundle();
                bundle.putInt("deckId", deck.id);
                Navigation.findNavController(view).navigate(R.id.action_browseSetListFragment_to_browseCardsFragment, bundle);
            });
            recyclerView.setAdapter(adapter);
        });
    }
}