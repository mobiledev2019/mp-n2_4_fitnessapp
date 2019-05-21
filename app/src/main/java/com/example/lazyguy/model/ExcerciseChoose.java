package com.example.lazyguy.model;

public class ExcerciseChoose {
    private Excercise excercise;
    private Boolean check;
    private int repetition;

    public ExcerciseChoose() {
    }

    public ExcerciseChoose(Excercise excercise, Boolean check, int repetition) {
        this.excercise = excercise;
        this.check = check;
        this.repetition = repetition;
    }

    public Excercise getExcercise() {
        return excercise;
    }

    public void setExcercise(Excercise excercise) {
        this.excercise = excercise;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public int getRepetition() {
        return repetition;
    }

    public void setRepetition(int repetition) {
        this.repetition = repetition;
    }
}
