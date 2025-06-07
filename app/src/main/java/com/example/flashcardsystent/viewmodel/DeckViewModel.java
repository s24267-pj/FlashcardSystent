package com.example.flashcardsystent.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.flashcardsystent.data.AppDatabase;
import com.example.flashcardsystent.data.Deck;

import java.util.List;

public class DeckViewModel extends AndroidViewModel {

    private final LiveData<List<Deck>> allDecks;
    private final AppDatabase db;

    public DeckViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
        allDecks = db.deckDao().getAllDecks();
    }

    public LiveData<List<Deck>> getAllDecks() {
        return allDecks;
    }

    public void insert(Deck deck) {
        new Thread(() -> db.deckDao().insert(deck)).start();
    }

    public void delete(Deck deck) {
        new Thread(() -> db.deckDao().delete(deck)).start();
    }

    public void update(Deck deck) {
        new Thread(() -> db.deckDao().update(deck)).start();
    }
}
