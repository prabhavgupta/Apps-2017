package com.example.arturrinkis.universitystudentrating.ServerServiceAPI;

import android.content.Context;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.HttpUtilities.CookieManager;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.HttpUtilities.HttpRequestUtility;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.IServerAPIManager;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.Services.IAuthService;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.Services.IDataService;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.Services.ISearchService;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.Services.IStatisticService;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.Services.IUserService;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Services.AuthService;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Services.DataService;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Services.SearchService;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Services.StatisticService;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Services.UserService;

public class ServerAPIManager implements IServerAPIManager {
    private String serverURL = "http://rxr12345-001-site1.itempurl.com";
    private Context context;
    private HttpRequestUtility httpRequestUtility;
    private CookieManager cookieManager;
    private IDataService dataServiceAPI;
    private IAuthService authServiceAPI;
    private IStatisticService statisticService;
    private IUserService userService;
    private ISearchService searchService;

    public ServerAPIManager(Context applicationContext) {
        httpRequestUtility = new HttpRequestUtility();
        cookieManager = new CookieManager(applicationContext);
        dataServiceAPI = new DataService(serverURL, httpRequestUtility, cookieManager);
        authServiceAPI = new AuthService(serverURL, httpRequestUtility, cookieManager);
        statisticService = new StatisticService(serverURL, httpRequestUtility, cookieManager);
        userService = new UserService(serverURL, httpRequestUtility, cookieManager);
        searchService = new SearchService(serverURL, httpRequestUtility, cookieManager);
    }

    public IDataService getDataServiceAPI() {
        return dataServiceAPI;
    }

    public IAuthService getAuthServiceAPI() {
        return authServiceAPI;
    }

    public IStatisticService getStatisticService() {
        return statisticService;
    }

    public IUserService getUserService() {
        return userService;
    }

    public ISearchService getSearchService() {
        return searchService;
    }
}