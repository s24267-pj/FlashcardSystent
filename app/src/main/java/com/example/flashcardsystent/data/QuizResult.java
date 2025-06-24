package com.example.flashcardsystent.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class QuizResult {
    @PrimaryKey(autoGenerate = true)
    public int id;

    final int deckId;
    public final int correct;

    public int wrong;
    final int total;
    final long timestamp;

    public QuizResult(int deckId, int correct, int total, long timestamp) {
        this.deckId = deckId;
        this.correct = correct;
        this.total = total;
        this.timestamp = timestamp;
    }
}
