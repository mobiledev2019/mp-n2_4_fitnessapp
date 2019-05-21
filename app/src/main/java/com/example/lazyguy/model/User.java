package com.example.lazyguy.model;

public class User {
    private String name;
    private float height;
    private float weight;
    private boolean sex;
    private String googleId;
    private String facebookId;

    public User() {
    }

    public User(String name, String googleId, String facebookId) {
        this.name = name;
        this.googleId = googleId;
        this.facebookId = facebookId;
    }

    public User(String name, float height, float weight, boolean sex, String googleId, String facebookId) {
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.sex = sex;
        this.googleId = googleId;
        this.facebookId = facebookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }
}
