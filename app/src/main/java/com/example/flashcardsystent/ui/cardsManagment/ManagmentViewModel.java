package com.example.flashcardsystent.ui.cardsManagment;

/**
 * ViewModel containing simple text displayed on the deck management screen.
 * Added comments explain how the MutableLiveData field is initialized and
 * exposed.
 */

// Observable value used by the UI
import androidx.lifecycle.LiveData;
// Mutable version allowing updates
import androidx.lifecycle.MutableLiveData;
// Base class for holding UI data
import androidx.lifecycle.ViewModel;

public class ManagmentViewModel extends ViewModel {

    // LiveData containing a short description text
    private final MutableLiveData<String> mText;

    /**
     * Initialize the text value with a default string.
     */
    public ManagmentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    /**
     * Provide immutable access to the text value.
     */
    public LiveData<String> getText() {
        return mText;
    }
}