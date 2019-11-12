package com.example.limboapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.appevents.AppEvent;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "LogInActivity";

    private static final int RC_SIGN_IN_GOOGLE = 9001;

    Intent intent = null;

    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private boolean newUser = false;

    SignInButton signInButtonGoogle;
    TwitterLoginButton signInButtonTwitter;
    LoginButton signInButtonFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("DEBUG:", "Inside of LogInActivity");
        super.onCreate(savedInstanceState);

        // Configure Google sign-in to request the user's ID, email address, and basic profile.
        // ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        setContentView(R.layout.activity_log_in);

        // Build FireBaseAuth to accept SignIn info
        mAuth = FirebaseAuth.getInstance();

        // Get sign in buttons
        signInButtonGoogle = findViewById(R.id.sign_in_button_google);
        signInButtonTwitter = findViewById(R.id.sign_in_button_twitter);
        signInButtonFacebook = findViewById(R.id.sign_in_button_facebook);

        // Google
        // Set on click listener
        signInButtonGoogle.setOnClickListener(this);

        // Twitter

        // Facebook
        FacebookSdk.getApplicationContext();
        AppEventsLogger.activateApp(this.getApplication());
    }

    public void goToMain(View view) {
        intent = new Intent(getApplicationContext(), MainActivity.class);

        startActivity(intent);
    }

    public void goToSignUp(View view) {
        intent = new Intent(getApplicationContext(), SignUpActivity.class);

        startActivity(intent);
    }

    @Override
    protected void onStart() {
        // Check for existing Firebase Sign In account, if the user is already signed in
        // the FirebaseUser will be non-null.
        FirebaseUser account = mAuth.getCurrentUser();
        updateUI(account);
        super.onStart();
    }

    private void updateUI(FirebaseUser account) {
        if(account != null){
            // if an account already exists, direct the user to the main activity
            if(newUser) {
                // if this is the first time the user is using the app, send them to sign up activity
                goToSignUp(findViewById(android.R.id.content).getRootView());
            }
            else {
                // else, take user to main activity
                goToMain(findViewById(android.R.id.content).getRootView());
            }
        }
        else{
            // if there is no login info or login failed, keep the user on this activity (for now)
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button_google:
                signInGoogle();
                break;

            // ...add a case for each sign in option (Google, Facebook, Twitter, etc)
        }
    }

    public void sendToast(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

    // add a signIn method for each sign in option (Google, Facebook, Twitter, etc)

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN_GOOGLE) {
            // Google Auth uses a Task with try/catch to handle exceptions
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            newUser = task.getResult().getAdditionalUserInfo().isNewUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            // Put Toast Message Here
                            sendToast("Sign In with Google Failed");
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    //add a firebaseAuth method for each sign in option (Google, Facebook, Twitter, etc)
}
