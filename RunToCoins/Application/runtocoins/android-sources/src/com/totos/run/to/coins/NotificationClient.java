
package com.totos.run.to.coins;

import android.content.Intent;
import android.provider.Settings;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import java.io.IOException;
import android.util.Log;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import org.qtproject.qt5.android.bindings.QtActivity;
import java.io.IOException;
import android.util.Log;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.content.Context;
import android.view.TextureView.SurfaceTextureListener;
import android.view.TextureView;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.widget.FrameLayout;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.Display;
import android.view.Surface;

import java.util.Vector;
import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import java.lang.Runnable;



import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;



import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.view.LayoutInflater;


import android.view.MotionEvent;



public class NotificationClient extends org.qtproject.qt5.android.bindings.QtActivity implements  OnFrameAvailableListener, OnGestureListener
{

    private   NotificationClient m_instance;
private View tmpView;;

private IGeoPoint mapCentro=null;

    private  SurfaceTexture st,stm,sts;
    private  Surface mSurface,surfaceScore;
    private  Camera camera;
    private  Canvas mSurfaceCanvas,canvasScore;
        private  Bitmap bitmapMapa=null;
            GestureDetector gestureScanner;

    private WindowSurface win ;
    private int wd=0,hd=0;

    private Vector<Texture> mTextures;

    private MapController myMapController;

                private MapView mapView;
               private  Location posicionGPS;

    private float[] mGravity;
    private float[] mMagnetic;

float angulito,vertical,inclinacion;


private static final int EGL_OPENGL_ES2_BIT = 4;
private static final int EGL_CONTEXT_CLIENT_VERSION = 0x3098;
private static final String TAG = "RenderThread";
private EGLDisplay mEglDisplay;
private EGLSurface mEglSurface;
private EGLContext mEglContext;
private int mProgram;
private EGL10 mEgl;
private GL11 mGl;



private FloatBuffer mVertices;
   private final float[] mVerticesData = {
           0.0f, 5.f, 0.0f, -5.f, -5.f, 0.0f, 5.f, -5.f, 0.0f
   };




@Override
public void onCreate(Bundle savedInstanceState) {
 super.onCreate(savedInstanceState);

 Display display = getWindowManager().getDefaultDisplay();


        wd = display.getWidth();  // deprecated
         hd = display.getHeight();  // deprecated

 mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
 initListeners();


 mSensorManager.registerListener(mEventListenerOrientation,
                        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                        SensorManager.SENSOR_DELAY_UI);


 mSensorManager.registerListener(mEventListenerOrientation,
                        mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                        SensorManager.SENSOR_DELAY_UI);
        gestureScanner = new GestureDetector(this);
 Log.w("","inicia sensores qaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa ");


 mTextures = new Vector<Texture>();
 mTextures.add(Texture.loadTextureFromApk("moneda.png", getAssets()));//0
 mTextures.add(Texture.loadTextureFromApk("texturaflecha.png", getAssets()));//1


 loadTexture();




 LayoutInflater inflater = getLayoutInflater();

 tmpView = inflater.inflate(R.layout.activity_openglndk, null);




 addContentView(tmpView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.FILL_PARENT));

      myMapController = (MapController)mapView.getController();


    myMapController.setZoom(19);
    myMapController.stopAnimation(false);






 locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

if(locationManager != null)
{

        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(lastLocation != null){
           updateLoc(lastLocation);
        }


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, myLocationListener);

}

getWindow().getDecorView().setSystemUiVisibility(
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

}
private     LocationManager locationManager;
private LocationListener myLocationListener  = new LocationListener(){

@Override
public void onLocationChanged(Location location) {
  // TODO Auto-generated method stub
      posicionGPS=location;

if(location!=null)
  updateLoc(location);

 //Log.w("llehue","actualiza posicion");

}

@Override
public void onProviderDisabled(String provider) {
  // TODO Auto-generated method stub

}

@Override
public void onProviderEnabled(String provider) {
  // TODO Auto-generated method stub

}

@Override
public void onStatusChanged(String provider, int status, Bundle extras) {
  // TODO Auto-generated method stub

}

};



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
           //sensores
               private  SensorManager mSensorManager;


               private  SensorEventListener mEventListenerOrientation;



               private  void initListeners() {


                       mEventListenerOrientation = new SensorEventListener() {




                               @Override
                               public void onAccuracyChanged(Sensor sensor, int accuracy) {
                               }

                               @Override
                               public void onSensorChanged(SensorEvent event) {
                                         switch(event.sensor.getType()) {
                                       case Sensor.TYPE_ACCELEROMETER:
                                           mGravity =  event.values.clone(),mGravity);
                                           break;
                                       case Sensor.TYPE_MAGNETIC_FIELD:
                                           mMagnetic = event.values.clone(),mMagnetic);

                                           break;
                                       default:
                                           return;
                                       }

                                       if(mGravity != null && mMagnetic != null) {
                                         updateDirection();

                                       }

                                    

                               }
                       };


               }

                  private void updateDirection() {
                       float[] temp = new float[9];
                       float[] R = new float[9];
                       //Load rotation matrix into R
                       SensorManager.getRotationMatrix(temp, null, mGravity, mMagnetic);
                       //Remap to camera's point-of-view
                       SensorManager.remapCoordinateSystem(temp, SensorManager.AXIS_X, SensorManager.AXIS_Z, R);
                       //Return the orientation values
                       float[] values = new float[3];
                       SensorManager.getOrientation(R, values);
                       //Convert to degrees
                       for (int i=0; i < values.length; i++) {
                           Double degrees = (values[i] * 180) / Math.PI;
                           values[i] = degrees.floatValue();
                       }
                       //Display the compass direction


                  angulito=values[0];
                  vertical=values[1] /50;
                  inclinacion=values[2] ;


                  if(vertical>1.1)
                  tmpView.setVisibility(View.VISIBLE);
                  else
                  tmpView.setVisibility(View.INVISIBLE);


                    if(posicionGPS!=null)
                  valores(posicionGPS.getLongitude(), posicionGPS.getLatitude(), angulito, vertical, inclinacion);


                  }
          

                  /** Returns the number of registered textures. */
                  public int getTextureCount()
                  {
                      return mTextures.size();
                  }


                  /** Returns the texture object at the specified index. */
                  public Texture getTexture(int i)
                  {
                      return mTextures.elementAt(i);
                  }


  public void checkGPS()
  {

      String locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
      if (locationProviders == null || locationProviders.equals("")) {
         
         flagGPS(0);

      }
  else
  {
     flagGPS(1);
     }

   // return true;

   }


public  void Inicia()
{



          stm = new SurfaceTexture(numtexturaMap());

    mSurface=new Surface(stm);
    stm.setOnFrameAvailableListener(this);
              stm.setDefaultBufferSize(wd, hd);

              mSurface = new Surface(stm);





         startCamera(numtextura());

}




private  void startCamera(int texture)
      {
         st = new SurfaceTexture(numtextura());
          st.setOnFrameAvailableListener(this);
          camera = Camera.open();


          try
          {
              camera.setPreviewTexture(st);

              Camera.Parameters parameters = camera.getParameters();
              parameters.setPreviewSize(640,480);
              camera.setParameters(parameters);

            camera.startPreview();


              Log.w("camara","camara num "+camera.getNumberOfCameras());
          }
          catch (IOException ioe)
          {
              Log.w("MainActivity","CAM LAUNCH FAILED");
          }


      }


  /** Send touch events to  native. */
             @Override
             public boolean onTouchEvent(MotionEvent e)
             {

    Log.w("","touch "+e.getX()+" "+e.getY()+" "+mapCentro==null?"true":"false");
    if(mapCentro!=null)
    addCoin(e.getX(),e.getY(),mapCentro.getLongitude() ,mapCentro.getLatitude() );


                 return gestureScanner.onTouchEvent(e);
             }

                 @Override
                 public boolean onDown(MotionEvent e) {
                         // TODO Auto-generated method stub
                         return false;
                 }

                 @Override
                 public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                 float velocityY) {
                         // TODO Auto-generated method stub
                         return false;
                 }

                 @Override
                 public void onLongPress(MotionEvent e) {
                         // TODO Auto-generated method stub

                 }

                 @Override
                 public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                 float distanceX, float distanceY) {

                                    // Log.w("","touch "+e1.getX()+" "+e1.getY());
                         return false;
                 }

                 @Override
                 public void onShowPress(MotionEvent e) {
                         // TODO Auto-generated method stub


                 }

                 @Override
                 public boolean onSingleTapUp(MotionEvent e) {
                         // TODO Auto-generated method stub
                         return false;
                 }
                  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



public   void Paso()
{




    synchronized(this)
    {

        st.updateTexImage();





                    if(bitmapMapa!=null)
                                        {
                                        onDrawViewBegin();
                                        onDrawViewEnd();
                                        }

                                        stm.updateTexImage();


              }

}

public Canvas onDrawViewBegin(){
    mSurfaceCanvas = null;
    if (mSurface != null) {
        try {
            mSurfaceCanvas = mSurface.lockCanvas(null);



            mSurfaceCanvas.drawBitmap(bitmapMapa, 0, 0, null);

        }catch (Exception e){
            Log.e(TAG, "error while rendering view to gl: " + e);
        }
    }
    return mSurfaceCanvas;
}

public void onDrawViewEnd(){
    if(mSurfaceCanvas != null) {
        mSurface.unlockCanvasAndPost(mSurfaceCanvas);
    }
    mSurfaceCanvas = null;
}


public   SurfaceTexture getST()
{

     st = new SurfaceTexture(numtextura());
return st;
}

private native void addCoin(float x,float y,double mx,double my);

    private  native int numtextura();
    private  native int numtexturaMap();
    private native void loadTexture();

    private native void setPos(double a, double b);

    private native void valores(double a, double b, float c, float d, float e);

private native void flagGPS(int a);

private int   _updateTexImageCounter = 0;
                @Override
                public void onFrameAvailable(SurfaceTexture sta) {

                         _updateTexImageCounter++;



                }



           private void updateLoc(Location loc){

               GeoPoint locGeoPoint = new GeoPoint(loc.getLatitude(), loc.getLongitude());
               myMapController.setCenter(locGeoPoint);


               mapView.invalidate();


               mapView.setEnabled(false);
               mapView.setClickable(false);



               mapView.setWillNotCacheDrawing(false);
               mapView.destroyDrawingCache();
               mapView.buildDrawingCache();


               if(mapView.getDrawingCache()!=null) {


                 mapCentro=  mapView.getMapCenter();

                 setPos(loc.getLongitude(),loc.getLatitude());



               Bitmap cachedImage = Bitmap.createBitmap(mapView.getDrawingCache());

               bitmapMapa=cachedImage;

               }

                              }

}
