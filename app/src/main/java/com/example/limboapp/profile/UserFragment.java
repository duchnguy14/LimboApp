package com.example.limboapp.profile;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.limboapp.R;
import com.example.limboapp.util.UniversalImageLoader;
import com.example.limboapp.dummy.DummyContent.DummyItem;
import com.example.limboapp.home.MainActivity;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class UserFragment extends Fragment 
{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    // Debug Tools
    private static final String TAG = "(Debug) UserFragment";

    private TextView mPosts, mFollowers, mFollowing, mDisplayName, mUsername, mWebsite, mDescription;
    private ProgressBar mProgressBar;
    private CircleImageView mProfilePhoto;
    private GridView gridView;
    private Toolbar toolbar;
    private ImageView profileMenu;

    private Context mContext;











    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserFragment() {
    }
    
    
    
    

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, 1);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        // Notes: XML References
        mDisplayName = (TextView) view.findViewById(R.id.display_name);
        mUsername = (TextView) view.findViewById(R.id.username);
        mWebsite = (TextView) view.findViewById(R.id.website);
        mDescription = (TextView) view.findViewById(R.id.description);
        mProfilePhoto = (CircleImageView) view.findViewById(R.id.profile_photo);
        mPosts = (TextView) view.findViewById(R.id.tvPosts);
        mFollowers = (TextView) view.findViewById(R.id.tvFollowers);
        mFollowing = (TextView) view.findViewById(R.id.tvFollowing);
        mProgressBar = (ProgressBar) view.findViewById(R.id.profileProgressBar);
        gridView = (GridView) view.findViewById(R.id.gridView);
        toolbar = (Toolbar) view.findViewById(R.id.profileToolBar);
        profileMenu = (ImageView) view.findViewById(R.id.profileMenu);

        // Notes: Variables
        mContext = getActivity();










        // Notes: Need to fix: Maybe this will be signout!!!
        TextView editProfile = (TextView) view.findViewById(R.id.textEditProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: Signout Button Pressed");
                // Signout
                // mAuth.signOut();

                // Notes: getActivity() because this is an anonymous inner class.
//                getActivity().finish();
            }
        });




        setupToolBar();

        setProfileImage();




//        ArrayList<String> vidList = new ArrayList<String>();
//        String video1 = "android.resource://" + MainActivity.PACKAGE_NAME + "/" + R.raw.stonefalls;
//        String video2 = "android.resource://" + MainActivity.PACKAGE_NAME + "/" + R.raw.stonefalls;
//        String video3 = "android.resource://" + MainActivity.PACKAGE_NAME + "/" + R.raw.stonefalls;
//        vidList.add(video1);
//        vidList.add(video2);
//        vidList.add(video3);
//
//
//        Users user1 = new Users("Paige", vidList);
//
//
//        ListView listView = (ListView) view.findViewById(R.id.profile_feed_listView);
//        TextView username = (TextView) view.findViewById(R.id.username_frag_textview);
//
//        username.setText(user1.getUsername());
//
//        ProfileAdapter profileAdapter = new ProfileAdapter(getContext(), R.layout.custom_profile, user1.getVideos());
//
//        listView.setAdapter(profileAdapter);


        // Set the adapter
//        if (view instanceof RecyclerView) {
//            Context context = view.getContext();
//            RecyclerView recyclerView = (RecyclerView) view;
//            if (mColumnCount <= 1) {
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            }
//            recyclerView.setAdapter(new MyUserRecyclerViewAdapter(DummyContent.ITEMS, mListener));
//        }



        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }





    // ***************************************************************************************
    // *********************************NOTES: HELPER METHODS*********************************
    // ***************************************************************************************
    
    private void tempGridSetUp()
    {
        ArrayList<String> image_urls_list = new ArrayList<String>();

        image_urls_list.add("https://i.ytimg.com/vi/bnBhFSNVABQ/maxresdefault.jpg");
        image_urls_list.add("https://i.kym-cdn.com/photos/images/newsfeed/001/093/560/9cb.png");
        image_urls_list.add("https://pm1.narvii.com/6058/6d48f960811ca618c33391a74789dc07405a9d91_hq.jpg");
        image_urls_list.add("http://i.ytimg.com/vi/YjKZr8bFEuA/maxresdefault.jpg");
        image_urls_list.add("https://images3.memedroid.com/images/UPLOADED344/5c107531283c7.jpeg");

//        setUpImageGrid(image_urls_list);
    }

//    private void setUpImageGrid(ArrayList<String> image_url_list)
//    {
//
//        int grid_width = getResources().getDisplayMetrics().widthPixels;
//        int image_width = grid_width / GRID_COLUMNS;
//        gridView.setColumnWidth(image_width);
//
//        GridAdapter adapter = new GridAdapter(ACTIVITY_CONTEXT, R.layout.layout_grid_imageview, "", image_url_list);
//
//        gridView.setAdapter(adapter);
//    }

    private void setProfileImage()
    {
        Log.d(TAG, "setProfileImage: setting profile photo");
        String image_url_string = "static.libsyn.com/p/assets/6/5/6/3/6563353b5ef2629d/androidcentral-podcast-1400.jpg";
        UniversalImageLoader.setImage(image_url_string, mProfilePhoto, mProgressBar, "http://");
    }


    private void setupToolBar()
    {
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);

        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: Navigating to account settings");
//                Intent intent = new Intent(mContext, AccountSettingsActivity.class);
//                startActivity(intent);
            }
        });
    }
}
