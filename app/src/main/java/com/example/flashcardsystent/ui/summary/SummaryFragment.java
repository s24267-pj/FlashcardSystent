package com.example.flashcardsystent.ui.summary;

/**
 * Fragment presenting statistics about completed quizzes. The class is heavily
 * commented so every line's purpose is clear to new developers.
 */

// Container of state information for fragment recreation

import android.os.Bundle;
// Converts XML layout resources into View objects
import android.view.LayoutInflater;
// Base type representing all UI components
import android.view.View;
// Group that can contain other views
import android.view.ViewGroup;
// Widget used to display text values
import android.widget.TextView;

import java.util.concurrent.Executors;

// Callback interface for intercepting back press events
import androidx.activity.OnBackPressedCallback;
// Marks values that must not be null
import androidx.annotation.NonNull;
// Reusable portion of UI tied to an activity
import androidx.fragment.app.Fragment;
// Lazily create ViewModel instances
import androidx.lifecycle.ViewModelProvider;

// Access to string resources
import com.example.flashcardsystent.R;
// Binding class generated from fragment_summary.xml
import com.example.flashcardsystent.databinding.FragmentSummaryBinding;
import com.example.flashcardsystent.data.AppDatabase;
import com.example.flashcardsystent.data.DeckDao;

public class SummaryFragment extends Fragment {

    // Holds references to the views defined in the layout
    private FragmentSummaryBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout with ViewBinding for type-safe access to views
        binding = FragmentSummaryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              Bundle savedInstanceState) {
        // Obtain a ViewModel used to load quiz statistics
        SummaryViewModel viewModel = new ViewModelProvider(this).get(SummaryViewModel.class);

        // Update text views whenever the statistics change
        viewModel.totalQuizzes.observe(getViewLifecycleOwner(), count ->
                binding.textTotalGames.setText(getString(R.string.total_quizzes_with_value, count != null ? count : 0))
        );

        viewModel.correctAnswers.observe(getViewLifecycleOwner(), count ->
                binding.textTotalCorrect.setText(getString(R.string.correct_answers_with_value, count != null ? count : 0))
        );

        viewModel.wrongAnswers.observe(getViewLifecycleOwner(), count ->
                binding.textTotalWrong.setText(getString(R.string.wrong_answers_with_value, count != null ? count : 0))
        );

        // Cache a reference to the TextView that shows the last quiz result
        TextView lastScore = binding.textLastScore;

        viewModel.lastResult.observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                // Format string with the correct and wrong counts
                lastScore.setText(getString(R.string.last_result_summary, result.correct, result.wrong));

            } else {
                // If there are no results yet show placeholder
                lastScore.setText(R.string.no_data);
            }
        });

        // Show classic mode statistics
        viewModel.totalClassic.observe(getViewLifecycleOwner(), count ->
                binding.textTotalClassic.setText(getString(R.string.classic_sessions_with_value, count != null ? count : 0))
        );

        DeckDao deckDao = AppDatabase.getInstance(requireContext()).deckDao();

        viewModel.lastClassic.observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                Executors.newSingleThreadExecutor().execute(() -> {
                    String name = deckDao.getById(result.deckId).name;
                    int total = result.correct + result.wrong;
                    int success = total > 0 ? (result.correct * 100 / total) : 0;
                    String text = getString(R.string.last_classic_result, name, success, result.hardestCard);
                    requireActivity().runOnUiThread(() -> binding.textLastClassic.setText(text));
                });
            } else {
                binding.textLastClassic.setText(R.string.no_classic_data);
            }
        });

        // Disable the system back button on this screen
        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        // Do nothing to block back navigation
                    }
                }
        );

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Release the binding when the view is destroyed to avoid leaks
        binding = null;
    }
}