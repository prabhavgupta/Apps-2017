package com.helper;

import java.awt.AWTException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletRequest;


/*
 */
public class StringHelper {
	public static String formatDBDate(String cdate){
		
		return parseDate(parseDate(cdate));
	}

	public static Date parseDate(String cdate){
		String pattern = "yyyy-MM-dd HH:mm:ss";	//2015-02-20 16:50:38
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		Date lastDate=null;
		try {
			lastDate=simpleDateFormat.parse(cdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lastDate;
	}
	public static String parseDate(Date cdate){
		String pattern = "dd-MM";	//2015-02-20 16:50:38
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String lastDate=null;
		try {
			lastDate=simpleDateFormat.format(cdate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lastDate;
	}
	
	
	
	public static void main(String[] args) throws AWTException {

		System.out.println(formatDBDate("2015-03-16 00:00:00"));
	}
	public static String n2s(String d){
		String dual="";
		if(d==null){
			dual =  "";
		}
		else
			dual=d.toString().trim();
		
		return dual;
	}
	public static boolean  n2b(Object d){
		boolean  dual=false;
		if(d==null){  
			dual =  false;
		}
		else
			dual=new Boolean(d.toString()).booleanValue();
		
		return dual;
	}
	public static String n2s(Object d){
		String dual="";
		if(d==null){
			dual =  "";
		}
		else
			dual=d.toString().trim();
		
		return dual;
	}
	public static float  nullObjectToFloatEmpty(Object d){
		float i=0;
		if(d!=null){
			String dual=d.toString().trim();
			try{
				i=new Float(dual).floatValue();
			}catch (Exception e) {
				System.out.println("Unable to find integer value");	
			}
		}
		return i;
	}
	public static double  n2d(Object d){
		double i=0;
		if(d!=null){
			String dual=d.toString().trim();
			try{
				i=new Double(dual).doubleValue();
			}catch (Exception e) {
				System.out.println("Unable to find integer value");	
			}
		}
		return i;
	}
	public static float  n2f(Object d){  
		float i=0;
		if(d!=null){
			String dual=d.toString().trim();
			try{
				i=new Float(dual).floatValue();
			}catch (Exception e) {
				System.out.println("Unable to find integer value");	
			}
		}
		return i;
	}
	public static int n2i(Object d){
		int i=0;
		if(d!=null){
			String dual=d.toString().trim();
			try{
				i=new Integer(dual).intValue();
			}catch (Exception e) {
				System.out.println("Unable to find integer value");	
			}
		}
		return i;
	}
	public static HashMap displayRequest(ServletRequest request){

		  Enumeration paraNames = request.getParameterNames();

		   System. out.println(" ------------------------------");

		   System. out.println("parameters:");

		    HashMap parameters=new HashMap();
		    

		    String pname;

		    String pvalue;

		    while (paraNames.hasMoreElements()) {

		      pname = (String)paraNames.nextElement();

		      pvalue = request.getParameter(pname);

		      System.out.println(pname + " = " + pvalue + "");

		      parameters.put(pname, pvalue);
		    }
		    
		    Enumeration<String> requestAttributes = request.getAttributeNames();
	        while (requestAttributes.hasMoreElements()) {
	        	try{
	            String attributeName = n2s(requestAttributes.nextElement());
	            String attributeValue = n2s(request.getAttribute(attributeName));
	     
	            parameters.put(attributeName, attributeValue);
	        	}catch (Exception e) {
					// TODO: handle exception
				}
	        	
	        }

		   System.out.println(" ------------------------------");   
		   return parameters;
		  }
	public static StringBuffer readURLContent(String url) {
		System.out.println("URL " + url);
		StringBuffer json = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new URL(url).openStream()));
			String line;
			while ((line = in.readLine()) != null) {
				json.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
}


