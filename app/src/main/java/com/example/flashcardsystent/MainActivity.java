package com.example.flashcardsystent;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.flashcardsystent.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

/**
 * MainActivity is the host Activity for the app's navigation graph,
 * managing the BottomNavigationView and NavController interactions.
 * It ensures that tapping any tab always pops to that tab's root fragment.
 */
public class MainActivity extends AppCompatActivity {

    /** View binding instance for accessing views in activity_main.xml. */
    private ActivityMainBinding binding;

    /**
     * Called when the Activity is starting. Sets up view binding and
     * configures bottom‐nav so that selecting any tab immediately
     * returns to that tab's root fragment.
     *
     * @param savedInstanceState if non-null, this Activity is being re-created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 1) Find the NavController hosted in our NavHostFragment
        NavController navController = Navigation.findNavController(
                this, R.id.nav_host_fragment_activity_main);
        BottomNavigationView bottomNav = binding.navView;

        // 2) Intercept all bottom‐nav selections and
        //    a) pop up to that tab's root (clearing deeper stack)
        //    b) navigate to it if not already there
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            // pop everything above this destination
            navController.popBackStack(itemId, false);
            // if we aren't already at that destination, navigate to it
            if (navController.getCurrentDestination() == null
                    || navController.getCurrentDestination().getId() != itemId) {
                navController.navigate(itemId);
            }
            return true;
        });

        // 3) Keep the correct tab visually highlighted as we navigate
        navController.addOnDestinationChangedListener((ctrl, dest, args) -> {
            int destId = dest.getId();
            if (destId == R.id.navigation_home
                    || destId == R.id.navigation_dashboard
                    || destId == R.id.navigation_notifications) {
                // top‐level tabs
                bottomNav.getMenu().findItem(destId).setChecked(true);
            } else if (destId == R.id.deckDetailFragment
                    || destId == R.id.addDeckFragment) {
                // sub‐screens of “Zestawy” still highlight that tab
                bottomNav.getMenu()
                        .findItem(R.id.navigation_dashboard)
                        .setChecked(true);
            }
        });
    }

    /**
     * Handles the Up button in the ActionBar, delegating to the NavController.
     *
     * @return true if navigation was successful
     */
    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(
                        this,
                        R.id.nav_host_fragment_activity_main)
                .navigateUp() || super.onSupportNavigateUp();
    }
}
