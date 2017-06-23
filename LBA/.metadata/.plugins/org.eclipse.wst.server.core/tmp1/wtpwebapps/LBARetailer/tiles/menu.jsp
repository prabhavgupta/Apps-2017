<%@page import="com.entity.RetailerMasterModel"%>
<%@page import="com.entity.UserAccountModel"%>
<%
	String home="home_retailer.jsp";
String name="Home";
if(session.getAttribute("USER_MODEL")!=null){
	Object o=session.getAttribute("USER_MODEL");
	if(o instanceof UserAccountModel){
		UserAccountModel um=(UserAccountModel)session.getAttribute("USER_MODEL");
		
		name="Welcome "+um.getUsername();
	
	}else if(o instanceof RetailerMasterModel){
		RetailerMasterModel um=(RetailerMasterModel)session.getAttribute("USER_MODEL");
		name=um.getShopname();
	}
}
%>

<div  data-activeslide="1" style="background-color: #ff432e;" >
<div class="row"  style="background-color: #ff432e">
	
		<div  style="margin-top: 1px;width: 90%" style="background-color: #ff432e">
		<h4 style="text-transform: capitalize;font-size: 18px;">&nbsp;&nbsp; <%=name %></h4>
		</div>          
<!-- 			<div class="col-8"> -->   
<!-- 				.navbar-toggle is used as the toggle for collapsed navbar content -->
<!-- 				<button type="button" class="navbar-toggle" data-toggle="collapse" -->
<!-- 					data-target=".navbar-responsive-collapse" style="left: 60px;"> -->
<!-- 					<span class="icon-bar"></span> <span class="icon-bar"></span> <span -->
<!-- 						class="icon-bar"></span> -->
<!-- 				</button> -->
<!-- 			</div> -->


<!-- 			<div class="nav-collapse collapse navbar-responsive-collapse"> -->
<!-- 				<ul class="nav row"> -->
<!-- 					<li data-slide="1" class="col-12 col-sm-2"><a id="menu-link-1" href="#slide-1" title="Next Section"><span class="icon icon-home"></span> <span class="text">HOME</span></a></li> -->
<!-- 					<li data-slide="2" class="col-12 col-sm-2"><a id="menu-link-2" href="#slide-2" title="Next Section"><span class="icon icon-user"></span> <span class="text">ABOUT US</span></a></li> -->
<!-- 					<li data-slide="3" class="col-12 col-sm-2"><a id="menu-link-3" href="#slide-3" title="Next Section"><span class="icon icon-briefcase"></span> <span class="text">PORTFOLIO</span></a></li> -->
<!-- 					<li data-slide="4" class="col-12 col-sm-2"><a id="menu-link-4" href="#slide-4" title="Next Section"><span class="icon icon-gears"></span> <span class="text">PROCESS</span></a></li> -->
<!-- 					<li data-slide="5" class="col-12 col-sm-2"><a id="menu-link-5" href="#slide-5" title="Next Section"><span class="icon icon-heart"></span> <span class="text">CLIENTS</span></a></li> -->
<!-- 					<li data-slide="6" class="col-12 col-sm-2"><a id="menu-link-6" href="#slide-6" title="Next Section"><span class="icon icon-envelope"></span> <span class="text">CONTACT</span></a></li> -->
<!-- 				</ul> -->
<!-- 				<div class="row"> -->
<!-- 					<div class="col-sm-2 active-menu"></div> -->
<!-- 				</div> -->
<!-- 			</div> -->
		
		</div>
	
	</div>
	