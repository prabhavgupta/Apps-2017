package com.example.arturrinkis.universitystudentrating;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.arturrinkis.universitystudentrating.CacheData.ProfileCache;
import com.example.arturrinkis.universitystudentrating.DTO.Rating;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.IServerAPIManager;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.ServerAPIManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class ProfessorRatingFragment extends Fragment {
    private String PROFESSOR_RATING_FRAGMENT_TAG = "PROFESSOR_RATING_FRAGMENT_TAG";
    private View view = null;
    private IServerAPIManager serverAPIManager = null;
    private ArrayList<TableRow> selectedRows = new ArrayList<>();
    private AlertDialog deleteDialog = null;
    private AlertDialog selectForDeleteDialog = null;
    private ProfessorSetRatingFragment professorSetRatingFragment = new ProfessorSetRatingFragment();
    private GetRatingsAsyncTask getRatingsAsyncTask = null;
    private DeteleRatingByIdsAsyncTask deteleRatingByIdsAsyncTask = null;
    private SimpleDateFormat dateFormat =  new SimpleDateFormat("dd MMMM yyyy");
    private int disciplineId;
    private int skipCount = 0;

    public ProfessorRatingFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        view = inflater.inflate(R.layout.fragment_set_rating, container, false);
        serverAPIManager = new ServerAPIManager(getActivity().getApplicationContext());

        initMainHeaderTextView();
        showLoadingAnimation();
        hideMainContent();

        disciplineId = getArguments().getInt("DisciplineId");

        if(savedInstanceState == null || ProfileCache.getInstance().getProfessorRatings() == null){
            getRatingsAsyncTask = new GetRatingsAsyncTask();
            getRatingsAsyncTask.execute(disciplineId);
        }
        else {
            initControls();
        }
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cancelGetRatingsAsyncTask();
        cancelDeteleRatingByIdsAsyncTask();
    }

    private void initControls(){
        try {
            initPrevButton();
            initNextButton();
            initDeleteBtn();
            initSetRatingBtn();
            initRatingsTable();
            hideLoadingAnimation();
            showMainContent();
        }
        catch (Exception e){

        }
    }

    private void initMainHeaderTextView(){
        String disciplineName = getArguments().getString("DisciplineName");
        TextView main_header_tv = (TextView)getActivity().findViewById(R.id.main_header_tv);
        main_header_tv.setText(disciplineName);
    }

    private void initDeleteBtn(){
        ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedRows.size() == 0){
                    initSelectForDeleteDialog();
                    selectForDeleteDialog.show();
                }
                else {
                    initDeleteDialog();
                    deleteDialog.show();
                }
            }
        });
    }

    private void initDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ArrayList<Integer> ratingIds = new ArrayList<Integer>();
                for(TableRow r : selectedRows){
                    String idStr = ((TextView)r.findViewById(R.id.rating_id_tv)).getText().toString();
                    ratingIds.add(Integer.parseInt(idStr));
                }

                showLoadingAnimation();
                hideMainContent();
                deteleRatingByIdsAsyncTask = new DeteleRatingByIdsAsyncTask();
                deteleRatingByIdsAsyncTask.execute(ratingIds);
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        builder.setMessage("Delete " + String.valueOf(selectedRows.size()) + " rows?")
                .setTitle("Delete");

        deleteDialog = builder.create();
    }

    private void initSelectForDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        builder.setMessage("No rows for delete")
                .setTitle("Delete");

        selectForDeleteDialog = builder.create();
    }

    private void initPrevButton(){
        ImageButton settingButton = (ImageButton)view.findViewById(R.id.prev_btn);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(skipCount >= ProfileCache.getInstance().getProfessorRatingTakeCount()){
                    skipCount -= ProfileCache.getInstance().getProfessorRatingTakeCount();
                    hideMainContent();
                    showLoadingAnimation();
                    getRatingsAsyncTask = new GetRatingsAsyncTask();
                    getRatingsAsyncTask.execute(disciplineId);
                }
            }
        });
    }

    private void initNextButton(){
        ImageButton settingButton = (ImageButton)view.findViewById(R.id.next_btn);
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ProfileCache.getInstance().getProfessorRatings().size() == ProfileCache.getInstance().getProfessorRatingTakeCount()){
                    skipCount += ProfileCache.getInstance().getProfessorRatingTakeCount();
                    hideMainContent();
                    showLoadingAnimation();
                    getRatingsAsyncTask = new GetRatingsAsyncTask();
                    getRatingsAsyncTask.execute(disciplineId);
                }
            }
        });
    }

    private void initSetRatingBtn(){
        ImageButton addBtn = (ImageButton)view.findViewById(R.id.add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putInt("DisciplineId", disciplineId);
                professorSetRatingFragment.setArguments(bundle);
                fragmentTransaction.addToBackStack(PROFESSOR_RATING_FRAGMENT_TAG);
                fragmentTransaction.replace(R.id.tab_container, professorSetRatingFragment);
                fragmentTransaction.commit();
            }
        });
    }

    private void initRatingsTable() {
        selectedRows.clear();
        TableLayout ratingsTableLayout = (TableLayout) view.findViewById(R.id.rating_table);
        int count = ratingsTableLayout.getChildCount();
        for (int i = count - 1; i > 0; --i) {
            View child = ratingsTableLayout.getChildAt(i);
            ratingsTableLayout.removeView(child);
        }

        if(ProfileCache.getInstance().getProfessorRatings().size() == 0){
            final TableRow row = (TableRow) LayoutInflater.from(getActivity()).inflate(R.layout.set_rating_empty_row, null);
            ratingsTableLayout.addView(row);
        }
        else {
            for (int i = 0; i < ProfileCache.getInstance().getProfessorRatings().size(); ++i) {
                Rating rating = ProfileCache.getInstance().getProfessorRatings().get(i);
                final TableRow row = (TableRow) LayoutInflater.from(getActivity()).inflate(R.layout.set_rating_row, null);
                ((TextView) row.findViewById(R.id.rating_id_tv)).setText(String.valueOf(rating.getId()));
                ((TextView) row.findViewById(R.id.date_tv)).setText(String.valueOf(dateFormat.format(rating.getDate())));
                ((TextView) row.findViewById(R.id.student_tv)).setText(rating.getUserProfile().getFirstName() + " " + rating.getUserProfile().getLastName() + "\n" + rating.getUserProfile().getCourse() + " course");
                ((TextView) row.findViewById(R.id.description_tv)).setText(rating.getName() == null || rating.getName() == "" ? "No desctiption" : rating.getName());
                ((TextView) row.findViewById(R.id.points_tv)).setText(String.valueOf(rating.getPoints()));

                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (selectedRows.indexOf(row) == -1) {
                            ((TextView) row.findViewById(R.id.rating_id_tv)).setBackgroundResource(R.drawable.rating_cell_back);
                            ((TextView) row.findViewById(R.id.date_tv)).setBackgroundResource(R.drawable.rating_cell_back);
                            ((TextView) row.findViewById(R.id.student_tv)).setBackgroundResource(R.drawable.rating_cell_back);
                            ((TextView) row.findViewById(R.id.description_tv)).setBackgroundResource(R.drawable.rating_cell_back);
                            ((TextView) row.findViewById(R.id.points_tv)).setBackgroundResource(R.drawable.rating_cell_back);
                            selectedRows.add(row);
                        } else {
                            ((TextView) row.findViewById(R.id.rating_id_tv)).setBackgroundResource(R.drawable.cell_shape);
                            ((TextView) row.findViewById(R.id.date_tv)).setBackgroundResource(R.drawable.cell_shape);
                            ((TextView) row.findViewById(R.id.student_tv)).setBackgroundResource(R.drawable.cell_shape);
                            ((TextView) row.findViewById(R.id.description_tv)).setBackgroundResource(R.drawable.cell_shape);
                            ((TextView) row.findViewById(R.id.points_tv)).setBackgroundResource(R.drawable.cell_shape);
                            selectedRows.remove(row);
                        }
                    }
                });

                ratingsTableLayout.addView(row);
            }
        }
        ratingsTableLayout.requestLayout();
    }

    private void showLoadingAnimation() {
        RelativeLayout loading_layout = (RelativeLayout) getActivity().findViewById(R.id.loading_layout);
        loading_layout.setVisibility(View.VISIBLE);
    }

    private void hideMainContent() {
        RelativeLayout search_layout = (RelativeLayout) view.findViewById(R.id.professor_rating);
        search_layout.setVisibility(View.GONE);
    }

    private void hideLoadingAnimation() {
        RelativeLayout loading_layout = (RelativeLayout) getActivity().findViewById(R.id.loading_layout);
        loading_layout.setVisibility(View.GONE);
    }

    private void showMainContent() {
        RelativeLayout search_layout = (RelativeLayout) view.findViewById(R.id.professor_rating);
        search_layout.setVisibility(View.VISIBLE);
    }

    //
    //
    //database async task

    class GetRatingsAsyncTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            int professorId = ProfileCache.getInstance().getAuthUser().getUserProfileId();
            int disciplineId = params[0].intValue();

            ArrayList<Rating> ratings = serverAPIManager.getStatisticService().getRatingsForProfessorByDiscipline(professorId, disciplineId, skipCount, ProfileCache.getInstance().getProfessorRatingTakeCount());
            ProfileCache.getInstance().setProfessorRatings(ratings);

            return null;
        }
        protected void onPostExecute(Void result) {
            initControls();
        }
    }

    class DeteleRatingByIdsAsyncTask extends AsyncTask<ArrayList<Integer>, Void, Void> {
        @Override
        protected Void doInBackground(ArrayList<Integer>... params) {
            serverAPIManager.getStatisticService().deleteRatingByIds(params[0]);
            return null;
        }

        protected void onPostExecute(Void result) {
            getRatingsAsyncTask = new GetRatingsAsyncTask();
            getRatingsAsyncTask.execute(disciplineId);
        }
    }

    private void cancelGetRatingsAsyncTask() {
        if (getRatingsAsyncTask == null) return;
        getRatingsAsyncTask.cancel(false);
    }

    private void cancelDeteleRatingByIdsAsyncTask() {
        if (deteleRatingByIdsAsyncTask == null) return;
        deteleRatingByIdsAsyncTask.cancel(false);
    }
}