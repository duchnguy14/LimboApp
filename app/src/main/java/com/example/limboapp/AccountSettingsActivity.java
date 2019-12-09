package com.example.limboapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class AccountSettingsActivity extends AppCompatActivity {

    // Notes: Debug tools
    static final String TAG = "(Debug) AccountSettingsActivity";
    private final Context ACTIVITY_CONTEXT = AccountSettingsActivity.this;

    // Notes: Variables
    private RelativeLayout relativeLayout;
    private SectionStatePagerAdapter pagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        Log.i(TAG, "onCreate: started");

        relativeLayout = (RelativeLayout) findViewById(R.id.relLayout1);
        viewPager = (ViewPager) findViewById(R.id.container);


        // Notes: Listview settings contents
        setUpSettingsList();

        // Notes: Setting up fragment list
        setUpFragments();

        // Notes: Back Arrow
        ImageView back_arrow_imageView = (ImageView) findViewById(R.id.backArrow);
        back_arrow_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

    }

    // ***************************************************************************************
    // *********************************NOTES: HELPER METHODS*********************************
    // ***************************************************************************************


    private void setUpFragments()
    {
        // Notes: Order Matters!!!
        pagerAdapter = new SectionStatePagerAdapter(getSupportFragmentManager(), 0);
        // Notes: Fragment #0
        pagerAdapter.addFragment(getString(R.string.edit_profile_fragment), new EditProfileFragment());
    }

    private void setUpSettingsList()
    {
        ListView listView = (ListView) findViewById(R.id.lvAccountSettings);

        ArrayList<String> options = new ArrayList<String>();

        /*
        Notes:
            Need to fix:
            Perhaps just have edit profile (changing username & pass) & edit bio.
            Place log out onto the profile page itself.
            Always good practice to put strings in the string file.
         */
        // Notes: Fragment #0
        options.add(getString(R.string.edit_profile_fragment));

        ArrayAdapter adapter = new ArrayAdapter(ACTIVITY_CONTEXT, android.R.layout.simple_list_item_1, options);
        listView.setAdapter(adapter);

        // Notes: to get the fragment number for navigation
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setViewPager(position);
            }
        });
    }

    /*
        Notes: This method is responsible for navigating to the fragment.
     */
    private void setViewPager(int fragment_number)
    {
        // Notes: When the viewpager is set up, the main screen of the account_settings
        //          goes away and only the fragment is displayed.
        relativeLayout.setVisibility(View.GONE);

        viewPager.setAdapter(pagerAdapter);

        // Notes: Navigates to whichever fragment
        viewPager.setCurrentItem(fragment_number);

        // Notes: To set the fragment, it will depends on which item we click on
        //          in the listview (setUpSettingsList)
    }
}
