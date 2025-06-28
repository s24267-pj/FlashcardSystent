// Pakiet danych – zawiera klasy odpowiadające tabelom w bazie danych Room
package com.example.flashcardsystent.data;

// Importujemy adnotacje Room – będą przekształcone w strukturę bazy danych
import androidx.room.Entity;         // Oznacza klasę jako tabelę w bazie danych
import androidx.room.ForeignKey;    // Definiuje relację klucza obcego
import androidx.room.Index;         // Pozwala tworzyć indeksy w tabeli (dla wydajności zapytań)
import androidx.room.PrimaryKey;    // Oznacza kolumnę jako klucz główny (primary key)

/**
 * Klasa `Card` reprezentuje jedną fiszkę i zostaje przekształcona przez Room w tabelę bazy danych.
 * Każda fiszka zawiera tekst z przodu i z tyłu, i należy do konkretnego zestawu (Deck).
 */
@Entity(
        tableName = "Card", // Nazwa tabeli w SQLite – duża litera nie jest wymagana, ale dozwolona
        foreignKeys = @ForeignKey(
                entity = Deck.class,        // Określa tabelę nadrzędną (do której należy ta encja) – Deck
                parentColumns = "id",       // Kolumna w tabeli nadrzędnej (Deck), która jest kluczem głównym
                childColumns = "deckId",    // Kolumna w tej tabeli, która przechowuje klucz obcy
                onDelete = ForeignKey.CASCADE // Jeśli Deck zostanie usunięty, wszystkie powiązane fiszki zostaną też usunięte
        ),
        indices = @Index("deckId") // Tworzy indeks na kolumnie deckId – przyspiesza zapytania filtrujące po zestawie
)
public class Card {

    /**
     * Unikalny identyfikator fiszki – klucz główny (Primary Key).
     * `autoGenerate = true` – Room automatycznie wygeneruje kolejne ID.
     */
    @PrimaryKey(autoGenerate = true)
    public int id;

    /**
     * Klucz obcy – ID zestawu (Deck), do którego ta fiszka należy.
     * Dzięki temu można pobrać wszystkie fiszki przypisane do konkretnego zestawu.
     */
    public int deckId;

    /**
     * Tekst wyświetlany na przedniej stronie fiszki.
     */
    public String front;

    /**
     * Tekst wyświetlany na tylnej stronie fiszki.
     */
    public String back;

    /**
     * Konstruktor niestandardowy – używany w kodzie aplikacji do tworzenia nowych obiektów fiszek.
     * @param deckId identyfikator zestawu, do którego należy fiszka
     * @param front tekst z przodu fiszki
     * @param back tekst z tyłu fiszki
     */
    public Card(int deckId, String front, String back) {
        this.deckId = deckId; // Przypisujemy ID zestawu
        this.front = front;   // Przypisujemy tekst z przodu
        this.back = back;     // Przypisujemy tekst z tyłu
    }

    /**
     * Pusty konstruktor domyślny – wymagany przez Room.
     * Room używa go, aby automatycznie tworzyć obiekty z danych z bazy.
     */
    public Card() {
    }
}
