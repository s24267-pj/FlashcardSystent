package com.example.flashcardsystent.repository;

import androidx.lifecycle.LiveData;

import com.example.flashcardsystent.data.Deck;

import java.util.List;

public interface IDeckRepository {
    LiveData<List<Deck>> getAllDecks();

    void insert(Deck deck);

    void update(Deck deck);

    void delete(Deck deck);
}
