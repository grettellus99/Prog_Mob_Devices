package com.example.workoutic.model;

import android.net.Uri;

public class Message {
    private String senderID;
    private String receiverID;
    private String message;
    private String imageURL;
    private String time;

    private String ID;

    public Message(String senderID, String receiverID, String message, String imageURL, String time, String ID) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.message = message;
        this.imageURL = imageURL;
        this.time = time;
        this.ID = ID;

    }
    public Message(){

    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}

