package com.example.arturrinkis.universitystudentrating.Utilities;

public class RatingMonth {
    private float value;
    private String month;

    public RatingMonth(float value, String month) {
        this.value = value;
        this.month = month;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float averageValue) {
        this.value = averageValue;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
