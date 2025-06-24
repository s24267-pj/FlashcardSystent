package com.example.flashcardsystent.repository;

import androidx.lifecycle.LiveData;

import com.example.flashcardsystent.data.QuizResult;

import java.util.List;

public interface IQuizResultRepository {
    LiveData<List<QuizResult>> getAllResults();

    LiveData<Integer> getCorrectTotal();

    LiveData<Integer> getWrongTotal();

    LiveData<Integer> getTotalCount();

    void insert(QuizResult result);
}
