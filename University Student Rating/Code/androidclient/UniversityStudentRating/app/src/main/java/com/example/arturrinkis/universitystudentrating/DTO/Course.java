package com.example.arturrinkis.universitystudentrating.DTO;

public class Course {
    private int id;
    private int number;

    public Course(int id, int number) {
        this.id = id;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return id == -1 ? "Loading..." : id == -2 ? "All courses" :  String.valueOf(number);
    }
}
