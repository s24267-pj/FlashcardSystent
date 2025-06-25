package com.example.flashcardsystent.data;

/**
 * Entity storing the result of a completed quiz.
 */

import androidx.room.Entity;      // table for quiz results
import androidx.room.PrimaryKey;  // primary key annotation

@Entity
public class QuizResult {
    @PrimaryKey(autoGenerate = true) // unique id for each result
    public int id;

    public final int deckId; // related deck
    public final int correct; // correct answers count

    public int wrong;   // incorrect answers count
    public final int total;    // total questions
    public final long timestamp; // when the quiz was taken

    public QuizResult(int deckId, int correct, int wrong, int total, long timestamp) {
        this.deckId = deckId;       // deck identifier
        this.correct = correct;     // number correct
        this.wrong = wrong;
        this.total = total;         // total questions asked
        this.timestamp = timestamp; // when quiz occurred
    }
}