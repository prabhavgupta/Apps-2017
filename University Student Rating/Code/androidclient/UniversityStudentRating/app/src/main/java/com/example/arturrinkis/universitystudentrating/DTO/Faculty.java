package com.example.arturrinkis.universitystudentrating.DTO;

public class Faculty {
    private int id;
    private String name;
    private University university;

    public Faculty(int id, String name, University university) {
        this.id = id;
        this.name = name;
        this.university = university;
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

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    @Override
    public String toString() {
        return name;
    }
}
