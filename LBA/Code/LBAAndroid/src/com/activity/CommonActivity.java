package com.activity;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.constant.AndroidConstants;
import com.helper.HttpView;
import com.helper.StringHelper;

@SuppressLint("NewApi")
public class CommonActivity extends Activity {

	public void sendSMS(String phoneNo, String message) {
		try{
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(phoneNo, null, message, null, null);
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	@JavascriptInterface
	public void goClass(String c1) {
		Class c = null;
		try {
			System.out.println("i am in here " + c1);
			c = Class.forName(c1);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Intent intent = new Intent(CommonActivity.this, c);
		startActivity(intent);
	}

	public void go(Class c) {
		Intent intent = new Intent(CommonActivity.this, c);
		startActivity(intent);

	}

	@JavascriptInterface
	public boolean checkConnectivityServer() {
		boolean success = checkConnectivityServer(
				AndroidConstants.MAIN_SERVER_IP,
				StringHelper
						.nullObjectToIntegerEmpty(AndroidConstants.MAIN_SERVER_PORT));
		return success;

	}

	public static boolean checkConnectivityServer(String ip, int port) {
		boolean success = false;
		try {
			System.out.println("Checking Connectivity With " + ip + " " + port);
			Socket soc = new Socket();
			SocketAddress socketAddress = new InetSocketAddress(ip, port);
			soc.connect(socketAddress, 3000);
			success = true;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println(" Connecting to server " + success);
		return success;

	}

//	public void toast(String message) {
//		Toast t = Toast.makeText(CommonActivity.this, message, 500);
//		t.show();
//	}

	ProgressDialog progressDialog = null;
	AlertDialog alertDialog = null;

	class CheckConnectivityAsyncTask extends AsyncTask<String, String, String> {
		String message = "";
		String title = "";
		String action = "";

		@Override
		protected void onPreExecute() {
			System.out.println("In Aysnc");
			progressDialog = ProgressDialog.show(CommonActivity.this,
					"Please Wait", "Loading....", true);
			alertDialog = new AlertDialog.Builder(CommonActivity.this).create();
		}

		@Override
		protected String doInBackground(String... params) {
			String ip = params[0];
			int port = StringHelper.nullObjectToIntegerEmpty(params[1]);
			boolean success = HttpView.checkConnectivityServer(ip, port);

			if (success) {

				title = "Success";
				if (params.length > 2 && params[2].equalsIgnoreCase("UpdateIp")) {
					action = "1";
					message = "Connection established with the Main Server.";
					AndroidConstants.MAIN_SERVER_IP = ip;
					AndroidConstants.MAIN_SERVER_PORT = port + "";
				} else {
					message = "Internet Connection Successful!";
				}
			} else {
				action = "";
				message = "Error Connecting to Server http://" + ip + ":"
						+ port;
				title = "Connectivity Error";
			}

			return success + "";
		}

		@Override
		protected void onPostExecute(String result) {
			progressDialog.dismiss();
			alertDialog.setTitle(title);
			alertDialog.setMessage(message);
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					alertDialog.hide();
					if (action.length() > 0) {

						Intent main = new Intent(CommonActivity.this,
								WelcomeActivity.class);
						startActivity(main);
						finish();

					}

				}
			});
			alertDialog.show();

		};

	}

	@JavascriptInterface
	public void finished() {
		try {
			System.runFinalizersOnExit(true);
			finish();
			super.finish();
			super.onDestroy();
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			android.os.Process.killProcess(android.os.Process.myPid());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@JavascriptInterface
	public String getIMEI() {
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		System.out.println("Device IMEI is " + imei);
		return imei;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.welcome, menu);
		return super.onCreateOptionsMenu(menu);
	}

}
