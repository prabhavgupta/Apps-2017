package com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Services;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.util.Base64;

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
import com.example.arturrinkis.universitystudentrating.DTO.UserProfile;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.HttpUtilities.CookieManager;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.HttpUtilities.HttpRequestUtility;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.Services.IDataService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataService implements IDataService {
    private String serverURL;
    private String serviceURL = "/api/Data";
    private HttpRequestUtility httpRequestUtility;
    private CookieManager cookieManager;

    public DataService(String serverURL, HttpRequestUtility httpRequestUtility, CookieManager cookieManager) {
        this.serverURL = serverURL;
        this.httpRequestUtility = httpRequestUtility;
        this.cookieManager = cookieManager;
    }

    @Override
    public String GetServiceURL() {
        return serviceURL;
    }

    @Override
    public ArrayList<GenderType> getGenderTypes() {
        Gson gson = new Gson();
        String methodURL = "/GetGenderTypes";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", null, cookieManager);
            ArrayList<GenderType> genderTypes = gson.fromJson(resultJson, new TypeToken<List<GenderType>>() {
            }.getType());
            return genderTypes;
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Country> getCountries() {
        Gson gson = new Gson();
        String methodURL = "/GetCountries";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", null, cookieManager);
            ArrayList<Country> countries = gson.fromJson(resultJson, new TypeToken<List<Country>>() {
            }.getType());
            return countries;
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<City> getCitiesByCountry(int countryId) {
        Gson gson = new Gson();
        ContentValues params = new ContentValues();
        params.put("countryId", countryId);
        String methodURL = "/GetCitiesByCountry";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", params, cookieManager);
            ArrayList<City> cities = gson.fromJson(resultJson, new TypeToken<List<City>>() {
            }.getType());
            return cities;
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<University> getUniversitiesByCity(int cityId) {
        Gson gson = new Gson();
        ContentValues params = new ContentValues();
        params.put("cityId", cityId);
        String methodURL = "/GetUniversitiesByCity";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", params, cookieManager);
            ArrayList<University> universities = gson.fromJson(resultJson, new TypeToken<List<University>>() {
            }.getType());
            return universities;
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Status> getStatuses() {
        Gson gson = new Gson();
        String methodURL = "/GetStatuses";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", null, cookieManager);
            ArrayList<Status> statuses = gson.fromJson(resultJson, new TypeToken<List<Status>>() {
            }.getType());
            return statuses;
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Faculty> getFacultiesByUniversity(int universityId) {
        Gson gson = new Gson();
        ContentValues params = new ContentValues();
        params.put("universityId", universityId);
        String methodURL = "/GetFacultiesByUniversity";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", params, cookieManager);
            ArrayList<Faculty> faculties = gson.fromJson(resultJson, new TypeToken<List<Faculty>>() {
            }.getType());
            return faculties;
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Course> getCourses() {
        Gson gson = new Gson();
        String methodURL = "/GetCourses";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", null, cookieManager);
            ArrayList<Course> courses = gson.fromJson(resultJson, new TypeToken<List<Course>>() {
            }.getType());
            return courses;
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<ProfessorDiscipline> getProfessorDisciplines(int professorId) {
        Gson gson = new Gson();
        ContentValues params = new ContentValues();
        params.put("professorId", professorId);
        String methodURL = "/GetProfessorDisciplines";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", params, cookieManager);
            ArrayList<ProfessorDiscipline> professorDisciplines  = gson.fromJson(resultJson, new TypeToken<List<ProfessorDiscipline>>() {
            }.getType());

            for(int i = 0; i < professorDisciplines.size(); ++i) {
                Bitmap bitmap = httpRequestUtility.downloadImage(serverURL, professorDisciplines.get(i).getUserProfile().getPhotoPath(), "png", cookieManager);
                professorDisciplines.get(i).getUserProfile().setPhotoPathBitmap(bitmap);
            }

            return professorDisciplines;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Discipline> getDisciplinesByProfessor(int professorId) {
        Gson gson = new Gson();
        ContentValues params = new ContentValues();
        params.put("professorId", professorId);
        String methodURL = "/GetDisciplinesByProfessor";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", params, cookieManager);
            ArrayList<Discipline> professorDisciplines  = gson.fromJson(resultJson, new TypeToken<List<Discipline>>() {
            }.getType());
            for(int i = 0; i < professorDisciplines.size(); ++i){
                Bitmap bitmap = httpRequestUtility.downloadImage(serverURL, professorDisciplines.get(i).getIconPath(), "png", cookieManager);
                professorDisciplines.get(i).setIconBitmap(bitmap);
            }
            return professorDisciplines;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<StudentProfessorDiscipline> getStudentProfessorDisciplines(int studentId) {
        Gson gson = new Gson();
        ContentValues params = new ContentValues();
        params.put("studentId", studentId);
        String methodURL = "/GetStudentProfessorDisciplines";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", params, cookieManager);
            ArrayList<StudentProfessorDiscipline> studentProfessorDisciplines  = gson.fromJson(resultJson, new TypeToken<List<StudentProfessorDiscipline>>() {
            }.getType());

            return studentProfessorDisciplines;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BranchProfessorDiscipline getBranchProfessorDiscipline(int studentId, int universityId) {
        Gson gson = new Gson();
        ContentValues params = new ContentValues();
        params.put("studentId", studentId);
        params.put("universityId", universityId);
        String methodURL = "/GetBranchProfessorDiscipline";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", params, cookieManager);
            BranchProfessorDiscipline branchProfessorDiscipline = gson.fromJson(resultJson, BranchProfessorDiscipline.class);

            for(int i = 0; i < branchProfessorDiscipline.getSciProfessorDisciplines().size(); ++i) {
                UserProfile userProfile = branchProfessorDiscipline.getSciProfessorDisciplines().get(i).getUserProfile();
                Bitmap bitmap = httpRequestUtility.downloadImage(serverURL, userProfile.getPhotoPath(), "png", cookieManager);
                userProfile.setPhotoPathBitmap(bitmap);
            }
            for(int i = 0; i < branchProfessorDiscipline.getSportProfessorDisciplines().size(); ++i) {
                UserProfile userProfile = branchProfessorDiscipline.getSportProfessorDisciplines().get(i).getUserProfile();
                Bitmap bitmap = httpRequestUtility.downloadImage(serverURL, userProfile.getPhotoPath(), "png", cookieManager);
                userProfile.setPhotoPathBitmap(bitmap);
            }
            for(int i = 0; i < branchProfessorDiscipline.getArtProfessorDisciplines().size(); ++i) {
                UserProfile userProfile = branchProfessorDiscipline.getArtProfessorDisciplines().get(i).getUserProfile();
                Bitmap bitmap = httpRequestUtility.downloadImage(serverURL, userProfile.getPhotoPath(), "png", cookieManager);
                userProfile.setPhotoPathBitmap(bitmap);
            }
            for(int i = 0; i < branchProfessorDiscipline.getSocietyProfessorDisciplines().size(); ++i) {
                UserProfile userProfile = branchProfessorDiscipline.getSocietyProfessorDisciplines().get(i).getUserProfile();
                Bitmap bitmap = httpRequestUtility.downloadImage(serverURL, userProfile.getPhotoPath(), "png", cookieManager);
                userProfile.setPhotoPathBitmap(bitmap);
            }

            return branchProfessorDiscipline;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<RatingDivision> GetRatingDivisions() {
        Gson gson = new Gson();
        String methodURL = "/GetRatingDivisions";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", null, cookieManager);
            ArrayList<RatingDivision> ratingDivisions = gson.fromJson(resultJson, new TypeToken<List<RatingDivision>>() {
            }.getType());
            return ratingDivisions;
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean uploadPhotoImageFile(Bitmap bitmap, String filePath) {
        Gson gson = new Gson();
        String methodURL = "/UploadPhotoImageFile";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            String base64 = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("base64Image", base64);
            jsonObject.addProperty("filePath", filePath + ".jpg");

            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL + methodURL, jsonObject, cookieManager);

            return true;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
}