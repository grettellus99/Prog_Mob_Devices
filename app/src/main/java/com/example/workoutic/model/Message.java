package com.example.workoutic.model;

public class Message {
    private String senderID;
    private String receiverID;
    private String message;
    private String time;

    public Message(String senderID, String receiverID, String message, String time) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.message = message;
        this.time = time;
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
}
