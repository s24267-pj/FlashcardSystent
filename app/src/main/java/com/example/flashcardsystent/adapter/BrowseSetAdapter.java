// Deklaracja pakietu (czyli logicznego folderu w projekcie).
// Kod ten należy do pakietu "adapter", który prawdopodobnie zawiera inne adaptery.
package com.example.flashcardsystent.adapter;

// Importy – pozwalają używać klas z innych bibliotek (np. Androida, Javy, czy projektu).
import android.view.LayoutInflater; // Służy do "nadmuchiwania" layoutu XML do obiektu View.
import android.view.View;           // Bazowa klasa wszystkich elementów widocznych w Androidzie.
import android.view.ViewGroup;     // Klasa dla widoków, które mogą zawierać inne widoki (layouty).
import android.widget.TextView;    // Klasa do wyświetlania tekstu.

import androidx.annotation.NonNull;               // Adnotacja mówiąca, że wartość nie może być null.
import androidx.recyclerview.widget.RecyclerView; // RecyclerView = lista, która wyświetla dane w sposób wydajny.

import com.example.flashcardsystent.R;         // R to automatycznie generowana klasa zawierająca zasoby (layouty, stringi itd.)
import com.example.flashcardsystent.data.Deck; // Nasza klasa reprezentująca jeden zestaw fiszek.

import java.util.List; // Lista danych – przechowuje wiele obiektów, np. wiele zestawów fiszek.

/**
 * Adapter odpowiedzialny za wyświetlanie listy dostępnych zestawów (Decks)
 * w trybie przeglądania.
 * Użytkownik może kliknąć zestaw, aby zobaczyć jego fiszki.
 */
public class BrowseSetAdapter extends RecyclerView.Adapter<BrowseSetAdapter.DeckViewHolder> {

    /**
     * Interfejs (czyli "umowa") mówiąca: jeśli ktoś kliknie na zestaw,
     * to trzeba wykonać metodę `onDeckClick(Deck deck)`.
     * Można go użyć do poinformowania aktywności lub fragmentu, że coś kliknięto.
     */
    public interface OnDeckClickListener {
        /**
         * Ta metoda zostanie wywołana, gdy użytkownik kliknie w zestaw.
         * @param deck – obiekt reprezentujący kliknięty zestaw.
         */
        void onDeckClick(Deck deck);
    }

    /** Lista zestawów do wyświetlenia w RecyclerView. */
    private final List<Deck> decks;

    /** Obiekt nasłuchujący kliknięć w zestawy. */
    private final OnDeckClickListener listener;

    /**
     * Konstruktor – czyli metoda tworząca obiekt tego adaptera.
     * @param decks – lista obiektów Deck (czyli zestawów fiszek).
     * @param listener – obiekt, który będzie nasłuchiwał kliknięć.
     */
    public BrowseSetAdapter(List<Deck> decks, OnDeckClickListener listener) {
        this.decks = decks;       // Przypisujemy listę zestawów do pola klasy
        this.listener = listener; // Przypisujemy listener do pola klasy
    }

    /**
     * Metoda wywoływana przez RecyclerView, gdy musi utworzyć nowy widok zestawu.
     * @param parent – widok nadrzędny (RecyclerView).
     * @param viewType – typ widoku (tu zawsze jeden, więc ignorowany).
     * @return nowy obiekt typu DeckViewHolder.
     */
    @NonNull
    @Override
    public DeckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tworzymy obiekt View z layoutu XML o nazwie item_deck.xml
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deck, parent, false);

        // Tworzymy i zwracamy ViewHoldera – obiekt do obsługi pojedynczego elementu listy
        return new DeckViewHolder(view);
    }

    /**
     * Metoda przypisująca dane do konkretnego widoku (czyli wypełnia kartę danymi).
     * @param holder – obiekt widoku pojedynczego zestawu.
     * @param position – pozycja w liście (np. pierwszy zestaw, drugi, trzeci...).
     */
    @Override
    public void onBindViewHolder(@NonNull DeckViewHolder holder, int position) {
        // Pobieramy zestaw z listy po jego pozycji
        Deck deck = decks.get(position);

        // Ustawiamy nazwę zestawu w polu tekstowym
        holder.deckName.setText(deck.name);

        // Ustawiamy zachowanie po kliknięciu na cały element listy
        holder.itemView.setOnClickListener(v -> {
            // Po kliknięciu wywołujemy metodę listenera i przekazujemy mu kliknięty zestaw
            listener.onDeckClick(deck);
        });
    }

    /**
     * Metoda mówi, ile elementów jest w liście.
     * RecyclerView musi to wiedzieć, żeby odpowiednio narysować widoki.
     */
    @Override
    public int getItemCount() {
        return decks.size();
    }

    /**
     * Klasa DeckViewHolder – reprezentuje jeden zestaw w liście.
     * Przechowuje referencję do pola tekstowego (TextView) z nazwą zestawu.
     */
    static class DeckViewHolder extends RecyclerView.ViewHolder {

        /** Pole tekstowe, w którym pokazujemy nazwę zestawu. */
        final TextView deckName;

        /**
         * Konstruktor widoku pojedynczego zestawu.
         * @param itemView – widok (layout) jednego elementu listy (czyli jednej "kafelkowej" karty).
         */
        public DeckViewHolder(@NonNull View itemView) {
            super(itemView); // Wywołanie konstruktora klasy nadrzędnej

            // Szukamy elementu z layoutu o ID "deck_name" – to TextView z nazwą zestawu
            deckName = itemView.findViewById(R.id.deck_name);
        }
    }
}
