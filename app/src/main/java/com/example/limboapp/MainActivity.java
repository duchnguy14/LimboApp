package com.example.limboapp;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.limboapp.dummy.DummyContent;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnListFragmentInteractionListener,
        RecordFragment.OnFragmentInteractionListener,
        UserFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Duck's
//        setContentView(R.layout.activity_main_2);

        BottomNavigationView navigation = findViewById(R.id.main_navi);
        navigation.setOnNavigationItemSelectedListener(this);

        loadFragment(new HomeFragment());

        //Duck's
//        Toast.makeText(MainActivity.this, "HERE in MainActivity.class", Toast.LENGTH_LONG).show();
//
//        ArrayList<Users> usersList = new ArrayList<Users>();
//
//        Users user1 = new Users("fiends3175");
//        Users user2 = new Users("Betsy");
//        Users user3 = new Users("Tonykyo");
//        Users user4 = new Users("WkeClutch");
//        Users user5 = new Users("Duwabu");
//
//        usersList.add(user1);
//        usersList.add(user2);
//        usersList.add(user3);
//        usersList.add(user4);
//        usersList.add(user5);
//
//        ListView news_feed_ListView = (ListView) findViewById(R.id.news_feed_listView);
//
//        CustomAdapter adapter = new CustomAdapter(MainActivity.this, R.layout.custom_row, usersList);
//
//        news_feed_ListView.setAdapter(adapter);

    }

    //checks if given fragment exists, and loads it if possible
    private boolean loadFragment(Fragment fragment) {
        if(fragment != null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout,fragment).commit();
            return true;
        }
        return false;
    }

    //switches between fragments for Main Activity
    //based on what the user has tapped on
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch(menuItem.getItemId()) {
            case R.id.navigation_home:
                fragment = HomeFragment.newInstance();
                break;
            case R.id.navigation_record:
                fragment = RecordFragment.newInstance();
                break;
            case R.id.navigation_user:
                fragment = UserFragment.newInstance();
                break;
        }
        return loadFragment(fragment);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
