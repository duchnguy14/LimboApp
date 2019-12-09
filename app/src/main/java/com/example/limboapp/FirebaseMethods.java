package com.example.limboapp;

import android.content.Context;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    public void updateUserAccountSettings(String displayName, String website, String description)
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


    /*
       Notes: Updates the posts in the users node in the database.
    */
    public void incrementPost()
    {
        Log.d(TAG, "addPost: incrementing post inside the users database!");

        myRef = myRef.child("users").child(mAuth.getCurrentUser().getUid());



            // Read from the database
            myRef.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    User currentUser = dataSnapshot.getValue(User.class);

                    Long post_num = currentUser.getPosts();

                    Log.d(TAG, "onDataChange: post num = " + post_num);

                    post_num += 1L;

                    Log.d(TAG, "onDataChange: (Incremented) post num = " + post_num);

                    myRef.child("posts").setValue(post_num);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });

    }






    /*
        Notes:
            Retrieves the account settings for the user currently logged in
                from the database (user node)
     */
    public User getUser(DataSnapshot dataSnapshot)
    {
        Log.d(TAG, "getUserAccountSettings: retrieving user account settings from firebase.");


        User user = new User();

        // Notes: Loops thru the main nodes: users
        for(DataSnapshot ds: dataSnapshot.getChildren())
        {
            // Notes: user node
            if(ds.getKey().equals(mAuth.getCurrentUser().getUid()))
            {
                Log.d(TAG, "getUserAccountSettings: user_account_settings datasnapshot: " + ds);

                // Notes: Try-Catch for possible null fields
//                try {
//                    // Notes: Setting values from user_account_settings node into object settings
//                    user.setDescription(
//                            ds.child(userID)
//                                    .getValue(User.class)
//                                    .getDescription()
//                    );
//
//                    user.setDisplay_name(
//                            ds.child(userID)
//                                    .getValue(User.class)
//                                    .getDisplay_name()
//                    );
//
//                    user.setFollowers(
//                            ds.child(userID)
//                                    .getValue(User.class)
//                                    .getFollowers()
//                    );
//
//                    user.setFollowing(
//                            ds.child(userID)
//                                    .getValue(User.class)
//                                    .getFollowing()
//                    );
//
//                    user.setPosts(
//                            ds.child(userID)
//                                    .getValue(User.class)
//                                    .getPosts()
//                    );
//
//                    user.setIconUrl(
//                            ds.child(userID)
//                                    .getValue(User.class)
//                                    .getIconUrl()
//                    );
//
//                    user.setUsername(
//                            ds.child(userID)
//                                    .getValue(User.class)
//                                    .getUsername()
//                    );
//
//
//                    Log.d(TAG, "getUser: retrieved user information: " + user.toString());
//                } catch (Exception e) {
//                    Log.e(TAG, "getUser: NullPointerException: " + e.getMessage());
//                }


                Log.d(TAG, "getUser: DSSSS: " + ds.toString());
                user = ds.getValue(User.class);


            }


        }

        return user;

    }





}
