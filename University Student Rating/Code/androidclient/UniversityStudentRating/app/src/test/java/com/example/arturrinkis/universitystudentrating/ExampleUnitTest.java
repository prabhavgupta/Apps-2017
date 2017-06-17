package com.example.arturrinkis.universitystudentrating;

import com.example.arturrinkis.universitystudentrating.DTO.RegisterModel;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.HttpUtilities.CookieManager;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.HttpUtilities.HttpRequestUtility;
import com.google.gson.Gson;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ExampleUnitTest {
    @Test
    public void RegisterRequest() throws IOException {
        URL url = new URL("http://localhost:4832/api/Account/Register");
        HttpURLConnection uc = (HttpURLConnection) url.openConnection();

        String line;
        StringBuffer jsonString = new StringBuffer();

        uc.setRequestProperty("Accept", "application/json");
        uc.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        uc.setRequestMethod("POST");
        uc.setDoInput(true);
        uc.setDoOutput(true);
        uc.setInstanceFollowRedirects(false);
        uc.connect();
        OutputStreamWriter writer = new OutputStreamWriter(uc.getOutputStream(), "UTF-8");
        RegisterModel model = new RegisterModel("1234567", "123456", "123456", "12345678@m.t", "234", "234", 1 , 1, 2, 5, 1);
        Gson gson = new Gson();
        String json = gson.toJson(model);
        writer.write(json);
        writer.close();
        try {
            String cookie = uc.getHeaderFields().get("Set-Cookie").get(0);
            BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            while((line = br.readLine()) != null){
                jsonString.append(line);
            }
            br.close();
        } catch (Exception ex) {
            BufferedReader br = new BufferedReader(new InputStreamReader(uc.getErrorStream()));
            while((line = br.readLine()) != null){
                jsonString.append(line);
            }
            ex.printStackTrace();
        }
        uc.disconnect();
        String res = jsonString.toString();
    }

    @Test
    public void LogoutRequest() throws IOException {
        URL url = new URL("http://localhost:4832/api/Account/Logout");
        HttpURLConnection uc = (HttpURLConnection) url.openConnection();

        String line;
        StringBuffer jsonString = new StringBuffer();

        uc.setRequestProperty("Accept", "application/json");
        uc.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        uc.setRequestProperty("Cookie", ".AspNet.ApplicationCookie=XwOsKlmwgbv2XAQ-XWhPMW6TQjRNLytYa97LlXQV67nzYN3nQyOwGyqZcDrrK22FEBxZc_YdKXG2LNmPUT3abA0qWjpj6ODa0xNVrR7M7rgBBeyUlmQyJnxQc2ZT6vmeRle_CqwcQv9ebwMgXYOfoJDduNBx0-geQLLnlSMY7wzgzJ-yQYFQrV_36ZWrrXnn1MGsB_1Ku3Aa7y59uFIAXQkjMzhzKazHG-N_tmgBFp38imgdqnrYn84Uwxsp-_BjBH3VnXcAEfix_WRQeVvRcZpJWLD058Ke4z0LXC3VQUrCGO7-N9PX7y-XufgaQw0EuMoZW4rYhz9kbBTTZwP2nku4jwVcR5NwTnxFbsHaWhbCfbNR0TFWjlsgv_hV1le5G5PCU4xCDFmV_36Qx2146bo8JY6Qa03ojaxofdM8cM_SQ6cll-uH1Akcm_K0AlFAnPPSJjuwKoJJ-KjWwI-yD0oI7rWPpJo1FRvRvx3AVyHQPL8n9WQ2qSxNEooVLxpx; path=/; expires=Thu, 01-Jun-2017 09:48:57 GMT; HttpOnly");
        uc.setRequestMethod("POST");
        uc.setDoInput(true);
        uc.setDoOutput(true);
        uc.setInstanceFollowRedirects(false);
        uc.connect();
        OutputStreamWriter writer = new OutputStreamWriter(uc.getOutputStream(), "UTF-8");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            while((line = br.readLine()) != null){
                jsonString.append(line);
            }
            br.close();
        } catch (Exception ex) {
            BufferedReader br = new BufferedReader(new InputStreamReader(uc.getErrorStream()));
            while((line = br.readLine()) != null){
                jsonString.append(line);
            }
            ex.printStackTrace();
        }
        uc.disconnect();
        String res = jsonString.toString();
    }

    @Test
    public void downloadImageRequest() throws IOException{
        HttpRequestUtility httpRequestUtility = new HttpRequestUtility();
        CookieManager cookieManager = new CookieManager("");

        String params = "?filePath=DisciplineBranchIcons/sport_icon.png";
        //String data = httpRequestUtility.downloadImage("http://localhost:4832", params, cookieManager);
    }
}