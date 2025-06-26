package com.example.flashcardsystent.ui.quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.adapter.QuizSetAdapter;
import com.example.flashcardsystent.data.AppDatabase;
import com.example.flashcardsystent.data.Deck;
import com.example.flashcardsystent.data.DeckDao;
import com.example.flashcardsystent.databinding.FragmentQuizSetListBinding;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Fragment displaying all available decks for quiz mode.
 * Selecting a deck navigates to a multiple-choice quiz.
 */
public class QuizSetListFragment extends Fragment {

    /** ViewBinding giving access to the layout views */
    private FragmentQuizSetListBinding binding;

    /** DAO used to retrieve decks from the database */
    private DeckDao deckDao;

    /**
     * Inflates the layout and loads the list of quiz decks.
     *
     * @param inflater LayoutInflater to inflate views
     * @param container Optional parent view
     * @param savedInstanceState Saved state, if any
     * @return root view of the fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout and obtain a binding instance
        binding = FragmentQuizSetListBinding.inflate(inflater, container, false);

        // Acquire the DAO from the database
        AppDatabase db = AppDatabase.getInstance(requireContext());
        deckDao = db.deckDao();

        // Fetch decks and populate the RecyclerView
        loadDecks();

        return binding.getRoot();
    }

    /**
     * Loads decks from the database on a background thread
     * and populates the RecyclerView adapter on the main thread.
     */
    private void loadDecks() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Deck> decks = deckDao.getAll();
            requireActivity().runOnUiThread(() -> {
                QuizSetAdapter adapter = new QuizSetAdapter(decks, deck -> {
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
