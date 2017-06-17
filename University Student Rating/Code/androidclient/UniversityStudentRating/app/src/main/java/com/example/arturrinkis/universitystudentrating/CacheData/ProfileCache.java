package com.example.arturrinkis.universitystudentrating.CacheData;

import com.example.arturrinkis.universitystudentrating.DTO.AuthUser;
import com.example.arturrinkis.universitystudentrating.DTO.BranchProfessorDiscipline;
import com.example.arturrinkis.universitystudentrating.DTO.ConcurentModel;
import com.example.arturrinkis.universitystudentrating.DTO.ConcurentResult;
import com.example.arturrinkis.universitystudentrating.DTO.Discipline;
import com.example.arturrinkis.universitystudentrating.DTO.DisciplineBranch;
import com.example.arturrinkis.universitystudentrating.DTO.IndividualRating;
import com.example.arturrinkis.universitystudentrating.DTO.ProfessorDiscipline;
import com.example.arturrinkis.universitystudentrating.DTO.ProfileRating;
import com.example.arturrinkis.universitystudentrating.DTO.Rating;
import com.example.arturrinkis.universitystudentrating.DTO.RatingType;
import com.example.arturrinkis.universitystudentrating.DTO.SearchModel;
import com.example.arturrinkis.universitystudentrating.DTO.StudentProfessorDiscipline;
import com.example.arturrinkis.universitystudentrating.DTO.UserProfile;

import java.util.ArrayList;

public class ProfileCache {
    private AuthUser authUser = null;
    private ProfileRating profileRating;
    private BranchProfessorDiscipline branchProfessorDiscipline;
    private IndividualRating individualRating;
    private ArrayList<IndividualRating> averageClassTopStudents;
    private ArrayList<IndividualRating> overallTopStudents;
    private ArrayList<IndividualRating> olympiadsTopStudents;
    private ArrayList<Rating> professorRatings;
    private ArrayList<StudentProfessorDiscipline> studentProfessorDisciplines;
    private RatingType ratingType;
    private ArrayList<DisciplineBranch> disciplineBranches;
    private ArrayList<Discipline> disciplines;
    private ArrayList<ProfessorDiscipline> professorDisciplinesProfile;
    private ArrayList<Discipline> sciDisciplinesProfile;
    private ArrayList<Discipline> sportDisciplinesProfile;
    private ArrayList<Discipline> artDisciplinesProfile;
    private ArrayList<Discipline> socDisciplinesProfile;
    private SearchModel searchModel;
    private ConcurentModel concurentModel;
    private ArrayList<UserProfile> searchUserResult;
    private ArrayList<ConcurentResult> concurentResults;
    private int topCount = 5;
    private int takeCount = 10;
    private int professorRatingTakeCount = 50;
    private double averageProfessorRating = 0;
    private ArrayList<String> studentRatingTypes = new ArrayList<String>() {{ add("Average"); add("Overall"); add("Olympiads"); }};

    private UserProfile showingUser = null;
    private ProfileRating showingProfileRating;
    private double averageShowingProfessorRating = 0;

    private static ProfileCache profileCache = null;
    public int someValueIWantToKeep;

    protected ProfileCache(){}

    public static synchronized ProfileCache getInstance(){
        if(null == profileCache){
            profileCache = new ProfileCache();
        }
        return profileCache;
    }

    public static synchronized void resetInstance(){
        if(null != profileCache){
            profileCache = null;
        }
    }

    public ProfileRating getProfileRating() {
        return profileRating;
    }

    public void setProfileRating(ProfileRating profileRating) {
        this.profileRating = profileRating;
    }

    public BranchProfessorDiscipline getBranchProfessorDiscipline() {
        return branchProfessorDiscipline;
    }

    public void setBranchProfessorDiscipline(BranchProfessorDiscipline branchProfessorDiscipline) {
        this.branchProfessorDiscipline = branchProfessorDiscipline;
    }

    public AuthUser getAuthUser() {
        return authUser;
    }

    public void setAuthUser(AuthUser authUser) {
        this.authUser = authUser;
    }

    public IndividualRating getIndividualRating() {
        return individualRating;
    }

    public void setIndividualRating(IndividualRating individualRating) {
        this.individualRating = individualRating;
    }

    public ArrayList<StudentProfessorDiscipline> getStudentProfessorDisciplines() {
        return studentProfessorDisciplines;
    }

    public void setStudentProfessorDisciplines(ArrayList<StudentProfessorDiscipline> studentProfessorDisciplines) {
        this.studentProfessorDisciplines = studentProfessorDisciplines;
    }

    public ArrayList<Discipline> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(ArrayList<Discipline> disciplines) {
        this.disciplines = disciplines;
    }

    public ArrayList<DisciplineBranch> getDisciplineBranches() {
        return disciplineBranches;
    }

    public void setDisciplineBranches(ArrayList<DisciplineBranch> disciplineBranches) {
        this.disciplineBranches = disciplineBranches;
    }

    public ArrayList<IndividualRating> getAverageClassTopStudents() {
        return averageClassTopStudents;
    }

    public void setAverageClassTopStudents(ArrayList<IndividualRating> averageClassTopStudents) {
        this.averageClassTopStudents = averageClassTopStudents;
    }

    public ArrayList<IndividualRating> getOverallTopStudents() {
        return overallTopStudents;
    }

    public void setOverallTopStudents(ArrayList<IndividualRating> overallTopStudents) {
        this.overallTopStudents = overallTopStudents;
    }

    public ArrayList<IndividualRating> getOlympiadsTopStudents() {
        return olympiadsTopStudents;
    }

    public void setOlympiadsTopStudents(ArrayList<IndividualRating> olympiadsTopStudents) {
        this.olympiadsTopStudents = olympiadsTopStudents;
    }

    public RatingType getRatingType() {
        return ratingType;
    }

    public void setRatingType(RatingType ratingType) {
        this.ratingType = ratingType;
    }

    public int getTopCount() {
        return topCount;
    }

    public void setTopCount(int topCount) {
        this.topCount = topCount;
    }

    public SearchModel getSearchModel() {
        return searchModel;
    }

    public void setSearchModel(SearchModel searchModel) {
        this.searchModel = searchModel;
    }

    public ArrayList<UserProfile> getSearchUserResult() {
        return searchUserResult;
    }

    public void setSearchUserResult(ArrayList<UserProfile> searchUserResult) {
        this.searchUserResult = searchUserResult;
    }

    public int getTakeCount() {
        return takeCount;
    }

    public void setTakeCount(int takeCount) {
        this.takeCount = takeCount;
    }

    public ArrayList<String> getStudentRatingTypes() {
        return studentRatingTypes;
    }

    public ArrayList<ConcurentResult> getConcurentResults() {
        return concurentResults;
    }

    public void setConcurentResults(ArrayList<ConcurentResult> concurentResults) {
        this.concurentResults = concurentResults;
    }

    public ArrayList<ProfessorDiscipline> getProfessorDisciplinesProfile() {
        return professorDisciplinesProfile;
    }

    public void setProfessorDisciplinesProfile(ArrayList<ProfessorDiscipline> professorDisciplinesProfile) {
        this.professorDisciplinesProfile = professorDisciplinesProfile;
    }

    public ArrayList<Discipline> getSciDisciplinesProfile() {
        return sciDisciplinesProfile;
    }

    public void setSciDisciplinesProfile(ArrayList<Discipline> sciDisciplinesProfile) {
        this.sciDisciplinesProfile = sciDisciplinesProfile;
    }

    public ArrayList<Discipline> getSportDisciplinesProfile() {
        return sportDisciplinesProfile;
    }

    public void setSportDisciplinesProfile(ArrayList<Discipline> sportDisciplinesProfile) {
        this.sportDisciplinesProfile = sportDisciplinesProfile;
    }

    public ArrayList<Discipline> getArtDisciplinesProfile() {
        return artDisciplinesProfile;
    }

    public void setArtDisciplinesProfile(ArrayList<Discipline> artDisciplinesProfile) {
        this.artDisciplinesProfile = artDisciplinesProfile;
    }

    public ArrayList<Discipline> getSocDisciplinesProfile() {
        return socDisciplinesProfile;
    }

    public void setSocDisciplinesProfile(ArrayList<Discipline> socDisciplinesProfile) {
        this.socDisciplinesProfile = socDisciplinesProfile;
    }

    public double getAverageProfessorRating() {
        return averageProfessorRating;
    }

    public void setAverageProfessorRating(double averageProfessorRating) {
        this.averageProfessorRating = averageProfessorRating;
    }

    public ArrayList<Rating> getProfessorRatings() {
        return professorRatings;
    }

    public void setProfessorRatings(ArrayList<Rating> professorRatings) {
        this.professorRatings = professorRatings;
    }

    public int getProfessorRatingTakeCount() {
        return professorRatingTakeCount;
    }

    public void setProfessorRatingTakeCount(int professorRatingTakeCount) {
        this.professorRatingTakeCount = professorRatingTakeCount;
    }

    public UserProfile getShowingUser() {
        return showingUser;
    }

    public void setShowingUser(UserProfile showingUser) {
        this.showingUser = showingUser;
    }

    public ProfileRating getShowingProfileRating() {
        return showingProfileRating;
    }

    public void setShowingProfileRating(ProfileRating showingProfileRating) {
        this.showingProfileRating = showingProfileRating;
    }

    public double getAverageShowingProfessorRating() {
        return averageShowingProfessorRating;
    }

    public void setAverageShowingProfessorRating(double averageShowingProfessorRating) {
        this.averageShowingProfessorRating = averageShowingProfessorRating;
    }

    public ConcurentModel getConcurentModel() {
        return concurentModel;
    }

    public void setConcurentModel(ConcurentModel concurentModel) {
        this.concurentModel = concurentModel;
    }
}