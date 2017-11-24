<!-- <div class="row">
	<div class="col-lg-12">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<strong>ROC</strong>
			</div>
			<div class="panel-body">
				<div style="width: 100%;">
					<canvas id="canvas1" style="padding: 1%"></canvas>
				</div>
			</div>
			<div class="panel-footer"></div>
		</div>
	</div>
</div> -->
<!-- <div class="row">
	<div class="col-lg-12">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<strong>Metrics by Thresholds</strong>
			</div>
			<div class="panel-body">
				<div style="width: 100%;">
					<canvas id="canvas2" style="padding: 1%"></canvas>
				</div>
			</div>
			<div class="panel-footer"></div>
		</div>
	</div>
</div> -->

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<strong>Metrics by thresholds</strong>
			</div>
			<div class="panel-body">
				<div id="hightChartMetrics"></div>
			</div>
			<div class="panel-footer"></div>
		</div>
	</div>
</div>


<div class="row">
	<div class="col-lg-3">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<strong>Area Under ROC</strong>
			</div>
			<div class="panel-body">
				<!-- Gauge -->
				<!-- <svg id="fillgauge1" width="100%" height="120%""></svg> -->
				<h2 align="center">${areaUnderRoc}</h2>
			</div>
			<div class="panel-footer"></div>
		</div>
	</div>
	<div class="col-lg-3">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<strong>Area Under PRC</strong>
			</div>
			<div class="panel-body">
				<!-- Gauge -->
				<!-- <svg id="fillgauge2" width="100%" height="120%""></svg> -->
				<h2 align="center">${areaUnderPrecisionRecallCurve}</h2>
			</div>
			<div class="panel-footer"></div>
		</div>
		<h1></h1>
	</div>
	<div class="col-lg-3">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<strong>Accuracy</strong>
			</div>
			<div class="panel-body">
				<!-- Gauge -->
				<!-- <svg id="fillgauge1" width="100%" height="120%""></svg> -->
				<h2 align="center">${accuracy}</h2>
			</div>
			<div class="panel-footer"></div>
		</div>
	</div>
	<div class="col-lg-3">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<strong>Weighted Precision</strong>
			</div>
			<div class="panel-body">
				<!-- Gauge -->
				<!-- <svg id="fillgauge2" width="100%" height="120%""></svg> -->
				<h2 align="center">${weightedPrecision}</h2>
			</div>
			<div class="panel-footer"></div>
		</div>
		<h1></h1>
	</div>
</div>

<div class="row">
	<div class="col-lg-3">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<strong>Weighted Recall</strong>
			</div>
			<div class="panel-body">
				<!-- Gauge -->
				<!-- <svg id="fillgauge1" width="100%" height="120%""></svg> -->
				<h2 align="center">${weightedRecall}</h2>
			</div>
			<div class="panel-footer"></div>
		</div>
	</div>
	<div class="col-lg-3">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<strong>Weighted True Positive</strong>
			</div>
			<div class="panel-body">
				<!-- Gauge -->
				<!-- <svg id="fillgauge2" width="100%" height="120%""></svg> -->
				<h2 align="center">${weightedTruePositiveRate}</h2>
			</div>
			<div class="panel-footer"></div>
		</div>
		<h1></h1>
	</div>
	<div class="col-lg-3">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<strong>Weighted False Positive</strong>
			</div>
			<div class="panel-body">
				<!-- Gauge -->
				<!-- <svg id="fillgauge1" width="100%" height="120%""></svg> -->
				<h2 align="center">${weightedFalsePositiveRate}</h2>
			</div>
			<div class="panel-footer"></div>
		</div>
	</div>
	<div class="col-lg-3">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<strong>Weighted F-Measure</strong>
			</div>
			<div class="panel-body">
				<!-- Gauge -->
				<!-- <svg id="fillgauge2" width="100%" height="120%""></svg> -->
				<h2 align="center">${weightedFMeasure}</h2>
			</div>
			<div class="panel-footer"></div>
		</div>
		<h1></h1>
	</div>
</div>

<!-- Formula -->
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="row" style="margin-bottom: 20px;">
	<div class="col-lg-12">
		<img src='<spring:url value="/resources/img/bg_formula.jpg"/>'>
	</div>	
</div>

<!-- Confusion Matrix -->
<div class="panel panel-primary">
	<div class="panel-heading">
		<strong>Confusion Matrix</strong>
	</div>
	<div class="panel-body">
		<div class="table-responsive">
			<table class="table table-bordered table-hover table-striped">
				<thead style="text-align: center;">
					<tr class="info">
						<th id="commonCell" colspan="2" class="col-md-3"></th>
						<th id="actualCell" style="text-align: center; ">Actual Class</th>
					</tr>
					<tr id="actualLabels" class="info" style="text-align: center;">
					</tr>
				</thead>
				<tbody id="tableBody">
					<tr id="firstRow">
						<th id="predictedCell" class="info" style="text-align: center; vertical-align: middle;">Predicted Class</th>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<div class="panel-footer"></div>
</div>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<strong>Results of Predicting</strong>
			</div>
			<div class="panel-body">
				<!-- Predict, Actual classes,Features viewer -->
				<div class="row">
					<div class="col-lg-12">
						<div class="panel-body">
							<div class="table-responsive">
								<table id="predictionInfo" class="table table-striped table-bordered" style="cellspacing: 0; width: 100%;"></table>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="panel-footer"></div>
		</div>
	</div>
</div>

<!-- <script type="text/javascript">

	/* ROC curve */
	var xValuesRoc = ${xValuesRoc};
	var yValuesRoc = ${yValuesRoc};
	
	/* Metrics by threshold */
	var threshold = ${threshold};
	var f1Score = ${f1Score};
	var f2Score = ${f2Score};
	var recall = ${recall};
	var precision = ${precision};
	
	var config1 = {
		type : 'line',
		data : {
			labels : xValuesRoc,
			datasets : [ {
				label : "ROC",
				backgroundColor : '#ff0000',
				borderColor : '#ff0000',
				data : yValuesRoc,
				fill : false,
			}]
		},
		options : {
			elements: { point: { radius: 0 } },
			responsive : true,
			title : {
				display : false,
				text : 'Receiver operating characteristic'
			},
			tooltips : {
				mode : 'index',
				intersect : true,
			},
			hover : {
				mode : 'nearest',
				intersect : true
			},
			scales : {
				xAxes : [ {
					display : false,
					scaleLabel : {
						display : true,
						labelString : 'False positive rate'
					}
				} ],
				yAxes : [ {
					display : true,
					scaleLabel : {
						display : true,
						labelString : 'True positive rate'
					}
				} ]
			}
		}
	};
	
	var config2 = {
		type : 'line',
		data : {
			labels : threshold,
			datasets : [ {
				label : "F1 Score",
				backgroundColor : '#ff0000',
				borderColor : '#ff0000',
				data : f1Score,
				fill : false,
			}, {
				label : "F2 Score",
				fill : false,
				backgroundColor : '#f0ff00',
				borderColor : '#f0ff00',
				data : f2Score,
			}, {
				label : "Recall",
				fill : false,
				backgroundColor : '#09ff00',
				borderColor : '#09ff00',
				data : recall,
			}, {
				label : "Precision",
				fill : false,
				backgroundColor : '006bff',
				borderColor : '#006bff',
				data : precision,
			} ]
		},
		options : {
			elements: { point: { radius: 0 } },
			responsive : true,
			title : {
				display : false,
				text : ''
			},
			tooltips : {
				mode : 'index',
				intersect : true,
			},
			hover : {
				mode : 'nearest',
				intersect : true
			},
			scales : {
				xAxes : [ {
					display : false,
					scaleLabel : {
						display : true,
						labelString : 'Thresholds'
					}
				} ],
				yAxes : [ {
					display : true,
					scaleLabel : {
						display : true,
						labelString : 'Metrics by Thresholds'
					}
				} ]
			}
		}
	};
	
	/* Draw charts */
	var ctx1 = document.getElementById("canvas1").getContext("2d");
	var ctx2 = document.getElementById("canvas2").getContext("2d");
	window.myLine = new Chart(ctx1, config1);
	window.myLine = new Chart(ctx2, config2);
</script> -->
	
<!-- Confusion Matrix -->
<script type="text/javascript">
	var confusionMatrix = ${confusionMatrix};
	var labels = ${labels};
	var matrixSize = Math.sqrt(confusionMatrix.length);
	
	// set rowspan, colspan to matrixSize
	$('#commonCell').attr('rowspan', matrixSize);
	$('#actualCell').attr('colspan', matrixSize);
	$('#predictedCell').attr('rowspan', matrixSize);
	
	// calculate total
	var countByLabels = [];
	var sum = 0;
	for(var i = 0; i < matrixSize; i++){
		for (var j = 0; j < matrixSize; j++){
			sum += confusionMatrix[j*matrixSize + i];
		}
		countByLabels[i] = sum;
		sum = 0;
	}
	
	// fill actual labels
	for (var i = 0; i < labels.length; i++){
		$('#actualLabels').append('<th>' + labels[i] + ' (' + countByLabels[i] + ')' + '</th>');
	}
	
	// fill data for rows (including labels and confusion metrix data)
	var firstRow = true;
	var index = 0;
	for (var i = 0; i < matrixSize; i++){
		if(firstRow){
			firstRow = false;
		}else{
			$('#tableBody').append('<tr></tr>');
		}
		$('#tableBody>tr:last').append('<td class="info">' + labels[i] + '</td>');
		for (var j = 0; j < matrixSize; j++){
			if (i == j){
				$('#tableBody>tr:last').append('<td class="success">' + confusionMatrix[index] + ' (' + Math.round(confusionMatrix[index++]*100/countByLabels[i], 2) + '%)' + '</td>');
			}else{
				$('#tableBody>tr:last').append('<td>' + confusionMatrix[index++] + '</td>');
			}
		}
	}
</script>

<!-- Detail data: Predicted, Actual classes and Features -->
<script type="text/javascript">
var predictionInfo = ${predictionInfo};
var columnsList = ${columnsList}; // #P0002

	$(document).ready(function() {
		$('#predictionInfo').DataTable({
			data : predictionInfo,
			/* columns : [ {
				title : "Predicted"
			}, {
				title : "Actual"
			}, {
				title : "Features"
			}], */ // #P0002
			columns: columnsList, // #P0002
			rowCallback: function(row, data, index){
				if(data[0] != data[1]){
					$(row).find('td:eq(0)').css('background-color', '#FF3333');
				}else{
					$(row).find('td:eq(0)').css('background-color', '#66FF66');
				}
			},
			responsive: true
		});
		
	});
</script>


<script type="text/javascript">
	/*
	The purpose of this demo is to demonstrate how multiple charts on the same page can be linked
	through DOM and Highcharts events and API methods. It takes a standard Highcharts config with a
	small variation for each data set, and a mouse/touch event handler to bind the charts together.
	*/
	
	/**
	 * In order to synchronize tooltips and crosshairs, override the
	 * built-in events with handlers defined on the parent element.
	 */
	$('#hightChartMetrics').bind('mousemove touchmove touchstart', function (e) {
	    var chart,
	        point,
	        i,
	        event;
	
	    for (i = 0; i < Highcharts.charts.length; i = i + 1) {
	        chart = Highcharts.charts[i];
	        event = chart.pointer.normalize(e.originalEvent); // Find coordinates within the chart
	        point = chart.series[0].searchPoint(event, true); // Get the hovered point
	
	        if (point) {
	            point.highlight(e);
	        }
	    }
	});
	/**
	 * Override the reset function, we don't need to hide the tooltips and crosshairs.
	 */
	Highcharts.Pointer.prototype.reset = function () {
	    return undefined;
	};
	
	/**
	 * Highlight a point by showing tooltip, setting hover state and draw crosshair
	 */
	Highcharts.Point.prototype.highlight = function (event) {
	    this.onMouseOver(); // Show the hover marker
	    this.series.chart.tooltip.refresh(this); // Show the tooltip
	    this.series.chart.xAxis[0].drawCrosshair(event, this); // Show the crosshair
	};
	
	/**
	 * Synchronize zooming through the setExtremes event handler.
	 */
	function syncExtremes(e) {
	    var thisChart = this.chart;
	
	    if (e.trigger !== 'syncExtremes') { // Prevent feedback loop
	        Highcharts.each(Highcharts.charts, function (chart) {
	            if (chart !== thisChart) {
	                if (chart.xAxis[0].setExtremes) { // It is null while updating
	                    chart.xAxis[0].setExtremes(e.min, e.max, undefined, false, { trigger: 'syncExtremes' });
	                }
	            }
	        });
	    }
	}

	// Get the data. The contents of the data file can be viewed at
	// https://github.com/highcharts/highcharts/blob/master/samples/data/activity.json
	/* $.getJSON('https://www.highcharts.com/samples/data/jsonp.php?filename=activity.json&callback=?', function (activity) */ 


	/* ROC curve */
	var xValuesRoc = ${xValuesRoc};
	var yValuesRoc = ${yValuesRoc};
	
	/* Metrics by threshold */
	var threshold = ${threshold};
	var f1Score = ${f1Score};
	var f2Score = ${f2Score};
	var recall = ${recall};
	var precision = ${precision};
	
	var activity = ({
    "xData": xValuesRoc,
    "datasets": [{
        "name": "ROC Curve",
        "data": yValuesRoc,
        "unit": "",
        "type": "area",
        "valueDecimals": 3
    }, {
        "name": "Precision",
        "data": precision,
        "unit": "",
        "type": "line",
        "valueDecimals": 3
    }, {
        "name": "Recall",
        "data": recall,
        "unit": "",
        "type": "line",
        "valueDecimals": 3
    } , {
       "name": "F-1 Score",
        "data": f1Score,
        "unit": "",
        "type": "line",
        "valueDecimals": 3
    }, {
        "name": "F-2 Score",
        "data": f2Score,
        "unit": "",
        "type": "line",
        "valueDecimals": 3
    }]
	});
	
    $.each(activity.datasets, function (i, dataset) {

        // Add X values
        dataset.data = Highcharts.map(dataset.data, function (val, j) {
            return [activity.xData[j], val];
        });

        $('<div class="chart">')
            .appendTo('#hightChartMetrics')
            .highcharts({
                chart: {
                    marginLeft: 40, // Keep all charts left aligned
                    spacingTop: 20,
                    spacingBottom: 20
                },
                title: {
                    text: dataset.name,
                    align: 'left',
                    margin: 0,
                    x: 30
                },
                credits: {
                    enabled: false
                },
                legend: {
                    enabled: false
                },
                xAxis: {
                	tickInterval: 0.05,
                	tickPosition: 'inside',
                    crosshair: true,
                    events: {
                        setExtremes: syncExtremes
                    },
                    labels: {
                        format: '{value}'
                    }
                },
                yAxis: {
                	crosshair: true,
                    title: {
                        text: null
                    }
                },
                tooltip: {
                    positioner: function () {
                        return {
                            x: this.chart.chartWidth - this.label.width, // right aligned
                            y: 10 // align to title
                        };
                    },
                    borderWidth: 0,
                    backgroundColor: 'none',
                    pointFormat: '{point.y}',
                    headerFormat: '',
                    shadow: false,
                    style: {
                        fontSize: '18px'
                    },
                    valueDecimals: dataset.valueDecimals
                },
                series: [{
                    data: dataset.data,
                    name: dataset.name,
                    type: dataset.type,
                    color: Highcharts.getOptions().colors[i],
                    fillOpacity: 0.3,
                    tooltip: {
                        valueSuffix: ' ' + dataset.unit
                    }
                }]
            });
    });
</script>