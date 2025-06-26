package com.example.flashcardsystent;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.flashcardsystent.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Main activity hosting the navigation container for the application.
 * Handles setup of the navigation graph and bottom navigation bar.
 */
public class MainActivity extends AppCompatActivity {

    /** Binding object for accessing views defined in activity_main.xml */
    private ActivityMainBinding binding;

    /** Controller that manages navigation between fragments */
    NavController navController;

    /**
     * Called when the activity is first created. Initializes layout, navigation host,
     * and bottom navigation behavior.
     *
     * @param savedInstanceState state from previous instance if available
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout and bind it
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up navigation host and controller
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        navController = navHostFragment.getNavController();

        // Link bottom nav menu to navigation controller
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Update bottom nav selection on destination change
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int destId = destination.getId();
            BottomNavigationView navView = binding.navView;

            if (destId == R.id.navigation_home) {
                navView.getMenu().findItem(R.id.navigation_home).setChecked(true);
            } else if (destId == R.id.navigation_dashboard) {
                navView.getMenu().findItem(R.id.navigation_dashboard).setChecked(true);
            } else if (destId == R.id.navigation_summary) {
                navView.getMenu().findItem(R.id.navigation_summary).setChecked(true);
            }
        });
    }
}
