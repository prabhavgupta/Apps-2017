package hr.ferit.mdudjak.healthdiary;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class SymptomStatistics extends AppCompatActivity implements View.OnClickListener{
    Button bSymptom, bBody,bDescriptionPerArea, bSymptomThroughTime;
    BarChart barChart;
    ArrayList<Symptom> symptomLogs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_statistics);
        this.setUpUI();
    }

    private void setUpUI() {
        this.bSymptom= (Button) this.findViewById(R.id.bSymptomStatistics);
        this.bBody= (Button) this.findViewById(R.id.bBodyStatistics);
        this.bDescriptionPerArea= (Button) this.findViewById(R.id.bDescriptionPerArea);
        this.bSymptomThroughTime= (Button) this.findViewById(R.id.bSymptomsIntensityPerDates);
        this.bBody.setOnClickListener(this);
        this.bSymptom.setOnClickListener(this);
        this.bDescriptionPerArea.setOnClickListener(this);
        this.bSymptomThroughTime.setOnClickListener(this);
        symptomLogs = DBHelper.getInstance(this).getAllSymptoms();
        this.setUpUserActivityChart();
    }

    private void setUpUserActivityChart() {
        barChart = (BarChart) findViewById(R.id.bargraph);
        ArrayList<String> dates = new ArrayList<>();
        for(int i=0;i<symptomLogs.size();i++){
            boolean same = false;
            for(int j=0;j<i;j++) {
                if (symptomLogs.get(i).getDate().equals(symptomLogs.get(j).getDate())){
                    same=true;
                    break;
                }
            }
            if(!same) {
                dates.add(symptomLogs.get(i).getDate());
            }
        }

        int[] numberOfLogsPerDate = new int[dates.size()];
        for(int j=0;j<dates.size();j++){
            for(int i=0;i<symptomLogs.size();i++){
                if(symptomLogs.get(i).getDate().equals(dates.get(j))){
                    numberOfLogsPerDate[j]=numberOfLogsPerDate[j]+1;
                }
                }
            }

         ArrayList<BarEntry> barEntries = new ArrayList<>();
            for(int j = 0; j< dates.size();j++) {
                barEntries.add(new BarEntry(numberOfLogsPerDate[j], j));
            }
        BarDataSet barDataSet = new BarDataSet(barEntries,"Dates");
        BarData barData = new BarData(dates,barDataSet);
        barChart.setData(barData);
        barChart.setDescription("Number of symptom logs per date");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bBodyStatistics:
                Intent intent= new Intent(this,BodyStatistics.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.bSymptomStatistics:
                break;
            case R.id.bDescriptionPerArea:
                Intent DescriptionsStatistic= new Intent(this,DescriptionPerArea.class);
                DescriptionsStatistic.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(DescriptionsStatistic);
                finish();
                break;
            case R.id.bSymptomsIntensityPerDates:
                Intent Intensityintent= new Intent(this,SymptomsThroughTime.class);
                Intensityintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(Intensityintent);
                finish();
                break;
        }
    }
}
