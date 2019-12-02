package com.example.limboapp;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Video {
    private String key, description, path, uid;
    //username, iconUrl
    private int likes;

    public Video() {

    }

    public Video(String key, String description, String path, int likes) {
        this.key = key;
        this.description = description;
        this.path = path;
        this.likes = likes;
    }

    public String getUsername() {
        final String[] username = {""};
        Query q = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("username");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username[0] = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return username[0];
    }

    public String getIconUrl() {
        final String[] iconUrl = {""};
        Query q = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("iconUrl");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                iconUrl[0] = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return iconUrl[0];
    }

    public String getPath() { return path; }

    public void setPath(String path) { this.path = path; }

    public int getLikes() { return likes; }

    public void incrementLikes() {
        likes++;
        //deincrement in firebase?
    }

    public void deincrementLikes() {
        likes--;
        //deincrement in firebase?
    }
}
