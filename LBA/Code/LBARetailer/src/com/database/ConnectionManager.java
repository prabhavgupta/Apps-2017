/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.database;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import com.constant.ServerConstants;
import com.entity.AdvertisementModel;
import com.entity.ProductModel;
import com.entity.RetailerMasterModel;
import com.entity.UserAccountModel;
import com.entity.UserModel;
import com.entity.WhishlistModel;
import com.helper.ReverseGeocoder;
import com.helper.SimpleCryptoAndroidJava;
import com.helper.StringHelper;

/**
 * 
 * @author Admin
 */
public class ConnectionManager extends DBUtils {

	public static Connection getDBConnection() {
		Connection conn = null;
		try {
			Class.forName(ServerConstants.db_driver);
			conn = DriverManager.getConnection(ServerConstants.db_url,
					ServerConstants.db_user, ServerConstants.db_pwd);
			System.out.println("Got Connection");
		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(
					null,
					"Please start the mysql Service from XAMPP Console.\n"
							+ ex.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return conn;
	}

	public static void insertWishlist(String adsId, String userId) {
		String q = " insert into whishlist(adsId, userId) values (?,?)";
		executeUpdate(q, adsId, userId);

	}

	public static ArrayList getAdvertisements(String retailerId) {

		String q = "SELECT *,date_format(a.`fdate`, '%d-%b-%y') as fdate,date_format(a.`tdate`, '%d-%b-%y') as tdate,date_format(a.`advdate`, '%d-%b-%y') as advdate FROM advertisement a,products p,retailermaster r where p.productId=a.product and r.retailerId=a.retailerId and a.retailerId="
				+ retailerId + " order by a.fdate desc";
		return (ArrayList) getBeanList(AdvertisementModel.class, q);
	}

	public static ArrayList getProductWiseAds(String productId) {

		String q = "SELECT *,date_format(a.`fdate`, '%d-%b-%y') as fdate,date_format(a.`tdate`, '%d-%b-%y') as tdate,date_format(a.`advdate`, '%d-%b-%y') as advdate FROM advertisement a,products p,retailermaster r where p.productId=a.product and r.retailerId=a.retailerId and a.product="
				+ productId
				+ " and current_date()  between a.fdate and a.tdate order by a.fdate desc";
		return (ArrayList) getBeanList(AdvertisementModel.class, q);
	}

	public static HashMap getWishList(String userId) {
		String q = "SELECT adsId,1 FROM whishlist where userId=" + userId;
		return getQueryMap(q);
	}

	public static ArrayList getProductWiseAdsAll() {

		String q = "SELECT *,date_format(a.`fdate`, '%d-%b-%y') as fdate,date_format(a.`tdate`, '%d-%b-%y') as tdate,date_format(a.`advdate`, '%d-%b-%y') as advdate FROM advertisement a,products p,retailermaster r where p.productId=a.product and r.retailerId=a.retailerId  and current_date()  between a.fdate and a.tdate order by a.fdate desc";
		return (ArrayList) getBeanList(AdvertisementModel.class, q);
	}

	public static ArrayList getProductWiseAds(String productId, String lat,
			String lng) {
		double clat = StringHelper.n2d(lat);
		double clng = StringHelper.n2d(lng);
		String q = "SELECT *,date_format(a.`fdate`, '%d-%b-%y') as fdate,date_format(a.`tdate`, '%d-%b-%y') as tdate,date_format(a.`advdate`, '%d-%b-%y') as advdate FROM advertisement a,products p,retailermaster r where p.productId=a.product and r.retailerId=a.retailerId and a.product="
				+ productId
				+ " and current_date()  between a.fdate and a.tdate order by a.fdate desc";
		ArrayList l = (ArrayList) getBeanList(AdvertisementModel.class, q);
		for (int i = l.size() - 1; i >= 0; i--) {
			AdvertisementModel am = (AdvertisementModel) l.get(i);
			String shopLat = am.getShopLat();
			String shopLng = am.getShopLng();
			double slat = StringHelper.n2d(shopLat);
			double slng = StringHelper.n2d(shopLng);
			double d = distance(clat, clng, slat, slng);
			System.out
					.println("Distance " + d + " " + am.getAdTitle() + " ["
							+ shopLat + "," + shopLng + " | " + clat + ","
							+ clng + "]");
			if (d > ServerConstants.RADIUS) {
				l.remove(i);
			}
		}
		return l;
	}

	public static ArrayList getProductWiseAds(String lat, String lng) {
		double clat = StringHelper.n2d(lat);
		double clng = StringHelper.n2d(lng);
		String q = "SELECT *,date_format(a.`fdate`, '%d-%b-%y') as fdate,date_format(a.`tdate`, '%d-%b-%y') as tdate,date_format(a.`advdate`, '%d-%b-%y') as advdate FROM advertisement a,products p,retailermaster r where p.productId=a.product and r.retailerId=a.retailerId  and current_date()  between a.fdate and a.tdate order by a.fdate desc";
		ArrayList l = (ArrayList) getBeanList(AdvertisementModel.class, q);
		for (int i = l.size() - 1; i >= 0; i--) {
			AdvertisementModel am = (AdvertisementModel) l.get(i);
			String shopLat = am.getShopLat();
			String shopLng = am.getShopLng();
			double slat = StringHelper.n2d(shopLat);
			double slng = StringHelper.n2d(shopLng);
			double d = distance(clat, clng, slat, slng);
			if (d > ServerConstants.RADIUS) {
				l.remove(i);
			}
		}
		return l;
	}

	public static double distance(double lat1, double lon1, double lat2,
			double lon2) { // in KM
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344;
		return (dist);
	}

	public static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	public static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	public static ArrayList<WhishlistModel> getWishListItem(String userId) {
		String sql = "SELECT distinct adsId FROM `whishlist` where userId="+userId;
		
		ArrayList<WhishlistModel> finalList=new ArrayList<WhishlistModel>();
		List list = getMapList(sql);
		for (int i = 0; i < list.size(); i++) {
			HashMap map=(HashMap) list.get(i);
			String adsId =StringHelper.n2s( map.get("adsId"));
			ArrayList<WhishlistModel> arr=new ArrayList<WhishlistModel>();
			String query = "SELECT adsId,adTitle,adsDesc,date_format(a.`tdate`, '%d-%b-%y') as tdate ,date_format(a.`fdate`, '%d-%b-%y') as fdate FROM `advertisement` a where adsId='"+adsId+"'";
			arr=(ArrayList<WhishlistModel>) getBeanList(WhishlistModel.class,query);
			finalList.addAll(arr);
		}
		
		return finalList;
	}

	public static ArrayList<ProductModel> getProducts() {
		String q = "SELECT * FROM `products`";
		return (ArrayList<ProductModel>) getBeanList(ProductModel.class, q);
	}

	public static ArrayList<UserAccountModel> getUserAccounts() {
		String q = "SELECT * FROM useraccount";
		return (ArrayList<UserAccountModel>) getBeanList(
				UserAccountModel.class, q);
	}

	public static ArrayList<AdvertisementModel> getUserAccounts(
			String retailerId) {
		String q = "SELECT *,date_format(av.udate,'%d-%b-%y') as udate FROM lba_retail.advertisement a,adsvisit av,useraccount u where a.adsId=av.adsId and av.userId=u.userId and a.retailerId="
				+ retailerId;
		return (ArrayList<AdvertisementModel>) getBeanList(
				AdvertisementModel.class, q);
	}

	public static ArrayList<AdvertisementModel> getAllView() {
		String q = "SELECT *,date_format(av.udate,'%d-%b-%y') as udate FROM lba_retail.advertisement a,adsvisit av,useraccount u where a.adsId=av.adsId and av.userId=u.userId";
		return (ArrayList<AdvertisementModel>) getBeanList(
				AdvertisementModel.class, q);
	}

	public static ArrayList<RetailerMasterModel> getRetailerAccounts() {
		String q = "SELECT * FROM retailermaster r left join (SELECT retailerId,count(*) as noOfAds FROM advertisement group by retailerId) B on r.retailerId=B.retailerId";
		return (ArrayList<RetailerMasterModel>) getBeanList(
				RetailerMasterModel.class, q);
	}

	public static Object checkLogin(HashMap parameters) {
		int typeId = StringHelper.n2i(parameters.get("typeId"));
		String phoneNo = StringHelper.n2s(parameters.get("phoneNo"));
		String userpass = StringHelper.n2s(parameters.get("userpass"));
		// if(userpass.length()>0){
		// try {
		// userpass=SimpleCryptoAndroidJava.encryptString(userpass);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		if (typeId == 1 || typeId == 3) {

			String query = "";
			if (typeId == 1)
				query = "SELECT * FROM useraccount  where phone like ? and pass like ?";
			else if (typeId == 3)
				query = "SELECT * FROM useraccount  where phone like ? and pass like ? and roleId='A'";
			UserAccountModel um = null;
			List list = DBUtils.getBeanList(UserAccountModel.class, query,
					phoneNo, userpass);
			if (list.size() > 0) {
				um = (UserAccountModel) list.get(0);
			}
			return um;
		} else {

			String query = "SELECT * FROM retailermaster  where phone like ? and pass like ?";
			RetailerMasterModel um = null;
			List list = DBUtils.getBeanList(RetailerMasterModel.class, query,
					phoneNo, userpass);
			if (list.size() > 0) {
				um = (RetailerMasterModel) list.get(0);
			}
			return um;
		}
	}

	public static UserModel checkLoginPhone(HashMap parameters) {
		String imei = StringHelper.n2s(parameters.get("imei"));
		String query = "SELECT * FROM useraccount where imei like ? ";
		UserModel um = null;
		List list = DBUtils.getBeanList(UserModel.class, query, imei);
		if (list.size() > 0) {
			um = (UserModel) list.get(0);
		}
		return um;
	}

	public static void addContact(HashMap parameters) {
		System.out.println(parameters);
		String success = "";
		// userid, uname, pass, email, phone, residency, familycontact

		String phoneNo = StringHelper.n2s(parameters.get("phoneNO"));
		String userId = StringHelper.n2s(parameters.get("userId"));
		String familyContact = StringHelper
				.n2s(parameters.get("familyContact"));
		String phon = "";
		if (familyContact.length() > 0) {

			phon = familyContact + "," + phoneNo;
		} else {
			phon = phoneNo;
		}
		String q = "update useraccount set familycontact=? where userid=?";
		executeUpdate(q, phon, userId);
	}

	public static String insertUser(HashMap parameters) {
		System.out.println(parameters);
		String success = "";
		// userid, uname, pass, email, phone, residency, familycontact

		String uname = StringHelper.n2s(parameters.get("uname"));
		String pass = StringHelper.n2s(parameters.get("pass"));
		String email = StringHelper.n2s(parameters.get("email"));
		String phone = StringHelper.n2s(parameters.get("phone"));
		String residency = StringHelper.n2s(parameters.get("residency"));
		String familycontact = StringHelper
				.n2s(parameters.get("familycontact"));
		String imei = StringHelper.n2s(parameters.get("imei"));
		boolean succ = dataExists("Select * from useraccount where email like '"
				+ email
				+ "' OR phone like '"
				+ phone
				+ "' OR imei like '"
				+ imei + "'");
		if (succ) {
			success = "User's Phone no or email id or IMEI No already registered!";
			return success;
		}
		String userpass = "";
		try {
			userpass = SimpleCryptoAndroidJava.encryptString(pass);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String sql = "insert into useraccount (uname, pass, email, phone, residency, familycontact,imei) values(?,?,?,?,?,?,?)";

		int list = DBUtils.executeUpdate(sql, uname, userpass, email, phone,
				residency, familycontact, imei);
		if (list > 0) {
			success = "User registered Successfully";

		} else {
			success = "Error adding user to database";
		}

		return success;
	}

	public static String fnPostAds(HashMap parameters) {
		System.out.println(parameters);
		String success = "";
		// adTitle,adsDesc, retailerId, product, fdate, tdate

		String adTitle = StringHelper.n2s(parameters.get("adTitle"));
		String adsDesc = StringHelper.n2s(parameters.get("adsDesc"));
		String product = StringHelper.n2s(parameters.get("product"));
		String fdate = parseDate(StringHelper.n2s(parameters.get("fdate")));
		String tdate = parseDate(StringHelper.n2s(parameters.get("tdate")));
		String retailerId = StringHelper.n2s(parameters.get("retailerId"));

		String sql = "insert into advertisement(adTitle,adsDesc, retailerId,  product, fdate, tdate) values(?,?,?,?,?,?);";

		int list = DBUtils.executeUpdate(sql, adTitle, adsDesc, retailerId,
				product, fdate, tdate);
		if (list > 0) {
			success = "Ad Added Successfully";

		} else {
			success = "Error Adding Ads to database";
		}
		return success;
	}

	public static void main(String[] args) {

		getDBConnection();
	}

	public static String parseDate(String d) {
		String tokens[] = d.split("-");
		int day = StringHelper.n2i(tokens[0]);
		int month = StringHelper.n2i(tokens[1]);
		Date date = new Date();
		date.setDate(day);
		date.setMonth(month - 1);
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);

		// String pattern = "2015-03-12 22:35:51";
		String pattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		String s = format.format(date);
		return s;

	}

	public static String saveCustomer(HashMap parameters) {
		System.out.println(parameters);
		String success = "";
		// userid, uname, pass, email, phone, residency, familycontact

		String username = StringHelper.n2s(parameters.get("username"));
		String emailid = StringHelper.n2s(parameters.get("emailid"));
		String phoneno = StringHelper.n2s(parameters.get("phoneno"));
		String userpass = StringHelper.n2s(parameters.get("userpass"));
		String imei = StringHelper.n2s(parameters.get("imei"));
		String q = "select 1 from  useraccount  where phone like '" + phoneno
				+ "'";
		int i = getMaxValue(q);
		if (i != -1) {
			return "Phone no already registered!";
		}

		String sql = "insert into useraccount (username, pass, emailid, phone, imei) values(?,?,?,?,?)";

		int list = DBUtils.executeUpdate(sql, username, userpass, emailid,
				phoneno, imei);
		if (list > 0) {
			success = "Customer registered Successfully";

		} else {
			success = "Error adding user to database";
		}

		return success;
	}

	public static String saveRetailer(HashMap parameters) {
		System.out.println(parameters);
		String success = "";
		// userid, uname, pass, email, phone, residency, familycontact

		String ownername = StringHelper.n2s(parameters.get("ownername"));
		String shopName = StringHelper.n2s(parameters.get("shopName"));
		String startlocation = StringHelper
				.n2s(parameters.get("startlocation"));
		String licenseNo = StringHelper.n2s(parameters.get("licenseNo"));
		String imei = StringHelper.n2s(parameters.get("imei"));
		String phone = StringHelper.n2s(parameters.get("phone"));
		String emailid = StringHelper.n2s(parameters.get("emailid"));
		String userpass = StringHelper.n2s(parameters.get("userpass"));
		String cuserpass = StringHelper.n2s(parameters.get("cuserpass"));
		String q = "select 1 from  retailermaster  where phone like '" + phone
				+ "'";
		int i = getMaxValue(q);
		if (i != -1) {
			return "Phone no already registered!";
		}
		double lat = 0, lng = 0;
		try {
			Double[] tokens = ReverseGeocoder.getLatlngOfAddress(startlocation);
			lat = tokens[0];
			lng = tokens[1];
		} catch (Exception e) {

		}
		;
		String sql = "insert into retailermaster (ownername, shopname, shopaddress, phone, emailId, pass, products, licenseNo, shopLat, shopLng,   imei) values(?,?,?,?,?,?,?,?,?,?,?)";

		int list = DBUtils.executeUpdate(sql, ownername, shopName,
				startlocation, phone, emailid, userpass, "", licenseNo, lat,
				lng, imei);
		if (list > 0) {
			success = "Retailer registered Successfully";

		} else {
			success = "Error adding user to database";
		}

		return success;
	}

	public static String getCombo(String sql) {
		List list = getMapList(sql);
		StringBuffer sb = new StringBuffer();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			HashMap record = (HashMap) iterator.next();
			String key = StringHelper.n2s(record.get("key"));
			String value = StringHelper.n2s(record.get("value"));
			sb.append("<option value='" + key + "'>" + value + "</option>");
		}
		return sb.toString();
	}

	public static List authenticateUser(String login, String pass) {
		String query = "SELECT * FROM useraccount where loginid = ? and pass =?";
		System.out.println("query " + query);
		try {
			pass = SimpleCryptoAndroidJava.encryptString(pass);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List list = getMapList(query, new Object[] { login, pass });
		return list;
	}

	public static List userList() {
		String query = "SELECT * FROM useraccount ";
		System.out.println("query " + query);
		List list = getBeanList(UserModel.class, query);
		return list;
	}

	public static void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static UserModel getUserDetails(HashMap parameters) {

		String userId = StringHelper.n2s(parameters.get("userId"));
		String pass = StringHelper.n2s(parameters.get("pass"));
		try {
			pass = SimpleCryptoAndroidJava.encryptString(pass);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sql = "SELECT * FROM useraccount e where  e.loginid='" + userId
				+ "' and e.pass='" + pass + "'";
		List list = DBUtils.getBeanList(UserModel.class, sql);
		UserModel em = null;
		String msg = "";
		if (list.size() > 0) {
			em = (UserModel) list.get(0);
		}
		return em;

	}

	public static String verifyUsername(HashMap parameters) {
		String success = "-1";
		String username = StringHelper.n2s(parameters.get("username"));
		String duplicateCheck = "Select 1 from useraccounts where loginid like ? ";
		List list = DBUtils.getParameterizedList(duplicateCheck, username);
		if (list.size() > 0) {
			success = "1";
			return success;
		}
		return success;
	}

	public static boolean saveImage(byte imagedata[], String filename,
			String adId) {
		String q = "Delete  from adimages where adsId=" + adId;
		executeUpdate(q);
		Connection connection = getDBConnection();
		PreparedStatement psmnt;
		boolean success = false;
		try {
			ByteArrayInputStream inStream = new ByteArrayInputStream(imagedata);
			// imageId, adsId, img, iname
			psmnt = connection
					.prepareStatement("insert into adimages ( adsId, img, iname) values(?,?,?)");
			psmnt.setString(1, adId);
			psmnt.setBinaryStream(2, inStream, imagedata.length);
			psmnt.setString(3, filename);
			int i = psmnt.executeUpdate();
			success = true;
			System.out.println("Image Uploaded To Database ...");
			psmnt.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			success = false;
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return success;
	}

	public static void getImage(String sql, OutputStream out) {
		// String sql = "SELECT pic FROM pictures where imageId=" + imageId;
		Connection c = null;
		try {
			c = getDBConnection();
			Statement stmt;
			stmt = c.createStatement();
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				Blob b = rs.getBlob(1);

				InputStream in = b.getBinaryStream();
				int length = 0;
				int bufferSize = 4096;
				byte[] buffer = new byte[bufferSize];
				while ((length = in.read(buffer)) != -1) {
					out.write(buffer, 0, length);
				}
				in.close();
				out.flush();
			}
			out.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (c != null)
					c.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
