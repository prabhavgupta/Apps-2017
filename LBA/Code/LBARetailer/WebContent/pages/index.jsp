<!doctype html>
<%@page import="com.helper.StringHelper"%>
<html>
<head lang="en">
<!-- <meta http-equiv="refresh" content="5"> -->
<%@include file="../tiles/inc.jsp"%>
<script type="text/javascript"
	src="http://maps.google.com/maps/api/js?sensor=false&libraries=places"></script>
</head>

<body style="background-color: black;">
	<%-- 	<%@include file="../tiles/menu.jsp" %> --%>
	<%
		String imei = StringHelper.n2s(request.getParameter("imei"));
	%>
	<!-- === MAIN Page Start  === -->
	<div class="slide story" id="slide-1" data-slide="1"
		style="background-color: black;">
		<div class="container">
			<div id="home-row-1" class="row clearfix">
				<div class="col-12">
					<h1 class="font-semibold">
						Location Based <span class="font-thin">Ads</span>
					</h1>
					<div class="row subtitle-row">
						<div class="col-12">
							<a href="#" style="width: 200px; height: 50px; font-size: 20px;"
								class="btn btn-success pull-center" onclick="fnCustomer(1);">Customer</a>
							<BR> <BR>
							<button type="button" id="RegisterMeBtn"
								style="width: 200px; height: 50px; font-size: 20px;"
								class="btn btn-success pull-center" onclick="fnCustomer(2);">Retailer</button>
							<BR> <BR>
							<button type="button" id="RegisterMeBtn"
								style="width: 200px; height: 50px; font-size: 20px;"
								class="btn btn-success pull-center" onclick="fnCustomer(3);">Administrator</button>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- === MAIN Page End  === -->

	<!-- === Login Page Start  === -->
	<div class="slide story" id="slide-5" data-slide="2"
		style="display: none; padding: 20px;">
		<div class="container">
			<div id="home-row-1" class="row clearfix">
				<div class="col-12">
					<h1 class="font-semibold" id="h1Tag">
						<span>Login Here.</span>
					</h1>
					<hr />
					<div class="row subtitle-row">
						<div class="col-12">
							<!-- Form Start here  -->
							<form id="login-form" action="javascript:fnLogin();">
								<input type="hidden" name="typeId" id="typeId" value="1" />
								<div class="row" style="margin-left: 2px; margin-right: 2px;">

									<div class="col-md-6">

										<div class="form-group">
											<label for="name">Phone No</label> <input type="number"
												style="text-transform: capitalize;" class="form-control" value="8956232132"
												id="name" name="phoneNo" tabindex="1" title="Please enter 10 digits" pattern="[0-9]{10}"
												placeholder="Enter Phone name" required="required" />
										</div>

										<div class="form-group">
											<label for="name">Password</label> <input type="password"
												class="form-control" id="userpass" tabindex="9" value="Vishwa"
												placeholder="Enter Password" name="userpass"
												required="required" />
										</div>

									</div>

									<div class="col-md-12" style="text-align: center;">
										&nbsp;&nbsp;

										<!--Login buttton  -->

										<button type="submit" id="ResetBtnId"
											class="btn btn-success pull-center">Login</button>

										<!--registration button  -->

										<button type="button" id="RegisterMeBtn"
											onclick="fnShowRegistration();" style="margin-right: 4px;"
											class="btn btn-success pull-center" tabindex="13">Register</button>
									</div>

									<!-- === Arrows === -->
									<BR>
									<div class="col-md-12" style="text-align: right:;">
										<%=upArrow%>
									</div>
								</div>
							</form>
							<!-- Form Start here end here -->

						</div>
					</div>


				</div>
			</div>
		</div>
	</div>
	<!-- === Login Page End  === -->

	<!-- ===Start Retailer Registration === -->

	<!-- === Slide 5 === -->
	<div class="slide story" id="slide-retailer" data-slide="5"
		style="display: none; background-color: #C3DC65; color: #000000;">

		<!-- Retailer Registration form -->

		<form id="retailer-form" action="javascript:fnRetailerRegister();">
			<h1 class="font-semibold" style="margin-top: 0px;">Retailer
				Registration</h1>

			<div class="container">
				<div class="row"
					style="width: 80%; text-align: center; margin: auto;">
					<div class="col-md-6">
						<input type="hidden" name="imei" id="imei" value="<%=imei%>" />
						<div class="form-group">
							<label for="name">Owner Name</label> <input type="text"
								style="text-transform: capitalize;" class="form-control"
								id="ownername" name="ownername" tabindex="1" title="Please enter only characters" pattern="[A-Za-z ]+"
								placeholder="Enter Your name" required="required" />
						</div>
						<div class="form-group">
							<label for="name">Shop Name </label> <input type="text"
								class="form-control" id="shopName" name="shopName" tabindex="2"
								title="Please enter only characters" pattern="[A-Za-z ]+"
								placeholder="Enter Shop Name" required="required" />
						</div>
						<div class="form-group">
							<label for="name">Shop Address</label>

							<div class="input-group">
								<span class="input-group-addon"><span
									class="glyphicon glyphicon-map-marker"></span> </span> <input
									type="text" class="form-control" tabindex="3"
									name="startlocation" id="startlocation" value=""
									placeholder="Enter Shop Location" required="required" />
							</div>
						</div>
						<div class="form-group">
							<label for="name">Shop License No </label> <input type="text"
								class="form-control" id="licenseNo" name="licenseNo"
								tabindex="4" placeholder="Enter License No" required="required" />
						</div>
						<div class="form-group">
							<label for="name">Phone No</label> <input type="text"
								class="form-control" name="phone" tabindex="5" id="phone"
								placeholder="Enter Phone No" required="required" maxlength="10"
								pattern="[0-9]{10}" />
						</div>
						<div class="form-group">
							<label for="email"> Email Address</label>
							<div class="input-group">
								<span class="input-group-addon"><span
									class="glyphicon glyphicon-envelope"></span> </span> <input
									type="email" class="form-control" tabindex="6" name="emailid"
									id="email" placeholder="Enter email" required="required"
									pattern="^\w+([.-]?\w+)*@\w+([.-]?\w+)*(.\w{2,3})+$" />
							</div>
						</div>
					</div>
					<div class="col-md-6">
						<div class="form-group">
							<label for="name">Password</label> <input type="password"
								class="form-control" id="userpass" tabindex="7"
								placeholder="Enter Password" name="userpass" required="required" />
						</div>
						<div class="form-group">
							<label for="name">Confirm Password</label> <input type="password"
								class="form-control" name="cuserpass" tabindex="8"
								id="cuserpass" placeholder="Enter Confirm Password"
								required="required" />
						</div>
					</div>
					<div class="col-md-12">
						&nbsp;&nbsp;
						<button type="reset" id="ResetBtnId"
							style="background-color: black;"
							class="btn btn-danger pull-right">Reset</button>
						&nbsp;&nbsp;
						<button type="submit" id="RegisterMeBtn"
							style="margin-right: 4px; background-color: black;"
							class="btn btn-danger pull-right" tabindex="13">Register
						</button>
					</div>
					<!-- === Arrows === -->
					<BR>
					<div class="col-md-12" style="text-align: right:;">
						<%=upArrow%>
					</div>
				</div>
			</div>
			<!-- /container -->
		</form>
		<!-- ===End Retailer Registration === -->
	</div>
	<!-- /slide5 -->
	
	<!-- === Slide 2 Customer Registration === -->
	<div class="slide story" id="slide-customer" data-slide="2"
		style="display: none; background-color: #1ABC9C; color: #000000;">
		<form id="customer-form" action="javascript:fnCustomerRegister();">
			<h1 class="font-semibold" style="margin-top: 0px;">Customer	Registration</h1>

			<div class="container">
				<div class="row" style="margin-left: 2px; margin-right: 2px;">
					<div class="col-md-6">
						<!-- userid, username, pass, emailid, phone, products, imei -->
						<div class="form-group">
							<label for="name">Name</label> <input type="text"
								style="text-transform: capitalize;" class="form-control"
								id="username" name="username" tabindex="1"
								title="Please enter only characters" pattern="[A-Za-z ]+"
								placeholder="Enter First name" required="required" />
						</div>
						<input type="hidden" name="imei" id="imei" value="<%=imei%>" />
						<div class="form-group">
							<label for="email">Email Address</label>
							<div class="input-group">
								<span class="input-group-addon"><span
									class="glyphicon glyphicon-envelope"></span> </span> <input
									type="email" class="form-control" tabindex="4" name="emailid"
									id="emailid" placeholder="Enter email" required="required"
									pattern="^\w+([.-]?\w+)*@\w+([.-]?\w+)*(.\w{2,3})+$" />
							</div>
						</div>
						<div class="form-group">
							<label for="name">Phone No</label> <input type="text"
								class="form-control" name="phoneno" tabindex="5" id="phone"
								placeholder="Enter Phone No" required="required" maxlength="10"
								pattern="[0-9]{10}" />
						</div>
						<div class="form-group">
							<label for="name">Password</label> <input type="password"
								class="form-control" id="userpass" tabindex="9"
								placeholder="Enter Password" name="userpass" required="required" />
						</div>
						<div class="form-group">
							<label for="name">Confirm Password</label> <input type="password"
								class="form-control" name="cuserpass" tabindex="10"
								id="cuserpass" placeholder="Enter Confirm Password"
								required="required" />
						</div>
					</div>
					<div class="col-md-12">
						&nbsp;&nbsp;
						<button type="reset" id="ResetBtnId"
							style="background-color: black;"
							class="btn btn-danger pull-right">Reset</button>
						&nbsp;&nbsp;
						<button type="submit" id="RegisterMeBtn"
							style="margin-right: 4px; background-color: black;"
							class="btn btn-danger pull-right" tabindex="13">Register</button>
					</div>
					<!-- === Arrows === -->
					<BR>
					<div class="col-md-12" style="text-align: right:;">
						<%=upArrow%>
					</div>
				</div>
			</div>
			<!-- /container -->
		</form>
		<!-- ===End Customer Registration === -->
	</div>
	<!-- /slide2 -->
<script>

$(document).ready(function(){

    var input = /** @type {HTMLInputElement} */(
		      document.getElementById('startlocation'));
	var searchBox = new google.maps.places.SearchBox(
			   (input));  
			    	   
});
function getScript(url) {
   // e = document.createElement('script');
   // e.src = url;
   // document.body.appendChild(e);


}

//------------------Registration function-------------------------

function fnRetailerRegister(){
	 var str = $( "#retailer-form" ).serialize();
		$.post("<%=request.getContextPath()%>/tiles/ajax.jsp?methodId=saveRetailer",
				str,
				function(data) {
			data=$.trim(data);
			alert(data);
			if(data!='Phone no already registered!'){
					$( "#retailer-form" )[0].reset();
					fnTop();
				}
				});
}

function fnCustomerRegister(){

	 var str = $( "#customer-form" ).serialize();
	$.post("<%=request.getContextPath()%>/tiles/ajax.jsp?methodId=saveCustomer",
			str,
			function(data) {
		data=$.trim(data);
		
		alert(data);
		if(data!='Phone no already registered!'){
		$( "#customer-form" )[0].reset();
		fnTop();
		}
			});

}

//------------------Login function-------------------------
function fnLogin() {
		 var str = $( "#login-form" ).serialize();
		$.post("<%=request.getContextPath()%>/tiles/ajax.jsp?methodId=checkLogin",
				str,
				function(data) {
			data=$.trim(data);
			if(data=='false'){
				alert('Invalid Credentials. Please try again.');
				$('#login-form')[0].reset();
			}else if(data=='true_c'){
				window.location.href='<%=request.getContextPath()%>/pages/home_customer.jsp';
			}else if(data=='true_r'){
				window.location.href='<%=request.getContextPath()%>/pages/home_retailer.jsp';
			}else if(data=='true_a'){
				window.location.href='<%=request.getContextPath()%>/pages/home_admin.jsp';
								}
							});
		}

		function fnShowRegistration() {
			if ($('#typeId').val() == '1') { //Customer
				sh('slide-customer');

			} else if ($('#typeId').val() == '2') { // Retailer
				sh('slide-retailer');
				str = window.ActivityObject.getLocation();
				if (str.indexOf('#') != -1) {
					str = str.substring(str.indexOf('#') + 1);
					$('#startlocation').val(str);
				}

			} else if ($('#typeId').val() == '3') { // Admin
				sh('slide-customer');
			}
		}
		
		function sh(id) {
			$('#' + id).fadeIn(1000);
			scrollToElement('#' + id, 600);
		}
		
		function fnCustomer(i) {
			if (i == 1) {
				$('#h1Tag').html('Customer Login');
				$('#typeId').val('1');
			} else if (i == 2) {
				$('#h1Tag').html('Retailer Login');
				$('#typeId').val('2');
			} else if (i == 3) {
				$('#h1Tag').html('Administrator Login');
				$('#typeId').val('3');
			}
			$('#slide-1').hide();
			sh('slide-5');
		}
		
		function fnTop() {
			$("#slide-5").hide();
			$("#slide-customer").hide();
			$("#slide-retailer").hide();

			sh('slide-1');
		}
/* 		

		function fnload(id) {
			
			if(id=="admin"){
				$("#admin").show();
				$("#retailer").hide();
				$("#customer").hide();

			}else if(id=="retailer"){
				$("#retailer").show();
				$("#admin").hide();
				$("#customer").hide();
				
				
				
			}else if(id=="customer"){
				$("#customer").show();
				$("#retailer").hide();
				$("#admin").hide();
				
			} 
		}*/
	</script>
</body>
</html>