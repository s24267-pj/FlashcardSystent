package com.example.flashcardsystent.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * DAO interface for accessing and managing {@link QuizResult} entities.
 * Provides methods for inserting new results and retrieving quiz history.
 */
@Dao
public interface QuizResultDao {

    /**
     * Inserts a new quiz result into the database.
     *
     * @param result the quiz result to insert
     */
    @Insert
    void insert(QuizResult result);

    /**
     * Retrieves all quiz results from the database, sorted from newest to oldest.
     *
     * @return observable list of quiz results ordered by timestamp descending
     */
    @Query("SELECT * FROM QuizResult ORDER BY timestamp DESC")
    LiveData<List<QuizResult>> getAllResults();
}
