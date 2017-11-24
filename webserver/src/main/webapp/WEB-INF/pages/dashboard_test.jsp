<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>DashBoard</title>
	
	<!-- all libraries -->
	<script src="<spring:url value="/resources/js/dashboardMain.js?1.0"/>"
		language="JavaScript" type="text/javascript"></script>
	<script src="/ml/resources/js/amcharts/amcharts.js"></script>
	<script src="/ml/resources/js/amcharts/serial.js"></script>
	<script src="/ml/resources/js/amcharts/pie.js"></script>
	<script src="/ml/resources/js/amcharts/plugins/export/export.min.js"></script>
	<link rel="stylesheet" href="/ml/resources/js/amcharts/plugins/export/export.css" type="text/css" media="all" />
	<script src="/ml/resources/js/amcharts/themes/light.js"></script>
	<!-- <%@ include file="subPage-libraries.jsp"%> -->
	<!-- <%@ include file="subPage-common-css.jsp"%>  -->
	
	<script type="text/javascript">
		$(function() {
			// activate tab name
			$("ul").find("li").removeClass("active");
			$("#mainDashboard").addClass("active");
		});
	</script>
	
	<style>
		#chartdiv {
			width	: 90%;
			height	: 200px;			
		}
		#chartdiv1 {
			width	: 90%;
			height	: 200px;			
		}
		#chartdiv2 {
			width	: 90%;
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
		.stepChart {
			border:2px solid #43b348;
			border-radius:0.5em;
		}
		.idTitle {
			width: 10%;
			font-size:15px;
			line-height:50px;
		}
		div {
			font-family:"Malgun Gothic";
			font-weight:bold;
		}
		h5:lang(en) {
			font-family:"Verdana";
			font-weight:bold;
			line-height:30px;
		}
		.bg-1 {
			background-color: #1abc9c;
			color: #ffffff;
		}
		.bg-2 { 
			background-color: #474e5d; /* Dark Blue */
			color: #ffffff;
	  	}
	  	.collapse {
	  		padding-left: 2px;
	  		padding-right:2px;
	  	}
	  	.panel-default {
	  		padding:0px;
	  		min-height:875px;
	  	}
	  	.panel-body {
	  		font-size:130%;
	  		height:50px;
	  		background: linear-gradient(to bottom, #f6f8f9 0%,#e5ebee 50%,#d7dee3 51%,#f5f7f9 100%);
	  	}
	  	.row {
	  		padding:0px;
	  	}
	  	div .no-padding {
	  		padding: 2px;
	  		height: 80px;
	  		width:20%;
	  	}
	  	.btnChart {
	  		height:100%;
	  		width:100%;
	  		background-color:white;	  		
	  		font-size:150%;
	  		font-weight:bold;
	  		border-radius:0.5em;
	  	}
	  	.tCol {
	  		width: 20%;
	  		padding-left: 2px;
	  		padding-right: 2px;
	  	}
	  	.tCondition {		  		  		
	  		 background-color: #93b1c3; 
	  		 text-align:center; 
	  		 border-radius:0.5em;
	  		 height: 30px;
	  	}
	  	.tGood {
	  		 background-color: #43b348; 
	  		 text-align:center; 
	  		 border-radius:0.5em;
	  		 height: 30px;
	  	}
	  	.tInspection {
	  		 background-color: #ff8f00; 
	  		 text-align:center; 
	  		 border-radius:0.5em;
	  		 height: 30px;
	  	}
	  	.tFailure {
	  		 background-color: #e53935; 
	  		 text-align:center; 
	  		 border-radius:0.5em;
	  		 height: 30px;
	  	}
	  	.tOperation {
	  		 background-color: #acacac; 
	  		 text-align:center; 
	  		 border-radius:0.5em;
	  		 height: 30px;
	  	}
	  	.imgBox {
	  		width: 20%;
	  		padding:2px;
	  	}
	  	.mImg {
	  		height:77px;
	  		 background-color:white;
	  		 border:2px solid #93b1c3; 
	  		 border-radius:0.5em;
	  	}
	  	.btn-success {	  		 
	  		 border:2px solid #43b348;
	  		 color: #43b348;
	  	}
	  	.btn-warning {
	  		 border:2px solid #ff8f00;
	  		 color: #ff8f00;
	  	}
	  	.btn-danger {
	  		 border:2px solid #e53935;
	  		 color: #e53935;
	  	}
	  	.btn-basic {
	  		 border:2px solid #acacac;
	  		 color: #acacac;
	  	}	  	
	  	.surface {
	  		width: 33.333%;
	  		height : 100px;
	  		padding:2px;
	  	}
	  	.iTitle {
	  		height: 50px;
	  		width: 33.333%;
	  		padding-left:2px;
	  		padding-right:2px;
	  		font-family: "맑은 고딕";
	  	}
	  	.surfaceTitle {
	  		
	  	}
	  	.organize {
	  		height: 100%;
	  		width: 100%;	  		
	  		text-align: left;
	  		padding-left: 40px;
	  		background-image: url('resources/img/chart_ico_01');
	  		background-color: #00b8f6;
	  	}
	  	.vehicle {
	  		height: 100%;
	  		width: 100%;
	  		text-align: left;
	  		padding-left: 40px;
	  		background-color: #2c6099;
	  	}
	  	.defect {
	  		height: 100%;
	  		width: 100%;
	  		text-align: left;
	  		padding-left: 40px;
	  		background-color: #00acac;
	  	}
	  	.labelSurface {
	  		line-height: 100px;
	  		font-size:50px;
	  		color: white;
	  	}
	  	.chartTitleBox {
	  		height: 50px;
	  		width: 33.333%;
	  		padding-left:2px;
	  		padding-right:2px;
	  		font-family: "맑은 고딕";
	  	}
	  	.chartBox {
	  		width: 33.333%;
	  		height : 270px;
	  		padding:2px;
	  	}
	  	.serious {
	  		height: 100%;
	  		width: 100%;
	  		border:2px solid gainsboro;
			border-radius:0.5em;
	  	}
	  	.fault {
	  		height: 100%;
	  		width: 100%;
	  		border:2px solid gainsboro;
			border-radius:0.5em;
	  	}
	  	.maintenance {
	  		height: 100%;
	  		width: 100%;
	  		border:2px solid gainsboro;
			border-radius:0.5em;
	  	}
	</style>	
		
	<script>
	var chart = AmCharts.makeChart("chartdiv", {
	    "type": "serial",
	    "theme": "none",
	    "titles":[{"text":"AVT"}],
	    "autoMarginOffset":25,
	    "dataProvider": [{
	        "date": "2017/10/01",
	        "value": 502.2
	    }, {
	        "date": "2017/10/02",
	        "value": 523.6
	    }, {
	        "date": "2017/10/03",
	        "value": 476.6
	    }, {
	        "date": "2017/10/04",
	        "value": 486.5
	    }, {
	        "date": "2017/10/05",
	        "value": 547.2
	    }, {
	        "date": "2017/10/06",
	        "value": 551.2
	    }, {
	        "date": "2017/10/07",
	        "value": 511.5
	    }, {
	        "date": "2017/10/08",
	        "value": 513.2
	    }, {
	        "date": "2017/10/09",
	        "value": 533.3
	    }, {
	        "date": "2017/10/10",
	        "value": 525.6
	    }, {
	        "date": "2017/10/11",
	        "value": 497.1
	    }, {
	        "date": "2017/10/12",
	        "value": 494.4
	    }, {
	        "date": "2017/10/13",
	        "value": 547.4
	    }, {
	        "date": "2017/10/14",
	        "value": 503.4
	    }, {
	        "date": "2017/10/15",
	        "value": 523.6
	    }],
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
	    "titles":[{"text":"ATC"}],
	    "autoMarginOffset":25,
	    "dataProvider": [{
	        "date": "2017/10/01",
	        "value": 188
	    }, {
	        "date": "2017/10/02",
	        "value": 181
	    }, {
	        "date": "2017/10/03",
	        "value": 180
	    }, {
	        "date": "2017/10/04",
	        "value": 173
	    }, {
	        "date": "2017/10/05",
	        "value": 163
	    }, {
	        "date": "2017/10/06",
	        "value": 190
	    }, {
	        "date": "2017/10/07",
	        "value": 155
	    }, {
	        "date": "2017/10/08",
	        "value": 182
	    }, {
	        "date": "2017/10/09",
	        "value": 179
	    }, {
	        "date": "2017/10/10",
	        "value": 174
	    }, {
	        "date": "2017/10/11",
	        "value": 211
	    }, {
	        "date": "2017/10/12",
	        "value": 202
	    }, {
	        "date": "2017/10/13",
	        "value": 188
	    }, {
	        "date": "2017/10/14",
	        "value": 172
	    }, {
	        "date": "2017/10/15",
	        "value": 191
	    }],
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
	    "titles":[{"text":"Severity"}],
	    "autoMarginOffset":25,
	    "dataProvider": [{
	        "date": "2017/10/01",
	        "value": 25
	    }, {
	        "date": "2017/10/02",
	        "value": 23
	    }, {
	        "date": "2017/10/03",
	        "value": 14
	    }, {
	        "date": "2017/10/04",
	        "value": 27
	    }, {
	        "date": "2017/10/05",
	        "value": 31
	    }, {
	        "date": "2017/10/06",
	        "value": 20
	    }, {
	        "date": "2017/10/07",
	        "value": 19
	    }, {
	        "date": "2017/10/08",
	        "value": 24
	    }, {
	        "date": "2017/10/09",
	        "value": 11
	    }, {
	        "date": "2017/10/10",
	        "value": 14
	    }, {
	        "date": "2017/10/11",
	        "value": 26
	    }, {
	        "date": "2017/10/12",
	        "value": 16
	    }, {
	        "date": "2017/10/13",
	        "value": 10
	    }, {
	        "date": "2017/10/14",
	        "value": 27
	    }, {
	        "date": "2017/10/15",
	        "value": 21
	    }],
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
		  "startDuration": 2,
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
		  "startDuration": 2,
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
		  "startDuration": 2,
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
		    "country": "Motorblock",
		    "visits": 64
		  }, {
		    "country": "Gear",
		    "visits": 43
		  }, {
		    "country": "Tripod",
		    "visits": 15
		  } ],
		  "valueField": "visits",
		  "titleField": "country",
		  "startEffect": "elastic",
		  "startDuration": 2,
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
		    "country": "Motorblock",
		    "visits": 37
		  }, {
		    "country": "Gear",
		    "visits": 46
		  }, {
		    "country": "Tripod",
		    "visits": 24
		  } ],
		  "valueField": "visits",
		  "titleField": "country",
		  "startEffect": "elastic",
		  "startDuration": 2,
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
		    "country": "Motorblock",
		    "visits": 25
		  }, {
		    "country": "Gear",
		    "visits": 43
		  }, {
		    "country": "Tripod",
		    "visits": 67
		  } ],
		  "valueField": "visits",
		  "titleField": "country",
		  "startEffect": "elastic",
		  "startDuration": 2,
		  "labelRadius": -10,
		  "innerRadius": "30%",
		  "depth3D": 0,
		  "balloonText": "[[title]]<br><span style='font-size:15px'><b>[[value]]</b> ([[percents]]%)</span>",
		  "angle": 0,
		  "export": {
		    "enabled": false
		  }
		} );
	
	$(document).ready(function(){
		$(".panel-collapse").on('show.bs.collapse', function () {
		    $(".panel-collapse").collapse('hide');
		});
	});
	</script>
</head>

<body>
<div class="container-fluid" style="padding-top: 2%;">
	<div class="col-xs-6">
		<div class="panel panel-default text-center">
			<div class="panel panel-body">
				지상(Way-Side Diagnostics)
			</div>
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
		        			<img src="resources/img/part01.jpg" style="width:70px; height:100%;"/>	        			
			      		</div>
		      		</div>
	      			<div class="col-xs-2 no-padding">
		        		<button type="button" class="btnChart btn-success" data-toggle="collapse" data-target="#demo">${w10}</button>							
		      		</div>
		      		<div class="col-xs-2 no-padding">
		        		<button type="button" class="btnChart btn-warning" data-toggle="collapse" data-target="#demo1">${w20}</button>							
		      		</div>
		      		<div class="col-xs-2 no-padding">
		        		<button type="button" class="btnChart btn-danger" data-toggle="collapse" data-target="#demo2">${w30}</button>							
	 		 		</div>
		      		<div class="col-xs-2 no-padding">
		        		<button type="button" class="btnChart btn-basic" data-toggle="collapse" data-target="#">${w40}</button>
		      		</div>
	      		</div>
		    </div>
		    
		    <div id="demo" class="panel-collapse collapse" style="padding-left:2px;">
			    <div class="stepChart">		    	
			    	<div class="row">
			    		<div class="col-xs-12">
				    		<div class="idTitle col-xs-2">ID:201345</div>
		        			<div id="chartdiv" class="col-xs-9"></div>
	        			</div>
	       			</div>
	       			<div class="row">
			    		<div class="col-xs-12">
				    		<div class="idTitle col-xs-2"></div>
		        			<div id="chartdiv1" class="col-xs-9"></div>
	        			</div>
	       			</div>
	       			<div class="row">
			    		<div class="col-xs-12">
				    		<div class="idTitle col-xs-2"></div>
		        			<div id="chartdiv2" class="col-xs-9"></div>
	        			</div>
	       			</div>   
    			</div>    			
      		</div>
      		<div id="demo1" class="panel-collapse collapse">        		
      		</div>					  
  			<div id="demo2" class="panel-collapse collapse">        		
      		</div>
			
		    <div class="row">
		    	<div class="col-xs-12">
		      		<div class="imgBox col-xs-2">
			      		<div class="mImg">
		        			<img src="resources/img/part02.jpg" style="width:80px; height:100%;"/>	        			
			      		</div>
		      		</div>
	      			<div class="col-xs-2 no-padding">
		        		<button type="button" class="btnChart btn-success" data-toggle="collapse" data-target="#">${b10}</button>							
		      		</div>
		      		<div class="col-xs-2 no-padding">
		        		<button type="button" class="btnChart btn-warning" data-toggle="collapse" data-target="#">${b20}</button>							
		      		</div>
		      		<div class="col-xs-2 no-padding">
		        		<button type="button" class="btnChart btn-danger" data-toggle="collapse" data-target="#">${b30}</button>							
	 		 		</div>
		      		<div class="col-xs-2 no-padding">
		        		<button type="button" class="btnChart btn-basic" data-toggle="collapse" data-target="#">${b40}</button>
		      		</div>
	      		</div>
		    </div>
		    
		    <div class="panel" style="padding-top:30px;">
		    	<div class="row">		  
		    		<div class="col-xs-12">
		    			<div class="iTitle col-xs-4"> 	
				    		<div class="panel-body text-center">
				    			<div class=surfaceTitle>편성별 총 누적 측정수</div>
			    			</div>
			    		</div> 
			    		<div class="iTitle col-xs-4">
				    		<div class="panel-body text-center">
				    			<div class="surfaceTitle">차량별 총 누적 측정수</div>
			    			</div>
				    	</div>
				    	<div class="iTitle col-xs-4">
				    		<div class="panel-body text-center">
				    			<div class="surfaceTitle">월간 고장 발생 지수</div>
			    			</div>
				    	</div>
			    	</div>		    	
		    	</div>  	
		    	<div class="row">		  
		    		<div class="col-xs-12">
		    			<div class="surface col-xs-4"> 	
				    		<div class="organize">
				    			<div class="labelSurface">123</div>
			    			</div>
			    		</div> 
			    		<div class="surface col-xs-4">
				    		<div class="vehicle">
				    			<div class="labelSurface">102</div>
			    			</div>
				    	</div>
				    	<div class="surface col-xs-4">
				    		<div class="defect">
				    			<div class="labelSurface">25.3</div>
			    			</div>
				    	</div>
			    	</div>		    	
		    	</div>
		    </div>
		    <div class="panel" style="padding-top:30px;">
		    	<div class="row">		  
		    		<div class="col-xs-12">
		    			<div class="chartTitleBox col-xs-4"> 	
				    		<div class="panel-body text-center">
				    			<div class=chartTitle>부품별 심각도</div>
			    			</div>
			    		</div> 
			    		<div class="chartTitleBox col-xs-4">
				    		<div class="panel-body text-center">
				    			<div class="chartTitle">부품별 결함 확률</div>
			    			</div>
				    	</div>
				    	<div class="chartTitleBox col-xs-4">
				    		<div class="panel-body text-center">
				    			<div class="chartTitle">유지보수 현황</div>
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
	  		  
 	<div class="col-xs-6">
		<div class="panel panel-default text-center">
			<div class="panel panel-body">
				차상(On-Board Diagnostics)
			</div>
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
		        			<img src="resources/img/part03.jpg" style="width:80px; height:100%;"/>	        			
			      		</div>
		      		</div>
					<div class="col-xs-2 no-padding">
						<button type="button" class="btnChart btn-success" data-toggle="collapse" data-target="#">0</button>							
					</div>
					<div class="col-xs-2 no-padding">
						<button type="button" class="btnChart btn-warning" data-toggle="collapse" data-target="#">0</button>							
					</div>
					<div class="col-xs-2 no-padding">
						<button type="button" class="btnChart btn-danger" data-toggle="collapse" data-target="#">0</button>							
					</div>
					<div class="col-xs-2 no-padding">
						<button type="button" class="btnChart btn-basic" data-toggle="collapse" data-target="#">0</button>							
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-12">
					<div class="imgBox col-xs-2">
			      		<div class="mImg">
		        			<img src="resources/img/part04.jpg" style="width:80px; height:100%;"/>	        			
			      		</div>
		      		</div>
					<div class="col-xs-2 no-padding">
						<button type="button" class="btnChart btn-success" data-toggle="collapse" data-target="#">0</button>							
					</div>
					<div class="col-xs-2 no-padding">
						<button type="button" class="btnChart btn-warning" data-toggle="collapse" data-target="#">0</button>
					</div>
					<div class="col-xs-2 no-padding">
						<button type="button" class="btnChart btn-danger" data-toggle="collapse" data-target="#">0</button>
					</div>
					<div class="col-xs-2 no-padding">
						<button type="button" class="btnChart btn-basic" data-toggle="collapse" data-target="#">0</button>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-12">
					<div class="imgBox col-xs-2">
			      		<div class="mImg">
		        			<img src="resources/img/part05.jpg" style="width:80px; height:100%;"/>	        			
			      		</div>
		      		</div>
					<div class="col-xs-2 no-padding">
						<button type="button" class="btnChart btn-success" data-toggle="collapse" data-target="#">0</button>							
					</div>
					<div class="col-xs-2 no-padding">
						<button type="button" class="btnChart btn-warning" data-toggle="collapse" data-target="#">0</button>
					</div>
					<div class="col-xs-2 no-padding">
						<button type="button" class="btnChart btn-danger" data-toggle="collapse" data-target="#">0</button>
					</div>
					<div class="col-xs-2 no-padding">
						<button type="button" class="btnChart btn-basic" data-toggle="collapse" data-target="#">0</button>
					</div>
				</div>
			</div>
			<div class="panel" style="padding-top:20px;">
		    	<div class="row">		  
		    		<div class="col-xs-12">
		    			<div class="iTitle col-xs-4"> 	
				    		<div class="panel-body text-center">
				    			<div class=surfaceTitle>편성별 총 누적 측정수</div>
			    			</div>
			    		</div> 
			    		<div class="iTitle col-xs-4">
				    		<div class="panel-body text-center">
				    			<div class="surfaceTitle">차량별 총 누적 측정수</div>
			    			</div>
				    	</div>
				    	<div class="iTitle col-xs-4">
				    		<div class="panel-body text-center">
				    			<div class="surfaceTitle">월간 고장 발생 지수</div>
			    			</div>
				    	</div>
			    	</div>		    	
		    	</div>  	
		    	<div class="row">		  
		    		<div class="col-xs-12">
		    			<div class="surface col-xs-4"> 	
				    		<div class="organize text-center">
				    			<div class="labelSurface">157</div>
			    			</div>
			    		</div> 
			    		<div class="surface col-xs-4">
				    		<div class="vehicle text-center">
				    			<div class="labelSurface">189</div>
			    			</div>
				    	</div>
				    	<div class="surface col-xs-4">
				    		<div class="defect text-center">
				    			<div class="labelSurface">17.3</div>
			    			</div>
				    	</div>
			    	</div>		    	
		    	</div>
		    </div>	
		    <div class="panel" style="padding-top:5px;">
		    	<div class="row">		  
		    		<div class="col-xs-12">
		    			<div class="chartTitleBox col-xs-4"> 	
				    		<div class="panel-body text-center">
				    			<div class=chartTitle>부품별 심각도</div>
			    			</div>
			    		</div> 
			    		<div class="chartTitleBox col-xs-4">
				    		<div class="panel-body text-center">
				    			<div class="chartTitle">부품별 결함 확률</div>
			    			</div>
				    	</div>
				    	<div class="chartTitleBox col-xs-4">
				    		<div class="panel-body text-center">
				    			<div class="chartTitle">유지보수 현황</div>
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
	
	<!-- Navigation bar -->
	<%@ include file="subPage-navigationBar.jsp"%>
	
	<!-- Footer -->
	<%@ include file="subPage-footer.jsp"%>
	
	<!-- Setting Page -->
	<%@ include file="subPage-modalSettingPage.jsp"%>	

</body>
</html>