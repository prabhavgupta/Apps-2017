package hr.ferit.mdudjak.healthdiary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mario on 9.6.2017..
 */

public class BodyStatisticsAdapter extends BaseAdapter {
    private ArrayList<LineData> mLineCharts;
    private  ArrayList<String> mBodyVarNames;

    public BodyStatisticsAdapter(ArrayList<LineData> lineCharts, ArrayList<String> varNames) { mLineCharts = lineCharts; mBodyVarNames=varNames; }
    @Override
    public int getCount() { return this.mLineCharts.size(); }
    @Override
    public Object getItem(int position) { return this.mLineCharts.get(position); }
    @Override
    public long getItemId(int position) { return position; }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BodyStatisticsAdapter.ViewHolder bodyStatisticsViewHolder;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.items_body_statistics, parent, false);
            bodyStatisticsViewHolder = new BodyStatisticsAdapter.ViewHolder(convertView);
            convertView.setTag(bodyStatisticsViewHolder);
        }
        else{
            bodyStatisticsViewHolder = (BodyStatisticsAdapter.ViewHolder) convertView.getTag();
        }
        String bodyVariableName;
        if(position<5) {
            bodyVariableName = this.mBodyVarNames.get(position);
        }
        else{
            bodyVariableName = "Body pressure";
        }

        bodyStatisticsViewHolder.tvTitle.setText(bodyVariableName);
        LineData bodyStatistics= this.mLineCharts.get(position);
        bodyStatisticsViewHolder.lineChart.setData(bodyStatistics);

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
            lineChart = (LineChart) view.findViewById(R.id.BodyStatisticslineChart);
        }
    }
}
