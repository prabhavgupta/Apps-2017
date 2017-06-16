package pl.jawegiel.mierzenieopencv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

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
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


public class StageTwo extends Activity implements CameraBridgeViewBase.CvCameraViewListener2, SeekBar.OnSeekBarChangeListener {


    int p5 = 480;

    private Mat mRgba;
    private Mat mGray;

    private CameraBridgeViewBase kamera;

    Scalar czarny = new Scalar(0,0,0);
    Scalar szary = new Scalar(127,127,127);

    boolean plytaok;

    MatOfPoint plyta;
    MatOfPoint cialo;

    double promien;
    double promien_max;
    double srednica;

    TextView text4;
    TextView text6;
    SeekBar bar1;
    SeekBar bar2;
    SeekBar bar01;
    SeekBar bar02;
    Button button1;

    int param1=100;
    int param2=100;
    int param3=0;
    int param4=0;


    AdView mAdView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drugi_etap_layout);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

        text4 = (TextView) findViewById(R.id.textView4);
        text4.setText("min(r)="+param3+"px\nmax(r)="+param4+"px");
        text6 = (TextView) findViewById(R.id.textView6);
        text6.setText("p1 = "+param1+"\np2 = "+ param2);



        bar1 = (SeekBar) findViewById(R.id.seekBar1);
        bar1.setOnSeekBarChangeListener(this);

        bar2 = (SeekBar) findViewById(R.id.seekBar2);
        bar2.setOnSeekBarChangeListener(this);

        bar01 = (SeekBar) findViewById(R.id.SeekBar01);
        bar01.setOnSeekBarChangeListener(this);

        bar02 = (SeekBar) findViewById(R.id.SeekBar02);
        bar02.setOnSeekBarChangeListener(this);

        button1 = (Button) findViewById(R.id.button2);
        button1.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //Toast.makeText(getApplicationContext(), "a", Toast.LENGTH_SHORT).show();
                kamera.disableView();
                Intent i = new Intent(StageTwo.this, StageThree.class);
                i.putExtra("param1", param1);
                i.putExtra("param2", param2);
                i.putExtra("param3", param3);
                i.putExtra("param4", param4);
                startActivity(i);

            }
        });

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
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
        if (kamera != null)
            kamera.disableView();
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
        if (kamera != null)
            kamera.disableView();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {
        if(seekBar.equals(bar1))
        {
            param1= progress+1; text6.setText("p1 = "+param1+"\np2 = "+ param2);
        }
        if(seekBar.equals(bar2))
        {
            param2= progress+1; text6.setText("p1 = "+param1+"\np2 = "+ param2);
        }
        if(seekBar.equals(bar01))
        {
            param3 = progress; text4.setText("min(r)="+param3+"px\nmax(r)="+param4+"px");
            if(param3>param4)param3=param4;
        }
        if(seekBar.equals(bar02))
        {
            param4 = progress; text4.setText("min(r)="+param3+"px\nmax(r)="+param4+"px");
            p5 = 480-param4;
        }
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
        cialo = new MatOfPoint();

                    Imgproc.cvtColor(inputFrame.gray(), mRgba, Imgproc.COLOR_GRAY2RGBA, 3);
                    Imgproc.warpAffine(mRgba, mRgba, mapMatrix, mRgba.size());
                    Imgproc.cvtColor(mRgba, mGray, Imgproc.COLOR_RGBA2GRAY);

        if (!plytaok) {

            Core.putText(mGray, "<= CD pr. <=", new Point(145, 378), Core.FONT_HERSHEY_TRIPLEX, 0.8, szary);

            Core.line(mGray, new Point(0, 370), new Point(param3, 370), szary, 2);
            Core.putText(mGray, param3 + "px", new Point(50, 400), Core.FONT_HERSHEY_TRIPLEX, 0.6, szary);
            Core.line(mGray, new Point(p5, 370), new Point(480, 370), szary, 2);
            Core.putText(mGray, param4 + "px", new Point(380, 400), Core.FONT_HERSHEY_TRIPLEX, 0.6, szary);

            Imgproc.HoughCircles(mGray, plyta, Imgproc.CV_HOUGH_GRADIENT, 1, 700, param1, param2, param3, param4);
            for (int x = 0; x < plyta.cols(); x++)
            {
                double vCircle[] = plyta.get(0, x);
                Point center2 = new Point(Math.round(vCircle[0]), Math.round(vCircle[1]));
                promien = (double) Math.round((vCircle[2]) * 100) / 100d;
                Core.circle(mGray, center2, 2, new Scalar(50, 100, 205, 255), 2);
                Core.circle(mGray, center2, (int) promien, czarny, 2);
                promien_max = Math.max(promien, promien);
                srednica = promien_max * 2;
            }
        }

            return mGray;
        }

    public void onCameraViewStarted(int width, int height)
    {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        new Mat(height, width, CvType.CV_8UC4);
        mGray = new Mat(height, width, CvType.CV_8UC1);

    }

    public void onCameraViewStopped() {}


}