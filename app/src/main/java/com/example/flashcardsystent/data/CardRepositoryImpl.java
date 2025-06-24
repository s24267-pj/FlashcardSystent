package com.example.flashcardsystent.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.flashcardsystent.repository.ICardRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CardRepositoryImpl implements ICardRepository {

    private final CardDao cardDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public CardRepositoryImpl(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        cardDao = db.cardDao();
    }

    @Override
    public LiveData<List<Card>> getCardsByDeck(int deckId) {
        return cardDao.getCardsByDeck(deckId);
    }

    @Override
    public void insert(Card card) {
        executor.execute(() -> cardDao.insert(card));
    }

    @Override
    public void update(Card card) {
        executor.execute(() -> cardDao.update(card));
    }

    @Override
    public void delete(Card card) {
        executor.execute(() -> cardDao.delete(card));
    }

    @Override
    public LiveData<Card> getCardById(int id) {
        return cardDao.getCardById(id);
    }
}
