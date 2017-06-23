<!doctype html>
<html>
<head lang="en">
<%@include file="../tiles/inc.jsp"%>

</head>

<body>
<%@include file="../tiles/menu.jsp"%>
	
	<div class="slide story" id="slide-1" data-slide="1" style="background-color: black;margin: 0px;padding: 0px;">
		<div class="container">
			<div id="home-row-1" class="row clearfix">
				<div class="col-12">
					<h1 class="font-semibold" >
					
					</h1>



					<div class="row content-row">
						<div class="row subtitle-row "  style="padding: 0px;margin: 0px;">
							<div class="col-6" >
								<div class="menu-item blue">
									<a href="<%=request.getContextPath()%>/pages/postAds.jsp"> <i class="fa fa-magic"></i>
										<p>
									Post Ads
										</p>
									</a>
								</div>

							</div>
							<div class="col-6">


								<div class="menu-item green">
									<a href="<%=request.getContextPath()%>/pages/viewOffersCustomer.jsp?productType=getRetailerAds"
										> <i class="fa fa-magic"></i>
										<p>View Ads</p>
									</a>
								</div>

							</div>
						</div>
						<div class="row subtitle-row" style="padding: 0px;margin: 0px;">
							<div class="col-6">
								<div class="menu-item light-red">
									<a href="<%=request.getContextPath()%>/pages/generateReport.jsp?type=2"
										data-toggle="modal"> <i class="fa fa-user"></i>
										<p>View Viewers</p>
									</a>
								</div>



							</div>

							<div class="col-6">
								<div class="menu-item color responsive">
									<a href="<%=request.getContextPath()%>/pages/wishlist.jsp"
										data-toggle="modal"> <i class="fa fa-user"></i>
										<p>Update Products</p>
									</a>
								</div>



							</div>

						</div>
						<div class="row subtitle-row"  style="padding: 0px;margin: 0px;">
<!-- 							<div class="col-6"> -->
<!-- 								<div class="menu-item light-orange responsive-2"> -->
<%-- 									<a href="<%=request.getContextPath()%>/pages/reportsar.jsp"   --%>
<!-- 										data-toggle="modal"> <i class="fa fa-envelope-o"></i> -->
<!-- 										<p> -->
<!-- 											Edit Profile  -->
<!-- 										</p> -->
<!-- 									</a> -->
<!-- 								</div> -->



<!-- 							</div> -->

							<div class="col-6">
								<div class="menu-item green">
									<a data-toggle="modal" href="<%=request.getContextPath()%>/tiles/ajax.jsp?methodId=logout"> <i
										class="fa fa-envelope-o"></i>
										<p>Logout</p>
									</a>
								</div>




							</div>

						</div>
						


						<!-- /row -->
					</div>
					<!-- /col-12 -->
				</div>
				<!-- /row -->
			</div>
			<!-- /container -->
		</div>
		<!-- /slide1 -->
	</div>
	<!-- === Slide 5 === -->
	<div class="slide story" id="slide-5" data-slide="5" style="display: none;">
		<div class="container">
	<div class="row">
								
								
							
							</div>
		</div><!-- /container -->
	</div><!-- /slide5 -->
</body>


</html>