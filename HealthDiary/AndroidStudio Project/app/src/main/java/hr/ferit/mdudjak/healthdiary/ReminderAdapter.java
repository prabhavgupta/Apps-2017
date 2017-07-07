package hr.ferit.mdudjak.healthdiary;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Mario on 7.6.2017..
 */

public class ReminderAdapter extends BaseAdapter {
    List<Reminder> mReminders;
    Context mContext;

    public ReminderAdapter(List<Reminder> reminders, Context context) {
        mReminders=reminders;
        mContext=context;
    }

    @Override
    public int getCount() {
        return this.mReminders.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mReminders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ReminderAdapter.ReminderViewHolder reminderViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.reminder_item, parent, false);
            reminderViewHolder = new ReminderAdapter.ReminderViewHolder (convertView);
            convertView.setTag(reminderViewHolder);
        } else {
            reminderViewHolder = (ReminderAdapter.ReminderViewHolder) convertView.getTag();
        }
        final Reminder reminder = this.mReminders.get(position);
        reminderViewHolder.tvTitle.setText(reminder.getTitle());
        reminderViewHolder.tvDescription.setText(reminder.getDescription());
        reminderViewHolder.tvPubDate.setText(reminder.getDateTime());
        final int repeatingTime = reminder.getRepeatingTime();
        if(repeatingTime<60){
            reminderViewHolder.tvRepeating.setText(repeatingTime+" minutes");
        }
        else if(repeatingTime>=60 && repeatingTime <60*24){
            double hours =repeatingTime/60.0;
            long lhours = (long) hours;
            double fhours = hours- lhours;
            if(fhours==0) {
                reminderViewHolder.tvRepeating.setText(lhours + " hours");
            }
            else{
                reminderViewHolder.tvRepeating.setText(repeatingTime + " minutes");
            }
        }
        else{
            double days = repeatingTime/(60.0*24);
            long ldays = (long) days;
            double fdays = days - ldays;
            if(fdays==0) {
                reminderViewHolder.tvRepeating.setText(ldays + " days");
            }
            else{
                double hours = repeatingTime/60.0;
                long lhours = (long) hours;
                double fhours = hours- lhours;
                if(fhours==0) {
                    reminderViewHolder.tvRepeating.setText(lhours + " hours");
                }
                else{
                    reminderViewHolder.tvRepeating.setText(repeatingTime + " minutes");
                }
            }
        }
        final String sCalendar = reminder.getDateTime();
        final Calendar calendar = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date firstDate =sdf.parse(sCalendar);
            calendar.setTime(sdf.parse(sCalendar));
            calendar.add(Calendar.MINUTE,repeatingTime);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("Error Parsing",e.toString());
        }
        reminderViewHolder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppTypeDetails.getInstance(mContext).setToggleStatus(true,position);
                    Toast.makeText(mContext, "Reminder is set.", Toast.LENGTH_SHORT);
                    new ReminderManager(mContext).setReminder(reminder.getID(), calendar, repeatingTime);
                } else {
                    AppTypeDetails.getInstance(mContext).setToggleStatus(false,position);
                    Toast.makeText(mContext,"Reminder is turned off.",Toast.LENGTH_SHORT);
                    new ReminderManager(mContext).cancelReminder(reminder.getID());
                }

            }
        });
        Boolean b = AppTypeDetails.getInstance(mContext).getToggleStatus(position);
        Log.e("ToggleStatus",b.toString());
        reminderViewHolder.aSwitch.setChecked(b);

        TypedArray ta1 = mContext.getResources().obtainTypedArray(R.array.remainder_backgrounds);
        int[] backgrounds = new int[ta1.length()];
        for (int i = 0; i < ta1.length(); i++) {
            backgrounds[i] = ta1.getColor(i, 0);
        }
        reminderViewHolder.relativeLayout.setBackgroundColor(backgrounds[position % 2]);
        ta1.recycle();
        return convertView;
    }


    public void deleteAt(int position) {
        this.mReminders.remove(position);
        this.notifyDataSetChanged();
    }


    static class ReminderViewHolder {
        TextView tvTitle, tvDescription, tvPubDate,tvRepeating;
        Switch aSwitch;
        RelativeLayout relativeLayout;
        public ReminderViewHolder (View reminderView) {
            this.tvTitle = (TextView) reminderView.findViewById(R.id.tvReminderTitle);
            this.tvDescription = (TextView) reminderView.findViewById(R.id.tvReminderDescription);
            this.tvPubDate= (TextView) reminderView.findViewById(R.id.tvReminderDate);
            this.aSwitch= (Switch) reminderView.findViewById(R.id.reminderSwitch);
            this.tvRepeating = (TextView) reminderView.findViewById(R.id.tvReminderRepeatingValue);
            this.relativeLayout = (RelativeLayout) reminderView.findViewById(R.id.rlReminderItem);
        }
    }
}
