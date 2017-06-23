package com.helper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;


/*
 */
public class StringHelper {
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
	public static int countOccurrences(String haystack, char needle)
	{
	    int count = 0;
	    for (int i=0; i < haystack.length(); i++)
	    {
	        if (haystack.charAt(i) == needle)
	        {
	             count++;
	        }
	    }
	    return count;
	}
	public static String nullToStringNull(String d){
		String ret = d;
		if(ret==null){
			ret = "NULL";
		}
		return ret;
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
	public static double n2d(Object d){
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
	public static boolean nullToBooleanEmpty(String d){
		boolean ret = false;
		if(d==null){
			ret =  false;
		}else{
			d=d.trim();
			ret=new Boolean(d).booleanValue();
		}
		
		return ret;
	}
	public static String emptyToStringNull(String d){
		String ret = d;
		if(ret.equals("")){
			ret = "NULL";
		}
		return ret;
	}
	
	public static String nullToStringEmpty(String d){
		String ret = d;
		if(ret==null){
			ret =  "";
		}
		
		return ret;
	}
	public static String nullObjectToStringEmpty(Object d){
		String dual="";
		if(d==null){
			dual =  "";
		}
		else
			dual=d.toString().trim();
		
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
	public static int nullObjectToIntegerEmpty(Object d){
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


    
}


