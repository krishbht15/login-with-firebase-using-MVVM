package com.example.fampayassignment;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

public interface FirebaseRepository {

    public void loginUser(String email,String password);
    public void registerUser(String email,String password    );
    public void addDatabase(UsersPojo usersPojo);
    public MutableLiveData<FirebaseUser> getLoginFirebaseUser();

   public MutableLiveData<UsersPojo> getRegisterLiveData();
//    public void fetchData();
}
