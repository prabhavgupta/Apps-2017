package com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Services;

import android.content.ContentValues;
import android.graphics.Bitmap;

import com.example.arturrinkis.universitystudentrating.DTO.Discipline;
import com.example.arturrinkis.universitystudentrating.DTO.DisciplineBranch;
import com.example.arturrinkis.universitystudentrating.DTO.IndividualRating;
import com.example.arturrinkis.universitystudentrating.DTO.ProfileRating;
import com.example.arturrinkis.universitystudentrating.DTO.Rating;
import com.example.arturrinkis.universitystudentrating.DTO.RatingType;
import com.example.arturrinkis.universitystudentrating.DTO.UserProfile;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.HttpUtilities.CookieManager;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.HttpUtilities.HttpRequestUtility;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.Services.IStatisticService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StatisticService implements IStatisticService {
    private String serverURL;
    private String serviceURL = "/api/Statistic";
    private HttpRequestUtility httpRequestUtility;
    private CookieManager cookieManager;

    public StatisticService(String serverURL, HttpRequestUtility httpRequestUtility, CookieManager cookieManager) {
        this.serverURL = serverURL;
        this.httpRequestUtility = httpRequestUtility;
        this.cookieManager = cookieManager;
    }

    @Override
    public ArrayList<Rating> getRatingsForProfessorByDiscipline(int professorId, int disciplineId, int skipCount, int takeCount){
        Gson gson = new Gson();
        ContentValues params = new ContentValues();
        params.put("professorId", professorId);
        params.put("disciplineId", disciplineId);
        params.put("skipCount", skipCount);
        params.put("takeCount", takeCount);
        String methodURL = "/GetRatingsForProfessorByDiscipline";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", params, cookieManager);
            ArrayList<Rating> ratings = gson.fromJson(resultJson, new TypeToken<List<Rating>>() {
            }.getType());
            return ratings;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<DisciplineBranch> getDisciplineBranches() {
        Gson gson = new Gson();
        String methodURL = "/GetDisciplineBranches";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", null, cookieManager);
            ArrayList<DisciplineBranch> disciplineBranches = gson.fromJson(resultJson, new TypeToken<List<DisciplineBranch>>() {
            }.getType());
            for(int i = 0; i < disciplineBranches.size(); ++i){
                Bitmap bitmap = httpRequestUtility.downloadImage(serverURL, disciplineBranches.get(i).getIconPath(), "png", cookieManager);
                disciplineBranches.get(i).setIconBitmap(bitmap);
            }
            return disciplineBranches;
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Discipline> getDisciplinesByBranch(int branchId) {
        Gson gson = new Gson();
        ContentValues params = new ContentValues();
        params.put("branchId", branchId);
        String methodURL = "/GetDisciplinesByBranch";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", params, cookieManager);
            ArrayList<Discipline> disciplines = gson.fromJson(resultJson, new TypeToken<List<Discipline>>() {
            }.getType());
            for(int i = 0; i < disciplines.size(); ++i){
                Bitmap bitmap = httpRequestUtility.downloadImage(serverURL, disciplines.get(i).getIconPath(), "png", cookieManager);
                disciplines.get(i).setIconBitmap(bitmap);
            }
            return disciplines;
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ProfileRating getProfileRating(int studentId, int courseId) {
        Gson gson = new Gson();
        ContentValues params = new ContentValues();
        params.put("studentId", studentId);
        params.put("courseId", courseId);
        String methodURL = "/GetProfileRating";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", params, cookieManager);
            ProfileRating profileRating = gson.fromJson(resultJson, ProfileRating.class);
            return profileRating;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IndividualRating getIndividualRating(int studentId, int disciplineId) {
        Gson gson = new Gson();
        ContentValues params = new ContentValues();
        params.put("studentId", studentId);
        params.put("disciplineId", disciplineId);
        String methodURL = "/GetIndividualRating";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", params, cookieManager);
            IndividualRating individualRating = gson.fromJson(resultJson, IndividualRating.class);
            UserProfile userProfile = individualRating.getUserProfile();
            Bitmap bitmap = httpRequestUtility.downloadImage(serverURL, userProfile.getPhotoPath(), "jpg", cookieManager);
            userProfile.setPhotoPathBitmap(bitmap);
            return individualRating;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<IndividualRating> getAverageClassTopStudentsBy(int universityId, int courseId, int disciplineId, int topCount) {
        Gson gson = new Gson();
        ContentValues params = new ContentValues();
        params.put("universityId", universityId);
        params.put("courseId", courseId);
        params.put("disciplineId", disciplineId);
        params.put("topCount", topCount);
        String methodURL = "/GetAverageClassTopStudentsBy";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", params, cookieManager);
            ArrayList<IndividualRating> ratings = gson.fromJson(resultJson, new TypeToken<List<IndividualRating>>() {
            }.getType());
            for(int i = 0; i < ratings.size(); ++i){
                UserProfile userProfile = ratings.get(i).getUserProfile();
                Bitmap bitmap = httpRequestUtility.downloadImage(serverURL, userProfile.getPhotoPath(), "jpg", cookieManager);
                userProfile.setPhotoPathBitmap(bitmap);
            }
            return ratings;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<IndividualRating> getOverallTopStudentsBy(int universityId, int courseId, int disciplineId, int topCount) {
        Gson gson = new Gson();
        ContentValues params = new ContentValues();
        params.put("universityId", universityId);
        params.put("courseId", courseId);
        params.put("disciplineId", disciplineId);
        params.put("topCount", topCount);
        String methodURL = "/GetOverallTopStudentsBy";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", params, cookieManager);
            ArrayList<IndividualRating> ratings = gson.fromJson(resultJson, new TypeToken<List<IndividualRating>>() {
            }.getType());
            for(int i = 0; i < ratings.size(); ++i){
                UserProfile userProfile = ratings.get(i).getUserProfile();
                Bitmap bitmap = httpRequestUtility.downloadImage(serverURL, userProfile.getPhotoPath(), "jpg", cookieManager);
                userProfile.setPhotoPathBitmap(bitmap);
            }
            return ratings;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<IndividualRating> getOlympiadsTopStudentsBy(int universityId, int courseId, int disciplineId, int topCount) {
        Gson gson = new Gson();
        ContentValues params = new ContentValues();
        params.put("universityId", universityId);
        params.put("courseId", courseId);
        params.put("disciplineId", disciplineId);
        params.put("topCount", topCount);
        String methodURL = "/GetOlympiadsTopStudentsBy";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", params, cookieManager);
            ArrayList<IndividualRating> ratings = gson.fromJson(resultJson, new TypeToken<List<IndividualRating>>() {
            }.getType());
            for(int i = 0; i < ratings.size(); ++i){
                UserProfile userProfile = ratings.get(i).getUserProfile();
                Bitmap bitmap = httpRequestUtility.downloadImage(serverURL, userProfile.getPhotoPath(), "jpg", cookieManager);
                userProfile.setPhotoPathBitmap(bitmap);
            }
            return ratings;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public RatingType getRatingType(int universityId, int disciplineId) {
        Gson gson = new Gson();
        ContentValues params = new ContentValues();
        params.put("universityId", universityId);
        params.put("disciplineId", disciplineId);
        String methodURL = "/GetRatingType";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", params, cookieManager);
            RatingType ratingType = gson.fromJson(resultJson, RatingType.class);
            return ratingType;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean setRating(Rating rating){
        Gson gson = new Gson();
        String methodURL = "/SetRating";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL + methodURL, rating, cookieManager);
            Boolean result = gson.fromJson(resultJson, Boolean.class);
            return result;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public double getAverageRatingForProfessor(int professorId) {
        Gson gson = new Gson();
        ContentValues params = new ContentValues();
        params.put("professorId", professorId);
        String methodURL = "/GetAverageRatingForProfessor";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", params, cookieManager);
            double rating = gson.fromJson(resultJson, Double.class);
            return rating;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return 0.0;
    }

    @Override
    public boolean deleteRatingByIds(ArrayList<Integer> ratingIds){
        Gson gson = new Gson();
        String methodURL = "/DeleteRatingByIds";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL + methodURL, ratingIds, cookieManager);
            Boolean result = gson.fromJson(resultJson, Boolean.class);
            return result;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
}