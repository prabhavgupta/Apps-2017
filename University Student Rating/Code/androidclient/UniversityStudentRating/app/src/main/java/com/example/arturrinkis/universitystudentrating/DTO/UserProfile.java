package com.example.arturrinkis.universitystudentrating.DTO;

import android.graphics.Bitmap;

public class UserProfile {
    private int id;
    private String firstName;
    private String lastName;
    private String photoPath;
    private Integer facultyId;
    private Integer courseId;
    private int genderTypeId;
    private int countryId;
    private int cityId;
    private int universityId;
    private int statusId;
    private City city;
    private Country country;
    private Course course;
    private Faculty faculty;
    private GenderType genderType;
    private University university;
    private Status status;

    private Bitmap photoPathBitmap;

    public UserProfile(int id, String firstName, String lastName){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public UserProfile(int id, University university, Status status, String firstName, String lastName, String photoPath, Integer facultyId, Integer courseId, int genderTypeId, int countryId, int cityId, int universityId, int statusId, City city, Country country, Course course, Faculty faculty, GenderType genderType) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoPath = photoPath;
        this.facultyId = facultyId;
        this.courseId = courseId;
        this.genderTypeId = genderTypeId;
        this.countryId = countryId;
        this.cityId = cityId;
        this.universityId = universityId;
        this.statusId = statusId;
        this.city = city;
        this.country = country;
        this.course = course;
        this.faculty = faculty;
        this.genderType = genderType;
        this.university = university;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Integer getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Integer facultyId) {
        this.facultyId = facultyId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
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

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public GenderType getGenderType() {
        return genderType;
    }

    public void setGenderType(GenderType genderType) {
        this.genderType = genderType;
    }

    public Bitmap getPhotoPathBitmap() {
        return photoPathBitmap;
    }

    public void setPhotoPathBitmap(Bitmap photoPathBitmap) {
        this.photoPathBitmap = photoPathBitmap;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
