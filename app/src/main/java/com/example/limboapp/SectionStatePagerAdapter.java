package com.example.limboapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SectionStatePagerAdapter extends FragmentStatePagerAdapter {
    static final String TAG = "(Debug) SectionStatePagerAdapter";

    private final List<Fragment> fragment_lists = new ArrayList<Fragment>();
    private final HashMap<Fragment, Integer> fragments_hashMap = new HashMap<>();
    private final HashMap<String, Integer> fragment_numbers_hashMap = new HashMap<>();
    private final HashMap<Integer, String> fragment_names_hashMap = new HashMap<>();


    public SectionStatePagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }


    @Override
    public int getCount()
    {
        return fragment_lists.size();
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        return fragment_lists.get(position);
    }


    public void addFragment(String fragment_name, Fragment fragment)
    {
        fragment_lists.add(fragment);
        fragments_hashMap.put(fragment, fragment_lists.size()-1);
        fragment_numbers_hashMap.put(fragment_name, fragment_lists.size()-1);
        fragment_names_hashMap.put(fragment_lists.size()-1, fragment_name);
    }



    public String getFragmentName(Integer fragment_number)
    {
        if(fragment_names_hashMap.containsKey(fragment_number))
        {
            return fragment_names_hashMap.get(fragment_number);
        }
        else
        {
            return null;
        }
    }


    public Integer getFragmentNumber(String fragment_name)
    {
        if(fragment_numbers_hashMap.containsKey(fragment_name))
        {
            return fragment_numbers_hashMap.get(fragment_name);
        }
        else
        {
            return null;
        }
    }

    public Integer getFragmentNumber(Fragment fragment)
    {
        if(fragment_numbers_hashMap.containsKey(fragment))
        {
            return fragment_numbers_hashMap.get(fragment);
        }
        else
        {
            return null;
        }
    }



}
