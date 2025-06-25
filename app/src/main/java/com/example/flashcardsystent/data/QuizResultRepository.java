package com.example.flashcardsystent.data;

/**
 * Repository for persisting and aggregating quiz results.
 */

import android.app.Application; // needed to access DB instance

import androidx.lifecycle.LiveData; // observable results

import java.util.List;                       // list of quiz results
import java.util.concurrent.ExecutorService; // asynchronous executor
import java.util.concurrent.Executors;

public class QuizResultRepository {
    /** DAO used to access quiz results */
    private final QuizResultDao dao;
    /** Executes operations on a background thread */
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public QuizResultRepository(Application app) {
        AppDatabase db = AppDatabase.getInstance(app); // open database
        dao = db.quizResultDao();
    }

    public void insert(QuizResult result) {
        executor.execute(() -> dao.insert(result)); // store new result
    }

    public LiveData<List<QuizResult>> getAll() {
        return dao.getAllResults(); // observe all saved results
    }

    public LiveData<Integer> getTotalCount() {
        return dao.getTotalQuizCount(); // number of played quizzes
    }

    public LiveData<Integer> getCorrectTotal() {
        return dao.getTotalCorrectAnswers(); // sum of correct answers
    }

    public LiveData<Integer> getWrongTotal() {
        return dao.getTotalWrongAnswers(); // sum of wrong answers
    }
}