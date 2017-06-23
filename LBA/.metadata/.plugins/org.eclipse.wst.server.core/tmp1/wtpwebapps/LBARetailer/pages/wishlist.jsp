<!doctype html>
<%@page import="com.entity.WhishlistModel"%>
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
		String userId = "2";
		int type = 1;
		String home = "home_retailer.jsp";
		if (session.getAttribute("USER_MODEL") != null) {
			Object o = session.getAttribute("USER_MODEL");
			if (o instanceof UserAccountModel) {
				UserAccountModel um = (UserAccountModel) session.getAttribute("USER_MODEL");
				userId = um.getUserid();
				type = 1;
				home = "home_customer.jsp";

			} else if (o instanceof RetailerMasterModel) {
				RetailerMasterModel um = (RetailerMasterModel) session.getAttribute("USER_MODEL");
				userId = um.getRetailerId();
				type = 2;
				home = "home_retailer.jsp";
			}
		}
	%>

	<div class="slide story" id="slide-4" data-slide="1"
		style="background-color: #C3DC65; color: #000000; padding: 0px">
		<div class="container">
			<div id="home-row-1" class="row clearfix">
				<div class="col-12">
					<h1 class="font-semibold">
						<a href="<%=request.getContextPath()%>/pages/<%=home%>">
						<img src="<%=request.getContextPath()%>/theme/images/home.png" />
						</a> WishList
					</h1>
					<div class="row content-row" style="margin: auto;">
						<form action="">
							<table class="simple">
								<tr>
									<td>Sr.No</td>
									<td>Advertisement Title</td>
									<td>Advertisement Description</td>
									<td>Form Date</td>
									<td>To Date</td>
									<td>Select Item</td>
								</tr>
								<%
// 									ArrayList arr = ConnectionManager.getProducts();
								ArrayList arr = ConnectionManager.getWishListItem(userId); 
									for (int i = 0; i < arr.size(); i++) {
										WhishlistModel wishlist = (WhishlistModel) arr.get(i);
								%>
								<tr>
									<td><%=i + 1%></td>
									<td><%=wishlist.getAdTitle()%></td>
									<td><%=wishlist.getAdsDesc()%></td>
									<td><%=wishlist.getFdate()%></td>
									<td><%=wishlist.getTdate()%></td>
									<td><input type="checkbox" name="productId[]"
										id="productId<%=i + 1%>" value="<%=wishlist.getAdsId()%>" /></td>  
								</tr>
								<%
									}
								%>
								<tr>
									<td colspan="3"><input type="button" value="Delete"
										onclick="fnUpdate();" /></td>
								</tr>
							</table>
						</form>
					</div>
					<!-- /col-12 -->
					<div class="col-md-12" style="margin: auto;">
							<a type="reset" id="ResetBtnId" style="background-color: black;"
								class="btn btn-danger pull-right"
								href="<%=request.getContextPath()%>/pages/<%=home%>">Main
								Menu</a> 

						</div>
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
		 alert(str);
	 }
		$.post("<%=request.getContextPath()%>/tiles/ajax.jsp?methodId=deleteProduct",
				'products='+str+"&userId=<%=userId%>&typeId=<%=type%>",
							function(data) {
								alert(data);
								if(data.indexOf('true')!=-1){
								alert('Selected Products have been Deleted.');
								}
							});
		}
	</script>
</body>
</html>