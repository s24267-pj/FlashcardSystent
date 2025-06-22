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
import com.example.flashcardsystent.data.AppDatabase;
import com.example.flashcardsystent.data.Deck;
import com.example.flashcardsystent.data.DeckDao;
import com.example.flashcardsystent.databinding.FragmentQuizSetListBinding;

import java.util.List;
import java.util.concurrent.Executors;

public class QuizSetListFragment extends Fragment {

    private FragmentQuizSetListBinding binding;
    private DeckDao deckDao;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentQuizSetListBinding.inflate(inflater, container, false);

        AppDatabase db = AppDatabase.getInstance(requireContext());
        deckDao = db.deckDao();

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
