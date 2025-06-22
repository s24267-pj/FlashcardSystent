package com.example.flashcardsystent;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.flashcardsystent.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Pobierz kontroler nawigacji
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        navController = navHostFragment.getNavController();

        // Zdefiniuj główne zakładki (destynacje rootowe)
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_summary
        ).build();

        // Podłącz dolny pasek do kontrolera nawigacji
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Ustaw aktualne zaznaczenie przy zmianie fragmentu
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
