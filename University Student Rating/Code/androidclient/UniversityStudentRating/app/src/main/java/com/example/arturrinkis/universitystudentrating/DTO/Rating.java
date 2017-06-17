package com.example.arturrinkis.universitystudentrating.DTO;

import java.util.Date;

public class Rating {
    private int id;

    private String name;

    private int points;

    private Date date;

    private int courseId;

    private int disciplineId;

    private Integer studentId;

    private Integer professorId;

    private Integer ratingDivisionId;

    private Course course;

    private Discipline discipline;

    private RatingDivision ratingDivision;

    private UserProfile userProfile;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(int disciplineId) {
        this.disciplineId = disciplineId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Integer professorId) {
        this.professorId = professorId;
    }

    public Integer getRatingDivisionId() {
        return ratingDivisionId;
    }

    public void setRatingDivisionId(Integer ratingDivisionId) {
        this.ratingDivisionId = ratingDivisionId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public RatingDivision getRatingDivision() {
        return ratingDivision;
    }

    public void setRatingDivision(RatingDivision ratingDivision) {
        this.ratingDivision = ratingDivision;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
