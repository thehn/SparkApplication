<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="row">
	<div class="col-sm-12"  style="margin-left: 10px; ">
		<h3 align="center">접속 현황</h3>
		<div id="dashboard_main" style="border: 2px solid #91AA9D;" ></div>
	</div>		
</div>
<div class="row" style="padding-bottom: 5%">
	<h3>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;주문 현황</h3>
	<div class="col-sm-7">
		<div id="dashboard_main_1" style="border: 2px solid #91AA9D;"></div>
	</div>
	<div class="col-sm-5">
		<div id="dashboard_main_2" style="border: 2px solid #91AA9D;"></div>
	</div>
</div>

<script type='text/javascript'>

var listDate_MC = ${listDate_MC};
var listDate_TC = ${listDate_TC};
var listDate_EC = ${listDate_EC};
var listDate_CALL = ${listDate_CALL};
var listCount = ${listCount};


//chart1

	Highcharts.chart('dashboard_main', {
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
	        valueSuffix: ' p/d'
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
	            pointInterval: (3600000 * 24), // one hour
	            pointStart: Date.UTC(2017, 07, 01, 0, 0, 0)
	        }
	    },
	    series: [{
	    	color: '#FFE11A',
	        name: 'MC',
	        data: listDate_MC 
	    }, {
	    	color: '#1F8A70',
	        name: 'EC',
	        data: listDate_EC
	    }, {
	    	color: '#BEDB39',
	        name: 'TC',
	        data: listDate_TC
	    },
	      {
	    	color: '#FD7400',
	        name: 'CALL',
	        data: listDate_CALL
	    }],
	    navigation: {
	        menuItemStyle: {
	            fontSize: '10px'
	        }
	    }
	});

//chart2
Highcharts.chart('dashboard_main_1', {
	exporting: { 
		enabled: false 
		},
	 credits: {
         enabled: false
     },
    chart: {
        type: 'column'
    },
    title: {
        text: ''
    },
    xAxis: {
        type: 'category'
    },
    yAxis: {
        title: {
            text: ''
        }

    },
    legend: {
        enabled: false
    },
    plotOptions: {
        series: {
            borderWidth: 0,
            dataLabels: {
                enabled: true,
                format: '{point.y}'
            }
        }
    },
    series: [{
        name: 'Brands',
        colorByPoint: true,
        data: [{
        	color: '#1F8A70',
            name: 'EC',
            y: listCount[2]
     
        }, {
        
        	color: '#BEDB39',
            name: 'TC',
            y: listCount[1]
          
        }, {
        	color: '#FFE11A',
            name: 'MC',
            y: listCount[0]
           
        }, {
        	color: '#FD7400',
            name: 'CALL',
            y: listCount[3]
          
        }]   
    }]
});

//chart3
Highcharts.chart('dashboard_main_2', {
	exporting: { 
		enabled: false 
		},
	credits: {
        enabled: false
    },
    chart: {
        plotBackgroundColor: null,
        plotBorderWidth: null,
        plotShadow: false,
        type: 'pie'
    },
    title: {
        text: ''
    },
    tooltip: {
        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
    },
    plotOptions: {
        pie: {
            allowPointSelect: true,
            cursor: 'pointer',
            dataLabels: {
                enabled: true,
                format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                style: {
                    color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                }
            }
        }
    },
    series: [{
        name: 'Brands',
        colorByPoint: true,
        data: [{
        	color: '#1F8A70',
            name: 'EC',
            y: listCount[2]
        }, {
        	color: '#BED839',
            name: 'TC',
            y: listCount[1],
            sliced: true,
            selected: true
        }, {
        	color: '#FFE11A',
            name: 'MC',
            y: listCount[0]
        }, {
        	color: '#FD7400',
            name: 'CALL',
            y: listCount[3]
        }]
    }]
});

</script>