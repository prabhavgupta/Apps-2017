package hr.ferit.mdudjak.healthdiary;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CameraLogsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{


    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE =10 ;
    ImageButton ibAddCameraLog;
    ListView lvCameraLogs;
    String mCurrentPhotoPath;
    CameraLog mCameraLog;
    String mDate,mURL,mImageName;
    File mFile;
    private String mFileName;
    CameraLogsAdapter mCameraLogsAdapter;
    Uri photoURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_log);
        this.setUpUI();
    }

    private void setUpUI() {
        this.ibAddCameraLog = (ImageButton) this. findViewById(R.id.bAddCameraLog);
        this.ibAddCameraLog.setOnClickListener(this);
        this.lvCameraLogs = (ListView) this.findViewById(R.id.lvCameraLogs);
        ArrayList<CameraLog> cameraLogs = DBHelper.getInstance(this).getAllCameraLogs();
        Collections.reverse(cameraLogs);
        mCameraLogsAdapter = new CameraLogsAdapter(cameraLogs,this);
        this.lvCameraLogs.setAdapter(mCameraLogsAdapter);
        this.lvCameraLogs.setOnItemClickListener(this);
        this.lvCameraLogs.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CameraLogsActivity.this);
                dialogBuilder.setMessage("Do you want to delete camera log?");
                dialogBuilder.setCancelable(true);

                dialogBuilder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DBHelper.getInstance(getApplicationContext()).deleteCameraLog((CameraLog) mCameraLogsAdapter.getItem(position));
                                mCameraLogsAdapter.deleteAt(position);
                                dialog.cancel();
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

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        List<ResolveInfo> resolvedIntentActivities = getApplicationContext().getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);


        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();

            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("exception",ex.toString());
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                mFile = photoFile;
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                    String packageName = resolvedIntentInfo.activityInfo.packageName;
                    getApplicationContext().grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                this.mURL=photoURI.toString();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onClick(View v) {
        this.askForPermission();
    }
    public void askForPermission(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            } else {

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
        else{
            this.dispatchTakePictureIntent();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    this.dispatchTakePictureIntent();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (mURL != null && mDate != null) {
                    mCameraLog = new CameraLog(mURL, mDate);
                    DBHelper.getInstance(getApplicationContext()).insertCameraLog(mCameraLog);
                    ArrayList<CameraLog> cameraLogs = DBHelper.getInstance(this).getAllCameraLogs();
                    Collections.reverse(cameraLogs);
                    mCameraLogsAdapter = new CameraLogsAdapter(cameraLogs,this);
                    this.lvCameraLogs.setAdapter(mCameraLogsAdapter);
                    this.galleryAddPic(mFileName);
                    Intent startingIntent = this.getIntent();
                    if(startingIntent.hasExtra(BodyLogActivity.KEY_REQUEST_IMAGE)) {
                        Intent resultIntent = new Intent(this, BodyLogActivity.class);
                        resultIntent.putExtra(BodyLogActivity.PICTURE_DATE, mDate);
                        CameraLogsActivity.this.setResult(RESULT_OK, resultIntent);
                        CameraLogsActivity.this.finish();
                    }
                } else {
                    Log.e("Error", mURL + " " + mDate);
                }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name

        StringBuilder stringBuilder = new StringBuilder();
        Calendar calendar =Calendar.getInstance();

        String sAmPm = Boolean.valueOf(String.valueOf(calendar.get(Calendar.AM_PM)))?"AM":"PM";
        stringBuilder.append(calendar.get(Calendar.HOUR_OF_DAY)).append(":").append(calendar.get(Calendar.MINUTE)+ " ").append(sAmPm).append("\n");
        stringBuilder.append(calendar.get(Calendar.DATE)+".").append(calendar.get(Calendar.MONTH)+".").append(calendar.get(Calendar.YEAR)+".").append("\n");
        mDate= String.valueOf(stringBuilder);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera/");
        //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String imageFileName = "JPEG_" + timeStamp + "_";
        this.mFileName=imageFileName;
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         //suffix
                storageDir      //directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        this.mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void galleryAddPic(String fileName) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File newfile = new File(mURL+ fileName+".jpg");
        Uri contentUri = Uri.fromFile(newfile);
        List<ResolveInfo> resolvedIntentActivities = getApplicationContext().getPackageManager().queryIntentActivities(mediaScanIntent, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
            String packageName = resolvedIntentInfo.activityInfo.packageName;

            getApplicationContext().grantUriPermission(packageName, contentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CameraLog cameraLog = (CameraLog) mCameraLogsAdapter.getItem(position);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(cameraLog.getPictureURL()));
        List<ResolveInfo> resolvedIntentActivities = getApplicationContext().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
            String packageName = resolvedIntentInfo.activityInfo.packageName;

            getApplicationContext().grantUriPermission(packageName, Uri.parse(cameraLog.getPictureURL()), Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        startActivity(intent);
    }
}
