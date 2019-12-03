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
import android.widget.VideoView;

import com.example.limboapp.UserFragment.OnListFragmentInteractionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyUserRecyclerViewAdapter extends RecyclerView.Adapter<MyUserRecyclerViewAdapter.ViewHolder> {

    private final String TAG = "MyUserRecyclerViewAdapter";
    private FirebaseAuth mAuth;

    private View view;
    private final ArrayList<Video> videos;
    private final ArrayList<String> videoIds;
    private final OnListFragmentInteractionListener listener;

    public MyUserRecyclerViewAdapter(Context context, OnListFragmentInteractionListener listener) {
        this.listener = listener;
        //get ids of all videos posted by user (stored in the user's videos path)
        mAuth = FirebaseAuth.getInstance();
        videos = new ArrayList<>();
        videoIds = new ArrayList<>();

        //Queries for user videos to add to list
        // (not too efficient, dangerous if there is a massive amount of videos)
        Log.d(TAG, "MyUserRecyclerViewAdapter: username = " + mAuth.getCurrentUser().getDisplayName());
        Query videoQuery = FirebaseDatabase.getInstance().getReference().child("videos")
                .orderByChild("username").equalTo(mAuth.getCurrentUser().getDisplayName());
        videoQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: reached");
                for (DataSnapshot databaseVideo: dataSnapshot.getChildren()) {
                    videos.add(databaseVideo.getValue(Video.class));
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
                .inflate(R.layout.fragment_user, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Uri videoUri = Uri.parse(videos.get(position).getPath());
        holder.video.setVideoURI(videoUri);

        holder.video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                holder.mp = mp;
                mp.setLooping(true);
                mp.setVolume(0f, 0f);
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
        public VideoView video;
        public MediaPlayer mp;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            video = view.findViewById(R.id.fragment_user_VideoView);
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
