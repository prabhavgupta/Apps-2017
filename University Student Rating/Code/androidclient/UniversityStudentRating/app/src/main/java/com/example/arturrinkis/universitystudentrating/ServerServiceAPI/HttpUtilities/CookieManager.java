package com.example.arturrinkis.universitystudentrating.ServerServiceAPI.HttpUtilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class CookieManager {
    private SharedPreferences sharedPreferences;
    private Context context;

    private String cookie;

    public CookieManager(String cookie){
        this.cookie = cookie;
    }

    public CookieManager(Context applicationContext){
        context = applicationContext;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        cookie = sharedPreferences.getString("Cookie", "");
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Cookie", this.cookie);
        editor.commit();
    }
}
