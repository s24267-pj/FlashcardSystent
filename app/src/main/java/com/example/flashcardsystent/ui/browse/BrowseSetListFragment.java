package com.example.flashcardsystent.ui.browse;

/**
 * Fragment showing all decks available for browsing. The user selects one and
 * then swipes through its cards. Each operation below is commented for clarity.
 */

// Holds saved state for the fragment instance
import android.os.Bundle;
// Inflates the layout resource
import android.view.LayoutInflater;
// Represents a visual element on screen
import android.view.View;
// Container that holds child views
import android.view.ViewGroup;

// Values marked as not nullable
import androidx.annotation.NonNull;
// Parameters which may be null
import androidx.annotation.Nullable;
// Reusable portion of UI
import androidx.fragment.app.Fragment;
// Factory for obtaining ViewModel instances
import androidx.lifecycle.ViewModelProvider;
// Simplifies navigation between fragments
import androidx.navigation.Navigation;
// Layout manager positioning items in a vertical list
import androidx.recyclerview.widget.LinearLayoutManager;
// List widget capable of recycling item views
import androidx.recyclerview.widget.RecyclerView;

// Access to resource identifiers
import com.example.flashcardsystent.R;
// ViewModel providing deck data
import com.example.flashcardsystent.viewmodel.DeckViewModel;

public class BrowseSetListFragment extends Fragment {
    // Shared ViewModel storing the list of decks
    DeckViewModel deckViewModel;
    // RecyclerView displaying the decks
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the list layout for this fragment
        return inflater.inflate(R.layout.fragment_browse_set_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set up RecyclerView with a vertical layout manager
        recyclerView = view.findViewById(R.id.browse_set_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Observe the list of decks and populate the adapter once loaded
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