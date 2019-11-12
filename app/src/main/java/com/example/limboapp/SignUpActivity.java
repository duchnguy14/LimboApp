package com.example.limboapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.regex.*;

public class SignUpActivity extends AppCompatActivity implements Button.OnClickListener, TextWatcher{

    static final String TAG = "SignUpActivity";
    ImageView inputImage;
    EditText inputUsername;
    Button uploadImage;
    Button createAccount;

    Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        inputImage = findViewById(R.id.inputIcon);
        inputUsername = findViewById(R.id.inputUsername);
        createAccount = findViewById(R.id.createAccount);
        uploadImage = findViewById(R.id.uploadImage);

        inputUsername.addTextChangedListener(this);

        uploadImage.setOnClickListener(this);
        createAccount.setOnClickListener(this);
    }

    // Button.OnClickListener methods
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.createAccount:
                Log.d(TAG, "onClick: go to main");
                goToMain(v);
                break;
            case R.id.uploadImage:
                Log.d(TAG, "onClick: upload image");
                break;
        }
    }

    public void goToMain(View view) {
        intent = new Intent(getApplicationContext(), MainActivity.class);

        startActivity(intent);
    }

    // TextWatcher methods
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d(TAG, TAG + ": current username input = " + s.toString());
        checkUsername(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public boolean checkUsername(String username){
        String errorMessage = "";
        if (username.length()<1 || username.length()>32){
            errorMessage = ("Must be between 1 to 32 characters");
        }
        else if (!username.matches("^[a-zA-Z0-9_]*$")) {
            errorMessage = ("Must only use Alphanumeric characters and _");
        }
        //if username already exists

        Log.d(TAG, "checkUsername: error = " + errorMessage);

        if (errorMessage.equals("")){
            Log.d(TAG, "checkUsername: empty error message. no error.");
            inputUsername.setError(null);
            return true;
        }
        else {
            Log.d(TAG, "checkUsername: username is invalid");
            inputUsername.setError(errorMessage);
            return false;
        }
    }
}
