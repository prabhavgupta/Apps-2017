package hr.ferit.mdudjak.healthdiary;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class SymptomsThroughTime extends AppCompatActivity implements View.OnClickListener {
    Button bPieCharts, bLineCharts;
    ListView lSymptomsStatistics;
    SymptomsStatisticsAdapter mSymptomStatisticsAdapter;
    ArrayList<LineData> mLineData;
    ArrayList<String> mChartTitles;
    ArrayList<String> mAreas;
    ArrayList<String> mDescriptions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms_through_time);
        this.setUpUI();
    }
    private void setUpUI() {
        this.lSymptomsStatistics = (ListView) this.findViewById(R.id.lvSymptomStatistics);
        this.bPieCharts =(Button) this.findViewById(R.id.bPieCharts);
        this.bLineCharts= (Button) this.findViewById(R.id.bLineCharts);
        this.bPieCharts.setOnClickListener(this);
        this.bLineCharts.setOnClickListener(this);
        ArrayList<Symptom> symptomLogs = DBHelper.getInstance(this).getAllSymptoms();
        this.mChartTitles= this.getChartTitles(symptomLogs);
        this.mLineData= this.makeLineCharts(symptomLogs);
        this.mSymptomStatisticsAdapter = new SymptomsStatisticsAdapter(mLineData,mChartTitles);
        this.lSymptomsStatistics.setAdapter(mSymptomStatisticsAdapter);
    }

    public ArrayList<LineData> makeLineCharts(ArrayList<Symptom> symptomLogs) {
        ArrayList<LineData> lineCharts = new ArrayList<>();

        ArrayList<ArrayList<Entry>> chartFunctions = new ArrayList<>();
        ArrayList<ArrayList<String>> arrayOfAxes = new ArrayList<>();

        for (int j = 0; j < mChartTitles.size(); j++){
            ArrayList<Entry> function = new ArrayList<>();
            ArrayList<String> xAXES = new ArrayList<>();

            for (int i = 0; i < symptomLogs.size(); i++) {
                if (symptomLogs.get(i).getArea().equals(mAreas.get(j)) && symptomLogs.get(i).getDescription().equals(mDescriptions.get(j))) {
                    function.add(new Entry(symptomLogs.get(i).getIntensity(), i));
                    xAXES.add(symptomLogs.get(i).getDate());
                }
            }
        arrayOfAxes.add(xAXES);
        chartFunctions.add(function);
        }

        for(int i=0;i<mChartTitles.size();i++) {

            String[] xaxes = new String[arrayOfAxes.get(i).size()];

            for(int j=0; j<arrayOfAxes.get(i).size();j++){
                xaxes[j] = arrayOfAxes.get(i).get(j).toString();
            }

            LineDataSet lineDataSet = new LineDataSet(chartFunctions.get(i), mChartTitles.get(i));
            lineDataSet.setDrawCircles(false);
            lineDataSet.setColor(Color.BLUE);
            lineCharts.add(new LineData(xaxes, lineDataSet));
        }

        return lineCharts;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bPieCharts:
                Intent intent= new Intent(this,DescriptionPerArea.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
            case R.id.bLineCharts:
                break;
        }
    }


    public ArrayList<String> getChartTitles(ArrayList<Symptom> symptomLogs) {
        mAreas=new ArrayList<>();
        mDescriptions=new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        for(int i=0;i<symptomLogs.size();i++){
            boolean same = false;
            for(int j=0;j<i;j++) {
                if (symptomLogs.get(i).getArea().equals(symptomLogs.get(j).getArea()) && symptomLogs.get(i).getDescription().equals(symptomLogs.get(j).getDescription())){
                    same=true;
                    break;
                }
            }
            if(!same) {
                String title = symptomLogs.get(i).getArea() +" - " + symptomLogs.get(i).getDescription();
                titles.add(title);
                mAreas.add(symptomLogs.get(i).getArea());
                mDescriptions.add(symptomLogs.get(i).getDescription());
            }
        }
        return titles;
    }
}


