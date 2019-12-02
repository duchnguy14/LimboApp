package com.example.limboapp;

import java.util.List;

public class User {
    private String username, iconUrl, uid;
    private List<String> videos;

    public User() {

    }

    public User(String username, String iconUrl) {
        this.username = username;
        this.iconUrl = iconUrl;
    }

    public User(String username, String iconUrl, String uid) {
        this.username = username;
        this.iconUrl = iconUrl;
        this.uid = uid;
    }

    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getIconUrl() { return iconUrl; }

    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }

    public List getVideoIds() { return videos; }

    public void setVideoIds(List videos) { this.videos = videos; }
}
