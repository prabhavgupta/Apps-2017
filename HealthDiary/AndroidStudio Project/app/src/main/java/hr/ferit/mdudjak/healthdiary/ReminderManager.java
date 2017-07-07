package hr.ferit.mdudjak.healthdiary;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Mario on 7.6.2017..
 */

public class ReminderManager{

    public static final String KEY_ROWID = "id";
    private Context mContext;
    private AlarmManager mAlarmManager;


    public ReminderManager(Context context) {
        mContext = context;
        mAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    }


    public void setReminder(int taskId, Calendar when, int repeatingTime) {
        Intent i = new Intent(mContext, OnAlarmReceiver.class);
        i.putExtra(KEY_ROWID, (long)taskId);
        PendingIntent pi = PendingIntent.getBroadcast(mContext, taskId, i, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(),pi);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(), 1000 * 60 * repeatingTime,pi);

    }

    public void cancelReminder(int taskId){
        Intent i = new Intent(mContext, OnAlarmReceiver.class);
        i.putExtra(KEY_ROWID, (long)taskId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, taskId, i, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.cancel(pendingIntent);
    }
}
