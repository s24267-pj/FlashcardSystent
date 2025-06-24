package com.example.flashcardsystent.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.flashcardsystent.repository.IDeckRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeckRepositoryImpl implements IDeckRepository {

    private final DeckDao deckDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public DeckRepositoryImpl(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        deckDao = db.deckDao();
    }

    @Override
    public LiveData<List<Deck>> getAllDecks() {
        return deckDao.getAllDecks();
    }

    @Override
    public void insert(Deck deck) {
        executor.execute(() -> deckDao.insert(deck));
    }

    @Override
    public void update(Deck deck) {
        executor.execute(() -> deckDao.update(deck));
    }

    @Override
    public void delete(Deck deck) {
        executor.execute(() -> deckDao.delete(deck));
    }
}
