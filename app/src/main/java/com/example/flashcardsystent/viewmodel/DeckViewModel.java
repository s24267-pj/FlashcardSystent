package com.example.flashcardsystent.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.flashcardsystent.data.AppDatabase;
import com.example.flashcardsystent.data.Deck;

import java.util.List;

/**
 * ViewModel managing operations on flashcard decks.
 * Exposes LiveData for observing all decks and provides async insert, update, delete operations.
 */
public class DeckViewModel extends AndroidViewModel {

    /** Observable list of all decks */
    private final LiveData<List<Deck>> allDecks;

    /** Database instance used for direct DAO access */
    private final AppDatabase db;

    /**
     * Constructs the ViewModel and initializes database access.
     *
     * @param application the application context
     */
    public DeckViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
        allDecks = db.deckDao().getAllDecks();
    }

    /**
     * Returns a LiveData stream of all decks stored in the database.
     *
     * @return observable list of decks
     */
    public LiveData<List<Deck>> getAllDecks() {
        return allDecks;
    }

    /**
     * Inserts a new deck asynchronously into the database.
     *
     * @param deck the deck to insert
     */
    public void insert(Deck deck) {
        new Thread(() -> db.deckDao().insert(deck)).start();
    }

    /**
     * Deletes a deck asynchronously from the database.
     *
     * @param deck the deck to delete
     */
    public void delete(Deck deck) {
        new Thread(() -> db.deckDao().delete(deck)).start();
    }

    /**
     * Updates an existing deck asynchronously in the database.
     *
     * @param deck the deck to update
     */
    public void update(Deck deck) {
        new Thread(() -> db.deckDao().update(deck)).start();
    }
}
