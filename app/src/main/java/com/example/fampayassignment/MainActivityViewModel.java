package com.example.fampayassignment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    private MainActivityRepository mainActivityRepository;
    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.mainActivityRepository=new MainActivityRepository(application);

    }
    public LiveData<List<LoginPojo>> getLoggedinUSers(){
        return mainActivityRepository.getAllNotes();
    }
}
