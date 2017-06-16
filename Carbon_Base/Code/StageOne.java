package pl.jawegiel.mierzenieopencv;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import android.graphics.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class StageOne extends Activity implements CvCameraViewListener2 {
    
    private static final int IMAGE_CAPTURE = 0;

    private Mat mRgba;
    private Mat mGray;

    Dialog dialog;
    Dialog dialog2;

    DBManager dbManager;

    EditText et1;
    EditText et2;

    private MenuItem tryb_info;

    private CameraBridgeViewBase kamera;
    Scalar czarny = new Scalar(0,0,0);

    TextView text1;
    TextView wzorzec;
    TextView typ;
    TextView nazwa_wzorca;
    TextView rozmiar_wzorca;
    String rozmiar="12";
    Button button1;
    Button button2;

    boolean okrag;
    boolean linia;
    boolean niereg;

    Button b8;
    Button b10;
    Button b11;

    Cursor cursor;


    private SimpleCursorAdapter adapter;

    Intent i;
    
    MatOfPoint plyta;
    MatOfPoint cialo;

    boolean isOpenCVInstalled;

    AdView mAdView;


    final String[] from = new String[] { DatabaseHelper._ID, DatabaseHelper.NAME, DatabaseHelper.REAL_CM };

    final int[] to = new int[] { R.id.textView17, R.id.textView13, R.id.textView14 };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.pierwszy_etap_layout);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

        typ = (TextView)findViewById(R.id.textView16);


        wzorzec = (TextView)findViewById(R.id.textView26);
        wzorzec.setText(getResources().getString(R.string.default_benchmark));

        isOpenCVInstalled = appInstalledOrNot("org.opencv.engine");
        if(!isOpenCVInstalled) Toast.makeText(this, "OpenCV Manager is required.", Toast.LENGTH_SHORT).show();





        b8 = (Button)findViewById(R.id.button8);
        b8.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.show();
            }
        });





        dbManager = new DBManager(this);
        dbManager.open();

        cursor = dbManager.fetch();

        LayoutInflater inflater = getLayoutInflater();
        View view= inflater.inflate(R.layout.benchmark_layout, null);



        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.benchmark_layout);
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.activity_background));

        dialog2 = new Dialog(this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.typ_rozpoznania_layout);
        dialog2.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.activity_background));


        //okrag
        b10 = (Button)dialog2.findViewById(R.id.button10);
        b10.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                typ.setText(getResources().getString(R.string.circle));
                okrag=true; linia=false; niereg=false;
                dialog2.dismiss();
            }
        });

        //linia
        b11 = (Button)dialog2.findViewById(R.id.button11);
        b11.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                typ.setText(getResources().getString(R.string.line));
                okrag=false; linia=true; niereg=false;
                dialog2.dismiss();
            }
        });

        final ListView lv= (ListView)dialog.findViewById(R.id.listView1);
        Button b3 = (Button)dialog.findViewById(R.id.button2);
        b3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                et1 = (EditText)dialog.findViewById(R.id.editText);
                et2 = (EditText)dialog.findViewById(R.id.editText2);
                if(!et1.getText().toString().equals("") && !et2.getText().toString().equals(""))
                {
                    dbManager.insert(et1.getText().toString(), et2.getText().toString());
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.add), Toast.LENGTH_SHORT).show();
                    cursor.requery();
                    dialog.dismiss();
                }
                else
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_add), Toast.LENGTH_SHORT).show();
            }
        });


        adapter = new SimpleCursorAdapter(this, R.layout.benchmark_entry_layout, cursor, from, to, 0);
        adapter.notifyDataSetChanged();


        lv.setAdapter(adapter);
        registerForContextMenu(lv);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                nazwa_wzorca = (TextView) dialog.findViewById(R.id.textView13);
                rozmiar_wzorca = (TextView) dialog.findViewById(R.id.textView14);

                Cursor c = (Cursor)lv.getItemAtPosition(i);


                wzorzec.setText(c.getString(1));
                rozmiar=c.getString(2);
                dialog.dismiss();
            }
        });





        kamera = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);
        kamera.setCvCameraViewListener(this);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        kamera.getLayoutParams().height = height/2;

        text1 = (TextView) findViewById(R.id.textView1);
        text1.setTextSize(18);
        text1.setTextColor(Color.BLACK);




        button1 = (Button) findViewById(R.id.button1);
        button1.setVisibility(SurfaceView.VISIBLE);
        button1.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick(View view) {

                if(okrag)
                {
                    Intent i = new Intent(StageOne.this, IntersitialAcivity.class);
                    i.putExtra("param_okrag", okrag);
                    startActivity(i);
                    //startActivity(new Intent(StageOne.this, IntersitialAcivity.class));
                    //startActivity(new Intent(StageOne.this, StageTwo.class));
                }
                if(linia)
                {
                    //startActivity(new Intent(StageOne.this, IntersitialAcivity.class));
                    Intent i = new Intent(StageOne.this, IntersitialAcivity.class);
                    i.putExtra("param_linia", linia);
                    i.putExtra("param_rozmiar", rozmiar);
                    startActivity(i);
                }
                if(niereg)
                {
                    Intent i = new Intent(StageOne.this, StageThree.class);
                    i.putExtra("param_niereg", niereg);
                    startActivity(i);
                }
                if(!okrag && !linia && !niereg)
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.set_rec_first), Toast.LENGTH_SHORT).show();

            }
        });

        button2 = (Button) findViewById(R.id.button7);
        button2.setVisibility(SurfaceView.VISIBLE);
        button2.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick(View view) {


                dialog.show();

            }
        });



    }



    @Override public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // second argument show what item was selected
        menu.add(0, 0, 1, "Usunąć?").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == 0) {
                    AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                    if(acmi.id==1)
                        Toast.makeText(StageOne.this.getBaseContext(),getResources().getString(R.string.no_del),Toast.LENGTH_LONG).show();
                    else
                    {
                        dbManager.delete(acmi.id);
                        Toast.makeText(StageOne.this.getBaseContext(), getResources().getString(R.string.del) + String.valueOf(acmi.id), Toast.LENGTH_LONG).show();
                        cursor.requery();
                        dialog.dismiss();
                    }



                    // get item id from listView if needed

                    //init();
                    return true;
                }
                return false;
            }
        });
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
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("program_main");
        tryb_info = menu.add("o aplikacji");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item == tryb_info) {
            startActivity(new Intent(StageOne.this, Info.class));
        }

        return (super.onOptionsItemSelected(item));
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







    public void onCameraViewStarted(int width, int height)
    {
        mRgba = new Mat(800, 800, CvType.CV_8UC4);
        mGray = new Mat(800, 800, CvType.CV_8UC1);
    }
    
    public void onCameraViewStopped() {}












    public Mat onCameraFrame(CvCameraViewFrame inputFrame)
    {
        
        org.opencv.core.Point center = new org.opencv.core.Point(mGray.cols()/2,mGray.rows()/2);
        double angle = 270;
        double scale = 1.4;
        Mat mapMatrix = Imgproc.getRotationMatrix2D(center, angle, scale);
        
        plyta = new MatOfPoint();
        cialo = new MatOfPoint();

    
    Imgproc.cvtColor(inputFrame.gray(), mRgba, Imgproc.COLOR_GRAY2RGBA, 3);    
    Imgproc.warpAffine(mRgba, mRgba, mapMatrix, mRgba.size());   
    Imgproc.cvtColor(mRgba, mGray, Imgproc.COLOR_RGBA2GRAY);

    Imgproc.dilate(mGray, mGray, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(90,150)));




        
        return mGray;
    }





	
	
	
	
    public void startCamera() {
        Log.d("ANDRO_CAMERA", "Starting camera on the phone...");
        String fileName = "testphoto.jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION,
                "Image capture by camera");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, IMAGE_CAPTURE);
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try
        {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {}

        return false;
    }

}
