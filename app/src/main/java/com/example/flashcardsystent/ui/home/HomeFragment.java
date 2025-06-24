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

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private DeckViewModel deckViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        deckViewModel = new ViewModelProvider(requireActivity()).get(DeckViewModel.class);

        binding.buttonModeClassic.setOnClickListener(v -> {
            deckViewModel.getAllDecks().observe(getViewLifecycleOwner(), decks -> {
                if (decks == null || decks.isEmpty()) {
                    Toast.makeText(getContext(), "Brak zestawów fiszek", Toast.LENGTH_SHORT).show();
                } else {
                    Navigation.findNavController(v).navigate(R.id.classicSetListFragment);
                }
            });
        });

        binding.buttonQuizMode.setOnClickListener(v -> {
            deckViewModel.getAllDecks().observe(getViewLifecycleOwner(), decks -> {
                if (decks == null || decks.isEmpty()) {
                    Toast.makeText(getContext(), "Brak zestawów fiszek", Toast.LENGTH_SHORT).show();
                } else {
                    Navigation.findNavController(v).navigate(R.id.quizSetListFragment);
                }
            });
        });

        binding.buttonBrowseMode.setOnClickListener(v -> {
            deckViewModel.getAllDecks().observe(getViewLifecycleOwner(), decks -> {
                if (decks == null || decks.isEmpty()) {
                    Toast.makeText(getContext(), "Brak zestawów fiszek", Toast.LENGTH_SHORT).show();
                } else {
                    Navigation.findNavController(v).navigate(R.id.browseSetListFragment);
                }
            });
        });

        binding.buttonModeTyping.setOnClickListener(v ->
                Toast.makeText(getContext(), "Tryb pisania (mock)", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
