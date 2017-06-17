package com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.Services;

import com.example.arturrinkis.universitystudentrating.DTO.AuthUser;
import com.example.arturrinkis.universitystudentrating.DTO.SaveProfessorDisciplinesModel;
import com.example.arturrinkis.universitystudentrating.DTO.SaveStudentProfessorDistModel;
import com.example.arturrinkis.universitystudentrating.DTO.UserProfile;

import java.util.ArrayList;

public interface IUserService {
    UserProfile getUserProfileById(int id);
    ArrayList<UserProfile> getUserProfiles();
    ArrayList<UserProfile> getStudentUserProfiles(int universityId, int facultyId, int courseId);
    ArrayList<UserProfile> getStudentUserProfilesByProfDisc(int universityId, int facultyId, int courseId, int professorId, int disciplineId);
    AuthUser getAuthUser(boolean doLoadPhoto);
    boolean saveStudentProfessorDisciplines(SaveStudentProfessorDistModel model);
    boolean saveProfessorDisciplines(SaveProfessorDisciplinesModel model);
}
