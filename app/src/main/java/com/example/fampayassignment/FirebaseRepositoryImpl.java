package com.example.fampayassignment;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseRepositoryImpl implements FirebaseRepository {
    private Context context;
    private FirebaseAuth mAuth;
    private MutableLiveData<FirebaseUser> loginMutableLiveData;
    private static final String TAG = "FirebaseRepositoryImpl";
    private MutableLiveData<UsersPojo> registerLivedata;

    public FirebaseRepositoryImpl(Context context) {
        this.context = context;
        this.registerLivedata=new MutableLiveData<>();
        this.mAuth=FirebaseAuth.getInstance();
        this.loginMutableLiveData =new MutableLiveData<>();
    }

    @Override
    public void loginUser(String email, String password) {
      Task<AuthResult> task=  mAuth.signInWithEmailAndPassword(email,password);
               task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){
                           loginMutableLiveData.postValue(task.getResult().getUser());
                       }
                       else{
                           Log.d(TAG, "onComplete: "+task.getException().getMessage());
loginMutableLiveData.postValue(null);
                       }
                   }
               });
    }

    @Override
    public void addDatabase(final UsersPojo usersPojo) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Task<Void> myRef = database.getReference().child("users").child(usersPojo.getPhone()).setValue(usersPojo);
        myRef.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: hoyga");
                    registerLivedata.postValue(usersPojo);
                }
                else {
                    registerLivedata.postValue(null);
                    Log.d(TAG, "onComplete: nhi hua "+task.getException().getMessage());
                }
            }
        });
    }

    @Override
    public void registerUser(String email, String password) {
      Task<AuthResult >task=  mAuth.createUserWithEmailAndPassword(email,password);
         task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                 if (task.isSuccessful()) {
                     // Sign in success, update UI with the signed-in user's information
                     Log.d(TAG, "createUserWithEmail:success");
                     FirebaseUser user = mAuth.getCurrentUser();
                     loginMutableLiveData.postValue(user);

                 } else {
                     loginMutableLiveData.postValue(null);
                     // If sign in fails, display a message to the user.
                     Log.w(TAG, "createUserWithEmail:failure", task.getException());

                 }
             }
         });

    }

    @Override
    public MutableLiveData<FirebaseUser> getLoginFirebaseUser() {
        return loginMutableLiveData;
    }

    @Override
    public MutableLiveData<UsersPojo> getRegisterLiveData() {
        return registerLivedata;
    }
}
