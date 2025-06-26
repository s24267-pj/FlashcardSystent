package com.example.flashcardsystent.data;

/**
 * Room database singleton used throughout the app.
 */

import android.content.Context; // application context for DB creation
import androidx.room.Database;   // Room annotation defining tables
import androidx.room.Room;       // database builder
import androidx.room.RoomDatabase; // base class for Room databases

// New entity and DAO for classic learning results
import com.example.flashcardsystent.data.ClassicResult;
import com.example.flashcardsystent.data.ClassicResultDao;

@Database(entities = {Deck.class, Card.class, QuizResult.class, ClassicResult.class}, version = 6)
public abstract class AppDatabase extends RoomDatabase {

    /** Singleton instance of the database */
    private static AppDatabase INSTANCE;

    public abstract DeckDao deckDao();
    public abstract CardDao cardDao();

    public abstract QuizResultDao quizResultDao();

    public abstract ClassicResultDao classicResultDao();

    /**
     * Returns the single database instance creating it if necessary.
     */
    public static synchronized AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            // create the database with name "flashcards-db"
            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "flashcards-db"
                    )
                    .fallbackToDestructiveMigration() // drop tables on incompatible schema
                    .build();
        }
        return INSTANCE;
    }

}