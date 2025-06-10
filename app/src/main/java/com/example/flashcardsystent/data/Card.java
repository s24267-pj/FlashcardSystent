package com.example.flashcardsystent.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "Card",
        foreignKeys = @ForeignKey(
                entity = Deck.class,
                parentColumns = "id",
                childColumns = "deckId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = @Index("deckId")
)
public class Card {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int deckId;

    public String front;

    public String back;

    public Card(int deckId, String front, String back) {
        this.deckId = deckId;
        this.front = front;
        this.back = back;
    }

    public Card() {
    }
}
