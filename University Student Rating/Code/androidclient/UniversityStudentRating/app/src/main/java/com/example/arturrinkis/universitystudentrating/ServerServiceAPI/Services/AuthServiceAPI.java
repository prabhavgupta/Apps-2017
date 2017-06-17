package com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Services;

import com.example.arturrinkis.universitystudentrating.DTO.HttpResponse;
import com.example.arturrinkis.universitystudentrating.DTO.LoginModel;
import com.example.arturrinkis.universitystudentrating.DTO.RegisterModel;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.HttpUtilities.CookieManager;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.HttpUtilities.HttpRequestUtility;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.Services.IAuthServiceAPI;
import com.google.gson.Gson;

import java.io.IOException;

public class AuthServiceAPI implements IAuthServiceAPI {
    private String serverURL;
    private String serviceURL = "/api/Account";
    private HttpRequestUtility httpRequestUtility;
    private CookieManager cookieManager;

    public AuthServiceAPI(String serverURL, HttpRequestUtility httpRequestUtility, CookieManager cookieManager) {
        this.serverURL = serverURL;
        this.httpRequestUtility = httpRequestUtility;
        this.cookieManager = cookieManager;
    }

    @Override
    public String GetServiceURL() {
        return serviceURL;
    }

    @Override
    public HttpResponse register(RegisterModel model){
        String methodURL = "/Register";
        try {
            HttpResponse httpResponse = httpRequestUtility.AuthRequestToServer(serverURL+serviceURL + methodURL, model, cookieManager);
            return httpResponse;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public HttpResponse logIn(LoginModel model) {
        String methodURL = "/Login";
        try {
            HttpResponse httpResponse = httpRequestUtility.AuthRequestToServer(serverURL+serviceURL + methodURL, model, cookieManager);
            return httpResponse;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public HttpResponse logOut() {
        String methodURL = "/Logout";
        try {
            HttpResponse httpResponse = httpRequestUtility.AuthRequestToServer(serverURL+serviceURL + methodURL, null, cookieManager);
            return httpResponse;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        Gson gson = new Gson();
        String methodURL = "/IsAuthenticated";
        try {
            String resultJson = httpRequestUtility.RequestToServer(serverURL+serviceURL+methodURL, "GET", null, cookieManager);
            boolean isAuthenticated = gson.fromJson(resultJson, Boolean.TYPE);
            return isAuthenticated;
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return false;
    }
}