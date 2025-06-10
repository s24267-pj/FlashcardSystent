package com.example.flashcardsystent.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.flashcardsystent.data.Card;
import com.example.flashcardsystent.data.CardRepository;

import java.util.List;

public class CardViewModel extends AndroidViewModel {
    private final CardRepository repository;

    public CardViewModel(@NonNull Application application) {
        super(application);
        repository = new CardRepository(application);
    }

    public LiveData<List<Card>> getCardsByDeck(int deckId) {
        return repository.getCardsByDeck(deckId);
    }

    public void insert(Card card) {
        repository.insert(card);
    }

    public void update(Card card) {
        repository.update(card);
    }

    public void delete(Card card) {
        repository.delete(card);
    }
}
