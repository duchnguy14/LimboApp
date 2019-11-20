package com.example.limboapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.media.Image;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.limboapp.HomeFragment.OnListFragmentInteractionListener;
import com.example.limboapp.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyHomeRecyclerViewAdapter extends RecyclerView.Adapter<MyHomeRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private View view;
    private ArrayList<Users> users;
    private HashMap<Integer,Long> videoTimestamps;
    private final OnListFragmentInteractionListener listener;

    public MyHomeRecyclerViewAdapter(Context context, ArrayList<Users> users, OnListFragmentInteractionListener listener) {
        this.context = context;
        this.users = users;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.username.setText(users.get(position).getUsername());
        holder.video.setVideoPath(users.get(position).getVideos().get(0));

        holder.video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                holder.mp = mp;
                mp.setLooping(true);
            }
        });
        holder.video.start();

        holder.video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.video.isPlaying()) {
                    holder.mp.pause();
                }
                else {
                    holder.mp.start();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public TextView username;
        public ImageView icon;
        public ImageView like_button;
        public VideoView video;
        public MediaPlayer mp;
        public int stopTime;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            username = view.findViewById(R.id.user_post_username_textView);
            icon = view.findViewById(R.id.user_icon);
            like_button = view.findViewById(R.id.like_button);
            video = view.findViewById(R.id.user_post_VideoView);
            stopTime = 0;
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        videoTimestamps.put(holder.getAdapterPosition(),holder.video.getCurrentPosition());
    }
}
