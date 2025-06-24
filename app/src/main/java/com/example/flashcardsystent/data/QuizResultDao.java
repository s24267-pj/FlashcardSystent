package com.example.flashcardsystent.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QuizResultDao {

    @Insert
    void insert(QuizResult result);

    @Query("SELECT * FROM QuizResult ORDER BY timestamp DESC")
    LiveData<List<QuizResult>> getAllResults();

    @Query("SELECT COUNT(*) FROM QuizResult")
    LiveData<Integer> getTotalQuizCount();

    @Query("SELECT SUM(correct) FROM QuizResult")
    LiveData<Integer> getTotalCorrectAnswers();

    @Query("SELECT SUM(total - correct) FROM QuizResult")
    LiveData<Integer> getTotalWrongAnswers();
}
