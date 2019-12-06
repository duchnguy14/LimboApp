package com.example.limboapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.VideoView;

import java.util.ArrayList;

public class ProfileAdapter extends ArrayAdapter<String> {

    int custom_resource_layout;
    Context custom_context;

    // int resource is our R.layout.custom_row,
    // Think: TV Screen, How you want it to display, and what is being displated
    public ProfileAdapter(Context context, int resource, ArrayList<String> videos) {
        super(context, resource, videos);
        custom_resource_layout = resource;
        custom_context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(custom_context);

        // Reference to our custom_row.xml
        convertView = inflater.inflate(custom_resource_layout, parent, false);


        String video_name = getItem(position);

        // Reference to our widgets
        VideoView videoView1 = (VideoView) convertView.findViewById(R.id.videoView1);
        VideoView videoView2 = (VideoView) convertView.findViewById(R.id.videoView2);
        VideoView videoView3 = (VideoView) convertView.findViewById(R.id.videoView3);

//        videoView1.setVideoPath("android.resource://" + MainActivity.PACKAGE_NAME + "/" + R.raw.stonefalls);
//        videoView2.setVideoPath("android.resource://" + MainActivity.PACKAGE_NAME + "/" + R.raw.stonefalls);
//        videoView3.setVideoPath("android.resource://" + MainActivity.PACKAGE_NAME + "/" + R.raw.stonefalls);

        videoView1.setVideoPath(video_name);
        videoView2.setVideoPath(video_name);
        videoView3.setVideoPath(video_name);


        videoView1.start();
        videoView2.start();
        videoView3.start();


        return convertView;
    }

}
