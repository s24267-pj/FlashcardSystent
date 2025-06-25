package com.example.flashcardsystent.ui.learning;

/**
 * Fragment listing decks for the classic learning mode. Tapping a deck starts
 * the card-flipping session.
 */

// Holds any saved state
import android.os.Bundle;
// Inflates XML layout resources
import android.view.LayoutInflater;
// Base class for UI elements
import android.view.View;
// Container for child views
import android.view.ViewGroup;

// Indicates parameters must not be null
import androidx.annotation.NonNull;
// Indicates parameters may be null
import androidx.annotation.Nullable;
// Reusable portion of UI
import androidx.fragment.app.Fragment;
// Helper for navigating to other fragments
import androidx.navigation.Navigation;
// Layout manager displaying list items vertically
import androidx.recyclerview.widget.LinearLayoutManager;
// RecyclerView that will hold the deck list
import androidx.recyclerview.widget.RecyclerView;

// Access to resource identifiers
import com.example.flashcardsystent.R;
// Adapter used for both quiz and classic lists
import com.example.flashcardsystent.adapter.QuizSetAdapter;
// Entry point to the database
import com.example.flashcardsystent.data.AppDatabase;
// Entity representing a deck of flashcards
import com.example.flashcardsystent.data.Deck;
// DAO for performing deck queries
import com.example.flashcardsystent.data.DeckDao;

// List collection interface
import java.util.List;
// Executor for running database work off the UI thread
import java.util.concurrent.Executors;

public class ClassicSetListFragment extends Fragment {

    // Data access object for retrieving decks
    private DeckDao deckDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout containing the list of decks
        return inflater.inflate(R.layout.fragment_classic_set_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Configure RecyclerView
        RecyclerView recycler = view.findViewById(R.id.recycler_classic_sets);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Obtain database DAO on the background thread
        deckDao = AppDatabase.getInstance(requireContext()).deckDao();

        // Load decks off the main thread then update the adapter
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Deck> decks = deckDao.getAll();
            requireActivity().runOnUiThread(() -> {
                QuizSetAdapter adapter = new QuizSetAdapter(decks, deck -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt("deckId", deck.id);
                    Navigation.findNavController(view).navigate(R.id.learningFragment, bundle);
                });
                recycler.setAdapter(adapter);
            });
        });
    }
}