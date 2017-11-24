<style>
.chart {
	
}

.main text {
	font: 10px sans-serif;
}

.axis line, .axis path {
	shape-rendering: crispEdges;
	stroke: black;
	fill: none;
}

circle {
	fill: steelblue;
}
</style>

<div class='content'>
	<div class="col-md-12" align="center">
		<div id="ChartID" style="margin: 0 auto;"></div>
	</div>
</div>

<script>
var listData = ${listData};
var seriesData = [];
var clusterIndex = ${minCluster};
var colorIndex = 0;
var dataIndex = 0;

listData.forEach(function(data){
	var _name = 'Cluster' + (clusterIndex++);
	var _color = Highcharts.Color(Highcharts.getOptions().colors[colorIndex++]).setOpacity(0.8).get('rgba');
	var _data = listData[dataIndex++];
	seriesData.push({name : _name , color : _color, data : _data});
});

Highcharts.chart('ChartID', {
	chart: {
		type: 'scatter',
		zoomType: 'xy'
	},
	title: {
		text: 'Clusters - scatter chart'
	},
	subtitle: {
		enabled: false,
		// text: 'Source: Heinz  2003'
	},
	xAxis: {
		title: {
			enabled: true,
			text: 'x'
		},
		startOnTick: true,
		endOnTick: true,
		showLastLabel: true
	},
	yAxis: {
		title: {
			text: 'y'
		}
	},
	legend: {
		layout: 'vertical',
		align: 'left',
		verticalAlign: 'top',
		x: 100,
		y: 70,
		floating: true,
		backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF',
		borderWidth: 1
	},
	plotOptions: {
		scatter: {
			marker: {
				radius: 5,
				states: {
					hover: {
						enabled: true,
						lineColor: 'rgb(100,100,100)'
					}
				}
			},
			states: {
				hover: {
					marker: {
						enabled: false
					}
				}
			},
			tooltip: {
				headerFormat: '<b>{series.name}</b><br>',
				pointFormat: '{point.x}; {point.y}'
			}
		}
	},
	series: seriesData
});
</script>