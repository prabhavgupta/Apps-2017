package hr.ferit.mdudjak.healthdiary;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class DescriptionPerArea extends AppCompatActivity implements View.OnClickListener{
    Button bBarCharts, bLineCharts;
    ListView lDescriptionStatistics;
    DescriptionStatisticsAdapter mDescriptionStatisticsAdapter;
    ArrayList<BarData> mBarData;
    ArrayList<String> mChartTitles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_per_area);
        this.setUpUI();
    }

    private void setUpUI() {
        this.bBarCharts= (Button) this.findViewById(R.id.bPieCharts);
        this.bLineCharts= (Button) this.findViewById(R.id.bLineCharts);
        this.lDescriptionStatistics= (ListView) this.findViewById(R.id.lvDescriptionStatistics);
        this.bLineCharts.setOnClickListener(this);
        this.bBarCharts.setOnClickListener(this);
        ArrayList<Symptom> symptomLogs = DBHelper.getInstance(this).getAllSymptoms();
        this.mChartTitles=this.getChartTitles(symptomLogs);
        this.mBarData=this.getBarData(symptomLogs);
        mDescriptionStatisticsAdapter = new DescriptionStatisticsAdapter(mBarData,mChartTitles);
        this.lDescriptionStatistics.setAdapter(mDescriptionStatisticsAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bPieCharts:
                break;
            case R.id.bLineCharts:
                Intent intent = new Intent(this, SymptomsThroughTime.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }
    }

    public ArrayList<String> getChartTitles(ArrayList<Symptom> symptomLogs) {
        ArrayList<String> areas =new ArrayList<>();
        for(int i=0;i<symptomLogs.size();i++){
            boolean same=false;
            for(int j=0;j<i;j++){
                if(symptomLogs.get(i).getArea().equals(symptomLogs.get(j).getArea())){
                    same=true;
                }
            }
            if(!same){
                areas.add(symptomLogs.get(i).getArea());
            }
        }
        return areas;
    }

    public ArrayList<BarData> getBarData(ArrayList<Symptom> symptomLogs) {
        ArrayList<BarData> barDataArray = new ArrayList<>();
        ArrayList<ArrayList<String>> descriptionsPerArea=new ArrayList<>();

        for(int i=0;i<mChartTitles.size();i++){
            ArrayList<String> descriptions=new ArrayList<>();
            for(int j=0;j<symptomLogs.size();j++){
                boolean same=false;
                for(int k=0;k<j;k++){
                    if((symptomLogs.get(k).getDescription().equals(symptomLogs.get(j).getDescription())) && (symptomLogs.get(k).getArea().equals(symptomLogs.get(j).getArea()))){
                        same = true;
                    }
                }
                if((!same) && symptomLogs.get(j).getArea().equals(mChartTitles.get(i))){
                    descriptions.add(symptomLogs.get(j).getDescription());
                }
            }
            descriptionsPerArea.add(i,descriptions);
        }

        ArrayList<ArrayList<Integer>> array_of_values= new ArrayList<>();
        for(int i =0;i<mChartTitles.size();i++){
            ArrayList<Integer> number_of_description_logs = new ArrayList<>();
            for(int j=0;j<descriptionsPerArea.get(i).size();j++){
                int br=0;
                for(int k=0;k<symptomLogs.size();k++){
                    if(symptomLogs.get(k).getArea().equals(mChartTitles.get(i)) && symptomLogs.get(k).getDescription().equals(descriptionsPerArea.get(i).get(j))){
                        br++;
                    }
                }
                number_of_description_logs.add(br);
            }
            array_of_values.add(number_of_description_logs);
        }

        ArrayList<ArrayList<BarEntry>> barEntries = new ArrayList<>();
        for(int j=0;j<mChartTitles.size();j++) {
            ArrayList<BarEntry> oneRow = new ArrayList<>();
            for (int i = 0; i < array_of_values.get(j).size(); i++) {
                oneRow.add(new BarEntry(array_of_values.get(j).get(i), i));
            }
            barEntries.add(oneRow);
        }

        //create the data set
        for (int i=0;i<mChartTitles.size();i++) {
            BarDataSet barDataSet = new BarDataSet(barEntries.get(i), "Descriptions");
            BarData barData = new BarData(descriptionsPerArea.get(i),barDataSet);
            barDataArray.add(barData);
        }

        return barDataArray;
    }
}
