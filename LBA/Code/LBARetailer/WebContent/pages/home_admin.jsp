<!doctype html>
<html>
<head lang="en">
<%@include file="../tiles/inc.jsp"%>

</head>
<%@include file="../tiles/menu.jsp"%>
<body>
	<!-- Start Main Body Section -->

	<!-- End Main Body Section -->


	<!-- === Arrows === -->
	<div class="slide story" id="slide-1" data-slide="1" style="background-color: black;margin: 0px;padding: 0px;">
		<div class="container">
			<div id="home-row-1" class="row clearfix">
				<div class="col-12">
					<h1 class="font-semibold" >
<!-- 						Welcome<span class="font-thin">Home</span> -->
					</h1>
<!-- 					<h4 class="font-thin" style="margin-top: 0px;"> -->
<!-- 						We are an <span class="font-semibold">independent -->
<!-- 							interactive agency</span> based in London. -->
<!-- 					</h4> -->


					<div class="row content-row">
						<div class="row subtitle-row "  style="padding: 0px;margin: 0px;">
							<div class="col-6" >
								<div class="menu-item blue">
									<a href="<%=request.getContextPath()%>/pages/generateReport.jsp?type=3"> <i class="fa fa-magic"></i>
										<p>
									Retailers
										</p>
									</a>
								</div>

							</div>
							<div class="col-6">


								<div class="menu-item green">
									<a href="<%=request.getContextPath()%>/pages/generateReport.jsp?type=1"> <i class="fa fa-magic"></i>
										<p>Customers</p>
									</a>
								</div>

							</div>
						</div>
						<div class="row subtitle-row" style="padding: 0px;margin: 0px;">
							<div class="col-6">
								<div class="menu-item light-red">
										<a href="<%=request.getContextPath()%>/pages/generateReport.jsp?type=4"> <i class="fa fa-magic"></i>
										<p>Viewers</p>
									</a>
								</div>



							</div>

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