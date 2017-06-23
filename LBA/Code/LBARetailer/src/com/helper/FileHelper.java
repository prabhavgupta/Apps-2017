package com.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;

public class FileHelper {

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

	public static void createLog(String msg) throws IOException {
		/*
		 * Function to initially create the log file and it also writes the time
		 * of creation to file.
		 */
		//File Root = Environment.getExternalStorageDirectory();
		File Root = new File("D:/test");
		if (Root.canWrite()) {
			File LogFile = new File(Root, "NewLog.txt");
			FileWriter LogWriter = new FileWriter(LogFile, true);
			BufferedWriter out = new BufferedWriter(LogWriter);
			Date date = new Date();
			System.out.println("in log file create  function ........");

			out.write("Logged at" + date.toLocaleString());
			// String
			// msg="\n\n\nSource  :-    "+from+"\n"+"Destination :-  "+to+"\n"+"Total Fare   "+fare+"\n"+"Diatance   "+distancee+"Requried  Time  to cover distance "+time;
			// System.out.println(msg);
			out.write(msg);
			out.flush();
			out.close();
		}
	}

}
