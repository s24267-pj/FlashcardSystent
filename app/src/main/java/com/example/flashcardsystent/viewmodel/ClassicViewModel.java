package com.example.flashcardsystent.viewmodel;

/**
 * ViewModel driving the classic flashcard mode where cards are shown sequentially.
 */

import android.app.Application; // needed for AndroidViewModel

import androidx.annotation.NonNull;          // for constructor
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;  // Android-specific ViewModel
import androidx.lifecycle.LifecycleOwner;   // used for observing data
import androidx.lifecycle.LiveData;        // observable data holder
import androidx.lifecycle.MutableLiveData; // mutable LiveData

import com.example.flashcardsystent.data.Card;          // flashcard model
import com.example.flashcardsystent.data.CardRepository; // repository for cards

import java.util.LinkedList; // simple queue implementation
import java.util.Queue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassicViewModel extends AndroidViewModel {

    /** Repository used to fetch cards */
    private final CardRepository repository;
    /** Queue of cards to display */
    private final Queue<Card> cardQueue = new LinkedList<>();
    /** Currently displayed card */
    private final MutableLiveData<Card> currentCard = new MutableLiveData<>();

    /** All cards loaded for the current session */
    private List<Card> allCards = new ArrayList<>();
    /** Count how many times each card was marked as unknown */
    private final Map<Integer, Integer> dontKnowCounts = new HashMap<>();
    /** Number of "I know" taps */
    private int knowClicks = 0;
    /** Number of "I don't know" taps */
    private int dontKnowClicks = 0;
    /** Total number of cards in the session */
    private int totalCount = 0;

    public ClassicViewModel(@NonNull Application application) {
        super(application);
        repository = new CardRepository(application); // create repository
    }

    public void loadCards(int deckId, LifecycleOwner lifecycleOwner) {
        repository.getCardsByDeck(deckId).observe(lifecycleOwner, cards -> {
            cardQueue.clear();
            knowClicks = 0;
            dontKnowClicks = 0;
            dontKnowCounts.clear();
            allCards = cards != null ? new ArrayList<>(cards) : new ArrayList<>();
            totalCount = allCards.size();

            if (totalCount > 0) {
                cardQueue.addAll(allCards);
                showNextCard();
            } else {
                currentCard.setValue(null);
            }
        });
    }

    public void onKnow() {
        knowClicks++;
        showNextCard();
    }

    public void onDontKnow() {
        Card current = currentCard.getValue();
        if (current != null) {
            dontKnowClicks++;
            int count = dontKnowCounts.getOrDefault(current.id, 0) + 1;
            dontKnowCounts.put(current.id, count);
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

    /** Total number of cards loaded for this session */
    public int getTotalCount() {
        return totalCount;
    }

    /** Number of times the user tapped "I know" */
    public int getKnowClicks() {
        return knowClicks;
    }

    /** Number of times the user tapped "I don't know" */
    public int getDontKnowClicks() {
        return dontKnowClicks;
    }

    /** Get how many times a card was marked as unknown */
    public int getDontKnowCount(int cardId) {
        return dontKnowCounts.getOrDefault(cardId, 0);
    }

    /**
     * Returns the card that was marked as unknown most often during this session.
     * May return {@code null} if no card was marked unknown.
     */
    @Nullable
    public Card getMostDifficultCard() {
        Card hardest = null;
        int max = 0;
        for (Card card : allCards) {
            int count = dontKnowCounts.getOrDefault(card.id, 0);
            if (count > max) {
                max = count;
                hardest = card;
            }
        }
        return hardest;
    }
}