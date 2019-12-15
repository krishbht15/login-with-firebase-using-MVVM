package com.example.fampayassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.fampayassignment.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel mainActivityViewModel;
    private ActivityMainBinding activityMainBinding;
    private ArrayList<LoginPojo> loginPojosList;
    private String[] spinnerarray;
    private ArrayAdapter<String > spinnerAdapter;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       activityMainBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        mainActivityViewModel= ViewModelProviders.of(this).get(MainActivityViewModel.class);
        Account[] accounts= AccountManager.get(MainActivity.this).getAccounts();
        loginPojosList =new ArrayList<>();

        mainActivityViewModel.getLoggedinUSers().observe(this, new Observer<List<LoginPojo>>() {
            @Override
            public void onChanged(List<LoginPojo> loginPojos) {
                if( loginPojosList.size()<loginPojos.size()){
                    for (int i = MainActivity.this.loginPojosList.size(); i <loginPojos.size() ; i++) {
                        MainActivity.this.loginPojosList.add(loginPojos.get(i));
                    }
                    spinnerarray=new String[loginPojosList.size()];

                    for (int i = 0; i <loginPojosList.size() ; i++) {
                        spinnerarray[i]=loginPojosList.get(i).getEmail();
                    }
                    spinnerAdapter= new ArrayAdapter<>(MainActivity.this,  R.layout.spinner_layout,
                            spinnerarray);
                    spinnerAdapter.setDropDownViewResource(R.layout.inner_spinner_layout);
                    activityMainBinding.spinner.setAdapter(spinnerAdapter);

                }
                Log.d(TAG, "Size hai "+loginPojos.size()+loginPojos.get(0).getEmail());

                Toast.makeText(MainActivity.this, "Size hai "+loginPojos.size()+loginPojos.get(0).getEmail(), Toast.LENGTH_SHORT).show();


            }
        });



    }
    
}
