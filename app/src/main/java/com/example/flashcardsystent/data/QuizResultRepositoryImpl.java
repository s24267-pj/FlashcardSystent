package com.example.flashcardsystent.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.flashcardsystent.repository.IQuizResultRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuizResultRepositoryImpl implements IQuizResultRepository {

    private final QuizResultDao dao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public QuizResultRepositoryImpl(Application app) {
        AppDatabase db = AppDatabase.getInstance(app);
        dao = db.quizResultDao();
    }

    @Override
    public void insert(QuizResult result) {
        executor.execute(() -> dao.insert(result));
    }

    @Override
    public LiveData<List<QuizResult>> getAllResults() {
        return dao.getAllResults();
    }

    @Override
    public LiveData<Integer> getCorrectTotal() {
        return dao.getTotalCorrectAnswers();
    }

    @Override
    public LiveData<Integer> getWrongTotal() {
        return dao.getTotalWrongAnswers();
    }

    @Override
    public LiveData<Integer> getTotalCount() {
        return dao.getTotalQuizCount();
    }
}
