<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>SRM</title>
<!-- all libraries -->
<%@ include file="subPage-libraries.jsp"%>

</head>
   <script src="/ml/resources/js/amcharts/amcharts.js"></script>
   <script src="/ml/resources/js/amcharts/serial.js"></script>
   <script src="/ml/resources/js/amcharts/plugins/export/export.min.js"></script>   
   <link rel="stylesheet" href="/ml/resources/js/amcharts/plugins/export/export.css" type="text/css" media="all" />
   <script src="/ml/resources/js/amcharts/themes/light.js"></script>
   <script src="/ml/resources/js/amcharts/gauge.js"></script>
<style>
    #chartcpuhost {
      width: 100%;
      height: 100%;
   }
   #chartcpuutil {
 	 width: 100%;
 	 height: 150%;
	}
   #chartramutil {
  	  height: 50%;
	}
   #chartswaputil {
  	  height: 50%;
	}
	
   #chartdiskutil {
	  height: 100%;
	  width: 110%;
    }
    
    #chartcputrend {
	  width	: 100%;
	  height: 100%;
	}		
	
	#chartnetworktrend {
	  width	: 100%;
	  height: 100%;
	}	
	
   .content-host {
      height: 100px;
      font-size: 100px;
      font-weight: bold;
      text-align: center;
      color:#11357E;
      font-style: italic;
      line-height: 100px;
   }
   .content-util {
      height: 200px;
      font-size: 20px;
      font-weight: bold;
      text-align: center;
   }
   .content-chart {
      height: 250px;
   }

</style>

<script type="text/javascript">
var cpuhosts;
var memhosts;

var cpuutilization ="${CpuUtilization}";
var cpudata = cpuutilization.slice(0, -1);

var usedswapp = Math.round(("${SwapUsed}"/"${SwapTotal}")*100);
var usedramp = Math.round(("${MemoryUsed}"/"${MemoryTotal}")*100);
var usedC = "${DiskUsageC}";
var usedD = "${DiskUsageD}";

var usedram, totalram, usedswap, totalswap, cpudata;

var time = "${Time}";
var firstname = "${FirstName}";
var secondname = "${SecondName}";
var thirdname = "${ThirdName}";
var fourthname = "${FourthName}";
var fifthname = "${FifthName}";

var cputrendmax = ${FirstData}+500;
var firstdata = "${FirstData}";
var seconddata = "${SecondData}";
var thirddata = "${ThirdData}";
var fourthdata = "${FourthData}";
var fifthdata = "${FifthData}";

var networktrendmax = ${OutBound}+50;
var inbound = "${InBound}";
var outbound = "${OutBound}";

	setInterval( function() {
		  $.ajax({
		       url : '/ml/hosts'
		       , type : 'POST'	        
		       , dataType : 'json'
		       , contentType : "application/json; charset=UTF-8"
		       , success: function (result) {
		    	   cpuhosts = result.CPU_HOSTS;
		    	   memhosts = result.MEM_HOSTS;
		    	   
		    	   $("#cpuhosts").html(cpuhosts);
		    	   $("#memhosts").html(memhosts);
		    	   
		       }
		  	});
		  }, 1000 );

	setInterval( function() {
		if(chartnetworktrend.dataProvider.length > 10) {
			chartnetworktrend.dataProvider.shift();
		}
		  $.ajax({
		       url : '/ml/chartdata'
		       , type : 'POST'	        
		       , dataType : 'json'
		       , contentType : "application/json; charset=UTF-8"
		       , success: function (result) {
		    	    cpudata = result.CPU_USE.slice(0,-1);
		    	    cpuutilization = result.CPU_USE;
		    		usedram = result.RAM_USE;
		    		usedswap = result.SWAP_USE;
		    		totalram = result.RAM_TOTAL;
		    		totalswap = result.SWAP_TOTAL;
		    		usedC = result.C_USE;
		    		usedD = result.D_USE;
		    		inbound = result.IN_BOUND;
		    		outbound = result.OUT_BOUND;
		    		
		    		usedramp = Math.round((usedram/totalram)*100);
					var ramdata = 
					{category: "\u00A0 RAM", full: 100, bullet: usedramp};
					chartramutil.dataProvider = [];
					chartramutil.dataProvider.push(ramdata);
					
					usedswapp = Math.round((usedswap/totalswap)*100);
					var swapdata = 
					{category: "SWAP", full: 100, bullet: usedswapp};
					chartswaputil.dataProvider = [];
					chartswaputil.dataProvider.push(swapdata);
					
					var diskdata = 
					{Disk: "", C: usedC, D: usedD};
					chartdiskutil.dataProvider = [];
					chartdiskutil.dataProvider.push(diskdata);
					
					chartcpuutil.axes[0].bands[1].endValue = cpudata;
					chartcpuutil.arrows[0].value = cpudata;
					chartcpuutil.axes[0].topText = cpuutilization;
					
					time = result.TIME;
			    	var networktrenddata = {"time": time, "InBound": inbound, "OutBound": outbound};
			    	chartnetworktrend.dataProvider.push(networktrenddata);
			    	
			    	var Max = new Array();
			    	for(var i = 1; i < chartnetworktrend.dataProvider.length; i++) {
			    		Max.push(parseInt(chartnetworktrend.dataProvider[i].OutBound)+50);
			    		networktrendmax = Math.max.apply(null, Max);
			    		chartnetworktrend.valueAxes[0].maximum = networktrendmax;
			    	}
		       }
		      , error:function(request,status,error){ 
		           console.log("error");
		       }
		   });
		  chartramutil.validateData();
		  chartswaputil.validateData();
		  chartdiskutil.validateData();
		  chartcpuutil.validateNow();
		  chartnetworktrend.validateData();
		}, 1000 );

	
	setInterval( function() {
		if(chartcputrend.dataProvider.length > 10) {
	  		chartcputrend.dataProvider.shift();
		}
		  $.ajax({
		       url : '/ml/cputrend'
		       , type : 'POST'	         
		       , dataType : 'json'
		       , contentType : "application/json; charset=UTF-8"
		       , success: function (result) {
		    	   firstname = result.First_Name;
		    	   firstdata = result.First_Data;
		    	  
		    	   secondname = result.Second_Name;
		    	   seconddata = result.Second_Data;
		    	   
		    	   thirdname = result.Third_Name;
		    	   thirddata = result.Third_Data;
		    	   
		    	   fourthname = result.Fourth_Name;
		    	   fourthdata = result.Fourth_Data;
		    	   
		    	   fifthname = result.Fifth_Name;
		    	   fifthdata = result.Fifth_Data;
		    	   
		    	   time = result.TIME;
		    	   
		    	   var cputrenddata = { "time": time,  "first": firstdata, "second": seconddata, "third": thirddata
		    	   					      , "fourth": fourthdata, "fifth": fifthdata};
		    	   
		    	   chartcputrend.dataProvider.push(cputrenddata);
		    	   
		    	   chartcputrend.graphs[0].title = firstname;
		    	   chartcputrend.graphs[1].title = secondname;
		    	   chartcputrend.graphs[2].title = thirdname;
		    	   chartcputrend.graphs[3].title = fourthname;
		    	   chartcputrend.graphs[4].title = fifthname;
		    	   /* 
		    	   var cputrendmax = parseInt(firstdata)+500;
			    	chartcputrend.valueAxes[0].maximum = cputrendmax; */
		    	   
			    	var Max = new Array();
			    	for(var i = 1; i < chartcputrend.dataProvider.length; i++) {
			    		Max.push(parseInt(chartcputrend.dataProvider[i].first)+500);
			    		cputrendmax = Math.max.apply(null, Max);
			    		chartcputrend.valueAxes[0].maximum = cputrendmax;
			    	}
		       }
		      , error:function(request,status,error){ 
		           console.log("error");
		       }
		   });
		  chartcputrend.validateData();
		}, 1000 );
	
var chartcpuutil = AmCharts.makeChart("chartcpuutil", {
	  "theme": "light",
	  "type": "gauge",
	  "autoResize": false,
	  "axes": [{
	    "topTextFontSize": 30,
	    "topTextYOffset": 50,
	    "topText" : cpuutilization,
	    "labelsEnabled" : false,
	    "axisColor": "#11357E",
	    "axisThickness": 1,
	    "endValue": 100,
	    "gridInside": true,
	    "inside": true,
	    "radius": "60%",
	    "valueInterval": 50,
	    "tickColor": "#11357E",
	    "startAngle": -90,
	    "endAngle": 90,
	    "unit": "%",
	    "bandOutlineAlpha": 0,
	    "bands": [{
	      "color": "#FFFFFF",
	      "endValue": 100,
	      "innerRadius": "100%",
	      "radius": "150%",
	      "gradientRatio": [0.1, 0, -0.1],
	      "startValue": 0
	    }, {
	      "color": "#00136F",
	      "endValue": cpudata,
	      "innerRadius": "100%",
	      "radius": "150%",
	      "gradientRatio": [0.1, 0, -0.1],
	      "startValue": 0
	    }]
	  }],
	  "arrows": [{
	    "alpha": 1,
	    "innerRadius": "35%",
	    "nailRadius": 0,
	    "radius": "100%",
	    "value" : cpudata
	  }]
	});
	
var chartramutil = AmCharts.makeChart( "chartramutil", {
	  "type": "serial",
	  "rotate": true,
	  "theme": "light",
	  "dataProvider": [ {
	    "category": "\u00A0 RAM",
	    "full": 100,
	    "bullet": usedramp
	  } ],
	  "valueAxes": [ {
	    "maximum": 100,
	    "stackType": "regular",
	    "gridAlpha": 0
	  } ],
	  "startDuration": 1,
	  "graphs": [ {
	    "valueField": "full",
	    "showBalloon": false,
	    "type": "column",
	    "lineAlpha": 0,
	    "fillAlphas": 0.8,
	    "fillColors": [ "#217328", "#f6d32b", "#fb2316" ],
	    "gradientOrientation": "horizontal",
	  }, {
	    "clustered": false,
	    "balloonText": "[[value]]%",
	    "columnWidth": 0.3,
	    "fillAlphas": 1,
	    "lineColor": "#000000",
	    "stackable": false,
	    "type": "column",
	    "valueField": "bullet"
	  } ],
	  "columnWidth": 1,
	  "categoryField": "category",
	  "categoryAxis": {
	    "gridAlpha": 0,
	    "position": "left"
	  }
	});
	
var chartswaputil = AmCharts.makeChart( "chartswaputil", {
	  "type": "serial",
	  "rotate": true,
	  "theme": "light",
	  "dataProvider": [ {
	    "category": "SWAP",
	    "full": 100,
	    "bullet": usedswapp
	  } ],
	  "valueAxes": [ {
	    "maximum": 100,
	    "stackType": "regular",
	    "gridAlpha": 0
	  } ],
	  "startDuration": 1,
	  "graphs": [ {
	    "valueField": "full",
	    "showBalloon": false,
	    "type": "column",
	    "lineAlpha": 0,
	    "fillAlphas": 0.8,
	    "fillColors": [ "#217328", "#f6d32b", "#fb2316" ],
	    "gradientOrientation": "horizontal",
	  }, {
	    "clustered": false,
	    "balloonText": "[[value]]%",
	    "columnWidth": 0.3,
	    "fillAlphas": 1,
	    "lineColor": "#000000",
	    "stackable": false,
	    "type": "column",
	    "valueField": "bullet"
	  }, {
	    "columnWidth": 0.5,
	    "lineColor": "#000000",
	    "lineThickness": 3,
	    "noStepRisers": true,
	    "stackable": false,
	    "type": "step",
	    "valueField": "limit"
	  } ],
	  "columnWidth": 1,
	  "categoryField": "category",
	  "categoryAxis": {
	    "gridAlpha": 0,
	    "position": "left"
	  }
	});

var chartdiskutil = AmCharts.makeChart("chartdiskutil", {
    "type": "serial",
	"theme": "light",
	"legend": {
        "horizontalGap": 10,
        "maxColumns": 1,
        "position": "right",
		"useGraphSettings": true,
		"markerSize": 10
    },
    "dataProvider": [{
        "Disk": "",
        "C": usedC,
        "D": usedD
    }],
    "valueAxes": [{
        "stackType": "regular",
        "axisAlpha": 0.3,
        "gridAlpha": 0
    }],
    "graphs": [{
        "balloonText": "<b>[[title]]</b><br><span style='font-size:14px'>[[category]]: <b>[[value]]GB</b></span>",
        "fillColors":"#FDB01C",
        "fillAlphas": 0.8,
        "labelText": "[[value]]GB",
        "lineAlpha": 0.3,
        "title": "C",
        "type": "column",
		"color": "#000000",
        "valueField": "C"
    }, {
        "balloonText": "<b>[[title]]</b><br><span style='font-size:14px'>[[category]]: <b>[[value]]GB</b></span>",
        "fillColors":"#11357E",       
        "fillAlphas": 0.8,
        "labelText": "[[value]]GB",
        "lineAlpha": 0.3,
        "title": "D",
        "type": "column",
		"color": "#000000",
        "valueField": "D"
    }],
    "categoryField": "Disk",
    "categoryAxis": {
        "gridPosition": "start",
        "axisAlpha": 0,
        "gridAlpha": 0,
        "position": "left"
    }
});
	
var chartcputrend = AmCharts.makeChart("chartcputrend", {
    "type": "serial",
    "theme": "light",
    "legend": {
        "useGraphSettings": true
    },
    "dataProvider": [{
        "time": time,
        "first": firstdata,
        "second": seconddata,        
        "third": thirddata,
        "fourth": fourthdata,
        "fifth": fifthdata
    }],
    "valueAxes": [{
        "integersOnly": true,
        "maximum": cputrendmax,
        "minimum": 0,
        "reversed": false,
        "axisAlpha": 0,
        "dashLength": 5,
        "gridCount": 10,
        "position": "left"
    }],
    "startDuration": 0.5,
    "graphs": [{
        "balloonText": "[[category]] - [[value]]",
        "lineColor": "#A61F12",
        "bullet": "round",
        "hidden": false,
        "title": firstname,
        "valueField": "first",
		"fillAlphas": 0
    },{
        "balloonText": "[[category]] - [[value]]",
        "lineColor": "#11357E",
        "bullet": "round",
        "hidden": false,
        "title": secondname,
        "valueField": "second",
		"fillAlphas": 0
    },{
        "balloonText": "[[category]] - [[value]]",
        "lineColor": "#FDB01C",
        "bullet": "round",
        "hidden": false,
        "title": thirdname,
        "valueField": "third",
		"fillAlphas": 0
    },{
        "balloonText": "[[category]] - [[value]]",
        "lineColor": "#593A1E",
        "bullet": "round",
        "hidden": false,
        "title": fourthname,
        "valueField": "fourth",
		"fillAlphas": 0
    },{
        "balloonText": "[[category]] - [[value]]",
        "lineColor": "#217328",
        "bullet": "round",
        "hidden": false,
        "title": fifthname,
        "valueField": "fifth",
		"fillAlphas": 0
    }],
    "chartCursor": {
        "cursorAlpha": 0,
        "zoomable": false
    },
    "categoryField": "time",
    "categoryAxis": {
        "gridPosition": "start",
        "axisAlpha": 0,
        "fillAlpha": 0.05,
        "fillColor": "#000000",
        "gridAlpha": 0,
        "position": "top"
    }
});	

var chartnetworktrend = AmCharts.makeChart("chartnetworktrend", {
    "type": "serial",
    "theme": "light",
    "legend": {
        "useGraphSettings": true
    },
    "dataProvider": [{
        "time": time,
        "InBound": inbound,
        "OutBound": outbound
    }],
    "valueAxes": [{
        "integersOnly": true,
        "maximum": networktrendmax,
        "minimum": 0,
        "reversed": false,
        "axisAlpha": 0,
        "dashLength": 5,
        "gridCount": 10,
        "position": "left"
    }],
    "startDuration": 0.5,
    "graphs": [{
        "balloonText": "[[category]] - [[value]]",
        "lineColor": "#A61F12",
        "bullet": "round",
        "hidden": false,
        "title": "InBound",
        "valueField": "InBound",
		"fillAlphas": 0
    },{
        "balloonText": "[[category]] - [[value]]",
        "lineColor": "#11357E",
        "bullet": "round",
        "hidden": false,
        "title": "OutBound",
        "valueField": "OutBound",
		"fillAlphas": 0
    }],
    "chartCursor": {
        "cursorAlpha": 0,
        "zoomable": false
    },
    "categoryField": "time",
    "categoryAxis": {
        "gridPosition": "start",
        "axisAlpha": 0,
        "fillAlpha": 0.05,
        "fillColor": "#000000",
        "gridAlpha": 0,
        "position": "top"
    }
});	
</script>
<body>
   <div class="container-fluid" style="padding-top: 3%; padding-bottom: 2%;">
      <div class="row">
         <div class="col-xs-6">
            <div class="panel panel-info">
               <div class="panel-heading text-center">Number of CPU hosts</div>
               <div class="panel-body">
                  <div class="content-host col-xs-12">
                  	<div id="cpuhosts">${CpuHosts}</div>
                  </div>
               </div>
            </div>
         </div>
         <div class="col-xs-6">
            <div class="panel panel-info">
               <div class="panel-heading text-center">Number of RAM hosts</div>
               <div class="panel-body">
                  <div class="content-host col-xs-12">
                  	<div id="memhosts">${MemoryHosts}</div>
                  </div>
               </div>
            </div>
         </div>
      </div>
      <div class="row">
         <div class="col-xs-4">
            <div class="panel panel-success">
               <div class="panel-heading text-center">CPU utilization</div>
               <div class="panel-body">
                  <div class="content-util col-xs-12">
                  	<div id="chartcpuutil"></div>
                  </div>
               </div>
            </div>
         </div>
         <div class="col-xs-4">
            <div class="panel panel-success">
               <div class="panel-heading text-center">Utilization of RAM/SWAP</div>
               <div class="panel-body">
                  <div class="content-util col-xs-12">
                  <div id="chartramutil"></div>
                  <div id="chartswaputil"></div>
                  </div>
               </div>
            </div>
         </div>
         <div class="col-xs-4">
            <div class="panel panel-success">
               <div class="panel-heading text-center">DISK utilization</div>
               <div class="panel-body">
                  <div class="content-util col-xs-12">
                  	<div id="chartdiskutil"></div>
                  </div>
               </div>
            </div>
         </div>
      </div>
      <div class="row">
         <div class="col-xs-6">
            <div class="panel panel-danger">
               <div class="panel-heading text-center">Utilization trend of CPU</div>
               <div class="panel-body" style="padding:0px;">
                  <div class="content-chart col-xs-12">
                     <div id="chartcputrend"></div>
                  </div>
               </div>
            </div>
         </div>
         <div class="col-xs-6">
            <div class="panel panel-danger">
               <div class="panel-heading text-center">Trend of Network I/O</div>
               <div class="panel-body" style="padding:0px;">
                  <div class="content-chart col-xs-12">
                  	<div id="chartnetworktrend"></div>
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