package com.example.fampayassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel mainActivityViewModel;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivityViewModel= ViewModelProviders.of(this).get(MainActivityViewModel.class);

        mainActivityViewModel.getLoggedinUSers().observe(this, new Observer<List<LoginPojo>>() {
            @Override
            public void onChanged(List<LoginPojo> loginPojos) {
                Log.d(TAG, "Size hai "+loginPojos.size()+loginPojos.get(0).getEmail());
                Toast.makeText(MainActivity.this, "Size hai "+loginPojos.size()+loginPojos.get(0).getEmail(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
}
