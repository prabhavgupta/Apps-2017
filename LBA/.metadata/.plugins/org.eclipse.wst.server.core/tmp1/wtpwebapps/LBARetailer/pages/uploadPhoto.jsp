<!doctype html>
<%@page import="java.net.URLDecoder"%>
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
	String adTitle = StringHelper.n2s(request.getParameter("adTitle"));
try{
	adTitle=URLDecoder.decode(adTitle);
}catch(Exception e){
	
}
	String adId = StringHelper.n2s(request.getParameter("adId"));
%>
<%-- 	<%@include file="../tiles/menu.jsp" %> --%>
	
	
	   
	
	
		<!-- === Slide 2 === -->
	<div class="slide story" id="slide-ads" data-slide="2" style="	background-color: #1ABC9C;	color: #000000;">
	
		<h1 class="font-semibold" style="margin-top: 0px;"> <a href="<%=request.getContextPath()%>/pages/postAds.jsp"><img src="<%=request.getContextPath()%>/theme/images/home.png"/></a>View Ads</h1>
				<!-- 			action="javascript:fnUpdate();" -->	
		<div class="container">
			<form id="login-form" action="<%=request.getContextPath()%>/tiles/ajax.jsp?methodId=uploadPhoto" 
			 target="targetFrame" method="post" enctype="multipart/form-data">
						
	<div class="row" style="margin:auto;width: 80%">
								<div class="col-md-6">
<!-- adsId, adsDesc, retailerId, adTitle, product, fdate, tdate, advDate -->
							
									<div class="col-md-12" style="margin: auto;">
						
								<div class="row" style="margin-left: 2px; margin-right: 2px;">

									<div class="col-md-6">

										<div class="form-group">
											<label for="name">Advertisment Title</label> 
											<h2><%=adTitle%></h2>  
										</div>  
										<input type="hidden" name="adId" id="adId" value="<%=adId%>" />

										<div class="form-group">
											<label for="name">Image: <input type="file"
												class="form-control" id="uploaded_file" tabindex="9"
												onclick="javascript:window.ActivityObject.openFile();" 
												placeholder="Select Image" name="uploaded_file"
												 /></label>
										</div>

									</div>    

									<div class="col-md-12" style="text-align: center;">
										&nbsp;&nbsp; <input type="submit" id="ResetBtnId"
											class="btn btn-success pull-center" value="Upload File" />
									</div>



								</div>

							</div>
							
							
											<!-- === Arrows === -->  
							<BR>   
						
							</div>   
		</div><!-- /container -->
			</form>
		</form>
		<iframe id="targetFrame" style="border: none;height: 40px;"  name="targetFrame"></iframe>
	</div><!-- /slide2 -->
	

</body>
<script type="text/javascript">

function fnUpdate(){
	alert(str);  
	ftype= $('#ftype').val();
	 
<%-- 	//<%=request.getContextPath()%>/tiles/ajax.jsp?methodId=uploadPhoto --%>
	url='<%=request.getContextPath()%>/tiles/ajax.jsp?methodId=uploadPhoto',
	str=$('#login-form').serialize();
	//alert(str);  
str=window.ActivityObject.uploadFile(url,str,"jpg");
if(str.length>0){
	alert(str);  
}
}


</script>
</html>