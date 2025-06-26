package com.example.flashcardsystent.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * DAO interface for accessing and storing results of classic learning sessions.
 * Provides operations for inserting and querying statistics.
 */
@Dao
public interface ClassicResultDao {

    /**
     * Inserts a completed classic learning result into the database.
     *
     * @param result the result to be saved
     */
    @Insert
    void insert(ClassicResult result);

    /**
     * Retrieves all classic learning results from the database, ordered from newest to oldest.
     *
     * @return LiveData list of results sorted by timestamp in descending order
     */
    @Query("SELECT * FROM ClassicResult ORDER BY timestamp DESC")
    LiveData<List<ClassicResult>> getAllResults();
}
