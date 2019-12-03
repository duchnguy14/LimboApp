package com.example.limboapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

//public class EditProfileFragment extends Fragment implements ConfirmPasswordDialog.OnConfirmPasswordListener{
public class EditProfileFragment extends Fragment
{

    // Notes: Debug Tools
    static final String TAG = "(Debug) EditProfileFragment";

    // Notes: XML References
    private CircleImageView mProfilePhoto;
    private EditText mDisplayName, mUsername, mDescription, mEmail, mPhoneNumber;
    private TextView mChangeProfilePhoto;


    // Notes: Firebase Variables
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;
    // Notes: NAME CHANGE: mFirebaseDatabase = database
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;


    // Notes: Variables
    private User mUser;


    // Notes: Interface Methods Implementations


//    @Override
//    public void onConfirmPassword(String password)
//    {
//        Log.d(TAG, "onConfirmPassword: got the password: " + password);
//
//        /*
//            Notes: From Firebase Documentation:
//                Get auth credentials from the user for re-authentication. The example below shows
//                email and password credentials but there are multiple possible providers,
//                such as GoogleAuthProvider or FacebookAuthProvider.
//
//         */
//
//        AuthCredential credential = EmailAuthProvider
//                .getCredential(mAuth.getCurrentUser().getEmail(), password);
//
//        /*
//            Notes:
//                Prompt the user to re-provide their sign-in credentials
//         */
//        mAuth.getCurrentUser().reauthenticate(credential)
//                .addOnCompleteListener(new OnCompleteListener<Void>()
//                {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task)
//                    {
//                        if(task.isSuccessful() == true)
//                        {
//                            Log.d(TAG, "User re-authenticated.");
//
//                            /*
//                                Notes:
//                                    check to see if the email is not already present in the database (Is this email in use?)
//                             */
//                            mAuth.fetchSignInMethodsForEmail(mEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>()
//                            {
//                                @Override
//                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task)
//                                {
//                                    if(task.isSuccessful() == true)
//                                    {
//                                        try
//                                        {
//                                            /*
//                                                Notes:
//                                                    We retrieved something because size = 1.
//                                                    Email is already in use.
//                                             */
//                                            if(task.getResult().getSignInMethods().size() == 1)
//                                            {
//                                                Log.d(TAG, "onComplete: that email is already in use.");
//                                                Toast.makeText(getActivity(), "That email is already in use", Toast.LENGTH_SHORT).show();
//                                            }
//                                            /*
//                                                Notes: size is null if there's 0 retrieved...email is available.
//                                             */
//                                            else
//                                            {
//                                                Log.d(TAG, "onComplete: That email is available.");
//
//                                                // Notes: Update to the new available email
//                                                mAuth.getCurrentUser().updateEmail(mEmail.getText().toString())
//                                                        .addOnCompleteListener(new OnCompleteListener<Void>()
//                                                        {
//                                                            @Override
//                                                            public void onComplete(@NonNull Task<Void> task)
//                                                            {
//                                                                if (task.isSuccessful())
//                                                                {
//                                                                    Log.d(TAG, "User email address updated.");
//                                                                    Toast.makeText(getActivity(), "email updated", Toast.LENGTH_SHORT).show();
//
//                                                                    /*
//                                                                        Notes:
//                                                                            so now the email is updated in authentication,
//                                                                                this method will update it in database.
//                                                                     */
//                                                                    mFirebaseMethods.updateEmail(mEmail.getText().toString());
//                                                                }
//                                                            }
//                                                        });
//                                            }
//                                        }
//                                        catch (NullPointerException e)
//                                        {
//                                            Log.e(TAG, "onComplete: NullPointerException: "  +e.getMessage() );
//                                        }
//                                    }
//                                }
//                            });
//
//
//
//                        }
//                        else
//                        {
//                            Log.d(TAG, "onComplete: re-authentication failed.");
//                        }
//
//                    }
//                });
//    }






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
                Log.i(TAG, "onClick: Navigating back to ProfileActivity");
                // Notes: we are in a fragment that's why we need getActivity()
                getActivity().finish();
            }
        });


        // Notes: Save changes that are made in the settings
        ImageView checkmark = (ImageView) view.findViewById(R.id.saveChanges);
        checkmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to save changes.");
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
            Need to remember...username and email chosen must be unique!
     */
    private void saveProfileSettings()
    {
        // Notes: Fields from XML
        final String displayName = mDisplayName.getText().toString();
        final String username = mUsername.getText().toString();
        final String description = mDescription.getText().toString();



        // Notes: case1 - User made a change to their username
        if(!mUser.getUsername().equals(username))
        {
            checkIfUsernameExists(username);
        }

        /*
            Notes: Case 3 - 6
                changing the rest of the settings that do not require uniqueness
         */

        // Notes: update displayname
        if(!mUser.getDisplay_name().equals(displayName))
        {
            mFirebaseMethods.updateUserAccountSettings(displayName, null, null, 0);
        }

        // Notes: update description
        if(!mUser.getDescription().equals(description))
        {
            mFirebaseMethods.updateUserAccountSettings(null, null, description, 0);
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


        query.addListenerForSingleValueEvent(new ValueEventListener()
        {
            // Notes: Returns a datasnapshot if a match is found

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists() == false){
                    //Notes: add the username if not found
                    mFirebaseMethods.updateUsername(username);
                    Toast.makeText(getActivity(), "saved username.", Toast.LENGTH_SHORT).show();

                }
                // Notes: singleSnapshot is a single item from database
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    if (singleSnapshot.exists() == true)
                    {
                        Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH: " + singleSnapshot.getValue(User.class).getUsername());
                        Toast.makeText(getActivity(), "That username already exists.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    private void setProfileWidgets(User user_param)
    {
        Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + user_param.toString());

        mUser = user_param;

        // Notes: Don't think i need this object anymore.
//        User user = user_param.getUser();
//        UserAccountSettings settings = user_param.getSettings();

        UniversalImageLoader.setImage(user_param.getIconUrl(), mProfilePhoto, null, "");

        mUsername.setText(user_param.getUsername());
        mDisplayName.setText(user_param.getDisplay_name());
        mDescription.setText(user_param.getDescription());




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
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");

        /* Notes:
            Initialize Firebase Auth.
            Works app-wide because it works on an instance bases.
            Any activity or fragment can access this object
         */
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if(user != null)
                {
                    // Notes: User is signed in
                    Log.d(TAG, "\tsetupFirebaseAuth() > onAuthStateChanged: signed in: " + user.getUid());
                }
                else
                {
                    // Notes: User is signed out
                    Log.d(TAG, "\tsetupFirebaseAuth() > onAuthStateChanged: signed out");
                }
            }
        };


        /*
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
        Log.d(TAG, "onStart: ");
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
