package com.example.flashcardsystent.ui.summary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.databinding.FragmentSummaryBinding;

public class SummaryFragment extends Fragment {

    private FragmentSummaryBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSummaryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              Bundle savedInstanceState) {
        SummaryViewModel viewModel = new ViewModelProvider(this).get(SummaryViewModel.class);

        viewModel.totalQuizzes.observe(getViewLifecycleOwner(), count ->
                binding.textTotalGames.setText(getString(R.string.total_quizzes) + (count != null ? count : 0))
        );

        viewModel.correctAnswers.observe(getViewLifecycleOwner(), count ->
                binding.textTotalCorrect.setText(getString(R.string.correct_answers) + (count != null ? count : 0))
        );

        viewModel.wrongAnswers.observe(getViewLifecycleOwner(), count ->
                binding.textTotalWrong.setText(getString(R.string.wrong_answers) + (count != null ? count : 0))
        );

        TextView lastScore = binding.textLastScore;

        viewModel.lastResult.observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                lastScore.setText(getString(R.string.correct) + result.correct +
                        " / " + getString(R.string.wrong_answers_2) + result.wrong);
            } else {
                lastScore.setText(R.string.no_data);
            }
        });


        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        // Nie rób nic — blokujemy cofanie
                    }
                }
        );

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
