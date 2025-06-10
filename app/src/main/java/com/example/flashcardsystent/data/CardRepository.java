package com.example.flashcardsystent.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CardRepository {
    private final CardDao cardDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public CardRepository(Application app) {
        AppDatabase db = AppDatabase.getInstance(app);
        cardDao = db.cardDao();
    }

    public LiveData<List<Card>> getCardsByDeck(int deckId) {
        return cardDao.getCardsByDeck(deckId);
    }

    public void insert(Card card) {
        executor.execute(() -> cardDao.insert(card));
    }

    public void update(Card card) {
        executor.execute(() -> cardDao.update(card));
    }

    public void delete(Card card) {
        executor.execute(() -> cardDao.delete(card));
    }
}
