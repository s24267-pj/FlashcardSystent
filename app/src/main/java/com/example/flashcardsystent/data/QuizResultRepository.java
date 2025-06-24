package com.example.flashcardsystent.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuizResultRepository {
    private final QuizResultDao dao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public QuizResultRepository(Application app) {
        AppDatabase db = AppDatabase.getInstance(app);
        dao = db.quizResultDao();
    }

    public void insert(QuizResult result) {
        executor.execute(() -> dao.insert(result));
    }

    public LiveData<List<QuizResult>> getAll() {
        return dao.getAllResults();
    }

    public LiveData<Integer> getTotalCount() {
        return dao.getTotalQuizCount();
    }

    public LiveData<Integer> getCorrectTotal() {
        return dao.getTotalCorrectAnswers();
    }

    public LiveData<Integer> getWrongTotal() {
        return dao.getTotalWrongAnswers();
    }
}
