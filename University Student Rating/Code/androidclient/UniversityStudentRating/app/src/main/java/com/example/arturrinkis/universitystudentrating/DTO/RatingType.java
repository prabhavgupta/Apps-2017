package com.example.arturrinkis.universitystudentrating.DTO;

public class RatingType {
    private int id;
    private String name;
    private String pointName;
    private int minPoints;
    private int maxPoints;

    public RatingType(int id, String name, String pointName, int minPoints, int maxPoints) {
        this.id = id;
        this.name = name;
        this.pointName = pointName;
        this.minPoints = minPoints;
        this.maxPoints = maxPoints;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public int getMinPoints() {
        return minPoints;
    }

    public void setMinPoints(int minPoints) {
        this.minPoints = minPoints;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }
}
