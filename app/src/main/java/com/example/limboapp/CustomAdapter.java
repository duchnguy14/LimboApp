package com.example.limboapp;

import android.content.Context;
import android.media.Image;
import android.media.MediaPlayer;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Users> {

    int custom_resource_layout;
    Context custom_context;

    // int resource is our R.layout.custom_row,
    // Think: TV Screen, How you want it to display, and what is being displated
    public CustomAdapter(Context context, int resource, ArrayList<Users> users) {
        super(context, resource, users);
        custom_resource_layout = resource;
        custom_context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String username_string = getItem(position).getUsername();
        String videoname = getItem(position).getVideos().get(0);

        LayoutInflater inflater = LayoutInflater.from(custom_context);

        // Reference to our custom_row.xml
        convertView = inflater.inflate(custom_resource_layout, parent, false);

//        // Reference to our widgets
        TextView username_textView = (TextView) convertView.findViewById(R.id.user_post_username_textView);
//        ImageButton image_button = (ImageButton) convertView.findViewById(R.id.profile_pic_imageButton);
        VideoView videoView = (VideoView) convertView.findViewById(R.id.user_post_VideoView);
//        TextView likes_textView = (TextView) convertView.findViewById(R.id.like_numbers_textView);
//        TextView views_textView = (TextView) convertView.findViewById(R.id.view_numbers_textView);
//        TextView username2_textView = (TextView) convertView.findViewById(R.id.username_under_vid_textView);
//
//        // Set the referenced widgets
        username_textView.setText(username_string);
//        image_button.setImageResource(R.drawable.limbo_giraffe);
        videoView.setVideoPath(videoname);
//        likes_textView.setText("100 Likes");
//        views_textView.setText("200 Views");
//        username2_textView.setText(username_string);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        videoView.start();

        return convertView;
    }
}
