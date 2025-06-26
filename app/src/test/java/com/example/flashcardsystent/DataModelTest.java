package com.example.flashcardsystent;

import com.example.flashcardsystent.data.Card;
import com.example.flashcardsystent.data.Deck;
import com.example.flashcardsystent.data.QuizResult;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for simple data model classes: {@link Card}, {@link Deck}, and {@link QuizResult}.
 * <p>
 * These tests verify that constructors assign values correctly and that default constructors produce expected states.
 */
public class DataModelTest {

    /**
     * Verifies that the {@link Card} constructor correctly sets the deck ID and front/back values.
     */
    @Test
    public void cardConstructor_setsValues() {
        Card card = new Card(2, "front", "back");
        assertEquals(2, card.deckId);
        assertEquals("front", card.front);
        assertEquals("back", card.back);
    }

    /**
     * Ensures that the {@link QuizResult} constructor stores all passed values accurately.
     */
    @Test
    public void quizResultConstructor_initializesFields() {
        QuizResult result = new QuizResult(1, 5, 6, 10, 100L);
        assertEquals(1, result.deckId);
        assertEquals(5, result.correct);
        assertEquals(6, result.wrong);
        assertEquals(10, result.total);
        assertEquals(100L, result.timestamp);
    }

    /**
     * Confirms that the {@link Deck} constructor properly sets the deck's name.
     */
    @Test
    public void deckConstructor_setsName() {
        Deck deck = new Deck("Test Deck");
        assertEquals("Test Deck", deck.name);
    }

    /**
     * Validates that the default constructor of {@link Card} sets zero/empty values.
     */
    @Test
    public void cardDefaultConstructor_hasDefaultValues() {
        Card card = new Card();
        assertEquals(0, card.id);
        assertEquals(0, card.deckId);
        assertNull(card.front);
        assertNull(card.back);
    }
}
