<!-- View list of Decision Tree -->
<style>
	.node circle {
		fill: #fff;
		stroke: steelblue;
		stroke-width: 3px;
	}
	.node text {
		font: 12px sans-serif;
	}

	.link {
		fill: none;
		stroke: #ccc;
		stroke-width: 2px;
	}
</style>

<script>
	var decisionTree = ${decisionTree};
	var numTrees = decisionTree.length;
	var requestUrl = "request-RfcDecisionTree";
	
	for(var tree_index = 0; tree_index < numTrees; tree_index ++){
		$('#treeList').append('<a class="list-group-item list-group-item-success tree-list">' + 'Random Forest Decision Tree ' + (tree_index + 1) + '</a>');
	}
	/* 
		Send ajax request to controller, then get the ModelAndView of DecisionTree
	*/
	$(document).ready(function() {
		$('.tree-list').click(function showTree(){
			var tree_index = $(this).text().split(" ")[4] - 1;
			var data = decisionTree[tree_index];
			
			$('#treeDataInput').val(JSON.stringify(data));
			
			$.ajax({
				type : "POST",
				url : requestUrl,
				data : $('#treeDataForm').serialize(),
				beforeSend : function() {
					//
				}
			}).done(function(response) {
				var win=window.open('about:blank');
				with(win.document)
				{
					open();
					write(response);
					close();
				}
				
			}).fail(function() {
				console.log("error");
			}).always(function() {
				//
			});
			
		});
	});
</script>
<!-- load the d3.js library -->
<script src="http://d3js.org/d3.v3.min.js"></script>
<div class="row">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<strong>List of Decision Trees</strong> (Click to a tree name to view its model visualization)
		</div>
		<div class="panel-body">
			<div class="list-group" id="treeList"></div>
			<form modelAttribute="treeData" id="treeDataForm" onsubmit="return false" style="display: none;">
				<input name="treeData" id="treeDataInput">
			</form>
		</div>
		<div class="panel-footer"></div>
	</div>
	
</div>

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
var columnsList = ${columnsList};

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
			columns: columnsList,
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
