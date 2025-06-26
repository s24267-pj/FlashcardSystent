package com.example.flashcardsystent.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.flashcardsystent.data.AppDatabase;
import com.example.flashcardsystent.data.Card;
import com.example.flashcardsystent.data.CardRepository;
import com.example.flashcardsystent.data.ClassicResult;
import com.example.flashcardsystent.data.ClassicResultDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ViewModel managing state and logic for the classic learning mode.
 * Handles showing cards in sequence and tracking performance (correct/incorrect).
 */
public class ClassicViewModel extends AndroidViewModel {

    private final CardRepository repository;
    private final Queue<Card> cardQueue = new LinkedList<>();
    private final MutableLiveData<Card> currentCard = new MutableLiveData<>();
    private List<Card> allCards = new ArrayList<>();
    private final Map<Integer, Integer> dontKnowCounts = new HashMap<>();
    private int knowClicks = 0;
    private int dontKnowClicks = 0;
    private int totalCount = 0;
    private final MutableLiveData<Boolean> finished = new MutableLiveData<>(false);
    private int correctCount = 0;
    private int wrongCount = 0;
    private final Map<String, Integer> wrongMap = new HashMap<>();
    private final ClassicResultDao resultDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * Constructs a new ClassicViewModel with access to repositories and DAOs.
     *
     * @param application the application context
     */
    public ClassicViewModel(@NonNull Application application) {
        super(application);
        repository = new CardRepository(application);
        resultDao = AppDatabase.getInstance(application).classicResultDao();
    }

    /**
     * Loads all cards from a given deck and prepares the session queue.
     *
     * @param deckId ID of the selected deck
     * @param lifecycleOwner the lifecycle context to observe data
     */
    public void loadCards(int deckId, LifecycleOwner lifecycleOwner) {
        correctCount = 0;
        wrongCount = 0;
        wrongMap.clear();
        finished.setValue(false);

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

    /**
     * Marks the current card as known and proceeds to the next one.
     * If all cards are completed, sets the session as finished.
     */
    public void onKnow() {
        correctCount++;
        knowClicks++;
        showNextCard();
        if (currentCard.getValue() == null) {
            finished.setValue(true);
        }
    }

    /**
     * Marks the current card as unknown, reinserts it into the queue,
     * and proceeds to the next one. Updates statistics.
     */
    public void onDontKnow() {
        Card current = currentCard.getValue();
        if (current != null) {
            wrongCount++;
            wrongMap.put(current.front, wrongMap.getOrDefault(current.front, 0) + 1);
            dontKnowClicks++;
            int count = dontKnowCounts.getOrDefault(current.id, 0) + 1;
            dontKnowCounts.put(current.id, count);
            cardQueue.offer(current);
        }
        showNextCard();
        if (currentCard.getValue() == null) {
            finished.setValue(true);
        }
    }

    /**
     * Updates the current card by polling the queue.
     */
    private void showNextCard() {
        currentCard.setValue(cardQueue.poll());
    }

    /**
     * @return LiveData that becomes true when all cards have been shown
     */
    public LiveData<Boolean> getFinished() {
        return finished;
    }

    /**
     * Saves the result of the session to the database asynchronously.
     *
     * @param deckId ID of the deck the session was based on
     */
    public void saveResult(int deckId) {
        String hardest = wrongMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("-");
        ClassicResult result = new ClassicResult(deckId, correctCount, wrongCount, hardest, System.currentTimeMillis());
        executor.execute(() -> resultDao.insert(result));
    }

    /**
     * @return LiveData representing the currently visible card
     */
    public LiveData<Card> getCurrentCard() {
        return currentCard;
    }

    /**
     * @return the total number of cards loaded for this session
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * @return the number of times the user clicked "I know"
     */
    public int getKnowClicks() {
        return knowClicks;
    }

    /**
     * @return the number of times the user clicked "I don't know"
     */
    public int getDontKnowClicks() {
        return dontKnowClicks;
    }

    /**
     * Returns how many times a specific card was marked as unknown.
     *
     * @param cardId the ID of the card
     * @return number of "don't know" interactions for the card
     */
    public int getDontKnowCount(int cardId) {
        return dontKnowCounts.getOrDefault(cardId, 0);
    }

    /**
     * @return the card most often marked as unknown, or null if none
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
