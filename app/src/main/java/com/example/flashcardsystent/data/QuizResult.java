package com.example.flashcardsystent.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class QuizResult {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public final int deckId;
    public final int correct;
    public final int total;
    public final long timestamp;

    public QuizResult(int deckId, int correct, int total, long timestamp) {
        this.deckId = deckId;
        this.correct = correct;
        this.total = total;
        this.timestamp = timestamp;
    }

    // Obliczanie błędnych odpowiedzi dynamicznie
    public int getWrong() {
        return total - correct;
    }
}
