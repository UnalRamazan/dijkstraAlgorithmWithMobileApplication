package com.example.dijkstraalgorithm.PageViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {

    private MutableLiveData<String> fromInput = new MutableLiveData<>();
    private MutableLiveData<String> toInput = new MutableLiveData<>();
    private MutableLiveData<String> isDelete = new MutableLiveData<>();

    public LiveData<String> getFromInput() {
        return fromInput;
    }

    public void setFromInput(String from) {
        fromInput.setValue(from);
    }

    public LiveData<String> getToInput() {
        return toInput;
    }

    public void setToInput(String to) {
        toInput.setValue(to);
    }

    public LiveData<String> getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String delete) {
        isDelete.setValue(delete);
    }
}