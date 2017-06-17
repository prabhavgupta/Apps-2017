package com.example.arturrinkis.universitystudentrating.DTO;


import java.util.ArrayList;

public class SaveProfessorDisciplinesModel {
    private int professorId;
    private ArrayList<ProfessorDiscipline> professorDisciplines;

    public int getProfessorId() {
        return professorId;
    }

    public void setProfessorId(int professorId) {
        this.professorId = professorId;
    }

    public ArrayList<ProfessorDiscipline> getProfessorDisciplines() {
        return professorDisciplines;
    }

    public void setProfessorDisciplines(ArrayList<ProfessorDiscipline> professorDisciplines) {
        this.professorDisciplines = professorDisciplines;
    }
}