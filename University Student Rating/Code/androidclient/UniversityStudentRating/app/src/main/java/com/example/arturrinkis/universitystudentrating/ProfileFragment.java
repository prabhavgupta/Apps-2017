package com.example.arturrinkis.universitystudentrating;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.example.arturrinkis.universitystudentrating.CacheData.ProfileCache;
import com.example.arturrinkis.universitystudentrating.CustomControls.Adapters.ProfessorDisciplineAdapter;
import com.example.arturrinkis.universitystudentrating.CustomControls.NonScrollListView;
import com.example.arturrinkis.universitystudentrating.CustomControls.SquareImageView;
import com.example.arturrinkis.universitystudentrating.DTO.AuthUser;
import com.example.arturrinkis.universitystudentrating.DTO.BranchProfessorDiscipline;
import com.example.arturrinkis.universitystudentrating.DTO.ProfessorDiscipline;
import com.example.arturrinkis.universitystudentrating.DTO.ProfileRating;
import com.example.arturrinkis.universitystudentrating.DTO.SaveStudentProfessorDistModel;
import com.example.arturrinkis.universitystudentrating.DTO.StudentProfessorDiscipline;
import com.example.arturrinkis.universitystudentrating.DTO.UserProfile;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.IServerAPIManager;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.ServerAPIManager;
import com.example.arturrinkis.universitystudentrating.Utilities.DataUtility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {
    private String PROFILE_FRAGMENT_TAG = "PROFILE_FRAGMENT_TAG";
    private View view = null;

    private ProfessorDisciplineAdapter sciProfessorDisciplineAdapter = null;
    private ProfessorDisciplineAdapter societyProfessorDisciplineAdapter = null;
    private ProfessorDisciplineAdapter artProfessorDisciplineAdapter = null;
    private ProfessorDisciplineAdapter sportProfessorDisciplineAdapter = null;
    private IServerAPIManager serverAPIManager = null;
    private DataUtility dataUtility = null;
    private ProgressDialog uploadPhotoImagePd = null;

    private GetAuthUserAsyncTask getAuthUserAsyncTask = null;
    private GetProfileRatingAsyncTask getProfileRatingAsyncTask = null;
    private SaveStudentProfessorDisciplinesAsyncTask saveStudentProfessorDisciplinesAsyncTask = null;
    private GetProfessorDisciplinesAsyncTask getProfessorDisciplinesAsyncTask = null;
    private GetStudentProfessorDisciplinesAsyncTask getStudentProfessorDisciplinesAsyncTask = null;
    private UploadImageAsyncTask uploadImageAsyncTask = null;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        serverAPIManager = new ServerAPIManager(getActivity().getApplicationContext());
        dataUtility = new DataUtility();

        uploadPhotoImagePd = new ProgressDialog(getActivity());
        uploadPhotoImagePd.setTitle("Uploading");
        uploadPhotoImagePd.setMessage("Uploading to the server...");

        initMainHeaderTextView();
        hideMainContent();
        showLoadingAnimation();

        if(ProfileCache.getInstance().getBranchProfessorDiscipline() == null || ProfileCache.getInstance().getStudentProfessorDisciplines() == null || ProfileCache.getInstance().getAuthUser() == null || ProfileCache.getInstance().getProfileRating() == null)
        {
            getAuthUserAsyncTask = new GetAuthUserAsyncTask();
            getAuthUserAsyncTask.execute();
        }
        else {
            initControls();
        }

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cancelSaveStudentProfessorDisciplinesAsyncTask();
        cancelGetAuthUserAsyncTask();
        cancelGetProfileRatingAsyncTask();
        cancelGetProfessorDisciplinesAsyncTask();
        cancelGetStudentProfessorDisciplinesAsyncTask();
        cancelUploadImageAsyncTask();
        ProfileCache.getInstance().setProfileRating(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);;

                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        final Bitmap croppedBmp = dataUtility.cropBitmap(selectedImage);
                        ByteArrayOutputStream outBitmapStream = new ByteArrayOutputStream();
                        croppedBmp.compress(Bitmap.CompressFormat.JPEG, 50, outBitmapStream);
                        ByteArrayInputStream outBitmapInStream = new ByteArrayInputStream(outBitmapStream.toByteArray());
                        int size = outBitmapInStream.available();
                        final Bitmap compressedBitmap = BitmapFactory.decodeStream(outBitmapInStream);
                        SquareImageView userphoto_image = (SquareImageView) view.findViewById(R.id.userphoto_image);
                        userphoto_image.setImageBitmap(compressedBitmap);
                        ProfileCache.getInstance().getAuthUser().getUserProfile().setPhotoPathBitmap(compressedBitmap);
                        uploadPhotoImagePd.show();
                        uploadImageAsyncTask = new UploadImageAsyncTask();
                        uploadImageAsyncTask.execute(compressedBitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
        }
    }

    private void initControls(){
        try {
            initRatingBarControls();
            initAuthUserControls();
            initSelectProfessorsControls();
            initProfessorDisciplinesControls();
            initSaveProfessorsButton();
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

    private void initSelectProfessorsControls(){
        LinearLayout scientific_professors_layout = (LinearLayout)view.findViewById(R.id.scientific_professors_layout);
        scientific_professors_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView scientific_professors_lv = (ListView)view.findViewById(R.id.scientific_professors_lv);
                if(scientific_professors_lv.getVisibility() == View.GONE){
                    scientific_professors_lv.setVisibility(View.VISIBLE);
                }
                else{
                    scientific_professors_lv.setVisibility(View.GONE);
                }
            }
        });
        LinearLayout sport_professors_layout = (LinearLayout)view.findViewById(R.id.sport_professors_layout);
        sport_professors_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView sport_professors_lv = (ListView)view.findViewById(R.id.sport_professors_lv);
                if(sport_professors_lv.getVisibility() == View.GONE){
                    sport_professors_lv.setVisibility(View.VISIBLE);
                }
                else{
                    sport_professors_lv.setVisibility(View.GONE);
                }
            }
        });
        LinearLayout art_professors_layout = (LinearLayout)view.findViewById(R.id.art_professors_layout);
        art_professors_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView art_professors_lv = (ListView)view.findViewById(R.id.art_professors_lv);
                if(art_professors_lv.getVisibility() == View.GONE){
                    art_professors_lv.setVisibility(View.VISIBLE);
                }
                else{
                    art_professors_lv.setVisibility(View.GONE);
                }
            }
        });
        LinearLayout society_professors_layout = (LinearLayout)view.findViewById(R.id.society_professors_layout);
        society_professors_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListView society_professors_lv = (ListView)view.findViewById(R.id.society_professors_lv);
                if(society_professors_lv.getVisibility() == View.GONE){
                    society_professors_lv.setVisibility(View.VISIBLE);
                }
                else{
                    society_professors_lv.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initAuthUserControls(){
        SquareImageView userphoto_image = (SquareImageView)view.findViewById(R.id.userphoto_image);
        userphoto_image.setImageBitmap(ProfileCache.getInstance().getAuthUser().getUserProfile().getPhotoPathBitmap());

        userphoto_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        TextView ratingBarHeader_tv = (TextView)view.findViewById(R.id.ratingBarHeader_tv);
        ratingBarHeader_tv.setText(Double.toString(ProfileCache.getInstance().getProfileRating().getAverageClassRating()));

        RatingBar ratingBar_indicator = (RatingBar)view.findViewById(R.id.ratingBar_indicator);
        ratingBar_indicator.setRating((float) ProfileCache.getInstance().getProfileRating().getAverageClassRating()/2);

        TextView ratingBarTotal_tv = (TextView)view.findViewById(R.id.ratingBarTotal_tv);
        ratingBarTotal_tv.setText(Integer.toString(ProfileCache.getInstance().getProfileRating().getTotalClassRating()) + " total");

        initOlympiadsRating();
        initTotalScienceRating();
        initTotalSportRating();
        initTotalArtRating();
        initTotalSocietyRating();

        TextView profile_username_tv = (TextView)view.findViewById(R.id.profile_username_tv);
        profile_username_tv.setText(ProfileCache.getInstance().getAuthUser().getUserName());

        TextView profile_status_tv = (TextView)view.findViewById(R.id.profile_status_tv);
        profile_status_tv.setText(ProfileCache.getInstance().getAuthUser().getUserProfile().getStatus().getName());

        TextView profile_gender_tv = (TextView)view.findViewById(R.id.profile_gender_tv);
        profile_gender_tv.setText(ProfileCache.getInstance().getAuthUser().getUserProfile().getGenderType().getName());

        TextView profile_email_tv = (TextView)view.findViewById(R.id.profile_email_tv);
        profile_email_tv.setText(ProfileCache.getInstance().getAuthUser().getEmail());

        TextView profile_firstname_tv = (TextView)view.findViewById(R.id.profile_firstname_tv);
        profile_firstname_tv.setText(ProfileCache.getInstance().getAuthUser().getUserProfile().getFirstName());

        TextView profile_surname_tv = (TextView)view.findViewById(R.id.profile_surname_tv);
        profile_surname_tv.setText(ProfileCache.getInstance().getAuthUser().getUserProfile().getLastName());

        TextView profile_country_tv = (TextView)view.findViewById(R.id.profile_country_tv);
        profile_country_tv.setText(ProfileCache.getInstance().getAuthUser().getUserProfile().getCountry().getName());

        TextView profile_city_tv = (TextView)view.findViewById(R.id.profile_city_tv);
        profile_city_tv.setText(ProfileCache.getInstance().getAuthUser().getUserProfile().getCity().getName());

        TextView profile_university_tv = (TextView)view.findViewById(R.id.profile_university_tv);
        profile_university_tv.setText(ProfileCache.getInstance().getAuthUser().getUserProfile().getUniversity().getName());

        TextView profile_faculty_tv = (TextView)view.findViewById(R.id.profile_faculty_tv);
        profile_faculty_tv.setText(ProfileCache.getInstance().getAuthUser().getUserProfile().getFaculty().getName());

        TextView profile_course_tv = (TextView)view.findViewById(R.id.profile_course_tv);
        profile_course_tv.setText(Integer.toString(ProfileCache.getInstance().getAuthUser().getUserProfile().getCourse().getNumber()));
    }

    private void initOlympiadsRating(){
        RoundCornerProgressBar total_olympiads_rating_pb = (RoundCornerProgressBar)view.findViewById(R.id.total_olympiads_rating_pb);

        total_olympiads_rating_pb.setProgress((float) ProfileCache.getInstance().getProfileRating().getTotalOlympiadsRating());
        total_olympiads_rating_pb.setMax(ProfileCache.getInstance().getProfileRating().getMaxOlympiadsRating());

        TextView total_olympiads_rating_tv = (TextView)view.findViewById(R.id.total_olympiads_rating_tv);
        total_olympiads_rating_tv.setText(Integer.toString(ProfileCache.getInstance().getProfileRating().getTotalOlympiadsRating()) + "/" + Integer.toString(ProfileCache.getInstance().getProfileRating().getMaxOlympiadsRating()));
    }

    private void initTotalScienceRating(){
        RoundCornerProgressBar total_science_rating_pb = (RoundCornerProgressBar)view.findViewById(R.id.total_science_rating_pb);

        total_science_rating_pb.setMax(ProfileCache.getInstance().getProfileRating().getMaxScientificRating());
        total_science_rating_pb.setProgress(ProfileCache.getInstance().getProfileRating().getTotalScientificRating());

        TextView total_science_rating_tv = (TextView)view.findViewById(R.id.total_science_rating_tv);
        total_science_rating_tv.setText(Integer.toString(ProfileCache.getInstance().getProfileRating().getTotalScientificRating()) + "/" + Integer.toString(ProfileCache.getInstance().getProfileRating().getMaxScientificRating()));
    }

    private void initTotalSportRating(){
        RoundCornerProgressBar total_sport_rating_pb = (RoundCornerProgressBar)view.findViewById(R.id.total_sport_rating_pb);

        total_sport_rating_pb.setMax(ProfileCache.getInstance().getProfileRating().getMaxSportRating());
        total_sport_rating_pb.setProgress(ProfileCache.getInstance().getProfileRating().getTotalSportRating());

        TextView total_sport_rating_tv = (TextView)view.findViewById(R.id.total_sport_rating_tv);
        total_sport_rating_tv.setText(Integer.toString(ProfileCache.getInstance().getProfileRating().getTotalSportRating()) + "/" + Integer.toString(ProfileCache.getInstance().getProfileRating().getMaxSportRating()));
    }

    private void initTotalArtRating(){
        RoundCornerProgressBar total_art_rating_pb = (RoundCornerProgressBar)view.findViewById(R.id.total_art_rating_pb);

        total_art_rating_pb.setMax(ProfileCache.getInstance().getProfileRating().getMaxArtRating());
        total_art_rating_pb.setProgress((float) ProfileCache.getInstance().getProfileRating().getTotalArtRating());

        TextView total_art_rating_tv = (TextView)view.findViewById(R.id.total_art_rating_tv);
        total_art_rating_tv.setText(Integer.toString(ProfileCache.getInstance().getProfileRating().getTotalArtRating()) + "/" + Integer.toString(ProfileCache.getInstance().getProfileRating().getMaxArtRating()));
    }

    private void initTotalSocietyRating(){
        RoundCornerProgressBar total_society_rating_pb = (RoundCornerProgressBar)view.findViewById(R.id.total_society_rating_pb);

        total_society_rating_pb.setMax(ProfileCache.getInstance().getProfileRating().getMaxSocietyRating());
        total_society_rating_pb.setProgress((float) ProfileCache.getInstance().getProfileRating().getTotalSocietyRating());

        TextView total_society_rating_tv = (TextView)view.findViewById(R.id.total_society_rating_tv);
        total_society_rating_tv.setText(Integer.toString(ProfileCache.getInstance().getProfileRating().getTotalSocietyRating()) + "/" + Integer.toString(ProfileCache.getInstance().getProfileRating().getMaxSocietyRating()));
    }

    private void initProfessorDisciplinesControls(){
        sciProfessorDisciplineAdapter = new ProfessorDisciplineAdapter(view.getContext(), ProfileCache.getInstance().getBranchProfessorDiscipline().getSciProfessorDisciplines(), ProfileCache.getInstance().getStudentProfessorDisciplines());
        societyProfessorDisciplineAdapter = new ProfessorDisciplineAdapter(view.getContext(), ProfileCache.getInstance().getBranchProfessorDiscipline().getSocietyProfessorDisciplines(), ProfileCache.getInstance().getStudentProfessorDisciplines());
        artProfessorDisciplineAdapter = new ProfessorDisciplineAdapter(view.getContext(), ProfileCache.getInstance().getBranchProfessorDiscipline().getArtProfessorDisciplines(), ProfileCache.getInstance().getStudentProfessorDisciplines());
        sportProfessorDisciplineAdapter = new ProfessorDisciplineAdapter(view.getContext(), ProfileCache.getInstance().getBranchProfessorDiscipline().getSportProfessorDisciplines(), ProfileCache.getInstance().getStudentProfessorDisciplines());

        NonScrollListView scientific_professors_lv = (NonScrollListView) view.findViewById(R.id.scientific_professors_lv);
        scientific_professors_lv.setAdapter(sciProfessorDisciplineAdapter);

        scientific_professors_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserProfile profile = ((ProfessorDiscipline)sciProfessorDisciplineAdapter.getItem(position)).getUserProfile();
                if(profile.getId() != ProfileCache.getInstance().getAuthUser().getUserProfileId()) {
                    Fragment showProfileFragment = profile.getStatusId() == 1 ? new ShowStudentProfileFragment() : new ShowProfessorProfileFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("ShowingUserId", profile.getId());
                    showProfileFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();

                    fragmentTransaction.replace(R.id.tab_container, showProfileFragment);
                    fragmentTransaction.addToBackStack(PROFILE_FRAGMENT_TAG);
                    fragmentTransaction.commit();
                }
            }
        });

        NonScrollListView sport_professors_lv = (NonScrollListView) view.findViewById(R.id.sport_professors_lv);
        sport_professors_lv.setAdapter(sportProfessorDisciplineAdapter);

        sport_professors_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserProfile profile = ((ProfessorDiscipline)sportProfessorDisciplineAdapter.getItem(position)).getUserProfile();
                if(profile.getId() != ProfileCache.getInstance().getAuthUser().getUserProfileId()) {
                    Fragment showProfileFragment = profile.getStatusId() == 1 ? new ShowStudentProfileFragment() : new ShowProfessorProfileFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("ShowingUserId", profile.getId());
                    showProfileFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();

                    fragmentTransaction.replace(R.id.tab_container, showProfileFragment);
                    fragmentTransaction.addToBackStack(PROFILE_FRAGMENT_TAG);
                    fragmentTransaction.commit();
                }
            }
        });

        NonScrollListView art_professors_lv = (NonScrollListView) view.findViewById(R.id.art_professors_lv);
        art_professors_lv.setAdapter(artProfessorDisciplineAdapter);

        art_professors_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserProfile profile = ((ProfessorDiscipline)artProfessorDisciplineAdapter.getItem(position)).getUserProfile();
                if(profile.getId() != ProfileCache.getInstance().getAuthUser().getUserProfileId()) {
                    Fragment showProfileFragment = profile.getStatusId() == 1 ? new ShowStudentProfileFragment() : new ShowProfessorProfileFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("ShowingUserId", profile.getId());
                    showProfileFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();

                    fragmentTransaction.replace(R.id.tab_container, showProfileFragment);
                    fragmentTransaction.addToBackStack(PROFILE_FRAGMENT_TAG);
                    fragmentTransaction.commit();
                }
            }
        });

        NonScrollListView society_professors_lv = (NonScrollListView) view.findViewById(R.id.society_professors_lv);
        society_professors_lv.setAdapter(societyProfessorDisciplineAdapter);

        society_professors_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserProfile profile = ((ProfessorDiscipline)societyProfessorDisciplineAdapter.getItem(position)).getUserProfile();
                if(profile.getId() != ProfileCache.getInstance().getAuthUser().getUserProfileId()) {
                    Fragment showProfileFragment = profile.getStatusId() == 1 ? new ShowStudentProfileFragment() : new ShowProfessorProfileFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("ShowingUserId", profile.getId());
                    showProfileFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();

                    fragmentTransaction.replace(R.id.tab_container, showProfileFragment);
                    fragmentTransaction.addToBackStack(PROFILE_FRAGMENT_TAG);
                    fragmentTransaction.commit();
                }
            }
        });
    }

    private void initSaveProfessorsButton(){
        Button save_professors_btn = (Button)view.findViewById(R.id.save_professors_btn);
        save_professors_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveStudentProfessorDistModel model = new SaveStudentProfessorDistModel();
                model.setStudentId(ProfileCache.getInstance().getAuthUser().getUserProfileId());
                ArrayList<StudentProfessorDiscipline> studentProfessorDisciplines = new ArrayList<StudentProfessorDiscipline>();

                for(int i = 0; i < sciProfessorDisciplineAdapter.getCount(); ++i){
                    ProfessorDiscipline professorDiscipline = (ProfessorDiscipline)sciProfessorDisciplineAdapter.getItem(i);
                    if(sciProfessorDisciplineAdapter.checkedProfessorDisciplines.get(i) == true){
                        studentProfessorDisciplines.add(new StudentProfessorDiscipline(0, ProfileCache.getInstance().getAuthUser().getUserProfileId(), professorDiscipline.getId(), null));
                    }
                }

                for(int i = 0; i < sportProfessorDisciplineAdapter.getCount(); ++i){
                    ProfessorDiscipline professorDiscipline = (ProfessorDiscipline)sportProfessorDisciplineAdapter.getItem(i);
                    if(sportProfessorDisciplineAdapter.checkedProfessorDisciplines.get(i) == true){
                        studentProfessorDisciplines.add(new StudentProfessorDiscipline(0, ProfileCache.getInstance().getAuthUser().getUserProfileId(), professorDiscipline.getId(), null));
                    }
                }

                for(int i = 0; i < societyProfessorDisciplineAdapter.getCount(); ++i){
                    ProfessorDiscipline professorDiscipline = (ProfessorDiscipline)societyProfessorDisciplineAdapter.getItem(i);
                    if(societyProfessorDisciplineAdapter.checkedProfessorDisciplines.get(i) == true){
                        studentProfessorDisciplines.add(new StudentProfessorDiscipline(0, ProfileCache.getInstance().getAuthUser().getUserProfileId(), professorDiscipline.getId(), null));
                    }
                }

                for(int i = 0; i < artProfessorDisciplineAdapter.getCount(); ++i){
                    ProfessorDiscipline professorDiscipline = (ProfessorDiscipline)artProfessorDisciplineAdapter.getItem(i);
                    if(artProfessorDisciplineAdapter.checkedProfessorDisciplines.get(i) == true){
                        studentProfessorDisciplines.add(new StudentProfessorDiscipline(0, ProfileCache.getInstance().getAuthUser().getUserProfileId(), professorDiscipline.getId(), null));
                    }
                }
                model.setStudentProfessorDisciplines(studentProfessorDisciplines);
                hideMainContent();
                showLoadingAnimation();

                ProfileCache.getInstance().setBranchProfessorDiscipline(null);
                ProfileCache.getInstance().setStudentProfessorDisciplines(null);

                saveStudentProfessorDisciplinesAsyncTask = new SaveStudentProfessorDisciplinesAsyncTask();
                saveStudentProfessorDisciplinesAsyncTask.execute(model);
            }
        });
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

    class SaveStudentProfessorDisciplinesAsyncTask extends AsyncTask<SaveStudentProfessorDistModel, Void, Boolean> {
        @Override
        protected Boolean doInBackground(SaveStudentProfessorDistModel... params) {
            return serverAPIManager.getUserService().saveStudentProfessorDisciplines(params[0]);
        }
        protected void onPostExecute(Boolean result) {
            getProfessorDisciplinesAsyncTask = new GetProfessorDisciplinesAsyncTask();
            getProfessorDisciplinesAsyncTask.execute(true);
        }
    }

    class UploadImageAsyncTask extends AsyncTask<Bitmap, Void, Void> {
        @Override
        protected Void doInBackground(Bitmap... params) {
            serverAPIManager.getDataServiceAPI().uploadPhotoImageFile(params[0], "UserPhotos/" + ProfileCache.getInstance().getAuthUser().getUserName());
            return null;
        }
        protected void onPostExecute(Void result){
            uploadPhotoImagePd.dismiss();
        }
    }


    class GetAuthUserAsyncTask extends AsyncTask<Void, Void, AuthUser> {
        @Override
        protected AuthUser doInBackground(Void... params) {
            if(ProfileCache.getInstance().getAuthUser() == null) {
                AuthUser authUser = serverAPIManager.getUserService().getAuthUser(true);
                ProfileCache.getInstance().setAuthUser(authUser);
            }
            return null;
        }
        protected void onPostExecute(AuthUser authUser){
            getProfileRatingAsyncTask = new GetProfileRatingAsyncTask();
            getProfileRatingAsyncTask.execute();
        }
    }

    class GetProfileRatingAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            if(ProfileCache.getInstance().getProfileRating() == null) {
                ProfileRating profileRating = serverAPIManager.getStatisticService().getProfileRating(ProfileCache.getInstance().getAuthUser().getUserProfileId(), ProfileCache.getInstance().getAuthUser().getUserProfile().getCourseId());
                ProfileCache.getInstance().setProfileRating(profileRating);
            }
            return null;
        }
        protected void onPostExecute(Void result){
            getProfessorDisciplinesAsyncTask = new GetProfessorDisciplinesAsyncTask();
            getProfessorDisciplinesAsyncTask.execute(false);
        }
    }

    class GetProfessorDisciplinesAsyncTask extends AsyncTask<Boolean, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Boolean... params) {
            int studentId = ProfileCache.getInstance().getAuthUser().getUserProfileId();
            int universityId = ProfileCache.getInstance().getAuthUser().getUserProfile().getUniversityId();
            if(ProfileCache.getInstance().getBranchProfessorDiscipline() == null || params[0].booleanValue() == true) {
                BranchProfessorDiscipline branchProfessorDiscipline = serverAPIManager.getDataServiceAPI().getBranchProfessorDiscipline(studentId, universityId);
                ProfileCache.getInstance().setBranchProfessorDiscipline(branchProfessorDiscipline);
            }
            return params[0].booleanValue();
        }
        protected void onPostExecute(Boolean result) {
            getStudentProfessorDisciplinesAsyncTask = new GetStudentProfessorDisciplinesAsyncTask();
            getStudentProfessorDisciplinesAsyncTask.execute(result);
        }
    }

    class GetStudentProfessorDisciplinesAsyncTask extends AsyncTask<Boolean, Void, Void> {
        @Override
        protected Void doInBackground(Boolean... params) {
            if(ProfileCache.getInstance().getStudentProfessorDisciplines() == null || params[0].booleanValue() == true) {
                ArrayList<StudentProfessorDiscipline> studentProfessorDisciplines = serverAPIManager.getDataServiceAPI().getStudentProfessorDisciplines(ProfileCache.getInstance().getAuthUser().getUserProfileId());
                ProfileCache.getInstance().setStudentProfessorDisciplines(studentProfessorDisciplines);
            }
            return null;
        }
        protected void onPostExecute(Void result) {
            initControls();
        }
    }


    private void cancelSaveStudentProfessorDisciplinesAsyncTask() {
        if (saveStudentProfessorDisciplinesAsyncTask == null) return;
        saveStudentProfessorDisciplinesAsyncTask.cancel(false);
    }

    private void cancelUploadImageAsyncTask() {
        if (uploadImageAsyncTask == null) return;
        uploadImageAsyncTask.cancel(false);
    }


    private void cancelGetAuthUserAsyncTask() {
        if (getAuthUserAsyncTask == null) return;
        getAuthUserAsyncTask.cancel(false);
    }

    private void cancelGetProfileRatingAsyncTask() {
        if (getProfileRatingAsyncTask == null) return;
        getProfileRatingAsyncTask.cancel(false);
    }

    private void cancelGetProfessorDisciplinesAsyncTask() {
        if (getProfessorDisciplinesAsyncTask == null) return;
        getProfessorDisciplinesAsyncTask.cancel(false);
    }

    private void cancelGetStudentProfessorDisciplinesAsyncTask() {
        if (getStudentProfessorDisciplinesAsyncTask == null) return;
        getStudentProfessorDisciplinesAsyncTask.cancel(false);
    }
}