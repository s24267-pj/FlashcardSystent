package com.example.flashcardsystent.viewmodel;

/**
 * ViewModel managing deck operations for UI fragments.
 */

import android.app.Application; // required by AndroidViewModel

import androidx.annotation.NonNull; // for ViewModel
import androidx.lifecycle.AndroidViewModel; // ViewModel tied to application
import androidx.lifecycle.LiveData; // observable lists

import com.example.flashcardsystent.data.AppDatabase; // database singleton
import com.example.flashcardsystent.data.Deck;       // entity class

import java.util.List; // list of decks

public class DeckViewModel extends AndroidViewModel {

    /** Observable list of all decks */
    private final LiveData<List<Deck>> allDecks;
    /** Database instance used for operations */
    private final AppDatabase db;

    public DeckViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application); // open DB
        allDecks = db.deckDao().getAllDecks();    // observe decks
    }

    public LiveData<List<Deck>> getAllDecks() {
        return allDecks; // expose list to UI
    }

    public void insert(Deck deck) {
        new Thread(() -> db.deckDao().insert(deck)).start(); // run async
    }

    public void delete(Deck deck) {
        new Thread(() -> db.deckDao().delete(deck)).start(); // run async
    }

    public void update(Deck deck) {
        new Thread(() -> db.deckDao().update(deck)).start(); // run async
    }
}