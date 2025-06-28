package com.example.flashcardsystent.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.flashcardsystent.data.AppDatabase;
import com.example.flashcardsystent.data.ClassicResult;
import com.example.flashcardsystent.data.ClassicResultDao;
import com.example.flashcardsystent.data.QuizResult;
import com.example.flashcardsystent.data.QuizResultDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ViewModel responsible for exposing quiz and classic learning statistics to the summary screen.
 * Uses LiveData transformations to derive aggregate statistics from the database.
 */
public class SummaryViewModel extends AndroidViewModel {

    /** Total number of quiz sessions completed */
    public final LiveData<Integer> totalQuizzes;

    /** Total number of correct answers given across all quiz sessions */
    public final LiveData<Integer> correctAnswers;

    /** Total number of incorrect answers across all quiz sessions */
    public final LiveData<Integer> wrongAnswers;

    /** Most recent quiz result, or null if none exist */
    public final LiveData<QuizResult> lastResult;

    /** Total number of classic learning sessions completed */
    public final LiveData<Integer> totalClassic;

    /** Mapping of deck ID to number of completed classic sessions */
    public final LiveData<Map<Integer, Integer>> classicPerDeck;

    /**
     * Constructs the SummaryViewModel and sets up LiveData transformations
     * based on quiz and classic learning result tables.
     *
     * @param application the application context
     */
    public SummaryViewModel(@NonNull Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        QuizResultDao dao = db.quizResultDao();
        ClassicResultDao classicDao = db.classicResultDao();

        totalQuizzes = Transformations.map(dao.getAllResults(), List::size);

        correctAnswers = Transformations.map(dao.getAllResults(),
                list -> list.stream().mapToInt(r -> r.correct).sum());

        wrongAnswers = Transformations.map(dao.getAllResults(),
                list -> list.stream().mapToInt(r -> r.wrong).sum());

        lastResult = Transformations.map(dao.getAllResults(), list ->
                list.isEmpty() ? null : list.get(list.size() - 1));

        totalClassic = Transformations.map(classicDao.getAllResults(), List::size);

        classicPerDeck = Transformations.map(classicDao.getAllResults(), list -> {
            Map<Integer, Integer> map = new HashMap<>();
            for (ClassicResult r : list) {
                int count = map.getOrDefault(r.deckId, 0) + 1;
                map.put(r.deckId, count);
            }
            return map;
        });
    }
}
