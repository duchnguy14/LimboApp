package com.example.limboapp;

import java.util.List;

public class User {
    private String username, iconUrl, uid;

    private String description;
    private String display_name;
    private long followers;
    private long following;
    private long posts;

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


    public User(String username, String iconUrl, String description, String display_name, long followers, long following, long posts, String uid) {
        this.username = username;
        this.iconUrl = iconUrl;
        this.description = description;
        this.display_name = display_name;
        this.followers = followers;
        this.following = following;
        this.posts = posts;
        this.uid = uid;
    }

    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getIconUrl() { return iconUrl; }

    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public long getFollowers() {
        return followers;
    }

    public void setFollowers(long followers) {
        this.followers = followers;
    }

    public long getFollowing() {
        return following;
    }

    public void setFollowing(long following) {
        this.following = following;
    }

    public long getPosts() {
        return posts;
    }

    public void setPosts(long posts) {
        this.posts = posts;
    }


    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", uid='" + uid + '\'' +
                ", description='" + description + '\'' +
                ", display_name='" + display_name + '\'' +
                ", followers=" + followers +
                ", following=" + following +
                ", posts=" + posts +
                '}';
    }
}
