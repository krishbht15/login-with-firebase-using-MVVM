package com.example.fampayassignment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    private MainActivityRepository mainActivityRepository;
    private FirebaseRepository firebaseRepository;
    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.mainActivityRepository=new MainActivityRepository(application);
        firebaseRepository=new FirebaseRepositoryImpl(application.getBaseContext());
    }
    public LiveData<List<LoginPojo>> getLoggedinUSers(){
        return mainActivityRepository.getAllNotes();
    }
    public void loginUser(String email,String pwd){
        firebaseRepository.loginUser(email,pwd);
    }
    public void deleteUser(LoginPojo loginPojo){
        mainActivityRepository.delete(loginPojo);
    }
    public MutableLiveData<FirebaseUser> getLoginLiveData(){
        return firebaseRepository.getLoginFirebaseUser();
    }
}
