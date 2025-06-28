// Pakiet danych – zawiera wszystkie klasy odpowiedzialne za operacje na bazie danych Room
package com.example.flashcardsystent.data;

// Importy potrzebnych adnotacji i klas Room oraz LiveData do obserwowania danych
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Interfejs DAO (Data Access Object) odpowiedzialny za operacje na wynikach quizów (`QuizResult`).
 * Room używa tego interfejsu do automatycznego generowania kodu SQL do pracy z bazą danych.
 */
@Dao
public interface QuizResultDao {

    /**
     * Wstawia nowy wynik quizu do bazy danych.
     * Room automatycznie wygeneruje zapytanie SQL typu INSERT.
     *
     * @param result obiekt `QuizResult`, który ma zostać zapisany
     */
    @Insert
    void insert(QuizResult result);

    /**
     * Pobiera wszystkie zapisane wyniki quizów z bazy danych.
     * Dane są posortowane malejąco według czasu wykonania (`timestamp DESC`),
     * czyli najnowsze wyniki będą na górze listy.
     *
     * Zwracany wynik to `LiveData`, czyli dane, które można obserwować z poziomu UI.
     * Gdy dane się zmienią (np. nowy wynik zostanie dodany), interfejs odświeży się automatycznie.
     *
     * @return obserwowalna lista wyników quizów (od najnowszego do najstarszego)
     */
    @Query("SELECT * FROM QuizResult ORDER BY timestamp DESC")
    LiveData<List<QuizResult>> getAllResults();
}
