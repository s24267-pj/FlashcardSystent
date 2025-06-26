package com.example.flashcardsystent.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Repository providing asynchronous access to {@link Card} data.
 * Acts as an abstraction layer between the database and the ViewModel.
 */
public class CardRepository {

    /** DAO providing access to the database operations for cards */
    private final CardDao cardDao;

    /** Executor used to run database operations on a background thread */
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * Constructs a new CardRepository.
     *
     * @param app the application context, required to initialize the database
     */
    public CardRepository(Application app) {
        AppDatabase db = AppDatabase.getInstance(app);
        cardDao = db.cardDao();
    }

    /**
     * Returns all cards that belong to a specified deck.
     *
     * @param deckId the ID of the deck
     * @return LiveData list of cards in that deck
     */
    public LiveData<List<Card>> getCardsByDeck(int deckId) {
        return cardDao.getCardsByDeck(deckId);
    }

    /**
     * Inserts a card into the database asynchronously.
     *
     * @param card the card to insert
     */
    public void insert(Card card) {
        executor.execute(() -> cardDao.insert(card));
    }

    /**
     * Updates a card in the database asynchronously.
     *
     * @param card the card with updated data
     */
    public void update(Card card) {
        executor.execute(() -> cardDao.update(card));
    }

    /**
     * Deletes a card from the database asynchronously.
     *
     * @param card the card to delete
     */
    public void delete(Card card) {
        executor.execute(() -> cardDao.delete(card));
    }
}
