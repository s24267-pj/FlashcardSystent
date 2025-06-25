package com.example.flashcardsystent.viewmodel;

/**
 * ViewModel exposing card-related operations to the UI layer. Provides methods
 * for inserting, updating and deleting cards as well as querying them by deck
 * or id.
 */

// Context used by AndroidViewModel subclasses
import android.app.Application;

// Annotation indicating parameter must be non-null
import androidx.annotation.NonNull;
// ViewModel implementation that has access to the Application instance
import androidx.lifecycle.AndroidViewModel;
// Lifecycle-aware observable data holder class
import androidx.lifecycle.LiveData;

// Singleton database that provides DAO instances
import com.example.flashcardsystent.data.AppDatabase;
// Data entity representing a flashcard
import com.example.flashcardsystent.data.Card;
// Data access object for performing queries on the Card table
import com.example.flashcardsystent.data.CardDao;
// Repository that mediates between ViewModel and database
import com.example.flashcardsystent.data.CardRepository;

import java.util.List; // list of cards

public class CardViewModel extends AndroidViewModel {
    /** Repository mediating between DB and UI */
    private final CardRepository repository;
    /** DAO used for single card queries */
    private final CardDao cardDao;

    public CardViewModel(@NonNull Application application) {
        // Call into AndroidViewModel to store the Application instance
        super(application);

        // Create repository which will manage async queries
        repository = new CardRepository(application);

        // Obtain a DAO for direct single-card lookups
        cardDao = AppDatabase.getInstance(application).cardDao();
    }

    public LiveData<List<Card>> getCardsByDeck(int deckId) {
        // Retrieve cards for the specified deck as LiveData so UI can observe
        // changes
        return repository.getCardsByDeck(deckId);
    }

    public LiveData<Card> getCardById(int id) {
        // Directly query the DAO for a single card by its id
        return cardDao.getCardById(id);
    }

    public void insert(Card card) {
        // Delegate insertion to the repository which handles threading
        repository.insert(card);
    }

    public void update(Card card) {
        // Update the given card's data in the database
        repository.update(card);
    }

    public void delete(Card card) {
        // Delete the specified card from the database
        repository.delete(card);
    }
}