package hr.ferit.mdudjak.healthdiary;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Mario on 7.6.2017..
 */

public class ReminderService extends WakeReminderIntentService {

    public ReminderService() {
        super("ReminderService");
    }

    @Override
    void doReminderWork(Intent intent) {
        Log.d("ReminderService", "Doing work.");
        Long rowId = intent.getExtras().getLong(ReminderManager.KEY_ROWID);

        NotificationManager mgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this, TherapyReminder.class);
        notificationIntent.putExtra(ReminderManager.KEY_ROWID, rowId);
        ArrayList<Reminder> reminders = DBHelper.getInstance(this).getAllReminders();
        String title,description;
        title="New task";
        description ="You have a task that needs to be reviewed.";
        int i=0;
        for(i=0;i<reminders.size();i++){
            if((long)(reminders.get(i).getID())==rowId){
                title = reminders.get(i).getTitle();
                description = reminders.get(i).getDescription();
            }
        }

        PendingIntent pi = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ReminderService.this);

        builder.setAutoCancel(true).setSmallIcon(R.drawable. notification_template_icon_bg)
        .setContentTitle(title)
        .setSmallIcon(android.R.drawable.ic_dialog_email)
        .setContentText(description)
        .setContentIntent(pi)
        .setLights(Color.BLUE, 2000, 1000)
        .setVibrate(new long[]{1000,1000,1000,1000,1000})
        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Notification notification = builder.getNotification();
        mgr.notify(0, notification);
    }

}