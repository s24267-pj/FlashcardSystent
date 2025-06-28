package com.example.flashcardsystent.ui.learning;

// Import klas systemowych Androida potrzebnych do tworzenia interfejsu
import android.os.Bundle; // Klasa do przechowywania danych przekazywanych między komponentami
import android.view.LayoutInflater; // Umożliwia "nadmuchanie" (utworzenie) widoku z pliku XML
import android.view.View; // Klasa bazowa dla wszystkich widoków (elementów UI)
import android.view.ViewGroup; // Kontener dla widoków — może zawierać inne widoki
import android.widget.Button; // Przycisk, który użytkownik może kliknąć
import android.widget.TextView; // Pole tekstowe wyświetlające napisy

// Import adnotacji wskazujących na nullowalność (przydatne dla IDE i narzędzi analizy)
import androidx.annotation.NonNull; // Oznacza, że argument nie powinien być nullem
import androidx.annotation.Nullable; // Oznacza, że argument może być nullem

// Import klas związanych z Fragmentami
import androidx.fragment.app.Fragment; // Podstawowa klasa dla fragmentów (części UI)

// Import narzędzi do nawigacji między fragmentami
import androidx.navigation.Navigation; // Umożliwia przechodzenie między fragmentami za pomocą kontrolera nawigacji

// Import zasobów zdefiniowanych w pliku R (teksty, layouty, identyfikatory)
import com.example.flashcardsystent.R; // Główny plik zasobów (wygenerowany automatycznie przez Android Studio)

/**
 * Fragment wyświetlający podsumowanie sesji nauki klasycznej.
 * Pokazuje statystyki oraz umożliwia powrót do ekranu głównego.
 */
public class ClassicSummaryFragment extends Fragment {

    /**
     * Metoda tworząca ("dmuchająca") widok layoutu XML i zwracająca jego korzeń.
     * To tutaj wczytywany jest plik fragment_learning_summary.xml, który zawiera układ podsumowania.
     *
     * @param inflater obiekt używany do tworzenia widoków na podstawie XML-a
     * @param container opcjonalny rodzic widoku
     * @param savedInstanceState zapisany stan, jeśli istnieje
     * @return główny widok fragmentu
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Dmuchamy (tworzymy) layout fragmentu z XML i zwracamy jako widok fragmentu
        return inflater.inflate(R.layout.fragment_learning_summary, container, false);
    }

    /**
     * Metoda wywoływana po utworzeniu widoku. Uzupełnia teksty statystyk oraz ustawia przycisk powrotu do ekranu głównego.
     *
     * @param view główny widok fragmentu
     * @param savedInstanceState zapisany stan, jeśli istnieje
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Pobieramy argumenty przekazane do tego fragmentu (np. z poprzedniego ekranu)
        Bundle args = getArguments();

        // Wyciągamy wartości z Bundle, jeśli nie ma — ustawiamy wartości domyślne
        int total = args != null ? args.getInt("totalCount", 0) : 0;
        String hardFront = args != null ? args.getString("hardFront") : null;
        String hardBack = args != null ? args.getString("hardBack") : null;
        int hardCount = args != null ? args.getInt("hardCount", 0) : 0;
        int rate = args != null ? args.getInt("successRate", 0) : 0;

        // Znajdujemy pola tekstowe w layoucie, które będą wyświetlały statystyki
        TextView totalView = view.findViewById(R.id.text_total_cards);
        TextView hardestView = view.findViewById(R.id.text_hardest_card);
        TextView rateView = view.findViewById(R.id.text_success_rate);

        // Ustawiamy tekst z całkowitą liczbą fiszek (z użyciem stringów z zasobów)
        totalView.setText(getString(R.string.total_flashcards_with_value, total));

        // Jeśli istnieje "najtrudniejsza" karta — wyświetlamy ją wraz z liczbą błędnych odpowiedzi
        if (hardFront != null) {
            // Składamy tekst: przód + tył karty
            String cardText = hardFront + " - " + hardBack;
            // Wyświetlamy nazwę karty oraz liczbę błędów
            hardestView.setText(getString(R.string.hardest_card_with_value, cardText, hardCount));
        } else {
            // Jeśli nie było najtrudniejszej karty (np. wszystkie poprawne) — pokazujemy odpowiedni komunikat
            hardestView.setText(R.string.hardest_card_none);
        }

        // Ustawiamy procent poprawnych odpowiedzi (success rate)
        rateView.setText(getString(R.string.success_rate_with_value, rate));

        // Obsługa przycisku powrotu do strony głównej
        Button goHome = view.findViewById(R.id.button_back_home);
        goHome.setOnClickListener(v ->
                // Używamy nawigacji, aby przejść do fragmentu Home (navigation_home to ID celu w pliku nav_graph.xml)
                Navigation.findNavController(v).navigate(R.id.navigation_home)
        );
    }
}
