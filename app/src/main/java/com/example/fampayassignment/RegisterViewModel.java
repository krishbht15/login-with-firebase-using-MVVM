package com.example.fampayassignment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

public class RegisterViewModel extends AndroidViewModel {
    private FirebaseRepository firebaseRepository;
    private RegisterRepository registerRepository;
    public RegisterViewModel(@NonNull Application application) {
        super(application);
        this.firebaseRepository=new FirebaseRepositoryImpl(application.getBaseContext());
    }
    public void register(String email,String pass){
        firebaseRepository.registerUser(email,pass);
    }
    public void addDatabase(UsersPojo usersPojo){
        firebaseRepository.addDatabase(usersPojo);
    }
    public MutableLiveData<UsersPojo> getRegisterLiveData(){
        return firebaseRepository.getRegisterLiveData();
    }
    public void insert(LoginPojo loginPojo) {
        registerRepository.insert(loginPojo);
    }
    public MutableLiveData<FirebaseUser> getLoginMutableData(){
        return firebaseRepository.getLoginFirebaseUser();
    }
}
