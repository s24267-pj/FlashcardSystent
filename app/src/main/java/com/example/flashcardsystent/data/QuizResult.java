// Pakiet, w którym znajduje się model danych `QuizResult` – należy do warstwy danych aplikacji
package com.example.flashcardsystent.data;

// Import adnotacji potrzebnych do działania z Room (Android Jetpack)
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Klasa `QuizResult` reprezentuje wynik pojedynczej sesji quizu.
 * Jest oznaczona jako `@Entity`, co oznacza, że Room potraktuje ją jako tabelę w bazie danych.
 * Każdy obiekt tej klasy odpowiada jednemu rekordowi w tabeli "QuizResult".
 */
@Entity
public class QuizResult {

    /**
     * Unikalny identyfikator każdego zapisu wyniku quizu.
     * `@PrimaryKey(autoGenerate = true)` oznacza, że Room sam przydzieli unikalny numer ID.
     * Jest to tzw. klucz główny (ang. Primary Key).
     */
    @PrimaryKey(autoGenerate = true)
    public int id;

    /**
     * ID zestawu fiszek (`Deck`), na którym przeprowadzono quiz.
     * Służy do powiązania wyniku quizu z konkretnym zestawem.
     */
    public final int deckId;

    /**
     * Liczba poprawnych odpowiedzi udzielonych w czasie quizu.
     */
    public final int correct;

    /**
     * Liczba błędnych odpowiedzi udzielonych przez użytkownika.
     * Ta zmienna **nie** jest `final`, więc może być zmieniona później w kodzie.
     */
    public int wrong;

    /**
     * Łączna liczba pytań, które zostały zadane w quizie.
     * Służy do wyliczania procentu poprawności.
     */
    public final int total;

    /**
     * Czas zakończenia quizu, zapisany jako liczba milisekund od 1 stycznia 1970 (tzw. UNIX epoch).
     * Służy do sortowania wyników według daty wykonania.
     */
    public final long timestamp;

    /**
     * Konstruktor tworzący nowy wynik quizu.
     * Wszystkie dane muszą być przekazane podczas tworzenia obiektu.
     * Room użyje tego konstruktora do zapisu i odczytu danych z bazy.
     *
     * @param deckId    identyfikator zestawu fiszek użytego w quizie
     * @param correct   liczba poprawnych odpowiedzi
     * @param wrong     liczba błędnych odpowiedzi
     * @param total     łączna liczba pytań w quizie
     * @param timestamp znacznik czasu zakończenia quizu (w milisekundach)
     */
    public QuizResult(int deckId, int correct, int wrong, int total, long timestamp) {
        this.deckId = deckId;
        this.correct = correct;
        this.wrong = wrong;
        this.total = total;
        this.timestamp = timestamp;
    }
}
