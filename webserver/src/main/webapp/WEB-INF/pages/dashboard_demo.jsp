<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Dashboard_demo</title>
	
	<!-- all libraries -->
	<script src="<spring:url value="/resources/js/dashboardMain.js?1.0"/>"
		language="JavaScript" type="text/javascript"></script>
	<script src="<spring:url value="/resources/js/json2.js"/>"
		language="JavaScript" type="text/javascript"></script>
	
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
	<link rel="stylesheet" href="/ml/resources/css/dropdownCheckboxes/dropdownCheckboxes.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="/ml/resources/js/dropdownCheckboxes/dropdownCheckboxes.min.js"></script>
	<script src="/ml/resources/js/amcharts/amcharts.js"></script>
	<script src="/ml/resources/js/amcharts/serial.js"></script>
	<script src="/ml/resources/js/amcharts/pie.js"></script>
	<script src="/ml/resources/js/amcharts/plugins/export/export.min.js"></script>
	<link rel="stylesheet" href="/ml/resources/js/amcharts/plugins/export/export.css" type="text/css" media="all" />
	<script src="/ml/resources/js/amcharts/themes/light.js"></script>
	<%@ include file="subPage-libraries.jsp"%>
	<%-- <%@ include file="subPage-common-css.jsp"%> --%>
	<link rel="stylesheet" type="text/css" href="<spring:url value="/resources/css/commonStyle.css"/>">
	<link rel="stylesheet" type="text/css" href="<spring:url value="/resources/css/dashboard.css"/>">
	
	<style>
		#chartdiv {
			width	: 100%;
			height	: 200px;			
		}
		#chartdiv1 {
			width	: 100%;
			height	: 200px;			
		}
		#chartdiv2 {
			width	: 100%;
			height	: 200px;			
		}
		#chartdiv3 {
			width	: 100%;
			height	: 200px;			
		}
		#chartdiv4 {
			width	: 100%;
			height	: 200px;			
		}
		#chartdiv5 {
			width	: 100%;
			height	: 200px;			
		}
		#chartdiv6 {
			width	: 100%;
			height	: 200px;			
		}
		#chartdiv7 {
			width	: 100%;
			height	: 200px;			
		}
		#chartdiv8 {
			width	: 100%;
			height	: 200px;			
		}
		#chartdiv9 {
			width	: 100%;
			height	: 200px;			
		}
		#chartdiv10 {
			width	: 100%;
			height	: 200px;			
		}
		#chartdiv11 {
			width	: 100%;
			height	: 200px;			
		}
		#chartdivPie1 {
		  width: 100%;
		  height: 100%;
		}
		#chartdivPie2 {
		  width: 100%;
		  height: 100%;
		}
		#chartdivPie3 {
		  width: 100%;
		  height: 100%;
		}		
		#chartdivPie4 {
		  width: 100%;
		  height: 100%;
		}
		#chartdivPie5 {
		  width: 100%;
		  height: 100%;
		}
		#chartdivPie6 {
		  width: 100%;
		  height: 100%;
		}
	</style>	
		
	<script type="text/javascript">	
	
	$(function(){
		// activate tab name
		$("ul").find("li").removeClass("active");
		$("#mainDashboard").addClass("active");
		$(".cq-dropdown").dropdownCheckboxes();
	});

	//chart.addListener("dataUpdated", zoomChart);

	//function zoomChart(){
	//    chart.zoomToDates(new Date(1965, 0), new Date(1975, 0));
	//}	
	
	var chartPie1 = AmCharts.makeChart( "chartdivPie1", {
		  "type": "pie",
		  "theme": "light",
		  "dataProvider": [ {
		    "country": "Wheel",
		    "visits": 78
		  }, {
		    "country": "Bearing",
		    "visits": 45
		  } ],
		  "valueField": "visits",
		  "titleField": "country",
		  "startEffect": "elastic",
		  "startDuration": 0,
		  "labelRadius": -10,
		  "innerRadius": "30%",
		  "depth3D": 0,
		  "balloonText": "[[title]]<br><span style='font-size:15px'><b>[[value]]</b> ([[percents]]%)</span>",
		  "angle": 0,
		  "export": {
		    "enabled": false
		  }
		} );
	
	var chartPie2 = AmCharts.makeChart( "chartdivPie2", {
		  "type": "pie",
		  "theme": "light",
		  "dataProvider": [ {
		    "country": "Wheel",
		    "visits": 46
		  }, {
		    "country": "Bearing",
		    "visits": 55
		  } ],
		  "valueField": "visits",
		  "titleField": "country",
		  "startEffect": "elastic",
		  "startDuration": 0,
		  "labelRadius": -10,
		  "innerRadius": "30%",
		  "depth3D": 0,
		  "balloonText": "[[title]]<br><span style='font-size:15px'><b>[[value]]</b> ([[percents]]%)</span>",
		  "angle": 0,
		  "export": {
		    "enabled": false
		  }
		} );
	
	var chartPie3 = AmCharts.makeChart( "chartdivPie3", {
		  "type": "pie",
		  "theme": "light",
		  "dataProvider": [ {
		    "country": "Wheel",
		    "visits": 26
		  }, {
		    "country": "Bearing",
		    "visits": 64
		  } ],
		  "valueField": "visits",
		  "titleField": "country",
		  "startEffect": "elastic",
		  "startDuration": 0,
		  "labelRadius": -10,
		  "innerRadius": "30%",
		  "depth3D": 0,
		  "balloonText": "[[title]]<br><span style='font-size:15px'><b>[[value]]</b> ([[percents]]%)</span>",
		  "angle": 0,
		  "export": {
		    "enabled": false
		  }
		} );
	
	var chartPie4 = AmCharts.makeChart( "chartdivPie4", {
		  "type": "pie",
		  "theme": "light",
		  "dataProvider": [ {
		    "country": "MotorBlock",
		    "visits": 64
		  }, {
		    "country": "GearBox",
		    "visits": 43
		  }, {
		    "country": "Tripod",
		    "visits": 15
		  } ],
		  "valueField": "visits",
		  "titleField": "country",
		  "startEffect": "elastic",
		  "startDuration": 0,
		  "labelRadius": -10,
		  "innerRadius": "30%",
		  "depth3D": 0,
		  "balloonText": "[[title]]<br><span style='font-size:15px'><b>[[value]]</b> ([[percents]]%)</span>",
		  "angle": 0,
		  "export": {
		    "enabled": false
		  }
		} );
	
	var chartPie5 = AmCharts.makeChart( "chartdivPie5", {
		  "type": "pie",
		  "theme": "light",
		  "dataProvider": [ {
		    "country": "MotorBlock",
		    "visits": 37
		  }, {
		    "country": "GearBox",
		    "visits": 46
		  }, {
		    "country": "Tripod",
		    "visits": 24
		  } ],
		  "valueField": "visits",
		  "titleField": "country",
		  "startEffect": "elastic",
		  "startDuration": 0,
		  "labelRadius": -10,
		  "innerRadius": "30%",
		  "depth3D": 0,
		  "balloonText": "[[title]]<br><span style='font-size:15px'><b>[[value]]</b> ([[percents]]%)</span>",
		  "angle": 0,
		  "export": {
		    "enabled": false
		  }
		} );
	
	var chartPie6 = AmCharts.makeChart( "chartdivPie6", {
		  "type": "pie",
		  "theme": "light",
		  "dataProvider": [ {
		    "country": "MotorBlock",
		    "visits": 25
		  }, {
		    "country": "GearBox",
		    "visits": 43
		  }, {
		    "country": "Tripod",
		    "visits": 67
		  } ],
		  "valueField": "visits",
		  "titleField": "country",
		  "startEffect": "elastic",
		  "startDuration": 0,
		  "labelRadius": -10,
		  "innerRadius": "30%",
		  "depth3D": 0,
		  "balloonText": "[[title]]<br><span style='font-size:15px'><b>[[value]]</b> ([[percents]]%)</span>",
		  "angle": 0,
		  "export": {
		    "enabled": false
		  }
		} );	 
	
	function change_go(form){
		var value = form.gu_3.options[form.gu_3.selectedIndex].value;

		var data1 = '${w30_1}';
		var data2 = '${w30_2}';
		var data3 = '${w30_3}';
		obj1 = JSON.parse(data1);
		obj2 = JSON.parse(data2);
		obj3 = JSON.parse(data3);
		
		if (value == 33) {

			$("#demo3").show();
			
			var chart = AmCharts.makeChart("chartdiv9", {
			    "type": "serial",
			    "theme": "none",
			    //"titles":[{"text":"AVT"}],
			    "autoMarginOffset":25,
			    "dataProvider": obj1,
			    "valueAxes": [{
			        "axisAlpha": 0,
			        "position": "left"
			    }],
			    "graphs": [{
			        "id":"g1",
			        "balloonText": "[[category]]<br><b>[[value]] C</b>",
			        "lineColor":"#e53935",	        
			        "type": "step",
			        "lineThickness": 2,
			        "bullet":"square",
			        "bulletAlpha":0,
			        "bulletSize":4,
			        "bulletBorderAlpha":0,
			        "valueField": "value"
			    }],
			    "chartScrollbar": {
			        "graph":"g1",
			        "gridAlpha":0,
			        "color":"#888888",
			        "scrollbarHeight":25,
			        "backgroundAlpha":0,
			        "selectedBackgroundAlpha":0.1,
			        "selectedBackgroundColor":"#888888",
			        "graphFillAlpha":0,
			        "autoGridCount":true,
			        "selectedGraphFillAlpha":0,
			        "graphLineAlpha":1,
			        "graphLineColor":"#c2c2c2",
			        "selectedGraphLineColor":"#888888",
			        "selectedGraphLineAlpha":1
			    },
			    "chartCursor": {
			        "fullWidth":true,
			        "categoryBalloonDateFormat": "YYYY/MM/DD",
			        "cursorAlpha": 0.05,
			        "graphBulletAlpha": 1
			    },
			    "dataDateFormat": "YYYY/MM/DD",
			    "categoryField": "date",
			    "categoryAxis": {
			        
			        "parseDates": true,
			        "gridAlpha": 0
			    },
			    "export": {
			        "enabled": true
			     }
			});
			
			var chart1 = AmCharts.makeChart("chartdiv10", {
			    "type": "serial",
			    "theme": "none",
			    //"titles":[{"text":"ATC"}],
			    "autoMarginOffset":25,
			    "dataProvider": obj2,
			    "valueAxes": [{
			        "axisAlpha": 0,
			        "position": "left"
			    }],
			    "graphs": [{
			        "id":"g1",
			        "balloonText": "[[category]]<br><b>[[value]] C</b>",
			        "lineColor":"#e53935",	        
			        "type": "step",
			        "lineThickness": 2,
			        "bullet":"square",
			        "bulletAlpha":0,
			        "bulletSize":4,
			        "bulletBorderAlpha":0,
			        "valueField": "value"
			    }],
			    "chartScrollbar": {
			        "graph":"g1",
			        "gridAlpha":0,
			        "color":"#888888",
			        "scrollbarHeight":25,
			        "backgroundAlpha":0,
			        "selectedBackgroundAlpha":0.1,
			        "selectedBackgroundColor":"#888888",
			        "graphFillAlpha":0,
			        "autoGridCount":true,
			        "selectedGraphFillAlpha":0,
			        "graphLineAlpha":1,
			        "graphLineColor":"#c2c2c2",
			        "selectedGraphLineColor":"#888888",
			        "selectedGraphLineAlpha":1
			    },
			    "chartCursor": {
			        "fullWidth":true,
			        "categoryBalloonDateFormat": "YYYY/MM/DD",
			        "cursorAlpha": 0.05,
			        "graphBulletAlpha": 1
			    },
			    "dataDateFormat": "YYYY/MM/DD",
			    "categoryField": "date",
			    "categoryAxis": {
			        
			        "parseDates": true,
			        "gridAlpha": 0
			    },
			    "export": {
			        "enabled": true
			     }
			});
			
			var chart2 = AmCharts.makeChart("chartdiv11", {
			    "type": "serial",
			    "theme": "none",
			    //"titles":[{"text":"Severity"}],
			    "autoMarginOffset":25,
			    "dataProvider": obj3,
			    "valueAxes": [{
			        "axisAlpha": 0,
			        "position": "left"
			    }],
			    "graphs": [{
			        "id":"g1",
			        "balloonText": "[[category]]<br><b>[[value]] C</b>",
			        "lineColor":"#e53935",	        
			        "type": "step",
			        "lineThickness": 2,
			        "bullet":"square",
			        "bulletAlpha":0,
			        "bulletSize":4,
			        "bulletBorderAlpha":0,
			        "valueField": "value"
			    }],
			    "chartScrollbar": {
			        "graph":"g1",
			        "gridAlpha":0,
			        "color":"#888888",
			        "scrollbarHeight":25,
			        "backgroundAlpha":0,
			        "selectedBackgroundAlpha":0.1,
			        "selectedBackgroundColor":"#888888",
			        "graphFillAlpha":0,
			        "autoGridCount":true,
			        "selectedGraphFillAlpha":0,
			        "graphLineAlpha":1,
			        "graphLineColor":"#c2c2c2",
			        "selectedGraphLineColor":"#888888",
			        "selectedGraphLineAlpha":1
			    },
			    "chartCursor": {
			        "fullWidth":true,
			        "categoryBalloonDateFormat": "YYYY/MM/DD",
			        "cursorAlpha": 0.05,
			        "graphBulletAlpha": 1
			    },
			    "dataDateFormat": "YYYY/MM/DD",
			    "categoryField": "date",
			    "categoryAxis": {
			        
			        "parseDates": true,
			        "gridAlpha": 0
			    },
			    "export": {
			        "enabled": true
			     }
			});
			
		}
	}
	
	
	var cnt1 = 0;
	var cnt2 = 0;
	var cnt3 = 0;
	
	function btn(num) {
		if (num =="btn1") {
			
			if (cnt1 == 0) {
				$("#demo").show();
				cnt1 = 1;
			} else {
				$("#demo").hide();
				cnt1 = 0;
			}
			
			cnt2 = 0;
			cnt3 = 0;
			
			$("#demo1").hide();
			$("#demo2").hide();
			$("#demo3").hide();
			
			var data1 = '${w10_1}';
			var data2 = '${w10_2}';
			var data3 = '${w10_3}';
			obj1 = JSON.parse(data1);
			obj2 = JSON.parse(data2);
			obj3 = JSON.parse(data3);
			
// 			var obj = JSON.parse("[" + array + "]");
			var chart = AmCharts.makeChart("chartdiv", {
			    "type": "serial",
			    "theme": "none",
			    //"titles":[{"text":"AVT"}],
			    "autoMarginOffset":25,
			    "dataProvider": obj1,
			    "valueAxes": [{
			        "axisAlpha": 0,
			        "position": "left"
			    }],
			    "graphs": [{
			        "id":"g1",
			        "balloonText": "[[category]]<br><b>[[value]] C</b>",
			        "lineColor":"#43b348",	        
			        "type": "step",
			        "lineThickness": 2,
			        "bullet":"square",
			        "bulletAlpha":0,
			        "bulletSize":4,
			        "bulletBorderAlpha":0,
			        "valueField": "value"
			    }],
			    "chartScrollbar": {
			        "graph":"g1",
			        "gridAlpha":0,
			        "color":"#888888",
			        "scrollbarHeight":25,
			        "backgroundAlpha":0,
			        "selectedBackgroundAlpha":0.1,
			        "selectedBackgroundColor":"#888888",
			        "graphFillAlpha":0,
			        "autoGridCount":true,
			        "selectedGraphFillAlpha":0,
			        "graphLineAlpha":1,
			        "graphLineColor":"#c2c2c2",
			        "selectedGraphLineColor":"#888888",
			        "selectedGraphLineAlpha":1
			    },
			    "chartCursor": {
			        "fullWidth":true,
			        "categoryBalloonDateFormat": "YYYY/MM/DD",
			        "cursorAlpha": 0.05,
			        "graphBulletAlpha": 1
			    },
			    "dataDateFormat": "YYYY/MM/DD",
			    "categoryField": "date",
			    "categoryAxis": {
			        
			        "parseDates": true,
			        "gridAlpha": 0
			    },
			    "export": {
			        "enabled": true
			     }
			});
			
			var chart1 = AmCharts.makeChart("chartdiv1", {
			    "type": "serial",
			    "theme": "none",
			    //"titles":[{"text":"ATC"}],
			    "autoMarginOffset":25,
			    "dataProvider": obj2,
			    "valueAxes": [{
			        "axisAlpha": 0,
			        "position": "left"
			    }],
			    "graphs": [{
			        "id":"g1",
			        "balloonText": "[[category]]<br><b>[[value]] C</b>",
			        "lineColor":"#43b348",	        
			        "type": "step",
			        "lineThickness": 2,
			        "bullet":"square",
			        "bulletAlpha":0,
			        "bulletSize":4,
			        "bulletBorderAlpha":0,
			        "valueField": "value"
			    }],
			    "chartScrollbar": {
			        "graph":"g1",
			        "gridAlpha":0,
			        "color":"#888888",
			        "scrollbarHeight":25,
			        "backgroundAlpha":0,
			        "selectedBackgroundAlpha":0.1,
			        "selectedBackgroundColor":"#888888",
			        "graphFillAlpha":0,
			        "autoGridCount":true,
			        "selectedGraphFillAlpha":0,
			        "graphLineAlpha":1,
			        "graphLineColor":"#c2c2c2",
			        "selectedGraphLineColor":"#888888",
			        "selectedGraphLineAlpha":1
			    },
			    "chartCursor": {
			        "fullWidth":true,
			        "categoryBalloonDateFormat": "YYYY/MM/DD",
			        "cursorAlpha": 0.05,
			        "graphBulletAlpha": 1
			    },
			    "dataDateFormat": "YYYY/MM/DD",
			    "categoryField": "date",
			    "categoryAxis": {
			        
			        "parseDates": true,
			        "gridAlpha": 0
			    },
			    "export": {
			        "enabled": true
			     }
			});
			
			var chart2 = AmCharts.makeChart("chartdiv2", {
			    "type": "serial",
			    "theme": "none",
			    //"titles":[{"text":"Severity"}],
			    "autoMarginOffset":25,
			    "dataProvider": obj3,
			    "valueAxes": [{
			        "axisAlpha": 0,
			        "position": "left"
			    }],
			    "graphs": [{
			        "id":"g1",
			        "balloonText": "[[category]]<br><b>[[value]] C</b>",
			        "lineColor":"#43b348",	        
			        "type": "step",
			        "lineThickness": 2,
			        "bullet":"square",
			        "bulletAlpha":0,
			        "bulletSize":4,
			        "bulletBorderAlpha":0,
			        "valueField": "value"
			    }],
			    "chartScrollbar": {
			        "graph":"g1",
			        "gridAlpha":0,
			        "color":"#888888",
			        "scrollbarHeight":25,
			        "backgroundAlpha":0,
			        "selectedBackgroundAlpha":0.1,
			        "selectedBackgroundColor":"#888888",
			        "graphFillAlpha":0,
			        "autoGridCount":true,
			        "selectedGraphFillAlpha":0,
			        "graphLineAlpha":1,
			        "graphLineColor":"#c2c2c2",
			        "selectedGraphLineColor":"#888888",
			        "selectedGraphLineAlpha":1
			    },
			    "chartCursor": {
			        "fullWidth":true,
			        "categoryBalloonDateFormat": "YYYY/MM/DD",
			        "cursorAlpha": 0.05,
			        "graphBulletAlpha": 1
			    },
			    "dataDateFormat": "YYYY/MM/DD",
			    "categoryField": "date",
			    "categoryAxis": {
			        
			        "parseDates": true,
			        "gridAlpha": 0
			    },
			    "export": {
			        "enabled": true
			     }
			});
			
		} else if (num =="btn2") {
			
			if (cnt2 == 0) {
				$("#demo1").show();
				cnt2 = 1;
			} else {
				$("#demo1").hide();
				cnt2 = 0;
			}
			
			cnt1 = 0;
			cnt3 = 0;
			
			$("#demo").hide();
			$("#demo2").hide();
			$("#demo3").hide();
			
			var data1 = '${w20_1}';
			var data2 = '${w20_2}';
			var data3 = '${w20_3}';
			obj1 = JSON.parse(data1);
			obj2 = JSON.parse(data2);
			obj3 = JSON.parse(data3);
			
			var chart = AmCharts.makeChart("chartdiv3", {
			    "type": "serial",
			    "theme": "none",
			    //"titles":[{"text":"AVT"}],
			    "autoMarginOffset":25,
			    "dataProvider": obj1,
			    "valueAxes": [{
			        "axisAlpha": 0,
			        "position": "left"
			    }],
			    "graphs": [{
			        "id":"g1",
			        "balloonText": "[[category]]<br><b>[[value]] C</b>",
			        "lineColor":"#ff8f00",	        
			        "type": "step",
			        "lineThickness": 2,
			        "bullet":"square",
			        "bulletAlpha":0,
			        "bulletSize":4,
			        "bulletBorderAlpha":0,
			        "valueField": "value"
			    }],
			    "chartScrollbar": {
			        "graph":"g1",
			        "gridAlpha":0,
			        "color":"#888888",
			        "scrollbarHeight":25,
			        "backgroundAlpha":0,
			        "selectedBackgroundAlpha":0.1,
			        "selectedBackgroundColor":"#888888",
			        "graphFillAlpha":0,
			        "autoGridCount":true,
			        "selectedGraphFillAlpha":0,
			        "graphLineAlpha":1,
			        "graphLineColor":"#c2c2c2",
			        "selectedGraphLineColor":"#888888",
			        "selectedGraphLineAlpha":1
			    },
			    "chartCursor": {
			        "fullWidth":true,
			        "categoryBalloonDateFormat": "YYYY/MM/DD",
			        "cursorAlpha": 0.05,
			        "graphBulletAlpha": 1
			    },
			    "dataDateFormat": "YYYY/MM/DD",
			    "categoryField": "date",
			    "categoryAxis": {
			        
			        "parseDates": true,
			        "gridAlpha": 0
			    },
			    "export": {
			        "enabled": true
			     }
			});
			
			var chart1 = AmCharts.makeChart("chartdiv4", {
			    "type": "serial",
			    "theme": "none",
			    //"titles":[{"text":"ATC"}],
			    "autoMarginOffset":25,
			    "dataProvider": obj2,
			    "valueAxes": [{
			        "axisAlpha": 0,
			        "position": "left"
			    }],
			    "graphs": [{
			        "id":"g1",
			        "balloonText": "[[category]]<br><b>[[value]] C</b>",
			        "lineColor":"#ff8f00",	        
			        "type": "step",
			        "lineThickness": 2,
			        "bullet":"square",
			        "bulletAlpha":0,
			        "bulletSize":4,
			        "bulletBorderAlpha":0,
			        "valueField": "value"
			    }],
			    "chartScrollbar": {
			        "graph":"g1",
			        "gridAlpha":0,
			        "color":"#888888",
			        "scrollbarHeight":25,
			        "backgroundAlpha":0,
			        "selectedBackgroundAlpha":0.1,
			        "selectedBackgroundColor":"#888888",
			        "graphFillAlpha":0,
			        "autoGridCount":true,
			        "selectedGraphFillAlpha":0,
			        "graphLineAlpha":1,
			        "graphLineColor":"#c2c2c2",
			        "selectedGraphLineColor":"#888888",
			        "selectedGraphLineAlpha":1
			    },
			    "chartCursor": {
			        "fullWidth":true,
			        "categoryBalloonDateFormat": "YYYY/MM/DD",
			        "cursorAlpha": 0.05,
			        "graphBulletAlpha": 1
			    },
			    "dataDateFormat": "YYYY/MM/DD",
			    "categoryField": "date",
			    "categoryAxis": {
			        
			        "parseDates": true,
			        "gridAlpha": 0
			    },
			    "export": {
			        "enabled": true
			     }
			});
			
			var chart2 = AmCharts.makeChart("chartdiv5", {
			    "type": "serial",
			    "theme": "none",
			    //"titles":[{"text":"Severity"}],
			    "autoMarginOffset":25,
			    "dataProvider": obj3,
			    "valueAxes": [{
			        "axisAlpha": 0,
			        "position": "left"
			    }],
			    "graphs": [{
			        "id":"g1",
			        "balloonText": "[[category]]<br><b>[[value]] C</b>",
			        "lineColor":"#ff8f00",	        
			        "type": "step",
			        "lineThickness": 2,
			        "bullet":"square",
			        "bulletAlpha":0,
			        "bulletSize":4,
			        "bulletBorderAlpha":0,
			        "valueField": "value"
			    }],
			    "chartScrollbar": {
			        "graph":"g1",
			        "gridAlpha":0,
			        "color":"#888888",
			        "scrollbarHeight":25,
			        "backgroundAlpha":0,
			        "selectedBackgroundAlpha":0.1,
			        "selectedBackgroundColor":"#888888",
			        "graphFillAlpha":0,
			        "autoGridCount":true,
			        "selectedGraphFillAlpha":0,
			        "graphLineAlpha":1,
			        "graphLineColor":"#c2c2c2",
			        "selectedGraphLineColor":"#888888",
			        "selectedGraphLineAlpha":1
			    },
			    "chartCursor": {
			        "fullWidth":true,
			        "categoryBalloonDateFormat": "YYYY/MM/DD",
			        "cursorAlpha": 0.05,
			        "graphBulletAlpha": 1
			    },
			    "dataDateFormat": "YYYY/MM/DD",
			    "categoryField": "date",
			    "categoryAxis": {
			        
			        "parseDates": true,
			        "gridAlpha": 0
			    },
			    "export": {
			        "enabled": true
			     }
			});
			
		} else if (num =="btn3") {
			
			if (cnt3 == 0) {
				$("#demo2").show();
				cnt3 = 1;
			} else {
				$("#demo2").hide();
				cnt3 = 0;
			}
			
			cnt1 = 0;
			cnt2 = 0;
			
			$("#demo").hide();
			$("#demo1").hide();
			$("#demo3").hide();
			
			var data1 = '${w30_1}';
			var data2 = '${w30_2}';
			var data3 = '${w30_3}';
			obj1 = JSON.parse(data1);
			obj2 = JSON.parse(data2);
			obj3 = JSON.parse(data3);
			
// 			var obj = JSON.parse("[" + array + "]");
			var chart = AmCharts.makeChart("chartdiv6", {
			    "type": "serial",
			    "theme": "none",
			    //"titles":[{"text":"AVT"}],
			    "autoMarginOffset":25,
			    "dataProvider": obj1,
			    "valueAxes": [{
			        "axisAlpha": 0,
			        "position": "left"
			    }],
			    "graphs": [{
			        "id":"g1",
			        "balloonText": "[[category]]<br><b>[[value]] C</b>",
			        "lineColor":"#e53935",	        
			        "type": "step",
			        "lineThickness": 2,
			        "bullet":"square",
			        "bulletAlpha":0,
			        "bulletSize":4,
			        "bulletBorderAlpha":0,
			        "valueField": "value"
			    }],
			    "chartScrollbar": {
			        "graph":"g1",
			        "gridAlpha":0,
			        "color":"#888888",
			        "scrollbarHeight":25,
			        "backgroundAlpha":0,
			        "selectedBackgroundAlpha":0.1,
			        "selectedBackgroundColor":"#888888",
			        "graphFillAlpha":0,
			        "autoGridCount":true,
			        "selectedGraphFillAlpha":0,
			        "graphLineAlpha":1,
			        "graphLineColor":"#c2c2c2",
			        "selectedGraphLineColor":"#888888",
			        "selectedGraphLineAlpha":1
			    },
			    "chartCursor": {
			        "fullWidth":true,
			        "categoryBalloonDateFormat": "YYYY/MM/DD",
			        "cursorAlpha": 0.05,
			        "graphBulletAlpha": 1
			    },
			    "dataDateFormat": "YYYY/MM/DD",
			    "categoryField": "date",
			    "categoryAxis": {
			        
			        "parseDates": true,
			        "gridAlpha": 0
			    },
			    "export": {
			        "enabled": true
			     }
			});
			
			var chart1 = AmCharts.makeChart("chartdiv7", {
			    "type": "serial",
			    "theme": "none",
			    //"titles":[{"text":"ATC"}],
			    "autoMarginOffset":25,
			    "dataProvider": obj2,
			    "valueAxes": [{
			        "axisAlpha": 0,
			        "position": "left"
			    }],
			    "graphs": [{
			        "id":"g1",
			        "balloonText": "[[category]]<br><b>[[value]] C</b>",
			        "lineColor":"#e53935",	        
			        "type": "step",
			        "lineThickness": 2,
			        "bullet":"square",
			        "bulletAlpha":0,
			        "bulletSize":4,
			        "bulletBorderAlpha":0,
			        "valueField": "value"
			    }],
			    "chartScrollbar": {
			        "graph":"g1",
			        "gridAlpha":0,
			        "color":"#888888",
			        "scrollbarHeight":25,
			        "backgroundAlpha":0,
			        "selectedBackgroundAlpha":0.1,
			        "selectedBackgroundColor":"#888888",
			        "graphFillAlpha":0,
			        "autoGridCount":true,
			        "selectedGraphFillAlpha":0,
			        "graphLineAlpha":1,
			        "graphLineColor":"#c2c2c2",
			        "selectedGraphLineColor":"#888888",
			        "selectedGraphLineAlpha":1
			    },
			    "chartCursor": {
			        "fullWidth":true,
			        "categoryBalloonDateFormat": "YYYY/MM/DD",
			        "cursorAlpha": 0.05,
			        "graphBulletAlpha": 1
			    },
			    "dataDateFormat": "YYYY/MM/DD",
			    "categoryField": "date",
			    "categoryAxis": {
			        
			        "parseDates": true,
			        "gridAlpha": 0
			    },
			    "export": {
			        "enabled": true
			     }
			});
			
			var chart2 = AmCharts.makeChart("chartdiv8", {
			    "type": "serial",
			    "theme": "none",
			    //"titles":[{"text":"Severity"}],
			    "autoMarginOffset":25,
			    "dataProvider": obj3,
			    "valueAxes": [{
			        "axisAlpha": 0,
			        "position": "left"
			    }],
			    "graphs": [{
			        "id":"g1",
			        "balloonText": "[[category]]<br><b>[[value]] C</b>",
			        "lineColor":"#e53935",	        
			        "type": "step",
			        "lineThickness": 2,
			        "bullet":"square",
			        "bulletAlpha":0,
			        "bulletSize":4,
			        "bulletBorderAlpha":0,
			        "valueField": "value"
			    }],
			    "chartScrollbar": {
			        "graph":"g1",
			        "gridAlpha":0,
			        "color":"#888888",
			        "scrollbarHeight":25,
			        "backgroundAlpha":0,
			        "selectedBackgroundAlpha":0.1,
			        "selectedBackgroundColor":"#888888",
			        "graphFillAlpha":0,
			        "autoGridCount":true,
			        "selectedGraphFillAlpha":0,
			        "graphLineAlpha":1,
			        "graphLineColor":"#c2c2c2",
			        "selectedGraphLineColor":"#888888",
			        "selectedGraphLineAlpha":1
			    },
			    "chartCursor": {
			        "fullWidth":true,
			        "categoryBalloonDateFormat": "YYYY/MM/DD",
			        "cursorAlpha": 0.05,
			        "graphBulletAlpha": 1
			    },
			    "dataDateFormat": "YYYY/MM/DD",
			    "categoryField": "date",
			    "categoryAxis": {
			        
			        "parseDates": true,
			        "gridAlpha": 0
			    },
			    "export": {
			        "enabled": true
			     }
			});
			
		}
	}
	</script>
</head>


<body>
<div class="container-fluid" style="padding-top: 3%; padding-bottom: 3%;">
	<div class="col-xs-6">
		<div class="panel panel-default text-center">
			<div class="panel-heading">
				지상(Way-Side Diagnostics)
			</div>
			<div class="panel-body">
			  	<div class="row">
			  		<div class="col-xs-12">
			      		<div class="tCol col-xs-2">
			      			<div class="tCondition"><h5 lang="en">Major Equipment</h5></div>
		      			</div>
			      		<div class="tCol col-xs-2">
			      			<div class="tGood"><h5 lang="en">Good</h5></div>
		      			</div>
			      		<div class="tCol col-xs-2">
			      			<div class="tInspection"><h5 lang="en">Need Inspection</h5></div>
		      			</div>
			      		<div class="tCol col-xs-2">
			      			<div class="tFailure"><h5 lang="en">Failure Probable</h5></div>
		      			</div>
			      		<div class="tCol col-xs-2">
			      			<div class="tOperation"><h5 lang="en">No Operation</h5></div>
		      			</div>
		      		</div>
			    </div>
			    <div class="row">
			    	<div class="col-xs-12">
			    		<div class="imgBox col-xs-2">
				      		<div class="mImg">
			        			<img src="resources/img/part_01.jpg" style="width:110px; height:110px;"/>	        			
				      		</div>
			      		</div>
		      			<div class="col-xs-2 no-padding">
			        		<button type="button" class="btnChart btn-good" data-toggle="collapse" data-target="#demo">${wGoodCount}</button>							
			      		</div>
			      		<div class="col-xs-2 no-padding">
			        		<button type="button" class="btnChart btn-inspection" data-toggle="collapse">${wWarningCount}</button>							
			      		</div>
			      		<div class="col-xs-2 no-padding">
			        		<button type="button" class="btnChart btn-failure" data-toggle="collapse">${wFailCount}</button>							
		 		 		</div>
			      		<div class="col-xs-2 no-padding">
			        		<button type="button" class="btnChart btn-operation" data-toggle="collapse" data-target="#">${wNoOperCount}</button>
			      		</div>
		      		</div>
			    </div>
			    
			    <!-- 1차트 그려주는 부분(동적으로 바인딩하여 그려줘야 함) -->
			    <div id="demo" class="panel-collapse collapse" style="padding-left:2px;">
			    	<form class='myform'>
			    	<div class="dropdown cq-dropdown" data-name='statuses' style="text-align:left;">
			    		<button class="btn btn-primary dropdown-toggle" type="button" id="btndropdown" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">Check ID
			    			<span class="caret"></span></button>
			    		<ul class="dropdown-menu" aria-labelledby="btndropdown" id="myUL">
			    			<input class="form-control" id="myInput" onkeyup="myFunction()" type="text" placeholder="Search..">
			    			<li><a href="#">
			    			<label class="radio-btn">
			    				<input type="checkbox" value='28380'>28380
			    			</label>
			    			</a></li>
			    			<li><a href="#">
			    			<label class="radio-btn">
			    				<input type="checkbox" value='28381'>28381
			    			</label>
			    			</a></li>
			    			<li><a href="#">
			    			<label class="radio-btn">
			    				<input type="checkbox" value='28383'>28383
			    			</label>
			    			</a></li>
			    			<li class='text-center'>
              					<button type='button' class='btn btn-xs btn-danger clear close-dropdown' value='Clear'>Clear</button>
          					</li>
			    		</ul>
			    		</form>
			    	</div>
				    <div class="stepChartGood">
				    	<div class="row">
				    		<div class="col-xs-12">
				    			<div class="col-xs-2">
				    				<div class="row">
				    					<div class="col-xs-12">
				    						<div style="height: 40px; line-height: 40px; color: #43b348; font-size: 15px; font-weight: bold;">
												    ID : 28388BRS2
				    						</div>
			    						</div>
				    				</div>
				    				<div class="row" style="padding-top: 40px;">
			    						<div class="col-xs-12 text-center" style="height: 120px; padding: 0px;">AVT<br/>(Avg. Total Value)</div>
				    				</div>
				    				<div class="row" style="padding-top: 80px;">
				    					<div class="col-xs-12 text-center" style="height: 120px; padding: 0px;">ATC<br/>(Avg. Total Count)</div>
				    				</div>
				    				<div class="row" style="padding-top: 80px;">
				    					<div class="col-xs-12 text-center" style="height: 120px;">Severity</div>
				    				</div>
				    			</div>
				    			<div class="col-xs-10" style="padding-left: 0px;">
				    				<div class="row">
							    		<div class="col-xs-12">							    		
						        			<div id="chartdiv" class="col-xs-9" style="padding-left: 0px;"></div>
					        			</div>
					       			</div>
					       			<div class="row">
							    		<div class="col-xs-12">
						        			<div id="chartdiv1" class="col-xs-9" style="padding-left: 0px;"></div>
					        			</div>
					       			</div>
					       			<div class="row">
							    		<div class="col-xs-12">
						        			<div id="chartdiv2" class="col-xs-9" style="padding-left: 0px;"></div>
					        			</div>
					       			</div>
				    			</div>
				    		</div>
				    	</div>
	    			</div>    			
	      		</div>
	      		<!-- -------------------여기까지------------------- -->
	      		
	      		 <!-- 2차트 그려주는 부분(동적으로 바인딩하여 그려줘야 함) -->
			    <div id="demo1" class="panel-collapse collapse" style="padding-left:2px;">
			    	<form name="form1" method="post" style="width: 200px; height: 30px; padding-top: 3px;">
						<select name = "gu_3" style="width: 100%; height: 90%; border-radius: 0.5em;">
							<option value="11" selected="selected">28388BRS2</option>
							<option value="22">42574BRS2</option>
							<option value="33">10012BRS2</option>
						</select>
					</form>
				    <div class="stepChartInspection">
				    	<div class="row">
				    		<div class="col-xs-12">
				    			<div class="col-xs-2">
				    				<div class="row">
				    					<div class="col-xs-12">
				    						<div style="height: 40px; line-height: 40px; color: #ff8f00; font-size: 15px; font-weight: bold;">					    						
													    ID : 28388BRS2
				    						</div>
			    						</div>
				    				</div>
				    				<div class="row" style="padding-top: 40px;">
			    						<div class="col-xs-12 text-center" style="height: 120px; padding: 0px;">AVT<br/>(Avg. Total Value)</div>
				    				</div>
				    				<div class="row" style="padding-top: 80px;">
				    					<div class="col-xs-12 text-center" style="height: 120px; padding: 0px;">ATC<br/>(Avg. Total Count)</div>
				    				</div>
				    				<div class="row" style="padding-top: 80px;">
				    					<div class="col-xs-12 text-center" style="height: 120px;">Severity</div>
				    				</div>
				    			</div>
				    			<div class="col-xs-10" style="padding-left: 0px;">
				    				<div class="row">
							    		<div class="col-xs-12">							    		
						        			<div id="chartdiv3" class="col-xs-9" style="padding-left: 0px;"></div>
					        			</div>
					       			</div>
					       			<div class="row">
							    		<div class="col-xs-12">
						        			<div id="chartdiv4" class="col-xs-9" style="padding-left: 0px;"></div>
					        			</div>
					       			</div>
					       			<div class="row">
							    		<div class="col-xs-12">
						        			<div id="chartdiv5" class="col-xs-9" style="padding-left: 0px;"></div>
					        			</div>
					       			</div>
				    			</div>
				    		</div>
				    	</div>
	    			</div>    			
	      		</div>
	      		<!-- -------------------여기까지------------------- -->
	      		
	      		 <!-- 3차트 그려주는 부분(동적으로 바인딩하여 그려줘야 함) -->
			    <div id="demo2" class="panel-collapse collapse" style="padding-left:2px;">
			    	<form name="form1" method="post" style="width: 200px; height: 30px; padding-top: 3px;">
						<select name = "gu_3" onchange="change_go(this.form)" style="width: 100%; height: 90%; border-radius: 0.5em;">
							<option value="11" selected="selected">28388BRS2</option>
							<option value="22">42574BRS2</option>
							<option value="33">10012BRS2</option>
						</select>
					</form>
				    <div class="stepChartFailure">				    	
				    	<div class="row">
				    		<div class="col-xs-12">
				    			<div class="col-xs-2">
				    				<div class="row">
				    					<div class="col-xs-12">
				    						<div style="height: 40px; line-height: 40px; color: #e53935; font-size: 15px; font-weight: bold;">
				    							<!-- <form name="form1" method="post">
													<select name = "gu_3" onchange="change_go(this.form)">
														<option value="11" selected="selected">28388BRS2</option>
														<option value="22">42574BRS2</option>
														<option value="33">10012BRS2</option>
													</select>
												</form> -->
												    ID : 28388BRS2
				    						</div>
			    						</div>
				    				</div>
				    				<div class="row" style="padding-top: 40px;">
			    						<div class="col-xs-12 text-center" style="height: 120px; padding: 0px;">AVT<br/>(Avg. Total Value)</div>
				    				</div>
				    				<div class="row" style="padding-top: 80px;">
				    					<div class="col-xs-12 text-center" style="height: 120px; padding: 0px;">ATC<br/>(Avg. Total Count)</div>
				    				</div>
				    				<div class="row" style="padding-top: 80px;">
				    					<div class="col-xs-12 text-center" style="height: 120px;">Severity</div>
				    				</div>
				    			</div>
				    			<div class="col-xs-10" style="padding-left: 0px;">
				    				<div class="row">
							    		<div class="col-xs-12">							    		
						        			<div id="chartdiv6" class="col-xs-9" style="padding-left: 0px;"></div>
					        			</div>
					       			</div>
					       			<div class="row">
							    		<div class="col-xs-12">
						        			<div id="chartdiv7" class="col-xs-9" style="padding-left: 0px;"></div>
					        			</div>
					       			</div>
					       			<div class="row">
							    		<div class="col-xs-12">
						        			<div id="chartdiv8" class="col-xs-9" style="padding-left: 0px;"></div>
					        			</div>
					       			</div>
				    			</div>
				    		</div>
				    	</div>
	    			</div>    			
	      		</div>
	      		<!-- -------------------여기까지------------------- -->

				<!-- 4차트 그려주는 부분(동적으로 바인딩하여 그려줘야 함) -->
			    <div id="demo3" class="panel-collapse collapse" style="margin-top:2px; padding-left:2px;">
				    <div class="stepChartFailure">
				    	<div class="row">
				    		<div class="col-xs-12">
				    			<div class="col-xs-2">
				    				<div class="row">
				    					<div class="col-xs-12">
				    						<div style="height: 40px; line-height: 40px; color: #e53935; font-size: 15px; font-weight: bold;">
												ID : 10012BRS2
				    						</div>
			    						</div>
				    				</div>
				    				<div class="row" style="padding-top: 40px;">
			    						<div class="col-xs-12 text-center" style="height: 120px; padding: 0px;">AVT<br/>(Avg. Total Value)</div>
				    				</div>
				    				<div class="row" style="padding-top: 80px;">
				    					<div class="col-xs-12 text-center" style="height: 120px; padding: 0px;">ATC<br/>(Avg. Total Count)</div>
				    				</div>
				    				<div class="row" style="padding-top: 80px;">
				    					<div class="col-xs-12 text-center" style="height: 120px;">Severity</div>
				    				</div>
				    			</div>
				    			<div class="col-xs-10" style="padding-left: 0px;">
				    				<div class="row">
							    		<div class="col-xs-12">							    		
						        			<div id="chartdiv9" class="col-xs-9" style="padding-left: 0px;"></div>
					        			</div>
					       			</div>
					       			<div class="row">
							    		<div class="col-xs-12">
						        			<div id="chartdiv10" class="col-xs-9" style="padding-left: 0px;"></div>
					        			</div>
					       			</div>
					       			<div class="row">
							    		<div class="col-xs-12">
						        			<div id="chartdiv11" class="col-xs-9" style="padding-left: 0px;"></div>
					        			</div>
					       			</div>
				    			</div>
				    		</div>
				    	</div>
	    			</div>    			
	      		</div>
	      		<!-- -------------------여기까지------------------- -->

			    <div class="row">
			    	<div class="col-xs-12">
			      		<div class="imgBox col-xs-2">
				      		<div class="mImg">
			        			<img src="resources/img/part_02.jpg" style="width:110px; height:110px;"/>	        			
				      		</div>
			      		</div>
		      			<div class="col-xs-2 no-padding">
			        		<button type="button" class="btnChart btn-good" data-toggle="collapse" data-target="#">${bGoodCount}</button>							
			      		</div>
			      		<div class="col-xs-2 no-padding">
			        		<button type="button" class="btnChart btn-inspection" data-toggle="collapse" data-target="#">${bWarningCount}</button>							
			      		</div>
			      		<div class="col-xs-2 no-padding">
			        		<button type="button" class="btnChart btn-failure" data-toggle="collapse" data-target="#">${bFailCount}</button>							
		 		 		</div>
			      		<div class="col-xs-2 no-padding">
			        		<button type="button" class="btnChart btn-operation" data-toggle="collapse" data-target="#">${bNoOperCount}</button>
			      		</div>
		      		</div>
			    </div>
			    
			    <div class="panel" style="padding-top:15px;">
			    	<div class="row">		  
			    		<div class="col-xs-12">
			    			<div class="iTitle col-xs-4"> 	
					    		<div class="panel-heading panel-wrap text-center">
					    			<div class="surfaceTitle">Cumulated measurement count<br/>by formation</div>
				    			</div>
				    		</div> 
				    		<div class="iTitle col-xs-4">
					    		<div class="panel-heading panel-wrap text-center">
					    			<div class="surfaceTitle">Cumulative measurement count<br/>by rolling stocks</div>
				    			</div>
					    	</div>
					    	<div class="iTitle col-xs-4">
					    		<div class="panel-heading panel-wrap text-center">
					    			<div class="surfaceTitle">Monthly failure<br/>occurrence index</div>
				    			</div>
					    	</div>
				    	</div>		    	
			    	</div>  	
			    	<div class="row">		  
			    		<div class="col-xs-12">
			    			<div class="surface col-xs-4"> 	
					    		<div class="organize col-xs-12">
					    			<div class="labelSurface col-xs-6">21</div>
					    			<div class="backOgnImg col-xs-6"></div>
				    			</div>
				    		</div> 
				    		<div class="surface col-xs-4">
					    		<div class="vehicle col-xs-12">
					    			<div class="labelSurface col-xs-6">168</div>
					    			<div class="backVicImg col-xs-6"></div>
				    			</div>
					    	</div>
					    	<div class="surface col-xs-4" >
					    		<div class="defect col-xs-12">
					    			<div class="labelDefect col-xs-6">
					    				<div class="row">
					    					<div class="col-xs-12" style="height:48px; color: white; font-size:40px;">18.4</div>				    					
					    				</div>
					    				<div class="row">
					    					<div class="col-xs-12" style="height:48px; color: white; font-size:12px; padding-top:5px;">Comparison to</br>last month : +5.5</div>				    					
					    				</div>
				    				</div>
					    			<div class="backDftImg col-xs-6"></div>
				    			</div>
					    	</div>
				    	</div>		    	
			    	</div>
			    </div>
			    <div class="panel">
			    	<div class="row">		  
			    		<div class="col-xs-12">
			    			<div class="chartTitleBox col-xs-4"> 	
					    		<div class="panel-heading text-center">
					    			<div class=chartTitle>Severity per component</div>
				    			</div>
				    		</div> 
				    		<div class="chartTitleBox col-xs-4">
					    		<div class="panel-heading text-center">
					    			<div class="chartTitle">Defect probability by part</div>
				    			</div>
					    	</div>
					    	<div class="chartTitleBox col-xs-4">
					    		<div class="panel-heading text-center">
					    			<div class="chartTitle">Maintenance status</div>
				    			</div>
					    	</div>
				    	</div>		    	
			    	</div>  	
			    	<div class="row">		  
			    		<div class="col-xs-12">
			    			<div class="chartBox col-xs-4"> 	
					    		<div class="serious">
					    			<div id="chartdivPie1"></div>
				    			</div>
				    		</div> 
				    		<div class="chartBox col-xs-4">
					    		<div class="fault">
					    			<div id="chartdivPie2"></div>
				    			</div>
					    	</div>
					    	<div class="chartBox col-xs-4">
					    		<div class="maintenance">
					    			<div id="chartdivPie3"></div>
				    			</div>
					    	</div>
				    	</div>		    	
			    	</div>
			    </div>
		    </div>
	  	</div>
  	</div>
	  		  
 	<div class="col-xs-6">
		<div class="panel panel-default text-center">
			<div class="panel-heading">
				차상(On-Board Diagnostics)
			</div>
			<div class="panel-body">
		  		<div class="row">
			  		<div class="col-xs-12">
			      		<div class="tCol col-xs-2">
			      			<div class="tCondition"><h5 lang="en">Condition</h5></div>
		      			</div>
			      		<div class="tCol col-xs-2">
			      			<div class="tGood"><h5 lang="en">Good</h5></div>
		      			</div>
			      		<div class="tCol col-xs-2">
			      			<div class="tInspection"><h5 lang="en">Need Inspection</h5></div>
		      			</div>
			      		<div class="tCol col-xs-2">
			      			<div class="tFailure"><h5 lang="en">Failure Probable</h5></div>
		      			</div>
			      		<div class="tCol col-xs-2">
			      			<div class="tOperation"><h5 lang="en">No Operation</h5></div>
		      			</div>
		      		</div>
			    </div>
			    <div class="row">
			    	<div class="col-xs-12">
						<div class="imgBox col-xs-2">
				      		<div class="mImg">
			        			<img src="resources/img/part_03.jpg" style="width:110px; height:110px;"/>	        			
				      		</div>
			      		</div>
						<div class="col-xs-2 no-padding">
							<button type="button" class="btnChart btn-good" data-toggle="collapse" data-target="#">133</button>							
						</div>
						<div class="col-xs-2 no-padding">
							<button type="button" class="btnChart btn-inspection" data-toggle="collapse" data-target="#">45</button>							
						</div>
						<div class="col-xs-2 no-padding">
							<button type="button" class="btnChart btn-failure" data-toggle="collapse" data-target="#">6</button>							
						</div>
						<div class="col-xs-2 no-padding">
							<button type="button" class="btnChart btn-operation" data-toggle="collapse" data-target="#">11</button>							
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12">
						<div class="imgBox col-xs-2">
				      		<div class="mImg">
			        			<img src="resources/img/part_04.jpg" style="width:110px; height:110px;"/>	        			
				      		</div>
			      		</div>
						<div class="col-xs-2 no-padding">
							<button type="button" class="btnChart btn-good" data-toggle="collapse" data-target="#">46</button>							
						</div>
						<div class="col-xs-2 no-padding">
							<button type="button" class="btnChart btn-inspection" data-toggle="collapse" data-target="#">13</button>
						</div>
						<div class="col-xs-2 no-padding">
							<button type="button" class="btnChart btn-failure" data-toggle="collapse" data-target="#">2</button>
						</div>
						<div class="col-xs-2 no-padding">
							<button type="button" class="btnChart btn-operation" data-toggle="collapse" data-target="#">3</button>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12">
						<div class="imgBox col-xs-2">
				      		<div class="mImg">
			        			<img src="resources/img/part_05.jpg" style="width:110px; height:110px;"/>	        			
				      		</div>
			      		</div>
						<div class="col-xs-2 no-padding">
							<button type="button" class="btnChart btn-good" data-toggle="collapse" data-target="#">231</button>							
						</div>
						<div class="col-xs-2 no-padding">
							<button type="button" class="btnChart btn-inspection" data-toggle="collapse" data-target="#">58</button>
						</div>
						<div class="col-xs-2 no-padding">
							<button type="button" class="btnChart btn-failure" data-toggle="collapse" data-target="#">7</button>
						</div>
						<div class="col-xs-2 no-padding">
							<button type="button" class="btnChart btn-operation" data-toggle="collapse" data-target="#">13</button>
						</div>
					</div>
				</div>
				<div class="panel" style="padding-top:15px;">
			    	<div class="row">		  
			    		<div class="col-xs-12">
			    			<div class="iTitle col-xs-4"> 	
					    		<div class="panel-heading panel-wrap text-center">
					    			<div class=surfaceTitle>Cumulated measurement count<br/>by formation</div>
				    			</div>
				    		</div> 
				    		<div class="iTitle col-xs-4">
					    		<div class="panel-heading panel-wrap text-center">
					    			<div class="surfaceTitle">Cumulative measurement count<br/>by rolling stocks</div>
				    			</div>
					    	</div>
					    	<div class="iTitle col-xs-4">
					    		<div class="panel-heading panel-wrap text-center">
					    			<div class="surfaceTitle">Monthly failure<br/>occurrence index</div>
				    			</div>
					    	</div>
				    	</div>		    	
			    	</div>  	
			    	<div class="row">		  
			    		<div class="col-xs-12">
			    			<div class="surface col-xs-4"> 	
					    		<div class="organize col-xs-12">
					    			<div class="labelSurface col-xs-6">18</div>
					    			<div class="backOgnImg col-xs-6"></div>
				    			</div>
				    		</div> 
				    		<div class="surface col-xs-4">
					    		<div class="vehicle col-xs-12">
					    			<div class="labelSurface col-xs-6">144</div>
					    			<div class="backVicImg col-xs-6"></div>
				    			</div>
					    	</div>
					    	<div class="surface col-xs-4">
					    		<div class="defect col-xs-12">
					    			<div class="labelDefect col-xs-6">
					    				<div class="row">
					    					<div class="col-xs-12" style="height:48px; color: white; font-size:40px;">23.7</div>				    					
					    				</div>
					    				<div class="row">
					    					<div class="col-xs-12" style="height:48px; color: white; font-size:12px; padding-top:5px;">Comparison to</br>last month : -1.8</div>				    					
					    				</div>
				    				</div>
					    			<div class="backDftImg col-xs-6"></div>
				    			</div>
					    	</div>
				    	</div>		    	
			    	</div>
			    </div>	
			    <div class="panel">
			    	<div class="row">		  
			    		<div class="col-xs-12">
			    			<div class="chartTitleBox col-xs-4"> 	
					    		<div class="panel-heading text-center">
					    			<div class=chartTitle>Severity per component</div>
				    			</div>
				    		</div> 
				    		<div class="chartTitleBox col-xs-4">
					    		<div class="panel-heading text-center">
					    			<div class="chartTitle">Defect probability by part</div>
				    			</div>
					    	</div>
					    	<div class="chartTitleBox col-xs-4">
					    		<div class="panel-heading text-center">
					    			<div class="chartTitle">Maintenance status</div>
				    			</div>
					    	</div>
				    	</div>		    	
			    	</div>  	
			    	<div class="row">		  
			    		<div class="col-xs-12">
			    			<div class="chartBox col-xs-4"> 	
					    		<div class="serious">
					    			<div id="chartdivPie4"></div>
				    			</div>
				    		</div> 
				    		<div class="chartBox col-xs-4">
					    		<div class="fault">
					    			<div id="chartdivPie5"></div>
				    			</div>
					    	</div>
					    	<div class="chartBox col-xs-4">
					    		<div class="maintenance">
					    			<div id="chartdivPie6"></div>
				    			</div>
					    	</div>
				    	</div>		    	
			    	</div>
			    </div>
		    </div>	
	  	</div>
	</div>


	<script>
		function myFunction(){
			var input, filter, ul, li, a, i;
			input = document.getElementById("myInput");
			filter = input.value.toUpperCase();
			ul = document.getElementById("myUL");
			li = ul.getElementsByTagName("li");
			for(i=0; i<li.length; i++){
					a=li[i].getElementsByTagName("a")[0];
					if(a.innerHTML.toUpperCase().indexOf(filter) > -1){
						li[i].style.display="";
					}else {
						li[i].style.display ="none";
					}
				}
			}
		
		$(".panel-collapse").on('show.bs.collapse', function () {
		    $(".panel-collapse").collapse('hide');
		})
	</script> 
	
	 
	<!-- Navigation bar -->
	<%@ include file="subPage-navigationBar.jsp"%>
	
	<!-- Footer -->
	<%@ include file="subPage-footer.jsp"%>
	
	<!-- Setting Page -->
	<%@ include file="subPage-modalSettingPage.jsp"%>	
    <script src="/ml/resources/js/dropdownCheckboxes.min.js"></script>
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>

</body>
</html>