package com.example.limboapp;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity implements Button.OnClickListener, TextWatcher{

    static final String TAG = "(Debug) SignUpActivity";
    ImageView inputImage;
    EditText inputUsername;
    Button createAccount;

    Intent intent = null;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference usersRef;
    UserProfileChangeRequest profileUpdates;
    String username, iconUrl;

    public static int flag = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        inputImage = findViewById(R.id.inputIcon);
        inputUsername = findViewById(R.id.inputUsername);
        createAccount = findViewById(R.id.createAccount);

        Glide.with(this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(inputImage);

        inputUsername.addTextChangedListener(this);

        createAccount.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    // Button.OnClickListener methods
    @Override
    public void onClick(View v) 
    {
        Log.d(TAG, "onClick: go to main");

        //get new user information
        username = inputUsername.getText().toString();


        checkIfUsernameExists(username);

    }

    public void goToMain() {
        intent = new Intent(getApplicationContext(), MainActivity.class);


        startActivity(intent);

        finish();
    }

    // TextWatcher methods that check user input
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


    /*
        Notes:
            Checks if username already exists in database.
            Use Query...its recommended and fast!!!

     */
    private void checkIfUsernameExists(final String username) {

        Log.d(TAG, "checkIfUsernameExists: Checking if  " + username + " already exists.");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        // Notes: Querying inside of the users node...searching for the username if it exists
        Query query = reference
                .child(getString(R.string.dbname_users)) // Notes: looking for the node that contains the object we're looking for
                .orderByChild(getString(R.string.field_username)) // Notes: looking for the field inside that object
                .equalTo(username);


        if (flag == 0) {
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                // Notes: Returns a datasnapshot if a match is found


                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists() == false) {
                        //Notes: add the username if not found

                        user = mAuth.getCurrentUser();


                        //get the icon
                        iconUrl = user.getPhotoUrl().toString().replace("s96-c", "s400-c");
                        //update the display name to reflect the input username
                        for (UserInfo profile : user.getProviderData()) {
                            profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();
                        }
                        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "profile update successful");
                                } else {
                                    Log.d(TAG, "profile update unsuccessful: " + task.getException());
                                }
                            }
                        });
                        //add to user object
                        User newUser = new User(username, iconUrl, "", username, 0L, 0L, 0L, mAuth.getCurrentUser().getUid());

                        Log.d(TAG, "onDataChange: newUser = " + newUser.toString());

                        //put new user data in database by UID
                        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

                        usersRef.child(mAuth.getCurrentUser().getUid()).setValue(newUser)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        //go to main upon confirming success
                                        Log.d(TAG, "successfully added new user to database");
                                        goToMain();
                                    } else {
                                        Log.d(TAG, "error adding new user to database: " + task.getException());
                                    }
                                }
                           });
                    }
                    else
                    {
                        Toast.makeText(SignUpActivity.this, "That username already exists.", Toast.LENGTH_SHORT).show();
                        flag++;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
