package com.activity;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.Method;

import map.helper.MapGeocoder;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.constant.AndroidConstants;
import com.googlemaphelper.GoogleMapActivity;
import com.helper.StringHelper;

public class WebViewActivity extends CommonActivity {

	private WebView webView;

	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		android.os.StrictMode.ThreadPolicy tp = android.os.StrictMode.ThreadPolicy.LAX;
		android.os.StrictMode.setThreadPolicy(tp);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.webview);
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);

		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setDefaultFontSize(14);
		
		webView.setWebViewClient(new WebViewClient() {
		        @Override
		        public boolean shouldOverrideUrlLoading(WebView view, String url) {
		            return false;
		        }
		    });
		webView.addJavascriptInterface(this, "ActivityObject");   
		webView.setWebChromeClient(new MyJavaScriptChromeClient());

		// TODO Auto-generated method stub

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String cur="";
				if(AndroidConstants.currentLocation!=null){
					cur=AndroidConstants.currentLocation.getLatitude()+","+AndroidConstants.currentLocation.getLongitude();
				}
				webView.loadUrl(AndroidConstants.PAGE_2_LOAD+"?imei="+getIMEI()+"&location="+cur);
				
				
//				webView.loadUrl("http://192.168.0.101/e/login.html");
			}
		});
		
	

		    WebSettings s = webView.getSettings();
            s.setBuiltInZoomControls(true);
            s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            s.setUseWideViewPort(true);
            s.setLoadWithOverviewMode(true);
            s.setSavePassword(true);
            s.setSaveFormData(true);
            s.setJavaScriptEnabled(true);
            
            // enable navigator.geolocation 
            s.setGeolocationEnabled(true);
            s.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");
            
            // enable Web Storage: localStorage, sessionStorage
            s.setDomStorageEnabled(true);
       
            WebSettings ws = webView.getSettings();

            ws.setJavaScriptEnabled(true);
            ws.setAllowFileAccess(true);


            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.ECLAIR) {
                try {
                    Log.d(TAG, "Enabling HTML5-Features");
                    Method m1 = WebSettings.class.getMethod("setDomStorageEnabled", new Class[]{Boolean.TYPE});
                    m1.invoke(ws, Boolean.TRUE);

                    Method m2 = WebSettings.class.getMethod("setDatabaseEnabled", new Class[]{Boolean.TYPE});
                    m2.invoke(ws, Boolean.TRUE);

                    Method m3 = WebSettings.class.getMethod("setDatabasePath", new Class[]{String.class});
                    m3.invoke(ws, "/data/data/" + getPackageName() + "/databases/");

                    Method m4 = WebSettings.class.getMethod("setAppCacheMaxSize", new Class[]{Long.TYPE});
                    m4.invoke(ws, 1024*1024*8);

                    Method m5 = WebSettings.class.getMethod("setAppCachePath", new Class[]{String.class});
                    m5.invoke(ws, "/data/data/" + getPackageName() + "/cache/");

                    Method m6 = WebSettings.class.getMethod("setAppCacheEnabled", new Class[]{Boolean.TYPE});
                    m6.invoke(ws, Boolean.TRUE);

                    Log.d(TAG, "Enabled HTML5-Features");
                }
                catch (NoSuchMethodException e) {
                    Log.e(TAG, "Reflection fail", e);
                }
                catch (InvocationTargetException e) {
                    Log.e(TAG, "Reflection fail", e);
                }
                catch (IllegalAccessException e) {
                    Log.e(TAG, "Reflection fail", e);
                }
            }
	}
	public String selectedFilePath = null;
	int FILE_OPENER_ID = 1234;
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	        Intent intent) {
	    if (requestCode == FILECHOOSER_RESULTCODE) {
	        if (null == mUploadMessage)
	            return;
	        Uri result = intent == null || resultCode != RESULT_OK ? null
	                : intent.getData();
//	        toast("URI "+result.toString());
	        mUploadMessage.onReceiveValue(result);
	        mUploadMessage = null;

	    }else if (requestCode == FILE_OPENER_ID) {

			String filePath = null;
			Uri _uri = intent.getData();
			System.out.println("URI = " + _uri);
			if (_uri != null && "content".equals(_uri.getScheme())) {
				Cursor cursor = this
						.getContentResolver()
						.query(_uri,
								new String[] { android.provider.MediaStore.Images.ImageColumns.DATA },
								null, null, null);
				cursor.moveToFirst();
				filePath = cursor.getString(0);
				cursor.close();
			} else {
				filePath = _uri.getPath();
			}
			this.selectedFilePath = filePath;
			// toast("Selected File Path is " + selectedFilePath);

		}
	}
	String TAG="WebViewActivity";
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}
	private class MyJavaScriptChromeClient extends WebChromeClient {
		
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				final JsResult result) {
			// handle Alert event, here we are showing AlertDialog
			new AlertDialog.Builder(WebViewActivity.this)
					.setTitle("Alert !")
					.setMessage(message)
					.setPositiveButton(android.R.string.ok,
							new AlertDialog.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									// do your stuff
									result.confirm();
								}
							}).setCancelable(false).create().show();
			return true;
		}
		// The undocumented magic method override
		// Eclipse will swear at you if you try to put @Override here
		// For Android 3.0+
		public void openFileChooser(ValueCallback<Uri> uploadMsg) {

			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("*/*");
			WebViewActivity.this.startActivityForResult(
					Intent.createChooser(i, "File Chooser"),
					FILECHOOSER_RESULTCODE);

		}

		// For Android 3.0+
		public void openFileChooser(ValueCallback uploadMsg,
				String acceptType) {
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("*/*");
			WebViewActivity.this.startActivityForResult(
					Intent.createChooser(i, "File Browser"),
					FILECHOOSER_RESULTCODE);
		}

		// For Android 4.1
		public void openFileChooser(ValueCallback<Uri> uploadMsg,
				String acceptType, String capture) {
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("*/*");
			WebViewActivity.this.startActivityForResult(
					Intent.createChooser(i, "File Chooser"),
					WebViewActivity.FILECHOOSER_RESULTCODE);

		}
	}
	private ValueCallback<Uri> mUploadMessage;
	private final static int FILECHOOSER_RESULTCODE = 1;
	@JavascriptInterface
	public void close(){
		finish();
	}
	@JavascriptInterface
	public void showDirections(String lat,String lng,String shopAddress,String shopName){
	 SharedPreferences s=PreferenceManager.getDefaultSharedPreferences(WebViewActivity.this);
//   	 String clat= s.getString("CURRENT_LAT","18.5092106");
//   	 String clng=s.getString("CURRENT_LNG","73.7915289");
//   	 double dlat=StringHelper.n2d(clat);
//   	 double dlng=StringHelper.n2d(clng);
//   	 
   	 double slat=StringHelper.n2d(lat);
   	 double slng=StringHelper.n2d(lng);
//   	 String[] add=MapGeocoder.getLocation(dlat+","+dlng);
//   	 String currentAddr=add[0];
 	Intent intent = new Intent(WebViewActivity.this, GoogleMapActivity.class);
	  String lat1=s.getString("CURRENT_LAT", "");
	  String lng1=s.getString("CURRENT_LNG", "");
	
				intent.putExtra("TYPE", 1);
				intent.putExtra("SHOW_DISTANCE_TIME", true);
				intent.putExtra("src", lat1+","+lng1);
				intent.putExtra("src_title", "Current Location ");
				intent.putExtra("src_desc", "Source");
				
				
				intent.putExtra("dest", lat+","+lng);
				intent.putExtra("dest_title", shopName);
				intent.putExtra("dest_desc", shopAddress);
				intent.putExtra("showDirections",true);
				intent.putExtra("showDirectionsTitle", shopName);
				startActivity(intent);
	}

	@JavascriptInterface
	public void refresh(){

	}

	@JavascriptInterface
	public String getLocation(){
		try{
		if(AndroidConstants.currentLocation!=null){
			String cur=AndroidConstants.currentLocation.getLatitude()+","+AndroidConstants.currentLocation.getLongitude();
//			String addr[]=MapGeocoder.getLocation(cur);
			return cur+"#"+AndroidConstants.CURRENT_LOCATION;
		}else{
			return "";
		}   
		}catch (Exception e) {

		}
		return "";
	}
	

}
