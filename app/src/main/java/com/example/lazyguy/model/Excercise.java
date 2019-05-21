package com.example.lazyguy.model;

public class Excercise {
    private String name;
    private String bodypart;
    private float calories;
    private String detail;
    private String url;

    public Excercise() {

    }

    public Excercise(String name, String bodypart, float calories, String detail, String url) {
        this.name = name;
        this.bodypart = bodypart;
        this.calories = calories;
        this.detail = detail;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBodypart() {
        return bodypart;
    }

    public void setBodypart(String bodypart) {
        this.bodypart = bodypart;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
