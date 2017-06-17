package com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Services;

import android.graphics.Bitmap;

import com.example.arturrinkis.universitystudentrating.DTO.ConcurentModel;
import com.example.arturrinkis.universitystudentrating.DTO.ConcurentResult;
import com.example.arturrinkis.universitystudentrating.DTO.SearchModel;
import com.example.arturrinkis.universitystudentrating.DTO.UserProfile;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.HttpUtilities.CookieManager;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.HttpUtilities.HttpRequestUtility;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.Services.ISearchService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SearchService implements ISearchService {
    private String serverURL;
    private String serviceURL = "/api/Search";
    private HttpRequestUtility httpRequestUtility;
    private CookieManager cookieManager;

    public SearchService(String serverURL, HttpRequestUtility httpRequestUtility, CookieManager cookieManager) {
        this.serverURL = serverURL;
        this.httpRequestUtility = httpRequestUtility;
        this.cookieManager = cookieManager;
    }

    @Override
    public ArrayList<UserProfile> searchUsers(SearchModel model) {
        Gson gson = new Gson();
        String methodURL = "/SearchUsers";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL + methodURL, model, cookieManager);
            ArrayList<UserProfile> userProfiles = gson.fromJson(resultJson, new TypeToken<List<UserProfile>>() {
            }.getType());
            for (int i = 0; i < userProfiles.size(); ++i) {
                Bitmap bitmap = httpRequestUtility.downloadImage(serverURL, userProfiles.get(i).getPhotoPath(), "png", cookieManager);
                userProfiles.get(i).setPhotoPathBitmap(bitmap);
            }
            return userProfiles;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<ConcurentResult> searchConcurents(ConcurentModel model) {
        Gson gson = new Gson();
        String methodURL = "/SearchConcurents";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL + methodURL, model, cookieManager);
            ArrayList<ConcurentResult> concurentResults = gson.fromJson(resultJson, new TypeToken<List<ConcurentResult>>() {
            }.getType());
            for (int i = 0; i < concurentResults.size(); ++i) {
                Bitmap bitmap = httpRequestUtility.downloadImage(serverURL, concurentResults.get(i).getUserProfile().getPhotoPath(), "jpg", cookieManager);
                concurentResults.get(i).getUserProfile().setPhotoPathBitmap(bitmap);
            }
            return concurentResults;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}