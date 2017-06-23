<!doctype html>
<html>
<head lang="en">
<%@include file="../tiles/inc.jsp"%>

</head>
<%@include file="../tiles/menu.jsp"%>
<body>
	<!-- Start Main Body Section -->
	<div class="slide story" id="slide-1" data-slide="1"
		style="background-color: black; margin: 0px; padding: 0px;">
		<div class="container">
			<div id="home-row-1" class="row clearfix">
				<div class="col-12">
					<h1 class="font-semibold" id="addressId" style="font-size: 12px;"></h1>
					<div class="row content-row">
						<div class="row subtitle-row " style="padding: 0px; margin: 0px;">
							<div class="col-6">
								<div class="menu-item blue">
									<a
										href="<%=request.getContextPath()%>/pages/viewOffersCustomer.jsp?productType=myLocationProducts&productId=1">
										<i class="fa fa-magic"></i>
										<p>Clothing</p>
									</a>
								</div>
							</div>
							<div class="col-6">
								<div class="menu-item green">
									<a
										href="<%=request.getContextPath()%>/pages/viewOffersCustomer.jsp?productType=myLocationProducts&productId=2">
										<i class="fa fa-magic"></i>
										<p>Cosmetics</p>
									</a>
								</div>
							</div>
						</div>
						<div class="row subtitle-row" style="padding: 0px; margin: 0px;">
							<div class="col-6">
								<div class="menu-item light-red">
									<a
										href="<%=request.getContextPath()%>/pages/viewOffersCustomer.jsp?productType=myLocationProducts&productId=3">
										<i class="fa fa-magic"></i>
										<p>Electronics</p>
									</a>
								</div>
							</div>
							<div class="col-6">
								<div class="menu-item color responsive">
									<a
										href="<%=request.getContextPath()%>/pages/viewOffersCustomer.jsp?productType=myLocationProducts&productId=4">
										<i class="fa fa-magic"></i>
										<p>Food</p>
									</a>
								</div>
							</div>
						</div>
						<div class="row subtitle-row" style="padding: 0px; margin: 0px;">
							<div class="col-6">
								<div class="menu-item light-orange responsive-2">
									<a
										href="<%=request.getContextPath()%>/pages/viewOffersCustomer.jsp?productType=myLocationProducts&productId=5">
										<i class="fa fa-magic"></i>
										<p>Sports</p>
									</a>
								</div>
							</div>
							<div class="col-6">
								<div class="menu-item green">
									<a href="<%=request.getContextPath()%>/pages/wishlist.jsp"
										data-toggle="modal"> <i class="fa fa-user"></i>
										<p>Wishlist</p>
									</a>
								</div>
							</div>

						</div>
						<div class="row subtitle-row" style="padding: 0px; margin: 0px;">
							<div class="col-6">
								<div class="menu-item green">
									<a data-toggle="modal"
										href="<%=request.getContextPath()%>/tiles/ajax.jsp?methodId=logout">
										<i class="fa fa-envelope-o"></i>
										<p>Logout</p>
									</a>
								</div>
							</div>
						</div>
						
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- End Main Body Section -->
	<span id="timepass"></span>
	<script>
	function fnGetLocation(){
		try{
			str=window.ActivityObject.getLocation();
			if(str.indexOf('#')!=-1){
				latlong=str.substring(0,str.indexOf('#'));
				address=str.substring(str.indexOf('#')+1);
				if(address.length>0){
				$('#addressId').html('Your Current Location <BR>'+address);
				}
				$('#timepass').load("<%=request.getContextPath()%>/tiles/ajax.jsp?methodId=setLocation&latlong="
									+ latlong + "&address="
									+ encodeURI(address));
					return [ latlong, address ];
					//$('#startlocation').val(str);
				}
			} catch (err) {

			}
			return "";
		}

		$(document).ready(function() {
			fnGetLocation();
			setTimeout("fnGetLocation()", 2000);
		});
	</script>
</body>
</html>