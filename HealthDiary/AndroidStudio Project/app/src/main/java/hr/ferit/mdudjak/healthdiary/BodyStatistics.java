package hr.ferit.mdudjak.healthdiary;

import android.app.IntentService;
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

public class BodyStatistics extends AppCompatActivity implements View.OnClickListener {
    Button bSymptom, bBody;
    ListView lBodyStatistics;
    BodyStatisticsAdapter mBodyStatisticsAdapter;
    ArrayList<LineData> mLineData;
    ArrayList<String> mBodyVarNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_statistics);
        this.setUpUI();
    }

    private void setUpUI() {
        this.lBodyStatistics = (ListView) this.findViewById(R.id.lvBodyStatistics);
        this.bSymptom= (Button) this.findViewById(R.id.bSymptomStatistics);
        this.bBody= (Button) this.findViewById(R.id.bBodyStatistics);
        this.bBody.setOnClickListener(this);
        this.bSymptom.setOnClickListener(this);
        ArrayList<BodyLog> bodyLogs = DBHelper.getInstance(this).getAllBodyLogs();
        this.mBodyVarNames=new ArrayList<>();
        mBodyVarNames.add("Weight");
        mBodyVarNames.add("Heart rate");
        mBodyVarNames.add("Blood sugar");
        mBodyVarNames.add("Upper body pressure");
        mBodyVarNames.add("Lower body pressure");
        this.mLineData= this.makeLineCharts(bodyLogs);
        this.mBodyStatisticsAdapter = new BodyStatisticsAdapter(mLineData,mBodyVarNames);
        this.lBodyStatistics.setAdapter(mBodyStatisticsAdapter);
    }

    public ArrayList<LineData> makeLineCharts(ArrayList<BodyLog> bodyLogs){
        ArrayList<LineData> lineCharts= new ArrayList<>();
        ArrayList<String> xAXES = new ArrayList<>();

        ArrayList<Entry> weights = new ArrayList<>();
        ArrayList<Entry> heartRates = new ArrayList<>();
        ArrayList<Entry> bloodSugars = new ArrayList<>();
        ArrayList<Entry> upperPressures = new ArrayList<>();
        ArrayList<Entry> lowerPressures = new ArrayList<>();


        int numDataPoints = bodyLogs.size();

        //5 funkcija za svaku varijablu
        for(int i=0;i<numDataPoints;i++){
            weights.add(new Entry(bodyLogs.get(i).getWeight(),i));
            heartRates.add(new Entry(bodyLogs.get(i).getHeartRate(),i));
            bloodSugars.add(new Entry(bodyLogs.get(i).getBloodSugar(),i));
            upperPressures.add(new Entry(bodyLogs.get(i).getUpperPressure(),i));
            lowerPressures.add(new Entry(bodyLogs.get(i).getLowerPressure(),i));
            xAXES.add(i, bodyLogs.get(i).getDate());
        }

        String[] xaxes = new String[xAXES.size()];
        for(int i=0; i<xAXES.size();i++){
            xaxes[i] = xAXES.get(i).toString();
        }
        ArrayList<ArrayList<Entry>> bodyVarFunctions = new ArrayList<>();
        bodyVarFunctions.add(weights);
        bodyVarFunctions.add(heartRates);
        bodyVarFunctions.add(bloodSugars);
        bodyVarFunctions.add(upperPressures);
        bodyVarFunctions.add(lowerPressures);

        for(int i=0;i<mBodyVarNames.size();i++) {
            LineDataSet lineDataSet = new LineDataSet(bodyVarFunctions.get(i), mBodyVarNames.get(i));
            lineDataSet.setDrawCircles(false);
            lineDataSet.setColor(Color.BLUE);
            lineCharts.add(new LineData(xaxes, lineDataSet));
        }
        LineDataSet lineDataSet1 = new LineDataSet(upperPressures,"Upper pressure");
        lineDataSet1.setDrawCircles(false);
        lineDataSet1.setColor(Color.BLUE);

        LineDataSet lineDataSet2 = new LineDataSet(lowerPressures,"Lower pressure");
        lineDataSet2.setDrawCircles(false);
        lineDataSet2.setColor(Color.RED);
        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(lineDataSet1);
        lineDataSets.add(lineDataSet2);
        lineCharts.add(new LineData(xaxes,lineDataSets));
        return lineCharts;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bBodyStatistics:
              break;
            case R.id.bSymptomStatistics:
                Intent intent= new Intent(this,SymptomStatistics.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }
    }
}
