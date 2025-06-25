package com.example.flashcardsystent.viewmodel;

/**
 * ViewModel driving the classic flashcard mode where cards are shown sequentially.
 */

import android.app.Application; // needed for AndroidViewModel

import androidx.annotation.NonNull;          // for constructor
import androidx.lifecycle.AndroidViewModel;  // Android-specific ViewModel
import androidx.lifecycle.LifecycleOwner;   // used for observing data
import androidx.lifecycle.LiveData;        // observable data holder
import androidx.lifecycle.MutableLiveData; // mutable LiveData

import com.example.flashcardsystent.data.Card;          // flashcard model
import com.example.flashcardsystent.data.CardRepository; // repository for cards

import java.util.LinkedList; // simple queue implementation
import java.util.Queue;

public class ClassicViewModel extends AndroidViewModel {

    /** Repository used to fetch cards */
    private final CardRepository repository;
    /** Queue of cards to display */
    private final Queue<Card> cardQueue = new LinkedList<>();
    /** Currently displayed card */
    private final MutableLiveData<Card> currentCard = new MutableLiveData<>();

    public ClassicViewModel(@NonNull Application application) {
        super(application);
        repository = new CardRepository(application); // create repository
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
        return currentCard; // observable current card
    }
}