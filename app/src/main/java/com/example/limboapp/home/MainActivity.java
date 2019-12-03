package com.example.limboapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.limboapp.R;
import com.example.limboapp.share.RecordFragment;
import com.example.limboapp.util.UniversalImageLoader;
import com.example.limboapp.profile.UserFragment;
import com.example.limboapp.dummy.DummyContent;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nostra13.universalimageloader.core.ImageLoader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        HomeFragment.OnListFragmentInteractionListener,
        RecordFragment.OnFragmentInteractionListener,
        UserFragment.OnListFragmentInteractionListener {
        Context context;
    private static final int MY_CAMERA_REQUEST_CODE = 100;


    public static String PACKAGE_NAME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Notes: MUST INITIALIZE UniversalImageLoader
        initImageLoader();



        PACKAGE_NAME = getApplicationContext().getPackageName();

        BottomNavigationView navigation = findViewById(R.id.main_navi);
        navigation.setOnNavigationItemSelectedListener(this);

        loadFragment(new HomeFragment());
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }

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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    private void initImageLoader()
    {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(getApplicationContext());
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }





    @Override
    public void onListFragmentInteraction(Video video) {

    }
}
