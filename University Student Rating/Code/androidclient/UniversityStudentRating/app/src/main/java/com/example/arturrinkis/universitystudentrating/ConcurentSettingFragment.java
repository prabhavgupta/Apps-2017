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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arturrinkis.universitystudentrating.CacheData.ProfileCache;
import com.example.arturrinkis.universitystudentrating.DTO.City;
import com.example.arturrinkis.universitystudentrating.DTO.ConcurentModel;
import com.example.arturrinkis.universitystudentrating.DTO.ConcurentResult;
import com.example.arturrinkis.universitystudentrating.DTO.Discipline;
import com.example.arturrinkis.universitystudentrating.DTO.DisciplineBranch;
import com.example.arturrinkis.universitystudentrating.DTO.Faculty;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.IServerAPIManager;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.ServerAPIManager;

import java.util.ArrayList;


public class ConcurentSettingFragment extends Fragment {
    private String SETTING_SEARCH_FRAGMENT_TAG = "SETTING_SEARCH_FRAGMENT_TAG";
    private View view = null;
    private IServerAPIManager serverAPIManager = null;
    private boolean isServerRespondError = false;

    GetBranchesAsyncTask getBranchesAsyncTask = null;
    GetDisciplinesByBranchAsyncTask getDisciplinesByBranchAsyncTask = null;
    GetFacultiesByUniversityAsyncTask getFacultiesByUniversityAsyncTask = null;
    SearchConcurentsAsyncTask searchConcurentsAsyncTask = null;

    public ConcurentSettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        view = inflater.inflate(R.layout.fragment_concurent_setting, container, false);
        serverAPIManager = new ServerAPIManager(getActivity().getApplicationContext());
        initControls();
        getBranchesAsyncTask = new GetBranchesAsyncTask();
        getBranchesAsyncTask.execute();

        getFacultiesByUniversityAsyncTask = new GetFacultiesByUniversityAsyncTask();
        getFacultiesByUniversityAsyncTask.execute(ProfileCache.getInstance().getAuthUser().getUserProfile().getUniversityId());

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cancelGetBranchesAsyncTask();
        cancelGetDisciplinesByBranchAsyncTask();
        cancelGetFacultiesByUniversityAsyncTask();
        cancelSearchConcurentsAsyncTask();
    }


    private void initControls(){
        try {
            hideLoadingAnimation();
            showTabLayout();
            initMainHeaderTextView();
            initSearchButton();
            initSpinners();
        }
        catch(Exception e){
            Log.d("async task", "activity has been destroyed");
        }
    }

    private void showTabLayout(){
        TabLayout tabLayout = (TabLayout)getActivity().findViewById(R.id.main_tab);
        tabLayout.setVisibility(View.VISIBLE);
    }

    private void initMainHeaderTextView(){
        TextView main_header_tv = (TextView)getActivity().findViewById(R.id.main_header_tv);
        main_header_tv.setText("Concurent setting");
    }

    private void initSearchButton(){
        final Button searchButton = (Button)view.findViewById(R.id.search_btn);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchButton.setText("PLEASE WAIT...");
                ConcurentModel model = new ConcurentModel();

                Spinner branch_spinner = (Spinner)view.findViewById(R.id.branch_spinner);
                Spinner discipline_spinner = (Spinner)view.findViewById(R.id.discipline_spinner);
                Spinner faculty_spinner = (Spinner)view.findViewById(R.id.faculty_spinner);
                Spinner student_rating_type_spinner = (Spinner)view.findViewById(R.id.student_rating_type_spinner);

                if(((DisciplineBranch)branch_spinner.getSelectedItem()).getId() > 0)
                    model.setDisciplineBranchId(((DisciplineBranch)branch_spinner.getSelectedItem()).getId());
                if(((Discipline)discipline_spinner.getSelectedItem()).getId() > 0)
                    model.setDisciplineId(((Discipline)discipline_spinner.getSelectedItem()).getId());
                if(((Faculty)faculty_spinner.getSelectedItem()).getId() > 0)
                    model.setFacultyId(((Faculty)faculty_spinner.getSelectedItem()).getId());

                for(int i = 0; i < ProfileCache.getInstance().getStudentRatingTypes().size(); ++i) {
                    if(ProfileCache.getInstance().getStudentRatingTypes().get(i) == ((String) student_rating_type_spinner.getSelectedItem()))
                    model.setStudentRatingType(i+1);
                }

                model.setStudentId(ProfileCache.getInstance().getAuthUser().getUserProfileId());
                model.setCourseId(ProfileCache.getInstance().getAuthUser().getUserProfile().getCourseId());
                model.setUniversityId(ProfileCache.getInstance().getAuthUser().getUserProfile().getUniversityId());
                model.setSkipCount(0);
                model.setTakeCount(10);

                ProfileCache.getInstance().setSearchUserResult(null);
                ProfileCache.getInstance().setConcurentModel(model);
                searchConcurentsAsyncTask = new SearchConcurentsAsyncTask();
                searchConcurentsAsyncTask.execute(model);
            }
        });
    }

    //
    //
    //init spinners

    private void initSpinners() {
        initDisciplineBranchSpinner();
        initDisciplineSpinner();
        initFacultySpinner();
        initStudentRatingTypeSpinner();
    }

    private void initDisciplineBranchSpinner() {
        ArrayList<DisciplineBranch> disciplineBranches = new ArrayList<DisciplineBranch>() {{
            add(new DisciplineBranch(-1, "Loading...", null));
        }};
        ArrayAdapter<DisciplineBranch> adapter = new ArrayAdapter<DisciplineBranch>(getActivity(), android.R.layout.simple_spinner_item, disciplineBranches);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) view.findViewById(R.id.branch_spinner);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DisciplineBranch selected = (DisciplineBranch) parent.getItemAtPosition(position);
                updateDisciplineSpinner(new ArrayList<Discipline>() {{
                    add(new Discipline(-1, "Loading...", null, null));
                }});
                if (selected.getId() != -1 && selected.getId() != -2) {
                    getDisciplinesByBranchAsyncTask = new GetDisciplinesByBranchAsyncTask();
                    getDisciplinesByBranchAsyncTask.execute(selected.getId());
                }
                else if(selected.getId() == -2){
                    updateDisciplineSpinner(new ArrayList<Discipline>() {{
                        add(new Discipline(-2, "Any discipline", null, null));
                    }});
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    private void initDisciplineSpinner() {
        ArrayList<City> cities = new ArrayList<City>() {{
            add(new City(-1, "Loading...", null));
        }};
        ArrayAdapter<City> adapter = new ArrayAdapter<City>(getActivity(), android.R.layout.simple_spinner_item, cities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) view.findViewById(R.id.discipline_spinner);
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

    private void initStudentRatingTypeSpinner() {
        ArrayList<String> types = ProfileCache.getInstance().getStudentRatingTypes();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) view.findViewById(R.id.student_rating_type_spinner);
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

    private void hideLoadingAnimation() {
        RelativeLayout loading_layout = (RelativeLayout) getActivity().findViewById(R.id.loading_layout);
        loading_layout.setVisibility(View.GONE);
    }


    //
    //
    //update spinners

    private void updateDisciplineBranchSpinner(ArrayList<DisciplineBranch> disciplineBranches) {
        if(disciplineBranches == null && isServerRespondError == false){
            isServerRespondError = true;
            Toast.makeText(getActivity(), "Server is not responding", Toast.LENGTH_LONG).show();
        }
        else if (disciplineBranches != null) {
            Spinner spinner = (Spinner) view.findViewById(R.id.branch_spinner);
            ArrayAdapter<DisciplineBranch> spinnerAdapter = new ArrayAdapter<DisciplineBranch>(getActivity(), android.R.layout.simple_spinner_item, disciplineBranches);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);
            spinnerAdapter.notifyDataSetChanged();
        }
    }

    private void updateDisciplineSpinner(ArrayList<Discipline> disciplines) {
        if(disciplines == null && isServerRespondError == false){
            isServerRespondError = true;
            Toast.makeText(getActivity(), "Server is not responding", Toast.LENGTH_LONG).show();
        }
        else if (disciplines != null) {
            Spinner spinner = (Spinner) view.findViewById(R.id.discipline_spinner);
            ArrayAdapter<Discipline> spinnerAdapter = new ArrayAdapter<Discipline>(getActivity(), android.R.layout.simple_spinner_item, disciplines);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);
            spinnerAdapter.notifyDataSetChanged();
        }
    }

    private void updateFacultySpinner(ArrayList<Faculty> faculties) {
        if(faculties == null && isServerRespondError == false){
            isServerRespondError = true;
            Toast.makeText(getActivity(), "Server is not responding", Toast.LENGTH_LONG).show();
        }
        else if (faculties != null) {
            Spinner spinner = (Spinner) view.findViewById(R.id.faculty_spinner);
            ArrayAdapter<Faculty> spinnerAdapter = new ArrayAdapter<Faculty>(getActivity(), android.R.layout.simple_spinner_item, faculties);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);
            spinnerAdapter.notifyDataSetChanged();
        }
    }


    private void showConcurentResult() {
        try {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
            ConcurentFragment concurentFragment = new ConcurentFragment();
            FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.tab_container, concurentFragment);
            fragmentTransaction.addToBackStack(SETTING_SEARCH_FRAGMENT_TAG);
            fragmentTransaction.commit();
        } catch (Exception e) {
            Log.e("async task exeption", e.getMessage());
        }
    }

    //
    //
    //database async tasks

    class GetBranchesAsyncTask extends AsyncTask<Void, Void, ArrayList<DisciplineBranch>> {
        @Override
        protected ArrayList<DisciplineBranch> doInBackground(Void... params) {
            return serverAPIManager.getStatisticService().getDisciplineBranches();
        }
        protected void onPostExecute(ArrayList<DisciplineBranch> disciplineBranches){
            disciplineBranches.add(0, new DisciplineBranch(-2, "Any branch", null));
            updateDisciplineBranchSpinner(disciplineBranches);
        }
    }

    class GetDisciplinesByBranchAsyncTask extends AsyncTask<Integer, Void, ArrayList<Discipline>> {
        @Override
        protected ArrayList<Discipline> doInBackground(Integer... params) {
            return serverAPIManager.getStatisticService().getDisciplinesByBranch(params[0].intValue());
        }
        protected void onPostExecute(ArrayList<Discipline> disciplines){
            disciplines.add(0, new Discipline(-2, "Any discipline", null, null));
            updateDisciplineSpinner(disciplines);
        }
    }

    class GetFacultiesByUniversityAsyncTask extends AsyncTask<Integer, Void, ArrayList<Faculty>> {
        @Override
        protected ArrayList<Faculty> doInBackground(Integer... params) {
            return serverAPIManager.getDataServiceAPI().getFacultiesByUniversity(params[0].intValue());
        }
        protected void onPostExecute(ArrayList<Faculty> faculties){
            faculties.add(0, new Faculty(-2, "Any faculty", null));
            updateFacultySpinner(faculties);
        }
    }

    class SearchConcurentsAsyncTask extends AsyncTask<ConcurentModel, Void, Void> {
        @Override
        protected Void doInBackground(ConcurentModel... params) {
            ArrayList<ConcurentResult> concurentResults = serverAPIManager.getSearchService().searchConcurents(params[0]);
            ProfileCache.getInstance().setConcurentResults(concurentResults);
            return null;
        }
        protected void onPostExecute(Void result) {
            showConcurentResult();
        }
    }

    private void cancelGetBranchesAsyncTask(){
        if (getBranchesAsyncTask == null) return;
        getBranchesAsyncTask.cancel(false);
    }
    private void cancelGetDisciplinesByBranchAsyncTask(){
        if (getDisciplinesByBranchAsyncTask == null) return;
        getDisciplinesByBranchAsyncTask.cancel(false);
    }
    private void cancelGetFacultiesByUniversityAsyncTask(){
        if (getFacultiesByUniversityAsyncTask == null) return;
        getFacultiesByUniversityAsyncTask.cancel(false);
    }
    private void cancelSearchConcurentsAsyncTask(){
        if (searchConcurentsAsyncTask == null) return;
        searchConcurentsAsyncTask.cancel(false);
    }
}
