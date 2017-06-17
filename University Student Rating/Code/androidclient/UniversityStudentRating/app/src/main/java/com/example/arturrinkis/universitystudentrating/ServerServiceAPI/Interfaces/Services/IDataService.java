package com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.Services;

import android.graphics.Bitmap;

import com.example.arturrinkis.universitystudentrating.DTO.BranchProfessorDiscipline;
import com.example.arturrinkis.universitystudentrating.DTO.City;
import com.example.arturrinkis.universitystudentrating.DTO.Country;
import com.example.arturrinkis.universitystudentrating.DTO.Course;
import com.example.arturrinkis.universitystudentrating.DTO.Discipline;
import com.example.arturrinkis.universitystudentrating.DTO.Faculty;
import com.example.arturrinkis.universitystudentrating.DTO.GenderType;
import com.example.arturrinkis.universitystudentrating.DTO.ProfessorDiscipline;
import com.example.arturrinkis.universitystudentrating.DTO.RatingDivision;
import com.example.arturrinkis.universitystudentrating.DTO.Status;
import com.example.arturrinkis.universitystudentrating.DTO.StudentProfessorDiscipline;
import com.example.arturrinkis.universitystudentrating.DTO.University;

import java.util.ArrayList;

public interface IDataService {
    String GetServiceURL();
    ArrayList<GenderType> getGenderTypes();
    ArrayList<Country> getCountries();
    ArrayList<City> getCitiesByCountry(int countryId);
    ArrayList<University> getUniversitiesByCity(int cityId);
    ArrayList<Status> getStatuses();
    ArrayList<Faculty> getFacultiesByUniversity(int universityId);
    ArrayList<Course> getCourses();
    ArrayList<ProfessorDiscipline> getProfessorDisciplines(int professorId);
    ArrayList<Discipline> getDisciplinesByProfessor(int professorId);
    ArrayList<StudentProfessorDiscipline> getStudentProfessorDisciplines(int studentId);
    BranchProfessorDiscipline getBranchProfessorDiscipline(int studentId, int universityId);
    ArrayList<RatingDivision> GetRatingDivisions();
    boolean uploadPhotoImageFile(Bitmap bitmap, String filePath);
}