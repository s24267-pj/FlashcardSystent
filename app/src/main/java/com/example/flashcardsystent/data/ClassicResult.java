// Pakiet z danymi aplikacji – zawiera modele wykorzystywane przez Room do tworzenia tabel
package com.example.flashcardsystent.data;

// Import adnotacji Room potrzebnej do oznaczenia klasy jako tabeli w bazie danych
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Klasa `ClassicResult` reprezentuje wynik sesji klasycznego trybu nauki fiszek.
 * Jest to encja Room – czyli klasa, którą Room traktuje jako **tabelę w bazie danych**.
 */
@Entity
public class ClassicResult {

    /**
     * Klucz główny (Primary Key) – identyfikator danego wyniku sesji.
     * `autoGenerate = true` oznacza, że Room sam automatycznie wygeneruje numer ID.
     */
    @PrimaryKey(autoGenerate = true)
    public int id;

    /**
     * Identyfikator zestawu (deckId), z którym związana była ta sesja.
     * Dzięki temu wiemy, do którego zestawu odnosi się dany wynik.
     */
    public final int deckId;

    /**
     * Liczba fiszek, które użytkownik oznaczył jako "znam".
     * Pokazuje, ile poprawnych odpowiedzi udzielono podczas sesji.
     */
    public final int correct;

    /**
     * Liczba fiszek oznaczonych jako "nie znam".
     * Pokazuje, ile błędnych odpowiedzi udzielono.
     */
    public final int wrong;

    /**
     * Najtrudniejsza fiszka podczas sesji – reprezentowana przez tekst z przodu karty.
     * Można to wykorzystać np. do pokazania, które słówko sprawiało największy problem.
     */
    public final String hardestCard;

    /**
     * Znacznik czasu (timestamp), kiedy sesja została zakończona.
     * Jest zapisany jako liczba milisekund od tzw. "epoki UNIX" (1 stycznia 1970).
     * Można go potem łatwo zamienić na datę i godzinę.
     */
    public final long timestamp;

    /**
     * Konstruktor tworzący nowy obiekt `ClassicResult` zawierający dane z sesji nauki.
     * Wszystkie dane są przekazywane jako argumenty i zapisywane do pól obiektu.
     *
     * @param deckId       identyfikator użytego zestawu
     * @param correct      liczba poprawnych odpowiedzi
     * @param wrong        liczba błędnych odpowiedzi
     * @param hardestCard  najtrudniejsza karta (tekst z przodu)
     * @param timestamp    czas zakończenia sesji (w milisekundach)
     */
    public ClassicResult(int deckId, int correct, int wrong, String hardestCard, long timestamp) {
        this.deckId = deckId;             // Zapisujemy ID zestawu
        this.correct = correct;           // Liczba poprawnych odpowiedzi
        this.wrong = wrong;               // Liczba błędnych odpowiedzi
        this.hardestCard = hardestCard;   // Najtrudniejsze słowo (tekst z przodu)
        this.timestamp = timestamp;       // Czas zakończenia sesji (UNIX timestamp)
    }
}
