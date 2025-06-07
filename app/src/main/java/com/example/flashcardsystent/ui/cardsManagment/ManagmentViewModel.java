package com.example.flashcardsystent.ui.cardsManagment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ManagmentViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ManagmentViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}