package com.example.limboapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserFragment extends Fragment
{

    // ***************************************************************************************
    // *********************************NOTES: Variables *********************************
    // ***************************************************************************************

    // Notes: Debug tools
    private static final String TAG = "(Debug) UserFragment";

    // Notes: XML References
    private TextView mPosts, mFollowers, mFollowing, mDisplayName, mUsername, mWebsite, mDescription;
    private ProgressBar mProgressBar;
    private CircleImageView mProfilePhoto;
    private RecyclerView gridView;
    private Toolbar toolbar;
    private ImageView profileMenu;

    // Notes: Variables:
    private Context mContext;
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 3;


    // Notes: Firebase Variables
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // Notes: NAME CHANGE: mFirebaseDatabase = database
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;


    // ***************************************************************************************
    // *********************************NOTES: MAIN METHOD *********************************
    // ***************************************************************************************


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Notes: XML References
        mDisplayName = (TextView) view.findViewById(R.id.display_name);
        mUsername = (TextView) view.findViewById(R.id.username);
        mDescription = (TextView) view.findViewById(R.id.description);
        mProfilePhoto = (CircleImageView) view.findViewById(R.id.profile_photo);
        mPosts = (TextView) view.findViewById(R.id.tvPosts);
        mFollowers = (TextView) view.findViewById(R.id.tvFollowers);
        mFollowing = (TextView) view.findViewById(R.id.tvFollowing);
        mProgressBar = (ProgressBar) view.findViewById(R.id.profileProgressBar);
        gridView = (RecyclerView) view.findViewById(R.id.gridView);
        toolbar = (Toolbar) view.findViewById(R.id.profileToolBar);
        profileMenu = (ImageView) view.findViewById(R.id.profileMenu);

        // Notes: Variables
        mContext = getActivity();


        // Notes: Firebase Variables
        // Notes: NEED TO FIX: Change to mContext for parameters?
        mFirebaseMethods = new FirebaseMethods(getActivity());


        setupToolBar();

        setupFirebaseAuth();


        // Notes: signout!!!
        TextView signout = (TextView) view.findViewById(R.id.signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LogInActivity.class));
                getActivity().finish();
            }
        });

        MyUserRecyclerViewAdapter adapter = new MyUserRecyclerViewAdapter(mContext);

        gridView.setAdapter(adapter);

        gridView.setLayoutManager(new GridLayoutManager(mContext,mColumnCount));

        return view;
    }


    // ***************************************************************************************
    // *********************************NOTES: HELPER METHODS*********************************
    // ***************************************************************************************


    private void setProfileWidgets(User user_param)
    {
        Log.d(TAG, "setProfileWidgets: setting widgets with data retrieving from firebase database: " + user_param.getUsername());


        User user = user_param;

        UniversalImageLoader.setImage(user.getIconUrl(), mProfilePhoto, null, "");

        mDescription.setText(user.getDescription());
        mDisplayName.setText(user.getDisplay_name());
        mFollowers.setText(String.valueOf(user.getFollowers()));
        mFollowing.setText(String.valueOf(user.getFollowing()));
        mPosts.setText(String.valueOf(user.getPosts()));
        mUsername.setText(user.getUsername());

        mProgressBar.setVisibility(View.GONE);
    }


    private void setupToolBar()
    {
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);

        // Notes: Need to fix
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(intent);
            }
        });
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
        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        myRef = mFirebaseDatabase.getReference();
        myRef = mFirebaseDatabase.getReference("users");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if(user != null)
                {
                    // Notes: User is signed in
                }
                else
                {
                    // Notes: User is signed out
                }
            }
        };

//        myRef = myRef.child(mAuth.getCurrentUser().getUid());


        // Notes: Allows us to get the dataSnapshot to either read or write to database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                //Notes retrieve user information from the database
                User user = mFirebaseMethods.getUser(dataSnapshot);
                setProfileWidgets(user);

//                mFirebaseMethods.incrementPost();


                //Notes: retrieve images for the user in question
                // Notes: NEED TO FIX: retrieve videos
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
    public void onResume() {
        super.onResume();

        myRef = mFirebaseDatabase.getReference("users");

        // Notes: Allows us to get the dataSnapshot to either read or write to database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                //Notes retrieve user information from the database
                User user = mFirebaseMethods.getUser(dataSnapshot);
                setProfileWidgets(user);

                //Notes: retrieve images for the user in question
                // Notes: NEED TO FIX: retrieve videos
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //cannot get user
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthListener != null)
        {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    //    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, 3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }
}