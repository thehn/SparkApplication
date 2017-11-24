<div style="margin: 10px;">
	<!-- ROC -->
	<!-- <div class="row">
		<div class="col-lg-12">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<strong>ROC</strong>
				</div>
				<div class="panel-body">
					<div style="width: 100%;">
						<canvas id="canvas1_1" style="padding: 1%"></canvas>
					</div>
				</div>
				<div class="panel-footer"></div>
			</div>
		</div>
	</div> -->
	<!-- Metrics by threshold -->
	<!-- <div class="row">
		<div class="col-lg-12">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<strong>Metrics by Thresholds</strong>
				</div>
				<div class="panel-body">
					<div style="width: 100%;">
						<canvas id="canvas2_1" style="padding: 1%"></canvas>
					</div>
				</div>
				<div class="panel-footer"></div>
			</div>
		</div>
	</div> -->
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
</div>

<script>
	//#PC0005 - Start
	var featuresForPredict = ${featuresForPredict};
	$('#fieldsForPredict').val(featuresForPredict);
	// #PC0005 - End
</script>

<!-- Confusion Matrix -->
<!-- <div class="panel panel-primary">
	<div class="panel-heading">
		<strong>Confusion Matrix</strong>
	</div>
	<div class="panel-body">
		<div class="row">
			<div class="table-responsive">
				<table class="table table-bordered table-hover table-striped">
					<thead style="text-align: center;">
						<tr class="info">
							<th id="commonCell_1" colspan="2" class="col-md-3"></th>
							<th id="actualCell_1" style="text-align: center; ">Actual Class</th>
						</tr>
						<tr id="actualLabels_1" class="info" style="text-align: center;">
						</tr>
					</thead>
					<tbody id="tableBody_1">
						<tr id="firstRow">
							<th id="predictedCell_1" class="info" style="text-align: center; vertical-align: middle;">Predicted Class</th>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div class="panel-footer"></div>
</div> -->

<!-- Confusion Matrix -->
<!-- <script type="text/javascript">
	var confusionMatrix = ${confusionMatrix};
	var labels = ${labels};
	var matrixSize = Math.sqrt(confusionMatrix.length);
	
	// set rowspan, colspan to matrixSize
	$('#commonCell_1').attr('rowspan', matrixSize);
	$('#actualCell_1').attr('colspan', matrixSize);
	$('#predictedCell_1').attr('rowspan', matrixSize);
	
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
		$('#actualLabels_1').append('<th>' + labels[i] + ' (' + countByLabels[i] + ')' + '</th>');
	}
	
	// fill data for rows (including labels and confusion metrix data)
	var firstRow = true;
	var index = 0;
	for (var i = 0; i < matrixSize; i++){
		if(firstRow){
			firstRow = false;
		}else{
			$('#tableBody_1').append('<tr></tr>');
		}
		$('#tableBody_1>tr:last').append('<td class="info">' + labels[i] + '</td>');
		for (var j = 0; j < matrixSize; j++){
			if (i == j){
				$('#tableBody_1>tr:last').append('<td class="success">' + confusionMatrix[index] + ' (' + Math.round(confusionMatrix[index++]*100/countByLabels[i], 2) + '%)' + '</td>');
			}else{
				$('#tableBody_1>tr:last').append('<td>' + confusionMatrix[index++] + '</td>');
			}
		}
	}
</script> -->

<!-- fill threshold to select form -->
<script type="text/javascript">
	var threshold = ${threshold};
	fillOptionsForSelectForm(threshold, "listThresholds");
</script>

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
	var ctx1 = document.getElementById("canvas1_1").getContext("2d");
	var ctx2 = document.getElementById("canvas2_1").getContext("2d");
	window.myLine = new Chart(ctx1, config1);
	window.myLine = new Chart(ctx2, config2);
</script> -->
	
