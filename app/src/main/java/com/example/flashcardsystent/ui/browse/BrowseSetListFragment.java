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
import com.example.flashcardsystent.adapter.BrowseSetAdapter;

/**
 * Fragment displaying a list of all available flashcard decks in browse mode.
 * Allows the user to select a deck to browse its cards.
 */
public class BrowseSetListFragment extends Fragment {

    /** ViewModel that provides access to all available decks */
    private DeckViewModel deckViewModel;

    /** RecyclerView that displays the list of decks */
    private RecyclerView recyclerView;

    /**
     * Inflates the layout for this fragment.
     *
     * @param inflater  LayoutInflater to inflate views
     * @param container Optional parent view
     * @param savedInstanceState Saved state if any
     * @return the root view for the fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browse_set_list, container, false);
    }

    /**
     * Called after the fragment view has been created.
     * Initializes the RecyclerView and observes the deck list.
     *
     * @param view Root view of the fragment
     * @param savedInstanceState Saved state if any
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.browse_set_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        deckViewModel = new ViewModelProvider(requireActivity()).get(DeckViewModel.class);
        deckViewModel.getAllDecks().observe(getViewLifecycleOwner(), decks -> {
            BrowseSetAdapter adapter = new BrowseSetAdapter(decks, deck -> {
                Bundle bundle = new Bundle();
                bundle.putInt("deckId", deck.id);
                Navigation.findNavController(view)
                        .navigate(R.id.action_browseSetListFragment_to_browseCardsFragment, bundle);
            });
            recyclerView.setAdapter(adapter);
        });
    }
}
