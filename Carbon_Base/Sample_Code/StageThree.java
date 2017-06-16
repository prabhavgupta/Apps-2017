package pl.jawegiel.mierzenieopencv;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


public class StageThree extends Activity implements CameraBridgeViewBase.CvCameraViewListener2, SeekBar.OnSeekBarChangeListener {


    private Mat mRgba;
    private Mat mGray;

    Point center2;

    MatOfPoint plyta;

    Point pkt1;
    Point pkt2;

    double rozmiar_x;
    double rozmiar_y;
    boolean usun_warstwe;

    SeekBar bar3;
    SeekBar bar4;
    SeekBar bar5;
    SeekBar bar6;
    String rozmiar;

    TextView tv;

    TextView tv2;
    TextView tv3;
    TextView tv4;
    TextView tv5;

    Button b;

    float camLayHeight;
    float camHeight;
    float camLayWidth;
    float camWidth;

    double promien;
    double promien_max;
    double srednica;

    double px_cm;

    int param1;
    int param2;
    int param3;
    int param4;

    Scalar bialy = new Scalar(255,255,255);

    boolean param_linia;
    boolean param_niereg;

    private CameraBridgeViewBase kamera;

    AdView mAdView;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trzeci_etap_layout);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        bar3 = (SeekBar) findViewById(R.id.seekBar);
        bar3.setOnSeekBarChangeListener(this);

        bar4 = (SeekBar) findViewById(R.id.seekBar8);
        bar4.setOnSeekBarChangeListener(this);

        bar5 = (SeekBar) findViewById(R.id.seekBar9);
        bar5.setOnSeekBarChangeListener(this);

        bar6 = (SeekBar) findViewById(R.id.seekBar7);
        bar6.setOnSeekBarChangeListener(this);

        tv = (TextView) findViewById(R.id.textView2);

        tv2 = (TextView) findViewById(R.id.textView20);
        tv3 = (TextView) findViewById(R.id.textView24);
        tv4 = (TextView) findViewById(R.id.textView25);
        tv5 = (TextView) findViewById(R.id.textView23);

        b = (Button) findViewById(R.id.button12);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StageThree.this, StageFive.class);
                i.putExtra("px_cm", px_cm);
                if(rozmiar_x>rozmiar_y) i.putExtra("rozmiar_px", rozmiar_x);
                if(rozmiar_x<rozmiar_y) i.putExtra("rozmiar_px", rozmiar_y);
                usun_warstwe=true;

                startActivity(i);
            }
        });



        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.confirm_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount=0.0f;

        dialog.getWindow().setAttributes(lp);

        Button btnNo = (Button) dialog.findViewById(R.id.button2);

        btnNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                px_cm=0;
                dialog.dismiss();

            }
        });

        Button btnYes = (Button) dialog.findViewById(R.id.button3);

        btnYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent i = new Intent(StageThree.this, StageFive.class);
                i.putExtra("px_cm", px_cm);
                i.putExtra("srednica", srednica);
                Toast.makeText(getApplicationContext(),String.valueOf(srednica+", "+px_cm), Toast.LENGTH_SHORT).show();

                usun_warstwe=true;
                startActivity(i);
                dialog.dismiss();

            }
        });










        Bundle extras = getIntent().getExtras();

        // Read the extras data if it's available.
        if (extras != null)
        {
            param_linia = extras.getBoolean("param_linia");
            param_niereg = extras.getBoolean("param_niereg");
            rozmiar = extras.getString("param_rozmiar");
            param1 = extras.getInt("param1");
            param2 = extras.getInt("param2");
            param3 = extras.getInt("param3");
            param4 = extras.getInt("param4");
        }

        if(param_linia) tv.setVisibility(View.INVISIBLE);
        if(!param_linia)
        {
            bar3.setVisibility(View.INVISIBLE);
            bar4.setVisibility(View.INVISIBLE);
            bar5.setVisibility(View.INVISIBLE);
            bar6.setVisibility(View.INVISIBLE);
            tv2.setVisibility(View.INVISIBLE);
            tv3.setVisibility(View.INVISIBLE);
            tv4.setVisibility(View.INVISIBLE);
            tv5.setVisibility(View.INVISIBLE);
            b.setVisibility(View.INVISIBLE);
        }

        kamera = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);
        kamera.setCvCameraViewListener(this);

        Display display = getWindowManager().getDefaultDisplay();
        android.graphics.Point size = new android.graphics.Point();
        display.getSize(size);
        int height = size.y;
        kamera.getLayoutParams().height = height/2;

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_5, this, mLoaderCallback);
    }

    @Override
    protected void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
        kamera.disableView();
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
        kamera.disableView();
    }







    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        if (seekBar.equals(bar3)) {
            pkt1.y = -progress + (camLayHeight/2);
            seekBar.setMax((int)camLayHeight/2);
            //text3.setText("g." + progress);
        }
        if (seekBar.equals(bar4)) {
            pkt1.x = -progress + (camLayWidth/2);
            seekBar.setMax((int)(camLayWidth/2));
            //text8.setText("l." + progress);
        }
        if (seekBar.equals(bar5)) {
            pkt2.x = progress + (camLayWidth/2);
            seekBar.setMax((int)(camLayWidth/2));
            //text9.setText("p." + progress);
        }
        if (seekBar.equals(bar6)) {
            pkt2.y = progress + (camLayHeight/2);
            seekBar.setMax((int)camLayHeight/2);
            //text10.setText("d." + progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status)
        {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:

                {

                    try{
                        InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                        File mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
                        FileOutputStream os = new FileOutputStream(mCascadeFile);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        is.close();
                        os.close();

                        new CascadeClassifier(mCascadeFile.getAbsolutePath());
                    } catch (Exception e) {
                        Log.e("OpenCVActivity", "Error loading cascade", e);
                    }

                    kamera.enableView();

                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        Point center = new Point(mGray.cols() / 2, mGray.rows() / 2);
        double angle = 270;
        double scale = 1.4;
        Mat mapMatrix = Imgproc.getRotationMatrix2D(center, angle, scale);

        plyta = new MatOfPoint();



        if(px_cm==0 && !param_linia && !param_niereg)
        {
            Imgproc.cvtColor(inputFrame.gray(), mRgba, Imgproc.COLOR_GRAY2RGBA, 3);
            Imgproc.warpAffine(mRgba, mRgba, mapMatrix, mRgba.size());
            Imgproc.cvtColor(mRgba, mGray, Imgproc.COLOR_RGBA2GRAY);

            bar3.setVisibility(View.INVISIBLE);
            bar4.setVisibility(View.INVISIBLE);
            bar5.setVisibility(View.INVISIBLE);
            bar6.setVisibility(View.INVISIBLE);

            Imgproc.HoughCircles(mGray, plyta, Imgproc.CV_HOUGH_GRADIENT, 1, 700, param1, param2, param3, param4);
            for (int x = 0; x < plyta.cols(); x++) {

                if(!usun_warstwe)
                {
                    double vCircle[] = plyta.get(0, x);
                    center2 = new Point(Math.round(vCircle[0]), Math.round(vCircle[1]));
                    promien = (double) Math.round((vCircle[2]) * 100) / 100d;
                    Core.circle(mGray, center2, 2, new Scalar(50, 100, 205, 255), 2);
                    promien_max = Math.max(promien, promien);
                    srednica = promien_max * 2;
                    Core.circle(mGray, center2, (int) promien, bialy, 2);
                }

                if(usun_warstwe)
                {
                    center2.x=0.0;
                    center2.y=0.0;
                    Core.circle(mGray, center2, (int) 1, bialy, 10);
                    Highgui.imwrite(Environment.getExternalStorageDirectory().getPath() + "/fota.jpg", mGray);
                }


                px_cm = (double) Math.round((srednica / 12) * 100000) / 100000d;
                Highgui.imwrite(Environment.getExternalStorageDirectory().getPath() + "/fota.jpg", mGray);
            }
        }

        if (px_cm!=0 && !param_linia && !param_niereg && !usun_warstwe)
        {
            Mat y = Highgui.imread(Environment.getExternalStorageDirectory().getPath() + "/fota.jpg");
            Imgproc.cvtColor(y, mGray, Imgproc.COLOR_BGR2GRAY);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog.show();
                }
            });
        }










        if(param_linia && !param_niereg)
        {
            Imgproc.cvtColor(inputFrame.gray(), mRgba, Imgproc.COLOR_GRAY2RGBA, 3);
            Imgproc.warpAffine(mRgba, mRgba, mapMatrix, mRgba.size());
            Imgproc.cvtColor(mRgba, mGray, Imgproc.COLOR_RGBA2GRAY);

            if(usun_warstwe)
            {
                pkt1.x=-1.0;
                pkt1.y=-1.0;
                pkt2.x=-1.0;
                pkt2.y=-1.0;
                Core.rectangle(mGray, pkt1, pkt2, bialy, 1);
                Highgui.imwrite(Environment.getExternalStorageDirectory().getPath() + "/fota.jpg", mGray);
            }
            if(!usun_warstwe) Core.rectangle(mGray, pkt1, pkt2, bialy, 2);
            rozmiar_y = (int) ((pkt2.y - pkt1.y));
            rozmiar_x = (int) ((pkt2.x - pkt1.x));

            if(rozmiar_x>rozmiar_y) px_cm = (double) Math.round((rozmiar_x / Integer.parseInt(rozmiar)) * 100000) / 100000d;
            if(rozmiar_x<rozmiar_y) px_cm = (double) Math.round((rozmiar_y / Integer.parseInt(rozmiar)) * 100000) / 100000d;
        }













        return mGray;
    }

    public void onCameraViewStarted(int width, int height)
    {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        new Mat(height, width, CvType.CV_8UC4);
        mGray = new Mat(height, width, CvType.CV_8UC1);

        camLayHeight = height;
        camHeight = kamera.getHeight();
        camLayWidth = width;
        camWidth = kamera.getWidth();

        pkt1 = new Point(camLayWidth/2,camLayHeight/2);
        pkt2 = new Point(camLayWidth/2,camLayHeight/2);
    }

    public void onCameraViewStopped() {}
}