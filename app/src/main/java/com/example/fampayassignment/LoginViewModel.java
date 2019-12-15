package com.example.fampayassignment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends AndroidViewModel {
    private FirebaseRepository firebaseRepository;
    public LoginViewModel(@NonNull Application application) {
        super(application);
        this.firebaseRepository=new FirebaseRepositoryImpl(application.getBaseContext());
    }
    public void login(String email,String password){
            firebaseRepository.loginUser(email,password);
    }
    public MutableLiveData<FirebaseUser> getLoginMutableData(){
     return    firebaseRepository.getLoginFirebaseUser();
    }

}
