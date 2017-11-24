<!-- Predict, Actual classes,Features viewer -->
<h4>Filtered features</h4>
<div class="row">
	<div class="col-lg-12">
		<div class="table-responsive">
			<table id="filteredFeatures" class="table table-striped table-bordered" style="cellspacing: 0; width: 100%;"></table>
		</div>
	</div>
</div>
<br>
<div class="row">
	<div class="col-md-12">
		<a href="/ml/download/${filteredFeaturesFileName}" class="btn btn-success pull-right" role="button">Download</a>
	</div>
</div>

<!-- Detail data: Predicted, Actual classes and Features -->
<script type="text/javascript">
var filteredFeatures = ${filteredFeatures};
var columnsList = ${columnsList};

	$(document).ready(function() {
		$('#filteredFeatures').DataTable({
			data : filteredFeatures,
			columns :columnsList,
			rowCallback: function(row, data, index){
				$(row).find('td:eq(0)').css('background-color', '#66FF66');
			},
			responsive: true
		});
		
	});
</script>