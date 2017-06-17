package com.example.arturrinkis.universitystudentrating.DTO;

public class ConcurentModel {
    private int skipCount;
    private int takeCount;
    private int studentId;
    private int courseId;
    private Integer universityId;
    private Integer disciplineBranchId;
    private Integer disciplineId;
    private Integer facultyId;
    private int studentRatingType;

    public int getSkipCount() {
        return skipCount;
    }

    public void setSkipCount(int skipCount) {
        this.skipCount = skipCount;
    }

    public int getTakeCount() {
        return takeCount;
    }

    public void setTakeCount(int takeCount) {
        this.takeCount = takeCount;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Integer getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Integer universityId) {
        this.universityId = universityId;
    }

    public Integer getDisciplineBranchId() {
        return disciplineBranchId;
    }

    public void setDisciplineBranchId(Integer disciplineBranchId) {
        this.disciplineBranchId = disciplineBranchId;
    }

    public Integer getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(Integer disciplineId) {
        this.disciplineId = disciplineId;
    }

    public Integer getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Integer facultyId) {
        this.facultyId = facultyId;
    }

    public int getStudentRatingType() {
        return studentRatingType;
    }

    public void setStudentRatingType(int studentRatingType) {
        this.studentRatingType = studentRatingType;
    }
}
