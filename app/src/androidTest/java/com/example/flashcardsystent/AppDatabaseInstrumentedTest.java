package com.example.flashcardsystent;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.flashcardsystent.data.AppDatabase;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented tests for application context and database singleton.
 */
@RunWith(AndroidJUnit4.class)
public class AppDatabaseInstrumentedTest {

    @Test
    /**
     * Verifies the app context has the expected package name.
     */
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.flashcardsystent", appContext.getPackageName());
    }

    @Test
    /**
     * Ensures that the database returns the same singleton instance
     * for successive calls with the same context.
     */
    public void databaseSingleton_returnsSameInstance() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        AppDatabase db1 = AppDatabase.getInstance(context);
        AppDatabase db2 = AppDatabase.getInstance(context);
        assertSame(db1, db2);
    }
}