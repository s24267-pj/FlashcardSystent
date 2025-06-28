package com.example.flashcardsystent.ui.summary;

// Importy Android SDK
import android.os.Bundle; // Klasa do przekazywania danych między komponentami (np. fragmentami)
import android.view.LayoutInflater; // Umożliwia "nadmuchiwanie" widoku z XML-a
import android.view.View; // Bazowa klasa widoku
import android.view.ViewGroup; // Kontener widoków (np. LinearLayout, FrameLayout)
import android.widget.TextView; // Klasa do wyświetlania tekstu

// Importy z Jetpacka
import androidx.activity.OnBackPressedCallback; // Umożliwia reagowanie na fizyczny przycisk "wstecz"
import androidx.annotation.NonNull; // Adnotacja mówiąca, że argument nie może być null
import androidx.fragment.app.Fragment; // Bazowa klasa fragmentów
import androidx.lifecycle.ViewModelProvider; // Umożliwia tworzenie i udostępnianie ViewModeli

// Import zasobów i klas aplikacji
import com.example.flashcardsystent.R; // Dostęp do zasobów aplikacji (np. tekstów, layoutów)
import com.example.flashcardsystent.databinding.FragmentSummaryBinding; // ViewBinding - bezpieczny dostęp do widoków
import com.example.flashcardsystent.viewmodel.SummaryViewModel; // ViewModel do zarządzania danymi statystyk

/**
 * Fragment prezentujący statystyki ukończonych quizów i klasycznych sesji nauki.
 * Pokazuje liczbę gier, poprawnych i błędnych odpowiedzi oraz wynik ostatniego quizu.
 */
public class SummaryFragment extends Fragment {

    // Obiekt ViewBinding dający bezpieczny dostęp do widoków z layoutu
    private FragmentSummaryBinding binding;

    /**
     * Tworzy widok ekranu podsumowania statystyk.
     *
     * @param inflater LayoutInflater do tworzenia widoków z XML-a
     * @param container Kontener, do którego widok może zostać dołączony (opcjonalnie)
     * @param savedInstanceState Stan zapisany wcześniej (np. po obrocie ekranu)
     * @return Główny widok fragmentu
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inicjalizacja ViewBinding do obsługi widoków
        binding = FragmentSummaryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * Metoda wywoływana po utworzeniu widoku. Ustawia obserwatorów i blokuje przycisk "wstecz".
     *
     * @param view Główny widok fragmentu
     * @param savedInstanceState Zapisany stan (jeśli istniał)
     */
    @Override
    public void onViewCreated(@NonNull View view,
                              Bundle savedInstanceState) {

        // Pobranie ViewModelu do ładowania statystyk quizu i klasycznych sesji
        SummaryViewModel viewModel = new ViewModelProvider(this).get(SummaryViewModel.class);

        // Obserwacja liczby rozegranych quizów
        viewModel.totalQuizzes.observe(getViewLifecycleOwner(), count ->
                binding.textTotalGames.setText(
                        getString(R.string.total_quizzes_with_value, count != null ? count : 0))
        );

        // Obserwacja liczby poprawnych odpowiedzi
        viewModel.correctAnswers.observe(getViewLifecycleOwner(), count ->
                binding.textTotalCorrect.setText(
                        getString(R.string.correct_answers_with_value, count != null ? count : 0))
        );

        // Obserwacja liczby błędnych odpowiedzi
        viewModel.wrongAnswers.observe(getViewLifecycleOwner(), count ->
                binding.textTotalWrong.setText(
                        getString(R.string.wrong_answers_with_value, count != null ? count : 0))
        );

        // Obserwacja ostatniego wyniku lub placeholdera jeśli brak danych
        TextView lastScore = binding.textLastScore;
        viewModel.lastResult.observe(getViewLifecycleOwner(), result -> {
            if (result != null) {
                lastScore.setText(
                        getString(R.string.last_result_summary, result.correct, result.wrong));
            } else {
                lastScore.setText(R.string.no_data); // Tekst zastępczy, gdy brak danych
            }
        });

        // Obserwacja liczby klasycznych sesji nauki
        viewModel.totalClassic.observe(getViewLifecycleOwner(), count ->
                binding.textTotalClassic.setText(
                        getString(R.string.classic_sessions_with_value, count != null ? count : 0))
        );

        // Blokowanie przycisku "wstecz" na tym ekranie
        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        // Brak działania - użytkownik nie może wrócić wstecz
                    }
                }
        );
    }

    /**
     * Sprzątanie po zniszczeniu widoku. Usuwamy referencję do ViewBinding.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
