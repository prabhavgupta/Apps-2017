package com.constant;

import android.location.Location;

import com.entity.UserModel;

public class AndroidConstants {
	public static String MAIN_SERVER_IP = "192.168.0.114";
	public static String MAIN_SERVER_PORT = "8080";

	public static String MAIN_URL() {
		return MAIN_SERVER() + "/LBARetailer/pages/index.jsp";
	}

	public static String REGISTRATION_URL() {
		return MAIN_SERVER() + "/LBARetailer/pages/user_registration.jsp";
	}

	public static String AJAX_URL() {
		return MAIN_SERVER() + "/LBARetailer/tiles/ajax.jsp";
	}

	public static String MAIN_SERVER() {
		return "http://" + AndroidConstants.MAIN_SERVER_IP + ":"
				+ AndroidConstants.MAIN_SERVER_PORT;
	}

	public static UserModel currentUser = null;
	public static String PAGE_2_LOAD = "";
	public static String CURRENT_LOCATION = "";
	public static Location currentLocation = null;

}
