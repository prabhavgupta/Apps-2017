package com.example.arturrinkis.universitystudentrating.DTO;

public class ProfessorDiscipline {
    private int id;
    private int professorId;
    private int disciplineId;
    private Discipline discipline;
    private UserProfile userProfile;

    public ProfessorDiscipline(int id, int professorId, int disciplineId, Discipline discipline, UserProfile userProfile) {
        this.id = id;
        this.professorId = professorId;
        this.disciplineId = disciplineId;
        this.discipline = discipline;
        this.userProfile = userProfile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProfessorId() {
        return professorId;
    }

    public void setProfessorId(int professorId) {
        this.professorId = professorId;
    }

    public int getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(int disciplineId) {
        this.disciplineId = disciplineId;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
