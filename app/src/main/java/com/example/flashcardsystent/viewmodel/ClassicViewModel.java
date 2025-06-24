package com.example.flashcardsystent.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.flashcardsystent.data.Card;
import com.example.flashcardsystent.data.CardRepository;

import java.util.LinkedList;
import java.util.Queue;

public class ClassicViewModel extends AndroidViewModel {

    private final CardRepository repository;
    private final Queue<Card> cardQueue = new LinkedList<>();
    private final MutableLiveData<Card> currentCard = new MutableLiveData<>();

    public ClassicViewModel(@NonNull Application application) {
        super(application);
        repository = new CardRepository(application);
    }

    public void loadCards(int deckId, LifecycleOwner lifecycleOwner) {
        repository.getCardsByDeck(deckId).observe(lifecycleOwner, cards -> {
            cardQueue.clear();
            if (cards != null && !cards.isEmpty()) {
                cardQueue.addAll(cards);
                showNextCard();
            } else {
                currentCard.setValue(null);
            }
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
