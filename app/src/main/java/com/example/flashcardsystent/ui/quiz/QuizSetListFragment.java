package com.example.flashcardsystent.ui.quiz;

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

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.adapter.QuizSetAdapter;
import com.example.flashcardsystent.databinding.FragmentQuizSetListBinding;
import com.example.flashcardsystent.viewmodel.DeckViewModel;

public class QuizSetListFragment extends Fragment {

    private FragmentQuizSetListBinding binding;
    private QuizSetAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentQuizSetListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new QuizSetAdapter(deck -> {
            Bundle bundle = new Bundle();
            bundle.putInt("setId", deck.id);
            Navigation.findNavController(binding.getRoot())
                    .navigate(R.id.action_quizSetListFragment_to_quizFragment, bundle);
        });
        binding.recyclerView.setAdapter(adapter);

        DeckViewModel viewModel = new ViewModelProvider(this).get(DeckViewModel.class);
        viewModel.getAllDecks().observe(getViewLifecycleOwner(), adapter::submitList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
