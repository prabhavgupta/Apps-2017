<!doctype html>
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
%>
<%-- 	<%@include file="../tiles/menu.jsp" %> --%>
	
	<!-- === Slide 2 === -->
	<div class="slide story" id="slide-customer" data-slide="2" style="	background-color: #1ABC9C;	color: #000000;">
		<form id="ads-form" action="javascript:fnPostAds();">
		<h1 class="font-semibold" style="margin-top: 0px;"><a href="<%=request.getContextPath()%>/pages/home_retailer.jsp"><img src="<%=request.getContextPath()%>/theme/images/home.png"/></a> Post Ads</h1>
					
		<div class="container">
	<div class="row" style="margin:auto;width: 80%">
								<div class="col-md-6">
<!-- adsId, adsDesc, retailerId, adTitle, product, fdate, tdate, advDate -->
									<div class="form-group">
										<label for="name">Advertisement Title</label> <input type="text" style="text-transform: capitalize;"
											class="form-control" id="adTitle" name="adTitle" tabindex="1"
											placeholder="Enter First name" required="required" />
									</div>
									
									<div class="form-group">
										<label for="name">Advertisement Desc</label> <textarea style="text-transform: capitalize;"
											class="form-control" id="adsDesc" name="adsDesc" tabindex="1"
											placeholder="Enter Advertisement Desc" required="required" ></textarea>
									</div>
									<div class="form-group">
										<label for="name">Product Name</label> 
										<select name="product" id="product"	class="form-control" >
										<option value="0" selected="selected">Select</option>
										<%=ConnectionManager
					.getCombo("SELECT productId as `key`, productName as `value` FROM products")%>
										</select>
									</div>
									
									
									
									<input type="hidden" name="retailerId"  id="retailerId" value="<%=retailerId%>"/>
									<%
										Date d = new Date();
									%>
									<div class="form-group">
										<label for="name">From Date(Day-Month)</label> <input type="text" style="text-transform: capitalize;"
											class="form-control" id="fdate" name="fdate" tabindex="1" title="Enter From Date DD-MM" 
											placeholder="Enter From Date" value="<%=d.getDate()%>-<%=d.getMonth() + 1%>" required="required" pattern="[0-9]{1,2}-[0-9]{1,2}" />
									</div>
									<div class="form-group">
										<label for="name">To Date(Day-Month)</label> <input type="text" style="text-transform: capitalize;"
											class="form-control" id="tdate" name="tdate" tabindex="1" title="Enter From Date DD-MM" 
											placeholder="Enter To Date" value="<%=d.getDate()%>-<%=d.getMonth() + 1%>" required="required" pattern="[0-9]{1,2}-[0-9]{1,2}" />
									</div>

								</div>
							
								<div class="col-md-12" style="margin: auto;">
									&nbsp;&nbsp;
									<button type="reset" id="ResetBtnId" style="background-color: black;"
										class="btn btn-danger pull-right">Reset</button>
									&nbsp;&nbsp;
									<button type="submit" id="RegisterMeBtn" style="margin-right: 4px; background-color: black;" class="btn btn-danger pull-right" tabindex="13">Post Ad!</button>
								</div>
											<!-- === Arrows === -->  
							<BR>   
						
							</div>   
		</div><!-- /container -->
		</form>
	</div><!-- /slide2 -->
	   
	
	
		<!-- === Slide 2 === -->
	<div class="slide story" id="slide-ads" data-slide="2" style="	background-color: #1ABC9C;	color: #000000;">
		<form id="ads-form" action="javascript:fnPostAds();">
		<h1 class="font-semibold" style="margin-top: 0px;"> View Ads</h1>
					
		<div class="container">
	<div class="row" style="margin:auto;width: 80%">
								<div class="col-md-6">
<!-- adsId, adsDesc, retailerId, adTitle, product, fdate, tdate, advDate -->
									<div class="col-md-12" style="margin: auto;">
						<%
							ArrayList ads = ConnectionManager.getAdvertisements(retailerId);
							session.setAttribute("ADS", ads);
						%>
						
							<display:table name="sessionScope.ADS" id="disasterModel" class="simple"  defaultsort="1" defaultorder="ascending">
	<%
							AdvertisementModel adsModel=(AdvertisementModel)pageContext.getAttribute("disasterModel");
	%>

								<display:setProperty name="export.excel.filename"
									value="report.xls" />

								<display:column title="Sr.No"><%=pageContext.getAttribute("disasterModel_rowNum")%></display:column>
								
								<display:column title="Ads Title" property="adTitle"></display:column>
								<display:column title="Product" property="productName"></display:column>
								<display:column title="From Date" property="fdate"></display:column>
								<display:column title="To Date" property="tdate"></display:column>
								<display:column title="adsDesc" property="adsDesc" />
								<display:column title="photo" ><A href="#" onclick="javascript:fnUploadPhoto('<%=adsModel.getAdsId()%>','<%=adsModel.getAdTitle()%>');">Upload Photo</A></display:column>
								<display:column title="delete" ><A href="#" onclick="javascript:fnDeleteAds('<%=adsModel.getAdsId()%>');">Delete</A></display:column>
								
							</display:table>
						</div>
							
								<div class="col-md-12" style="margin: auto;">
									&nbsp;&nbsp;
									
										<a id="ResetBtnId" style="background-color: black;"
										class="btn btn-danger pull-right" href="<%=request.getContextPath()%>/pages/postAds.jsp">Refresh</a>
									&nbsp;&nbsp;
									  
								</div>
											<!-- === Arrows === -->  
							<BR>   
						
							</div>   
		</div><!-- /container -->
		</form>
	</div><!-- /slide2 -->
	

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