package com.example.flashcardsystent.ui.quiz;

/**
 * Summary fragment shown after finishing a quiz. Displays the score and offers
 * buttons to go home or open statistics.
 */

// Bundle containing saved state data
import android.os.Bundle;
// Used to inflate the layout XML
import android.view.LayoutInflater;
// Base class of all view objects
import android.view.View;
// Parent view that contains other UI elements
import android.view.ViewGroup;
// Standard push button widget
import android.widget.Button;
// Widget used to display the score
import android.widget.TextView;

// Annotation marking parameters that must not be null
import androidx.annotation.NonNull;
// Annotation marking parameters that may be null
import androidx.annotation.Nullable;
// Reusable UI component
import androidx.fragment.app.Fragment;
// Utility for performing navigation actions
import androidx.navigation.Navigation;

// Access to resource identifiers
import com.example.flashcardsystent.R;

public class QuizSummaryFragment extends Fragment {

    // Number of correctly answered questions
    private int correctCount;
    // Total number of questions asked
    private int totalCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the summary layout showing quiz results
        return inflater.inflate(R.layout.fragment_quiz_summary, container, false);
    }

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