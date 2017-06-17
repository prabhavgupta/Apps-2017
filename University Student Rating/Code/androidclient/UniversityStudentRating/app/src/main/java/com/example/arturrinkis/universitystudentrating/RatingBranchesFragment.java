package com.example.arturrinkis.universitystudentrating;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.arturrinkis.universitystudentrating.CacheData.ProfileCache;
import com.example.arturrinkis.universitystudentrating.CustomControls.Adapters.BranchAdapter;
import com.example.arturrinkis.universitystudentrating.DTO.DisciplineBranch;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.IServerAPIManager;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.ServerAPIManager;

import java.util.ArrayList;


public class RatingBranchesFragment extends Fragment {
    private String RATING_BRANCHES_FRAGMENT_TAG = "RATING_BRANCHES_FRAGMENT_TAG";
    private View view = null;
    private IServerAPIManager serverAPIManager = null;

    GetDisciplineBranchesAsyncTask getDisciplineBranchesAsyncTask = null;

    public RatingBranchesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        view =  inflater.inflate(R.layout.fragment_statistic_branches, container, false);
        serverAPIManager = new ServerAPIManager(getActivity().getApplicationContext());

        initMainHeaderTextView();
        hideMainContent();
        showLoadingAnimation();

        if(ProfileCache.getInstance().getDisciplineBranches() == null) {
            getDisciplineBranchesAsyncTask = new GetDisciplineBranchesAsyncTask();
            getDisciplineBranchesAsyncTask.execute();
        }
        else {
            initControls();
        }

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cancelGetDisciplineBranchesAsyncTask();
    }

    private void initControls() {
        try {
            initDisciplineBranchesListView();
            hideLoadingAnimation();
            showMainContent();
        } catch (NullPointerException e) {
            Log.e("asynch task", "Activity has been destroyed");
        }
    }

    private void initMainHeaderTextView(){
        TextView main_header_tv = (TextView)getActivity().findViewById(R.id.main_header_tv);
        main_header_tv.setText("Select branch");
    }

    private void initDisciplineBranchesListView(){
        final BranchAdapter branchAdapter = new BranchAdapter(view.getContext(), ProfileCache.getInstance().getDisciplineBranches());

        ListView branches_lv = (ListView) view.findViewById(R.id.branches_lv);
        branches_lv.setAdapter(branchAdapter);

        branches_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DisciplineBranch disciplineBranch = (DisciplineBranch)branchAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putInt("DisciplineBranchId", disciplineBranch.getId());
                RatingDisciplinesFragment ratingDisciplinesFragment = new RatingDisciplinesFragment();
                ratingDisciplinesFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.tab_container, ratingDisciplinesFragment);
                fragmentTransaction.addToBackStack(RATING_BRANCHES_FRAGMENT_TAG);
                fragmentTransaction.commit();
            }
        });
    }

    private void showLoadingAnimation() {
        RelativeLayout loading_layout = (RelativeLayout) getActivity().findViewById(R.id.loading_layout);
        loading_layout.setVisibility(View.VISIBLE);
    }

    private void hideMainContent() {
        ListView branches_lv = (ListView) view.findViewById(R.id.branches_lv);
        branches_lv.setVisibility(View.GONE);
    }

    private void hideLoadingAnimation() {
        RelativeLayout loading_layout = (RelativeLayout) getActivity().findViewById(R.id.loading_layout);
        loading_layout.setVisibility(View.GONE);
    }

    private void showMainContent(){
        ListView branches_lv = (ListView) view.findViewById(R.id.branches_lv);
        branches_lv.setVisibility(View.VISIBLE);
    }

    //
    //
    //database async tasks

    class GetDisciplineBranchesAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            if(ProfileCache.getInstance().getDisciplineBranches() == null) {
                ArrayList<DisciplineBranch> disciplineBranches = serverAPIManager.getStatisticService().getDisciplineBranches();
                ProfileCache.getInstance().setDisciplineBranches(disciplineBranches);
            }
            return null;
        }
        protected void onPostExecute(Void result){
            initControls();
        }
    }

    private void cancelGetDisciplineBranchesAsyncTask(){
        if (getDisciplineBranchesAsyncTask == null) return;
        getDisciplineBranchesAsyncTask.cancel(false);
    }
}
