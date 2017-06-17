package com.example.arturrinkis.universitystudentrating.DTO;

public class ConcurentResult {
    private UserProfile userProfile;
    private double ratingValue;

    public ConcurentResult(UserProfile userProfile, double ratingValue) {
        this.userProfile = userProfile;
        this.ratingValue = ratingValue;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public double getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(double ratingValue) {
        this.ratingValue = ratingValue;
    }
}
