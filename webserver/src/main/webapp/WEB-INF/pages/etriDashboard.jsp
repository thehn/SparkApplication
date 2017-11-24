<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>DashBoard_ETRI</title>
	
	<script src="<spring:url value="/resources/js/dashboardMain.js?1.0"/>"
		language="JavaScript" type="text/javascript"></script>
	
	<style>
		#chartdiv {
			width	: 100%;
			height	: 500px;
		}
		#chartdiv1 {
			width	: 100%;
			height	: 500px;
		}
		#chartdiv2 {
			width	: 100%;
			height	: 500px;
		}
		.bg-1 {
			background-color: #1abc9c;
			color: #ffffff;
		}
		.bg-2 { 
			background-color: #474e5d; /* Dark Blue */
			color: #ffffff;
	  	}
	</style>
	
	<script src="https://www.amcharts.com/lib/3/amcharts.js"></script>
	<script src="https://www.amcharts.com/lib/3/serial.js"></script>
	<script src="https://www.amcharts.com/lib/3/plugins/export/export.min.js"></script>
	<link rel="stylesheet" href="https://www.amcharts.com/lib/3/plugins/export/export.css" type="text/css" media="all" />
	<script src="https://www.amcharts.com/lib/3/themes/light.js"></script>
	
	
	
	
</head>
<body>

<div id="chartdiv"></div>
<h4>Data: ${dataCount}</h4>
<h4>Data: ${dataName}</h4>
<h4>a1: ${a1}</h4>
<h4>a2: ${a2}</h4>
<h4>Data: ${b}</h4>
<h4>Data: ${c}</h4>
<h4>Data: ${d}</h4>
</body>
</html>