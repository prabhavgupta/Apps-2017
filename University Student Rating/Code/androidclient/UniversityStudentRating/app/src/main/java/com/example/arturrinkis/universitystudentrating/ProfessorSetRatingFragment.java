package com.example.arturrinkis.universitystudentrating;


import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arturrinkis.universitystudentrating.CacheData.ProfileCache;
import com.example.arturrinkis.universitystudentrating.DTO.Course;
import com.example.arturrinkis.universitystudentrating.DTO.Faculty;
import com.example.arturrinkis.universitystudentrating.DTO.Rating;
import com.example.arturrinkis.universitystudentrating.DTO.RatingDivision;
import com.example.arturrinkis.universitystudentrating.DTO.RatingType;
import com.example.arturrinkis.universitystudentrating.DTO.UserProfile;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.IServerAPIManager;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.ServerAPIManager;

import java.util.ArrayList;

public class ProfessorSetRatingFragment extends Fragment {
    private String SET_RATING_FRAGMENT_TAG = "SET_RATING_FRAGMENT_TAG";
    private View view = null;
    private boolean isServerRespondError = false;
    private IServerAPIManager serverAPIManager = null;

    private GetFacultiesByUniversityAsyncTask getFacultiesByUniversityAsyncTask = null;
    private GetCoursesAsyncTask getCoursesAsyncTask = null;
    private GetStudentsAsyncTask getStudentsAsyncTask = null;
    private GetRatingDivisionsAsyncTask getRatingDivisionsAsyncTask = null;
    private GetRatingTypeAsyncTask getRatingTypeAsyncTask = null;
    private SetRatingAsyncTask setRatingAsyncTask = null;
    private int disciplineId;

    public ProfessorSetRatingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        view = inflater.inflate(R.layout.fragment_professor_set_rating, container, false);
        serverAPIManager = new ServerAPIManager(getActivity().getApplicationContext());
        disciplineId = getArguments().getInt("DisciplineId");
        
        initControls();

        getRatingDivisionsAsyncTask = new GetRatingDivisionsAsyncTask();
        getRatingDivisionsAsyncTask.execute();

        getFacultiesByUniversityAsyncTask = new GetFacultiesByUniversityAsyncTask();
        getFacultiesByUniversityAsyncTask.execute(ProfileCache.getInstance().getAuthUser().getUserProfile().getUniversityId());

        getCoursesAsyncTask = new GetCoursesAsyncTask();
        getCoursesAsyncTask.execute();

        getRatingTypeAsyncTask = new GetRatingTypeAsyncTask();
        getRatingTypeAsyncTask.execute();

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cancelGetFacultiesByUniversityAsyncTask();
        cancelGetCoursesAsyncTask();
        cancelGetStudentsAsyncTask();
        cancelGetRatingDivisionsAsyncTask();
        cancelGetRatingTypeAsyncTask();
        cancelSetRatingAsyncTask();
    }

    private void initControls() {
        try {
            hideLoadingAnimation();
            showTabLayout();
            initMainHeaderTextView();
            initSpinners();
            initSetRatingButton();
        }
        catch (Exception e){
           // Log.e("exception", "");
        }
    }

    private void showTabLayout() {
        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.main_tab);
        tabLayout.setVisibility(View.VISIBLE);
    }

    private void initMainHeaderTextView() {
        TextView main_header_tv = (TextView) getActivity().findViewById(R.id.main_header_tv);
        main_header_tv.setText("Search setting");
    }

    //
    //
    //init spinners
    
    private void initSpinners() {
        initFacultySpinner();
        initCourseSpinner();
        initRatingDivisionSpinner();
    }

    private void initFacultySpinner() {
        ArrayList<Faculty> faculties = new ArrayList<Faculty>() {{
            add(new Faculty(-1, "Loading...", null));
        }};
        ArrayAdapter<Faculty> adapter = new ArrayAdapter<Faculty>(getActivity(), android.R.layout.simple_spinner_item, faculties);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) view.findViewById(R.id.faculty_spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View _view, int position, long id) {
                Faculty selectFaculty = (Faculty) parent.getItemAtPosition(position);
                updateStudentsSpinner(new ArrayList<UserProfile>() {{
                    add(new UserProfile(-1, "Loading...", ""));
                }});
                if (selectFaculty.getId() != -1) {
                    Spinner course_spinner = (Spinner) view.findViewById(R.id.course_spinner);
                    if (((Course) course_spinner.getSelectedItem()).getId() > 0) {
                        getStudentsAsyncTask = new GetStudentsAsyncTask();
                        getStudentsAsyncTask.execute(selectFaculty.getId(), ((Course) course_spinner.getSelectedItem()).getId());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void hideLoadingAnimation() {
        RelativeLayout loading_layout = (RelativeLayout) getActivity().findViewById(R.id.loading_layout);
        loading_layout.setVisibility(View.GONE);
    }

    private void initCourseSpinner() {
        ArrayList<Course> courses = new ArrayList<Course>() {{
            add(new Course(-1, -1));
        }};
        ArrayAdapter<Course> adapter = new ArrayAdapter<Course>(getActivity(), android.R.layout.simple_spinner_item, courses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) view.findViewById(R.id.course_spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View _view, int position, long id) {
                Course selectCourse = (Course) parent.getItemAtPosition(position);
                updateStudentsSpinner(new ArrayList<UserProfile>() {{
                    add(new UserProfile(-1, "Loading...", ""));
                }});
                if (selectCourse.getId() != -1) {
                    Spinner faculty_spinner = (Spinner) view.findViewById(R.id.faculty_spinner);
                    if (((Faculty) faculty_spinner.getSelectedItem()).getId() > 0) {
                        getStudentsAsyncTask = new GetStudentsAsyncTask();
                        getStudentsAsyncTask.execute(((Faculty) faculty_spinner.getSelectedItem()).getId(), selectCourse.getId());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void initRatingDivisionSpinner() {
        ArrayList<RatingDivision> ratingDivisions = new ArrayList<RatingDivision>() {{
            add(new RatingDivision(-1, "Loading..."));
        }};
        ArrayAdapter<RatingDivision> adapter = new ArrayAdapter<RatingDivision>(getActivity(), android.R.layout.simple_spinner_item, ratingDivisions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) view.findViewById(R.id.rating_division_spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View _view, int position, long id) {
                RatingDivision selectRatingDivision = (RatingDivision) parent.getItemAtPosition(position);

                if(selectRatingDivision.getId() == 1){
                    EditText points_et =  (EditText) view.findViewById(R.id.points_et);
                    points_et.setVisibility(View.GONE);

                    Spinner points_spinner = (Spinner) view.findViewById(R.id.points_spinner);
                    points_spinner.setVisibility(View.VISIBLE);
                }
                else{
                    EditText points_et =  (EditText) view.findViewById(R.id.points_et);
                    points_et.setVisibility(View.VISIBLE);

                    Spinner points_spinner = (Spinner) view.findViewById(R.id.points_spinner);
                    points_spinner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void initSetRatingButton(){
        final Button searchButton = (Button) view.findViewById(R.id.set_rating_btn);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchButton.setText("PLEASE WAIT...");
                Rating rating = new Rating();

                try {
                    Spinner student_spinner = (Spinner) view.findViewById(R.id.student_spinner);
                    Spinner rating_division_spinner = (Spinner) view.findViewById(R.id.rating_division_spinner);
                    Spinner points_spinner = (Spinner) view.findViewById(R.id.points_spinner);
                    EditText points_et = (EditText) view.findViewById(R.id.points_et);
                    EditText description_et = (EditText) view.findViewById(R.id.description_et);


                    if (((UserProfile) student_spinner.getSelectedItem()).getId() > 0) {
                        rating.setStudentId(((UserProfile) student_spinner.getSelectedItem()).getId());
                        rating.setCourseId(((UserProfile) student_spinner.getSelectedItem()).getCourseId());
                    }
                    if (((RatingDivision) rating_division_spinner.getSelectedItem()).getId() > 0) {
                        rating.setRatingDivisionId(((RatingDivision) rating_division_spinner.getSelectedItem()).getId());
                        if (((RatingDivision) rating_division_spinner.getSelectedItem()).getId() == 1) {
                            rating.setPoints(Integer.parseInt(((String) points_spinner.getSelectedItem())));
                        } else {
                            rating.setPoints(Integer.parseInt((points_et.getText().toString())));
                        }
                        if(rating.getPoints() <= 0) throw new Exception("set rating error data parse");
                    }
                    rating.setName(description_et.getText().toString());
                    rating.setDisciplineId(disciplineId);
                    rating.setProfessorId(ProfileCache.getInstance().getAuthUser().getUserProfileId());

                    setRatingAsyncTask = new SetRatingAsyncTask();
                    setRatingAsyncTask.execute(rating);
                }
                catch(Exception e){
                    Toast.makeText(getActivity(), "Set rating error data parse. Try again.", Toast.LENGTH_LONG).show();
                    searchButton.setText("SET RATING");
                    Log.d("set rating", "set rating error data parse");
                }
            }
        });
    }

    //
    //
    //update spinners

    private void updateFacultySpinner(ArrayList<Faculty> faculties) {
        try {
            if (faculties == null && isServerRespondError == false) {
                isServerRespondError = true;
                Toast.makeText(getActivity(), "Server is not responding", Toast.LENGTH_LONG).show();
            } else if (faculties != null) {
                Spinner spinner = (Spinner) view.findViewById(R.id.faculty_spinner);
                ArrayAdapter<Faculty> spinnerAdapter = new ArrayAdapter<Faculty>(getActivity(), android.R.layout.simple_spinner_item, faculties);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
                spinnerAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e("async task exeption", e.getMessage());
        }
    }

    private void updateCourseSpinner(ArrayList<Course> courses) {
        try {
            if (courses == null && isServerRespondError == false) {
                isServerRespondError = true;
                Toast.makeText(getActivity(), "Server is not responding", Toast.LENGTH_LONG).show();
            } else if (courses != null) {
                Spinner spinner = (Spinner) view.findViewById(R.id.course_spinner);
                ArrayAdapter<Course> spinnerAdapter = new ArrayAdapter<Course>(getActivity(), android.R.layout.simple_spinner_item, courses);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
                spinnerAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e("async task exeption", e.getMessage());
        }
    }

    private void updateStudentsSpinner(ArrayList<UserProfile> userProfiles){
        try {
            if (userProfiles == null && isServerRespondError == false) {
                isServerRespondError = true;
                Toast.makeText(getActivity(), "Server is not responding", Toast.LENGTH_LONG).show();
            } else if (userProfiles != null) {
                Spinner spinner = (Spinner) view.findViewById(R.id.student_spinner);
                ArrayAdapter<UserProfile> spinnerAdapter = new ArrayAdapter<UserProfile>(getActivity(), android.R.layout.simple_spinner_item, userProfiles);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
                spinnerAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e("async task exeption", e.getMessage());
        }
    }

    private void updateRatingDivisionSpinner(ArrayList<RatingDivision> ratingDivisions) {
        try {
            if (ratingDivisions == null && isServerRespondError == false) {
                isServerRespondError = true;
                Toast.makeText(getActivity(), "Server is not responding", Toast.LENGTH_LONG).show();
            } else if (ratingDivisions != null) {
                Spinner spinner = (Spinner) view.findViewById(R.id.rating_division_spinner);
                ArrayAdapter<RatingDivision> spinnerAdapter = new ArrayAdapter<RatingDivision>(getActivity(), android.R.layout.simple_spinner_item, ratingDivisions);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
                spinnerAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e("async task exeption", e.getMessage());
        }
    }
    
    private void updatePointsSpinner(RatingType ratingType){
        try {
            if (ratingType == null && isServerRespondError == false) {
                isServerRespondError = true;
                Toast.makeText(getActivity(), "Server is not responding", Toast.LENGTH_LONG).show();
            } else if (ratingType != null) {
                Spinner spinner = (Spinner) view.findViewById(R.id.points_spinner);
                
                ArrayList<String> pointsStr = new ArrayList<>();
                for(int i = ratingType.getMinPoints(); i <= ratingType.getMaxPoints(); ++i){
                    pointsStr.add(String.valueOf(i));
                }
                
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, pointsStr);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
                spinnerAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e("async task exeption", e.getMessage());
        }
    }

    //
    //
    //database async tasks

    class GetFacultiesByUniversityAsyncTask extends AsyncTask<Integer, Void, ArrayList<Faculty>> {
        @Override
        protected ArrayList<Faculty> doInBackground(Integer... params) {
            return serverAPIManager.getDataServiceAPI().getFacultiesByUniversity(params[0].intValue());
        }

        protected void onPostExecute(ArrayList<Faculty> faculties) {
            updateFacultySpinner(faculties);
        }
    }

    class GetCoursesAsyncTask extends AsyncTask<Void, Void, ArrayList<Course>> {
        @Override
        protected ArrayList<Course> doInBackground(Void... params) {
            return serverAPIManager.getDataServiceAPI().getCourses();
        }

        protected void onPostExecute(ArrayList<Course> courses) {
            updateCourseSpinner(courses);
        }
    }

    class GetStudentsAsyncTask extends AsyncTask<Integer, Void, ArrayList<UserProfile>> {
        @Override
        protected ArrayList<UserProfile> doInBackground(Integer... params) {
            int universityId = ProfileCache.getInstance().getAuthUser().getUserProfile().getUniversityId();
            int facultyId = params[0].intValue();
            int courseId = params[1].intValue();
            int professorId = ProfileCache.getInstance().getAuthUser().getUserProfileId();

            return serverAPIManager.getUserService().getStudentUserProfilesByProfDisc(universityId, facultyId, courseId, professorId, disciplineId);
        }

        protected void onPostExecute(ArrayList<UserProfile> userProfiles) {
            updateStudentsSpinner(userProfiles);
        }
    }

    class GetRatingDivisionsAsyncTask extends AsyncTask<Void, Void, ArrayList<RatingDivision>> {
        @Override
        protected ArrayList<RatingDivision> doInBackground(Void... params) {
            return serverAPIManager.getDataServiceAPI().GetRatingDivisions();
        }

        protected void onPostExecute(ArrayList<RatingDivision> ratingDivisions) {
            updateRatingDivisionSpinner(ratingDivisions);
        }
    }

    class GetRatingTypeAsyncTask extends AsyncTask<Void, Void, RatingType> {
        @Override
        protected RatingType doInBackground(Void... params) {
            int universityId = ProfileCache.getInstance().getAuthUser().getUserProfile().getUniversityId();
            return serverAPIManager.getStatisticService().getRatingType(universityId, disciplineId);
        }

        protected void onPostExecute(RatingType ratingType) {
            updatePointsSpinner(ratingType);
        }
    }


    class SetRatingAsyncTask extends AsyncTask<Rating, Void, Void> {
        @Override
        protected Void doInBackground(Rating... params) {
            serverAPIManager.getStatisticService().setRating(params[0]);
            return null;
        }

        protected void onPostExecute(Void result) {
            final Button searchButton = (Button) view.findViewById(R.id.set_rating_btn);
            searchButton.setText("SET RATING");
        }
    }



    private void cancelGetFacultiesByUniversityAsyncTask() {
        if (getFacultiesByUniversityAsyncTask == null) return;
        getFacultiesByUniversityAsyncTask.cancel(false);
    }

    private void cancelGetCoursesAsyncTask() {
        if (getCoursesAsyncTask == null) return;
        getCoursesAsyncTask.cancel(false);
    }

    private void cancelGetStudentsAsyncTask() {
        if (getStudentsAsyncTask == null) return;
        getStudentsAsyncTask.cancel(false);
    }

    private void cancelGetRatingDivisionsAsyncTask() {
        if (getRatingDivisionsAsyncTask == null) return;
        getRatingDivisionsAsyncTask.cancel(false);
    }

    private void cancelGetRatingTypeAsyncTask() {
        if (getRatingTypeAsyncTask == null) return;
        getRatingTypeAsyncTask.cancel(false);
    }


    private void cancelSetRatingAsyncTask() {
        if (setRatingAsyncTask == null) return;
        setRatingAsyncTask.cancel(false);
    }
}