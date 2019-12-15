package com.example.fampayassignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.fampayassignment.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;
    private FirebaseAuth mAuth;
    private Context context;
    private LoginViewModel loginViewModel;
    private static final String TAG = "LoginActivity";
    private GoogleSignInClient mGoogleSignInClient;
    private ActivityLoginBinding activityLoginBinding;
    private int check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        context = LoginActivity.this;
        check=getIntent().getIntExtra(Constants.ADD_USERS,0);
        if(SharedPreferenceImpl.getInstance().get(Constants.HAS_USERS,context).equals("y") && check==0){

            startActivity(new Intent(context,MainActivity.class));
            finish();
        }
        final AccountManager accountManager=AccountManager.get(context);

        mAuth = FirebaseAuth.getInstance();
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        activityLoginBinding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginViewModel.login(activityLoginBinding.emailEditText.getText().toString(), activityLoginBinding.passwordEditText.getText().toString());
            }
        });
        loginViewModel.getLoginMutableData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Log.d(TAG, "onChanged:   login");
                    LoginPojo loginPojo=null;
                    try {
                          loginPojo=new LoginPojo(firebaseUser.getEmail(),EncryptionClass.encrypt(activityLoginBinding.passwordEditText.getText().toString()));
                        Log.d(TAG, "onChanged: pwd"+loginPojo.getEncryptedPwd());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    loginViewModel.insert(loginPojo);
                    AccountManager accountManager = AccountManager.get(LoginActivity.this); //this is Activity
                    Account account = new Account(firebaseUser.getEmail(),"com.example.fampayassignment");

                    boolean success = accountManager.addAccountExplicitly(account,activityLoginBinding.passwordEditText.getText().toString(),null);
                    if(success){
                        Log.d(TAG,"Account created");
                        SharedPreferenceImpl.getInstance().save(Constants.HAS_USERS,"y",LoginActivity.this);
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        finish();
                    }else{
                        Log.d(TAG,"Account creation failed. Look at previous logs to investigate");
                    }
                } else {
                    Log.d(TAG, "onChanged: no login");

                }
            }
        });
        activityLoginBinding.CreateAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });


    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Log.d(TAG, "onActivityResult: " + e.getMessage());
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                            Toast.makeText(LoginActivity.this, "user hai " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
}
