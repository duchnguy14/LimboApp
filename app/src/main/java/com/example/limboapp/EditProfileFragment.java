package com.example.limboapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends Fragment
{
    // Notes: Debug Tools
    static final String TAG = "(Debug) EditProfileFragment";

    // Notes: XML References
    private CircleImageView mProfilePhoto;
    private EditText mDisplayName, mUsername, mDescription;


    // Notes: Firebase Variables
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;


    // Notes: Variables
    private User mUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        // Notes: XML References
        mProfilePhoto = (CircleImageView) view.findViewById(R.id.profile_photo);
        mDisplayName = (EditText) view.findViewById(R.id.display_name);
        mUsername = (EditText) view.findViewById(R.id.username);
        mDescription = (EditText) view.findViewById(R.id.description);


        // Notes: Firebase Variables
        mFirebaseMethods = new FirebaseMethods(getActivity());

        setupFirebaseAuth();

        // Notes: Back Arrow for navigating back to ProfileActivity
        ImageView back_arrow = (ImageView) view.findViewById(R.id.backArrow);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notes: we are in a fragment that's why we need getActivity()
                getActivity().finish();
            }
        });

        // Notes: Save changes that are made in the settings
        ImageView checkmark = (ImageView) view.findViewById(R.id.saveChanges);
        checkmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileSettings();
            }
        });

        return view;
    }


    // ***************************************************************************************
    // *********************************NOTES: HELPER METHODS*********************************
    // ***************************************************************************************

    /*
        Notes:
            Retrieves the data contained in the widgets and saves it to the database.
            Need to remember...username chosen must be unique!
     */
    private void saveProfileSettings()
    {
        // Notes: Fields from XML
//        final String displayName = mDisplayName.getText().toString();
//        final String username = mUsername.getText().toString();
//        final String description = mDescription.getText().toString();

        String displayName = null;
        String username = null;
        String description = null;


        if(mDisplayName.getText().toString().length() != 0)
        {
            displayName = mDisplayName.getText().toString();
        }

        if(mUsername.getText().toString().length() != 0)
        {
            username = mUsername.getText().toString();
        }

        if(mDescription.getText().toString().length() != 0)
        {
            description = mDescription.getText().toString();
        }


        if(username != null)
        {
            // Notes: case1 - User made a change to their username
            if(!mUser.getUsername().equals(username))
            {
                checkIfUsernameExists(username);
            }
        }

        /*
            Notes: Case 2 & 3
                changing the rest of the settings that do not require uniqueness
         */

        // Notes: update displayname
        if(displayName != null)
        {
            if(!mUser.getDisplay_name().equals(displayName))
            {
                mFirebaseMethods.updateUserAccountSettings(displayName, null, null);
            }
        }

        if(description != null)
        {
            // Notes: update description
            if(!mUser.getDescription().equals(description))
            {
                mFirebaseMethods.updateUserAccountSettings(null, null, description);
            }
        }
    }

    /*
        Notes:
            Checks if username already exists in database.
            Use Query...its recommended and fast!!!

     */
    private void checkIfUsernameExists(final String username) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();


        // Notes: Querying inside of the users node...searching for the username if it exists
        Query query = reference
                .child("users")
                .orderByChild(getString(R.string.field_username))
                .equalTo(username);

        query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            // Notes: Returns a datasnapshot if a match is found

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean found = false;
                // Notes: singleSnapshot is a single item from database
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    if (singleSnapshot.exists() == true)
                    {
                        Toast.makeText(getActivity(), "That username already exists.", Toast.LENGTH_LONG).show();
                        found = true;
                    }
                }
                if(dataSnapshot.exists() == false){
                    //Notes: add the username if not found
                    mFirebaseMethods.updateUsername(username);
                }
                if(!found) {
                    Toast.makeText(getActivity(),"Your changes have been saved",Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setProfileWidgets(User user_param) {

        mUser = user_param;

        if(user_param.getIconUrl() != null)
        {
            UniversalImageLoader.setImage(user_param.getIconUrl(), mProfilePhoto, null, "");
        }

        if(user_param.getUsername() != null)
        {
            mUsername.setText(user_param.getUsername());
        }

        if(user_param.getDisplay_name() != null)
        {
            mDisplayName.setText(user_param.getDisplay_name());
        }
        if(user_param.getDescription() != null)
        {
            mDescription.setText(user_param.getDescription());
        }
    }

    // ***************************************************************************************
    // *********************************NOTES: FIREBASE METHODS*********************************
    // ***************************************************************************************

    /*
       Notes:
           Setting up the firebase auth object
    */
    private void setupFirebaseAuth()
    {

        /* Notes:
            Initialize Firebase Auth.
            Works app-wide because it works on an instance bases.
            Any activity or fragment can access this object
         */
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("users");

        /* ◊
            Notes:
                Allows us to get the dataSnapshot to either read or write to database.
                This listener always listens

         */
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Notes retrieve user information from the database
                User user = mFirebaseMethods.getUser(dataSnapshot);
                setProfileWidgets(user);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        // Notes: For whatever reason we start this activity, it always checks the user.
        FirebaseUser user = mAuth.getCurrentUser();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthListener != null)
        {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
