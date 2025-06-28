// Pakiet, w którym znajduje się ten plik – organizuje klasy w logiczne "foldery".
package com.example.flashcardsystent.adapter;

// Importujemy klasy potrzebne do działania adaptera i interfejsu użytkownika.
import android.view.LayoutInflater; // Służy do tworzenia (inflate) widoków z plików XML.
import android.view.View;           // Bazowa klasa wszystkich widocznych elementów interfejsu.
import android.view.ViewGroup;     // Grupa widoków (np. LinearLayout) – może zawierać inne widoki.
import android.widget.TextView;    // Klasa do wyświetlania tekstu na ekranie.

import androidx.annotation.NonNull;               // Adnotacja: oznacza, że dana wartość nie może być `null`.
import androidx.recyclerview.widget.DiffUtil;     // Klasa do porównywania różnic między elementami listy.
import androidx.recyclerview.widget.ListAdapter;  // Wersja adaptera obsługująca automatyczne różnice w danych.
import androidx.recyclerview.widget.RecyclerView; // RecyclerView = wydajna lista przewijana.

import com.example.flashcardsystent.R;         // Klasa R – automatycznie generowana, zawiera odwołania do zasobów XML.
import com.example.flashcardsystent.data.Card; // Nasz model danych – klasa reprezentująca pojedynczą fiszkę.

/**
 * Klasa adaptera do RecyclerView, która wyświetla fiszki (`Card`) w widoku listy.
 * Dziedziczy po `ListAdapter`, czyli adapterze, który potrafi automatycznie aktualizować widok,
 * gdy zmienią się dane.
 */
public class CardAdapter extends ListAdapter<Card, CardAdapter.CardVH> {

    /** Interfejs nasłuchujący kliknięć na fiszki. */
    private final OnCardClickListener clickListener;

    /**
     * Konstruktor adaptera. Wywoływany, gdy tworzymy nowy adapter.
     * @param clickListener – obiekt, który "nasłuchuje" kliknięć w fiszki.
     */
    public CardAdapter(OnCardClickListener clickListener) {
        super(DIFF_CALLBACK); // Wywołujemy konstruktor klasy ListAdapter, podając obiekt DIFF_CALLBACK.
        // Dzięki temu adapter będzie inteligentnie porównywał zmiany w liście.
        this.clickListener = clickListener; // Przypisujemy listener do pola klasy.
    }

    /**
     * Tworzy nowy ViewHolder (widok pojedynczego elementu listy).
     * @param parent – widok nadrzędny (RecyclerView).
     * @param viewType – typ widoku (tutaj nieużywany).
     */
    @NonNull
    @Override
    public CardVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Używamy LayoutInflater do przekształcenia layoutu XML w rzeczywisty obiekt View.
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false); // "Nadmuchujemy" layout jednej fiszki
        return new CardVH(v); // Zwracamy nowy obiekt CardVH (czyli ViewHolder dla tej fiszki)
    }

    /**
     * Przypisuje dane do widoku dla danej pozycji.
     * @param holder – obiekt, który "trzyma" widok jednej fiszki.
     * @param position – numer pozycji w liście (np. 0, 1, 2...).
     */
    @Override
    public void onBindViewHolder(@NonNull CardVH holder, int position) {
        // getItem(position) pobiera obiekt Card z pozycji w liście
        Card c = getItem(position);

        // Ustawiamy tekst na przedzie i tyle fiszki
        holder.front.setText(c.front);
        holder.back.setText(c.back);

        // Ustawiamy reakcję na kliknięcie całego widoku fiszki
        holder.itemView.setOnClickListener(v -> clickListener.onCardClick(c));
    }

    /**
     * Dodatkowa metoda – pozwala pobrać fiszkę z konkretnej pozycji.
     * Przydatne np. do testów jednostkowych albo edycji konkretnej fiszki.
     */
    public Card getItemAt(int position) {
        return getItem(position); // Zwracamy obiekt Card z podanej pozycji
    }

    /**
     * Interfejs opisujący, co zrobić, gdy klikniemy fiszkę.
     */
    public interface OnCardClickListener {
        /**
         * Metoda wywoływana, gdy klikniemy fiszkę.
         * @param card – obiekt klikniętej fiszki.
         */
        void onCardClick(Card card);
    }

    /**
     * ViewHolder – klasa pomocnicza, która przechowuje referencje do widoków
     * jednej pozycji w RecyclerView (tutaj: przód i tył fiszki).
     */
    static class CardVH extends RecyclerView.ViewHolder {
        /** Pola tekstowe do wyświetlania przodu i tyłu fiszki. */
        final TextView front, back;

        /**
         * Konstruktor ViewHoldera.
         * @param itemView – cały widok jednej fiszki.
         */
        CardVH(@NonNull View itemView) {
            super(itemView); // Wywołujemy konstruktor klasy bazowej
            front = itemView.findViewById(R.id.text_front); // Znajdujemy TextView z przodem fiszki
            back = itemView.findViewById(R.id.text_back);   // Znajdujemy TextView z tyłem fiszki
        }
    }

    /**
     * Obiekt, który służy do inteligentnego porównywania dwóch fiszek.
     * Dzięki temu RecyclerView wie, które elementy się zmieniły i nie musi odświeżać całej listy.
     */
    private static final DiffUtil.ItemCallback<Card> DIFF_CALLBACK = new DiffUtil.ItemCallback<Card>() {
        /**
         * Porównuje, czy dwie fiszki mają ten sam identyfikator (czy to ten sam obiekt).
         */
        @Override
        public boolean areItemsTheSame(@NonNull Card oldItem, @NonNull Card newItem) {
            return oldItem.id == newItem.id; // Porównujemy po polu `id`
        }

        /**
         * Porównuje, czy zawartość dwóch fiszek jest taka sama (czy przód i tył się nie zmieniły).
         */
        @Override
        public boolean areContentsTheSame(@NonNull Card oldItem, @NonNull Card newItem) {
            return oldItem.front.equals(newItem.front) &&
                    oldItem.back.equals(newItem.back);
        }
    };
}
