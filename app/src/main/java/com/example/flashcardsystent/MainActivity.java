package com.example.flashcardsystent;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.flashcardsystent.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController; // 👈 dodaj to

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        navController = navHostFragment.getNavController();

        binding.navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                navController.popBackStack(R.id.navigation_home, false);
                navController.navigate(R.id.navigation_home);
                return true;
            } else if (itemId == R.id.navigation_dashboard) {
                navController.popBackStack(R.id.navigation_dashboard, false);
                navController.navigate(R.id.navigation_dashboard);
                return true;
            } else if (itemId == R.id.navigation_notifications) {
                navController.popBackStack(R.id.navigation_notifications, false);
                navController.navigate(R.id.navigation_notifications);
                return true;
            }

            return false;
        });

    }
}
