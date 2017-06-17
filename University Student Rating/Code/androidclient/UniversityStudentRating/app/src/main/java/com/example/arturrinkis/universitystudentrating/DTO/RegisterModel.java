package com.example.arturrinkis.universitystudentrating.DTO;

public class RegisterModel {
    private String userName;
    private String password;
    private String confirmPassword;
    private String email;
    private String firstName;
    private String lastName;
    private int genderTypeId;
    private int countryId;
    private int cityId;
    private int universityId;
    private int statusId;
    private int facultyId;
    private int courseId;
    private String specialKEY;

    public RegisterModel(){

    }

    public RegisterModel(String userName, String password, String confirmPassword, String email, String firstName, String lastName,
                         int genderTypeId, int countryId, int cityId, int universityId, int statusId) {
        this.userName = userName;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.genderTypeId = genderTypeId;
        this.countryId = countryId;
        this.cityId = cityId;
        this.universityId = universityId;
        this.statusId = statusId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getGenderTypeId() {
        return genderTypeId;
    }

    public void setGenderTypeId(int genderTypeId) {
        this.genderTypeId = genderTypeId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getUniversityId() {
        return universityId;
    }

    public void setUniversityId(int universityId) {
        this.universityId = universityId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int faciltyId) {
        this.facultyId = faciltyId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getSpecialKEY() {
        return specialKEY;
    }

    public void setSpecialKEY(String specialKEY) {
        this.specialKEY = specialKEY;
    }
}