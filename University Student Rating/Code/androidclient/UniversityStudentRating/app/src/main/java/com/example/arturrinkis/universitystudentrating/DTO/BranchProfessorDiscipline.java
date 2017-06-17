package com.example.arturrinkis.universitystudentrating.DTO;

import java.util.ArrayList;

public class BranchProfessorDiscipline {
    private ArrayList<ProfessorDiscipline> sciProfessorDisciplines;
    private ArrayList<ProfessorDiscipline> sportProfessorDisciplines;
    private ArrayList<ProfessorDiscipline> artProfessorDisciplines;
    private ArrayList<ProfessorDiscipline> societyProfessorDisciplines;
    private ArrayList<StudentProfessorDiscipline> studentProfessorDisciplines;

    public BranchProfessorDiscipline(ArrayList<ProfessorDiscipline> sciProfessorDisciplines, ArrayList<ProfessorDiscipline> sportProfessorDisciplines, ArrayList<ProfessorDiscipline> artProfessorDisciplines, ArrayList<ProfessorDiscipline> societyProfessorDisciplines, ArrayList<StudentProfessorDiscipline> studentProfessorDisciplines) {
        this.sciProfessorDisciplines = sciProfessorDisciplines;
        this.sportProfessorDisciplines = sportProfessorDisciplines;
        this.artProfessorDisciplines = artProfessorDisciplines;
        this.societyProfessorDisciplines = societyProfessorDisciplines;
        this.studentProfessorDisciplines = studentProfessorDisciplines;
    }

    public ArrayList<ProfessorDiscipline> getSciProfessorDisciplines() {
        return sciProfessorDisciplines;
    }

    public void setSciProfessorDisciplines(ArrayList<ProfessorDiscipline> sciProfessorDisciplines) {
        this.sciProfessorDisciplines = sciProfessorDisciplines;
    }

    public ArrayList<ProfessorDiscipline> getSportProfessorDisciplines() {
        return sportProfessorDisciplines;
    }

    public void setSportProfessorDisciplines(ArrayList<ProfessorDiscipline> sportProfessorDisciplines) {
        this.sportProfessorDisciplines = sportProfessorDisciplines;
    }

    public ArrayList<ProfessorDiscipline> getArtProfessorDisciplines() {
        return artProfessorDisciplines;
    }

    public void setArtProfessorDisciplines(ArrayList<ProfessorDiscipline> artProfessorDisciplines) {
        this.artProfessorDisciplines = artProfessorDisciplines;
    }

    public ArrayList<ProfessorDiscipline> getSocietyProfessorDisciplines() {
        return societyProfessorDisciplines;
    }

    public void setSocietyProfessorDisciplines(ArrayList<ProfessorDiscipline> societyProfessorDisciplines) {
        this.societyProfessorDisciplines = societyProfessorDisciplines;
    }

    public ArrayList<StudentProfessorDiscipline> getStudentProfessorDiscipline() {
        return studentProfessorDisciplines;
    }

    public void setStudentProfessorDiscipline(ArrayList<StudentProfessorDiscipline> studentProfessorDisciplines) {
        this.studentProfessorDisciplines = studentProfessorDisciplines;
    }
}
