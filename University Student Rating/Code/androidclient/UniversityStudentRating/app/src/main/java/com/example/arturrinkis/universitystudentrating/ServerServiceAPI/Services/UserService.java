package com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Services;

import android.content.ContentValues;
import android.graphics.Bitmap;

import com.example.arturrinkis.universitystudentrating.DTO.AuthUser;
import com.example.arturrinkis.universitystudentrating.DTO.SaveProfessorDisciplinesModel;
import com.example.arturrinkis.universitystudentrating.DTO.SaveStudentProfessorDistModel;
import com.example.arturrinkis.universitystudentrating.DTO.UserProfile;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.HttpUtilities.CookieManager;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.HttpUtilities.HttpRequestUtility;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.Services.IUserService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IUserService {
    private String serverURL;
    private String serviceURL = "/api/User";
    private HttpRequestUtility httpRequestUtility;
    private CookieManager cookieManager;

    public UserService(String serverURL, HttpRequestUtility httpRequestUtility, CookieManager cookieManager) {
        this.serverURL = serverURL;
        this.httpRequestUtility = httpRequestUtility;
        this.cookieManager = cookieManager;
    }

    @Override
    public UserProfile getUserProfileById(int id) {
        Gson gson = new Gson();
        ContentValues params = new ContentValues();
        params.put("id", id);
        String methodURL = "/GetUserProfileById";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", params, cookieManager);
            UserProfile userProfile = gson.fromJson(resultJson, UserProfile.class);
            Bitmap bitmap = httpRequestUtility.downloadImage(serverURL, userProfile.getPhotoPath(), "jpd", cookieManager);
            userProfile.setPhotoPathBitmap(bitmap);

            return userProfile;
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<UserProfile> getUserProfiles() {
        Gson gson = new Gson();
        String methodURL = "/GetUserProfiles";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", null, cookieManager);
            ArrayList<UserProfile> userProfiles = gson.fromJson(resultJson, new TypeToken<List<UserProfile>>() {
            }.getType());
            for(int i = 0; i < userProfiles.size(); ++i){
                Bitmap bitmap = httpRequestUtility.downloadImage(serverURL, userProfiles.get(i).getPhotoPath(), "jpg", cookieManager);
                userProfiles.get(i).setPhotoPathBitmap(bitmap);
            }
            return userProfiles;
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<UserProfile> getStudentUserProfiles(int universityId, int facultyId, int courseId) {
        Gson gson = new Gson();
        ContentValues params = new ContentValues();
        params.put("universityId", universityId);
        params.put("facultyId", facultyId);
        params.put("courseId", courseId);
        String methodURL = "/GetStudentUserProfiles";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", params, cookieManager);
            ArrayList<UserProfile> userProfiles = gson.fromJson(resultJson, new TypeToken<List<UserProfile>>() {
            }.getType());
            return userProfiles;
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<UserProfile> getStudentUserProfilesByProfDisc(int universityId, int facultyId, int courseId, int professorId, int disciplineId) {
        Gson gson = new Gson();
        ContentValues params = new ContentValues();
        params.put("universityId", universityId);
        params.put("facultyId", facultyId);
        params.put("courseId", courseId);
        params.put("professorId", professorId);
        params.put("discipineId", disciplineId);
        String methodURL = "/GetStudentUserProfilesByProfDisc";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", params, cookieManager);
            ArrayList<UserProfile> userProfiles = gson.fromJson(resultJson, new TypeToken<List<UserProfile>>() {
            }.getType());
            return userProfiles;
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public AuthUser getAuthUser(boolean doLoadPhoto) {
        Gson gson = new Gson();
        String methodURL = "/GetAuthUserProfile";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", null, cookieManager);
            AuthUser authUser = gson.fromJson(resultJson, AuthUser.class);
            if(doLoadPhoto == true){
                Bitmap bitmap = httpRequestUtility.downloadImage(serverURL, authUser.getUserProfile().getPhotoPath(), "jpg", cookieManager);
                authUser.getUserProfile().setPhotoPathBitmap(bitmap);
            }
            return authUser;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean saveStudentProfessorDisciplines(SaveStudentProfessorDistModel model){
        Gson gson = new Gson();
        String methodURL = "/SaveStudentProfessorDisciplines";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL + methodURL, model, cookieManager);
            Boolean result = gson.fromJson(resultJson, Boolean.class);
            return result;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean saveProfessorDisciplines(SaveProfessorDisciplinesModel model){
        Gson gson = new Gson();
        String methodURL = "/SaveProfessorDisciplines";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL + methodURL, model, cookieManager);
            Boolean result = gson.fromJson(resultJson, Boolean.class);
            return result;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }
}