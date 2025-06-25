package com.example.flashcardsystent.data;

/**
 * DAO for quiz result statistics and persistence.
 */

import androidx.lifecycle.LiveData; // observable query results
import androidx.room.Dao;            // defines DAO interface
import androidx.room.Insert;         // insert annotation
import androidx.room.Query;          // query annotation

import java.util.List; // list container

@Dao
public interface QuizResultDao {

    @Insert
    void insert(QuizResult result); // save a quiz result

    @Query("SELECT * FROM QuizResult ORDER BY timestamp DESC")
    LiveData<List<QuizResult>> getAllResults(); // all results newest first

    @Query("SELECT COUNT(*) FROM QuizResult")
    LiveData<Integer> getTotalQuizCount(); // total number of quizzes taken

    @Query("SELECT SUM(correct) FROM QuizResult")
    LiveData<Integer> getTotalCorrectAnswers(); // total correct answers overall

    @Query("SELECT SUM(total - correct) FROM QuizResult")
    LiveData<Integer> getTotalWrongAnswers(); // total wrong answers overall
}