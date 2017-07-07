package hr.ferit.mdudjak.healthdiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static hr.ferit.mdudjak.healthdiary.R.id.txConnectivity;
import static hr.ferit.mdudjak.healthdiary.R.id.visible;

public class TherapyReminder extends AppCompatActivity{

    public static final int REQUEST_REMINDER = 1;
    ListView mRemindersList;
    TextView tvNoReminders;
    ReminderAdapter mReminderAdapter;
    Switch mSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapy_reminder);
        this.setUpUI();
    }

    private void setUpUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarReminder);
        setSupportActionBar(toolbar);
        this.mRemindersList = (ListView) this.findViewById(R.id.lRemindersHistory);
        this.tvNoReminders = (TextView) this.findViewById(R.id.txNoReminders);
        this.setUpList();
    }

    private void setUpList() {
        if (DBHelper.getInstance(this).getAllReminders().isEmpty()) {
            tvNoReminders.setVisibility(View.VISIBLE);
        } else {
            ArrayList<Reminder> reminders = DBHelper.getInstance(this).getAllReminders();
            tvNoReminders.setVisibility(View.GONE);
            this.mReminderAdapter = new ReminderAdapter(reminders,this);
            this.mRemindersList.setAdapter(mReminderAdapter);
        }

        this.mRemindersList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TherapyReminder.this);
                dialogBuilder.setMessage("Do you want to delete reminder?");
                dialogBuilder.setCancelable(true);
                dialogBuilder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Reminder reminder = (Reminder) mReminderAdapter.getItem(position);
                                DBHelper.getInstance(getApplicationContext()).deleteReminder((Reminder) mReminderAdapter.getItem(position));
                                mReminderAdapter.deleteAt(position);
                                dialog.cancel();
                                new ReminderManager(getApplicationContext()).cancelReminder(reminder.getID());
                            }
                        });

                dialogBuilder.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_insert_reminder) {
            Intent intent = new Intent(this, InsertReminder.class);
            this.startActivityForResult(intent, REQUEST_REMINDER);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_REMINDER:
                if (resultCode == RESULT_OK) {
                    this.setUpList();
                    break;
                }
        }
    }


}

