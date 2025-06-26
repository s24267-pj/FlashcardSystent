package com.example.flashcardsystent.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.flashcardsystent.data.AppDatabase;
import com.example.flashcardsystent.data.Card;
import com.example.flashcardsystent.data.CardDao;
import com.example.flashcardsystent.data.CardRepository;

import java.util.List;

/**
 * ViewModel exposing card-related operations to the UI layer.
 * Provides methods for inserting, updating and deleting cards
 * as well as querying them by deck ID or individual ID.
 */
public class CardViewModel extends AndroidViewModel {

    /** Repository mediating between DB and UI */
    private final CardRepository repository;

    /** DAO used for single card queries */
    private final CardDao cardDao;

    /**
     * Constructs a CardViewModel and initializes the repository and DAO.
     *
     * @param application the application context required by AndroidViewModel
     */
    public CardViewModel(@NonNull Application application) {
        super(application);
        repository = new CardRepository(application);
        cardDao = AppDatabase.getInstance(application).cardDao();
    }

    /**
     * Returns a list of cards belonging to a specific deck.
     *
     * @param deckId the ID of the deck to filter cards by
     * @return observable list of cards from the deck
     */
    public LiveData<List<Card>> getCardsByDeck(int deckId) {
        return repository.getCardsByDeck(deckId);
    }

    /**
     * Returns a single card by its unique ID.
     *
     * @param id the card ID
     * @return observable card with the given ID
     */
    public LiveData<Card> getCardById(int id) {
        return cardDao.getCardById(id);
    }

    /**
     * Inserts a new card into the database.
     *
     * @param card the card to insert
     */
    public void insert(Card card) {
        repository.insert(card);
    }

    /**
     * Updates an existing card in the database.
     *
     * @param card the card with updated values
     */
    public void update(Card card) {
        repository.update(card);
    }

    /**
     * Deletes a card from the database.
     *
     * @param card the card to delete
     */
    public void delete(Card card) {
        repository.delete(card);
    }
}
