package com.example.arturrinkis.universitystudentrating.DTO;

import java.util.ArrayList;

public class SaveStudentProfessorDistModel {
    private int studentId;
    private ArrayList<StudentProfessorDiscipline> studentProfessorDisciplines;

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public ArrayList<StudentProfessorDiscipline> getStudentProfessorDisciplines() {
        return studentProfessorDisciplines;
    }

    public void setStudentProfessorDisciplines(ArrayList<StudentProfessorDiscipline> studentProfessorDisciplines) {
        this.studentProfessorDisciplines = studentProfessorDisciplines;
    }
}
