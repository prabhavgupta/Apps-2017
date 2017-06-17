package com.example.arturrinkis.universitystudentrating.DTO;

import java.util.ArrayList;

public class IndividualRating {
    private UserProfile userProfile;
    private Discipline discipline;
    private double averageClassRating;
    private int totalClassRating;
    private int totalScippingClassRating;
    private int totalOlympiadsRating;
    private ArrayList<Rating> classRatings;
    private ArrayList<Rating> scippingClassRatings;
    private ArrayList<Rating> olympiadsRatings;

    public IndividualRating(double averageClassRating, int totalClassRating, int totalScippingClassRating, int totalOlympiadsRating, ArrayList<Rating> classRatings, ArrayList<Rating> scippingClassRatings, ArrayList<Rating> olympiadsRatings) {
        this.averageClassRating = averageClassRating;
        this.totalClassRating = totalClassRating;
        this.totalScippingClassRating = totalScippingClassRating;
        this.totalOlympiadsRating = totalOlympiadsRating;
        this.classRatings = classRatings;
        this.scippingClassRatings = scippingClassRatings;
        this.olympiadsRatings = olympiadsRatings;
    }

    public double getAverageClassRating() {
        return averageClassRating;
    }

    public void setAverageClassRating(double averageClassRating) {
        this.averageClassRating = averageClassRating;
    }

    public int getTotalClassRating() {
        return totalClassRating;
    }

    public void setTotalClassRating(int totalClassRating) {
        this.totalClassRating = totalClassRating;
    }

    public int getTotalScippingClassRating() {
        return totalScippingClassRating;
    }

    public void setTotalScippingClassRating(int totalScippingClassRating) {
        this.totalScippingClassRating = totalScippingClassRating;
    }

    public int getTotalOlympiadsRating() {
        return totalOlympiadsRating;
    }

    public void setTotalOlympiadsRating(int totalOlympiadsRating) {
        this.totalOlympiadsRating = totalOlympiadsRating;
    }

    public ArrayList<Rating> getClassRatings() {
        return classRatings;
    }

    public void setClassRatings(ArrayList<Rating> classRatings) {
        this.classRatings = classRatings;
    }

    public ArrayList<Rating> getScippingClassRatings() {
        return scippingClassRatings;
    }

    public void setScippingClassRatings(ArrayList<Rating> scippingClassRatings) {
        this.scippingClassRatings = scippingClassRatings;
    }

    public ArrayList<Rating> getOlympiadsRatings() {
        return olympiadsRatings;
    }

    public void setOlympiadsRatings(ArrayList<Rating> olympiadsRatings) {
        this.olympiadsRatings = olympiadsRatings;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }
}
