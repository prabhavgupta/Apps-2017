package hr.ferit.mdudjak.healthdiary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;

import java.util.ArrayList;

/**
 * Created by Mario on 9.6.2017..
 */

public class SymptomsStatisticsAdapter extends BaseAdapter{
    private ArrayList<LineData> mLineCharts;
    private  ArrayList<String> mSymptomVarNames;

    public SymptomsStatisticsAdapter(ArrayList<LineData> lineCharts, ArrayList<String> varNames) { mLineCharts = lineCharts; mSymptomVarNames=varNames; }
    @Override
    public int getCount() { return this.mLineCharts.size(); }
    @Override
    public Object getItem(int position) { return this.mLineCharts.get(position); }
    @Override
    public long getItemId(int position) { return position; }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SymptomsStatisticsAdapter.ViewHolder symptomStatisticsViewHolder;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.items_symptom_statistics, parent, false);
            symptomStatisticsViewHolder = new SymptomsStatisticsAdapter.ViewHolder(convertView);
            convertView.setTag(symptomStatisticsViewHolder);
        }
        else{
            symptomStatisticsViewHolder = (SymptomsStatisticsAdapter.ViewHolder) convertView.getTag();
        }
        String symptomVariableName = this.mSymptomVarNames.get(position);
        symptomStatisticsViewHolder.tvTitle.setText(symptomVariableName);
        LineData symptomStatistics= this.mLineCharts.get(position);
        symptomStatisticsViewHolder.lineChart.setData(symptomStatistics);

        return convertView;
    }

    public void deleteAt(int position) {
        this.mLineCharts.remove(position);
        this.notifyDataSetChanged();
    }
    public static class ViewHolder {
        public TextView tvTitle;
        public LineChart lineChart;
        public ViewHolder(View view) {
            tvTitle = (TextView) view.findViewById(R.id.tvStatisticItemTitle);
            lineChart = (LineChart) view.findViewById(R.id.SymptomStatisticslineChart);
        }
    }
}

