package com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.Services;

import com.example.arturrinkis.universitystudentrating.DTO.HttpResponse;
import com.example.arturrinkis.universitystudentrating.DTO.LoginModel;
import com.example.arturrinkis.universitystudentrating.DTO.RegisterModel;

public interface IAuthServiceAPI {
    String GetServiceURL();
    HttpResponse register(RegisterModel model);
    HttpResponse logIn(LoginModel model);
    HttpResponse logOut();
    boolean isAuthenticated();
}
