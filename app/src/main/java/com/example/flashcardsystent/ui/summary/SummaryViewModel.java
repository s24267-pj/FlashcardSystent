package com.example.flashcardsystent.ui.summary;

/**
 * {@link androidx.lifecycle.ViewModel ViewModel} responsible for exposing quiz
 * statistics to the summary screen. Extensive comments document how data is
 * loaded from the database using LiveData transformations.
 */

// Required when a ViewModel needs access to the Application context
import android.app.Application;

// Annotation indicating a parameter cannot be null
import androidx.annotation.NonNull;
// ViewModel tied to the Application lifecycle
import androidx.lifecycle.AndroidViewModel;
// Lifecycle-aware observable data holder
import androidx.lifecycle.LiveData;
// Utility class for transforming LiveData values
import androidx.lifecycle.Transformations;

// Singleton entry point to the Room database
import com.example.flashcardsystent.data.AppDatabase;
// Entity representing a quiz result row
import com.example.flashcardsystent.data.QuizResult;
// DAO providing queries for quiz results
import com.example.flashcardsystent.data.QuizResultDao;

public class SummaryViewModel extends AndroidViewModel {
    // Number of quiz sessions stored in the database
    public final LiveData<Integer> totalQuizzes;
    // Total number of correctly answered questions across all sessions
    public final LiveData<Integer> correctAnswers;
    // Total number of wrong answers across all sessions
    public final LiveData<Integer> wrongAnswers;

    // Last recorded quiz result
    public final LiveData<QuizResult> lastResult;

    /**
     * Create the ViewModel and wire LiveData to the underlying database.
     */
    public SummaryViewModel(@NonNull Application application) {
        super(application);
        // Retrieve the singleton database instance
        AppDatabase db = AppDatabase.getInstance(application);
        // Acquire the DAO for quiz results
        QuizResultDao dao = db.quizResultDao();

        // Count how many records exist in the results table
        totalQuizzes = Transformations.map(dao.getAllResults(), list -> list.size());
        // Sum up the number of correct answers across all results
        correctAnswers = Transformations.map(dao.getAllResults(), list ->
                list.stream().mapToInt(r -> r.correct).sum());
        // Sum up the incorrect answers in the same way
        wrongAnswers = Transformations.map(dao.getAllResults(), list ->
                list.stream().mapToInt(r -> r.wrong).sum());

        // Retrieve the most recent quiz result or null if none exist
        lastResult = Transformations.map(dao.getAllResults(), list ->
                list.isEmpty() ? null : list.get(list.size() - 1)
        );
    }

}