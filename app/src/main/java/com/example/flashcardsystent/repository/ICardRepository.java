package com.example.flashcardsystent.repository;

import androidx.lifecycle.LiveData;

import com.example.flashcardsystent.data.Card;

import java.util.List;

public interface ICardRepository {
    LiveData<List<Card>> getCardsByDeck(int deckId);

    void insert(Card card);

    void update(Card card);

    void delete(Card card);

    LiveData<Card> getCardById(int id);
}
