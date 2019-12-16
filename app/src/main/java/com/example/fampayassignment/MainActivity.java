package com.example.fampayassignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.fampayassignment.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel mainActivityViewModel;
    private ActivityMainBinding activityMainBinding;
    private ArrayList<LoginPojo> loginPojosList;
    private UsersPojo usersPojo;
    private String[] spinnerarray;
    private ArrayAdapter<String> spinnerAdapter;
    private int pos;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
//        Account[] accounts= AccountManager.get(MainActivity.this).getAccounts();
        loginPojosList = new ArrayList<>();
        activityMainBinding.addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                intent.putExtra(Constants.ADD_USERS,1);
                startActivity(intent);
            }
        });
        activityMainBinding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: " + position);
pos=position;
                try {
                    if (!loginPojosList.get(position).getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {

                        activityMainBinding.progressbar.setVisibility(View.VISIBLE);
                        activityMainBinding.layout.setEnabled(false);
                        activityMainBinding.layout.setAlpha(0.5f);
                        activityMainBinding.layout.setClickable(false);

                        changeUser(position);
                    }
                    else {
                        activityMainBinding.progressbar.setVisibility(View.VISIBLE);
                        activityMainBinding.layout.setEnabled(false);
                        activityMainBinding.layout.setAlpha(0.5f);
                        activityMainBinding.layout.setClickable(false);
                        getData();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mainActivityViewModel.getLoggedinUSers().observe(this, new Observer<List<LoginPojo>>() {
            @Override
            public void onChanged(List<LoginPojo> loginPojos) {
             if(loginPojos.size()==0){
                 SharedPreferenceImpl.getInstance().remove(Constants.HAS_USERS,MainActivity.this);
                 Log.d(TAG, "onChanged: snnslnalnsnslnasonol");
                 finish();

             }
                if (loginPojosList.size() < loginPojos.size()) {
                    for (int i = MainActivity.this.loginPojosList.size(); i < loginPojos.size(); i++) {
                        MainActivity.this.loginPojosList.add(loginPojos.get(i));
                    }
                    spinnerarray = new String[loginPojosList.size()];

                    for (int i = 0; i < loginPojosList.size(); i++) {
                        spinnerarray[i] = loginPojosList.get(i).getEmail();
                    }
                    spinnerAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.spinner_layout,
                            spinnerarray);
                    spinnerAdapter.setDropDownViewResource(R.layout.inner_spinner_layout);
                    activityMainBinding.spinner.setAdapter(spinnerAdapter);
                    activityMainBinding.spinner.setSelection(spinnerarray.length - 1);

                }
//                Log.d(TAG, "Size hai " + loginPojos.size() + loginPojos.get(0).getEmail());
//
//                Toast.makeText(MainActivity.this, "Size hai " + loginPojos.size() + loginPojos.get(0).getEmail(), Toast.LENGTH_SHORT).show();
//

            }
        });

        mainActivityViewModel.getLoginLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                Log.d(TAG, "onChanged: " + firebaseUser.getEmail());
                Toast.makeText(MainActivity.this, "sss" + firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();
                getData();
            }
        });
activityMainBinding.signout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        FirebaseAuth.getInstance().signOut();
        AccountManager accountManager=AccountManager.get(MainActivity.this);
       accountManager.removeAccount(accountManager.getAccounts()[pos],null,null);
        mainActivityViewModel.deleteUser(loginPojosList.get(pos));
        Intent intent= new Intent(MainActivity.this,LoginActivity.class);
        intent.putExtra(Constants.HAS_USERS,1);
        startActivity(intent);
        finish();

    }
});
    }

    private void changeUser(int a) throws Exception {
        LoginPojo loginPojo = loginPojosList.get(a);

        String pwd = EncryptionClass.decrypt(loginPojo.getEncryptedPwd());
        FirebaseAuth.getInstance().signOut();
        mainActivityViewModel.loginUser(loginPojo.getEmail(), pwd);
    }

    private void getData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 Log.d(TAG, "onDataChange: sssnbsao");

                 Log.d(TAG, "onDataChange: "+dataSnapshot.child("city").getValue());

                 activityMainBinding.city.setText(dataSnapshot.child("city").getValue().toString());
                 activityMainBinding.name.setText(dataSnapshot.child("name").getValue().toString());
                 activityMainBinding.email.setText(dataSnapshot.child("email").getValue().toString());
                 activityMainBinding.phone.setText(dataSnapshot.child("phone").getValue().toString());


                 //here compare your local password inserted in your editText with the one pulled from firebase

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        Log.d(TAG, "getData: "+usersPojo.getName()+usersPojo.getCity());
        activityMainBinding.progressbar.setVisibility(View.GONE);
        activityMainBinding.layout.setEnabled(true);
        activityMainBinding.layout.setAlpha(1f);
        activityMainBinding.layout.setClickable(true);


    }
}
