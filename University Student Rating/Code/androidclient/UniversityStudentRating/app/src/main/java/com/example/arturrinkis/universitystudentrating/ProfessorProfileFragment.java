package com.example.arturrinkis.universitystudentrating;

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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arturrinkis.universitystudentrating.CacheData.ProfileCache;
import com.example.arturrinkis.universitystudentrating.CustomControls.Adapters.ProfileDisciplineAdapter;
import com.example.arturrinkis.universitystudentrating.CustomControls.NonScrollListView;
import com.example.arturrinkis.universitystudentrating.CustomControls.SquareImageView;
import com.example.arturrinkis.universitystudentrating.DTO.AuthUser;
import com.example.arturrinkis.universitystudentrating.DTO.Discipline;
import com.example.arturrinkis.universitystudentrating.DTO.ProfessorDiscipline;
import com.example.arturrinkis.universitystudentrating.DTO.SaveProfessorDisciplinesModel;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.IServerAPIManager;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.ServerAPIManager;
import com.example.arturrinkis.universitystudentrating.Utilities.DataUtility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class ProfessorProfileFragment extends Fragment {
    private View view = null;
    private ProfileDisciplineAdapter sciDisciplineAdapter = null;
    private ProfileDisciplineAdapter societyDisciplineAdapter = null;
    private ProfileDisciplineAdapter artDisciplineAdapter = null;
    private ProfileDisciplineAdapter sportDisciplineAdapter = null;
    private IServerAPIManager serverAPIManager = null;
    private DataUtility dataUtility = null;
    private ProgressDialog uploadPhotoImagePd = null;

    private GetAuthUserAsyncTask getAuthUserAsyncTask = null;
    private GetProfileRatingAsyncTask getProfileRatingAsyncTask = null;
    private SaveProfessorDisciplinesAsyncTask saveProfessorDisciplinesAsyncTask = null;
    private GetDisciplinesAsyncTask getDisciplinesAsyncTask = null;
    private GetProfessorDisciplinesAsyncTask getProfessorDisciplinesAsyncTask = null;
    private UploadImageAsyncTask uploadImageAsyncTask = null;

    public ProfessorProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        view = inflater.inflate(R.layout.fragment_professor_profile, container, false);
        serverAPIManager = new ServerAPIManager(getActivity().getApplicationContext());
        dataUtility = new DataUtility();
        uploadPhotoImagePd = new ProgressDialog(getActivity());
        uploadPhotoImagePd.setTitle("Uploading");
        uploadPhotoImagePd.setMessage("Uploading to the server...");

        initMainHeaderTextView();
        hideMainContent();
        showLoadingAnimation();

        if(ProfileCache.getInstance().getAuthUser() == null || ProfileCache.getInstance().getSciDisciplinesProfile() == null ||
           ProfileCache.getInstance().getSportDisciplinesProfile() == null || ProfileCache.getInstance().getArtDisciplinesProfile() == null ||
           ProfileCache.getInstance().getSocDisciplinesProfile() == null || ProfileCache.getInstance().getAverageProfessorRating() == 0.0 ||
           ProfileCache.getInstance().getProfessorDisciplinesProfile() == null)
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
        cancelGetDisciplinesAsyncTask();
        cancelGetProfessorDisciplinesAsyncTask();
        cancelUploadImageAsyncTask();
        ProfileCache.getInstance().setAverageProfessorRating(0);
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
        ratingBarHeader_tv.setText(Double.toString(ProfileCache.getInstance().getAverageProfessorRating()));

        RatingBar ratingBar_indicator = (RatingBar)view.findViewById(R.id.ratingBar_indicator);
        ratingBar_indicator.setRating((float)ProfileCache.getInstance().getAverageProfessorRating()/2);

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
    }

    private void initProfessorDisciplinesControls(){
        sciDisciplineAdapter = new ProfileDisciplineAdapter(view.getContext(), ProfileCache.getInstance().getSciDisciplinesProfile(), ProfileCache.getInstance().getProfessorDisciplinesProfile());
        societyDisciplineAdapter = new ProfileDisciplineAdapter(view.getContext(), ProfileCache.getInstance().getSocDisciplinesProfile(), ProfileCache.getInstance().getProfessorDisciplinesProfile());
        artDisciplineAdapter = new ProfileDisciplineAdapter(view.getContext(), ProfileCache.getInstance().getArtDisciplinesProfile(), ProfileCache.getInstance().getProfessorDisciplinesProfile());
        sportDisciplineAdapter = new ProfileDisciplineAdapter(view.getContext(), ProfileCache.getInstance().getSportDisciplinesProfile(), ProfileCache.getInstance().getProfessorDisciplinesProfile());

        NonScrollListView scientific_professors_lv = (NonScrollListView) view.findViewById(R.id.scientific_professors_lv);
        scientific_professors_lv.setAdapter(sciDisciplineAdapter);

        NonScrollListView sport_professors_lv = (NonScrollListView) view.findViewById(R.id.sport_professors_lv);
        sport_professors_lv.setAdapter(sportDisciplineAdapter);

        NonScrollListView art_professors_lv = (NonScrollListView) view.findViewById(R.id.art_professors_lv);
        art_professors_lv.setAdapter(artDisciplineAdapter);

        NonScrollListView society_professors_lv = (NonScrollListView) view.findViewById(R.id.society_professors_lv);
        society_professors_lv.setAdapter(societyDisciplineAdapter);
    }

    private void initSaveProfessorsButton(){
        Button save_professors_btn = (Button)view.findViewById(R.id.save_professors_btn);
        save_professors_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveProfessorDisciplinesModel model = new SaveProfessorDisciplinesModel();
                model.setProfessorId(ProfileCache.getInstance().getAuthUser().getUserProfileId());
                ArrayList<ProfessorDiscipline> professorDisciplines = new ArrayList<ProfessorDiscipline>();

                for(int i = 0; i < sciDisciplineAdapter.getCount(); ++i){
                    Discipline discipline = (Discipline)sciDisciplineAdapter.getItem(i);
                    if(sciDisciplineAdapter.checkedDisciplines.get(i) == true){
                        professorDisciplines.add(new ProfessorDiscipline(0, ProfileCache.getInstance().getAuthUser().getUserProfileId(), discipline.getId(), null, null));
                    }
                }

                for(int i = 0; i < sportDisciplineAdapter.getCount(); ++i){
                    Discipline discipline = (Discipline)sportDisciplineAdapter.getItem(i);
                    if(sportDisciplineAdapter.checkedDisciplines.get(i) == true){
                        professorDisciplines.add(new ProfessorDiscipline(0, ProfileCache.getInstance().getAuthUser().getUserProfileId(), discipline.getId(), null, null));
                    }
                }

                for(int i = 0; i < artDisciplineAdapter.getCount(); ++i){
                    Discipline discipline = (Discipline)artDisciplineAdapter.getItem(i);
                    if(artDisciplineAdapter.checkedDisciplines.get(i) == true){
                        professorDisciplines.add(new ProfessorDiscipline(0, ProfileCache.getInstance().getAuthUser().getUserProfileId(), discipline.getId(), null, null));
                    }
                }

                for(int i = 0; i < societyDisciplineAdapter.getCount(); ++i){
                    Discipline discipline = (Discipline)societyDisciplineAdapter.getItem(i);
                    if(societyDisciplineAdapter.checkedDisciplines.get(i) == true){
                        professorDisciplines.add(new ProfessorDiscipline(0, ProfileCache.getInstance().getAuthUser().getUserProfileId(), discipline.getId(), null, null));
                    }
                }

                model.setProfessorDisciplines(professorDisciplines);
                hideMainContent();
                showLoadingAnimation();

                ProfileCache.getInstance().setSciDisciplinesProfile(null);
                ProfileCache.getInstance().setSportDisciplinesProfile(null);
                ProfileCache.getInstance().setArtDisciplinesProfile(null);
                ProfileCache.getInstance().setSocDisciplinesProfile(null);
                ProfileCache.getInstance().setProfessorDisciplinesProfile(null);

                saveProfessorDisciplinesAsyncTask = new SaveProfessorDisciplinesAsyncTask();
                saveProfessorDisciplinesAsyncTask.execute(model);
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

    class SaveProfessorDisciplinesAsyncTask extends AsyncTask<SaveProfessorDisciplinesModel, Void, Boolean> {
        @Override
        protected Boolean doInBackground(SaveProfessorDisciplinesModel... params) {
            return serverAPIManager.getUserService().saveProfessorDisciplines(params[0]);
        }
        protected void onPostExecute(Boolean result) {
            getDisciplinesAsyncTask = new GetDisciplinesAsyncTask();
            getDisciplinesAsyncTask.execute(true);
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
            if(ProfileCache.getInstance().getAverageProfessorRating() == 0.0) {
                double averageRating = serverAPIManager.getStatisticService().getAverageRatingForProfessor(ProfileCache.getInstance().getAuthUser().getUserProfileId());
                ProfileCache.getInstance().setAverageProfessorRating(averageRating);
            }
            return null;
        }
        protected void onPostExecute(Void result){
            getDisciplinesAsyncTask = new GetDisciplinesAsyncTask();
            getDisciplinesAsyncTask.execute(false);
        }
    }

    class GetDisciplinesAsyncTask extends AsyncTask<Boolean, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Boolean... params) {
            if(ProfileCache.getInstance().getSciDisciplinesProfile() == null || params[0].booleanValue() == true) {
                ArrayList<Discipline> sciDisciplines = serverAPIManager.getStatisticService().getDisciplinesByBranch(2);
                ProfileCache.getInstance().setSciDisciplinesProfile(sciDisciplines);
            }

            if(ProfileCache.getInstance().getSportDisciplinesProfile() == null || params[0].booleanValue() == true) {
                ArrayList<Discipline> sportDisciplines = serverAPIManager.getStatisticService().getDisciplinesByBranch(1);
                ProfileCache.getInstance().setSportDisciplinesProfile(sportDisciplines);
            }

            if(ProfileCache.getInstance().getArtDisciplinesProfile() == null || params[0].booleanValue() == true) {
                ArrayList<Discipline> artDisciplines = serverAPIManager.getStatisticService().getDisciplinesByBranch(4);
                ProfileCache.getInstance().setArtDisciplinesProfile(artDisciplines);
            }

            if(ProfileCache.getInstance().getSocDisciplinesProfile() == null || params[0].booleanValue() == true) {
                ArrayList<Discipline> socDisciplines = serverAPIManager.getStatisticService().getDisciplinesByBranch(5);
                ProfileCache.getInstance().setSocDisciplinesProfile(socDisciplines);
            }

            return params[0].booleanValue();
        }
        protected void onPostExecute(Boolean result){
            getProfessorDisciplinesAsyncTask = new GetProfessorDisciplinesAsyncTask();
            getProfessorDisciplinesAsyncTask.execute(result);
        }
    }

    class GetProfessorDisciplinesAsyncTask extends AsyncTask<Boolean, Void, Void> {
        @Override
        protected Void doInBackground(Boolean... params) {
            int professorId = ProfileCache.getInstance().getAuthUser().getUserProfileId();

            if(ProfileCache.getInstance().getProfessorDisciplinesProfile() == null || params[0].booleanValue() == true) {
                ArrayList<ProfessorDiscipline> professorDisciplines = serverAPIManager.getDataServiceAPI().getProfessorDisciplines(professorId);
                ProfileCache.getInstance().setProfessorDisciplinesProfile(professorDisciplines);
            }
            return null;
        }
        protected void onPostExecute(Void result) {
            initControls();
        }
    }


    private void cancelSaveStudentProfessorDisciplinesAsyncTask() {
        if (saveProfessorDisciplinesAsyncTask == null) return;
        saveProfessorDisciplinesAsyncTask.cancel(false);
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

    private void cancelGetDisciplinesAsyncTask() {
        if (getProfessorDisciplinesAsyncTask == null) return;
        getProfessorDisciplinesAsyncTask.cancel(false);
    }

    private void cancelGetProfessorDisciplinesAsyncTask() {
        if (getProfessorDisciplinesAsyncTask == null) return;
        getProfessorDisciplinesAsyncTask.cancel(false);
    }
}