<div class="row">
	<div class="col-lg-12">
		<!-- Predict, Actual classes,Features viewer -->
		<div class="panel panel-default">
			<div class="panel-heading">
				<strong>Predicted labels and features</strong>
			</div>
			<div class="panel-body">
				<div class="row">
					<div class="col-lg-12">
						<div class="panel-body">
							<div class="table-responsive">
								<table id="predictionInfo_1" class="table table-striped table-bordered" style="cellspacing: 0; width: 100%;"></table>
							</div>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-md-12">
						<a href="/ml/download/${predictedFileName}" class="btn btn-success pull-right" role="button">Download</a>
					</div>
				</div>
			</div>
			<div class="panel-footer"></div>
		</div>
	</div>
</div>

<!-- Detail data: Predicted, Actual classes and Features -->
<script type="text/javascript">
var predictionInfo = ${predictionInfo};
/* var header = $('#header').val(); #P00005 */

// make columns list
var colunmsList = [];
var element;
element = {title: "Label"};
colunmsList.push(element);
// var delimiter = $('#delimiter').val(); // #PC0011
var delimiter = ','; // #PC0011
// var fieldsList = header.split(delimiter); // #P00005
var fieldsList = $('#fieldsForPredict').val().toString().split(delimiter); // #P00005
var field;
for (field of fieldsList){
	element = {title: field};
	colunmsList.push(element);
}

	$(document).ready(function() {
		$('#predictionInfo_1').DataTable({
			data : predictionInfo,
			/* columns : [ {
				title : "Predicted"
			}, {
				title : "Actual",
				"visible": false
			}, {
				title : "Features"
			}] */
			columns : colunmsList,
			rowCallback: function(row, data, index){
				// $(row).find('td:eq(0)').css('background-color', '#66FF66');
				if(data[0] == '10'){
					$(row).find('td:eq(0)').css('background-color', '#66FF66');
				}else if(data[0] == '20'){
					$(row).find('td:eq(0)').css('background-color', '#FFA500');
				}else if(data[0] == '30' || data[0] == '40'){
					$(row).find('td:eq(0)').css('background-color', '#FF3333');
				}else{
					$(row).find('td:eq(0)').css('background-color', '#C0C0C0');
				};
			},
			responsive: true
		});
		
	});
</script>
