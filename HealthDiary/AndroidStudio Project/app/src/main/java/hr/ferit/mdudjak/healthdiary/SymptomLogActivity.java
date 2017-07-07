package hr.ferit.mdudjak.healthdiary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class SymptomLogActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    public static final int REQUEST_AREA = 1;
    public static final int REQUEST_DESCRIPTION = 2;
    public static final String KEY_REQUEST_AREA = "area";
    public static final String KEY_REQUEST_DESCRIPTION = "description";
    public static final String AREA_RESULT = "area_result";
    public static final String DESCRIPTION_RESULT ="description_result";
    ImageButton bAddArea, bAddDescription;
    int i=0;
    Button bAddSymptom;
    TextView txtAreaLog, txtDescriptionLog;
    TextView txtAreaLogTitle, txtDescriptionLogTitle;
    SeekBar sbPainIntensity;
    String area=null, description=null;
    int intensity=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptom_log);
        this.setUpUI();
    }

    private void setUpUI() {
        this.bAddArea = (ImageButton) this.findViewById(R.id.bAddPainArea);
        this.bAddDescription = (ImageButton) this.findViewById(R.id.bAddPainDescription);
        this.bAddSymptom= (Button) this.findViewById(R.id.bAddSymptom);
        this.sbPainIntensity = (SeekBar) this. findViewById(R.id.seekBar);
        this.sbPainIntensity.setThumb(writeOnDrawable("5"));
        this.sbPainIntensity.setOnSeekBarChangeListener(this);
        this.txtDescriptionLog= (TextView) this.findViewById(R.id.txtPainDescriptionLog);
        this.txtAreaLog= (TextView) this.findViewById(R.id.txtPainAreaLog);
        this.txtAreaLogTitle= (TextView) this.findViewById(R.id.txtPainAreaTitle);
        this.txtDescriptionLogTitle= (TextView) this.findViewById(R.id.txtPainDescriptionTitle);
        this.bAddDescription.setOnClickListener(this);
        this.bAddArea.setOnClickListener(this);
        this.bAddSymptom.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        final Intent intent = new Intent(this, AddSymptomInfo.class);
        switch (v.getId()) {
            case (R.id.bAddPainArea):
                intent.putExtra(KEY_REQUEST_AREA,REQUEST_AREA);
                this.startActivityForResult(intent, REQUEST_AREA);
                break;
            case (R.id.bAddPainDescription):
                intent.putExtra(KEY_REQUEST_DESCRIPTION,REQUEST_DESCRIPTION);
                this.startActivityForResult(intent, REQUEST_DESCRIPTION);
                break;
            case (R.id.bAddSymptom):
                if(intensity==0 || txtAreaLog.getText().toString().isEmpty() ||txtDescriptionLog.getText().toString().isEmpty()){

                }
                else {
                        if ((area != null) && (description != null)) {
                            StringBuilder stringBuilder = new StringBuilder();
                            Calendar calendar = Calendar.getInstance();
                            String sAmPm = Boolean.valueOf(String.valueOf(calendar.get(Calendar.AM_PM))) ? "AM" : "PM";
                            stringBuilder.append(calendar.get(Calendar.HOUR_OF_DAY)).append(":").append(calendar.get(Calendar.MINUTE) + " ").append(sAmPm).append("\n");
                            stringBuilder.append(calendar.get(Calendar.DATE) + ".").append(calendar.get(Calendar.MONTH) + ".").append(calendar.get(Calendar.YEAR) + ".").append("\n");
                            String date = String.valueOf(stringBuilder);
                            final Symptom newSymptom = new Symptom(area, description, intensity,date);
                            AlertDialog.Builder dialogBuilderForSaving = new AlertDialog.Builder(this);
                            final AlertDialog.Builder dialogBuilderAfterSaving = new AlertDialog.Builder(this);
                            final Intent historyIntent = new Intent(this, SymptomsHistory.class);
                            dialogBuilderForSaving.setMessage(R.string.SymptomLogToSaveDialogMessage);
                            dialogBuilderForSaving.setCancelable(true);
                            dialogBuilderForSaving.setPositiveButton(
                                    "Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            DBHelper.getInstance(getApplicationContext()).insertSymptom(newSymptom);
                                            dialogBuilderAfterSaving.setMessage(R.string.SymptomLogDialogMessage);
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

                }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_AREA:
                if (resultCode == RESULT_OK) {
                    area = String.valueOf(data.getStringExtra(AREA_RESULT));
                    this.txtAreaLog.setText(area);
                    this.txtAreaLogTitle.setText("Area of pain:");

                }
                break;
            case REQUEST_DESCRIPTION:
                if (resultCode == RESULT_OK) {
                    description = String.valueOf(data.getStringExtra(DESCRIPTION_RESULT));
                    this.txtDescriptionLog.setText(description);
                    this.txtDescriptionLogTitle.setText("Pain description:");

                }
                break;
        }
    }

    public BitmapDrawable writeOnDrawable(String text){

       // Bitmap bm = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), drawableId));
        Bitmap bm = Bitmap.createBitmap(100,450, Bitmap.Config.ALPHA_8);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        paint.setTextSize(75);

        Canvas canvas = new Canvas(bm);

        canvas.drawText(text, 25, 300, paint);

        return new BitmapDrawable(bm);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        this.sbPainIntensity.setThumb(writeOnDrawable(String.valueOf(progress+1)));
        this.intensity=progress+1;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
