<!doctype html>
<%@page import="java.util.HashMap"%>
<%@page import="com.entity.UserAccountModel"%>
<%@page import="com.entity.RetailerMasterModel"%>
<%@page import="com.constant.ServerConstants"%>
<%@page import="com.helper.StringHelper"%>
<%@page import="com.entity.AdvertisementModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@page import="com.database.ConnectionManager"%>
<%@taglib prefix="display" uri="/WEB-INF/displaytag-12.tld"%>
<html>
<head lang="en">
<!-- <meta http-equiv="refresh" content="5"> -->
<%@include file="../tiles/inc.jsp"%>
<style>
#popup_box,#popup_box2 {
	display: none;
	position: fixed;
	_position: absolute;
	height: 200px;
	width: 200px;
	background-color: #1ABC9C;
	color: #000000; "
	left:;
	top: 150px;
	z-index: 100;
	margin-left: 5px;
	border: 2px solid #0000FF;
	padding: 5px;
	font-size: 12px;
	-moz-box-shadow: 0 0 5px #0000FF;
	-webkit-box-shadow: 0 0 5px #0000FF;
	box-shadow: 0 0 5px #0000FF;
}

#popupBoxClose,#popupBoxClose2 {
	font-size: 12px;
	line-height: 15px;
	right: 15px;
	top: 15px;
	position: absolute;
	color: #6fa5e2;
	font-weight: 500;
}
</style>
</head>

<body style="background-color: black;">
	<%
		String type = StringHelper.n2s(request.getParameter("productType"));
		String productId = StringHelper.n2s(request.getParameter("productId"));
		String latlng = StringHelper.n2s(request.getParameter("latlng"));
		System.out.println("LatLong: "+latlng);
		String KMs = StringHelper.n2s(request.getParameter("KMs"));
		String address = "";
		
		if (latlng.length() == 0) {
			latlng = StringHelper.n2s(session.getAttribute("latlong"));
			address = StringHelper.n2s(session.getAttribute("address"));
		}
		
		if (KMs.length() > 0) {
			ServerConstants.RADIUS = StringHelper.n2i(KMs);
		} else {
			KMs = "0";
		}
		
		String refreshURL = request.getContextPath()
				+ "/pages/viewOffersCustomer.jsp?latlng=" + latlng
				+ "&productType=" + type + "&productId=" + productId;

		String refreshURLWithoutProductId = request.getContextPath()
				+ "/pages/viewOffersCustomer.jsp?latlng=" + latlng
				+ "&productType=" + type;
		
		String retailerId = "2";
		String home = "home_retailer.jsp";
		
		if (session.getAttribute("USER_MODEL") != null) {
			
			Object o = session.getAttribute("USER_MODEL");
			if (o instanceof RetailerMasterModel) {
				
				RetailerMasterModel um = (RetailerMasterModel) session.getAttribute("USER_MODEL");
				retailerId = um.getRetailerId();
				home = "home_retailer.jsp";
			} else if (o instanceof UserAccountModel) {
				
				UserAccountModel um = (UserAccountModel) session.getAttribute("USER_MODEL");
				retailerId = um.getUserid();
				if (um.getRoleId().equalsIgnoreCase("A")) {
					home = "home_admin.jsp";
				} else {
					home = "home_customer.jsp";
				}
			}
		}
		HashMap wishList = ConnectionManager.getWishList(retailerId);
	%>


	<!-- === Slide 2 === -->
	<div class="slide story" id="slide-ads" data-slide="2"
		style="background-color: #1ABC9C; color: #000000;">
		<form id="ads-form" action="javascript:fnPostAds();">
			<h1 class="font-semibold" style="margin-top: 0px;">
				<a href="<%=request.getContextPath()%>/pages/<%=home%>"><img
					src="<%=request.getContextPath()%>/theme/images/home.png" /></a>View Ads
			</h1>
			<div class="container">
				<div class="row"
					style="margin: auto; width: 80%; border-style: solid; border-width: thin; border-bottom-style: none; padding: 5px;">
					<div class="col-12 col-sm-3">
						<h4 class="font-semibold">Search Here:</h4>
					</div>
					<div class="col-12 col-sm-3">
						<select name="productId" id="productId" class="form-control">
							<option value="0" selected="selected">Search Product</option>
							<%=ConnectionManager.getCombo("SELECT productId as `key`, productName as `value` FROM products")%>
						</select>
					</div>
					<div class="col-12 col-sm-3">
						<select name="KMs" id="KMs" class="form-control">
							<option value="0" selected="selected">Search Radius</option>
							<option value="5">5 KM</option>
							<option value="10">10 KM</option>
							<option value="20">20 KM</option>
							<option value="30">30 KM</option>
							<option value="60">60 KM</option>
						</select>
					</div>
					<div class="col-12 col-sm-3">
						<a id="ResetBtnId" style="background-color: black;"
							class="btn btn-danger pull-right" href="#" onclick="fnSearch();">Search</a>
					</div>
				</div>

				<!-- /container -->
				<div class="row" style="margin: auto; width: 80%">
					<div class="col-md-6">
						<!-- adsId, adsDesc, retailerId, adTitle, product, fdate, tdate, advDate -->
						<div class="col-md-12" style="margin: auto;">
							<%
								ArrayList ads = null;
								if (type.equalsIgnoreCase("products")) {
									
									ads = ConnectionManager.getProductWiseAds(productId);
								} else if (type.equalsIgnoreCase("myLocation")) {
									ads = ConnectionManager.getProductWiseAds(latlng.split(",")[0],
											latlng.split(",")[1]);
								} else if (type.equalsIgnoreCase("myLocationProducts")) {
									ads = ConnectionManager.getProductWiseAds(productId,
											latlng.split(",")[0], latlng.split(",")[1]);
								} else if (type.equalsIgnoreCase("getRetailerAds")) {
									ads = ConnectionManager.getAdvertisements(retailerId);
								}

								else {
									ads = ConnectionManager.getProductWiseAdsAll();
								}

								session.setAttribute("ADS", ads);
								for (int i = 0; ads != null && i < ads.size(); i++) {
									AdvertisementModel am = (AdvertisementModel) ads.get(i);
									String wishL = StringHelper
											.n2s(wishList.get(am.getAdsId() + ""));
									String style = "";
									String name = "star.png";
									if (wishL.length() > 0) {
										style = "color:blue;";
										name = "star2.png";
									}
							%>
							<div class="row subtitle-row"
								style="border-style: solid; border-width: thin; border-bottom-style: none; padding: 5px;">

								<div class="col-2 col-sm-2" style="">
									<div class="home-hover navigation-slide" data-slide="4">
										<img
											src="<%=request.getContextPath()%>/tiles/ajax.jsp?methodId=downloadImage&imageId=<%=am.getAdsId()%>"
											alt="No Image" style="width: 50px; height: 50px;" />
									</div>
								</div>
								<div class="col-10 col-sm-10 font-light"
									style="font-size: 18px;<%=style%>">
									<B><%=am.getAdTitle()%></B><BR> <span
										style="font-size: 14px;"><%=am.getAdsDesc()%>-[<%=am.getTdate()%>]</span><BR>
									<span style="font-size: 14px;"> <a href="#"
										onclick="fnShowDirections('<%=am.getShopLat()%>','<%=am.getShopLng()%>','<%=am.getShopaddress()%>','<%=am.getShopname()%>')"><img
											src="<%=request.getContextPath()%>/theme/images/directions.png" /></a>
										<a href="#"
										onclick="fnShow('<%=am.getAdsId()%>','<%=am.getShopname()%>','<%=am.getShopaddress()%>','<%=am.getOwnername()%>','<%=am.getPhone()%>');"><img
											src="<%=request.getContextPath()%>/theme/images/info.png" /></a>
										<a href="#" title="Add to Wishlist"
										onclick="fnAddWishlist('<%=am.getAdsId()%>','<%=retailerId%>');"><img
											src="<%=request.getContextPath()%>/theme/images/<%=name%>" /></a>
									</span>
								</div>
							</div>

							<!-- /row -->
							<%
								}
							%>

						</div>

						<div class="col-md-12" style="margin: auto;">
							<a type="reset" id="ResetBtnId" style="background-color: black;"
								class="btn btn-danger pull-right"
								href="<%=request.getContextPath()%>/pages/<%=home%>">Main
								Menu</a> &nbsp;&nbsp; <a id="ResetBtnId"
								style="background-color: black;"
								class="btn btn-danger pull-right" href="<%=refreshURL%>">Refresh</a>
							&nbsp;&nbsp;

						</div>
						<!-- === Arrows === -->
						<BR>

					</div>
				</div>
				<!-- /container -->


				<div id="popup_box2" name="popup_box2"
					style="display: none; z-index: 9999;">
					<span id="popup_text2"> </span> <a id="popupBoxClose2"><img
						src="<%=request.getContextPath()%>/theme/images/CloseButton.png"
						onclick="$('#popup_box2').fadeOut('slow');" /> </a>
				</div>


			</div>
		</form>
	</div>
	<!-- /slide2 -->

	<script>
function fnShowDirections(lat,lng,addr,name){
	window.ActivityObject.showDirections(lat,lng,addr,name);
}
function fnShow(adId,shopName,shopAddress,owner,phone){
	$('#popup_box2').show();
	$('#popup_text2').html('<h3>'+shopName+'</h3><BR><span>'+owner+'</span><BR><span>'+shopAddress+'</span><BR><span>'+phone+'</span>');
	fnAddAdView(adId);
}
function fnAddWishlist(adsId,userId){
	$.post("<%=request.getContextPath()%>/tiles/ajax.jsp?methodId=insertWishlist",
			'adsId='+adsId+"&userId="+userId,
			function(data) {
		data=$.trim(data);
		alert('Added to whishlist');

			});
}
function fnSearch(){
	
	
	window.location.href='<%=refreshURLWithoutProductId%>&productId='+$('#productId').val()+'&KMs='+$('#KMs').val();
}
function fnPostAds() {
		 var str = $( "#ads-form" ).serialize();
		$.post("<%=request.getContextPath()%>/tiles/ajax.jsp?methodId=fnPostAds",
				str,
				function(data) {
			data=$.trim(data);
			alert(data);
			$( "#ads-form" )[0].reset();
				});
}
function fnAddAdView(adId) {
	 var str = $( "#ads-form" ).serialize();
	$.post("<%=request.getContextPath()%>/tiles/ajax.jsp?methodId=addView",
			'reatailerId=<%=retailerId%>&adId='+adId,
			function(data) {
		data=$.trim(data);
		//alert('');
		});
}
function fnDeleteAds(adsId) {
	 if(confirm('Are you sure?')){
	$.post("<%=request.getContextPath()%>/tiles/ajax.jsp?methodId=fnDeleteAds",
			'adsId='+adsId,
			function(data) {
		alert('Ad Has Been deleted');
		
			});
	 }
}
$('#productId').val('<%=productId%>');
$('#KMs').val('<%=KMs%>');
		function fnGetLocation() {
			try {
				str = window.ActivityObject.getLocation();
				if (str.indexOf('#') != -1) {
					latlong = str.substring(0, str.indexOf('#'));
					address = str.substring(str.indexOf('#') + 1);
					return [ latlong, address ];
					//$('#startlocation').val(str);
				}
			} catch (err) {

			}
			return "";

		}
	</script>
</body>
</html>