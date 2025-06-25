package com.example.flashcardsystent.ui.quiz;

/**
 * Fragment showing all decks available for quiz mode. Selecting a deck begins
 * a multiple choice quiz.
 */

// Container for saved instance state
import android.os.Bundle;
// Used to inflate XML layout resources
import android.view.LayoutInflater;
// Base class of all visual elements
import android.view.View;
// Container for child views
import android.view.ViewGroup;

// Annotation marking a non-null parameter
import androidx.annotation.NonNull;
// Base class for UI screens
import androidx.fragment.app.Fragment;
// Helper class for navigation actions
import androidx.navigation.Navigation;
// Layout manager arranging items vertically
import androidx.recyclerview.widget.LinearLayoutManager;

// Resource identifiers
import com.example.flashcardsystent.R;
// Access point to the database instance
import com.example.flashcardsystent.data.AppDatabase;
// Entity representing a deck
import com.example.flashcardsystent.data.Deck;
// DAO for retrieving decks
import com.example.flashcardsystent.data.DeckDao;
// ViewBinding for fragment_quiz_set_list.xml
import com.example.flashcardsystent.databinding.FragmentQuizSetListBinding;

// List collection interface
import java.util.List;
// Executor used to load data on a background thread
import java.util.concurrent.Executors;

public class QuizSetListFragment extends Fragment {

    // Binding object giving access to layout views
    private FragmentQuizSetListBinding binding;
    // DAO used to query decks
    private DeckDao deckDao;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout and obtain a binding instance
        binding = FragmentQuizSetListBinding.inflate(inflater, container, false);

        // Acquire the DAO from the database
        AppDatabase db = AppDatabase.getInstance(requireContext());
        deckDao = db.deckDao();

        // Fetch decks and populate the RecyclerView
        loadDecks();

        return binding.getRoot();
    }

    private void loadDecks() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Deck> decks = deckDao.getAll();
            requireActivity().runOnUiThread(() -> {
                com.example.flashcardsystent.adapter.QuizSetAdapter adapter = new com.example.flashcardsystent.adapter.QuizSetAdapter(decks, deck -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt("setId", deck.id);
                    Navigation.findNavController(binding.getRoot())
                            .navigate(R.id.action_quizSetListFragment_to_quizFragment, bundle);
                });
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                binding.recyclerView.setAdapter(adapter);
            });
        });
    }

}