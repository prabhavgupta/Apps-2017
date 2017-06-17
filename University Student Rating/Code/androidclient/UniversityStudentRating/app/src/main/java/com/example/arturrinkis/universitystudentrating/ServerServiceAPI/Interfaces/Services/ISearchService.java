package com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.Services;

import com.example.arturrinkis.universitystudentrating.DTO.ConcurentModel;
import com.example.arturrinkis.universitystudentrating.DTO.ConcurentResult;
import com.example.arturrinkis.universitystudentrating.DTO.SearchModel;
import com.example.arturrinkis.universitystudentrating.DTO.UserProfile;

import java.util.ArrayList;

public interface ISearchService {
    ArrayList<UserProfile> searchUsers(SearchModel model);
    ArrayList<ConcurentResult> searchConcurents(ConcurentModel model);
}
