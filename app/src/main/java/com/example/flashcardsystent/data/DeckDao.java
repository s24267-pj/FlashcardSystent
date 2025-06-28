// Pakiet danych aplikacji – zawiera interfejsy DAO i encje Room
package com.example.flashcardsystent.data;

// Importujemy niezbędne klasy Room i LiveData
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Interfejs `DeckDao` (Data Access Object) definiuje operacje, które można wykonać
 * na obiektach typu `Deck` (czyli zestawach fiszek).
 * Room na podstawie tego interfejsu automatycznie generuje kod SQL,
 * dzięki czemu nie trzeba pisać go ręcznie.
 */
@Dao
public interface DeckDao {

    /**
     * Wstawia nowy zestaw (`Deck`) do bazy danych.
     * Room wygeneruje kod SQL: `INSERT INTO Deck (...) VALUES (...)`
     *
     * @param deck obiekt zestawu, który ma być zapisany w bazie
     */
    @Insert
    void insert(Deck deck);

    /**
     * Usuwa podany zestaw z bazy danych.
     * Room wygeneruje kod SQL: `DELETE FROM Deck WHERE id = :deck.id`
     *
     * @param deck zestaw, który ma zostać usunięty
     */
    @Delete
    void delete(Deck deck);

    /**
     * Aktualizuje istniejący zestaw w bazie danych.
     * Room porównuje `id` obiektu `deck` i aktualizuje odpowiedni rekord.
     *
     * @param deck zestaw z nowymi danymi (np. nową nazwą)
     */
    @Update
    void update(Deck deck);

    /**
     * Zwraca wszystkie zestawy fiszek zapisane w bazie, opakowane w `LiveData`.
     * Dzięki `LiveData`, interfejs użytkownika automatycznie zaktualizuje się,
     * gdy dane w bazie się zmienią.
     *
     * @return obserwowalna lista zestawów
     */
    @Query("SELECT * FROM Deck")
    LiveData<List<Deck>> getAllDecks();

    /**
     * Zwraca wszystkie zestawy bez `LiveData`, czyli w sposób synchroniczny.
     * Taka metoda może być użyta np. w testach jednostkowych lub logice bezpośredniej.
     *
     * @return lista wszystkich zestawów w danej chwili
     */
    @Query("SELECT * FROM Deck")
    List<Deck> getAll();

    /**
     * Pobiera jeden zestaw po jego ID.
     * `LIMIT 1` oznacza, że interesuje nas tylko pierwszy pasujący wynik (a ID powinno być unikalne).
     *
     * @param id identyfikator zestawu
     * @return obiekt `Deck` o podanym ID, lub `null` jeśli nie znaleziono
     */
    @Query("SELECT * FROM Deck WHERE id = :id LIMIT 1")
    Deck getById(int id);
}
