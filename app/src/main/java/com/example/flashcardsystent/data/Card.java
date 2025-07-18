package com.example.flashcardsystent.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Room entity representing a single flashcard.
 * Each card belongs to a deck and stores front and back text.
 */
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

    /** Unique ID of the card, autogenerated by Room. */
    @PrimaryKey(autoGenerate = true)
    public int id;

    /** ID of the deck this card belongs to. */
    public int deckId;

    /** Text shown on the front side of the card. */
    public String front;

    /** Text shown on the back side of the card. */
    public String back;

    /**
     * Constructs a new card instance.
     * @param deckId deck to which this card belongs
     * @param front front text
     * @param back back text
     */
    public Card(int deckId, String front, String back) {
        this.deckId = deckId;
        this.front = front;
        this.back = back;
    }

    /**
     * Default constructor required by Room.
     */
    public Card() {
    }
}
