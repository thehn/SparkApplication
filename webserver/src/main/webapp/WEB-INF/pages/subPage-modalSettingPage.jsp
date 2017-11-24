<!-- Setting Page -->
<div class="modal fade" id="squarespaceModal" tabindex="-1"
	role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">×</span><span class="sr-only">Close</span>
				</button>
				<h2 class="modal-title algorithm-header">Configuration</h2>
			</div>
			<div class="modal-body">

				<ul class="nav nav-tabs">
					<li class="active"><a data-toggle="tab" href="#sparkServer">Spark
							Server</a></li>
					<li><a data-toggle="tab" href="#elasticsearch">Elasticsearch</a></li>
					<li><a data-toggle="tab" href="#separator">Separator</a></li>

				</ul>

				<!-- all settings go here -->
				<form id="settingsForm" onsubmit="return false"
					modelAttribute="webSettings">
					<div class="tab-content">

						<!-- Spark server tab settings -->
						<div id="sparkServer" class="tab-pane fade in active">
							<h4>Settings for Spark server</h4><br>
							<div class="form-group">
								<label for="sparkHost">Spark Host</label> <input type="text"
									class="form-control" name="sparkHost" value=${sparkHost }
									placeholder="Enter spark host">
							</div>
							<div class="form-group">
								<label for="sparkPort">Spark Port</label> <input type="number"
									class="form-control" name="sparkPort" value=${sparkPort }
									placeholder="Enter spark port">
							</div>
						</div>

						<!-- ElasticSearch tab settings -->
						<div id="elasticsearch" class="tab-pane fade">
							<h4>Settings for Elasticsearch server</h4><br>
							<p>Note(*): The change of Elasticsearch setting will be only applied after restarting Web server !!!</p>
							<div class="form-group">
								<label for="esHost">Elasticsearch Host</label> <input
									type="text" class="form-control" name="esHost" value=${esHost }
									placeholder="Enter Elasticsearch host">
							</div>
							<div class="form-group">
								<label for="esTransportPort">Elasticsearch Transport
									Port</label> <input type="number" class="form-control"
									name=esTransportPort value=${esTransportPort }
									placeholder="Enter Elasticsearch port">
							</div>
							<div class="form-group">
								<label for="esTransportPort">Cluster name
									</label> <input type="text" class="form-control"
									name=esClusterName value=${esClusterName }
									placeholder="Enter Elasticsearch cluster name">
							</div>
						</div>

						<div id="separator" class="tab-pane fade">
							<h4>Settings separate character for matching table</h4><br>
							<div class="form-group">
								<label for="dataSeparator">Matching table separator</label> <input
									type="text" class="form-control" name="dataSeparator"
									value=${dataSeparator }>
							</div>
						</div>
					</div>
				</form>

			</div>
			<div class="modal-footer">
				<div class="btn-group btn-group-justified" role="group"
					aria-label="group button">
					<div class="btn-group" role="group">
						<button type="button" class="btn btn-primary" data-dismiss="modal"
							id="btnSaveSettings" role="button">Save</button>
					</div>
					<div class="btn-group btn-delete hidden" role="group">
						<button type="button" id="delImage"
							class="btn btn-default btn-hover-red" data-dismiss="modal"
							role="button">Delete</button>
					</div>
				</div>
			</div>

			<div id="responseView"></div>

		</div>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function() {

		/* Button "Save" for updating some settings */
		$('#btnSaveSettings').click(function() {
			updateConfig(settingsForm);
		});

	});
</script>