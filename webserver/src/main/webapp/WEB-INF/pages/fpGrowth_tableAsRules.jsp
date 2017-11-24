<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-success">
			<div class="panel-heading">
				<strong>Association Rules</strong>
			</div>
			<div class="panel-body">
				<div class="table-responsive">
					<table id="asRules" class="table table-striped table-bordered" style="cellspacing: 0; width: 100%;"></table>
				</div>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
var dataAsRules = ${dataAsRules};

	$(document).ready(function() {
		$('#asRules').DataTable({
			data : dataAsRules,
			columns : [ {
				title : "Antecedence"
			}, {
				title : "Consequence"
			}, {
				title : "Confidence"
			}],
			responsive: true
		});
		
	});
</script>