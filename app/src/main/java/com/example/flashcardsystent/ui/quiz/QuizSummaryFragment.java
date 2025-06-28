package com.example.flashcardsystent.ui.quiz;

// Importy Android SDK
import android.os.Bundle; // Klasa do przekazywania danych między komponentami
import android.view.LayoutInflater; // Umożliwia "nadmuchiwanie" widoku z XML-a
import android.view.View; // Klasa bazowa każdego widoku na ekranie
import android.view.ViewGroup; // Kontener widoków (np. LinearLayout, FrameLayout)
import android.widget.Button; // Klasa przycisku
import android.widget.TextView; // Klasa tekstu

// Importy z Jetpacka
import androidx.annotation.NonNull; // Adnotacja mówiąca, że parametr nie może być null
import androidx.annotation.Nullable; // Adnotacja mówiąca, że parametr może być null
import androidx.fragment.app.Fragment; // Bazowa klasa fragmentów
import androidx.navigation.Navigation; // Umożliwia nawigację między fragmentami

// Import zasobów aplikacji
import com.example.flashcardsystent.R; // Dostęp do zasobów takich jak teksty i identyfikatory widoków

/**
 * Fragment wyświetlany po zakończeniu quizu.
 * Pokazuje wynik i daje użytkownikowi możliwość powrotu do ekranu głównego lub przejścia do statystyk.
 */
public class QuizSummaryFragment extends Fragment {

    // Liczba poprawnych odpowiedzi udzielonych przez użytkownika
    private int correctCount;

    // Całkowita liczba pytań w quizie
    private int totalCount;

    /**
     * Tworzy i zwraca widok fragmentu z pliku XML fragment_quiz_summary.xml
     *
     * @param inflater Obiekt do tworzenia widoków z XML-a
     * @param container Rodzic (może być null), do którego fragment może zostać dołączony
     * @param savedInstanceState Stan zapisany wcześniej (np. przy rotacji ekranu)
     * @return Główny widok fragmentu
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz_summary, container, false);
    }

    /**
     * Wywoływana po tym, jak widok został już utworzony.
     * Odczytuje dane z argumentów i ustawia interakcje dla przycisków.
     *
     * @param view Główny widok fragmentu
     * @param savedInstanceState Zapisany stan, jeśli fragment był wcześniej zniszczony
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Pobieramy argumenty przekazane do fragmentu (wyniki quizu)
        Bundle args = getArguments();
        if (args != null) {
            correctCount = args.getInt("correctCount", 0); // liczba poprawnych odpowiedzi
            totalCount = args.getInt("totalCount", 0); // liczba wszystkich pytań
        }

        // Pobieramy referencje do elementów interfejsu
        TextView resultText = view.findViewById(R.id.text_result); // tekst z wynikiem
        Button buttonBack = view.findViewById(R.id.button_back_home); // przycisk powrotu do ekranu głównego
        Button buttonStats = view.findViewById(R.id.button_view_stats); // przycisk przejścia do statystyk

        // Ustawiamy sformatowany tekst z wynikiem quizu, np. "Wynik: 7/10"
        resultText.setText(getString(R.string.quiz_result_summary, correctCount, totalCount));

        // Po kliknięciu: wróć do ekranu głównego
        buttonBack.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.navigation_home)
        );

        // Po kliknięciu: przejdź do panelu statystyk, usuwając z historii wszystkie wcześniejsze ekrany
        buttonStats.setOnClickListener(v -> Navigation.findNavController(v).navigate(
                R.id.navigation_summary, // ID miejsca docelowego (fragment z podsumowaniem)
                null, // brak dodatkowych danych
                new androidx.navigation.NavOptions.Builder()
                        .setPopUpTo(R.id.navigation_home, true) // usuń wszystko aż do ekranu głównego
                        .setLaunchSingleTop(true) // jeśli już jesteś na ekranie docelowym, nie twórz nowego
                        .build()
        ));
    }
}
