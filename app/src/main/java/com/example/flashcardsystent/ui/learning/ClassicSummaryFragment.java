package com.example.flashcardsystent.ui.learning;

/**
 * Summary screen shown after completing a classic session. Contains a single
 * button that returns to the home screen.
 */

// Saved state bundle
import android.os.Bundle;
// Inflates the layout for this fragment
import android.view.LayoutInflater;
// Base UI element type
import android.view.View;
// Container for other views
import android.view.ViewGroup;
// Simple clickable button
import android.widget.Button;

// Annotation for parameters that cannot be null
import androidx.annotation.NonNull;
// Annotation for parameters that may be null
import androidx.annotation.Nullable;
// Basic fragment
import androidx.fragment.app.Fragment;
// Helper for navigating to other fragments
import androidx.navigation.Navigation;

// Access to resource identifiers
import com.example.flashcardsystent.R;

public class ClassicSummaryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout containing the "home" button
        return inflater.inflate(R.layout.fragment_learning_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // When the user taps the button navigate to the home screen
        Button goHome = view.findViewById(R.id.button_back_home);
        goHome.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.navigation_home));
    }
}