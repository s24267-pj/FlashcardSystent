package com.example.flashcardsystent.ui.browse;

/**
 * Simple summary screen displayed after browsing cards. Buttons let the user
 * repeat the session or return home.
 */

// Holds saved state for fragment recreation
import android.os.Bundle;
// Inflates the layout resource for this fragment
import android.view.LayoutInflater;
// Base class for UI elements
import android.view.View;
// Container that holds child views
import android.view.ViewGroup;
// Standard push button widget
import android.widget.Button;

// Marks parameters that must not be null
import androidx.annotation.NonNull;
// Marks parameters that may be null
import androidx.annotation.Nullable;
// Basic fragment subclass
import androidx.fragment.app.Fragment;
// Used for navigating to other fragments
import androidx.navigation.Navigation;

// Access to string and layout resources
import com.example.flashcardsystent.R;

public class BrowseSummaryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout with two buttons
        return inflater.inflate(R.layout.fragment_browse_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Buttons for repeating or exiting
        Button playAgain = view.findViewById(R.id.button_play_again);
        Button goHome = view.findViewById(R.id.button_go_home);

        // Restart browsing the same deck
        playAgain.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_browseSummaryFragment_to_browseSetListFragment));
        // Navigate back to the home screen
        goHome.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.navigation_home));
    }
}