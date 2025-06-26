package com.example.flashcardsystent.ui.quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.flashcardsystent.R;

/**
 * Fragment shown after finishing a quiz session.
 * Displays the score and provides options to return to home or view statistics.
 */
public class QuizSummaryFragment extends Fragment {

    /** Number of correctly answered questions */
    private int correctCount;

    /** Total number of questions asked */
    private int totalCount;

    /**
     * Inflates the layout showing quiz results and navigation buttons.
     *
     * @param inflater LayoutInflater used to inflate the fragment's view
     * @param container Optional parent view container
     * @param savedInstanceState Previously saved state (if any)
     * @return the root view of the fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz_summary, container, false);
    }

    /**
     * Called when the fragment's view is fully created.
     * Retrieves quiz results from arguments and sets up UI interactions.
     *
     * @param view the root view of the fragment
     * @param savedInstanceState previously saved state (if any)
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Read the score values passed as arguments
        Bundle args = getArguments();
        if (args != null) {
            correctCount = args.getInt("correctCount", 0);
            totalCount = args.getInt("totalCount", 0);
        }

        // Grab references to the views
        TextView resultText = view.findViewById(R.id.text_result);
        Button buttonBack = view.findViewById(R.id.button_back_home);
        Button buttonStats = view.findViewById(R.id.button_view_stats);

        // Show the formatted score
        resultText.setText(getString(R.string.quiz_result_summary, correctCount, totalCount));

        // Navigate straight back to home
        buttonBack.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.navigation_home)
        );

        // Open the statistics tab, clearing intermediate destinations
        buttonStats.setOnClickListener(v -> Navigation.findNavController(v).navigate(
                R.id.navigation_summary,
                null,
                new androidx.navigation.NavOptions.Builder()
                        .setPopUpTo(R.id.navigation_home, true) // pop back to home
                        .setLaunchSingleTop(true)
                        .build()
        ));
    }
}
