package com.example.flashcardsystent.data;

/**
 * DAO for persisting classic learning session results.
 */

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface ClassicResultDao {
    @Insert
    void insert(ClassicResult result);

    @Query("SELECT * FROM ClassicResult ORDER BY timestamp DESC")
    LiveData<List<ClassicResult>> getAllResults();
}