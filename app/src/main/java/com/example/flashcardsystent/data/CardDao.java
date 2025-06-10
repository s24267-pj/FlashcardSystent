package com.example.flashcardsystent.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CardDao {
    @Insert
    void insert(Card card);

    @Update
    void update(Card card);

    @Delete
    void delete(Card card);

    @Query("SELECT * FROM Card WHERE deckId = :deckId")
    LiveData<List<Card>> getCardsByDeck(int deckId);
}

