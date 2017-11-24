<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
<head>
<title>차/지상 철도차량 모니터링 시스템</title>
<!-- all libraries -->
<%@ include file="subPage-libraries.jsp"%>
<%-- <%@ include file="subPage-common-css.jsp"%>--%>
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

.error {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #a94442;
	background-color: #f2dede;
	border-color: #ebccd1;
}

.msg {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #31708f;
	background-color: #d9edf7;
	border-color: #bce8f1;
}

</style>

<body onload='document.loginForm.username.focus();'>

	<!-- Navigation bar -->
	<%@ include file="subPage-navigationBar.jsp"%>

	<!-- Container -->
	<div style="padding-top: 2%; padding-bottom: 5%; height: 100vh" class=" main-bg">
		
		<div class="container">
			<div class="row" style="padding-top: 23%;">
				<div class="col-md-3 login-area">
					<div class="col-md-12">
						<div class="row">
						</div>
						<div class="row">
							
							<c:if test="${not empty error}">
								<div class="error">${error}</div>
							</c:if>
							<c:if test="${not empty msg}">
								<div class="msg">${msg}</div>
							</c:if>
							
							<form name='loginForm'
								action="login" method='POST'>
								
								<div class="form-group">
									<label class="sr-only" for="username">Email address</label>
										<input type="email" class="form-control" name='username' placeholder="Email address" required>
								</div>
								<div class="form-group">
									<label class="sr-only" for="password">Password</label>
									<input type="password" name='password' class="form-control" placeholder="Password" required>
									<div class="help-block text-right">
										<a href="#">Forget the password?</a>
									</div>
								</div>
								<div class="form-group">
									<button type="submit" name="submit" class="btn btn-success btn-block">Sign in</button>
								</div>

								<%-- <input type="hidden" name="${_csrf.parameterName}"
								value="${_csrf.token}" /> --%>
							</form>
						</div>
						
					</div>
					<div class="bottom text-right">
						<a href="#"><b>Register</b></a>
					</div>
				</div>
			</div>
		</div>

	</div>

	<!-- Footer -->
	<%@ include file="subPage-footer.jsp"%>

	<!-- Setting Page -->
	<%-- <%@ include file="subPage-modalSettingPage.jsp"%> --%>
</body>
</html>