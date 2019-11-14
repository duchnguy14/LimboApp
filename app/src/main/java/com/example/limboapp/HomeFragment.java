package com.example.limboapp;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.limboapp.dummy.DummyContent.DummyItem;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class HomeFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener listener;
    private Context context;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HomeFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home_list, container, false);

        context = getContext();

        ArrayList<Users> usersList = new ArrayList<>();
        ArrayList<String> vidList = new ArrayList<>();
        String video1 = "android.resource://" + MainActivity.PACKAGE_NAME + "/" + R.raw.stonefalls;
        //String video2 = "android.resource://" + MainActivity.PACKAGE_NAME + "/" + R.raw.launch;
        //String video3 = "android.resource://" + MainActivity.PACKAGE_NAME + "/" + R.raw.boat;
        String krispyVideo = "android.resource://" + MainActivity.PACKAGE_NAME + "/" + R.raw.krispy;
        vidList.add(krispyVideo);
        vidList.add(krispyVideo);
        vidList.add(krispyVideo);

        Users user1 = new Users("Duc", vidList);
        Users user2 = new Users("Paige", vidList);
        Users user3 = new Users("Dara", vidList);

        usersList.add(user1);
        usersList.add(user2);
        usersList.add(user3);

        RecyclerView news_feed_ListView = view.findViewById(R.id.news_feed_listView);

//        CustomAdapter adapter = new CustomAdapter(getContext(), R.layout.custom_row, usersList);
        MyHomeRecyclerViewAdapter adapter = new MyHomeRecyclerViewAdapter(context, usersList, listener);

        news_feed_ListView.setAdapter(adapter);

        news_feed_ListView.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(news_feed_ListView);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            listener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
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
}
