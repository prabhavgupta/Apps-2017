package com.example.arturrinkis.universitystudentrating;


import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arturrinkis.universitystudentrating.CacheData.ProfileCache;
import com.example.arturrinkis.universitystudentrating.DTO.City;
import com.example.arturrinkis.universitystudentrating.DTO.Country;
import com.example.arturrinkis.universitystudentrating.DTO.Course;
import com.example.arturrinkis.universitystudentrating.DTO.Faculty;
import com.example.arturrinkis.universitystudentrating.DTO.GenderType;
import com.example.arturrinkis.universitystudentrating.DTO.SearchModel;
import com.example.arturrinkis.universitystudentrating.DTO.Status;
import com.example.arturrinkis.universitystudentrating.DTO.University;
import com.example.arturrinkis.universitystudentrating.DTO.UserProfile;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.IServerAPIManager;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.ServerAPIManager;

import java.util.ArrayList;

public class SettingSearchFragment extends Fragment {
    private String SETTING_SEARCH_FRAGMENT_TAG = "SETTING_SEARCH_FRAGMENT_TAG";
    private View view = null;
    private boolean isServerRespondError = false;
    private IServerAPIManager serverAPIManager;

    private GetGenderTypesAsyncTask getGenderTypesAsyncTask = null;
    private GetCountriesAsyncTask getCountriesAsyncTask = null;
    private GetCitiesByCountryAsyncTask getCitiesByCountryAsyncTask = null;
    private GetUniversitiesByCityAsyncTask getUniversitiesByCityAsyncTask = null;
    private GetStatusesAsyncTask getStatusesAsyncTask = null;
    private GetFacultiesByUniversityAsyncTask getFacultiesByUniversityAsyncTask = null;
    private GetCoursesAsyncTask getCoursesAsyncTask = null;
    private SearchUsersAsyncTask searchUsersAsyncTask = null;

    public SettingSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        view = inflater.inflate(R.layout.fragment_setting_search, container, false);
        serverAPIManager = new ServerAPIManager(getActivity().getApplicationContext());
        initControls();

        getGenderTypesAsyncTask = new GetGenderTypesAsyncTask();
        getGenderTypesAsyncTask.execute();

        getCountriesAsyncTask = new GetCountriesAsyncTask();
        getCountriesAsyncTask.execute();

        getStatusesAsyncTask = new GetStatusesAsyncTask();
        getStatusesAsyncTask.execute();

        getCoursesAsyncTask = new GetCoursesAsyncTask();
        getCoursesAsyncTask.execute();

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cancelGetGenderTypesAsyncTask();
        cancelGetCountriesAsyncTask();
        cancelGetCitiesByCountryAsyncTask();
        cancelGetUniversitiesByCityAsyncTask();
        cancelGetStatusesAsyncTask();
        cancelGetFacultiesByUniversityAsyncTask();
        cancelGetCoursesAsyncTask();
        cancelSearchUsersAsyncTask();
    }

    private void initControls() {
        try {
            if(getActivity().getClass() != GuestActivity.class) showTabLayout();
            hideLoadingAnimation();
            initMainHeaderTextView();
            initSearchButton();
            initSpinners();
        }
        catch (Exception e){

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

    private void initSearchButton() {
        final Button searchButton = (Button) view.findViewById(R.id.search_btn);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchButton.setText("PLEASE WAIT...");
                SearchModel model = new SearchModel();

                EditText firstname_et = (EditText) view.findViewById(R.id.firstname_et);
                EditText surname_et = (EditText) view.findViewById(R.id.surname_et);
                Spinner gender_spinner = (Spinner) view.findViewById(R.id.gender_type_spinner);
                Spinner country_spinner = (Spinner) view.findViewById(R.id.country_spinner);
                Spinner city_spinner = (Spinner) view.findViewById(R.id.city_spinner);
                Spinner university_spinner = (Spinner) view.findViewById(R.id.university_spinner);
                Spinner status_spinner = (Spinner) view.findViewById(R.id.status_spinner);
                Spinner faculty_spinner = (Spinner) view.findViewById(R.id.faculty_spinner);
                Spinner course_spinner = (Spinner) view.findViewById(R.id.course_spinner);

                model.setFirstName(firstname_et.getText().toString());
                model.setLastName(surname_et.getText().toString());

                if (((Country) country_spinner.getSelectedItem()).getId() > 0)
                    model.setCountryId(((Country) country_spinner.getSelectedItem()).getId());
                if (((City) city_spinner.getSelectedItem()).getId() > 0)
                    model.setCityId(((City) city_spinner.getSelectedItem()).getId());
                if (((University) university_spinner.getSelectedItem()).getId() > 0)
                    model.setUniversityId(((University) university_spinner.getSelectedItem()).getId());
                if (((Status) status_spinner.getSelectedItem()).getId() > 0)
                    model.setStatusId(((Status) status_spinner.getSelectedItem()).getId());
                if (((GenderType) gender_spinner.getSelectedItem()).getId() > 0)
                    model.setGenderTypeId(((GenderType) gender_spinner.getSelectedItem()).getId());
                if (((Faculty) faculty_spinner.getSelectedItem()).getId() > 0)
                    model.setFacultyId(((Faculty) faculty_spinner.getSelectedItem()).getId());
                if (((Course) course_spinner.getSelectedItem()).getId() > 0)
                    model.setCourseId(((Course) course_spinner.getSelectedItem()).getId());
                model.setSkipCount(0);
                model.setTakeCount(ProfileCache.getInstance().getTakeCount());

                ProfileCache.getInstance().setSearchUserResult(null);
                ProfileCache.getInstance().setSearchModel(model);
                searchUsersAsyncTask = new SearchUsersAsyncTask();
                searchUsersAsyncTask.execute(model);
            }
        });
    }

    private void hideLoadingAnimation() {
        RelativeLayout loading_layout = (RelativeLayout) getActivity().findViewById(R.id.loading_layout);
        loading_layout.setVisibility(View.GONE);
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
        ArrayAdapter<GenderType> adapter = new ArrayAdapter<GenderType>(getActivity(), android.R.layout.simple_spinner_item, genderTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) view.findViewById(R.id.gender_type_spinner);
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
        ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(getActivity(), android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) view.findViewById(R.id.country_spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Country selectedCountry = (Country) parent.getItemAtPosition(position);
                updateCitySpinner(new ArrayList<City>() {{
                    add(new City(-1, "Loading...", null));
                }});
                if (selectedCountry.getId() != -1 && selectedCountry.getId() != -2) {
                    getCitiesByCountryAsyncTask = new GetCitiesByCountryAsyncTask();
                    getCitiesByCountryAsyncTask.execute(selectedCountry.getId());
                } else if (selectedCountry.getId() == -2) {
                    updateCitySpinner(new ArrayList<City>() {{
                        add(new City(-2, "City", null));
                    }});
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
        ArrayAdapter<City> adapter = new ArrayAdapter<City>(getActivity(), android.R.layout.simple_spinner_item, cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) view.findViewById(R.id.city_spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                City selectedCity = (City) parent.getItemAtPosition(position);
                updateUniversitySpinner(new ArrayList<University>() {{
                    add(new University(-1, "Loading...", null));
                }});
                if (selectedCity.getId() != -1 && selectedCity.getId() != -2) {
                    getUniversitiesByCityAsyncTask = new GetUniversitiesByCityAsyncTask();
                    getUniversitiesByCityAsyncTask.execute(selectedCity.getId());
                } else if (selectedCity.getId() == -2) {
                    updateUniversitySpinner(new ArrayList<University>() {{
                        add(new University(-2, "University", null));
                    }});
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
        ArrayAdapter<University> adapter = new ArrayAdapter<University>(getActivity(), android.R.layout.simple_spinner_item, universities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) view.findViewById(R.id.university_spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                University selectedUniversity = (University) parent.getItemAtPosition(position);
                updateFacultySpinner(new ArrayList<Faculty>() {{
                    add(new Faculty(-1, "Loading...", null));
                }});

                if (selectedUniversity.getId() != -1 && selectedUniversity.getId() != -2) {
                    getFacultiesByUniversityAsyncTask = new GetFacultiesByUniversityAsyncTask();
                    getFacultiesByUniversityAsyncTask.execute(selectedUniversity.getId());
                } else if (selectedUniversity.getId() == -2) {
                    updateFacultySpinner(new ArrayList<Faculty>() {{
                        add(new Faculty(-2, "Faculty", null));
                    }});
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
        ArrayAdapter<Status> adapter = new ArrayAdapter<Status>(getActivity(), android.R.layout.simple_spinner_item, statuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) view.findViewById(R.id.status_spinner);
        spinner.setAdapter(adapter);

        if (statuses.get(0).getId() == 1) {
            LinearLayout faculty_layout = (LinearLayout) view.findViewById(R.id.faculty_layout);
            faculty_layout.setVisibility(View.VISIBLE);

            LinearLayout course_layout = (LinearLayout) view.findViewById(R.id.course_layout);
            course_layout.setVisibility(View.VISIBLE);
        } else {
            LinearLayout faculty_layout = (LinearLayout) view.findViewById(R.id.faculty_layout);
            faculty_layout.setVisibility(View.GONE);

            LinearLayout course_layout = (LinearLayout) view.findViewById(R.id.course_layout);
            course_layout.setVisibility(View.GONE);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View _view, int position, long id) {
                Status selectedStatus = (Status) parent.getItemAtPosition(position);
                if (selectedStatus.getId() == 1) {
                    LinearLayout faculty_layout = (LinearLayout) view.findViewById(R.id.faculty_layout);
                    faculty_layout.setVisibility(View.VISIBLE);

                    LinearLayout course_layout = (LinearLayout) view.findViewById(R.id.course_layout);
                    course_layout.setVisibility(View.VISIBLE);
                } else {
                    LinearLayout faculty_layout = (LinearLayout) view.findViewById(R.id.faculty_layout);
                    faculty_layout.setVisibility(View.GONE);

                    LinearLayout course_layout = (LinearLayout) view.findViewById(R.id.course_layout);
                    course_layout.setVisibility(View.GONE);
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
        ArrayAdapter<Faculty> adapter = new ArrayAdapter<Faculty>(getActivity(), android.R.layout.simple_spinner_item, faculties);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) view.findViewById(R.id.faculty_spinner);
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
        ArrayAdapter<Course> adapter = new ArrayAdapter<Course>(getActivity(), android.R.layout.simple_spinner_item, courses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) view.findViewById(R.id.course_spinner);
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
        try {
            if (genderTypes == null && isServerRespondError == false) {
                isServerRespondError = true;
                Toast.makeText(getActivity(), "Server is not responding", Toast.LENGTH_LONG).show();
            } else if (genderTypes != null) {
                Spinner spinner = (Spinner) view.findViewById(R.id.gender_type_spinner);
                ArrayAdapter<GenderType> spinnerAdapter = new ArrayAdapter<GenderType>(getActivity(), android.R.layout.simple_spinner_item, genderTypes);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
                spinnerAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e("async task exeption", e.getMessage());
        }
    }

    private void updateCountrySpinner(ArrayList<Country> countries) {
        try {
            if (countries == null && isServerRespondError == false) {
                isServerRespondError = true;
                Toast.makeText(getActivity(), "Server is not responding", Toast.LENGTH_LONG).show();
            } else if (countries != null) {
                Spinner spinner = (Spinner) view.findViewById(R.id.country_spinner);
                ArrayAdapter<Country> spinnerAdapter = new ArrayAdapter<Country>(getActivity(), android.R.layout.simple_spinner_item, countries);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
                spinnerAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e("async task exeption", e.getMessage());
        }
    }

    private void updateCitySpinner(ArrayList<City> cities) {
        try {
            if (cities == null && isServerRespondError == false) {
                isServerRespondError = true;
                Toast.makeText(getActivity(), "Server is not responding", Toast.LENGTH_LONG).show();
            } else if (cities != null) {
                Spinner spinner = (Spinner) view.findViewById(R.id.city_spinner);
                ArrayAdapter<City> spinnerAdapter = new ArrayAdapter<City>(getActivity(), android.R.layout.simple_spinner_item, cities);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
                spinnerAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e("async task exeption", e.getMessage());
        }
    }

    private void updateUniversitySpinner(ArrayList<University> universities) {
        try {
            if (universities == null && isServerRespondError == false) {
                isServerRespondError = true;
                Toast.makeText(getActivity(), "Server is not responding", Toast.LENGTH_LONG).show();
            } else if (universities != null) {
                Spinner spinner = (Spinner) view.findViewById(R.id.university_spinner);
                ArrayAdapter<University> spinnerAdapter = new ArrayAdapter<University>(getActivity(), android.R.layout.simple_spinner_item, universities);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
                spinnerAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e("async task exeption", e.getMessage());
        }
    }

    private void updateStatusSpinner(ArrayList<Status> statuses) {
        try {
            if (statuses == null && isServerRespondError == false) {
                isServerRespondError = true;
                Toast.makeText(getActivity(), "Server is not responding", Toast.LENGTH_LONG).show();
            } else if (statuses != null) {
                Spinner spinner = (Spinner) view.findViewById(R.id.status_spinner);
                ArrayAdapter<Status> spinnerAdapter = new ArrayAdapter<Status>(getActivity(), android.R.layout.simple_spinner_item, statuses);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerAdapter);
                spinnerAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e("async task exeption", e.getMessage());
        }
    }

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


    private void showSearchResult() {
        try {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
            SearchFragment searchFragment = new SearchFragment();
            FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.tab_container, searchFragment);
            fragmentTransaction.addToBackStack(SETTING_SEARCH_FRAGMENT_TAG);
            fragmentTransaction.commit();
        } catch (Exception e) {
            Log.e("async task exeption", e.getMessage());
        }
    }

    //
    //
    //database async tasks

    class GetGenderTypesAsyncTask extends AsyncTask<Void, Void, ArrayList<GenderType>> {
        @Override
        protected ArrayList<GenderType> doInBackground(Void... params) {
            return serverAPIManager.getDataServiceAPI().getGenderTypes();
        }

        protected void onPostExecute(ArrayList<GenderType> genderTypes) {
            genderTypes.add(0, new GenderType(-2, "Gender"));
            updateGenderTypeSpinner(genderTypes);
        }
    }

    class GetCountriesAsyncTask extends AsyncTask<Void, Void, ArrayList<Country>> {
        @Override
        protected ArrayList<Country> doInBackground(Void... params) {
            return serverAPIManager.getDataServiceAPI().getCountries();
        }

        protected void onPostExecute(ArrayList<Country> countries) {
            countries.add(0, new Country(-2, "Country"));
            updateCountrySpinner(countries);
        }
    }

    class GetCitiesByCountryAsyncTask extends AsyncTask<Integer, Void, ArrayList<City>> {
        @Override
        protected ArrayList<City> doInBackground(Integer... params) {
            return serverAPIManager.getDataServiceAPI().getCitiesByCountry(params[0].intValue());
        }

        protected void onPostExecute(ArrayList<City> cities) {
            cities.add(0, new City(-2, "City", null));
            updateCitySpinner(cities);
        }
    }

    class GetUniversitiesByCityAsyncTask extends AsyncTask<Integer, Void, ArrayList<University>> {
        @Override
        protected ArrayList<University> doInBackground(Integer... params) {
            return serverAPIManager.getDataServiceAPI().getUniversitiesByCity(params[0].intValue());
        }

        protected void onPostExecute(ArrayList<University> universities) {
            universities.add(0, new University(-2, "University", null));
            updateUniversitySpinner(universities);
        }
    }

    class GetStatusesAsyncTask extends AsyncTask<Void, Void, ArrayList<Status>> {
        @Override
        protected ArrayList<com.example.arturrinkis.universitystudentrating.DTO.Status> doInBackground(Void... params) {
            return serverAPIManager.getDataServiceAPI().getStatuses();
        }

        protected void onPostExecute(ArrayList<com.example.arturrinkis.universitystudentrating.DTO.Status> statuses) {
            statuses.add(0, new com.example.arturrinkis.universitystudentrating.DTO.Status(-2, "Status"));
            updateStatusSpinner(statuses);
        }
    }

    class GetFacultiesByUniversityAsyncTask extends AsyncTask<Integer, Void, ArrayList<Faculty>> {
        @Override
        protected ArrayList<Faculty> doInBackground(Integer... params) {
            return serverAPIManager.getDataServiceAPI().getFacultiesByUniversity(params[0].intValue());
        }

        protected void onPostExecute(ArrayList<Faculty> faculties) {
            faculties.add(0, new Faculty(-2, "Faculty", null));
            updateFacultySpinner(faculties);
        }
    }

    class GetCoursesAsyncTask extends AsyncTask<Void, Void, ArrayList<Course>> {
        @Override
        protected ArrayList<Course> doInBackground(Void... params) {
            return serverAPIManager.getDataServiceAPI().getCourses();
        }

        protected void onPostExecute(ArrayList<Course> courses) {
            courses.add(0, new Course(-2, 0));
            updateCourseSpinner(courses);
        }
    }

    class SearchUsersAsyncTask extends AsyncTask<SearchModel, Void, Void> {
        @Override
        protected Void doInBackground(SearchModel... params) {
            ArrayList<UserProfile> searchedUsers = serverAPIManager.getSearchService().searchUsers(params[0]);
            ProfileCache.getInstance().setSearchUserResult(searchedUsers);
            return null;
        }

        protected void onPostExecute(Void result) {
            showSearchResult();
        }
    }


    private void cancelGetGenderTypesAsyncTask() {
        if (getGenderTypesAsyncTask == null) return;
        getGenderTypesAsyncTask.cancel(false);
    }

    private void cancelGetCountriesAsyncTask() {
        if (getCountriesAsyncTask == null) return;
        getCountriesAsyncTask.cancel(false);
    }

    private void cancelGetCitiesByCountryAsyncTask() {
        if (getCitiesByCountryAsyncTask == null) return;
        getCitiesByCountryAsyncTask.cancel(false);
    }

    private void cancelGetUniversitiesByCityAsyncTask() {
        if (getUniversitiesByCityAsyncTask == null) return;
        getUniversitiesByCityAsyncTask.cancel(false);
    }

    private void cancelGetStatusesAsyncTask() {
        if (getStatusesAsyncTask == null) return;
        getStatusesAsyncTask.cancel(false);
    }

    private void cancelGetFacultiesByUniversityAsyncTask() {
        if (getFacultiesByUniversityAsyncTask == null) return;
        getFacultiesByUniversityAsyncTask.cancel(false);
    }

    private void cancelGetCoursesAsyncTask() {
        if (getCoursesAsyncTask == null) return;
        getCoursesAsyncTask.cancel(false);
    }

    private void cancelSearchUsersAsyncTask() {
        if (searchUsersAsyncTask == null) return;
        searchUsersAsyncTask.cancel(false);
    }
}