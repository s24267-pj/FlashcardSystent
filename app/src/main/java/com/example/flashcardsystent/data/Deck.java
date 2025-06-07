package com.example.flashcardsystent.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Deck {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    public Deck(String name) {
        this.name = name;
    }
}
