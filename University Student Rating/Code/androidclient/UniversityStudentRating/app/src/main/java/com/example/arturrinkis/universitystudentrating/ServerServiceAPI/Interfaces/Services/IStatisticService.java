package com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.Services;

import com.example.arturrinkis.universitystudentrating.DTO.Discipline;
import com.example.arturrinkis.universitystudentrating.DTO.DisciplineBranch;
import com.example.arturrinkis.universitystudentrating.DTO.IndividualRating;
import com.example.arturrinkis.universitystudentrating.DTO.ProfileRating;
import com.example.arturrinkis.universitystudentrating.DTO.Rating;
import com.example.arturrinkis.universitystudentrating.DTO.RatingType;

import java.util.ArrayList;

public interface IStatisticService {
    ArrayList<Rating> getRatingsForProfessorByDiscipline(int professorId, int disciplineId, int skipCount, int takeCount);
    ArrayList<DisciplineBranch> getDisciplineBranches();
    ArrayList<Discipline> getDisciplinesByBranch(int branchId);
    ProfileRating getProfileRating(int studentId, int courseId);
    IndividualRating getIndividualRating(int studentId, int disciplineId);
    ArrayList<IndividualRating> getAverageClassTopStudentsBy(int universityId, int courseId, int disciplineId, int topCount);
    ArrayList<IndividualRating> getOverallTopStudentsBy(int universityId, int courseId, int disciplineId, int topCount);
    ArrayList<IndividualRating> getOlympiadsTopStudentsBy(int universityId, int courseId, int disciplineId, int topCount);
    RatingType getRatingType(int universityId,  int disciplineId);
    double getAverageRatingForProfessor(int professorId);
    boolean setRating(Rating rating);
    boolean deleteRatingByIds(ArrayList<Integer> ratingIds);
}
