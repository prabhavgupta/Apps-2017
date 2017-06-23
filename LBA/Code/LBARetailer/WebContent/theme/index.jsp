<!doctype html>
<html>
<head lang="en">
<!-- <meta http-equiv="refresh" content="5"> -->
	<%@include file="../tiles/inc.jsp" %>	
</head>
     
<body style="background-color:black;;">
	<%@include file="../tiles/menu.jsp" %>
	
	<!-- === MAIN Page Start  === -->
	<div class="slide story" id="slide-1" data-slide="1" style="background-color: black;" >
		<div class="container" >
			<div id="home-row-1" class="row clearfix">
				<div class="col-12">
					<h1 class="font-semibold">Location Based <span class="font-thin">Ads</span></h1>
					<div class="row subtitle-row">
						<div class="col-12">
							<a href="#"
								style="width: 200px; height: 50px; font-size: 20px;"
								class="btn btn-success pull-center" onclick="fnCustomer(1);">Customer</a>
							<BR>

							<BR>
							<button type="button" id="RegisterMeBtn"
								style="width: 200px; height: 50px; font-size: 20px;"
								class="btn btn-success pull-center" onclick="fnCustomer(2);">Retailer</button>
							<BR>
							<BR>
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
	<div class="slide story" id="slide-5" data-slide="2" style="display: none;padding: 20px;" >
		<div class="container" >
			<div id="home-row-1" class="row clearfix">
				<div class="col-12">
					<h1 class="font-semibold" id="h1Tag">Login Here.</span></h1>
					<hr/>
					<div class="row subtitle-row">
						<div class="col-12">
							<form id="contact-form" action="javascript:fnSubmit();">
								<div class="row" style="margin-left: 2px; margin-right: 2px;">

									<div class="col-md-6">

										<div class="form-group">
											<label for="name">Username</label> <input type="text"
												style="text-transform: capitalize;" class="form-control"
												id="name" name="fname" tabindex="1"
												placeholder="Enter First name" required="required" />
										</div>

										<div class="form-group">
											<label for="name">Password</label> <input type="password"
												class="form-control" id="userpass" tabindex="9"
												placeholder="Enter Password" name="userpass"
												required="required" />
										</div>

									</div>

									<div class="col-md-12"  style="text-align: center;">
									&nbsp;&nbsp;
										<button type="reset" id="ResetBtnId"  
											class="btn btn-success pull-center">Login</button>
										<button type="submit" id="RegisterMeBtn"
											style="margin-right: 4px;" class="btn btn-success pull-center"
											tabindex="13">Register</button>	
									
									
									</div>
									
										<!-- === Arrows === -->
							<BR>
							<div class="col-md-12"  style="text-align: right: ;">
						<%=upArrow %>
							</div>  
								</div>
							</form>

						</div>
					</div>

				 
			</div>
		</div>
	</div>
	</div>
	<!-- === Login Page End  === -->
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
		<!-- ===Start Retailer Registration === -->
	
		<!-- === Slide 5 === -->
	<div class="slide story" id="slide-5" data-slide="5" style="display: none;">
	<h1 class="font-semibold" style="margin-top: 0px;"> Retailer Registration </h1>
					
		<div class="container">
	<div class="row" style="margin-left: 2px;margin-right: 2px;">
								<div class="col-md-6">

									<div class="form-group">
										<label for="name">Name</label> <input type="text" style="text-transform: capitalize;"
											class="form-control" id="name" name="fname" tabindex="1"
											placeholder="Enter First name" required="required" />
									</div>

									<div class="form-group">
										<label for="email"> Email Address</label>
										<div class="input-group">
											<span class="input-group-addon"><span
												class="glyphicon glyphicon-envelope"></span> </span> <input
												type="email" class="form-control" tabindex="4"
												name="emailid" id="email" placeholder="Enter email"
												required="required" pattern="^\w+([.-]?\w+)*@\w+([.-]?\w+)*(.\w{2,3})+$"/>
										</div>
									</div>
									<div class="form-group">
										<label for="name">Phone No</label> <input type="text"
											class="form-control" name="phoneno" tabindex="5" id="phone"
											placeholder="Enter Phone No" required="required" maxlength="10" pattern="[0-9]{10}"/>
									</div>
								</div>
								<div class="col-md-6">
								
									<div class="form-group">
										<label for="name">User Name </label> <input type="text"
											class="form-control" id="username" name="username"
											tabindex="8" placeholder="User Name" required="required" />
									</div>
										
									

									<div class="form-group">
										<label for="name">Password</label> <input type="password"
											class="form-control" id="userpass" tabindex="9"
											placeholder="Enter Password" name="userpass"
											required="required" />
									</div>
									<div class="form-group">
										<label for="name">Confirm Password</label> <input
											type="password" class="form-control" name="cuserpass"
											tabindex="10" id="cuserpass"
											placeholder="Enter Confirm Password" required="required" />
									</div>
										<div class="form-group">
										<label for="name">Shop Name </label> <input type="text"
											class="form-control" id="username" name="username"
											tabindex="8" placeholder="User Name" required="required" />
									</div>
								
									<div class="form-group">
										<label for="name">Shop Address</label>

										<div class="input-group">
											<span class="input-group-addon"><span
												class="glyphicon glyphicon-map-marker"></span> </span> <input
												type="text" class="form-control" tabindex="12"
												name="startlocation" id="startlocation" value=""
												placeholder="Enter Start Location" required="required" />
										</div>
									</div>
								</div>
								<div class="col-md-12">
									&nbsp;&nbsp;
									<button type="reset" id="ResetBtnId" style="background-color: black;"
										class="btn btn-danger pull-right">Reset</button>
									&nbsp;&nbsp;
									<button type="submit" id="RegisterMeBtn" style="margin-right: 4px; background-color: black;"
										class="btn btn-danger pull-right" tabindex="13">Register
										</button>
								</div>
							</div>
		</div><!-- /container -->
	</div><!-- /slide5 -->
	<!-- ===End Retailer Registration === -->
	<!-- === Slide 2 === -->
	<div class="slide story" id="slide-2" data-slide="2" style="display: none;">
		<h1 class="font-semibold" style="margin-top: 0px;"> Customer Registration </h1>
					
		<div class="container">
	<div class="row" style="margin-left: 2px;margin-right: 2px;">
								<div class="col-md-6">

									<div class="form-group">
										<label for="name">Name</label> <input type="text" style="text-transform: capitalize;"
											class="form-control" id="name" name="fname" tabindex="1"
											placeholder="Enter First name" required="required" />
									</div>
										<div class="form-group">
										<label for="email"> Email Address</label>
										<div class="input-group">
											<span class="input-group-addon"><span
												class="glyphicon glyphicon-envelope"></span> </span> <input
												type="email" class="form-control" tabindex="4"
												name="emailid" id="email" placeholder="Enter email"
												required="required" pattern="^\w+([.-]?\w+)*@\w+([.-]?\w+)*(.\w{2,3})+$"/>
										</div>
									</div>
										<div class="form-group">
										<label for="name">Phone No</label> <input type="text"
											class="form-control" name="phoneno" tabindex="5" id="phone"
											placeholder="Enter Phone No" required="required" maxlength="10" pattern="[0-9]{10}"/>
									</div>
									<div class="form-group">
										<label for="name">Password</label> <input type="password"
											class="form-control" id="userpass" tabindex="9"
											placeholder="Enter Password" name="userpass"
											required="required" />
									</div>
									<div class="form-group">
										<label for="name">Confirm Password</label> <input
											type="password" class="form-control" name="cuserpass"
											tabindex="10" id="cuserpass"
											placeholder="Enter Confirm Password" required="required" />
									</div>

								</div>
							
								<div class="col-md-12">
									&nbsp;&nbsp;
									<button type="reset" id="ResetBtnId" style="background-color: black;"
										class="btn btn-danger pull-right">Reset</button>
									&nbsp;&nbsp;
									<button type="submit" id="RegisterMeBtn" style="margin-right: 4px; background-color: black;"
										class="btn btn-danger pull-right" tabindex="13">Register
										</button>
								</div>
							</div>
		</div><!-- /container -->
	</div><!-- /slide2 -->
	
</body>
<script >

$(document).ready(function(){
	var input = /** @type {HTMLInputElement} */(
		      document.getElementById('startlocation'));


	var searchBox = new google.maps.places.SearchBox(
			   (input));
	
			    	   
});
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
}
function fnTop(){
	$("#slide-5").hide();
	scrollToElement('#slide-1', 600);
}
function fnCustomer(i){
	if(i==1)
		$('#h1Tag').html('Customer Login');
	else if(i==2)
		$('#h1Tag').html('Retailer Login');
	else if(i==3)
		$('#h1Tag').html('Administrator Login');
	$('#slide-5').fadeIn(1000);
	scrollToElement('#slide-5', 600);
}
 var scrollToElement = function(el, ms){
	 
	    var speed = (ms) ? ms : 600;
	    $('html,body').animate({
	        scrollTop: $(el).offset().top
	    }, speed);
	
	}

</script>
</html>