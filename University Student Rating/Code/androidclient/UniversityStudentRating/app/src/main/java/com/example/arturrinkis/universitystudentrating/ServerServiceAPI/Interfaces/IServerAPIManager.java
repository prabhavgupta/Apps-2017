package com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces;

import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.Services.IAuthService;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.Services.IDataService;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.Services.ISearchService;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.Services.IStatisticService;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.Services.IUserService;

public interface IServerAPIManager {
    IDataService getDataServiceAPI();
    IStatisticService getStatisticService();
    IAuthService getAuthServiceAPI();
    IUserService getUserService();
    ISearchService getSearchService();
}
