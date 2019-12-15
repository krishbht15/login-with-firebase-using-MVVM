package com.example.fampayassignment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class LoginViewModel extends AndroidViewModel {
    private FirebaseRepository firebaseRepository;
    private LoginRepository repository;
    private LiveData<List<LoginPojo>> allallLoggedinUsers;
    public LoginViewModel(@NonNull Application application) {
        super(application);
        this.firebaseRepository=new FirebaseRepositoryImpl(application.getBaseContext());
        this.repository=new LoginRepository(application);
    }
    public void login(String email,String password){
            firebaseRepository.loginUser(email,password);
    }
    public MutableLiveData<FirebaseUser> getLoginMutableData(){
     return    firebaseRepository.getLoginFirebaseUser();
    }


    public void insert(LoginPojo loginPojo) {
        repository.insert(loginPojo);
    }



}
