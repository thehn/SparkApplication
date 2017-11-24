<!-- Metrics -->
<div class="row">
	<div class="col-lg-4">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<strong>Accuracy</strong>
			</div>
			<div class="panel-body">
				<!-- <svg id="fillgauge1" width="100%" height="120%""></svg> -->
				<h1 align="center">${accuracy}</h1>
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
				<h1 align="center">${weightedPrecision}</h1>
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
				<h1 align="center">${weightedRecall}</h1>
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
				<h1 align="center">${weightedFMeasure}</h1>
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
				<h1 align="center">${weightedFalsePositiveRate}</h1>
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
				<h1 align="center">${weightedTruePositiveRate}</h1>
			</div>
		</div>
	</div>
</div>

<!-- Formula -->
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="row" style="margin-bottom: 20px;">
	<div class="col-lg-12">
		<img src='<spring:url value="/resources/img/bg_formula.jpg"/>'>
	</div>	
</div>

<!-- Classification Results -->
<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<strong>Classification Results</strong>
			</div>
			<div class="panel-body">
				<div class="row">
					<h4 align="center">Confusion Matrix</h4>
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
				<br><br>
				
				<!-- Predict, Actual classes,Features viewer -->
				<h4 align="center">Predicted, Actual classes and Features</h4>
				<div class="row">
					<div class="col-lg-12">
						<div class="table-responsive">
							<table id="predictionInfo" class="table table-striped table-bordered" style="cellspacing: 0; width: 100%;"></table>
						</div>
					</div>
				</div>
			</div>
			<div class="panel-footer"></div>
		</div>
	</div>
</div>


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
