package com.example.flashcardsystent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

/**
 * Simple Espresso tests verifying basic UI interactions.
 */
@RunWith(AndroidJUnit4.class)
public class HomeFragmentTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    /**
     * Checks that the classic mode button is visible on the home screen.
     */
    public void homeDisplaysClassicButton() {
        onView(withId(R.id.button_mode_classic))
                .check(matches(isDisplayed()))
                .check(matches(withText(R.string.mode_classic)));
    }

    @Test
    /**
     * Navigates to the summary screen using the bottom navigation bar
     * and verifies that statistics text is shown.
     */
    public void bottomNavOpensSummary() {
        onView(withId(R.id.navigation_summary)).perform(click());
        onView(withId(R.id.text_total_games)).check(matches(isDisplayed()));
    }
}