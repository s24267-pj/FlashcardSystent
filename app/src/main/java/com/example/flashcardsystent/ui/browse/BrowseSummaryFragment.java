package com.example.flashcardsystent.ui.browse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.flashcardsystent.R;

/**
 * Fragment shown at the end of a browsing session.
 * Allows the user to either start over or return to the home screen.
 */
public class BrowseSummaryFragment extends Fragment {

    /**
     * Inflates the layout for this summary screen.
     *
     * @param inflater           LayoutInflater used to inflate views
     * @param container          Optional parent view
     * @param savedInstanceState Previous state, if any
     * @return root view for the fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_browse_summary, container, false);
    }

    /**
     * Sets up click listeners for navigation buttons: replay or go home.
     *
     * @param view               the view hierarchy returned from onCreateView
     * @param savedInstanceState previous saved state, if any
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button playAgain = view.findViewById(R.id.button_play_again);
        Button goHome = view.findViewById(R.id.button_go_home);

        // Navigate back to deck selection
        playAgain.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_browseSummaryFragment_to_browseSetListFragment));

        // Navigate to home screen
        goHome.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_browseSummaryFragment_to_navigation_home));
    }
}
