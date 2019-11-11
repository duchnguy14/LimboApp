package com.example.limboapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.regex.*;

public class SignUpActivity extends AppCompatActivity implements Button.OnClickListener, TextWatcher{

    ImageView inputImage;
    EditText inputUsername;
    Button createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        inputImage = findViewById(R.id.inputIcon);
        inputUsername = findViewById(R.id.inputUsername);
        createAccount = findViewById(R.id.createAccount);

        inputUsername.addTextChangedListener(this);
    }

    // Button.OnClickListener methods
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.uploadImage:
                break;
            case R.id.createAccount:
                break;
        }
    }

    // TextWatcher methods
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        checkUsername(String.valueOf(s));
    }

    public boolean checkUsername(String username){
        String errorMessage = "";
        if (username.length()<1 || username.length()>32){
            errorMessage.concat("\nMust be between 1 to 32 characters");
        }
        if (!username.matches("^.*[^a-zA-Z0-9_ ].*$")) {
            errorMessage.concat("\nMust only use Alphanumeric characters and _");
        }
        //if username already exists

        inputUsername.setError(errorMessage);

        if (errorMessage.equals("")){
            return true;
        }
        return false;
    }
}
