package com.example.limboapp;

public class Video {
    private String key, username, iconUrl, videoUrl;
    private int likes;

    public Video() {

    }

    public Video(String username, String iconUrl, String videoUrl, int likes) {
        this.username = username;
        this.iconUrl = iconUrl;
        this.videoUrl = videoUrl;
        this.likes = likes;
    }

    public String getKey() { return key; }

    public void setKey(String key) { this.key = key; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getIconUrl() { return iconUrl; }

    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }

    public String getVideoUrl() { return videoUrl; }

    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

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
