package com.example.limboapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class LogInActivity extends AppCompatActivity {

    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("DEBUG:", "Inside of LogInActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }

    public void goToForgotInfo(View view)
    {
        // The way to move from one activity to the next is to use "intent"
        // Only makes an intent, not executing it.
        intent = new Intent(LogInActivity.this, ForgotInfoActivity.class);

        // Execute intent
        startActivity(intent);
    }

    public void goToSignUp(View view)
    {
        intent = new Intent(LogInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }


    public void goToMain(View view)
    {

        intent = new Intent(LogInActivity.this, MainActivity.class);
        startActivity(intent);
    }


}
