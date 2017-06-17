package com.example.arturrinkis.universitystudentrating.DTO;

public class StudentProfessorDiscipline {
    private int id;
    private int studentId;
    private int professorDisciplineId;
    private ProfessorDiscipline professorDiscipline;

    public StudentProfessorDiscipline(int id, int studentId, int professorDisciplineId, ProfessorDiscipline professorDiscipline) {
        this.id = id;
        this.studentId = studentId;
        this.professorDisciplineId = professorDisciplineId;
        this.professorDiscipline = professorDiscipline;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getProfessorDisciplineId() {
        return professorDisciplineId;
    }

    public void setProfessorDisciplineId(int professorDisciplineId) {
        this.professorDisciplineId = professorDisciplineId;
    }

    public ProfessorDiscipline getProfessorDiscipline() {
        return professorDiscipline;
    }

    public void setProfessorDiscipline(ProfessorDiscipline professorDiscipline) {
        this.professorDiscipline = professorDiscipline;
    }
}