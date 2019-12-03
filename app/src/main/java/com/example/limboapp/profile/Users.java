package com.example.limboapp.profile;

import java.util.ArrayList;

public class Users {
    private String username;
    private ArrayList<String> videos;

    public Users(String username, ArrayList<String> videos) {
        this.username = username;
        this.videos = videos;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getVideos() {
        return videos;
    }

    public void setVideos(ArrayList<String> videos) {
        this.videos = videos;
    }
}
