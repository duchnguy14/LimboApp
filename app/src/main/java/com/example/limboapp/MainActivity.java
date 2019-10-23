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
    }
}
