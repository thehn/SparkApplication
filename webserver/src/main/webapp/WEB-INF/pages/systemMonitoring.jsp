<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>STM</title>
<!-- all libraries -->
<%@ include file="subPage-libraries.jsp"%>
<%-- <%@ include  --%>file="subPage-common-css.jsp"%>
<link rel="stylesheet" type="text/css" href="<spring:url value="/resources/css/commonStyle.css"/>">
<link rel="stylesheet" type="text/css" href="<spring:url value="/resources/css/systemMonitoring.css"/>">
</head>
	
<script type="text/javascript">
	$(function() {
		// activate tab name
		$("ul").find("li").removeClass("active");
		$("#sysMonitor").addClass("active");
	});
</script>


<body>
	<!-- Navigation bar -->
	<%@ include file="subPage-navigationBar.jsp"%>
	<div style="padding-top: 5%; padding-bottom: 5%;">
		<div class="outer">
			<div class="panel_all">
				<div class="panel_01"></div>
				<div class="panel_02"></div>
				<div class="panel_03"></div>
				<div class="clear"></div>
				<div class="panel_04"></div>
				<div class="panel_05"></div>
				<div class="panel_06"></div>
				<div class="clear"></div>
				<div class="panel_07"></div>
				<div class="panel_08"></div>
				<div class="clear"></div>
				<div class="panel_09"></div>
				<div class="panel_10"></div>
				<div class="panel_11"></div>
				<div class="clear"></div>
				<div class="blank"></div>
			</div>
		</div>
	</div>
	
	
	<!-- Footer -->
	<%@ include file="subPage-footer.jsp"%>
	<!-- Setting Page -->
	<%@ include file="subPage-modalSettingPage.jsp"%>
</body>
</html>