package com.example.flashcardsystent.ui.home;

/**
 * Fragment displayed when the application launches. It allows the user to
 * choose between different learning modes. Each statement is commented to
 * describe how the screen is set up and how clicks are handled.
 */

// Bundle passed in containing any saved state
import android.os.Bundle;
// Inflates XML layouts into View objects
import android.view.LayoutInflater;
// Base class for all visual elements on screen
import android.view.View;
// Holds child views within the fragment
import android.view.ViewGroup;
// Utility for showing short messages
import android.widget.Toast;

// Annotation for parameters that cannot be null
import androidx.annotation.NonNull;
// Basic building block of UI screens
import androidx.fragment.app.Fragment;
// Factory for creating ViewModel instances
import androidx.lifecycle.ViewModelProvider;
// Helper for navigating between fragments
import androidx.navigation.Navigation;

// Access to resource identifiers
import com.example.flashcardsystent.R;
// Generated binding class for fragment_home.xml
import com.example.flashcardsystent.databinding.FragmentHomeBinding;
// ViewModel holding deck information
import com.example.flashcardsystent.viewmodel.DeckViewModel;

public class HomeFragment extends Fragment {

    // Reference to the binding object representing our layout
    private FragmentHomeBinding binding;
    // Shared ViewModel providing access to decks stored in the database
    private DeckViewModel deckViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment using ViewBinding
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot(); // Return the root view to be displayed
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Obtain a ViewModel instance scoped to the activity
        deckViewModel = new ViewModelProvider(requireActivity()).get(DeckViewModel.class);

        // Navigate to the classic learning list when there are decks available
        binding.buttonModeClassic.setOnClickListener(v -> {
            deckViewModel.getAllDecks().observe(getViewLifecycleOwner(), decks -> {
                if (decks == null || decks.isEmpty()) {
                    Toast.makeText(getContext(), R.string.no_flashcard_sets, Toast.LENGTH_SHORT).show();
                } else {
                    Navigation.findNavController(v).navigate(R.id.classicSetListFragment);
                }
            });
        });

        // Navigate to the quiz mode when at least one deck exists
        binding.buttonQuizMode.setOnClickListener(v -> {
            deckViewModel.getAllDecks().observe(getViewLifecycleOwner(), decks -> {
                if (decks == null || decks.isEmpty()) {
                    Toast.makeText(getContext(), R.string.no_flashcard_sets, Toast.LENGTH_SHORT).show();
                } else {
                    Navigation.findNavController(v).navigate(R.id.quizSetListFragment);
                }
            });
        });

        // Navigate to the browse mode (cards swipe) if decks are present
        binding.buttonBrowseMode.setOnClickListener(v -> {
            deckViewModel.getAllDecks().observe(getViewLifecycleOwner(), decks -> {
                if (decks == null || decks.isEmpty()) {
                    Toast.makeText(getContext(), R.string.no_flashcard_sets, Toast.LENGTH_SHORT).show();
                } else {
                    Navigation.findNavController(v).navigate(R.id.browseSetListFragment);
                }
            });
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Avoid memory leaks by clearing the binding when the view is destroyed
        binding = null;
    }
}