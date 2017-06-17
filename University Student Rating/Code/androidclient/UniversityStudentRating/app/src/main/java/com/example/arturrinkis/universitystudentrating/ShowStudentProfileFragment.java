package com.example.arturrinkis.universitystudentrating;


import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.example.arturrinkis.universitystudentrating.CacheData.ProfileCache;
import com.example.arturrinkis.universitystudentrating.CustomControls.SquareImageView;
import com.example.arturrinkis.universitystudentrating.DTO.ProfileRating;
import com.example.arturrinkis.universitystudentrating.DTO.UserProfile;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.IServerAPIManager;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.ServerAPIManager;

public class ShowStudentProfileFragment extends Fragment {
    private String SHOW_PROFILE_FRAGMENT_TAG = "SHOW_PROFILE_FRAGMENT_TAG";
    private View view = null;

    private IServerAPIManager serverAPIManager = null;

    private GetShowingUserAsyncTask getShowingUserAsyncTask = null;
    private GetShowingProfileRatingAsyncTask getShowingProfileRatingAsyncTask = null;
    private int showingUserId;

    public ShowStudentProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        view = inflater.inflate(R.layout.fragment_show_profile, container, false);
        serverAPIManager = new ServerAPIManager(getActivity().getApplicationContext());

        showingUserId = getArguments().getInt("ShowingUserId");

        initMainHeaderTextView();
        hideMainContent();
        showLoadingAnimation();

        if(savedInstanceState == null || ProfileCache.getInstance().getShowingUser() == null || ProfileCache.getInstance().getShowingProfileRating() == null)
        {
            getShowingUserAsyncTask = new GetShowingUserAsyncTask();
            getShowingUserAsyncTask.execute();
        }
        else {
            initControls();
        }

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cancelGetAuthUserAsyncTask();
        cancelgetShowingProfileRatingAsyncTask();
    }

    private void initControls(){
        try {
            initRatingBarControls();
            initAuthUserControls();
            hideLoadingAnimation();
            showMainContent();
            showAppBarLayoutControl();
            showContainer();
        }
        catch(NullPointerException e){
            Log.e("asynch task", "Activity has been destroyed");
        }
    }

    private void initMainHeaderTextView(){
        TextView main_header_tv = (TextView)getActivity().findViewById(R.id.main_header_tv);
        main_header_tv.setText("Profile");
    }

    private void initRatingBarControls(){
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar_indicator);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(1).setColorFilter(Color.rgb(0,131,126), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(2).setColorFilter(Color.rgb(255,170,109), PorterDuff.Mode.SRC_ATOP);
        ratingBar.setStepSize(0.01f);
    }

    private void initAuthUserControls(){
        SquareImageView userphoto_image = (SquareImageView)view.findViewById(R.id.userphoto_image);
        userphoto_image.setImageBitmap(ProfileCache.getInstance().getShowingUser().getPhotoPathBitmap());

        TextView ratingBarHeader_tv = (TextView)view.findViewById(R.id.ratingBarHeader_tv);
        ratingBarHeader_tv.setText(Double.toString(ProfileCache.getInstance().getShowingProfileRating().getAverageClassRating()));

        RatingBar ratingBar_indicator = (RatingBar)view.findViewById(R.id.ratingBar_indicator);
        ratingBar_indicator.setRating((float) ProfileCache.getInstance().getShowingProfileRating().getAverageClassRating()/2);

        TextView ratingBarTotal_tv = (TextView)view.findViewById(R.id.ratingBarTotal_tv);
        ratingBarTotal_tv.setText(Integer.toString(ProfileCache.getInstance().getShowingProfileRating().getTotalClassRating()) + " total");

        initOlympiadsRating();
        initTotalScienceRating();
        initTotalSportRating();
        initTotalArtRating();
        initTotalSocietyRating();
        
        TextView profile_status_tv = (TextView)view.findViewById(R.id.profile_status_tv);
        profile_status_tv.setText(ProfileCache.getInstance().getShowingUser().getStatus().getName());

        TextView profile_gender_tv = (TextView)view.findViewById(R.id.profile_gender_tv);
        profile_gender_tv.setText(ProfileCache.getInstance().getShowingUser().getGenderType().getName());

        TextView profile_firstname_tv = (TextView)view.findViewById(R.id.profile_firstname_tv);
        profile_firstname_tv.setText(ProfileCache.getInstance().getShowingUser().getFirstName());

        TextView profile_surname_tv = (TextView)view.findViewById(R.id.profile_surname_tv);
        profile_surname_tv.setText(ProfileCache.getInstance().getShowingUser().getLastName());

        TextView profile_country_tv = (TextView)view.findViewById(R.id.profile_country_tv);
        profile_country_tv.setText(ProfileCache.getInstance().getShowingUser().getCountry().getName());

        TextView profile_city_tv = (TextView)view.findViewById(R.id.profile_city_tv);
        profile_city_tv.setText(ProfileCache.getInstance().getShowingUser().getCity().getName());

        TextView profile_university_tv = (TextView)view.findViewById(R.id.profile_university_tv);
        profile_university_tv.setText(ProfileCache.getInstance().getShowingUser().getUniversity().getName());

        TextView profile_faculty_tv = (TextView)view.findViewById(R.id.profile_faculty_tv);
        profile_faculty_tv.setText(ProfileCache.getInstance().getShowingUser().getFaculty().getName());

        TextView profile_course_tv = (TextView)view.findViewById(R.id.profile_course_tv);
        profile_course_tv.setText(Integer.toString(ProfileCache.getInstance().getShowingUser().getCourse().getNumber()));
    }

    private void initOlympiadsRating(){
        RoundCornerProgressBar total_olympiads_rating_pb = (RoundCornerProgressBar)view.findViewById(R.id.total_olympiads_rating_pb);

        total_olympiads_rating_pb.setProgress((float) ProfileCache.getInstance().getShowingProfileRating().getTotalOlympiadsRating());
        total_olympiads_rating_pb.setMax(ProfileCache.getInstance().getShowingProfileRating().getMaxOlympiadsRating());

        TextView total_olympiads_rating_tv = (TextView)view.findViewById(R.id.total_olympiads_rating_tv);
        total_olympiads_rating_tv.setText(Integer.toString(ProfileCache.getInstance().getShowingProfileRating().getTotalOlympiadsRating()) + "/" + Integer.toString(ProfileCache.getInstance().getShowingProfileRating().getMaxOlympiadsRating()));
    }

    private void initTotalScienceRating(){
        RoundCornerProgressBar total_science_rating_pb = (RoundCornerProgressBar)view.findViewById(R.id.total_science_rating_pb);

        total_science_rating_pb.setMax(ProfileCache.getInstance().getShowingProfileRating().getMaxScientificRating());
        total_science_rating_pb.setProgress(ProfileCache.getInstance().getShowingProfileRating().getTotalScientificRating());

        TextView total_science_rating_tv = (TextView)view.findViewById(R.id.total_science_rating_tv);
        total_science_rating_tv.setText(Integer.toString(ProfileCache.getInstance().getShowingProfileRating().getTotalScientificRating()) + "/" + Integer.toString(ProfileCache.getInstance().getShowingProfileRating().getMaxScientificRating()));
    }

    private void initTotalSportRating(){
        RoundCornerProgressBar total_sport_rating_pb = (RoundCornerProgressBar)view.findViewById(R.id.total_sport_rating_pb);

        total_sport_rating_pb.setMax(ProfileCache.getInstance().getShowingProfileRating().getMaxSportRating());
        total_sport_rating_pb.setProgress(ProfileCache.getInstance().getShowingProfileRating().getTotalSportRating());

        TextView total_sport_rating_tv = (TextView)view.findViewById(R.id.total_sport_rating_tv);
        total_sport_rating_tv.setText(Integer.toString(ProfileCache.getInstance().getShowingProfileRating().getTotalSportRating()) + "/" + Integer.toString(ProfileCache.getInstance().getShowingProfileRating().getMaxSportRating()));
    }

    private void initTotalArtRating(){
        RoundCornerProgressBar total_art_rating_pb = (RoundCornerProgressBar)view.findViewById(R.id.total_art_rating_pb);

        total_art_rating_pb.setMax(ProfileCache.getInstance().getShowingProfileRating().getMaxArtRating());
        total_art_rating_pb.setProgress((float) ProfileCache.getInstance().getShowingProfileRating().getTotalArtRating());

        TextView total_art_rating_tv = (TextView)view.findViewById(R.id.total_art_rating_tv);
        total_art_rating_tv.setText(Integer.toString(ProfileCache.getInstance().getShowingProfileRating().getTotalArtRating()) + "/" + Integer.toString(ProfileCache.getInstance().getShowingProfileRating().getMaxArtRating()));
    }

    private void initTotalSocietyRating(){
        RoundCornerProgressBar total_society_rating_pb = (RoundCornerProgressBar)view.findViewById(R.id.total_society_rating_pb);

        total_society_rating_pb.setMax(ProfileCache.getInstance().getShowingProfileRating().getMaxSocietyRating());
        total_society_rating_pb.setProgress((float) ProfileCache.getInstance().getShowingProfileRating().getTotalSocietyRating());

        TextView total_society_rating_tv = (TextView)view.findViewById(R.id.total_society_rating_tv);
        total_society_rating_tv.setText(Integer.toString(ProfileCache.getInstance().getShowingProfileRating().getTotalSocietyRating()) + "/" + Integer.toString(ProfileCache.getInstance().getShowingProfileRating().getMaxSocietyRating()));
    }

    private void showLoadingAnimation() {
        RelativeLayout loading_layout = (RelativeLayout) getActivity().findViewById(R.id.loading_layout);
        loading_layout.setVisibility(View.VISIBLE);
    }

    private void hideMainContent() {
        LinearLayout content_layout = (LinearLayout) view.findViewById(R.id.content_layout);
        content_layout.setVisibility(View.GONE);
    }

    private void hideLoadingAnimation() {
        RelativeLayout loading_layout = (RelativeLayout) getActivity().findViewById(R.id.loading_layout);
        loading_layout.setVisibility(View.GONE);
    }

    private void showMainContent(){
        LinearLayout content_layout = (LinearLayout) view.findViewById(R.id.content_layout);
        content_layout.setVisibility(View.VISIBLE);
    }

    private void showAppBarLayoutControl(){
        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.app_bar_layout);
        appBarLayout.setVisibility(View.VISIBLE);
    }

    private void showContainer(){
        FrameLayout frameLayout = (FrameLayout) getActivity().findViewById(R.id.container);
        frameLayout.setVisibility(View.VISIBLE);
    }

    //
    //
    //database async tasks

    class GetShowingUserAsyncTask extends AsyncTask<Void, Void, UserProfile> {
        @Override
        protected UserProfile doInBackground(Void... params) {
            UserProfile userProfile = serverAPIManager.getUserService().getUserProfileById(showingUserId);
            ProfileCache.getInstance().setShowingUser(userProfile);

            return null;
        }
        protected void onPostExecute(UserProfile userProfile){
            getShowingProfileRatingAsyncTask = new GetShowingProfileRatingAsyncTask();
            getShowingProfileRatingAsyncTask.execute();
        }
    }

    class GetShowingProfileRatingAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            UserProfile showingUser = ProfileCache.getInstance().getShowingUser();
            ProfileRating profileRating = serverAPIManager.getStatisticService().getProfileRating(showingUser.getId(), showingUser.getCourseId());
            ProfileCache.getInstance().setShowingProfileRating(profileRating);

            return null;
        }
        protected void onPostExecute(Void result){
            initControls();
        }
    }

    private void cancelGetAuthUserAsyncTask() {
        if (getShowingUserAsyncTask == null) return;
        getShowingUserAsyncTask.cancel(false);
    }

    private void cancelgetShowingProfileRatingAsyncTask() {
        if (getShowingProfileRatingAsyncTask == null) return;
        getShowingProfileRatingAsyncTask.cancel(false);
    }

}