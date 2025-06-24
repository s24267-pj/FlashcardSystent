package com.example.flashcardsystent.ui.summary;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.flashcardsystent.data.AppDatabase;
import com.example.flashcardsystent.data.QuizResult;
import com.example.flashcardsystent.data.QuizResultDao;

public class SummaryViewModel extends AndroidViewModel {
    public final LiveData<Integer> totalQuizzes;
    public final LiveData<Integer> correctAnswers;
    public final LiveData<Integer> wrongAnswers;

    public final LiveData<QuizResult> lastResult;

    public SummaryViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        QuizResultDao dao = db.quizResultDao();

        totalQuizzes = Transformations.map(dao.getAllResults(), list -> list.size());
        correctAnswers = Transformations.map(dao.getAllResults(), list ->
                list.stream().mapToInt(r -> r.correct).sum());
        wrongAnswers = Transformations.map(dao.getAllResults(), list ->
                list.stream().mapToInt(r -> r.wrong).sum());

        lastResult = Transformations.map(dao.getAllResults(), list ->
                list.isEmpty() ? null : list.get(list.size() - 1)
        );
    }

}
