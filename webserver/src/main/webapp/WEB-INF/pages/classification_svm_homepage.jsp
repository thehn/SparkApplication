<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Classification - SVM</title>

<!-- all libraries -->
<%@ include file="subPage-libraries.jsp"%>
<%-- <%@ include file="subPage-common-css.jsp"%> --%>
<link rel="stylesheet" type="text/css" href="<spring:url value="/resources/css/commonStyle.css"/>">
<script src='<spring:url value="/resources/js/svm-algorithm.js?6.0"/>' type="text/javascript"></script>
<script src='<spring:url value="/resources/js/cehCommon.js?6.0"/>' type="text/javascript"></script>

<style type="text/css">
	.customize-pre-scrollable{
		height: 26em;
		overflow-y: scroll;
	}
</style>

</head>

<body>

	<!-- Navigation bar -->
	<%@ include file="subPage-navigationBar.jsp"%>

	<!-- Container -->
	<div class="container" id="container">
		<ul class="nav nav-tabs">
			<li class="active"><a data-toggle="tab" href="#trainingTab" id="trainingTabPaneBtn">Model Generation</a></li>
			<li><a data-toggle="tab" href="#predictionTab" id="linkPredictionTab">Failure Diagnostics</a></li>
		</ul>

		<div class="tab-content">
			<div id="trainingTab" class="tab-pane active in">
				<h2 class="algorithm-header">Linear Support Vector Machine (SVM)</h2>
				<p></p>
				<div class="panel panel-main">
					<div class="panel-heading">
						<strong>Training new model</strong>
					</div>
					<div class="panel-body" id="ml_form">
						<div class="form-group col-md-12">

							<!-- Form data -->
							<form method="POST" id="algorithmSettingForm"
								modelAttribute="appProperties" onsubmit="return false">
								<input name="algorithm" value="SvmClassifier" style="display: none;">
								<input name="action" value="train" style="display: none;">
								<!-- Data input -->
								<h3 class="algorithm-header">Data input settings</h3>
								<div class="panel panel-default">
									<div class="panel-heading">
									</div>
									<div class="panel-body">
										<div class="row">
											<div class="form-group col-md-12">
												<div class="form-group col-md-3">
													<label class="control-label" for="listLookups">Training Dataset:</label>
													<select name="listLookups" id="listLookups">
													</select>
												</div>
												<div class="form-group col-md-3">
													<label class="control-label" for="classCol">Class Field:</label>
													<select name="classCol" id="classCol">
													</select>
												</div>
												<div class="form-group col-md-6">
													<label class="control-label" for="featureCols">Feature Fields:</label>
													<button type="button" class="btn btn-success btn-xs" id="selectAll">Select all</button>
													<button type="button" class="btn btn-warning btn-xs" id="clearAll">Clear all</button>
													<select multiple name="featureCols" id="featureCols">
													</select>
												</div>
											</div>
										</div>
									</div>
									<div class="panel-footer"></div>
								</div>
								
								<!-- #PC0016 - Start -->
								<!-- Data Pre-Processing option -->
								<h3 class="algorithm-header">Pre-Processing</h3>
								<div class="checkbox">
									<label class="checkbox-inline">
										<strong>Features Selection: </strong>
										<input type="checkbox" name="featuresSelectionEnabelFlg" id="featureSelectionCheckBox" class="bootstrap-switch-btn" value="false"/>
									</label>
								</div>
								<div class="panel panel-default panel-collapse collapse"
									id="collapseFeatureSelection">
									<div class="panel-heading">
									</div>
									<div class="panel-body">
										<div class="row">
											<div class="form-group col-md-12">
												<div class="col-md-3">
													<label class="control-label" for="numberFeatures" data-toggle="tooltip" title="Choose amount of feature will be filtered.">Number Features</label>
													<input name="numberFeatures" type="number" min="1" class="form-control input-md feature-selection-input">
												</div>
												<div class="col-md-3">
													<label class="control-label" for="bin" data-toggle="tooltip" 
													title="Number of bins for discretization data. Since ChiSqSelector requires categorical features, although features are doubles, 
													the ChiSqSelector treats each unique value as a category. Default: 1">Size of bin</label> 
														<input name="bin" type="number" min="1" class="form-control input-md">
												</div>
											</div>
										</div>
									</div>
									<div class="panel-footer"></div>
								</div>
								<!-- #PC0016 - End -->
								
								<!-- Algorithm Settings -->
								<h3 class="algorithm-header">Parameters setting</h3>
								<div class="panel panel-default">
									<div class="panel-heading">
									</div>
									<div class="panel-body">
										<div class="row">
											<div class="form-group col-md-12">
												<div class="col-md-4">
													<label class="control-label" for="fraction">Split for
														training / test</label> <input type="range" id="rangeData"
														value="80" min="0" max="100" step="5" defaultValue="80"
														name="fraction"> <span>Ratio: <span
														id="range_value">80 / Non-split</span>
													</span>
												</div>
												<div class="col-md-4">
													<label class="control-label" for="numIterations" data-toggle="tooltip" title="Number iteration of training. Default value: 10">
														Number iteration</label> <input name="numIterations" type="number" min="0"
														placeholder="Optional"
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
															placeholder="type will be saved as model name"
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
										<a id="btnShowScheduler_1" class="btn btn-xm btn-primary pull-right" style="margin-left: 2px">
											<span data-toggle="tooltip" data-placement="top" title="Schedule this job" class="fa fa-clock-o" style="font-size:16px"></span>
										</a>
										<button id="brnTrainModel"
											class="btn btn-md btn-primary pull-right" style="margin-right: 2px">Train model</button>
									</div>
								</div>
							</form>
						</div>
					</div>
					<div class="panel-footer">SVM-Classification</div>
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
				<h2 class="algorithm-header">Class Prediction</h2>
				<div class="panel panel-main">
					<div class="panel-heading">
						<strong>Linear Support Vector Machine - Predicting</strong>
					</div>
					<div class="panel-body">
						<!-- Option: Choose data from File or ElasticSearch-->
						
						<!-- Data input from File -->
						<h3 class="algorithm-header">Data input settings</h3>
						<div class="panel panel-default">
							<div class="panel-heading">
							</div>
							<div class="panel-body">
								<form id="unlabeledFileForm">
									<div class="row">
										<div class="form-group col-md-12">
											<div class="col-md-6">
												<label class="control-label" for="file">No-operation data</label>
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
							<input name="algorithm" value="SvmClassifier" style="display: none;">
							<input name="action" value="predict" style="display: none;">
							<input name="fieldsForPredict" style="display: none;" id='fieldsForPredict'> <!-- #PC0005 -->
							<!-- Data input from ElasticSearch -->
							<!-- <div class="panel panel-default">
								<div class="panel-heading">
									<strong>Data from Elasticsearch</strong>
								</div>
								<div class="panel-body">
									<div class="row">
										<div class="form-group col-md-12">
											<div class="col-md-4">
												<label class="control-label" for="indexR">Index</label> <input
													name="indexR" type="text"
													placeholder="Elasticsearch index to get data"
													class="form-control input-md" required>
											</div>
											<div class="col-md-4">
												<label class="control-label" for="typeR">Type</label> <input
													name="typeR" type="text"
													placeholder="Elasticsearch type to get data"
													class="form-control input-md" required>
											</div>
											<div class="col-md-4">
												<label class="control-label" for="fieldNameR">Source
												</label> <input name="fieldNameR" type="text"
													placeholder="Elasticsearch field name to get data"
													class="form-control input-md" required>
											</div>
										</div>
									</div>
								</div>
								<div class="panel-footer"></div>
							</div> -->
							
							<!-- Choose model -->
							<h3 class="algorithm-header">Model selection</h3>
							<div class="row">
								<div class="col-md-12" >
									<div class="col-md-2">
										<div class="row">
										<div class="panel panel-default" style="margin-right: 5px;">
											<div class="panel-heading">
												<strong>Models list</strong>
											</div>
											<select
												class="selectpicker show-tick"
												size="9" style="width: 100%;" id="listModels" name="modelName" required>
											</select>
											<div>
												<button type="button" class="btn btn-danger btn-sm center-block" style="width: 100%;" 
												id="deleteModel" disabled><span class="glyphicon glyphicon-trash"></span> Delete Model</button>
											</div>
										</div>
										
										</div>
										<div class="row">
											<div class="panel panel-default" style="margin-right: 5px">
												<div class="panel-heading">
													<strong>Choose a threshold</strong>
												</div>
													<select size="6" style="width: 100%;" id="listThresholds" name="threshold">
													</select>
											</div>
										</div>
									</div>
									<div class="col-md-10">
										<div class="row" style="height: 80%;">
											<div class="panel panel-default" style="margin-left: 5px;">
												<div class="panel-heading">
													<strong>Model evaluation indexes</strong>
												</div>
												<div class="customize-pre-scrollable">
													<div id="responseViewer_3"></div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>

							<!-- Data output -->
							<div style="display: none;">
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
									<a id="btnShowScheduler_2" class="btn btn-xm btn-primary pull-right" disabled="true" style="margin-left: 4px">
										<span data-toggle="tooltip" data-placement="top" title="Schedule this job" class="fa fa-clock-o" style="font-size:16px"></span>
									</a>
									<button id="btnPredict"
										class="btn btn-md btn-primary pull-right" disabled>Predict</button>
								</div>
							</div>
						</form>
					</div>

					<div class="panel-footer">SVM-Classification</div>
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