// Pakiet, w którym znajduje się adapter. Adaptery łączą dane z widokiem (RecyclerView).
package com.example.flashcardsystent.adapter;

// Importujemy klasy potrzebne do tworzenia widoków i adapterów
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;     // Umożliwia inteligentne śledzenie zmian danych
import androidx.recyclerview.widget.ListAdapter; // Adapter oparty na różnicach (DiffUtil)

import com.example.flashcardsystent.R;         // Zasoby XML (np. ID widoków, layouty)
import com.example.flashcardsystent.data.Deck; // Klasa reprezentująca zestaw fiszek (Deck)

/**
 * Adapter wyświetlający zestawy fiszek utworzone przez użytkownika
 * w ekranach zarządzania (ManagementFragment).
 * Obsługuje kliknięcia i dłuższe kliknięcia (long click).
 */
public class DeckAdapter extends ListAdapter<Deck, DeckAdapter.DeckViewHolder> {

    /** Interfejs słuchacza – pozwala obsłużyć kliknięcia na zestawach */
    private final OnDeckClickListener listener;

    /**
     * Konstruktor adaptera.
     * @param listener obiekt, który będzie reagował na kliknięcia zestawów
     */
    public DeckAdapter(OnDeckClickListener listener) {
        // Wywołanie konstruktora klasy ListAdapter z narzędziem DIFF_CALLBACK,
        // które automatycznie porównuje stare i nowe dane (dla wydajności)
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    /**
     * Tworzy nowy ViewHolder, który "opakowuje" widok pojedynczego zestawu.
     * Wywoływana, gdy RecyclerView potrzebuje nowego widoku.
     */
    @NonNull
    @Override
    public DeckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater zamienia layout XML (item_deck.xml) w obiekt View
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deck, parent, false);

        // Tworzymy i zwracamy ViewHolder
        return new DeckViewHolder(itemView);
    }

    /**
     * Przypisuje dane do ViewHoldera (czyli wypełnia kafelek widoku zestawu).
     */
    @Override
    public void onBindViewHolder(@NonNull DeckViewHolder holder, int position) {
        // Pobieramy obiekt Deck z listy na pozycji `position`
        Deck currentDeck = getItem(position);

        // Ustawiamy nazwę zestawu w polu tekstowym
        holder.textViewName.setText(currentDeck.name);

        // Obsługa kliknięcia zestawu – np. przejście do edycji lub przeglądu fiszek
        holder.itemView.setOnClickListener(v ->
                listener.onDeckClick(currentDeck)
        );

        // Obsługa dłuższego kliknięcia – np. pokazanie opcji "usuń", "zmień nazwę"
        holder.itemView.setOnLongClickListener(v -> {
            listener.onDeckLongClick(currentDeck); // Wywołujemy metodę listenera
            return true; // Zwracamy `true`, żeby system wiedział, że kliknięcie zostało obsłużone
        });
    }

    /**
     * Interfejs obsługujący kliknięcia i dłuższe kliknięcia zestawu.
     */
    public interface OnDeckClickListener {
        /**
         * Wywoływane, gdy zestaw został kliknięty (zwykłe kliknięcie).
         */
        void onDeckClick(Deck deck);

        /**
         * Wywoływane, gdy zestaw został kliknięty dłużej (long click).
         */
        void onDeckLongClick(Deck deck);
    }

    /**
     * Klasa ViewHolder przechowująca referencje do widoków jednego kafelka zestawu.
     * Dzięki temu nie musimy szukać tych widoków za każdym razem od nowa.
     */
    static class DeckViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        /** Tekst z nazwą zestawu */
        private final TextView textViewName;

        /**
         * Konstruktor ViewHoldera – przypisuje widoki z layoutu do zmiennych
         */
        public DeckViewHolder(@NonNull View itemView) {
            super(itemView);
            // Znajdujemy TextView po ID z layoutu item_deck.xml
            textViewName = itemView.findViewById(R.id.deck_name);
        }
    }

    /**
     * DIFF_CALLBACK to narzędzie do porównywania danych.
     * Dzięki temu RecyclerView odświeża tylko to, co się naprawdę zmieniło.
     */
    private static final DiffUtil.ItemCallback<Deck> DIFF_CALLBACK = new DiffUtil.ItemCallback<Deck>() {

        /**
         * Porównuje, czy dwa obiekty Deck to ten sam zestaw (np. po ID).
         */
        @Override
        public boolean areItemsTheSame(@NonNull Deck oldItem, @NonNull Deck newItem) {
            return oldItem.id == newItem.id;
        }

        /**
         * Porównuje, czy zawartość dwóch zestawów jest taka sama (np. czy zmieniła się nazwa).
         */
        @Override
        public boolean areContentsTheSame(@NonNull Deck oldItem, @NonNull Deck newItem) {
            return oldItem.name.equals(newItem.name);
        }
    };
}
