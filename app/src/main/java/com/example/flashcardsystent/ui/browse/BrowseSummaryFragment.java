// Pakiet, w którym znajduje się ten fragment – należy do modułu przeglądania fiszek
package com.example.flashcardsystent.ui.browse;

// Importy potrzebnych klas Androida
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.flashcardsystent.R;

/**
 * Fragment wyświetlany na zakończenie sesji przeglądania fiszek.
 * Użytkownik może zdecydować, czy chce przeglądać ponownie, czy wrócić do ekranu głównego.
 */
public class BrowseSummaryFragment extends Fragment {

    /**
     * Tworzy i zwraca widok hierarchii fragmentu.
     * Metoda ta jest wywoływana automatycznie przy tworzeniu widoku fragmentu.
     *
     * @param inflater obiekt odpowiedzialny za zamianę pliku XML na strukturę widoków
     * @param container opcjonalny widok nadrzędny (rodzic)
     * @param savedInstanceState zapisany stan (np. przy obrocie ekranu)
     * @return główny widok interfejsu użytkownika dla fragmentu
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Ładujemy layout fragmentu z pliku fragment_browse_summary.xml
        return inflater.inflate(R.layout.fragment_browse_summary, container, false);
    }

    /**
     * Wywoływana po stworzeniu widoku fragmentu.
     * W tej metodzie przypisujemy przyciski i ustawiamy ich zachowanie.
     *
     * @param view widok zwrócony wcześniej przez onCreateView
     * @param savedInstanceState zapisany stan (jeśli był wcześniej zapisany)
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Przypisujemy przyciski z layoutu do zmiennych
        Button playAgain = view.findViewById(R.id.button_play_again);
        Button goHome = view.findViewById(R.id.button_go_home);

        // Obsługa kliknięcia przycisku „Zagraj ponownie” – użytkownik wraca do listy zestawów
        playAgain.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_browseSummaryFragment_to_browseSetListFragment));

        // Obsługa kliknięcia przycisku „Wróć do strony głównej” – użytkownik wraca do głównego panelu aplikacji
        goHome.setOnClickListener(v ->
                Navigation.findNavController(v)
                        .navigate(R.id.action_browseSummaryFragment_to_navigation_home));
    }
}
