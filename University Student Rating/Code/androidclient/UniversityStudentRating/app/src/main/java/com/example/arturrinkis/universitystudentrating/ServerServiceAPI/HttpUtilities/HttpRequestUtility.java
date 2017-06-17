package com.example.arturrinkis.universitystudentrating.ServerServiceAPI.HttpUtilities;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.arturrinkis.universitystudentrating.DTO.HttpResponse;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class HttpRequestUtility {

    public String RequestToServer(String queryURL, String requestMethod, ContentValues params, CookieManager cookieManager) throws IOException {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";

        URL url = new URL(queryURL + (params != null ? getQuery(params) : ""));
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod(requestMethod);
        urlConnection.setReadTimeout(100000);
        urlConnection.setConnectTimeout(150000);
        urlConnection.setRequestProperty("Cookie", cookieManager.getCookie());
        urlConnection.connect();

        InputStream inputStream = urlConnection.getInputStream();
        StringBuffer buffer = new StringBuffer();

        reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        resultJson = buffer.toString();
        urlConnection.disconnect();

        return resultJson;
    }

    public HttpResponse AuthRequestToServer(String queryURL, Object bodyData, CookieManager cookieManager) throws IOException {
        URL url = new URL(queryURL);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        HttpResponse httpResponse = new HttpResponse();
        StringBuffer messageJson = new StringBuffer();
        Gson gson = new Gson();

        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        urlConnection.setRequestProperty("Cookie", cookieManager.getCookie());
        urlConnection.setRequestMethod("POST");
        urlConnection.setReadTimeout(100000);
        urlConnection.setConnectTimeout(150000);
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setInstanceFollowRedirects(false);
        urlConnection.connect();

        OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8");
        String json = gson.toJson(bodyData);
        writer.write(json);
        writer.close();

        try {
            String cookie = urlConnection.getHeaderFields().get("Set-Cookie").get(0);
            cookieManager.setCookie(cookie);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                messageJson.append(line);
            }
            br.close();
        } catch (Exception ex) {
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
            String line;
            while ((line = br.readLine()) != null) {
                messageJson.append(line);
            }
            httpResponse = gson.fromJson(messageJson.toString(), HttpResponse.class);
            br.close();
            ex.printStackTrace();
        }
        httpResponse.setCode(urlConnection.getResponseCode());
        urlConnection.disconnect();

        return httpResponse;
    }

    public String RequestToServer(String queryURL, Object bodyData, CookieManager cookieManager) throws IOException {
        URL url = new URL(queryURL);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        StringBuffer resultJson = new StringBuffer();
        Gson gson = new Gson();

        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        urlConnection.setRequestProperty("Cookie", cookieManager.getCookie());
        urlConnection.setRequestMethod("POST");
        urlConnection.setReadTimeout(100000);
        urlConnection.setConnectTimeout(150000);
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setInstanceFollowRedirects(false);
        urlConnection.connect();

        OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8");
        String json = gson.toJson(bodyData);
        writer.write(json);
        writer.close();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                resultJson.append(line);
            }
            br.close();
        } catch (Exception ex) {
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
            String line;
            while ((line = br.readLine()) != null) {
                resultJson.append(line);
            }
            br.close();
            ex.printStackTrace();
        }
        urlConnection.disconnect();

        return resultJson.toString();
    }

    public Bitmap downloadImage(String serverURL, String filePath, String format, CookieManager cookieManager) throws IOException {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        URL url = new URL(serverURL + "/api/Data/GetImageFile?filePath=" + filePath + "&format=" + format);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Content-Type", "image/" + format);
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(100000);
        urlConnection.setConnectTimeout(150000);
        urlConnection.setRequestProperty("Cookie", cookieManager.getCookie());
        urlConnection.setDoInput(true);
        urlConnection.connect();

        InputStream inputStream = urlConnection.getInputStream();
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        urlConnection.disconnect();

        return bitmap;
    }

    //
    //
    //private methods

    private String getQuery(ContentValues params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, Object> pair : params.valueSet())
        {
            if (first) {
                result.append("?");
                first = false;
            }
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue().toString(), "UTF-8"));
        }

        return result.toString();
    }
}
