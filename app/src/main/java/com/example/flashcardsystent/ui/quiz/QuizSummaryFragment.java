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

public class QuizSummaryFragment extends Fragment {

    private int correctCount;
    private int totalCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            correctCount = args.getInt("correctCount", 0);
            totalCount = args.getInt("totalCount", 0);
        }

        TextView resultText = view.findViewById(R.id.text_result);
        Button buttonBack = view.findViewById(R.id.button_back_home);
        Button buttonStats = view.findViewById(R.id.button_view_stats);

        resultText.setText(getString(R.string.quiz_result_summary, correctCount, totalCount));

        buttonBack.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.navigation_home)
        );

        buttonStats.setOnClickListener(v -> Navigation.findNavController(v).navigate(
                R.id.navigation_summary,
                null,
                new androidx.navigation.NavOptions.Builder()
                        .setPopUpTo(R.id.navigation_home, true) // lub navigation_home
                        .setLaunchSingleTop(true)
                        .build()
        ));
    }
}
