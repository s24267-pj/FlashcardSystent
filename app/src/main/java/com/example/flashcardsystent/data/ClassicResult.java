package com.example.flashcardsystent.data;

/**
 * Entity storing statistics from a completed classic learning session.
 */

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ClassicResult {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public final int deckId;
    public final int correct;
    public final int wrong;
    public final String hardestCard;
    public final long timestamp;

    public ClassicResult(int deckId, int correct, int wrong, String hardestCard, long timestamp) {
        this.deckId = deckId;
        this.correct = correct;
        this.wrong = wrong;
        this.hardestCard = hardestCard;
        this.timestamp = timestamp;
    }
}