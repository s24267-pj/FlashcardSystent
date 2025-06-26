package com.example.flashcardsystent.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Room database singleton used throughout the app.
 * Holds references to all DAOs and provides a global access point.
 */
@Database(entities = {Deck.class, Card.class, QuizResult.class, ClassicResult.class}, version = 6)
public abstract class AppDatabase extends RoomDatabase {

    /** Singleton instance of the database. */
    private static AppDatabase INSTANCE;

    /**
     * Returns DAO for deck entity.
     * @return DeckDao instance
     */
    public abstract DeckDao deckDao();

    /**
     * Returns DAO for card entity.
     * @return CardDao instance
     */
    public abstract CardDao cardDao();

    /**
     * Returns DAO for quiz results.
     * @return QuizResultDao instance
     */
    public abstract QuizResultDao quizResultDao();

    /**
     * Returns DAO for classic learning results.
     * @return ClassicResultDao instance
     */
    public abstract ClassicResultDao classicResultDao();

    /**
     * Returns the single database instance, creating it if necessary.
     * Uses application context to prevent memory leaks.
     *
     * @param context application context
     * @return singleton AppDatabase instance
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "flashcards-db"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
