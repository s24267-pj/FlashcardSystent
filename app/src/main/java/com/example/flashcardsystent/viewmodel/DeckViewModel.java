package com.example.flashcardsystent.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.flashcardsystent.data.Deck;
import com.example.flashcardsystent.data.DeckRepositoryImpl;
import com.example.flashcardsystent.repository.IDeckRepository;

import java.util.List;

public class DeckViewModel extends AndroidViewModel {

    private final IDeckRepository repository;

    public DeckViewModel(@NonNull Application application) {
        super(application);
        repository = new DeckRepositoryImpl(application);
    }

    public LiveData<List<Deck>> getAllDecks() {
        return repository.getAllDecks();
    }

    public void insert(Deck deck) {
        repository.insert(deck);
    }

    public void delete(Deck deck) {
        repository.delete(deck);
    }

    public void update(Deck deck) {
        repository.update(deck);
    }
}
