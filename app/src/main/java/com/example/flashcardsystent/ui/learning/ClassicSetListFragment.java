package com.example.flashcardsystent.ui.learning;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.adapter.QuizSetAdapter;
import com.example.flashcardsystent.data.AppDatabase;
import com.example.flashcardsystent.data.Deck;
import com.example.flashcardsystent.data.DeckDao;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Fragment displaying a list of available flashcard decks for classic learning mode.
 * Tapping a deck starts the card-flipping learning session.
 */
public class ClassicSetListFragment extends Fragment {

    /** DAO used to load flashcard decks from the database */
    private DeckDao deckDao;

    /**
     * Inflates the layout for the deck list.
     *
     * @param inflater LayoutInflater used to inflate views
     * @param container Optional parent view
     * @param savedInstanceState saved state, if any
     * @return the root view of the fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_classic_set_list, container, false);
    }

    /**
     * Loads decks from the database and displays them in a RecyclerView.
     *
     * @param view the fragment’s root view
     * @param savedInstanceState saved state if any
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recycler = view.findViewById(R.id.recycler_classic_sets);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        deckDao = AppDatabase.getInstance(requireContext()).deckDao();

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
