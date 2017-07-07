package hr.ferit.mdudjak.healthdiary;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mario on 13.5.2017..
 */

public class SymptomsAdapter extends BaseAdapter {

    private ArrayList<Symptom> mSymptoms;
    Context mContext;
    public SymptomsAdapter(ArrayList<Symptom> symptoms , Context context) { mSymptoms = symptoms; mContext=context;}
    @Override
    public int getCount() { return this.mSymptoms.size(); }
    @Override
    public Object getItem(int position) { return this.mSymptoms.get(position); }
    @Override
    public long getItemId(int position) { return position; }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder symptomViewHolder;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.item_symptom, parent, false);
            symptomViewHolder = new ViewHolder(convertView);
            convertView.setTag(symptomViewHolder);
        }
        else{
            symptomViewHolder = (ViewHolder) convertView.getTag();
        }
        Symptom symptom= this.mSymptoms.get(position);
        symptomViewHolder.tvArea.setText(symptom.getArea());
        symptomViewHolder.tvDescription.setText(symptom.getDescription());
        symptomViewHolder.tvIntensity.setText(String.valueOf(symptom.getIntensity()));
        symptomViewHolder.tvDate.setText(symptom.getDate());

        TypedArray ta = mContext.getResources().obtainTypedArray(R.array.colors);
        int[] colors = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            colors[i] = ta.getColor(i, 0);
        }
        ta.recycle();
        symptomViewHolder.tvIntensityColor.setBackgroundColor(colors[symptom.getIntensity()-1]);

        TypedArray ta1 = mContext.getResources().obtainTypedArray(R.array.backgrounds);
        int[] backgrounds = new int[ta1.length()];
        for (int i = 0; i < ta1.length(); i++) {
            backgrounds[i] = ta1.getColor(i, 0);
        }
        symptomViewHolder.relativeLayout.setBackgroundColor(backgrounds[position % 2]);
        ta1.recycle();
        int lognumber =mSymptoms.size()-position;
        symptomViewHolder.tvTitle.setText("Log number "+lognumber);
        return convertView;
    }
    public void insert(Symptom symptom) {
        this.mSymptoms.add(symptom);
        this.notifyDataSetChanged();
    }
    public void deleteAt(int position) {
        this.mSymptoms.remove(position);
        this.notifyDataSetChanged();
    }
    public static class ViewHolder {
        public TextView tvArea, tvDescription, tvIntensity,tvDate, tvIntensityColor, tvTitle;
        public RelativeLayout relativeLayout;
        public ViewHolder(View symptomView) {
            tvDate= (TextView) symptomView.findViewById(R.id.tvSymptomDate);
            tvArea = (TextView) symptomView.findViewById(R.id.tvSymptomArea);
            tvDescription = (TextView) symptomView.findViewById(R.id.tvSymptomDescription);
            tvIntensity = (TextView) symptomView.findViewById(R.id.tvSymptomIntensity);
            tvIntensityColor= (TextView) symptomView.findViewById(R.id.txtForColorIntensity);
            relativeLayout= (RelativeLayout) symptomView.findViewById(R.id.rlItemSypmtom);
            tvTitle= (TextView) symptomView.findViewById(R.id.tvSymptomTitle);
        }
    }
}
