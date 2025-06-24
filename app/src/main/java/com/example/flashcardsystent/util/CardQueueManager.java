package com.example.flashcardsystent.util;

import com.example.flashcardsystent.data.Card;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Manages a queue of flashcards for learning sessions.
 */
public class CardQueueManager {
    private final Queue<Card> queue = new LinkedList<>();
    private Card currentCard;

    public void setCards(Iterable<Card> cards) {
        queue.clear();
        for (Card card : cards) {
            queue.offer(card);
        }
        advance();
    }

    public void markAsKnown() {
        advance();
    }

    public void markAsUnknown() {
        if (currentCard != null) {
            queue.offer(currentCard);
        }
        advance();
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    private void advance() {
        currentCard = queue.poll();
    }

    public boolean isEmpty() {
        return currentCard == null;
    }
}
