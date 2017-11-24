<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Pattern mining</title>
<!-- all libraries -->
<%@ include file="subPage-libraries.jsp"%>
<link rel="stylesheet" type="text/css" href="<spring:url value="/resources/css/commonStyle.css"/>">
<script src="<spring:url value="/resources/js/cehCommon.js?6.0"/>" type="text/javascript"></script>
<script src="<spring:url value="/resources/js/fpGrowth.js?6.0"/>" type="text/javascript"></script>
</head>

<body>

	<!-- Navigation bar -->
	<%@ include file="subPage-navigationBar.jsp"%>

	<!-- Container -->
	<div class="container" id="container">
		<ul class="nav nav-tabs">
			<li class="active"><a data-toggle="tab" id="trainingTabPaneBtn" href="#freqPatterns">Frequent
					Patterns Mining</a></li>
			<li><a data-toggle="tab" href="#asRulesTab" id="asRulesTabPaneBtn">Association
					Rules</a></li>
			<li><a data-toggle="tab" href="#matchingTable">Matching
					Table</a></li>
		</ul>

		<div class="tab-content">
			<div id="freqPatterns" class="tab-pane fade in active">
				<h2 class="algorithm-header">Frequent pattern mining</h2>
				
				<div class="panel panel-main">
					<div class="panel-heading">
						<strong>Creating new model</strong>
					</div>
					<div class="panel-body" id="ml_form">
						<div class="form-group col-md-12">

							<!-- Form data -->
							<form method="POST" id="algorithmSettingForm"
								modelAttribute="appProperties" onsubmit="return false">
								<input name="action" value="train" style="display: none;">
								<input name="algorithm" value="FpGrowth" style="display: none;">
								<!-- Data input -->
								<h3>Data input settings</h3>
								<div class="panel panel-default">
									<div class="panel-heading">
										<strong>Data Lookup</strong>
									</div>
									<div class="panel-body">
										<div class="row">
											<div class="form-group col-md-12">
													<select class="lookup-selection-box" name="listLookups" id="listLookups">
													</select>
											</div>
										</div>
									</div>
									<div class="panel-footer"></div>
								</div>

								<h3>Algorithm settings</h3>
								<!-- Algorithm Settings -->
								<div class="panel panel-default">
									<div class="panel-heading">
										<strong>Parameters for running</strong>
									</div>
									<div class="panel-body">
										<div class="row">
											<div class="form-group col-md-12">
												<div class="col-md-4">
													<label class="control-label" for="minSupport" 
														data-toggle="tooltip" title="Minimum support for an itemset to be identified as frequent. Values: [0.0 - 1.0]">Min Support
													</label>
													<input
														name="minSupport" type="number" min="0"  max="1" step="0.01"
														placeholder="FP-Growth min support value"
														class="form-control input-md" required>
												</div>
												<div class="col-md-4">
													<label class="control-label" for="modelName" data-toggle="tooltip" title="If not choose, it will be saved as default model name.">Save
														Model As</label> <input id="modelName" name="modelName"
														type="text" placeholder="Optional, no space"
														class="form-control input-md">
												</div>
												<div class="col-md-4">
													<label class="control-label" for="numberPartition"
														data-toggle="tooltip" title="Number partitions for running. Default value: 10">
														Number Partition
													</label> <input
														name="numberPartition" type="number" min="0"
														placeholder="Optional"
														class="form-control input-md">
												</div>
											</div>
										</div>
									</div>
									<div class="panel-footer"></div>
								</div>
								
								<!-- Data output -->
								<h3>Output settings</h3>
								<div class="checkbox">
									<label class="checkbox-inline" data-toggle="collapse"
										data-target="#collapseOutputEs_2"> <input
										type="checkbox" name="saveToES"
										id="outputEsCheck_2" /> <strong>Save results to
											Elasticsearch</strong></label>
								</div>
								<div class="panel panel-default panel-collapse collapse"
									id="collapseOutputEs_2">
									<div class="panel-heading">
										<strong>Results will be saved to Elasticsearch</strong>
									</div>
									<div class="panel-body">
										<div class="row">
											<div class="form-group col-md-12">
												<div class="col-md-4">
													<label class="control-label" for="indexW">Index</label> <input
														name="indexW" type="text"
														placeholder="Elasticsearch index to save data"
														class="form-control input-md outputEs_2" required>
												</div>
												<div class="col-md-4">
													<label class="control-label" for="typeW">Type</label> <input
														name="typeW" type="text"
														placeholder="to save Frequent-Items"
														class="form-control input-md outputEs_2" required>
												</div>
											</div>
										</div>
									</div>
									<div class="panel-footer"></div>
								</div>
								
								<!-- Button submit -->
								<div class="form-group" style="padding-top: 5%">
									<div class="col-md-12">
										<a id="btnShowScheduler_1" class="btn btn-xm btn-primary pull-right" style="margin-left: 4px;">
											<span data-toggle="tooltip" data-placement="top" title="Schedule this job" class="fa fa-clock-o" style="font-size:16px"></span>
										</a>
										<button id="btnTrainModel" class="btn btn-md btn-primary pull-right"
										data-toggle="tooltip" data-placement="top" title="Run immediately">Train model</button>
									</div>
								</div>
							</form>
						</div>
					</div>
					<div class="panel-footer">FP-Growth Algorithm</div>
				</div>

				<div id="visualizer_1" class="panel panel-main"
					style="display: none;">
					<div class="panel-heading">
						<strong>Visualization Results</strong>
					</div>

					<div id="responseViewer_1"></div>
				</div>
			</div>

			<div id="asRulesTab" class="tab-pane fade">
				<h2>Association Rules</h2>
				<p>to generate association rules with specified confidence
					value.</p>
				<div class="panel panel-main">
					<div class="panel-heading">
						<strong>Generating rules form</strong>
					</div>
					<div class="panel-body">
						<form method="POST" id="requestAsRulesForm"
							modelAttribute="appProperties" onsubmit="return false">
							<input name="action" value="genAsRules" style="display: none;">
							<input name="algorithm" value="FpGrowth" style="display: none;">
							<!-- Algorithm Settings -->
							<h3>Model choosing</h3>
							<div class="panel panel-default">
								<div class="panel-heading">
									<strong>Parameters for running</strong>
								</div>
								<div class="panel-body">
									<div class="row">
										<div class="form-group col-md-12">
											<div class="col-md-4">
												<div class="row">
													<label class="control-label" for="modelName">Choose a model</label>
													<select class="form-control" id="listModels" name="modelName" required></select>
												</div>
												<div class="row" style="padding-top: 2%;">
													<button type="button" class="btn btn-danger btn-sm pull-left" 
													style="width: 40%;" id="deleteModel" disabled="true">
														<span class="glyphicon glyphicon-trash"></span> Delete Model
													</button>
												</div>
											</div>
											<div class="col-md-4">
												<label class="control-label" for="minConfidence">Min
													Confidence [0.0 - 1.0]</label> <input id="minConfidence"
													name="minConfidence" type="text"
													placeholder="FP-Growth min confidence value"
													class="form-control input-md" required>
											</div>
										</div>
									</div>
									<div class="row">
										<div class="col-md-4" style="padding-left: 15;">
											
										</div>
									</div>
								</div>
								<div class="panel-footer"></div>
							</div>

							<h3>Output settings</h3>
							<!-- Data output -->
							<div class="checkbox">
								<label class="checkbox-inline" data-toggle="collapse"
									data-target="#collapseOutputEs_1"> <input
									type="checkbox" name="saveToES"
									id="outputEsCheck_1" /> <strong>Save results to Elasticsearch</strong></label>
							</div>

							<div class="panel panel-default panel-collapse collapse"
								id="collapseOutputEs_1">
								<div class="panel-heading">
									<strong>Results will be saved to Elasticsearch</strong>
								</div>
								<div class="panel-body">
									<div class="row">
										<div class="form-group col-md-12">
											<div class="col-md-4">
												<label class="control-label" for="indexW">Index</label> <input
													name="indexW" type="text"
													placeholder="Elasticsearch index to save data"
													class="form-control input-md outputEs_1" required>
											</div>
											<div class="col-md-4">
												<label class="control-label" for="typeW">Type</label> <input
													name="typeW" type="text"
													placeholder="to save Frequent-Items"
													class="form-control input-md outputEs_1" required>
											</div>
										</div>
									</div>
								</div>
								<div class="panel-footer"></div>
							</div>

							<!-- Button submit -->
							<div class="form-group" style="padding-top: 5%">
								<div class="col-md-12">
									<a id="btnShowScheduler_2" class="btn btn-xm btn-primary pull-right" style="margin-left: 4px;">
										<span data-toggle="tooltip" data-placement="top" title="Schedule this job" class="fa fa-clock-o" style="font-size:16px"></span>
									</a>
									<button id="btnGenAsRules"
										class="btn btn-md btn-primary pull-right">Generate Rules</button>
								</div>
							</div>
						</form>
					</div>

					<div class="panel-footer">FP-Growth Algorithm</div>
				</div>

				<div id="visualizer_2" class="panel panel-main"
					style="display: none;">
					<div class="panel-heading">
						<strong>Visualization Results</strong>
					</div>

					<div id="responseViewer_2"></div>
				</div>
			</div>

			<!-- Matching table upload tab -->
			<div id="matchingTable" class="tab-pane fade">
				<h3>Upload file for matching from code to name.</h3><br>
				<p>Note that separator has to be specified before (by clicking to "Setting" at right top corner).</p>
				<form id="fileForm">
					<div class="form-group">
						<label for="file">Matching table file</label> <input type="file"
							name="file" /><br><br>
						<button id="btnUploadMatchingTbl" type="button"
							class="btn btn-md btn-info pull-left">Upload file</button>
						<br> <br>
					</div>

				</form>
			</div>
		</div>

		<!-- Using ajax here to get response -->
		<div id="loaderContainer">
			<div id="loader"></div>
		</div>
		
		<%@ include file="subPage-modalDeleteWarning.jsp"%>

		<!-- Footer -->
		<%@ include file="subPage-footer.jsp"%>

		<!-- Setting Page -->
		<%@ include file="subPage-modalSettingPage.jsp"%>
		
		<!-- Schedule page -->
		<%@ include file="subPage-modalScheduleJob.jsp"%>
	</div>
</body>
</html>