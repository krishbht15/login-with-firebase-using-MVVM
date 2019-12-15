package com.example.fampayassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.fampayassignment.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
private FirebaseUser user;
private FirebaseAuth mAuth;
private RegisterViewModel registerViewModel;
private ActivityRegisterBinding activityRegisterBinding;
    private static final String TAG = "RegisterActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      activityRegisterBinding= DataBindingUtil.setContentView(this,R.layout.activity_register);
       registerViewModel= ViewModelProviders.of(this).get(RegisterViewModel.class);

        user=null;
        mAuth=FirebaseAuth.getInstance();
        activityRegisterBinding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
registerViewModel.register(activityRegisterBinding.emailEditText.getText().toString(),activityRegisterBinding.passwordEditText.getText().toString());
            }
        });
registerViewModel.getLoginMutableData().observe(this, new Observer<FirebaseUser>() {
    @Override
    public void onChanged(FirebaseUser firebaseUser) {
        if(firebaseUser!=null){
            UsersPojo usersPojo=new UsersPojo(activityRegisterBinding.cityEditText.getText().toString(),activityRegisterBinding.nameEditText.getText().toString(),activityRegisterBinding.emailEditText.getText().toString(),activityRegisterBinding.phoneEditText.getText().toString());
            registerViewModel.addDatabase(usersPojo);

        }
        else{
            Log.d(TAG, "onChanged: No login ");
        }
    }
});
registerViewModel.getRegisterLiveData().observe(this, new Observer<UsersPojo>() {
    @Override
    public void onChanged(UsersPojo usersPojo) {
        if(usersPojo!=null){
            Log.d(TAG, "onChanged: fulllllllllllll");
        }
    }
});
    }
}
