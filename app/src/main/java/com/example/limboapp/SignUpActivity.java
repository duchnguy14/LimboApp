package com.example.limboapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.regex.*;

public class SignUpActivity extends AppCompatActivity {

    ImageView inputImage;
    EditText inputUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        inputImage = findViewById(R.id.inputIcon);
        inputUsername = findViewById(R.id.inputUsername);

        inputUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String current = String.valueOf(s);
                String errorMessage = "";
                if (current.length()<1 || current.length()>32){
                    errorMessage.concat("\nMust be between 1 to 32 characters");
                }
                if (!current.matches("^.*[^a-zA-Z0-9_ ].*$")) {
                    errorMessage.concat("\nMust only use Alphanumeric characters and _");
                }
                inputUsername.setError(errorMessage);
            }
        });
    }


}
