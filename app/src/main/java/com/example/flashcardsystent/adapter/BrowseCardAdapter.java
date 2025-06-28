// To jest deklaracja pakietu, czyli logicznego "folderu" w projekcie.
// Pozwala na uporządkowanie plików w projekcie.
// Nazwa pakietu odpowiada strukturze folderów w katalogu projektu.
package com.example.flashcardsystent.adapter;

// Poniżej znajdują się importy, czyli włączenie klas z innych bibliotek.
// Dzięki nim możemy korzystać z funkcjonalności, które nie są wbudowane w ten plik.
import android.view.LayoutInflater; // Klasa, która pozwala "nadmuchać" (inflate) layout XML do obiektów View.
import android.view.View;           // Podstawowa klasa reprezentująca każdy element interfejsu w Androidzie.
import android.view.ViewGroup;     // Specjalny typ widoku, który może zawierać inne widoki (np. LinearLayout).
import android.widget.TextView;    // Widok służący do wyświetlania tekstu.

import androidx.annotation.NonNull;               // Adnotacja informująca, że wartość nie może być null.
import androidx.recyclerview.widget.RecyclerView; // RecyclerView to specjalna lista przewijana w Androidzie.

import com.example.flashcardsystent.R;         // Klasa automatycznie generowana – umożliwia dostęp do zasobów jak layouty, stringi, ID.
import com.example.flashcardsystent.data.Card; // Nasza klasa reprezentująca pojedynczą fiszkę.

import java.util.List; // Lista w Javie – przechowuje wiele elementów (np. wiele fiszek).

/**
 * Klasa BrowseCardAdapter to tzw. adapter RecyclerView.
 * Adapter to element pośredniczący między danymi (lista fiszek)
 * a elementem graficznym, który je wyświetla (czyli RecyclerView).
 *
 * RecyclerView.Adapter<> to generyczna klasa – typ w nawiasach ostrych <>
 * mówi, jakiego rodzaju ViewHolder będzie używany.
 * Tutaj: BrowseCardAdapter.ViewHolder
 */
public class BrowseCardAdapter extends RecyclerView.Adapter<BrowseCardAdapter.ViewHolder> {

    /**
     * To jest tzw. interfejs. Można go porównać do "umowy".
     * Mówi, że każda klasa, która go zaimplementuje, musi zawierać
     * metodę `onCardFlipped(Card card)`.
     *
     * Będziemy z niego korzystać, żeby np. powiadomić inne części aplikacji,
     * że użytkownik kliknął i "przewrócił" kartę.
     */
    public interface OnCardFlipListener {
        void onCardFlipped(Card card); // Metoda wywoływana po obróceniu fiszki
    }

    // Lista przechowująca obiekty Card – czyli nasze fiszki.
    private final List<Card> cards;

    // Obiekt nasłuchujący, czy karta została obrócona.
    // Typ to interfejs OnCardFlipListener, który sami zdefiniowaliśmy wyżej.
    private final OnCardFlipListener flipListener;

    /**
     * Konstruktor klasy BrowseCardAdapter.
     * Konstruktor to specjalna metoda wywoływana w momencie tworzenia nowego obiektu tej klasy.
     * @param cards – lista fiszek, które chcemy wyświetlić.
     * @param flipListener – obiekt, który zostanie powiadomiony, gdy użytkownik obróci kartę.
     */
    public BrowseCardAdapter(List<Card> cards, OnCardFlipListener flipListener) {
        this.cards = cards; // this.cards – odnosi się do pola klasy; = cards (argument przekazany do konstruktora)
        this.flipListener = flipListener;
    }

    /**
     * Ta metoda tworzy nowy ViewHolder – czyli "opakowanie" na jeden element listy.
     * @param parent – widok nadrzędny (czyli RecyclerView).
     * @param viewType – typ widoku (tutaj nie używany, ale wymagany przez interfejs).
     * @return nowy ViewHolder z załadowanym layoutem item_flashcard.xml
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater to klasa, która zamienia plik XML (layout) w prawdziwy obiekt View.
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flashcard, parent, false);
        // Tworzymy i zwracamy nowy ViewHolder z widokiem karty
        return new ViewHolder(view);
    }

    /**
     * Metoda przypisuje dane do widoku. Wywoływana, gdy element listy ma być wyświetlony.
     * @param holder – ViewHolder, który ma być uzupełniony.
     * @param position – pozycja elementu w liście.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Pobieramy kartę z listy po jej pozycji
        Card card = cards.get(position);
        // Przekazujemy kartę do holdera, żeby ją wyświetlił
        holder.bind(card);
    }

    /**
     * Zwraca liczbę elementów w liście.
     * RecyclerView musi wiedzieć, ile ma ich narysować.
     */
    @Override
    public int getItemCount() {
        return cards.size();
    }

    /**
     * Klasa ViewHolder – reprezentuje jeden element listy.
     * Dziedziczy po RecyclerView.ViewHolder.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        // Tekst na karcie – widok TextView do wyświetlania treści fiszki.
        private final TextView textView;

        // Flaga mówiąca, czy karta została obrócona (true = pokazuje tył, false = przód)
        private boolean isFlipped = false;

        /**
         * Konstruktor ViewHoldera – otrzymuje widok i szuka w nim TextView.
         * @param view – cały widok jednej fiszki (stworzonej na podstawie layoutu XML)
         */
        ViewHolder(View view) {
            // Wywołujemy konstruktor klasy bazowej (RecyclerView.ViewHolder)
            super(view);

            // Szukamy w przekazanym widoku elementu o ID flashcard_text
            // czyli tekstu fiszki
            textView = view.findViewById(R.id.flashcard_text);
        }

        /**
         * Metoda przypisuje konkretne dane (tekst) do widoku karty.
         * Ustawia też zachowanie po kliknięciu.
         * @param card – obiekt Card zawierający tekst przodu i tyłu fiszki.
         */
        void bind(Card card) {
            isFlipped = false; // Na początek karta pokazuje przód
            textView.setText(card.front); // Ustawiamy tekst przodu karty

            // Ustawiamy reakcję na kliknięcie w kartę
            textView.setOnClickListener(v -> {
                // Zmieniamy wartość isFlipped na przeciwną (! oznacza negację logiczną)
                isFlipped = !isFlipped;

                // Ustawiamy tekst – jeśli obrócone, pokaż tył; jeśli nie, pokaż przód
                textView.setText(isFlipped ? card.back : card.front);

                // Jeżeli karta została obrócona (czyli pokazuje tył) i flipListener istnieje (nie jest null),
                // to powiadamiamy listenera o tym zdarzeniu
                if (isFlipped && flipListener != null) {
                    flipListener.onCardFlipped(card);
                }
            });
        }
    }
}
