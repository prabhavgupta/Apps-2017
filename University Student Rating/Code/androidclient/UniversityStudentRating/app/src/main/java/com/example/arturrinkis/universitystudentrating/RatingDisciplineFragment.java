package com.example.arturrinkis.universitystudentrating;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.arturrinkis.universitystudentrating.CacheData.ProfileCache;
import com.example.arturrinkis.universitystudentrating.CustomControls.Adapters.StudentRatingAdapter;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.Interfaces.IServerAPIManager;
import com.example.arturrinkis.universitystudentrating.ServerServiceAPI.ServerAPIManager;
import com.example.arturrinkis.universitystudentrating.Utilities.StudentRatingType;
import com.example.arturrinkis.universitystudentrating.DTO.IndividualRating;
import com.example.arturrinkis.universitystudentrating.DTO.Rating;
import com.example.arturrinkis.universitystudentrating.DTO.RatingType;
import com.example.arturrinkis.universitystudentrating.Utilities.DataUtility;
import com.example.arturrinkis.universitystudentrating.Utilities.RatingMonth;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class RatingDisciplineFragment extends Fragment {
    private View view = null;
    private IServerAPIManager serverAPIManager = null;
    private GetIndividualRatingAsyncTask getIndividualRatingAsyncTask = null;
    private DataUtility dataUtility = null;
    private SimpleDateFormat dateFormat =  new SimpleDateFormat("dd MMMM yyyy", Locale.US);
    private int disciplineBranchId = 0;
    private ArrayList<Integer> colors = new ArrayList<Integer>();

    public RatingDisciplineFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        view = inflater.inflate(R.layout.fragment_discipline_statistic, container, false);
        serverAPIManager = new ServerAPIManager(getActivity().getApplicationContext());

        initMainHeaderTextView();
        initChartColors();
        hideMainContent();
        showLoadingAnimation();

        dataUtility = new DataUtility();
        disciplineBranchId = getArguments().getInt("DisciplineBranchId");
        int disciplineId = getArguments().getInt("DisciplineId");

        if(savedInstanceState == null || ProfileCache.getInstance().getIndividualRating() == null || ProfileCache.getInstance().getAverageClassTopStudents() == null ||
           ProfileCache.getInstance().getOverallTopStudents() == null || ProfileCache.getInstance().getOlympiadsTopStudents() == null || ProfileCache.getInstance().getRatingType() == null) {
            getIndividualRatingAsyncTask = new GetIndividualRatingAsyncTask();
            getIndividualRatingAsyncTask.execute(disciplineId);
        }
        else {
            initControls();
        }

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cancelGetIndividualRatingAsyncTask();
    }

    private void initChartColors(){
        colors.add(Color.BLUE);
        colors.add(Color.CYAN);
        colors.add(Color.GRAY);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.RED);
        colors.add(Color.BLACK);
        colors.add(Color.MAGENTA);
        colors.add(Color.DKGRAY);
        colors.add(Color.LTGRAY);
        colors.add(Color.argb(1,122,165,190));
        colors.add(Color.argb(1,202,15,10));
    }

    private void initControls(){
        try {
            initAverageClassRating();
            initRatingBarControls();
            initTotalClassRating();
            initOverallRatingChart();
            initOlympiadsTable();
            initAverageClassRatingByMonthes();
            initAverageClassTopRatingChart();
            initAverageClassTopRatingsListView();
            initAverageClassTopRatingLineChartByMonthes();
            initAverageClassTopRatingRadarChartByMonthes();
            initOverallTopRatingChart();
            initOverallTopRatingsListView();
            initOverallTopRatingLineChartByMonthes();
            initOverallTopRatingRadarChartByMonthes();
            initOlympiadsTopRatingChart();
            initOlympiadsTopRatingsListView();
            initOlympiadsTopRatingRadarChartByMonthes();
            initOlympiadsTopRatingLineChartByMonthes();

            hideLoadingAnimation();
            showMainContent();
        }
        catch(NullPointerException e){
            Log.e("asynch task", "Activity has been destroyed");
        }
    }

    private void initMainHeaderTextView(){
        String disciplineName = getArguments().getString("DisciplineName");
        TextView main_header_tv = (TextView)getActivity().findViewById(R.id.main_header_tv);
        main_header_tv.setText(disciplineName);
    }

    private void initAverageClassRating(){
        TextView ratingBarHeader_tv = (TextView)view.findViewById(R.id.ratingBarHeader_tv);
        ratingBarHeader_tv.setText(Double.toString(ProfileCache.getInstance().getIndividualRating().getAverageClassRating()));
    }

    private void initRatingBarControls(){
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar_indicator);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(1).setColorFilter(Color.rgb(255,170,109), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(2).setColorFilter(Color.rgb(255,93,119), PorterDuff.Mode.SRC_ATOP);
        ratingBar.setStepSize(0.01f);

        RatingBar ratingBar_indicator = (RatingBar)view.findViewById(R.id.ratingBar_indicator);
        ratingBar_indicator.setRating((float) ProfileCache.getInstance().getIndividualRating().getAverageClassRating()/2);
    }

    private void initTotalClassRating(){
        TextView ratingBarTotal_tv = (TextView) view.findViewById(R.id.ratingBarTotal_tv);
        ratingBarTotal_tv.setText(Integer.toString(ProfileCache.getInstance().getIndividualRating().getTotalClassRating()) + " total");
    }

    private void initOverallRatingChart(){
        PieChart pieChart = (PieChart) view.findViewById(R.id.overall_rating_piechart);

        ArrayList<Entry> ratingData = new ArrayList<Entry>();

        ratingData.add(new Entry(ProfileCache.getInstance().getIndividualRating().getTotalClassRating(), 0));
        ratingData.add(new Entry(ProfileCache.getInstance().getIndividualRating().getTotalOlympiadsRating(), 1));
        ratingData.add(new Entry(ProfileCache.getInstance().getIndividualRating().getTotalScippingClassRating(), 2));

        PieDataSet dataSet = new PieDataSet(ratingData, "");
        dataSet.setSliceSpace(1f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("Classwork");
        xVals.add("Olympiads");
        xVals.add("Forfeiture");

        PieData data = new PieData(xVals, dataSet);

        pieChart.setDescription("");
        pieChart.getLegend().setWordWrapEnabled(true);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    private void initOlympiadsTable(){
        TableLayout olympiadsTableLayout = (TableLayout)view.findViewById(R.id.olympiads_table);

        if(ProfileCache.getInstance().getIndividualRating().getOlympiadsRatings().size() == 0){
            TableRow row = (TableRow) LayoutInflater.from(getActivity()).inflate(R.layout.olympiads_rating_empty_row, null);
            olympiadsTableLayout.addView(row);
        }
        else {
            for (Rating r : ProfileCache.getInstance().getIndividualRating().getOlympiadsRatings()) {
                TableRow row = (TableRow) LayoutInflater.from(getActivity()).inflate(R.layout.olympiads_rating_row, null);
                ((TextView) row.findViewById(R.id.points)).setText(String.valueOf(r.getPoints()));
                ((TextView) row.findViewById(R.id.description)).setText(String.valueOf(r.getName()));
                ((TextView) row.findViewById(R.id.date)).setText(String.valueOf(dateFormat.format(r.getDate())));
                olympiadsTableLayout.addView(row);
            }
        }
        olympiadsTableLayout.requestLayout();
    }

    private void initAverageClassRatingByMonthes(){
        LineChart lineChart = (LineChart) view.findViewById(R.id.average_rating_linechart);

        ArrayList<Rating> ratings = ProfileCache.getInstance().getIndividualRating().getClassRatings();
        ArrayList<RatingMonth> ratingByMonthes = dataUtility.getRatingByMonthes(ratings, StudentRatingType.Average);
        ArrayList<Entry> entries = new ArrayList();
        ArrayList<String> labels = new ArrayList<String>();

        for(int i = 0; i < ratingByMonthes.size(); ++i)
        {
            entries.add(new Entry(ratingByMonthes.get(i).getValue(), i));
            labels.add(String.valueOf(ratingByMonthes.get(i).getMonth()));
        }

        LineDataSet dataset1 = new LineDataSet(entries, "Average point");
        dataset1.setColor(Color.GREEN);
        dataset1.setDrawValues(false);
        dataset1.setLineWidth(1f);
        dataset1.setDrawCircles(false);

        LineData data = new LineData(labels);
        data.addDataSet(dataset1);
        lineChart.getAxisLeft().setAxisMaxValue(ProfileCache.getInstance().getRatingType().getMaxPoints()+1);
        lineChart.getAxisLeft().setAxisMinValue(ProfileCache.getInstance().getRatingType().getMinPoints()-1);
        lineChart.getAxisRight().setAxisMaxValue(ProfileCache.getInstance().getRatingType().getMaxPoints()+1);
        lineChart.getAxisRight().setAxisMinValue(ProfileCache.getInstance().getRatingType().getMinPoints()-1);
        lineChart.setData(data);
        lineChart.setDescription("");
        lineChart.getLegend().setWordWrapEnabled(true);
        lineChart.invalidate();
    }


    private void initAverageClassTopRatingChart(){
        PieChart pieChart = (PieChart) view.findViewById(R.id.average_classwork_top_rating_piechart);

        ArrayList<Entry> ratingData = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        for(int i = 0; i < ProfileCache.getInstance().getAverageClassTopStudents().size(); ++i){
            IndividualRating individualRating = ProfileCache.getInstance().getAverageClassTopStudents().get(i);
            ratingData.add(new Entry((float)individualRating.getAverageClassRating(), 0));
            xVals.add(individualRating.getUserProfile().getFirstName() + " " + individualRating.getUserProfile().getLastName());
        }

        PieDataSet dataSet = new PieDataSet(ratingData, "");
        dataSet.setSliceSpace(1f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData data = new PieData(xVals, dataSet);

        pieChart.setDescription("");
        pieChart.setData(data);
        pieChart.getLegend().setWordWrapEnabled(true);
        pieChart.invalidate();
    }

    private void initAverageClassTopRatingsListView(){
        final StudentRatingAdapter studentRatingAdapter = new StudentRatingAdapter(view.getContext(), ProfileCache.getInstance().getAverageClassTopStudents(), StudentRatingType.Average);

        ListView concurents_lv = (ListView) view.findViewById(R.id.average_classwork_top_rating_lv);
        concurents_lv.setAdapter(studentRatingAdapter);

        concurents_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //show profile
            }
        });
    }

    private void initAverageClassTopRatingRadarChartByMonthes(){
        ArrayList<ArrayList<Entry>> ratingsData = new ArrayList<>();
        ArrayList<RadarDataSet> dataSets = new ArrayList<RadarDataSet>();

        for(int i = 0; i < ProfileCache.getInstance().getAverageClassTopStudents().size(); ++i){
            IndividualRating individualRating = ProfileCache.getInstance().getAverageClassTopStudents().get(i);
            ArrayList<RatingMonth> ratingByMonthes = dataUtility.getRatingByMonthes(individualRating.getClassRatings(), StudentRatingType.Average);
            ArrayList<Entry> ratingData = new ArrayList<Entry>();

            for(int j = 0; j < ratingByMonthes.size(); ++j)
            {
                ratingData.add(new Entry(ratingByMonthes.get(j).getValue(), j));
            }
            RadarDataSet dataSet = new RadarDataSet(ratingData, individualRating.getUserProfile().getFirstName() + " " + individualRating.getUserProfile().getLastName());
            dataSet.setColor(colors.get(i));
            dataSet.setDrawFilled(true);
            dataSet.setDrawValues(false);
            dataSets.add(dataSet);
        }

        ArrayList<String> labels = new ArrayList<String>();
        for(int j = 0; j < 12; ++j) {
            labels.add(String.valueOf(dataUtility.getMonthForInt(j)));
        }

        RadarData data = new RadarData(labels, dataSets);
        RadarChart radarChart = (RadarChart) view.findViewById(R.id.average_top_rating_radarchart);
        radarChart.setData(data);
        radarChart.setWebLineWidthInner(0.5f);
        radarChart.getLegend().setWordWrapEnabled(true);
        radarChart.invalidate();
        radarChart.animate();
    }

    private void initAverageClassTopRatingLineChartByMonthes(){
        ArrayList<ArrayList<Entry>> ratingsData = new ArrayList<>();
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

        for(int i = 0; i < ProfileCache.getInstance().getAverageClassTopStudents().size(); ++i){
            IndividualRating individualRating = ProfileCache.getInstance().getAverageClassTopStudents().get(i);
            ArrayList<RatingMonth> ratingByMonthes = dataUtility.getRatingByMonthes(individualRating.getClassRatings(), StudentRatingType.Average);
            ArrayList<Entry> ratingData = new ArrayList<Entry>();

            for(int j = 0; j < ratingByMonthes.size(); ++j)
            {
                ratingData.add(new Entry(ratingByMonthes.get(j).getValue(), j));
            }
            LineDataSet dataSet = new LineDataSet(ratingData, individualRating.getUserProfile().getFirstName() + " " + individualRating.getUserProfile().getLastName());
            dataSet.setColor(colors.get(i));
            dataSet.setDrawValues(false);
            dataSet.setDrawCircles(false);
            dataSets.add(dataSet);
        }

        ArrayList<String> labels = new ArrayList<String>();
        for(int j = 0; j < 12; ++j) {
            labels.add(String.valueOf(dataUtility.getMonthForInt(j)));
        }

        LineData data = new LineData(labels, dataSets);
        LineChart lineChart = (LineChart) view.findViewById(R.id.average_top_rating_linechart);
        lineChart.setData(data);
        lineChart.getLegend().setWordWrapEnabled(true);
        lineChart.invalidate();
        lineChart.animate();
    }


    private void initOlympiadsTopRatingChart(){
        PieChart pieChart = (PieChart) view.findViewById(R.id.olympiads_top_rating_piechart);

        ArrayList<Entry> ratingData = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        for(int i = 0; i < ProfileCache.getInstance().getOlympiadsTopStudents().size(); ++i){
            IndividualRating individualRating = ProfileCache.getInstance().getOlympiadsTopStudents().get(i);
            ratingData.add(new Entry(individualRating.getTotalOlympiadsRating(), 0));
            xVals.add(individualRating.getUserProfile().getFirstName() + " " + individualRating.getUserProfile().getLastName());
        }

        PieDataSet dataSet = new PieDataSet(ratingData, "");
        dataSet.setSliceSpace(1f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData data = new PieData(xVals, dataSet);

        pieChart.setDescription("");
        pieChart.getLegend().setWordWrapEnabled(true);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    private void initOlympiadsTopRatingsListView(){
        final StudentRatingAdapter studentRatingAdapter = new StudentRatingAdapter(view.getContext(), ProfileCache.getInstance().getOlympiadsTopStudents(), StudentRatingType.Olympiads);

        ListView concurents_lv = (ListView) view.findViewById(R.id.olympiads_top_rating_lv);
        concurents_lv.setAdapter(studentRatingAdapter);

        concurents_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //show profile
            }
        });
    }

    private void initOlympiadsTopRatingRadarChartByMonthes(){
        ArrayList<ArrayList<Entry>> ratingsData = new ArrayList<>();
        ArrayList<RadarDataSet> dataSets = new ArrayList<RadarDataSet>();

        for(int i = 0; i < ProfileCache.getInstance().getOlympiadsTopStudents().size(); ++i){
            IndividualRating individualRating = ProfileCache.getInstance().getOlympiadsTopStudents().get(i);
            ArrayList<RatingMonth> ratingByMonthes = dataUtility.getRatingByMonthes(individualRating.getOlympiadsRatings(), StudentRatingType.Olympiads);
            ArrayList<Entry> ratingData = new ArrayList<Entry>();

            for(int j = 0; j < ratingByMonthes.size(); ++j)
            {
                ratingData.add(new Entry(ratingByMonthes.get(j).getValue(), j));
            }
            RadarDataSet dataSet = new RadarDataSet(ratingData, individualRating.getUserProfile().getFirstName() + " " + individualRating.getUserProfile().getLastName());
            dataSet.setColor(colors.get(i));
            dataSet.setDrawFilled(true);
            dataSet.setDrawValues(false);
            dataSets.add(dataSet);
        }

        ArrayList<String> labels = new ArrayList<String>();
        for(int j = 0; j < 12; ++j) {
            labels.add(String.valueOf(dataUtility.getMonthForInt(j)));
        }

        RadarData data = new RadarData(labels, dataSets);
        RadarChart radarChart = (RadarChart) view.findViewById(R.id.olympiads_top_rating_radarchart);
        radarChart.setData(data);
        radarChart.setWebLineWidthInner(0.5f);
        radarChart.getLegend().setWordWrapEnabled(true);
        radarChart.invalidate();
        radarChart.animate();
    }

    private void initOlympiadsTopRatingLineChartByMonthes(){
        ArrayList<ArrayList<Entry>> ratingsData = new ArrayList<>();
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

        for(int i = 0; i < ProfileCache.getInstance().getOlympiadsTopStudents().size(); ++i){
            IndividualRating individualRating = ProfileCache.getInstance().getOlympiadsTopStudents().get(i);
            ArrayList<RatingMonth> ratingByMonthes = dataUtility.getRatingByMonthes(individualRating.getOlympiadsRatings(), StudentRatingType.Olympiads);
            ArrayList<Entry> ratingData = new ArrayList<Entry>();

            for(int j = 0; j < ratingByMonthes.size(); ++j)
            {
                ratingData.add(new Entry(ratingByMonthes.get(j).getValue(), j));
            }
            LineDataSet dataSet = new LineDataSet(ratingData, individualRating.getUserProfile().getFirstName() + " " + individualRating.getUserProfile().getLastName());
            dataSet.setColor(colors.get(i));
            dataSet.setDrawValues(false);
            dataSet.setDrawCircles(false);
            dataSets.add(dataSet);
        }

        ArrayList<String> labels = new ArrayList<String>();
        for(int j = 0; j < 12; ++j) {
            labels.add(String.valueOf(dataUtility.getMonthForInt(j)));
        }

        LineData data = new LineData(labels, dataSets);
        LineChart lineChart = (LineChart) view.findViewById(R.id.olympiads_top_rating_linechart);
        lineChart.setData(data);
        lineChart.getLegend().setWordWrapEnabled(true);
        lineChart.invalidate();
        lineChart.animate();
    }


    private void initOverallTopRatingChart(){
        PieChart pieChart = (PieChart) view.findViewById(R.id.overall_top_rating_piechart);

        ArrayList<Entry> ratingData = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        for(int i = 0; i < ProfileCache.getInstance().getOverallTopStudents().size(); ++i){
            IndividualRating individualRating = ProfileCache.getInstance().getOverallTopStudents().get(i);
            ratingData.add(new Entry(individualRating.getTotalClassRating(), 0));
            xVals.add(individualRating.getUserProfile().getFirstName() + " " + individualRating.getUserProfile().getLastName());
        }

        PieDataSet dataSet = new PieDataSet(ratingData, "");
        dataSet.setSliceSpace(1f);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData data = new PieData(xVals, dataSet);

        pieChart.setDescription("");
        pieChart.getLegend().setWordWrapEnabled(true);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    private void initOverallTopRatingsListView(){
        final StudentRatingAdapter studentRatingAdapter = new StudentRatingAdapter(view.getContext(), ProfileCache.getInstance().getOverallTopStudents(), StudentRatingType.Overall);

        ListView concurents_lv = (ListView) view.findViewById(R.id.overall_top_rating_lv);
        concurents_lv.setAdapter(studentRatingAdapter);

        concurents_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //show profile
            }
        });
    }

    private void initOverallTopRatingRadarChartByMonthes(){
        ArrayList<ArrayList<Entry>> ratingsData = new ArrayList<>();
        ArrayList<RadarDataSet> dataSets = new ArrayList<RadarDataSet>();

        for(int i = 0; i < ProfileCache.getInstance().getOverallTopStudents().size(); ++i){
            IndividualRating individualRating = ProfileCache.getInstance().getOverallTopStudents().get(i);
            ArrayList<RatingMonth> ratingByMonthes = dataUtility.getRatingByMonthes(individualRating.getClassRatings(), StudentRatingType.Overall);
            ArrayList<Entry> ratingData = new ArrayList<Entry>();

            for(int j = 0; j < ratingByMonthes.size(); ++j)
            {
                ratingData.add(new Entry(ratingByMonthes.get(j).getValue(), j));
            }
            RadarDataSet dataSet = new RadarDataSet(ratingData, individualRating.getUserProfile().getFirstName() + " " + individualRating.getUserProfile().getLastName());
            dataSet.setColor(colors.get(i));
            dataSet.setDrawFilled(true);
            dataSet.setDrawValues(false);
            dataSets.add(dataSet);
        }

        ArrayList<String> labels = new ArrayList<String>();
        for(int j = 0; j < 12; ++j) {
            labels.add(String.valueOf(dataUtility.getMonthForInt(j)));
        }

        RadarData data = new RadarData(labels, dataSets);
        RadarChart radarChart = (RadarChart) view.findViewById(R.id.overall_top_rating_radarchart);
        radarChart.setData(data);
        radarChart.setWebLineWidthInner(0.5f);
        radarChart.getLegend().setWordWrapEnabled(true);
        radarChart.invalidate();
        radarChart.animate();
    }

    private void initOverallTopRatingLineChartByMonthes(){
        ArrayList<ArrayList<Entry>> ratingsData = new ArrayList<>();
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();

        for(int i = 0; i < ProfileCache.getInstance().getOverallTopStudents().size(); ++i){
            IndividualRating individualRating = ProfileCache.getInstance().getOverallTopStudents().get(i);
            ArrayList<RatingMonth> ratingByMonthes = dataUtility.getRatingByMonthes(individualRating.getClassRatings(), StudentRatingType.Overall);
            ArrayList<Entry> ratingData = new ArrayList<Entry>();

            for(int j = 0; j < ratingByMonthes.size(); ++j)
            {
                ratingData.add(new Entry(ratingByMonthes.get(j).getValue(), j));
            }
            LineDataSet dataSet = new LineDataSet(ratingData, individualRating.getUserProfile().getFirstName() + " " + individualRating.getUserProfile().getLastName());
            dataSet.setColor(colors.get(i));
            dataSet.setDrawValues(false);
            dataSet.setDrawCircles(false);
            dataSets.add(dataSet);
        }

        ArrayList<String> labels = new ArrayList<String>();
        for(int j = 0; j < 12; ++j) {
            labels.add(String.valueOf(dataUtility.getMonthForInt(j)));
        }

        LineData data = new LineData(labels, dataSets);
        LineChart lineChart = (LineChart) view.findViewById(R.id.overall_top_rating_linechart);
        lineChart.setData(data);
        lineChart.getLegend().setWordWrapEnabled(true);
        lineChart.invalidate();
        lineChart.animate();
    }


    private void showLoadingAnimation(){
        RelativeLayout loading_layout = (RelativeLayout) getActivity().findViewById(R.id.loading_layout);
        loading_layout.setVisibility(View.VISIBLE);
    }

    private void hideMainContent(){
        LinearLayout content_layout = (LinearLayout) view.findViewById(R.id.content_layout);
        content_layout.setVisibility(View.GONE);
    }

    private void hideLoadingAnimation(){
        RelativeLayout loading_layout = (RelativeLayout) getActivity().findViewById(R.id.loading_layout);
        loading_layout.setVisibility(View.GONE);
    }

    private void showMainContent(){
        LinearLayout content_layout = (LinearLayout) view.findViewById(R.id.content_layout);
        content_layout.setVisibility(View.VISIBLE);
    }

    //
    //
    //database async task

    class GetIndividualRatingAsyncTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            int studentId = ProfileCache.getInstance().getAuthUser().getUserProfileId();
            int courseId = ProfileCache.getInstance().getAuthUser().getUserProfile().getCourseId();
            int universityId = ProfileCache.getInstance().getAuthUser().getUserProfile().getUniversityId();
            int disciplineId = params[0].intValue();

            IndividualRating individualRating = serverAPIManager.getStatisticService().getIndividualRating(studentId, disciplineId);
            ProfileCache.getInstance().setIndividualRating(individualRating);

            ArrayList<IndividualRating> averageClassTopStudents = serverAPIManager.getStatisticService().getAverageClassTopStudentsBy(universityId, courseId, disciplineId, ProfileCache.getInstance().getTopCount());
            ProfileCache.getInstance().setAverageClassTopStudents(averageClassTopStudents);

            ArrayList<IndividualRating> overallTopStudents = serverAPIManager.getStatisticService().getOverallTopStudentsBy(universityId, courseId, disciplineId, ProfileCache.getInstance().getTopCount());
            ProfileCache.getInstance().setOverallTopStudents(overallTopStudents);

            ArrayList<IndividualRating> olympiadsTopStudents = serverAPIManager.getStatisticService().getOlympiadsTopStudentsBy(universityId, courseId, disciplineId, ProfileCache.getInstance().getTopCount());
            ProfileCache.getInstance().setOlympiadsTopStudents(olympiadsTopStudents);

            RatingType ratingType = serverAPIManager.getStatisticService().getRatingType(universityId, disciplineId);
            ProfileCache.getInstance().setRatingType(ratingType);
            return null;
        }
        protected void onPostExecute(Void result) {
            initControls();
        }
    }

    private void cancelGetIndividualRatingAsyncTask() {
        if (getIndividualRatingAsyncTask == null) return;
        getIndividualRatingAsyncTask.cancel(false);
    }
}