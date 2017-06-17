package com.example.arturrinkis.universitystudentrating;

import com.example.arturrinkis.universitystudentrating.DTO.HttpResponse;
import com.example.arturrinkis.universitystudentrating.DTO.Rating;
import com.google.gson.Gson;

import org.junit.Test;

public class GsonTest {
    @Test
    public void TestObjectWithArrayProp(){
        Gson gson = new Gson();
        String messageJson = "{\"message\":\"[\\\"The UserName must be at least 3 characters long.\\\",\\\"The UserName field is required.\\\",\\\"The Password must be at least 6 characters long.\\\",\\\"The Password field is required.\\\",\\\"The Email field is required.\\\",\\\"Incorrect email address\\\",\\\"Minimum length is 3 characters\\\",\\\"The First name field is required.\\\",\\\"Minimum length is 3 characters\\\",\\\"The Last name field is required.\\\"]\"}";
        messageJson = messageJson.replace("\"[", "[");
        HttpResponse httpResponse = gson.fromJson(messageJson, HttpResponse.class);
    }

    @Test
    public void TestRatingObjectParse() {
        Rating rating = null;
        try {
            Gson gson = new Gson();
            String ratingJsonString = "{\"course\":{\"id\":1,\"number\":1},\"discipline\":{\"disciplineBranch\":{\"id\":2,\"name\":\"Scientific\",\"iconPath\":\"DisciplineBranchIcons/scientific_icon.png\"},\"id\":2,\"name\":\"Physics\",\"iconPath\":\"DisciplineIcons/Physics_icon.png\",\"branchId\":2},\"ratingDivision\":{\"id\":1,\"name\":\"Classwork\"},\"id\":2,\"points\":2,\"date\":\"23/05/2017\",\"courseId\":1,\"disciplineId\":2,\"studentId\":13,\"professorId\":19,\"ratingDivisionId\":1}";
            rating = gson.fromJson(ratingJsonString, Rating.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
