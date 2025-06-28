// Pakiet adapterów – czyli klas, które łączą dane z interfejsem użytkownika (RecyclerView)
package com.example.flashcardsystent.adapter;

// Importy – pozwalają używać klas potrzebnych do tworzenia widoków i obsługi listy
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flashcardsystent.R;         // Automatycznie generowana klasa, która zawiera odwołania do zasobów (layouty, ID itp.)
import com.example.flashcardsystent.data.Deck; // Klasa reprezentująca zestaw fiszek

import java.util.List; // Lista – struktura danych przechowująca wiele obiektów

/**
 * Adapter do RecyclerView, który wyświetla dostępne zestawy fiszek (Deck)
 * podczas wybierania zestawu do quizu lub nauki.
 */
public class QuizSetAdapter extends RecyclerView.Adapter<QuizSetAdapter.ViewHolder> {

    /**
     * Interfejs, który umożliwia nasłuchiwanie kliknięć na elementach listy.
     * Inna klasa (np. fragment) może go zaimplementować i zareagować na kliknięcie zestawu.
     */
    public interface OnItemClickListener {
        /**
         * Metoda wywoływana, gdy użytkownik kliknie dany zestaw.
         * @param deck obiekt zestawu, który został kliknięty
         */
        void onItemClick(Deck deck);
    }

    /** Lista zestawów do wyświetlenia */
    final List<Deck> sets;

    /** Obiekt nasłuchujący kliknięć */
    final OnItemClickListener listener;

    /**
     * Konstruktor adaptera – przypisuje dane i listener.
     * @param sets lista zestawów
     * @param listener obiekt reagujący na kliknięcia
     */
    public QuizSetAdapter(List<Deck> sets, OnItemClickListener listener) {
        this.sets = sets;         // Przypisujemy listę zestawów do pola klasy
        this.listener = listener; // Przypisujemy listener (reakcję na kliknięcia)
    }

    /**
     * Metoda tworząca nowy ViewHolder.
     * Wywoływana, gdy RecyclerView potrzebuje nowego kafelka (widoku zestawu).
     * @param parent rodzic (RecyclerView)
     * @param viewType typ widoku (tutaj nieużywany, bo mamy tylko jeden typ)
     * @return nowy obiekt ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tworzymy widok (kafelek) na podstawie layoutu XML item_deck.xml
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deck, parent, false);

        // Zwracamy nowy ViewHolder z tym widokiem
        return new ViewHolder(view);
    }

    /**
     * Metoda przypisująca dane do widoku na konkretnej pozycji w liście.
     * @param holder obiekt ViewHolder przechowujący widok
     * @param position pozycja w liście danych
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Pobieramy obiekt Deck z listy danych
        Deck deck = sets.get(position);

        // Szukamy pola tekstowego z layoutu (TextView z nazwą zestawu)
        TextView deckName = holder.itemView.findViewById(R.id.deck_name);

        // Ustawiamy nazwę zestawu w tym polu tekstowym
        deckName.setText(deck.name);

        // Ustawiamy reakcję na kliknięcie całego elementu listy
        holder.itemView.setOnClickListener(v -> listener.onItemClick(deck));
    }

    /**
     * Zwraca liczbę wszystkich elementów w liście.
     * Potrzebne do tego, by RecyclerView wiedział ile "kafelków" narysować.
     */
    @Override
    public int getItemCount() {
        return sets.size();
    }

    /**
     * Klasa ViewHolder – reprezentuje jeden "kafelek" w RecyclerView.
     * Przechowuje odniesienie do widoku, który został stworzony w `onCreateViewHolder()`.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * Konstruktor ViewHoldera – otrzymuje cały widok jednego zestawu.
         * @param itemView widok pojedynczego elementu (z layoutu item_deck.xml)
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView); // Przekazujemy widok do klasy nadrzędnej
        }
    }
}
