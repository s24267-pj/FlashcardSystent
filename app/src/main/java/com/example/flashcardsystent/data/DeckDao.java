package com.example.flashcardsystent.data;

/**
 * DAO for performing operations on Deck entities.
 */

import androidx.lifecycle.LiveData; // LiveData wrapper for observable queries
import androidx.room.*;             // Room DAO annotations

import java.util.List; // list of decks

@Dao
public interface DeckDao {

    @Insert
    void insert(Deck deck); // insert new deck

    @Delete
    void delete(Deck deck); // delete deck

    @Update
    void update(Deck deck); // update deck name

    @Query("SELECT * FROM Deck")
    LiveData<List<Deck>> getAllDecks(); // observable list of decks

    @Query("SELECT * FROM Deck")
    List<Deck> getAll(); // synchronous query

}