// Pakiet danych – zawiera interfejsy DAO oraz modele powiązane z bazą danych Room
package com.example.flashcardsystent.data;

// Importujemy potrzebne klasy z Android Jetpack i Room
import androidx.lifecycle.LiveData;   // LiveData pozwala obserwować zmiany danych w bazie
import androidx.room.Dao;            // Adnotacja oznaczająca interfejs jako DAO
import androidx.room.Insert;         // Adnotacja oznaczająca metodę jako operację INSERT
import androidx.room.Query;          // Adnotacja oznaczająca metodę jako zapytanie SQL

import java.util.List;              // Lista wyników klasycznego trybu nauki

/**
 * Interfejs DAO (Data Access Object) dla wyników z klasycznego trybu nauki.
 * Zawiera operacje do wstawiania wyników oraz ich pobierania z bazy danych.
 * Room automatycznie generuje implementację tych metod.
 */
@Dao
public interface ClassicResultDao {

    /**
     * Wstawia nowy wynik sesji klasycznej nauki do bazy danych.
     * Room wygeneruje automatycznie zapytanie SQL `INSERT INTO ClassicResult(...) VALUES(...)`.
     *
     * @param result obiekt ClassicResult zawierający dane sesji nauki
     */
    @Insert
    void insert(ClassicResult result);

    /**
     * Pobiera wszystkie zapisane wyniki z klasycznego trybu nauki.
     * Dane są sortowane malejąco według znacznika czasu (`timestamp`),
     * co oznacza, że **najbardziej aktualne wyniki są na górze listy**.
     *
     * @return LiveData z listą wyników, automatycznie aktualizowaną gdy dane się zmienią
     */
    @Query("SELECT * FROM ClassicResult ORDER BY timestamp DESC")
    LiveData<List<ClassicResult>> getAllResults();
}
