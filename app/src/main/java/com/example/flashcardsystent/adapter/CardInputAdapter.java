// Pakiet adapterów – czyli klas pośredniczących między danymi a widokiem
package com.example.flashcardsystent.adapter;

// Importujemy klasy potrzebne do obsługi tekstu, widoków i listy
import android.text.Editable;        // Reprezentuje edytowalny tekst (np. w EditText)
import android.text.TextWatcher;     // Interfejs – pozwala „nasłuchiwać” zmian w polu tekstowym
import android.view.LayoutInflater;  // Służy do tworzenia widoków z plików XML
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;       // Pole tekstowe, które można edytować

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView; // Lista przewijalna

import com.example.flashcardsystent.R;           // Dostęp do zasobów XML (np. layoutów, ID)
import com.example.flashcardsystent.data.Card;   // Model danych: jedna fiszka

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter do RecyclerView wyświetlający wiersze do wprowadzania fiszek (Card).
 * Każdy wiersz ma dwa pola tekstowe: przód i tył fiszki.
 * Gdy użytkownik wypełni ostatni wiersz – automatycznie dodawany jest nowy pusty.
 */
public class CardInputAdapter extends RecyclerView.Adapter<CardInputAdapter.CardInputVH> {

    /** Lista fiszek aktualnie wpisywanych – po jednej na każdy wiersz widoczny na ekranie */
    private final List<Card> items = new ArrayList<>();

    /** Identyfikator zestawu, do którego mają należeć tworzone fiszki */
    private final int deckId;

    /**
     * Konstruktor adaptera. Wywoływany przy tworzeniu nowego adaptera.
     * @param deckId identyfikator zestawu, do którego będą należały fiszki
     */
    public CardInputAdapter(int deckId) {
        this.deckId = deckId;

        // Na początek dodajemy 3 puste fiszki – żeby użytkownik miał miejsce do wpisania danych
        for (int i = 0; i < 3; i++) {
            items.add(new Card(deckId, "", "")); // Fiszka z pustym przodem i tyłem
        }
    }

    /**
     * Tworzy nowy widok (wiersz) z layoutu item_card_input.xml
     */
    @NonNull
    @Override
    public CardInputVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_input, parent, false); // "Nadmuchujemy" layout jednej fiszki
        return new CardInputVH(v); // Zwracamy nowy ViewHolder
    }

    /**
     * Wypełnia dane w widoku i ustawia logikę obsługi zmian tekstu
     */
    @Override
    public void onBindViewHolder(@NonNull CardInputVH holder, int position) {
        Card initialCard = items.get(position); // Pobieramy fiszkę z listy
        holder.front.setText(initialCard.front); // Wpisujemy tekst przodu
        holder.back.setText(initialCard.back);   // Wpisujemy tekst tyłu

        // Nasłuchiwanie zmian w polu „przód fiszki”
        holder.front.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int pos = holder.getAdapterPosition(); // Pobieramy aktualną pozycję
                if (pos == RecyclerView.NO_POSITION) return; // Bezpiecznik: jeśli coś poszło nie tak – przerwij

                Card card = items.get(pos); // Pobieramy odpowiednią fiszkę
                card.front = holder.front.getText().toString().trim(); // Zapisujemy zmiany do obiektu
                maybeAddNewRow(pos); // Sprawdzamy, czy trzeba dodać nowy pusty wiersz
            }
        });

        // Nasłuchiwanie zmian w polu „tył fiszki”
        holder.back.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int pos = holder.getAdapterPosition();
                if (pos == RecyclerView.NO_POSITION) return;

                Card card = items.get(pos);
                card.back = holder.back.getText().toString().trim();
                maybeAddNewRow(pos);
            }
        });
    }

    /**
     * Zwraca liczbę wszystkich elementów w liście – RecyclerView musi to wiedzieć
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Sprawdza, czy użytkownik wypełnił ostatni wiersz.
     * Jeśli tak, to dodaje nowy pusty wiersz na końcu.
     * @param pos – indeks wiersza, w którym coś zostało zmienione
     */
    private void maybeAddNewRow(int pos) {
        // Sprawdzamy, czy zmiana dotyczy ostatniego wiersza
        if (pos == items.size() - 1) {
            Card last = items.get(pos);

            // Jeśli oba pola (przód i tył) są wypełnione – dodajemy nową pustą fiszkę
            if (!last.front.isEmpty() && !last.back.isEmpty()) {
                items.add(new Card(deckId, "", "")); // Dodajemy nową fiszkę z pustymi polami
                notifyItemInserted(items.size() - 1); // Informujemy RecyclerView, że coś się zmieniło
            }
        }
    }

    /**
     * Zwraca tylko te fiszki, które mają coś wpisane po obu stronach.
     * (czyli użytkownik je faktycznie uzupełnił)
     */
    public List<Card> getFilledCards() {
        List<Card> out = new ArrayList<>();
        for (Card c : items) {
            if (!c.front.isEmpty() && !c.back.isEmpty()) {
                out.add(c); // Dodajemy tylko wypełnione fiszki
            }
        }
        return out;
    }

    /**
     * ViewHolder – przechowuje referencje do pól tekstowych w jednym wierszu edycji fiszki
     */
    public static class CardInputVH extends RecyclerView.ViewHolder {
        /** Pola tekstowe EditText do wpisania przodu i tyłu fiszki */
        final EditText front, back;

        /**
         * Konstruktor – inicjuje pola tekstowe
         */
        CardInputVH(@NonNull View itemView) {
            super(itemView);
            front = itemView.findViewById(R.id.edit_front); // Pole tekstowe na przód fiszki
            back = itemView.findViewById(R.id.edit_back);   // Pole tekstowe na tył fiszki
        }
    }

    /**
     * Klasa pomocnicza upraszczająca implementację TextWatcher.
     * Dzięki niej nie musimy implementować wszystkich 3 metod – tylko te, które nas interesują.
     */
    private abstract static class SimpleTextWatcher implements TextWatcher {
        @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {} // Nie używane
        @Override public void afterTextChanged(Editable s) {} // Nie używane
    }
}
