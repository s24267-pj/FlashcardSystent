package com.example.flashcardsystent.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.databinding.FragmentHomeBinding;
import com.example.flashcardsystent.viewmodel.DeckViewModel;

/**
 * Fragment representing the home screen.
 * Allows the user to choose between different learning modes (classic, quiz, browse).
 */
public class HomeFragment extends Fragment {

    /** View binding for the fragment layout */
    private FragmentHomeBinding binding;

    /** ViewModel providing access to available flashcard decks */
    private DeckViewModel deckViewModel;

    /**
     * Inflates the layout using ViewBinding.
     *
     * @param inflater  LayoutInflater used to inflate views
     * @param container Optional parent view
     * @param savedInstanceState Previously saved state
     * @return the root view of the fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * Sets up click listeners for navigation to each learning mode.
     *
     * @param view the root view of the fragment
     * @param savedInstanceState saved state, if any
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        deckViewModel = new ViewModelProvider(requireActivity()).get(DeckViewModel.class);

        // Classic mode button
        binding.buttonModeClassic.setOnClickListener(v ->
                deckViewModel.getAllDecks().observe(getViewLifecycleOwner(), decks -> {
                    if (decks == null || decks.isEmpty()) {
                        Toast.makeText(getContext(), R.string.no_flashcard_sets, Toast.LENGTH_SHORT).show();
                    } else {
                        Navigation.findNavController(v).navigate(R.id.classicSetListFragment);
                    }
                })
        );

        // Quiz mode button
        binding.buttonQuizMode.setOnClickListener(v ->
                deckViewModel.getAllDecks().observe(getViewLifecycleOwner(), decks -> {
                    if (decks == null || decks.isEmpty()) {
                        Toast.makeText(getContext(), R.string.no_flashcard_sets, Toast.LENGTH_SHORT).show();
                    } else {
                        Navigation.findNavController(v).navigate(R.id.quizSetListFragment);
                    }
                })
        );

        // Browse mode button
        binding.buttonBrowseMode.setOnClickListener(v ->
                deckViewModel.getAllDecks().observe(getViewLifecycleOwner(), decks -> {
                    if (decks == null || decks.isEmpty()) {
                        Toast.makeText(getContext(), R.string.no_flashcard_sets, Toast.LENGTH_SHORT).show();
                    } else {
                        Navigation.findNavController(v).navigate(R.id.browseSetListFragment);
                    }
                })
        );
    }

    /**
     * Cleans up the binding to avoid memory leaks.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
