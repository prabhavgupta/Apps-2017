package com.example.arturrinkis.universitystudentrating;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arturrinkis.universitystudentrating.DTO.AuthUser;
import com.example.arturrinkis.universitystudentrating.DTO.City;
import com.example.arturrinkis.universitystudentrating.DTO.Country;
import com.example.arturrinkis.universitystudentrating.DTO.Course;
import com.example.arturrinkis.universitystudentrating.DTO.Faculty;
import com.example.arturrinkis.universitystudentrating.DTO.GenderType;
import com.example.arturrinkis.universitystudentrating.DTO.HttpResponse;
import com.example.arturrinkis.universitystudentrating.DTO.RegisterModel;
import com.example.arturrinkis.universitystudentrating.DTO.Status;
import com.example.arturrinkis.universitystudentrating.DTO.University;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.IServerAPIManager;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.ServerAPIManager;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {
    IServerAPIManager serverAPIManager;
    boolean isServerRespondError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        serverAPIManager = new ServerAPIManager(this.getApplicationContext());
        initControls();
        new GetGenderTypesAsyncTask().execute();
        new GetCountriesAsyncTask().execute();
        new GetStatusesAsyncTask().execute();
        new GetCoursesAsyncTask().execute();
    }

    private void initControls(){
        initButtons();
        initSpinners();
    }

    private void initButtons(){
        initRegisterButton();
    }

    private void initRegisterButton(){
        final Button registerButton = (Button)findViewById(R.id.register_btn);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    registerButton.setText("PLEASE WAIT...");
                    RegisterModel model = new RegisterModel();
                    EditText userName_et = (EditText) findViewById(R.id.username_et);
                    EditText email_et = (EditText) findViewById(R.id.email_et);
                    EditText firstname_et = (EditText) findViewById(R.id.firstname_et);
                    EditText surname_et = (EditText) findViewById(R.id.surname_et);
                    EditText password_et = (EditText) findViewById(R.id.password_et);
                    EditText confirmpassword_et = (EditText) findViewById(R.id.confirm_password_et);
                    EditText specialkey_et = (EditText) findViewById(R.id.specialkey_et);
                    Spinner gender_spinner = (Spinner) findViewById(R.id.gender_type_spinner);
                    Spinner country_spinner = (Spinner) findViewById(R.id.country_spinner);
                    Spinner city_spinner = (Spinner) findViewById(R.id.city_spinner);
                    Spinner university_spinner = (Spinner) findViewById(R.id.university_spinner);
                    Spinner status_spinner = (Spinner) findViewById(R.id.status_spinner);
                    Spinner faculty_spinner = (Spinner) findViewById(R.id.faculty_spinner);
                    Spinner course_spinner = (Spinner) findViewById(R.id.course_spinner);

                    model.setUserName(userName_et.getText().toString());
                    model.setEmail(email_et.getText().toString());
                    model.setFirstName(firstname_et.getText().toString());
                    model.setLastName(surname_et.getText().toString());
                    model.setPassword(password_et.getText().toString());
                    model.setConfirmPassword(confirmpassword_et.getText().toString());
                    model.setSpecialKEY(specialkey_et.getText().toString());
                    model.setCountryId(((Country) country_spinner.getSelectedItem()).getId());
                    model.setCityId(((City) city_spinner.getSelectedItem()).getId());
                    model.setUniversityId(((University) university_spinner.getSelectedItem()).getId());
                    model.setStatusId(((Status) status_spinner.getSelectedItem()).getId());
                    model.setGenderTypeId(((GenderType) gender_spinner.getSelectedItem()).getId());
                    model.setFacultyId(((Faculty) faculty_spinner.getSelectedItem()).getId());
                    model.setCourseId(((Course) course_spinner.getSelectedItem()).getId());

                    new GetRegisterAsyncTask().execute(model);
                }
                catch (Exception e){
                    registerButton.setText("REGISTER");
                }
            }
        });
    }

    private void handleRegisterResult(HttpResponse httpResponse) {
        final Button registerButton = (Button) findViewById(R.id.register_btn);
        if (httpResponse == null) {
            registerButton.setText("REGISTER");
            Toast.makeText(this, "Server is not responding", Toast.LENGTH_LONG).show();
        } else if (httpResponse.getCode() == 200) {
            new GetAuthUserAsyncTask().execute();
        } else {
            registerButton.setText("REGISTER");
            TextView error_messages_tv = (TextView) findViewById(R.id.error_messages_tv);
            if (httpResponse.getMessage() != "") {
                error_messages_tv.setText("");
                for (String text : httpResponse.getMessage().split("\n")) {
                    error_messages_tv.append("* " + text + "\n");
                }
                error_messages_tv.setVisibility(View.VISIBLE);
            } else {
                error_messages_tv.setText("");
                error_messages_tv.setVisibility(View.GONE);
            }
        }
    }

    //
    //
    //init spinners

    private void initSpinners() {
        initGenderTypeSpinner();
        initCountrySpinner();
        initCitySpinner();
        initUniversitySpinner();
        initStatusSpinner();
        initFacultySpinner();
        initCourseSpinner();
    }

    private void initGenderTypeSpinner() {
        ArrayList<GenderType> genderTypes = new ArrayList<GenderType>() {{
            add(new GenderType(-1, "Loading..."));
        }};
        ArrayAdapter<GenderType> adapter = new ArrayAdapter<GenderType>(this, android.R.layout.simple_spinner_item, genderTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.gender_type_spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    private void initCountrySpinner() {
        ArrayList<Country> countries = new ArrayList<Country>() {{
            add(new Country(-1, "Loading..."));
        }};
        ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(this, android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.country_spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Country selectedCountry = (Country) parent.getItemAtPosition(position);
                updateCitySpinner(new ArrayList<City>() {{
                    add(new City(-1, "Loading...", null));
                }});
                if (selectedCountry.getId() != -1) {
                    new GetCitiesByCountryAsyncTask().execute(selectedCountry.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    private void initCitySpinner() {
        ArrayList<City> cities = new ArrayList<City>() {{
            add(new City(-1, "Loading...", null));
        }};
        ArrayAdapter<City> adapter = new ArrayAdapter<City>(this, android.R.layout.simple_spinner_item, cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.city_spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                City selectedCity = (City) parent.getItemAtPosition(position);
                updateUniversitySpinner(new ArrayList<University>() {{
                    add(new University(-1, "Loading...", null));
                }});
                if (selectedCity.getId() != -1) {
                    new GetUniversitiesByCityAsyncTask().execute(selectedCity.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    private void initUniversitySpinner() {
        ArrayList<University> universities = new ArrayList<University>() {{
            add(new University(-1, "Loading...", null));
        }};
        ArrayAdapter<University> adapter = new ArrayAdapter<University>(this, android.R.layout.simple_spinner_item, universities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.university_spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                University selectedUniversity = (University) parent.getItemAtPosition(position);
                updateFacultySpinner(new ArrayList<Faculty>() {{
                    add(new Faculty(-1, "Loading...", null));
                }});

                if (selectedUniversity.getId() != -1) {
                    new GetFacultiesByUniversityAsyncTask().execute(selectedUniversity.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void initStatusSpinner() {
        ArrayList<Status> statuses = new ArrayList<Status>() {{
            add(new Status(-1, "Loading..."));
        }};
        ArrayAdapter<Status> adapter = new ArrayAdapter<Status>(this, android.R.layout.simple_spinner_item, statuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.status_spinner);
        spinner.setAdapter(adapter);

        if (statuses.get(0).getId() == 1) {
            TextView faculty_tv = (TextView) findViewById(R.id.faculty_tv);
            faculty_tv.setVisibility(View.VISIBLE);
            Spinner faculty_spinner = (Spinner) findViewById(R.id.faculty_spinner);
            faculty_spinner.setVisibility(View.VISIBLE);

            TextView course_tv = (TextView) findViewById(R.id.course_tv);
            course_tv.setVisibility(View.VISIBLE);
            Spinner course_spinner = (Spinner) findViewById(R.id.course_spinner);
            course_spinner.setVisibility(View.VISIBLE);
        } else {
            TextView faculty_tv = (TextView) findViewById(R.id.faculty_tv);
            faculty_tv.setVisibility(View.GONE);
            Spinner faculty_spinner = (Spinner) findViewById(R.id.faculty_spinner);
            faculty_spinner.setVisibility(View.GONE);

            TextView course_tv = (TextView) findViewById(R.id.course_tv);
            course_tv.setVisibility(View.GONE);
            Spinner course_spinner = (Spinner) findViewById(R.id.course_spinner);
            course_spinner.setVisibility(View.GONE);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Status selectedStatus = (Status) parent.getItemAtPosition(position);
                if (selectedStatus.getId() == 1) {
                    TextView faculty_tv = (TextView) findViewById(R.id.faculty_tv);
                    faculty_tv.setVisibility(View.VISIBLE);
                    Spinner faculty_spinner = (Spinner) findViewById(R.id.faculty_spinner);
                    faculty_spinner.setVisibility(View.VISIBLE);

                    TextView course_tv = (TextView) findViewById(R.id.course_tv);
                    course_tv.setVisibility(View.VISIBLE);
                    Spinner course_spinner = (Spinner) findViewById(R.id.course_spinner);
                    course_spinner.setVisibility(View.VISIBLE);
                } else {
                    TextView faculty_tv = (TextView) findViewById(R.id.faculty_tv);
                    faculty_tv.setVisibility(View.GONE);
                    Spinner faculty_spinner = (Spinner) findViewById(R.id.faculty_spinner);
                    faculty_spinner.setVisibility(View.GONE);

                    TextView course_tv = (TextView) findViewById(R.id.course_tv);
                    course_tv.setVisibility(View.GONE);
                    Spinner course_spinner = (Spinner) findViewById(R.id.course_spinner);
                    course_spinner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void initFacultySpinner() {
        ArrayList<Faculty> faculties = new ArrayList<Faculty>() {{
            add(new Faculty(-1, "Loading...", null));
        }};
        ArrayAdapter<Faculty> adapter = new ArrayAdapter<Faculty>(this, android.R.layout.simple_spinner_item, faculties);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.faculty_spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void initCourseSpinner() {
        ArrayList<Course> courses = new ArrayList<Course>() {{
            add(new Course(-1, -1));
        }};
        ArrayAdapter<Course> adapter = new ArrayAdapter<Course>(this, android.R.layout.simple_spinner_item, courses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.course_spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    //
    //
    //update spinners

    private void updateGenderTypeSpinner(ArrayList<GenderType> genderTypes) {
        if(genderTypes == null && isServerRespondError == false){
            isServerRespondError = true;
            Toast.makeText(this, "Server is not responding", Toast.LENGTH_LONG).show();
        }
        else if (genderTypes != null) {
            Spinner spinner = (Spinner) findViewById(R.id.gender_type_spinner);
            ArrayAdapter<GenderType> spinnerAdapter = new ArrayAdapter<GenderType>(this, android.R.layout.simple_spinner_item, genderTypes);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);
            spinnerAdapter.notifyDataSetChanged();
        }
    }

    private void updateCountrySpinner(ArrayList<Country> countries) {
        if(countries == null && isServerRespondError == false){
            isServerRespondError = true;
            Toast.makeText(this, "Server is not responding", Toast.LENGTH_LONG).show();
        }
        else if (countries != null) {
            Spinner spinner = (Spinner) findViewById(R.id.country_spinner);
            ArrayAdapter<Country> spinnerAdapter = new ArrayAdapter<Country>(this, android.R.layout.simple_spinner_item, countries);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);
            spinnerAdapter.notifyDataSetChanged();
        }
    }

    private void updateCitySpinner(ArrayList<City> cities) {
        if(cities == null && isServerRespondError == false){
            isServerRespondError = true;
            Toast.makeText(this, "Server is not responding", Toast.LENGTH_LONG).show();
        }
        else if (cities != null) {
            Spinner spinner = (Spinner) findViewById(R.id.city_spinner);
            ArrayAdapter<City> spinnerAdapter = new ArrayAdapter<City>(this, android.R.layout.simple_spinner_item, cities);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);
            spinnerAdapter.notifyDataSetChanged();
        }
    }

    private void updateUniversitySpinner(ArrayList<University> universities) {
        if(universities == null && isServerRespondError == false){
            isServerRespondError = true;
            Toast.makeText(this, "Server is not responding", Toast.LENGTH_LONG).show();
        }
        else if (universities != null) {
            Spinner spinner = (Spinner) findViewById(R.id.university_spinner);
            ArrayAdapter<University> spinnerAdapter = new ArrayAdapter<University>(this, android.R.layout.simple_spinner_item, universities);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);
            spinnerAdapter.notifyDataSetChanged();
        }
    }

    private void updateStatusSpinner(ArrayList<Status> statuses) {
        if(statuses == null && isServerRespondError == false){
            isServerRespondError = true;
            Toast.makeText(this, "Server is not responding", Toast.LENGTH_LONG).show();
        }
        else if (statuses != null) {
            Spinner spinner = (Spinner) findViewById(R.id.status_spinner);
            ArrayAdapter<Status> spinnerAdapter = new ArrayAdapter<Status>(this, android.R.layout.simple_spinner_item, statuses);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);
            spinnerAdapter.notifyDataSetChanged();
        }
    }

    private void updateFacultySpinner(ArrayList<Faculty> faculties) {
        if(faculties == null && isServerRespondError == false){
            isServerRespondError = true;
            Toast.makeText(this, "Server is not responding", Toast.LENGTH_LONG).show();
        }
        else if (faculties != null) {
            Spinner spinner = (Spinner) findViewById(R.id.faculty_spinner);
            ArrayAdapter<Faculty> spinnerAdapter = new ArrayAdapter<Faculty>(this, android.R.layout.simple_spinner_item, faculties);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);
            spinnerAdapter.notifyDataSetChanged();
        }
    }

    private void updateCourseSpinner(ArrayList<Course> courses) {
        if(courses == null && isServerRespondError == false){
            isServerRespondError = true;
            Toast.makeText(this, "Server is not responding", Toast.LENGTH_LONG).show();
        }
        else if (courses != null) {
            Spinner spinner = (Spinner) findViewById(R.id.course_spinner);
            ArrayAdapter<Course> spinnerAdapter = new ArrayAdapter<Course>(this, android.R.layout.simple_spinner_item, courses);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);
            spinnerAdapter.notifyDataSetChanged();
        }
    }

    private void endAuthenticate(AuthUser authUser) {
        Intent intent = new Intent(this, authUser.getUserProfile().getStatusId() == 1 ? StudentActivity.class : ProfessorActivity.class);
        startActivity(intent);
        finish();
    }

    //
    //
    //database async tasks

    class GetAuthUserAsyncTask extends AsyncTask<Void, Void, AuthUser> {
        @Override
        protected AuthUser doInBackground(Void... params) {
            return serverAPIManager.getUserService().getAuthUser(false);
        }
        protected void onPostExecute(AuthUser authUser){
            endAuthenticate(authUser);
        }
    }

    class GetGenderTypesAsyncTask extends AsyncTask<Void, Void, ArrayList<GenderType>> {
        @Override
        protected ArrayList<GenderType> doInBackground(Void... params) {
            return  serverAPIManager.getDataServiceAPI().getGenderTypes();
        }
        protected void onPostExecute(ArrayList<GenderType> genderTypes){
            updateGenderTypeSpinner(genderTypes);
        }
    }

    class GetCountriesAsyncTask extends AsyncTask<Void, Void, ArrayList<Country>> {
        @Override
        protected ArrayList<Country> doInBackground(Void... params) {
            return serverAPIManager.getDataServiceAPI().getCountries();
        }
        protected void onPostExecute(ArrayList<Country> countries){
            updateCountrySpinner(countries);
        }
    }

    class GetCitiesByCountryAsyncTask extends AsyncTask<Integer, Void, ArrayList<City>> {
        @Override
        protected ArrayList<City> doInBackground(Integer... params) {
            return serverAPIManager.getDataServiceAPI().getCitiesByCountry(params[0].intValue());
        }
        protected void onPostExecute(ArrayList<City> cities){
            updateCitySpinner(cities);
        }
    }

    class GetUniversitiesByCityAsyncTask extends AsyncTask<Integer, Void, ArrayList<University>> {
        @Override
        protected ArrayList<University> doInBackground(Integer... params) {
            return serverAPIManager.getDataServiceAPI().getUniversitiesByCity(params[0].intValue());
        }
        protected void onPostExecute(ArrayList<University> universities){
            updateUniversitySpinner(universities);
        }
    }

    class GetStatusesAsyncTask extends AsyncTask<Void, Void, ArrayList<Status>> {
        @Override
        protected ArrayList<com.example.arturrinkis.universitystudentrating.DTO.Status> doInBackground(Void... params) {
            return serverAPIManager.getDataServiceAPI().getStatuses();
        }
        protected void onPostExecute(ArrayList<com.example.arturrinkis.universitystudentrating.DTO.Status> statuses){
            updateStatusSpinner(statuses);
        }
    }

    class GetFacultiesByUniversityAsyncTask extends AsyncTask<Integer, Void, ArrayList<Faculty>> {
        @Override
        protected ArrayList<Faculty> doInBackground(Integer... params) {
            return serverAPIManager.getDataServiceAPI().getFacultiesByUniversity(params[0].intValue());
        }
        protected void onPostExecute(ArrayList<Faculty> faculties){
            updateFacultySpinner(faculties);
        }
    }

    class GetCoursesAsyncTask extends AsyncTask<Void, Void, ArrayList<Course>> {
        @Override
        protected ArrayList<Course> doInBackground(Void... params) {
            return serverAPIManager.getDataServiceAPI().getCourses();
        }
        protected void onPostExecute(ArrayList<Course> courses){
            updateCourseSpinner(courses);
        }
    }

    class GetRegisterAsyncTask extends AsyncTask<RegisterModel, Void, HttpResponse> {
        @Override
        protected HttpResponse doInBackground(RegisterModel... params) {
            return serverAPIManager.getAuthServiceAPI().register(params[0]);
        }
        protected void onPostExecute(HttpResponse HttpResponse) {
            handleRegisterResult(HttpResponse);
        }
    }
}