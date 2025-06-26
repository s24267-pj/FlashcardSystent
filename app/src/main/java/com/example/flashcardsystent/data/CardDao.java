package com.example.flashcardsystent.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data access object (DAO) for performing operations on {@link Card} entities.
 * Provides methods to insert, update, delete and query cards from the database.
 */
@Dao
public interface CardDao {

    /**
     * Inserts a new flashcard into the database.
     *
     * @param card the card to insert
     */
    @Insert
    void insert(Card card);

    /**
     * Updates an existing card in the database.
     *
     * @param card the card with updated data
     */
    @Update
    void update(Card card);

    /**
     * Deletes a card from the database.
     *
     * @param card the card to delete
     */
    @Delete
    void delete(Card card);

    /**
     * Retrieves all cards that belong to a given deck.
     *
     * @param deckId the ID of the deck
     * @return observable list of cards in that deck
     */
    @Query("SELECT * FROM Card WHERE deckId = :deckId")
    LiveData<List<Card>> getCardsByDeck(int deckId);

    /**
     * Retrieves a single card by its unique ID.
     *
     * @param id the ID of the card
     * @return observable card with the given ID
     */
    @Query("SELECT * FROM Card WHERE id = :id")
    LiveData<Card> getCardById(int id);
}
