package hr.ferit.mdudjak.healthdiary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;

import java.util.ArrayList;

/**
 * Created by Mario on 10.6.2017..
 */

public class DescriptionStatisticsAdapter extends BaseAdapter {
    private ArrayList<BarData> mBarCharts;
    private  ArrayList<String> mDescriptionVarNames;

    public DescriptionStatisticsAdapter(ArrayList<BarData> barCharts, ArrayList<String> varNames) { mBarCharts = barCharts; mDescriptionVarNames=varNames; }
    @Override
    public int getCount() { return this.mBarCharts.size(); }
    @Override
    public Object getItem(int position) { return this.mBarCharts.get(position); }
    @Override
    public long getItemId(int position) { return position; }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DescriptionStatisticsAdapter.ViewHolder DescriptiontatisticsViewHolder;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.item_description_statistics, parent, false);
            DescriptiontatisticsViewHolder = new DescriptionStatisticsAdapter.ViewHolder(convertView);
            convertView.setTag(DescriptiontatisticsViewHolder);
        }
        else{
            DescriptiontatisticsViewHolder = (DescriptionStatisticsAdapter.ViewHolder) convertView.getTag();
        }
        String DescriptionVariableName = this.mDescriptionVarNames.get(position);
        DescriptiontatisticsViewHolder.tvTitle.setText(DescriptionVariableName);

        BarData barData= this.mBarCharts.get(position);
        DescriptiontatisticsViewHolder.barChart.setData(barData);
        DescriptiontatisticsViewHolder.barChart.setDescription("Description per Area");
        return convertView;
    }

    public void deleteAt(int position) {
        this.mBarCharts.remove(position);
        this.notifyDataSetChanged();
    }
    public static class ViewHolder {
        public TextView tvTitle;
        public BarChart barChart;
        public ViewHolder(View view) {
            tvTitle = (TextView) view.findViewById(R.id.tvDescriptionStatisticItemTitle);
            barChart = (BarChart) view.findViewById(R.id.DescriptionBargraph);
        }
    }
}

