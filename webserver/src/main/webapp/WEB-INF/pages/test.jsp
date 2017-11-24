<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>

<!-- all libraries -->
<%@ include file="subPage-libraries.jsp"%>
<link rel="stylesheet" type="text/css" href="<spring:url value="/resources/css/commonStyle.css"/>">

	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="https://code.highcharts.com/highcharts.js"></script>
	<script src="https://code.highcharts.com/modules/exporting.js"></script>
	<script src="https://code.highcharts.com/modules/data.js"></script>
	<script src="https://code.highcharts.com/modules/drilldown.js"></script>

</head>
<body>

<script type='text/javascript'>


//chart1

	Highcharts.chart('test', {
		exporting: { 
			enabled: false 
			},
	    credits: {
	        enabled: false
	    },
	    chart: {
	        type: 'spline'
	    },
	    title: {
	        text: ''
	    },
	    
	    xAxis: {
	        type: 'datetime',
	        labels: {
	            overflow: 'justify'
	        }
	    },
	    yAxis: {
	        title: {
	            text: ''
	        },
	        minorGridLineWidth: 0,
	        gridLineWidth: 0,
	        alternateGridColor: null,
	       
	    },
	    tooltip: {
	        valueSuffix: ' m/s'
	    },
	    plotOptions: {
	        spline: {
	            lineWidth: 4,
	            states: {
	                hover: {
	                    lineWidth: 5
	                }
	            },
	            marker: {
	                enabled: false
	            },
	            pointInterval: 3600000, // one hour
	            pointStart: Date.UTC(2015, 4, 31, 0, 0, 0)
	        }
	    },
	    series: [{
	    	color: '#FFE11A',
	        name: 'MC',
	        data: [0.2, 0.8, 0.8, 0.8, 1, 1.3, 1.5, 2.9, 1.9, 2.6, 1.6, 3, 4, 3.6, 4.5, 4.2, 4.5, 4.5, 4, 3.1, 2.7, 4, 2.7, 2.3, 2.3, 4.1, 7.7, 7.1, 5.6, 6.1, 5.8, 8.6, 7.2, 9, 10.9, 11.5, 11.6, 11.1, 12, 12.3, 10.7, 9.4, 9.8, 9.6, 9.8, 9.5, 8.5, 7.4, 7.6]

	    }, {
	    	color: '#1F8A70',
	        name: 'EC',
	        data: [0, 0, 0.6, 0.9, 0.8, 0.2, 0, 0, 0, 0.1, 0.6, 0.7, 0.8, 0.6, 0.2, 0, 0.1, 0.3, 0.3, 0, 0.1, 0, 0, 0, 0.2, 0.1, 0, 0.3, 0, 0.1, 0.2, 0.1, 0.3, 0.3, 0, 3.1, 3.1, 2.5, 1.5, 1.9, 2.1, 1, 2.3, 1.9, 1.2, 0.7, 1.3, 0.4, 0.3]
	    }, {
	    	color: '#BEDB39',
	        name: 'TC',
	        data: [0, 0, 0.6, 0.9, 0.8, 0.2, 0, 0, 0, 0.1, 0, 0.7, 0.8, 0.9, 0.2, 0, 0.1, 0.3, 0.3, 0, 0.1, 0.5, 0.5, 0, 0.2, 0.1, 0, 0.3, 0, 0.1, 0.2, 0.1, 0.3, 0.3, 0, 3.1, 3.1, 2.5, 1.5, 1.9, 2.1, 1, 2.3, 1.9, 1.2, 0.7, 1.3, 0.4, 0.3]
	    }, {
	    	color: '#FD7400',
	        name: 'CALL',
	        data: [0, 0, 0.2, 0.4, 0.8, 0.2, 0, 0, 0, 0.1, 0.1, 0.7, 0.8, 0.3, 0.2, 0, 0.1, 0.3, 0.9, 0, 0.9, 0, 0, 0, 0.9, 0.1, 0, 0.3, 0, 0.1, 0.2, 0.1, 0.3, 0.3, 0, 3.1, 3.1, 2.5, 1.5, 1.9, 2.1, 1, 2.3, 1.9, 1.2, 0.7, 1.3, 0.4, 0.3]
	    }],
	    navigation: {
	        menuItemStyle: {
	            fontSize: '10px'
	        }
	    }
	});
	
</script>
	<!-- Navigation bar -->
	<%@ include file="subPage-navigationBar.jsp"%>
	
	<!-- Footer -->
	<%@ include file="subPage-footer.jsp"%>

	<!-- Setting Page -->
	<%@ include file="subPage-modalSettingPage.jsp"%>
	
</body>
</html>