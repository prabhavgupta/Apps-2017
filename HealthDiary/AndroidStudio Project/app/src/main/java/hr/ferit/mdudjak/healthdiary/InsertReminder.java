package hr.ferit.mdudjak.healthdiary;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;

import android.icu.util.TimeZone;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Date;
import java.util.Calendar;


public class InsertReminder extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int DATE_PICKER_DIALOG = 0;
    private static final int TIME_PICKER_DIALOG = 1;
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";

    private EditText mTitleText;
    private EditText mBodyText;
    private Button mDateButton;
    private Button mTimeButton;
    private Button mConfirmButton;
    private Calendar mCalendar;
    private EditText mRepeatingValue;
    private Spinner mSpinner;
    private String mSelectedValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_reminder);
        this.setUpUI();

    }

    private void setUpUI() {
        mCalendar = Calendar.getInstance();
        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);
        mDateButton = (Button) findViewById(R.id.reminder_date);
        mTimeButton = (Button) findViewById(R.id.reminder_time);
        mConfirmButton = (Button) findViewById(R.id.confirm);
        mRepeatingValue = (EditText) findViewById(R.id.etRepeatingTime);
        mSpinner= (Spinner) findViewById(R.id.repeatingSpinner);
        mSpinner.setOnItemSelectedListener(this);
        setUpButtons();
    }

    private void setUpButtons() {

        mDateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(DATE_PICKER_DIALOG);
            }
        });


        mTimeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(TIME_PICKER_DIALOG);
            }
        });

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                saveToDatabase();
                setResult(RESULT_OK);
                Toast.makeText(InsertReminder.this, getString(R.string.task_saved_message), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case DATE_PICKER_DIALOG:
                return showDatePicker();
            case TIME_PICKER_DIALOG:
                return showTimePicker();
        }
        return super.onCreateDialog(id);
    }

    private DatePickerDialog showDatePicker() {

        DatePickerDialog datePicker = new DatePickerDialog(InsertReminder.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateButtonText();
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        return datePicker;
    }

    private TimePickerDialog showTimePicker() {
        TimePickerDialog timePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCalendar.set(Calendar.MINUTE, minute);
                updateTimeButtonText();
            }
        }, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE), true);

        return timePicker;
    }

    private void updateTimeButtonText() {
        SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
        timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+02:00"));
        String timeForButton = timeFormat.format(mCalendar.getTime());
        mTimeButton.setText(timeForButton);
    }

    private void updateDateButtonText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String dateForButton = dateFormat.format(mCalendar.getTime());
        mDateButton.setText(dateForButton);
    }



    private void saveToDatabase() {
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();
        int repeatingValue=0;
        if(!(mRepeatingValue.getText().toString().isEmpty())){
            repeatingValue = Integer.parseInt(mRepeatingValue.getText().toString());
        }
        switch (mSelectedValue){
            case "Minutes":
                break;
            case "Hours":
                repeatingValue=repeatingValue*60;
                break;
            case "Days":
                repeatingValue=repeatingValue*60*24;
                break;
        }
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        dateTimeFormat.setTimeZone(TimeZone.getTimeZone("GMT+02:00"));
        String reminderDateTime = dateTimeFormat.format(mCalendar.getTime());
        Reminder reminder = new Reminder(title,body,reminderDateTime,repeatingValue);
        DBHelper.getInstance(getApplicationContext()).insertReminder(reminder);
        int ID=DBHelper.getInstance(this).getAllReminders().get(DBHelper.getInstance(this).getAllReminders().size()-1).getID();
        int repeatingTime = DBHelper.getInstance(this).getAllReminders().get(DBHelper.getInstance(this).getAllReminders().size()-1).getRepeatingTime();
        new ReminderManager(this).setReminder(ID, mCalendar,repeatingTime);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSelectedValue= String.valueOf(parent.getItemAtPosition(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        mSelectedValue="Minutes";
    }
}
