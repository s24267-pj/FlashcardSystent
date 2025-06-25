package com.example.flashcardsystent.data;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

/**
 * DAO (Data Access Object) for performing operations on {@link Deck} entities.
 * Provides methods to insert, update, delete and query flashcard decks.
 */
@Dao
public interface DeckDao {

    /**
     * Inserts a new deck into the database.
     *
     * @param deck the deck to insert
     */
    @Insert
    void insert(Deck deck);

    /**
     * Deletes a specific deck from the database.
     *
     * @param deck the deck to delete
     */
    @Delete
    void delete(Deck deck);

    /**
     * Updates an existing deck in the database.
     *
     * @param deck the deck with updated information
     */
    @Update
    void update(Deck deck);

    /**
     * Retrieves all decks as a LiveData list for observation in the UI.
     *
     * @return observable list of all decks
     */
    @Query("SELECT * FROM Deck")
    LiveData<List<Deck>> getAllDecks();

    /**
     * Retrieves all decks synchronously (non-observable).
     *
     * @return list of all decks
     */
    @Query("SELECT * FROM Deck")
    List<Deck> getAll();

    /**
     * Retrieves a single deck by its ID.
     *
     * @param id the ID of the deck
     * @return the deck with the given ID, or null if not found
     */
    @Query("SELECT * FROM Deck WHERE id = :id LIMIT 1")
    Deck getById(int id);
}
