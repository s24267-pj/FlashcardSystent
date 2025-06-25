package com.example.flashcardsystent.data;

/**
 * Repository providing asynchronous access to card data.
 */

import android.app.Application; // required to access application context

import androidx.lifecycle.LiveData; // observable data holder

import java.util.List;                       // list of cards
import java.util.concurrent.ExecutorService; // executes operations in the background
import java.util.concurrent.Executors;

public class CardRepository {
    /** DAO providing database operations */
    private final CardDao cardDao;
    /** Executes DB operations asynchronously */
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public CardRepository(Application app) {
        AppDatabase db = AppDatabase.getInstance(app); // obtain database
        cardDao = db.cardDao();
    }

    public LiveData<List<Card>> getCardsByDeck(int deckId) {
        return cardDao.getCardsByDeck(deckId); // stream of cards for a deck
    }

    public void insert(Card card) {
        executor.execute(() -> cardDao.insert(card)); // run in background
    }

    public void update(Card card) {
        executor.execute(() -> cardDao.update(card)); // run in background
    }

    public void delete(Card card) {
        executor.execute(() -> cardDao.delete(card)); // run in background
    }
}