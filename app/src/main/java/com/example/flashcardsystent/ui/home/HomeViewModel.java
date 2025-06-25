package com.example.flashcardsystent.ui.home;

/**
 * Simple {@link androidx.lifecycle.ViewModel ViewModel} holding text for the
 * home screen. Each import and statement below is documented so beginners can
 * easily understand the purpose of every line.
 */

// Observable data holder that allows the UI to react to changes
import androidx.lifecycle.LiveData;
// Mutable version of LiveData so the value can be updated
import androidx.lifecycle.MutableLiveData;
// Base class for maintaining UI-related data
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    // LiveData containing the current message to show on the home screen
    private final MutableLiveData<String> mText;

    /**
     * Initialize the text with a default message.
     */
    public HomeViewModel() {
        // Create the LiveData instance
        mText = new MutableLiveData<>();
        // Set a default value so observers immediately receive something
        mText.setValue("This is home fragment");
    }

    /**
     * Expose the immutable LiveData so the UI can observe changes.
     */
    public LiveData<String> getText() {
        return mText;
    }
}