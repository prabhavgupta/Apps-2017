fi<!doctype html>
<%@page import="com.entity.RetailerMasterModel"%>
<%@page import="com.entity.UserAccountModel"%>
<%@page import="com.entity.ProductModel"%>
<%@page import="com.database.ConnectionManager"%>
<%@page import="java.util.ArrayList"%>
<html>
<head lang="en">
<%@include file="../tiles/inc.jsp"%>
</head>
<body>
<%
	String userId="2";
int type=1;
String home="home_retailer.jsp";
if(session.getAttribute("USER_MODEL")!=null){
	Object o=session.getAttribute("USER_MODEL");
	if(o instanceof UserAccountModel){
		UserAccountModel um=(UserAccountModel)session.getAttribute("USER_MODEL");
		userId=um.getUserid();
		type=1;
		home="home_customer.jsp";
	
	}else if(o instanceof RetailerMasterModel){
		RetailerMasterModel um=(RetailerMasterModel)session.getAttribute("USER_MODEL");
		userId=um.getRetailerId();
		type=2;
		home="home_retailer.jsp";
	}
}
%>
<%-- <%@include file="../tiles/menu.jsp"%> --%>
	
	<div class="slide story" id="slide-4" data-slide="1"  style="background-color: #C3DC65;color: #000000;padding: 0px">
		<div class="container">
			<div id="home-row-1" class="row clearfix">
				<div class="col-12" >
					<h1 class="font-semibold" >
	Add Your Product Catagories
					</h1>  
						<%
					ArrayList arr=ConnectionManager.getProducts();
				
					%>
					<div class="row content-row" style="margin: auto;">
					<form action="">
					<table class="simple">
					<tr>
						<td>Sr.No</td>
					<td>Product Name</td>
					<td>Select</td>
					</tr>
				
					<tr>
					<td></td>
					<td></td>
					<td></td>
					</tr>   
				
					<tr>
					<td colspan="3"><input  type="button" value="Update" onclick="fnUpdate();"/> </td>
					</tr>
					</table>
					</form>
					</div>
					<!-- /col-12 -->
				</div>
				<!-- /row -->
			</div>
			<!-- /container -->
		</div>
		<!-- /slide1 -->
	</div>
<script>
function fnUpdate(){
	str='';
	 $(':checkbox:checked').each(function(i){
		 str+=','+$(this).val();
       });
	 //alert(str);
	 if(str==''){
		 alert('Please select a product catagory!');
		 return;
	 }else{
		 str=str.substring(1);
	 }
		$.post("<%=request.getContextPath()%>/tiles/ajax.jsp?methodId=updateProducts",
				'products='+str+"&userId=<%=userId%>&typeId=<%=type%>",
				function(data) {
			alert('Selected Products have been updated.');
	});

	 
}
</script>
</body>


</html>