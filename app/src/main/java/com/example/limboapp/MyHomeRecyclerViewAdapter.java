package com.example.limboapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.limboapp.HomeFragment.OnListFragmentInteractionListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyHomeRecyclerViewAdapter extends RecyclerView.Adapter<MyHomeRecyclerViewAdapter.ViewHolder> {

    private final String TAG = "MyHomeRecyclerViewAdapter";

    private Context context;
    private View view;
    private final ArrayList<Video> videos;
    private final OnListFragmentInteractionListener listener;

    public MyHomeRecyclerViewAdapter(Context context, OnListFragmentInteractionListener listener) {
        this.context = context;
        this.listener = listener;
        this.videos = new ArrayList<>();

        //Queries for all videos to add to list
        // (dangerous if there is a massive amount of videos)
        Query q = FirebaseDatabase.getInstance().getReference().child("videos");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //if no videos exist yet
                if(videos.isEmpty()){
                    for (DataSnapshot databaseVideo: dataSnapshot.getChildren()) {
                        videos.add(databaseVideo.getValue(Video.class));
                    }
                    notifyDataSetChanged();
                }
                else {
                    //if a video is added or changed
                    for (DataSnapshot databaseVideo: dataSnapshot.getChildren()) {
                        boolean exists = false;
                        for (Video currentVideo: videos) {
                            if(currentVideo.getKey().equals(databaseVideo.getKey())) {
                                exists = true;
                                Video tempVideo = databaseVideo.getValue(Video.class);
                                currentVideo.update(tempVideo);
                                notifyItemChanged(videos.indexOf(currentVideo),"data_update");
                                break;
                            }
                        }
                        if(!exists) {
                            videos.add(databaseVideo.getValue(Video.class));
                            notifyItemInserted(videos.size()-1);
                        }
                    }

                    //if a video is removed
                    for (Video currentVideo: videos) {
                        boolean exists = false;
                        for (DataSnapshot databaseVideo: dataSnapshot.getChildren()) {
                            if(currentVideo.getKey().equals(databaseVideo.getKey())) {
                                exists = true;
                                break;
                            }
                        }
                        if(!exists){
                            notifyItemRemoved(videos.indexOf(currentVideo));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.username.setText(videos.get(position).getUsername());
        holder.description.setText(videos.get(position).getDescription());
        holder.likeCount.setText(String.valueOf(videos.get(position).getLikes()));
        Glide.with(context).load(videos.get(position).getIconUrl()).into(holder.icon);
        final Uri videoUri = Uri.parse(videos.get(position).getPath());
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

        holder.like_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videos.get(position).incrementLikes(context);
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if(!payloads.isEmpty()) {
            holder.username.setText(videos.get(position).getUsername());
            holder.description.setText(videos.get(position).getDescription());
            holder.likeCount.setText(String.valueOf(videos.get(position).getLikes()));
            Glide.with(context).load(videos.get(position).getIconUrl()).into(holder.icon);
        }
        else{
            onBindViewHolder(holder,position);
        }
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public TextView username, description, likeCount;
        public ImageView icon;
        public ImageView like_button;
        public VideoView video;
        public MediaPlayer mp;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            username = view.findViewById(R.id.user_post_username_textView);
            description = view.findViewById(R.id.user_post_description);
            likeCount = view.findViewById(R.id.user_post_likeCount);
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
