package com.example.limboapp;

import android.content.Context;

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

                post_num += 1L;

                myRef.child("posts").setValue(post_num);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

    }

    public void incrementLikes(final String key)
    {

        myRef = myRef.child("videos").child(key);

        // Read from the database
        myRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Video currentVideo = dataSnapshot.getValue(Video.class);

                int likes = currentVideo.getLikes();

                likes++;


                myRef.child("likes").setValue(likes);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
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
        User user = new User();

        // Notes: Loops thru the main nodes: users
        for(DataSnapshot ds: dataSnapshot.getChildren())
        {
            // Notes: user node
            if(ds.getKey().equals(mAuth.getCurrentUser().getUid()))
            {
                user = ds.getValue(User.class);
            }
        }

        return user;
    }
}
