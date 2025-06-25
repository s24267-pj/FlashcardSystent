package com.example.flashcardsystent.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Room entity representing a set of flashcards grouped under a named deck.
 * Each deck can contain multiple cards associated by foreign key.
 */
@Entity
public class Deck {

    /** Unique identifier for the deck, generated automatically by Room */
    @PrimaryKey(autoGenerate = true)
    public int id;

    /** Name of the deck, shown to the user */
    public String name;

    /**
     * Constructs a new deck with the given name.
     *
     * @param name the name of the deck
     */
    public Deck(String name) {
        this.name = name;
    }
}
