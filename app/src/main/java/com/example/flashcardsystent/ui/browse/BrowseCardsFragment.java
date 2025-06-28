// Pakiet, do którego należy ten fragment. Fragment ten odpowiada za przeglądanie fiszek przez użytkownika.
package com.example.flashcardsystent.ui.browse;

// Importujemy klasy potrzebne do działania fragmentu, przycisków, widoków itp.
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardsystent.R;
import com.example.flashcardsystent.adapter.BrowseCardAdapter;
import com.example.flashcardsystent.data.Card;
import com.example.flashcardsystent.viewmodel.CardViewModel;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * Fragment odpowiedzialny za tryb "przeglądania fiszek".
 * Pozwala użytkownikowi przeglądać po jednej fiszce naraz.
 * Gdy użytkownik odwróci fiszkę, po kilku sekundach zostaje ona wymieniona na następną z kolejki.
 */
public class BrowseCardsFragment extends Fragment {

    /** Identyfikator zestawu fiszek, który ma być przeglądany. Jest przekazywany do fragmentu przez argumenty. */
    private int deckId;

    /** ViewModel, który zapewnia dostęp do danych fiszek z bazy danych. */
    private CardViewModel cardViewModel;

    /** Kolejka kart, które czekają na wyświetlenie. Struktura FIFO – First In First Out. */
    private final Deque<Card> cardStack = new ArrayDeque<>();

    /** Lista kart aktualnie widocznych na ekranie (do 4 naraz). */
    private final List<Card> visibleCards = new ArrayList<>();

    /** Stała definiująca maksymalną liczbę kart widocznych jednocześnie. */
    private static final int MAX_VISIBLE = 4;

    /** RecyclerView, który wyświetla fiszki na ekranie. */
    private RecyclerView recyclerView;

    /** Przycisk kończący przeglądanie fiszek i przechodzący do podsumowania. */
    private Button finishButton;

    /** Adapter obsługujący logikę i wygląd fiszek w trybie przeglądania. */
    private BrowseCardAdapter adapter;

    /** Handler służący do opóźnienia (np. wymiana fiszki po 3 sekundach od odwrócenia). */
    private final Handler handler = new Handler(Looper.getMainLooper());

    /**
     * Metoda wywoływana przy tworzeniu widoku fragmentu.
     * Odpowiada za wczytanie pliku XML z układem graficznym.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Tutaj wczytujemy layout fragmentu z pliku fragment_browse_cards.xml
        return inflater.inflate(R.layout.fragment_browse_cards, container, false);
    }

    /**
     * Metoda wywoływana po utworzeniu widoku.
     * Ustawia ViewModel, nasłuchuje danych z bazy i ustawia przycisk zakończenia przeglądania.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Pobieramy ID zestawu z argumentów przekazanych do fragmentu
        deckId = getArguments().getInt("deckId", -1);

        // Inicjalizujemy widoki z layoutu XML
        recyclerView = view.findViewById(R.id.browse_recycler);
        finishButton = view.findViewById(R.id.button_finish_browse);

        // Tworzymy ViewModel – umożliwia to dostęp do danych Room bezpośrednio z UI
        cardViewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);

        // Obserwujemy dane z bazy – wszystkie karty w danym zestawie
        cardViewModel.getCardsByDeck(deckId).observe(getViewLifecycleOwner(), cards -> {
            // Czyścimy poprzednie dane
            cardStack.clear();
            visibleCards.clear();

            // Tasujemy karty losowo, aby użytkownik nie przeglądał ich zawsze w tej samej kolejności
            Collections.shuffle(cards);

            // Dodajemy wszystkie karty do kolejki
            cardStack.addAll(cards);

            // Wczytujemy maksymalnie 4 pierwsze karty do listy widocznych
            for (int i = 0; i < MAX_VISIBLE && !cardStack.isEmpty(); i++) {
                visibleCards.add(cardStack.pollFirst());
            }

            // Tworzymy adapter do obsługi RecyclerView, przekazując listę kart oraz callback do obsługi odwrócenia fiszki
            adapter = new BrowseCardAdapter(visibleCards, this::onCardFlipped);

            // RecyclerView używa GridLayoutManager z jedną kolumną (czyli działa jak lista pionowa)
            recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 1));

            // Ustawiamy adapter
            recyclerView.setAdapter(adapter);

            // Pokazujemy przycisk zakończenia przeglądania
            finishButton.setVisibility(View.VISIBLE);
        });

        // Ustawiamy działanie przycisku "Zakończ" – przechodzimy do fragmentu podsumowania
        finishButton.setOnClickListener(v ->
                Navigation.findNavController(view)
                        .navigate(R.id.action_browseCardsFragment_to_browseSummaryFragment)
        );
    }

    /**
     * Callback wywoływany, gdy użytkownik odwróci fiszkę (czyli zobaczy jej tył).
     * Po 3 sekundach wymieniamy tę kartę na następną z kolejki.
     *
     * @param flippedCard karta, która została właśnie odwrócona
     */
    private void onCardFlipped(Card flippedCard) {
        // Uruchamiamy opóźnione zadanie – wykona się po 3 sekundach (3000 milisekund)
        handler.postDelayed(() -> {
            // Jeśli są jeszcze karty w kolejce do wyświetlenia
            if (!cardStack.isEmpty()) {
                // Sprawdzamy indeks odwróconej karty w widocznej liście
                int index = visibleCards.indexOf(flippedCard);
                if (index != -1) {
                    // Usuwamy odwróconą kartę i wstawiamy w to miejsce następną z kolejki
                    visibleCards.remove(index);
                    visibleCards.add(index, cardStack.pollFirst());

                    // Odwrócona karta wraca na koniec kolejki (będzie jeszcze raz do obejrzenia)
                    cardStack.offerLast(flippedCard);

                    // Informujemy adapter, że dana karta się zmieniła i trzeba ją odświeżyć
                    adapter.notifyItemChanged(index);
                }
            }
        }, 3000); // 3 sekundy opóźnienia
    }
}
