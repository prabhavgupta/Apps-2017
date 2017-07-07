package hr.ferit.mdudjak.healthdiary;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mario on 13.5.2017..
 */

public class BodyLogsAdapter extends BaseAdapter {

    private ArrayList<BodyLog> mBodyLogs;
    Context mContext;
    public BodyLogsAdapter(ArrayList<BodyLog> bodyLogs, Context context) { mBodyLogs = bodyLogs; mContext=context; }
    @Override
    public int getCount() { return this.mBodyLogs.size(); }
    @Override
    public Object getItem(int position) { return this.mBodyLogs.get(position); }
    @Override
    public long getItemId(int position) { return position; }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BodyLogsAdapter.ViewHolder bodyLogsViewHolder;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.item_body_logs, parent, false);
            bodyLogsViewHolder = new BodyLogsAdapter.ViewHolder(convertView);
            convertView.setTag(bodyLogsViewHolder);
        }
        else{
            bodyLogsViewHolder = (BodyLogsAdapter.ViewHolder) convertView.getTag();
        }
        BodyLog bodyLog= this.mBodyLogs.get(position);
        bodyLogsViewHolder.tvWeight.setText(bodyLog.getWeight().toString()+" kg");
        bodyLogsViewHolder.tvHeartRate.setText(String.valueOf(bodyLog.getHeartRate() + " rpm"));
        bodyLogsViewHolder.tvBloodSugar.setText(bodyLog.getBloodSugar().toString() + " mmol/L");
        String sBodyPressure=String.valueOf(bodyLog.getUpperPressure())+"/"+String.valueOf(bodyLog.getLowerPressure());
        bodyLogsViewHolder.tvBodyPressure.setText(sBodyPressure + " mm/Hg");
        bodyLogsViewHolder.tvDate.setText(bodyLog.getDate());
        String pictureURL=null;
        ArrayList<CameraLog> cameraLogs = DBHelper.getInstance(mContext).getAllCameraLogs();
        int j=0;
        for(j=0;j<cameraLogs.size();j++){
            if(cameraLogs.get(j).getPictureDate().equals(bodyLog.getDate())){
                pictureURL=cameraLogs.get(j).getPictureURL();
                Log.e("Position:", String.valueOf(position));
                Log.e("Camera log Date:", cameraLogs.get(j).getPictureDate());
                Log.e("Body log date:",bodyLog.getDate());
                break;
            }
        }
//Prvo odrediti

        if (pictureURL!=null) {
            bodyLogsViewHolder.ivImage.setVisibility(View.VISIBLE);
            Picasso.with(parent.getContext())
                    .load(pictureURL)
                    .fit()
                    .centerCrop()
                    .rotate(90f)
                    .into(bodyLogsViewHolder.ivImage);
        }

        TypedArray ta1 = mContext.getResources().obtainTypedArray(R.array.backgrounds);
        int[] backgrounds = new int[ta1.length()];
        for (int i = 0; i < ta1.length(); i++) {
            backgrounds[i] = ta1.getColor(i, 0);
        }
        bodyLogsViewHolder.relativeLayout.setBackgroundColor(backgrounds[position % 2]);
        ta1.recycle();
        int lognumber =mBodyLogs.size()-position;
        bodyLogsViewHolder.tvTitle.setText("Log number "+lognumber);
        return convertView;
    }
    public void insert(BodyLog bodyLog) {
        this.mBodyLogs.add(bodyLog);
        this.notifyDataSetChanged();
    }
    public void deleteAt(int position) {
        this.mBodyLogs.remove(position);
        this.notifyDataSetChanged();
    }
    public static class ViewHolder {
        public TextView tvWeight, tvHeartRate, tvBloodSugar, tvBodyPressure,tvDate,tvTitle;
        public ImageView ivImage;
        public RelativeLayout relativeLayout;
        public ViewHolder(View view) {
            tvWeight = (TextView) view.findViewById(R.id.tvBodyLogWeight);
            tvHeartRate= (TextView) view.findViewById(R.id.tvBodyLogHeartRate);
            tvBloodSugar= (TextView) view.findViewById(R.id.tvBodyLogBloodSugar);
            tvBodyPressure= (TextView) view.findViewById(R.id.tvBodyLogBodyPressure);
            tvDate = (TextView) view.findViewById(R.id.txtBodyLogDate);
            ivImage= (ImageView) view.findViewById(R.id.ivCameraLogImage);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.rlBodyLog);
            tvTitle = (TextView) view.findViewById(R.id.tvBodyLogTitle);
        }
    }
}
