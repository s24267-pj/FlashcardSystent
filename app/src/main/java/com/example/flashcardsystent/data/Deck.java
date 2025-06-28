// Pakiet danych – zawiera modele danych, które są używane przez Room do tworzenia bazy danych
package com.example.flashcardsystent.data;

// Importujemy adnotacje Room, które informują jak traktować klasę jako tabelę w bazie
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Klasa `Deck` reprezentuje **zestaw fiszek** – czyli grupę powiązanych kart (np. słówek).
 * Jest to tzw. **encja** Room, co oznacza, że zostanie zamieniona na **tabelę w bazie danych**.
 * Jeden zestaw (deck) może zawierać wiele fiszek (Card), które są z nim powiązane przez `deckId`.
 */
@Entity
public class Deck {

    /**
     * Identyfikator zestawu fiszek.
     * To pole pełni rolę **klucza głównego** (Primary Key) w tabeli.
     * `autoGenerate = true` oznacza, że Room sam będzie przydzielał kolejne numery ID (np. 1, 2, 3...).
     */
    @PrimaryKey(autoGenerate = true)
    public int id;

    /**
     * Nazwa zestawu fiszek.
     * Jest to tekst wprowadzany przez użytkownika, np. "Angielski - czasowniki".
     * Będzie wyświetlana w interfejsie użytkownika.
     */
    public String name;

    /**
     * Konstruktor, który tworzy nowy obiekt `Deck`.
     * Wymaga podania tylko nazwy zestawu – identyfikator `id` zostanie wygenerowany automatycznie przez Room.
     *
     * @param name nazwa zestawu fiszek (np. "Niemiecki - przymiotniki")
     */
    public Deck(String name) {
        this.name = name;  // przypisujemy nazwę do pola klasy
    }
}
