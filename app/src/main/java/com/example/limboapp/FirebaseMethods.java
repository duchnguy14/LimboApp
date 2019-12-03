package com.example.limboapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseMethods
{
    // ***************************************************************************************
    // *********************************NOTES: Variables *********************************
    // ***************************************************************************************

    // Notes: Debug Tools
    private static final String TAG = "(Debug) FirebaseMethods";

    // Notes: Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // Notes: Firebase Database
    // Notes: NAME CHANGE: mFirebaseDatabase = database
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;


    // Notes: Variables
    private Context mContext;
    private String userID;



    // Notes: Constructor

    public FirebaseMethods(Context context) {
        mContext = context;
        mAuth = FirebaseAuth.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        myRef = mFirebaseDatabase.getReference();



        if(mAuth.getCurrentUser() != null)
        {
            userID = mAuth.getCurrentUser().getUid();
        }
    }





    // ***************************************************************************************
    // *********************************NOTES: HELPER METHODS*********************************
    // ***************************************************************************************



    /*
        Notes:
            Update 'user' node in database for the current user
     */
    public void updateUserAccountSettings(String displayName, String website, String description, long phoneNumber)
    {

        Log.d(TAG, "updateUserAccountSettings: updating user account settings.");

        if(displayName != null){
            myRef.child(mContext.getString(R.string.dbname_users))
                    .child(userID)
                    .child(mContext.getString(R.string.field_display_name))
                    .setValue(displayName);
        }

        if(description != null) {
            myRef.child(mContext.getString(R.string.dbname_users))
                    .child(userID)
                    .child(mContext.getString(R.string.field_description))
                    .setValue(description);
        }
    }



    /*
       Notes: Updates the username in the users node in the database.
    */
    public void updateUsername(String username)
    {
        Log.d(TAG, "updateUsername: updating username to: " + username);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID) // Notes: Current User logged in
                .child(mContext.getString(R.string.field_username)) // Notes: the username of the current user logged in
                .setValue(username);
    }


//    /*
//       Notes: Updates the posts in the users node in the database.
//    */
//    public void addPost()
//    {
//        Long post_num =
//
//
//
//        Log.d(TAG, "addPost: incrementing post inside the users database!");
//        myRef.child(mContext.getString(R.string.dbname_users))
//                .child(userID) // Notes: Current User logged in
//                .child(mContext.getString(R.string.field_posts)) // Notes: the posts of the current user logged in
//                .setValue(username);
//
//    }



    /*
        Notes: Updates the email in the users node in the database.
     */
    public void updateEmail(String email){
        Log.d(TAG, "updateEmail: upadting email to: " + email);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_email))
                .setValue(email);

    }





//    /*
//        Notes: Source V27
//            Need DataSnapShot to see what's inside database.
//
//     */
//    public boolean checkIfUsernameExists(String username, DataSnapshot datasnapshot)
//    {
//        Log.d(TAG, "checkIfUsernameExists: checking if " + username + " already exists.");
//
//        User user = new User();
//
//        /*
//            Notes:
//                DataSnapshot contains every node inside the database.
//                Looping through DataSnapShot is looping thru the nodes.
//         */
//        for(DataSnapshot ds: datasnapshot.child(userID).getChildren())
//        {
//            Log.d(TAG, "checkIfUsernameExists: datasnapshot: " + ds);
//
//            // Notes: Get's the username node inside of UserID's Node: UserID > username
//            user.setUsername(ds.getValue(User.class).getUsername());
//            Log.d(TAG, "checkIfUsernameExists: username" + user.getUsername());
//
//            // Notes: Checking if the username already exists
//            if(StringManipulation.expandUsername(user.getUsername()).equals(username))
//            {
//                Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH: " + user.getUsername());
//                return true;
//            }
//
//        }
//
//        // Notes: username does not exist.
//        return false;
//    }



    /*
        Notes:
            Register a new email and password to Firebase Authentication
     */
    public void registerNewEmail(final String email, String password, final String username)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "registerNewEmail > createUserWithEmail:failure", task.getException());
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else if(task.isSuccessful())
                        {
                            // Notes: Send verification email
                            sendVerificationEmail();

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "registerNewEmail > createUserWithEmail > onComplete: " + task.isSuccessful());
                            Log.d(TAG, "registerNewEmail > createUserWithEmail:success");
                            Log.d(TAG, "registerNewEmail > createUserWithEmail > onComplete: Authstate changed: " + userID);
                            userID = mAuth.getCurrentUser().getUid();
                        }

                        // ...
                    }
                });

    }


    public void sendVerificationEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null)
        {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(mContext, "Please check your email for verification", Toast.LENGTH_LONG).show();

                            }
                            else
                            {
                                Toast.makeText(mContext, "couldn't send verification email.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }



//    /*
//        Notes:
//            Adding information to users nodes and user_account_settings node
//     */
//    public void addNewUser(String email, String username, String description, String website, String profile_photo)
//    {
//        User user = new User(userID, 1L, email, StringManipulation.condenseUsername(username));
//
//        // Notes: Inserts a new node inside user node.
//        myRef.child(mContext.getString(R.string.dbname_users)) // Notes: This references the users node
//                .child(userID) // Notes: This refererences: users node > userID node
//                .setValue(user); // Notes: Adds new user
//
//        // Notes: Remember that the universal image loader can handle empty strings,
//        //              it will load the default image.
//        UserAccountSettings settings = new UserAccountSettings(
//                description, username, 0L, 0L, 0L, profile_photo,
//                StringManipulation.condenseUsername(username), website);
//
//
//        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
//                .child(userID)
//                .setValue(settings);
//    }


    /*
        Notes:
            Retrieves the account settings for the user currently logged in
                from the database (user_account_settings node)
            UserSettings is a class we created to hold both User and UserAccountSettings classes.

     */
    public User getUser(DataSnapshot dataSnapshot)
    {
        Log.d(TAG, "getUserAccountSettings: retrieving user account settings from firebase.");


        User user = new User();

        // Notes: Loops thru the main nodes: user_account_settings & users
        for(DataSnapshot ds: dataSnapshot.getChildren())
        {

            // Notes: user node
            if(ds.getKey().equals(mContext.getString(R.string.dbname_users)))
            {
                Log.d(TAG, "getUserAccountSettings: user_account_settings datasnapshot: " + ds);

                // Notes: Try-Catch for possible null fields
                try {
                    // Notes: Setting values from user_account_settings node into object settings
                    user.setDescription(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getDescription()
                    );

                    user.setDisplay_name(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getDisplay_name()
                    );

                    user.setFollowers(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getFollowers()
                    );

                    user.setFollowing(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getFollowing()
                    );

                    user.setPosts(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getPosts()
                    );

                    user.setIconUrl(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getIconUrl()
                    );

                    user.setUsername(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getUsername()
                    );


                    Log.d(TAG, "getUser: retrieved user information: " + user.toString());
                } catch (Exception e) {
                    Log.e(TAG, "getUser: NullPointerException: " + e.getMessage());
                }
            }




        }
        return user;

    }





}
