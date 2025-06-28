package com.example.flashcardsystent;

// Importy Android SDK
import android.os.Bundle; // Klasa służąca do przekazywania danych pomiędzy komponentami (np. przy obrotach ekranu)

// Importy z Android Jetpack
import androidx.appcompat.app.AppCompatActivity; // Bazowa klasa aktywności z paskiem akcji
import androidx.navigation.NavController; // Kontroler zarządzający nawigacją między fragmentami
import androidx.navigation.fragment.NavHostFragment; // Fragment przechowujący hosta nawigacji
import androidx.navigation.ui.NavigationUI; // Klasa ułatwiająca integrację elementów UI z kontrolerem nawigacji

// Importy aplikacji i bibliotek UI
import com.example.flashcardsystent.databinding.ActivityMainBinding; // ViewBinding - dostęp do widoków z layoutu
import com.google.android.material.bottomnavigation.BottomNavigationView; // Dolne menu nawigacyjne (BottomNavigation)

/**
 * Główna aktywność, która hostuje kontener nawigacyjny aplikacji.
 * Odpowiada za konfigurację nawigacji oraz dolnego paska menu.
 */
public class MainActivity extends AppCompatActivity {

    // Obiekt ViewBinding dający dostęp do widoków zadeklarowanych w activity_main.xml
    private ActivityMainBinding binding;

    // Kontroler obsługujący nawigację między fragmentami
    NavController navController;

    /**
     * Wywoływana przy tworzeniu aktywności. Inicjalizuje układ, hosta nawigacji
     * i zachowanie dolnego paska nawigacyjnego.
     *
     * @param savedInstanceState zapisany stan, jeśli wcześniej istniał
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Nadmuchanie (inflacja) layoutu i przypisanie bindingu
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Pobranie hosta nawigacji zdefiniowanego w XML i uzyskanie kontrolera
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_activity_main);
        navController = navHostFragment.getNavController();

        // Powiązanie dolnego menu nawigacyjnego z kontrolerem nawigacji
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Ręczne zaznaczenie odpowiedniej ikony w menu przy zmianie ekranu
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
