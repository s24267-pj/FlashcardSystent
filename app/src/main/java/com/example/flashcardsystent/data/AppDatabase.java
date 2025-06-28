// Pakiet, w którym znajduje się klasa AppDatabase – organizuje kod związany z bazą danych
package com.example.flashcardsystent.data;

// Importujemy niezbędne klasy z Androida i Room
import android.content.Context;

import androidx.room.Database;       // Adnotacja do oznaczania klasy jako bazy danych Room
import androidx.room.Room;           // Narzędzie do tworzenia instancji bazy danych
import androidx.room.RoomDatabase;   // Bazowa klasa dla baz danych Room

/**
 * Klasa `AppDatabase` to główna definicja bazy danych Room w aplikacji.
 * Umożliwia dostęp do wszystkich tabel (encji) poprzez DAO.
 * Zastosowano tutaj wzorzec projektowy Singleton – czyli tworzona jest tylko jedna instancja bazy,
 * która jest używana globalnie w całej aplikacji.
 */
@Database(
        entities = {Deck.class, Card.class, QuizResult.class, ClassicResult.class}, // Lista tabel w bazie danych
        version = 6 // Wersja bazy – przy jej zmianie konieczna może być migracja danych
)
public abstract class AppDatabase extends RoomDatabase {

    /**
     * Prywatne statyczne pole przechowujące instancję bazy danych.
     * Dzięki modyfikatorowi `static`, jest współdzielone przez całą aplikację.
     * Dzięki `private`, nie można go zmienić bezpośrednio z zewnątrz.
     */
    private static AppDatabase INSTANCE;

    /**
     * Metoda abstrakcyjna – zwraca DAO dla encji `Deck`.
     * DAO (Data Access Object) to interfejs służący do wykonywania operacji na tabelach bazy danych.
     * Room sam wygeneruje implementację tego DAO.
     */
    public abstract DeckDao deckDao();

    /**
     * Zwraca DAO dla encji `Card` (czyli pojedynczych fiszek).
     */
    public abstract CardDao cardDao();

    /**
     * Zwraca DAO służące do zapisywania i pobierania wyników z trybu quizowego.
     */
    public abstract QuizResultDao quizResultDao();

    /**
     * Zwraca DAO do przechowywania wyników z klasycznego trybu nauki.
     */
    public abstract ClassicResultDao classicResultDao();

    /**
     * Zwraca jedyną (globalną) instancję bazy danych w aplikacji.
     * Jeśli instancja nie istnieje – zostaje utworzona.
     * Zastosowano `synchronized`, aby zapewnić bezpieczeństwo przy tworzeniu instancji w środowisku wielowątkowym.
     *
     * @param context kontekst aplikacji – używamy `getApplicationContext()`, by uniknąć wycieków pamięci
     * @return instancja bazy danych AppDatabase
     */
    public static synchronized AppDatabase getInstance(Context context) {
        // Jeśli baza nie została jeszcze utworzona – tworzymy ją
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(), // Kontekst aplikacji – bezpieczny do użycia globalnego
                            AppDatabase.class,               // Klasa bazy danych (this)
                            "flashcards-db"                  // Nazwa pliku bazy danych (SQLite)
                    )
                    // Jeśli zmieni się wersja bazy danych i brak migracji – usuń starą i stwórz nową bazę (uwaga: utrata danych!)
                    .fallbackToDestructiveMigration()
                    .build(); // Tworzy i zwraca instancję bazy
        }

        // Zwracamy istniejącą lub nowo utworzoną instancję bazy
        return INSTANCE;
    }
}
