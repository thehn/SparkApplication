<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
<!-- all libraries -->
<%@ include file="subPage-libraries.jsp"%>
<%-- <%@ include file="subPage-common-css.jsp"%> --%>
<link rel="stylesheet" type="text/css" href="<spring:url value="/resources/css/commonStyle.css"/>">
<script src="<spring:url value="/resources/js/clustering-algorithm.js?6.0"/>" type="text/javascript"></script>
<script src="<spring:url value="/resources/js/cehCommon.js?6.0"/>" type="text/javascript"></script>


<style type="text/css">
	.customize-pre-scrollable{
		height: 27em;
		overflow-y: scroll;
	}
</style>

</head>

<body>
	
	<input style="display: none;" id="clusterAlgorithm" value="${clusterAlgorithm}">
	
	<!-- Navigation bar -->
	<%@ include file="subPage-navigationBar.jsp"%>

	<!-- Container -->
	<div class="container" id="container">
		<ul class="nav nav-tabs">
			<li class="active"><a data-toggle="tab" id="trainingTabPaneBtn" href="#trainingTab">New Model</a></li>
			<li><a data-toggle="tab" href="#predictionTab" id="linkPredictionTab">Cluster data</a></li>
		</ul>

		<div class="tab-content">
			<div id="trainingTab" class="tab-pane fade in active">
				<h2 class="algorithm-header">${clusterAlgorithm}</h2>
				<div class="panel panel-main">
					<div class="panel-heading">
						<strong>Creating new model</strong>
					</div>
					<div class="panel-body" id="ml_form">
						<div class="form-group col-md-12">

							<!-- Form data -->
							<form method="POST" id="algorithmSettingForm"
								modelAttribute="appProperties" onsubmit="return false">
								<input name="algorithm" value="${clusterAlgorithm}" style="display: none;">
								<input name="action" value="train" style="display: none;">
								<!-- Data input -->
								<h3 class="algorithm-header">Input settings</h3>
								<div class="panel panel-default">
									<div class="panel-heading">
										<strong>Data was saved to Elasticsearch</strong>
									</div>
									<div class="panel-body">
										<div class="form-group col-md-12">
												<select class="lookup-selection-box" name="listLookups" id="listLookups">
												</select>
										</div>
									</div>
									<div class="panel-footer"></div>
								</div>

								<!-- Algorithm Settings -->
								<h3 class="algorithm-header">Algorithm's Parameters</h3>
								<div class="panel panel-default">
									<div class="panel-heading">
										<strong>Parameters setting</strong>
									</div>
									<div class="panel-body">
										<div class="row">
											<div class="form-group col-md-12">
												<div class="col-md-4 kmeans kmediods" style="display: none;">
													<label class="control-label" for="numClasses">Number Clusters</label>
														<input name="numClusters" type="number" name="quantity" min="1" max="10"
														class="form-control input-md">
												</div>
												<div class="col-md-4 dbscan" style="display: none;">
													<label class="control-label" for="epsilon"  data-toggle="tooltip" title="Default value: 1.0">Epsilon</label>
														<input name="epsilon" type="number" name="quantity" min="0" max="1" step="0.01" placeholder="Optional"
														class="form-control input-md">
												</div>
												<div class="col-md-4 kmeans" style="display: none;">
													<label class="control-label" for="numIterations" data-toggle="tooltip" title="Default value: 10">Number Iterations</label>
														<input name="numIterations" type="number" placeholder="Optional"
														class="form-control input-md">
												</div>
												<div class="col-md-4">
													<label class="control-label" for="modelName" data-toggle="tooltip" title="If not choose, it will be saved as default model name.">Save
														Model As</label> <input id="modelName" name="modelName"
														type="text" placeholder="Optional, no space"
														class="form-control input-md">
												</div>
											</div>
										</div>
									</div>
									<div class="panel-footer"></div>
								</div>
								
								<div class="panel panel-default">
									<div class="panel-heading">
										<strong>Visualization setting</strong>
									</div>
									<div class="panel-body">
										<div class="row">
											<div class="form-group col-md-12">
												<div class="col-md-4">
													<label class="control-label" for="listLookups">X-Axis</label>
													<select name="xAxisField" id="xAxisField">
													</select>
												</div>
												<div class="col-md-4">
													<label class="control-label" for="listLookups">Y-Axis</label>
													<select name="yAxisField" id="yAxisField">
													</select>
												</div>
											</div>
											
										</div>
									</div>
									<div class="panel-footer"></div>
								</div>

								<!-- Data output -->
								<div style="display: none;">
									<h3>Output settings</h3>
									<div class="checkbox">
										<label class="checkbox-inline" data-toggle="collapse"
											data-target="#collapseOutputEs_1"> <input
											type="checkbox" name="saveToES"
											id="outputEsCheck_2" /> <strong>Save this Model</strong></label>
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
															name="indexW" type="text" value="model"
															class="form-control input-md outputEs_1" required readonly>
													</div>
													<div class="col-md-4">
														<label class="control-label" for="typeW">Type</label> <input
															name="typeW" type="text" id="typeW"
															placeholder="_type value"
															class="form-control input-md outputEs_2" required readonly>
													</div>
												</div>
											</div>
										</div>
										<div class="panel-footer"></div>
									</div>
								</div>
								
								<!-- Button submit -->
								<div class="form-group" style="padding-top: 5%">
									<div class="col-md-12">
										<a id="btnShowScheduler_1" class="btn btn-xm btn-primary pull-right" style="margin-left: 4px;'">
											<span data-toggle="tooltip" data-placement="top" title="Schedule this job" class="fa fa-clock-o" style="font-size:16px"></span>
										</a>
										<button id="brnTrainModel"
											class="btn btn-md btn-primary pull-right">Cluster</button>
									</div>
								</div>
							</form>
						</div>
					</div>
					<div class="panel-footer">Clustering Algorithm</div>
				</div>

				<div id="visualizer_1" class="panel panel-main"
					style="display: none;">
					<div class="panel-heading">
						<strong>Metrics Evaluation</strong>
					</div>
					<div class="panel-body">
						<div id="responseViewer_1"></div>
					</div>
					<div class="panel-footer"></div>
				</div>
			</div>
			
			<div id="predictionTab" class="tab-pane fade">
				<h2 class="algorithm-header">Clustering</h2>
				<div class="panel panel-main">
					<div class="panel-heading">
						<strong>Clustering request form</strong>
					</div>
					<div class="panel-body">
					
						<!-- Data input from File -->
						<h3>Data input settings</h3>
						<div class="panel panel-default">
							<div class="panel-heading">
								<strong>Data for clustering</strong>
							</div>
							<div class="panel-body">
								<form id="unlabeledFileForm">
									<div class="row">
										<div class="form-group col-md-12">
											<div class="col-md-6">
												<label class="control-label" for="file">Data File</label>
												<input type="file" id="inputFile"
													class="form-control input-md" name="file" />
											</div>
											<div class="col-md-2">
												<label class="control-label" for="indexR">Delimiter</label> <input
												id="dataDelimiter" type="text" name="tmpDelimiter"
												class="form-control input-md" required>
											</div>
											
										</div>
									</div>
									<div class="row">
										<div class="form-group col-md-12">
											<button id="btnUploadUnlabeledFile" type="button"
											class="btn btn-md btn-info pull-right" disabled>Upload file</button>
										</div>
									</div>
								</form>
							</div>
							<div class="panel-footer"></div>
						</div>
					
						<form method="POST" id="predictionForm"
							modelAttribute="appProperties" onsubmit="return false">
							<input name="algorithm" value="${clusterAlgorithm}" style="display: none;">
							<input name="action" value="predict" style="display: none;">
							<h3>Model choosing</h3>
							<div class="row">
								<div class="col-md-12 well">
									<div class="col-md-2">
										<div class="row">
											<div class="panel panel-default">
												<div class="panel-heading">
													<strong>Choose a model</strong>
												</div>
												<select
													class="selectpicker show-tick" size="20"
													style="width: 100%; height: 100%" id="listModels" name="modelName" required>
												</select>
												<div>
													<button type="button" class="btn btn-danger btn-sm center-block" style="width: 100%;" 
													id="deleteModel" disabled><span class="glyphicon glyphicon-trash"></span> Delete Model</button>
												</div>
											</div>
										</div>
									</div>
									<div class="col-md-10">
										<div class="row" style="height: 100%;">
											<div class="panel panel-default">
												<div class="panel-heading">
													<strong>Model evaluation metrics</strong>
												</div>
												<div class="customize-pre-scrollable">
													<div id="responseViewer_3"></div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
							
							<!-- Data input -->
							<!-- <div class="panel panel-default">
								<div class="panel-heading">
									<strong>Data input</strong> <small> (data was saved to
										Elasticsearch) </small>
								</div>
								<div class="panel-body">
									<div class="row">
										<div class="form-group col-md-12">
											<div class="col-md-4">
												<label class="control-label" for="indexR">Index</label> <input
													name="indexR" type="text"
													placeholder="_index value"
													class="form-control input-md" required>
											</div>
											<div class="col-md-4">
												<label class="control-label" for="typeR">Type</label> <input
													name="typeR" type="text"
													placeholder="_type value"
													class="form-control input-md" required>
											</div>
											<div class="col-md-4">
												<label class="control-label" for="fieldNameR">Source
												</label> <input name="fieldNameR" type="text"
													placeholder="the key under _source value"
													class="form-control input-md" required>
											</div>
										</div>
									</div>
								</div>
								<div class="panel-footer"></div>
							</div> -->

							<!-- Data output -->
							<h3>Output settings</h3>
							<div class="checkbox">
								<label class="checkbox-inline" data-toggle="collapse"
									data-target="#collapseOutputEs_2"> <input
									type="checkbox" name="saveToES"
									id="outputEsCheck_1" /> <strong>Save results to
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
													placeholder="_index value"
													class="form-control input-md outputEs_2" required>
											</div>
											<div class="col-md-4">
												<label class="control-label" for="typeW">Type</label> <input
													name="typeW" type="text"
													placeholder="_type value"
													class="form-control input-md outputEs_2" required>
											</div>
										</div>
									</div>
								</div>
								<div class="panel-footer"></div>
							</div>

							<!-- hidden input (file name full path of data file) -->
							<div style="display: none;">
								<input name="fileName" id="uploadedFileName">
							</div>
							<!-- hidden input (delimiter) -->
							<div style="display: none;">
								<input name="delimiter" id="delimiter">
							</div>
							
							<!-- Button submit -->
							<div class="form-group" style="padding-top: 5%">
								<div class="col-md-12">
									<a id="btnShowScheduler_2" class="btn btn-xm btn-primary pull-right" disabled="true" style="margin-left: 4px;">
										<span data-toggle="tooltip" data-placement="top" title="Schedule this job" class="fa fa-clock-o" style="font-size:16px"></span>
									</a>
									<button id="btnPredict"
										class="btn btn-md btn-primary pull-right" disabled>Clustering</button>
								</div>
							</div>
						</form>
					</div>

					<div class="panel-footer">Random Forest Classification</div>
				</div>

				<div id="visualizer_2" class="panel panel-main"
					style="display: none;">
					<div class="panel-heading">
						<strong>Visualization Results</strong>
					</div>
					<div id="responseViewer_2"></div>
					<div class="panel-footer"></div>
				</div>
			</div>
		</div>

		<!-- Using ajax here to get response -->
		<div id="loaderContainer">
			<div id="loader"></div>
		</div>
		
		<!-- show modal warning when delete model -->
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