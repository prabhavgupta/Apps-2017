package com.helper;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class HttpView {
	static String TAG = "HttpView";

	public static Bitmap drawable_from_url(String url) {
		Bitmap x = null;
		try {
			Log.v(TAG, url);
			HttpURLConnection connection = (HttpURLConnection) new URL(url)
					.openConnection();
			connection.setRequestProperty("User-agent", "Mozilla/4.0");
			connection.connect();
			InputStream input = connection.getInputStream();
			x = BitmapFactory.decodeStream(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return x;
	}
	  public static String uploadFile(String sourceFileUri,String upLoadServerUri) {
       	 String result=null;
	          String fileName = sourceFileUri;
	          HttpURLConnection conn = null;
	          DataOutputStream dos = null; 
	          String lineEnd = "\r\n";
	          String twoHyphens = "--";
	          String boundary = "*****";
	          int bytesRead, bytesAvailable, bufferSize;
	          byte[] buffer;
	          int maxBufferSize = 1 * 1024 * 1024;
	          File sourceFile = new File(sourceFileUri);	           
	          if (!sourceFile.isFile()) {
	               return "-1";
	          }
	          else
	          {
	               try {	                    
	                     // open a URL connection to the Servlet
	                   FileInputStream fileInputStream = new FileInputStream(sourceFile);
	                   URL url = new URL(upLoadServerUri);	                    
	                   // Open a HTTP  connection to  the URL
	                   conn = (HttpURLConnection) url.openConnection();
	                   conn.setDoInput(true); // Allow Inputs
	                   conn.setDoOutput(true); // Allow Outputs
	                   conn.setUseCaches(false); // Don't use a Cached Copy
	                   conn.setRequestMethod("POST");
	                   conn.setRequestProperty("Connection", "Keep-Alive");
	                   conn.setRequestProperty("ENCTYPE", "multipart/form-data");
	                   conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
	                   conn.setRequestProperty("uploaded_file", fileName);
	                    
	                   dos = new DataOutputStream(conn.getOutputStream());
	          
	                   dos.writeBytes(twoHyphens + boundary + lineEnd);
	                   dos.writeBytes("Content-Disposition: form-data; name=uploaded_file;filename=\""
	                                             + fileName + "\"" + lineEnd);
	                    
	                   dos.writeBytes(lineEnd);
	          
	                   // create a buffer of  maximum size
	                   bytesAvailable = fileInputStream.available();
	          
	                   bufferSize = Math.min(bytesAvailable, maxBufferSize);
	                   buffer = new byte[bufferSize];
	          
	                   // read file and write it into form...
	                   bytesRead = fileInputStream.read(buffer, 0, bufferSize); 
	                      
	                   while (bytesRead > 0) {
	                        
	                     dos.write(buffer, 0, bufferSize);
	                     bytesAvailable = fileInputStream.available();
	                     bufferSize = Math.min(bytesAvailable, maxBufferSize);
	                     bytesRead = fileInputStream.read(buffer, 0, bufferSize);  	                      
	                    }
	          
	                   // send multipart form data necesssary after file data...
	                   dos.writeBytes(lineEnd);
	                   dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
	                    // Responses from the server (code and message)
	              int     serverResponseCode = conn.getResponseCode();
	                   String serverResponseMessage = conn.getResponseMessage();
	                     
	                   Log.i("uploadFile", "HTTP Response is : "
	                           + serverResponseMessage + ": " + serverResponseCode);
	                    
	                   if(serverResponseCode == 200){
	                       InputStream is=conn.getInputStream();
	                       byte[] b=new byte[1024];
	                       is.read(b);
	                       result=new String(b);
	                       result=result.trim();
	                       Log.i("uploadFile", "HTTP Response is : "
	                               + result);
	                   }   
	                    
	                   //close the streams //
	                   fileInputStream.close();
	                   dos.flush();
	                   dos.close();
	                     
	              } catch (MalformedURLException ex) {
	                   
	              
	                  ex.printStackTrace();
	                   
	                         System.out.println("MalformedURLException Exception : check script url.");
	                    
	                   
	                  Log.e("Upload file to server", "error: " + ex.getMessage(), ex); 
	              } catch (Exception e) {
	                   
	            
	                  e.printStackTrace();
	                   
	              
	                  Log.e("Upload file to server Exception", "Exception : "
	                                                   + e.getMessage(), e); 
	              }

	              return result;
	               
	           } // End else block
	         }
	    
	public static boolean checkConnectivityServer(String ip, int port) {
		boolean success = false;
		try {
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
	public static Object connect2ServerObject(String url) {
		Log.v(TAG, "Reading Object");
		Log.v(TAG, url);
		Object o = null;
		URL u;
		try {
			u = new URL(url);
			
			URLConnection uc = u.openConnection();
			
			ObjectInputStream ois = new ObjectInputStream(uc.getInputStream());
			o = ois.readObject();
			System.out.println(o);
			u = null;
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return o;
	}

}
