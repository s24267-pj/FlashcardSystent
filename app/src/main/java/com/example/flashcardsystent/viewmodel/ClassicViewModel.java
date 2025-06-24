package com.example.flashcardsystent.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.flashcardsystent.data.Card;
import com.example.flashcardsystent.data.CardRepositoryImpl;
import com.example.flashcardsystent.util.CardQueueManager;

public class ClassicViewModel extends AndroidViewModel {

    private final CardRepositoryImpl repository;
    private final CardQueueManager manager = new CardQueueManager();
    private final MutableLiveData<Card> currentCard = new MutableLiveData<>();
    private final MutableLiveData<Boolean> showingFront = new MutableLiveData<>(true);

    public ClassicViewModel(@NonNull Application application) {
        super(application);
        repository = new CardRepositoryImpl(application);
    }

    public void loadCards(int deckId, LifecycleOwner owner) {
        repository.getCardsByDeck(deckId).observe(owner, cards -> {
            manager.setCards(cards);
            resetFlip();
            currentCard.setValue(manager.getCurrentCard());
        });
    }

    public void onKnow() {
        manager.markAsKnown();
        resetFlip();
        currentCard.setValue(manager.getCurrentCard());
    }

    public void onDontKnow() {
        manager.markAsUnknown();
        resetFlip();
        currentCard.setValue(manager.getCurrentCard());
    }

    public LiveData<Card> getCurrentCard() {
        return currentCard;
    }

    public LiveData<Boolean> isShowingFront() {
        return showingFront;
    }

    public void flipCard() {
        Boolean current = showingFront.getValue();
        showingFront.setValue(current == null || !current);
    }

    public void resetFlip() {
        showingFront.setValue(true);
    }
}
