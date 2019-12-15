package com.example.fampayassignment;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class FirebaseRepositoryImpl implements FirebaseRepository {
    private Context context;
    private FirebaseAuth mAuth;
    private MutableLiveData<FirebaseUser> mutableLiveData;
    private static final String TAG = "FirebaseRepositoryImpl";
    public FirebaseRepositoryImpl(Context context) {
        this.context = context;
        this.mAuth=FirebaseAuth.getInstance();
        this.mutableLiveData=new MutableLiveData<>();
    }

    @Override
    public void loginUser(String email, String password) {
      Task<AuthResult> task=  mAuth.signInWithEmailAndPassword(email,password);
               task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){
                           mutableLiveData.postValue(task.getResult().getUser());
                       }
                       else{
                           Log.d(TAG, "onComplete: "+task.getException().getMessage());
mutableLiveData.postValue(null);
                       }
                   }
               });
    }

    @Override
    public void registerUser(String email, String password, UsersPojo usersPojo) {

    }

    @Override
    public MutableLiveData<FirebaseUser> getLoginFirebaseUser() {
        return mutableLiveData;
    }
}
