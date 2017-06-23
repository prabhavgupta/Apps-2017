<!doctype html>
<%@page import="com.helper.StringHelper"%>
<%@page import="com.entity.RetailerMasterModel"%>
<%@page import="com.entity.UserAccountModel"%>
<%@page import="com.entity.AdvertisementModel"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@page import="com.database.ConnectionManager"%>
<%@taglib prefix="display" uri="/WEB-INF/displaytag-12.tld"%>
<html>
<head lang="en">
<!-- <meta http-equiv="refresh" content="5"> -->
	<%@include file="../tiles/inc.jsp" %>	

</head>
     
<body style="background-color:black;;">
<%
	String retailerId = "2";
String home="home_retailer.jsp";
if(session.getAttribute("USER_MODEL")!=null){
	Object o=session.getAttribute("USER_MODEL");
	 if(o instanceof RetailerMasterModel){
		RetailerMasterModel um=(RetailerMasterModel)session.getAttribute("USER_MODEL");
		retailerId=um.getRetailerId();
		home="home_retailer.jsp";
	}
}
int report= StringHelper.n2i(request.getParameter("type"));
if(session.getAttribute("USER_MODEL")!=null){
	Object o=session.getAttribute("USER_MODEL");
	 if(o instanceof RetailerMasterModel){
		RetailerMasterModel um=(RetailerMasterModel)session.getAttribute("USER_MODEL");
		retailerId=um.getRetailerId();
		home="home_retailer.jsp";
	}else if(o instanceof UserAccountModel){
		UserAccountModel um=(UserAccountModel)session.getAttribute("USER_MODEL");
		retailerId=um.getUserid();
		if(um.getRoleId().equalsIgnoreCase("A")){
	home="home_admin.jsp";
		}else{
	home="home_customer.jsp";
		}
		
	}
}
%>
<%-- 	<%@include file="../tiles/menu.jsp" %> --%>
	

	   
	
	
		<!-- === Slide 2 === -->
	<div class="slide story" id="slide-ads" data-slide="2" style="	background-color: #1ABC9C;	color: #000000;">
		
		<h1 class="font-semibold" style="margin-top: 0px;"><a href="<%=request.getContextPath()%>/pages/<%=home%>"><img src="<%=request.getContextPath()%>/theme/images/home.png"/></a> View Customers</h1>
					
		<div class="container" style="margin:auto;">
		<form id="ads-form" action="javascript:fnPostAds();">
	<div class="row" >
								<div class="col-md-6">
<!-- adsId, adsDesc, retailerId, adTitle, product, fdate, tdate, advDate -->
									<div class="col-md-12">
						<%
							if(report==1){
											ArrayList ads = ConnectionManager.getUserAccounts();
											session.setAttribute("ADS", ads);
						%>
							<h2><%=ads.size()%> Customers Found!</h2>
							<display:table name="sessionScope.ADS" id="disasterModel" class="simple"  defaultsort="1" defaultorder="ascending">
								<%
									UserAccountModel adsModel=(UserAccountModel)pageContext.getAttribute("disasterModel");
								%>
								<display:setProperty name="export.excel.filename" value="report.xls" />
								<display:column title="Sr.No"><%=pageContext.getAttribute("disasterModel_rowNum")%></display:column>
								<display:column title="UserName" property="username"></display:column>
								<display:column title="Email Id" property="emailid"></display:column>
								<display:column title="Phone No" property="phone"></display:column>
							</display:table>
							<%
								} else if(report==2){	// Usage Report
												ArrayList ads = ConnectionManager.getUserAccounts(retailerId);
												session.setAttribute("ADS", ads);
							%>
													<h2><%=ads.size()%> Visits For Your Ads!</h2>
							<display:table name="sessionScope.ADS" id="disasterModel" class="simple"  defaultsort="1" defaultorder="ascending">
								<%
									AdvertisementModel adsModel=(AdvertisementModel)pageContext.getAttribute("disasterModel");
								%>
								<display:setProperty name="export.excel.filename" value="report.xls" />
								<display:column title="Sr.No"><%=pageContext.getAttribute("disasterModel_rowNum")%></display:column>
								<display:column title="UserName" property="username"></display:column>
								<display:column title="Email Id" property="emailid"></display:column>
								<display:column title="Phone No" property="phone"></display:column>
								<display:column title="Ad Title" property="adTitle"></display:column>
									<display:column title="Visited On" property="udate"></display:column>
							</display:table>
							<%
								} else if(report==3){	// Retailer Report
												ArrayList ads = ConnectionManager.getRetailerAccounts();
												session.setAttribute("ADS", ads);
							%>
													<h2><%=ads.size()%> Retailers Registered on Site!</h2>
							<display:table name="sessionScope.ADS" id="disasterModel" class="simple"  defaultsort="1" defaultorder="ascending">
								<%
									RetailerMasterModel adsModel=(RetailerMasterModel)pageContext.getAttribute("disasterModel");
								%>
								<display:setProperty name="export.excel.filename" value="report.xls" />
								<display:column title="Sr.No"><%=pageContext.getAttribute("disasterModel_rowNum")%></display:column>
								<display:column title="No Of Ads" property="noOfAds"></display:column>
								<display:column title="Owner Name" property="ownername"></display:column>
								<display:column title="Shop Name" property="shopname"></display:column>
								<display:column title="Shop Address" property="shopaddress"></display:column>
								<display:column title="Phone" property="phone"></display:column>
								<display:column title="Email Id" property="emailId"></display:column>
								
									
							</display:table>
							<%} else if(report==4){	// All Views Report
							ArrayList ads = ConnectionManager.getAllView();
							session.setAttribute("ADS", ads);
						%>
													<h2><%=ads.size()%> Ad Views On Site!</h2>
							<display:table name="sessionScope.ADS" id="disasterModel" class="simple"  defaultsort="1" defaultorder="ascending">
								<% AdvertisementModel adsModel=(AdvertisementModel)pageContext.getAttribute("disasterModel");%>
								<display:setProperty name="export.excel.filename" value="report.xls" />
								<display:column title="Sr.No"><%=pageContext.getAttribute("disasterModel_rowNum")%></display:column>
								<display:column title="UserName" property="username"></display:column>
								<display:column title="Email Id" property="emailid"></display:column> 
								<display:column title="Phone No" property="phone"></display:column>
								<display:column title="Ad Title" property="adTitle"></display:column>
									<display:column title="Visited On" property="udate"></display:column>
							</display:table>
							<%}%>
							
							<div class="col-md-12" style="margin: auto;">
							<a type="reset" id="ResetBtnId" style="background-color: black;"
								class="btn btn-danger pull-right"
								href="<%=request.getContextPath()%>/pages/<%=home%>">Main
								Menu</a> 

							</div>
							
							
						</div>
							
							
							<BR>   
						
							</div>   
		</div><!-- /container -->
		</form>
	</div><!-- /slide2 -->
	</div>

<script >
function fnUploadPhoto(adId,adTitle){
	window.location.href='<%=request.getContextPath()%>/pages/uploadPhoto.jsp?adId='+adId+'&adTitle='+adTitle;
	
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
function fnDeleteAds(adsId) {
	 if(confirm('Are you sure?')){
	$.post("<%=request.getContextPath()%>/tiles/ajax.jsp?methodId=fnDeleteAds",
			'adsId='+adsId,
			function(data) {
		alert('Ad Has Been deleted');
		
			});
	 }
}
</script>
</body>
</html>