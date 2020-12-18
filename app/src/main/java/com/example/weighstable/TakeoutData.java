package com.example.weighstable;

import androidx.annotation.NonNull;

public class TakeoutData {

    public TakeoutData() {

    }

    public TakeoutData(String timestamp, String user, double weight) {
        this.timestamp = timestamp;
        this.user = user;
        this.weight = weight;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @NonNull
    @Override
    public String toString() {
        return this.timestamp + ": " + this.user + ": " + this.weight + "lbs";
    }

    private String timestamp;
    private String user;
    private double weight;

}
