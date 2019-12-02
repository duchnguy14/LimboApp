package com.example.limboapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.limboapp.HomeFragment.OnListFragmentInteractionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyUserRecyclerViewAdapter extends RecyclerView.Adapter<MyUserRecyclerViewAdapter.ViewHolder> {

    private final String TAG = "MyHomeRecyclerViewAdapter";
    private FirebaseAuth mAuth;
    private DatabaseReference database;

    private Context context;
    private View view;
    private final ArrayList<Video> videos;
    private final ArrayList<String> videoIds;
    private final OnListFragmentInteractionListener listener;

    public MyUserRecyclerViewAdapter(Context context, final ArrayList<Video> videos, OnListFragmentInteractionListener listener) {
        this.context = context;
        this.videos = videos;
        this.listener = listener;

        //get ids of all videos posted by user (stored in the user's videos path
        mAuth = FirebaseAuth.getInstance();
        videoIds = new ArrayList<>();
        Query idQuery = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid()).child("videos");
        idQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Queries for user videos to add to list
        // (dangerous if there is a massive amount of videos)
        Query videoQuery = FirebaseDatabase.getInstance().getReference().child("videos");
        videoQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot databaseVideo: dataSnapshot.getChildren()) {
                    for (String videoId: videoIds) {
                        //only add videos whose ids exist in the user's list of video ids
                        if(databaseVideo.getKey().equals(videoId)){
                            videos.add(databaseVideo.getValue(Video.class));
                        }
                    }
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: database error: " + databaseError);
            }
        });
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

        holder.username.setText(videos.get(position).getUsername());
        Uri videoUri = Uri.parse(videos.get(position).getVideoUrl());
        holder.video.setVideoURI(videoUri);

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
        return videos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public TextView username;
        public ImageView icon;
        public ImageView like_button;
        public VideoView video;
        public MediaPlayer mp;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            username = view.findViewById(R.id.user_post_username_textView);
            icon = view.findViewById(R.id.user_icon);
            like_button = view.findViewById(R.id.like_button);
            video = view.findViewById(R.id.user_post_VideoView);
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.video.start();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }
}
