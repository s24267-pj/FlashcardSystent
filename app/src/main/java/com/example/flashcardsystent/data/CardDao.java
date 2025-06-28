// Pakiet danych – zawiera interfejsy DAO oraz klasy modeli bazy danych
package com.example.flashcardsystent.data;

// Importujemy klasy potrzebne do działania DAO i architektury Room
import androidx.lifecycle.LiveData;  // LiveData to obserwowalny kontener danych (np. dla UI)
import androidx.room.Dao;           // Adnotacja oznaczająca interfejs jako DAO
import androidx.room.Delete;        // Adnotacja – oznacza metodę jako usuwanie danych
import androidx.room.Insert;        // Adnotacja – oznacza metodę jako wstawianie danych
import androidx.room.Query;         // Adnotacja – oznacza metodę jako zapytanie SQL
import androidx.room.Update;        // Adnotacja – oznacza metodę jako aktualizację danych

import java.util.List;             // Lista obiektów – tutaj: lista fiszek (Card)

/**
 * Interfejs DAO (Data Access Object) służący do operacji na tabeli `Card`.
 * Room automatycznie generuje kod SQL na podstawie tych metod.
 * Umożliwia dodawanie, usuwanie, aktualizowanie i pobieranie fiszek z bazy danych.
 */
@Dao
public interface CardDao {

    /**
     * Wstawia nową fiszkę do bazy danych.
     * @param card obiekt fiszki do zapisania
     *
     * Room sam wygeneruje zapytanie INSERT.
     */
    @Insert
    void insert(Card card);

    /**
     * Aktualizuje dane istniejącej fiszki w bazie.
     * Identyfikacja odbywa się po `card.id`.
     * @param card obiekt fiszki z nowymi danymi
     */
    @Update
    void update(Card card);

    /**
     * Usuwa fiszkę z bazy danych.
     * @param card obiekt fiszki, który ma zostać usunięty
     *
     * Usuwanie odbywa się na podstawie `card.id`.
     */
    @Delete
    void delete(Card card);

    /**
     * Pobiera wszystkie fiszki przypisane do konkretnego zestawu (deckId).
     * `:deckId` to parametr dynamiczny – zostanie zastąpiony wartością przekazaną w argumencie metody.
     *
     * @param deckId identyfikator zestawu, którego fiszki mają być pobrane
     * @return LiveData z listą fiszek należących do danego zestawu
     */
    @Query("SELECT * FROM Card WHERE deckId = :deckId")
    LiveData<List<Card>> getCardsByDeck(int deckId);

    /**
     * Pobiera jedną fiszkę na podstawie jej unikalnego ID.
     * @param id identyfikator fiszki
     * @return LiveData z obiektem fiszki (Card)
     */
    @Query("SELECT * FROM Card WHERE id = :id")
    LiveData<Card> getCardById(int id);
}
