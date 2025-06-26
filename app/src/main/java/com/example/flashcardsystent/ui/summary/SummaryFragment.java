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
import com.example.flashcardsystent.viewmodel.SummaryViewModel;

/**
 * Fragment presenting statistics about completed quizzes and classic sessions.
 * Shows total games, answers, and the result of the most recent quiz.
 */
public class SummaryFragment extends Fragment {

    /** Holds references to the views defined in the layout using ViewBinding */
    private FragmentSummaryBinding binding;

    /**
     * Inflates the layout for the statistics summary screen.
     *
     * @param inflater LayoutInflater used to inflate views
     * @param container Optional parent container
     * @param savedInstanceState Previously saved state (if any)
     * @return the root view of the fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout with ViewBinding for type-safe access to views
        binding = FragmentSummaryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * Called when the view has been created. Sets up observers and disables back navigation.
     *
     * @param view root view of the fragment
     * @param savedInstanceState previously saved state (if any)
     */
    @Override
    public void onViewCreated(@NonNull View view,
                              Bundle savedInstanceState) {

        // Obtain a ViewModel used to load quiz and classic statistics
        SummaryViewModel viewModel = new ViewModelProvider(this).get(SummaryViewModel.class);

        // Update quiz summary statistics
        viewModel.totalQuizzes.observe(getViewLifecycleOwner(), count ->
                binding.textTotalGames.setText(
                        getString(R.string.total_quizzes_with_value, count != null ? count : 0))
        );

        viewModel.correctAnswers.observe(getViewLifecycleOwner(), count ->
                binding.textTotalCorrect.setText(
                        getString(R.string.correct_answers_with_value, count != null ? count : 0))
        );

        viewModel.wrongAnswers.observe(getViewLifecycleOwner(), count ->
                binding.textTotalWrong.setText(
                        getString(R.string.wrong_answers_with_value, count != null ? count : 0))
        );

        // Show last result or placeholder
        TextView lastScore = binding.textLastScore;
        viewModel.lastResult.observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                lastScore.setText(
                        getString(R.string.last_result_summary, result.correct, result.wrong));
            } else {
                lastScore.setText(R.string.no_data);
            }
        });

        // Show total number of classic sessions
        viewModel.totalClassic.observe(getViewLifecycleOwner(), count ->
                binding.textTotalClassic.setText(
                        getString(R.string.classic_sessions_with_value, count != null ? count : 0))
        );

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

    /**
     * Called when the view is being destroyed. Cleans up ViewBinding reference.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
