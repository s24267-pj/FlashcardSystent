package com.example.flashcardsystent.data;

/**
 * Data access object for Card entity.
 */

import androidx.lifecycle.LiveData; // observable wrapper for query results
import androidx.room.Dao;           // marks interface as Room DAO
import androidx.room.Delete;        // delete annotation
import androidx.room.Insert;        // insert annotation
import androidx.room.Query;         // query annotation
import androidx.room.Update;        // update annotation

import java.util.List; // list of card objects

@Dao
public interface CardDao {
    @Insert
    void insert(Card card); // add a new card

    @Update
    void update(Card card); // modify existing card

    @Delete
    void delete(Card card); // remove a card

    @Query("SELECT * FROM Card WHERE deckId = :deckId")
    LiveData<List<Card>> getCardsByDeck(int deckId); // get cards by deck

    @Query("SELECT * FROM Card WHERE id = :id")
    LiveData<Card> getCardById(int id); // get single card
}