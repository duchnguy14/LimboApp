package com.example.limboapp;

import java.util.ArrayList;

public class User {
    private String username, iconUrl;
    private ArrayList<String> videoUrls;


    public User() {

    }

    public User(String username, String iconUrl) {
        this.username = username;
        this.iconUrl = iconUrl;
        this.videoUrls = new ArrayList<>();
    }

    public User(String username, String iconUrl, ArrayList<String> videoUrls) {
        this.username = username;
        this.iconUrl = iconUrl;
        this.videoUrls = videoUrls;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public ArrayList<String> getVideoUrls() {
        return videoUrls;
    }

    public void setVideoUrls(ArrayList<String> videoUrls) {
        this.videoUrls = videoUrls;
    }
}
