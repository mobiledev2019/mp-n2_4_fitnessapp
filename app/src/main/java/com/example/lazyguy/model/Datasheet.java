package com.example.lazyguy.model;

public class Datasheet {
    private String exid;
    private int repetition;
    private int set;
    private int type;

    public Datasheet() {
    }

    public Datasheet(String exid, int repetition, int set, int type) {
        this.exid = exid;
        this.repetition = repetition;
        this.set = set;
        this.type = type;
    }

    public String getExid() {
        return exid;
    }

    public void setExid(String exid) {
        this.exid = exid;
    }

    public int getRepetition() {
        return repetition;
    }

    public void setRepetition(int repetition) {
        this.repetition = repetition;
    }

    public int getSet() {
        return set;
    }

    public void setSet(int set) {
        this.set = set;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
