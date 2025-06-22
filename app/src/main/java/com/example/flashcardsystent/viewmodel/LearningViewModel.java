package com.example.flashcardsystent.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.flashcardsystent.data.Card;
import com.example.flashcardsystent.data.CardRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class LearningViewModel extends AndroidViewModel {

    private final CardRepository repository;
    private final Queue<Card> cardQueue = new LinkedList<>();
    private final MutableLiveData<Card> currentCard = new MutableLiveData<>();

    public LearningViewModel(@NonNull Application application) {
        super(application);
        repository = new CardRepository(application);
    }

    public void loadCards(int deckId) {
        repository.getCardsByDeck(deckId).observeForever(cards -> {
            cardQueue.clear();
            cardQueue.addAll(cards);
            showNextCard();
        });
    }

    public void onKnow() {
        showNextCard();
    }

    public void onDontKnow() {
        Card current = currentCard.getValue();
        if (current != null) {
            cardQueue.offer(current);
        }
        showNextCard();
    }

    private void showNextCard() {
        currentCard.setValue(cardQueue.poll());
    }

    public LiveData<Card> getCurrentCard() {
        return currentCard;
    }
}
