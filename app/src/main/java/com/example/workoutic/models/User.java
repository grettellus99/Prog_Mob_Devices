package com.example.workoutic.models;

public class User {

    private String id;
    private String username;
    private String email;
    private String usericon;

    public User(String id, String username, String email, String usericon) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.usericon = usericon;
    }

    public User(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsericon() {
        return usericon;
    }

    public void setUsericon(String usericon) {
        this.usericon = usericon;
    }

}


