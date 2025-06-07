package com.example.flashcardsystent.data;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface DeckDao {

    @Insert
    void insert(Deck deck);

    @Delete
    void delete(Deck deck);

    @Update
    void update(Deck deck);

    @Query("SELECT * FROM Deck")
    LiveData<List<Deck>> getAllDecks();
}
