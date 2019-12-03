package com.example.limboapp;

import android.util.Log;

public class Video {
    private String key;
    private String description;
    private String path;
    private String username;
    private String iconUrl;
    private String uid;
    //gets username, uid, and iconUrl from user
    private int likes;

    public Video() {

    }

//    public Video(String key, String description, String path, String username, String iconUrl, int likes) {
//        this.key = key;
//        this.description = description;
//        this.path = path;
//        this.username = username;
//        this.iconUrl = iconUrl;
//        this.likes = likes;
//
//        Log.d("Video", "Video data:\nkey = " + key
//            + "\ndescription = " + description
//            + "\npath = " + path
//            + "\nusername = " + username
//            + "\niconUrl = " + iconUrl
//            + "\nlikes" + likes
//        );
//    }


    public Video(String key, String description, String path, String username, String iconUrl, String uid, int likes) {
        this.key = key;
        this.description = description;
        this.path = path;
        this.username = username;
        this.iconUrl = iconUrl;
        this.uid = uid;
        this.likes = likes;


        Log.d("Video", "Video data:\nkey = " + key
                + "\ndescription = " + description
                + "\npath = " + path
                + "\nusername = " + username
                + "\niconUrl = " + iconUrl
                + "\nlikes" + likes
        );
    }

    public String getKey() { return key; }

    public void setKey(String key) { this.key = key; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getIconUrl() { return iconUrl; }

    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }

    public String getPath() { return path; }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setPath(String path) { this.path = path; }

    public int getLikes() { return likes; }

    public void setLikes(int likes) { this.likes = likes; }

    public void incrementLikes() {
        likes++;
        //deincrement in firebase?
    }

    public void deincrementLikes() {
        likes--;
        //deincrement in firebase?
    }
}
