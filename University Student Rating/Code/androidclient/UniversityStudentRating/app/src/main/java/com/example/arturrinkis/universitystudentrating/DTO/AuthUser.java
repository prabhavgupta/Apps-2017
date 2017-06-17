package com.example.arturrinkis.universitystudentrating.DTO;

public class AuthUser {
    private String userName;
    private String email;

    private Integer userProfileId;
    private UserProfile userProfile;

    public AuthUser(String userName, String email, Integer userProfileId, UserProfile userProfile) {
        this.userName = userName;
        this.email = email;
        this.userProfileId = userProfileId;
        this.userProfile = userProfile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(Integer userProfileId) {
        this.userProfileId = userProfileId;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
