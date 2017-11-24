<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- <%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%> --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
<head>
<title>차/지상 철도차량 모니터링 시스템</title>
<!-- all libraries -->
<%@ include file="subPage-libraries.jsp"%>
<%-- <%@ include file="subPage-common-css.jsp"%> --%>
<link rel="stylesheet" type="text/css" href="<spring:url value="/resources/css/commonStyle.css"/>">

<head>
<meta charset="utf-8">

<title>Home Landing Page</title>

<style type="text/css">
h1 {
	text-shadow: #525252 0px 0px 20px;
	font-weight: bold;
}

h4 {
	font-weight: bold;
}

pre {
	border: 0;
	background-color: transparent;
	white-space: nowrap;
}

.col-md-6 p {
	color: #555;
}


.key_features {
	text-shadow: none;
	font-weight: bold;
}

.tagline {
	text-shadow: #525252 0px 0px 20px;
}

.center {
	width: 300px;
	margin: 0 auto;
	text-align: left;
}

.custom-image {
	position: relative;
	bottom: 50px;
	padding: 0px;
	margin: 0 auto;
	margin-top: 100px;
	width: auto;
	height: auto;
	border: solid;
	border-width: 3px;
	box-shadow: 3px;
	opacity: 0.9;
	padding-right: 0px;
	padding-left: 0px;
}

.custom-title{
	position: relative;
	bottom: 50px;
	padding: 0px;
	margin: 0 auto;
	margin-top: 120px;
	width: auto;
	height: auto;
	opacity: 0.9;
}

.logo-image {
	width: 150px;
	height: auto;
	display: initial;
}

.jumbotron {
	padding-top: 0;
	background-color: #4CAF50;
	color: #ffffff;
}

.main-bg{
	background: no-repeat fixed center;
	background-image: url('<c:url value="/resources/img/main_bg_v5.jpg?v=5"/>');
	background-size:100% auto;
} 

.login-area{
	background-color: black;
	z-index: 0;
	opacity: 0.6;
	font-size: 18px;
	color: white;
	padding: 30px;
}

#p{
	color: white;
}

</style>

<script type="text/javascript">
	$(function() {
		// activate tab name
		$("ul").find("li").removeClass("active");
		$("#homeNav").addClass("active");
	});
</script>

<body>

	<!-- Navigation bar -->
	<%@ include file="subPage-navigationBar.jsp"%>

	<!-- Container -->
	<div style="padding-top: 2%; padding-bottom: 5%; height: 100vh" class=" main-bg">
		
		<div class="container">
			<%-- <div class="row">
				<div class="col-md-6">
					<img src="<spring:url value="/resources/img/main_title.png"/>"
					class="img-responsive custom-title">
				</div>
			</div> --%>
			<div class="row" style="padding-top: 23%;">
				<div class="col-md-3 login-area">
					<div class="col-md-12">
						<div class="row">
						</div>
						<div class="row">
							<form class="form" role="form" method="post" action="login"
								accept-charset="UTF-8" id="login-nav">
								<div class="form-group">
									<label class="sr-only" for="exampleInputEmail2">Email
										address</label> <input type="email" class="form-control"
										id="exampleInputEmail2" placeholder="Email address" required>
								</div>
								<div class="form-group">
									<label class="sr-only" for="exampleInputPassword2">Password</label>
									<input type="password" class="form-control"
										id="exampleInputPassword2" placeholder="Password" required>
									<div class="help-block text-right">
										<a href="#">Forget the password?</a>
									</div>
								</div>
								<div class="form-group">
									<!-- <button type="button" class="btn btn-primary btn-block">Sign in</button> -->
									<a href="/ml/dashboard_demo" class="btn btn-primary btn-block" role="button">Sign in</a>
								</div>
								<!-- <div class="checkbox">
									<label> <input type="checkbox"> keep me logged-in
									</label>
								</div> -->
							</form>
						</div>
						
					</div>
					<div class="bottom text-right">
						<a href="#"><b>Register</b></a>
					</div>
				</div>
			</div>
		</div>

		<!-- <div class="container">
			<div class="col-md-6 text-area">
				<div class="row">
					<div class="col-md-12">
						<h2 class="key_features">Key Features</h2>
					</div>
				</div>
	
				<div class="row">
					<div class="col-md-12">
						<h3>
							Feature Selection:
							<a href="/ml/featuresSel-FSChiSqSelector">ChiSqSelector</a>
						</h3>
						<p>In machine learning and statistics, feature selection, also known as variable selection, attribute selection or variable subset selection, is the process of selecting a subset of relevant features (variables, predictors) for use in model construction.</p>
					</div>
				</div>
				
				<div class="row">
					<div class="col-md-12">
						<h3>
							Clustering: <a href="/ml/clustering-KmeansClustering">KMeans</a>,
							<a href="/ml/clustering-KMediodsClustering">KMediods</a>,
							<a href="/ml/clustering-DBSCAN">DBSCAN</a>
						</h3>
						<p>Clustering is the task of grouping a set of objects in such a way that objects in the same group (called a cluster) are more similar (in some sense or another) to each other than to those in other groups (clusters).</p>
					</div>
				</div>
	
				<div class="row">
					<div class="col-md-12">
						<h3>
							Classification:
							<a href="/ml/classification-rfc-homepage">Random Forest</a>,
							<a href="/ml/classification-svm-homepage">Support Vector Machine (SVM)</a>,
							<a href="/ml/classification-mlp-homepage">Multiple Layers Perceptron</a>,
							<a href="/ml/classification-nb-homepage">Naive Bayes</a>
						</h3>
						<p>In machine learning and statistics, classification is the problem of identifying to which of a set of categories (sub-populations) a new observation belongs, on the basis of a training set of data containing observations (or instances) whose category membership is known.</p>
					</div>
				</div>
				
				<div class="row">
					<div class="col-md-12">
						<h3>
							Schedule & Alert:
							<a href="/ml/scheduledJobs">Schedule</a>,
							<a href="/ml/#">Alert</a>
						</h3>
						<p> You can schedule a machine learning task to run at the specific time and achieve its alerts.</p>
					</div>
				</div>
			</div>
			
		</div> -->

	</div>

	<!-- Footer -->
	<%@ include file="subPage-footer.jsp"%>

	<!-- Setting Page -->
	<%-- <%@ include file="subPage-modalSettingPage.jsp"%> --%>
</body>
</html>