package com.example.flashcardsystent.ui.learning;

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
 * Fragment displaying a summary of the classic learning session.
 * Shows statistics and allows returning to the home screen.
 */
public class ClassicSummaryFragment extends Fragment {

    /**
     * Inflates the layout for the summary screen.
     *
     * @param inflater LayoutInflater used to inflate views
     * @param container Optional parent container
     * @param savedInstanceState Previously saved state
     * @return root view of the fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_learning_summary, container, false);
    }

    /**
     * Populates statistics from arguments and sets up navigation button.
     *
     * @param view the root view of the fragment
     * @param savedInstanceState saved state if any
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        int total = args != null ? args.getInt("totalCount", 0) : 0;
        String hardFront = args != null ? args.getString("hardFront") : null;
        String hardBack = args != null ? args.getString("hardBack") : null;
        int hardCount = args != null ? args.getInt("hardCount", 0) : 0;
        int rate = args != null ? args.getInt("successRate", 0) : 0;

        TextView totalView = view.findViewById(R.id.text_total_cards);
        TextView hardestView = view.findViewById(R.id.text_hardest_card);
        TextView rateView = view.findViewById(R.id.text_success_rate);

        totalView.setText(getString(R.string.total_flashcards_with_value, total));

        if (hardFront != null) {
            String cardText = hardFront + " - " + hardBack;
            hardestView.setText(getString(R.string.hardest_card_with_value, cardText, hardCount));
        } else {
            hardestView.setText(R.string.hardest_card_none);
        }

        rateView.setText(getString(R.string.success_rate_with_value, rate));

        Button goHome = view.findViewById(R.id.button_back_home);
        goHome.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.navigation_home)
        );
    }
}
