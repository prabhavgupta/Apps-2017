package hr.ferit.mdudjak.healthdiary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class BodyLogActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_IMAGE = 2 ;
    public static final String PICTURE_DATE ="picture_date";
    public static final String KEY_REQUEST_IMAGE = "image";
    Button mSubmitBodyLog, mSubmitCameraBodyLog;
    EditText etWeight, etHeartRate, etBloodSugar, etBodyPressureUpper, etBodyPressureLower;
    String mDate=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_log);
        this.setUpUI();
    }

    private void setUpUI() {
        this.etWeight= (EditText) this.findViewById(R.id.etAddWeight);
        this.etHeartRate= (EditText) this.findViewById(R.id.etAddHeartRate);
        this.etBloodSugar= (EditText) this.findViewById(R.id.etAddBloodSugar);
        this.etBodyPressureUpper= (EditText) this.findViewById(R.id.etAddBodyPressureUpper);
        this.etBodyPressureLower = (EditText) this.findViewById(R.id.etAddBodyPressureLower);
        this.mSubmitBodyLog= (Button) this.findViewById(R.id.bAddBodyLog);
        this.mSubmitCameraBodyLog= (Button) this.findViewById(R.id.bAddBodyCameraLog);
        this.mSubmitBodyLog.setOnClickListener(this);
        this.mSubmitCameraBodyLog.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bAddBodyLog:
            if (etWeight.getText().toString().isEmpty() || etHeartRate.getText().toString().isEmpty() || etBloodSugar.getText().toString().isEmpty() || etBodyPressureUpper.getText().toString().isEmpty() || etBodyPressureLower.getText().toString().isEmpty()) {
                //Napraviti LOG
            } else {
                Float fWeight = Float.parseFloat(etWeight.getText().toString());
                int iHeartRate = Integer.parseInt(etHeartRate.getText().toString());
                Float fBloodSugar = Float.parseFloat(etBloodSugar.getText().toString());
                int iBodyPressureUpper = Integer.parseInt(etBodyPressureUpper.getText().toString());
                int iBodyPressureLower = Integer.parseInt(etBodyPressureLower.getText().toString());
                if (fWeight <= 0 || fWeight > 800) {
                    etWeight.setError("Please input realistic weight.");
                } else if (iHeartRate <= 0 || iHeartRate > 250) {
                    etHeartRate.setError("Please input realistic heart rate");
                } else if (fBloodSugar <= 0 || fBloodSugar > 150) {
                    etBloodSugar.setError("Please input realistic blood sugar");
                } else if (iBodyPressureUpper <= 0 || iBodyPressureUpper > 250) {
                    etBodyPressureUpper.setError("Please input realistic body pressure");
                } else if (iBodyPressureLower <= 0 || iBodyPressureLower > 250) {
                    etBodyPressureLower.setError("Please input realistic body pressure");
                } else {
                    //Spremanje u bazu
                    if(mDate==null) {
                        StringBuilder stringBuilder = new StringBuilder();
                        Calendar calendar = Calendar.getInstance();
                        String sAmPm = Boolean.valueOf(String.valueOf(calendar.get(Calendar.AM_PM))) ? "AM" : "PM";
                        stringBuilder.append(calendar.get(Calendar.HOUR_OF_DAY)).append(":").append(calendar.get(Calendar.MINUTE) + " ").append(sAmPm).append("\n");
                        stringBuilder.append(calendar.get(Calendar.DATE) + ".").append(calendar.get(Calendar.MONTH) + ".").append(calendar.get(Calendar.YEAR) + ".").append("\n");
                        mDate = String.valueOf(stringBuilder);
                    }
                    final BodyLog bodyLog = new BodyLog(fWeight, iHeartRate, fBloodSugar, iBodyPressureUpper, iBodyPressureLower,mDate);
                    AlertDialog.Builder dialogBuilderForSaving = new AlertDialog.Builder(this);
                    final AlertDialog.Builder dialogBuilderAfterSaving = new AlertDialog.Builder(this);
                    final Intent historyIntent = new Intent(this, BodyLogsHistory.class);
                    dialogBuilderForSaving.setMessage(R.string.BodyLogToSaveDialogMessage);
                    dialogBuilderForSaving.setCancelable(true);
                    dialogBuilderForSaving.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    DBHelper.getInstance(getApplicationContext()).insertBodyLog(bodyLog);
                                    dialogBuilderAfterSaving.setMessage(R.string.BodyLogDialogMessage);
                                    dialogBuilderAfterSaving.setCancelable(true);
                                    dialogBuilderAfterSaving.setPositiveButton(
                                            "Yes",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    historyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(historyIntent);
                                                    dialog.cancel();
                                                    finish();
                                                }
                                            });

                                    dialogBuilderAfterSaving.setNegativeButton(
                                            "No",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    //LOG i.
                                                    dialog.cancel();
                                                }
                                            });

                                    AlertDialog alertDialog = dialogBuilderAfterSaving.create();
                                    alertDialog.show();
                                    dialog.cancel();
                                }
                            });

                    dialogBuilderForSaving.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //LOG i.
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog = dialogBuilderForSaving.create();
                    alertDialog.show();

                }
            }
        break;
            case R.id.bAddBodyCameraLog:
                Intent cameraIntent = new Intent(this, CameraLogsActivity.class);
                cameraIntent.putExtra(KEY_REQUEST_IMAGE,REQUEST_IMAGE);
                this.startActivityForResult(cameraIntent, REQUEST_IMAGE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE:
                if (resultCode == RESULT_OK) {
                    this.mDate = String.valueOf(data.getStringExtra(PICTURE_DATE));
                    Toast.makeText(this, R.string.CameraLogDoneMessage, Toast.LENGTH_SHORT);
                    this.mSubmitBodyLog.performClick();
                    break;
                }
        }
    }

}



