package com.example.flashcardsystent.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.flashcardsystent.repository.IQuizResultRepository;
import com.example.flashcardsystent.data.QuizResult;
import com.example.flashcardsystent.data.QuizResultRepositoryImpl;

import java.util.List;

public class SummaryViewModel extends AndroidViewModel {

    private final IQuizResultRepository repository;

    public final LiveData<Integer> totalQuizzes;
    public final LiveData<Integer> correctAnswers;
    public final LiveData<Integer> wrongAnswers;
    public final LiveData<QuizResult> lastResult;

    public SummaryViewModel(@NonNull Application application) {
        super(application);
        repository = new QuizResultRepositoryImpl(application);


        LiveData<List<QuizResult>> allResults = repository.getAllResults();

        totalQuizzes = Transformations.map(allResults, List::size);

        correctAnswers = Transformations.map(allResults, list ->
                list.stream().mapToInt(r -> r.correct).sum());

        wrongAnswers = Transformations.map(allResults, list ->
                list.stream().mapToInt(QuizResult::getWrong).sum());

        lastResult = Transformations.map(allResults, list ->
                list.isEmpty() ? null : list.get(list.size() - 1));
    }
}
