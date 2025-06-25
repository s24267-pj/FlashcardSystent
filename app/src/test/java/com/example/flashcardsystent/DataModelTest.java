package com.example.flashcardsystent;

import com.example.flashcardsystent.data.Card;
import com.example.flashcardsystent.data.Deck;
import com.example.flashcardsystent.data.QuizResult;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for simple data model classes.
 */
public class DataModelTest {

    @Test
    /**
     * Verifies that the Card constructor properly assigns all fields.
     */
    public void cardConstructor_setsValues() {
        Card card = new Card(2, "front", "back");
        assertEquals(2, card.deckId);
        assertEquals("front", card.front);
        assertEquals("back", card.back);
    }

    @Test
    /**
     * Ensures the QuizResult constructor stores the provided quiz data.
     */
    public void quizResultConstructor_initializesFields() {
        QuizResult result = new QuizResult(1, 5, 10, 100L);
        assertEquals(1, result.deckId);
        assertEquals(5, result.correct);
        assertEquals(10, result.total);
        assertEquals(100L, result.timestamp);
    }

    @Test
    /**
     * Confirms that creating a Deck sets the given name.
     */
    public void deckConstructor_setsName() {
        Deck deck = new Deck("Test Deck");
        assertEquals("Test Deck", deck.name);
    }

    @Test
    /**
     * Checks the default constructor of Card for initial values of fields.
     */
    public void cardDefaultConstructor_hasDefaultValues() {
        Card card = new Card();
        assertEquals(0, card.id);
        assertEquals(0, card.deckId);
        assertNull(card.front);
        assertNull(card.back);
    }
}