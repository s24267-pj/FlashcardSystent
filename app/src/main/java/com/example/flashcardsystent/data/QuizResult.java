package com.example.flashcardsystent.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class QuizResult {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int deckId;
    public int correct;

    public int wrong;
    public int total;
    public long timestamp;

    public QuizResult(int deckId, int correct, int total, long timestamp) {
        this.deckId = deckId;
        this.correct = correct;
        this.total = total;
        this.timestamp = timestamp;
    }
}
