<div style="margin: 10px;">
	<div class="row">
		<div class="col-lg-4">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<strong>Accuracy</strong>
				</div>
				<div class="panel-body">
					<!-- <svg id="fillgauge1" width="100%" height="120%""></svg> -->
					<h1>${accuracy}</h1>
				</div>
			</div>
		</div>
		<div class="col-lg-4">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<strong>Precision</strong>
				</div>
				<div class="panel-body">
					<!-- <svg id="fillgauge2" width="100%" height="120%""></svg> -->
					<h1>${weightedPrecision}</h1>
				</div>
			</div>
		</div>
		<div class="col-lg-4">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<strong>Recall</strong>
				</div>
				<div class="panel-body">
					<!-- <svg id="fillgauge3" width="100%" height="120%""></svg> -->
					<h1>${weightedRecall}</h1>
				</div>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-lg-4">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<strong>F-Measure</strong>
				</div>
				<div class="panel-body">
					<!-- <svg id="fillgauge4" width="100%" height="120%""></svg> -->
					<h1>${weightedFMeasure}</h1>
				</div>
			</div>
		</div>
		<div class="col-lg-4">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<strong>False Positive Rate</strong>
				</div>
				<div class="panel-body">
					<!-- <svg id="fillgauge5" width="100%" height="120%""></svg> -->
					<h1>${weightedFalsePositiveRate}</h1>
				</div>
			</div>
		</div>
		<div class="col-lg-4">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<strong>True Positive Rate</strong>
				</div>
				<div class="panel-body">
					<!-- <svg id="fillgauge6" width="100%" height="120%""></svg> -->
					<h1>${weightedTruePositiveRate}</h1>
				</div>
			</div>
		</div>
	</div>
</div>

<script>
	//#P00005 - Start
	var featuresForPredict = ${featuresForPredict};
	$('#fieldsForPredict').val(featuresForPredict);
	// #P00005 - End
</script>

<!-- Confusion Matrix -->
<!-- <div class="panel panel-default">
	<div class="panel-heading">
		<strong>Confusion Matrix</strong>
	</div>
	<div class="panel-body">
		<div class="row">
			<h4 align="center">Confusion Matrix</h4>
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