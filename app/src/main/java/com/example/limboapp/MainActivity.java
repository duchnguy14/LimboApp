package com.example.limboapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("DEBUG:", "App launched into MainActivity");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        setContentView(R.layout.activity_log_in);




            // The way to move from one activity to the next is to use "intent"
            // Only makes an intent, not executing it.
            Intent intent = new Intent(getApplicationContext(), LogInActivity.class);

            // Execute intent
            startActivity(intent);


    }
}
