package com.example.flashcardsystent;
/**
 * Main activity hosting navigation for the application.
 * <p>
 * Each line below is accompanied by an English comment explaining
 * the purpose of the statement so the project can be used as a
 * learning reference. These comments also serve as a simple form of
 * Javadoc documentation.
 */

// Bundle carries information passed between activities and fragments at creation time
import android.os.Bundle;
// Base class that provides compatibility features for older Android versions
import androidx.appcompat.app.AppCompatActivity;
// Controller object that performs fragment navigation operations
import androidx.navigation.NavController;
// Fragment implementation that hosts a navigation graph
import androidx.navigation.fragment.NavHostFragment;
// Holds configuration about which destinations are considered "top level"
import androidx.navigation.ui.AppBarConfiguration;
// Utility methods for tying action bar and bottom navigation to NavController
import androidx.navigation.ui.NavigationUI;

// Generated class that gives type-safe access to views in activity_main.xml
import com.example.flashcardsystent.databinding.ActivityMainBinding;
// Material component used to show a menu at the bottom of the screen
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    /** Binding object for accessing views defined in activity_main.xml */
    private ActivityMainBinding binding;

    /** Controller that manages navigation between fragments */
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Always call super to allow the parent class to restore state
        super.onCreate(savedInstanceState);
        // Inflate the layout defined in activity_main.xml and obtain a binding
        // object that provides direct references to all views in that layout
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        // Specify the layout that should be used for this activity by passing
        // the root view from the binding
        setContentView(binding.getRoot());

        // Find the NavHostFragment defined in the activity's XML layout.  This
        // fragment hosts our navigation graph and is responsible for swapping
        // fragments as the user navigates
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        // Ask the host fragment for its NavController which provides APIs to
        // perform navigation actions and query back stack state
        navController = navHostFragment.getNavController();

        // Tie the bottom navigation view to the NavController so taps on the
        // menu automatically trigger navigation to the associated destination
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Manually update the selected bottom navigation item whenever the
        // NavController reports that the destination has changed
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            // Determine the id of the fragment that was navigated to
            int destId = destination.getId();
            // Convenience reference to the bottom navigation view
            BottomNavigationView navView = binding.navView;

            // Check the menu item that corresponds to the current destination
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
