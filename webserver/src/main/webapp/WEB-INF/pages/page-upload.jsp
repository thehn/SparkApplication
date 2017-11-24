<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>File upload</title>

<!-- all libraries -->
<%@ include file="subPage-libraries.jsp"%>
<%-- <%@ include file="subPage-common-css.jsp"%> --%>
<link rel="stylesheet" type="text/css" href="<spring:url value="/resources/css/commonStyle.css"/>">
<script src="<spring:url value="/resources/js/upload.js?4.0"/>" type="text/javascript"></script>
</head>

<body>

	<!-- Navigation bar -->
	<%@ include file="subPage-navigationBar.jsp"%>

	<!-- Container -->
	<div class="container" id="container">

		<ul class="nav nav-tabs">
			<li class="active"><a data-toggle="tab" href="#uploadTab">Upload Data</a></li>
			<li><a data-toggle="tab" href="#indexToEsTab">Index Data</a></li>
			<li><a data-toggle="tab" href="#lookupData" id="lookupTab">Data Lookup</a></li>
		</ul>

		<div class="tab-content">
			<div id="uploadTab" class="tab-pane fade in active">
				<h2 class="algorithm-header">Upload Data File</h2>
				<!-- <p>for indexing data into Elasticsearch.</p> -->
				<div class="panel panel-main">
					<div class="panel-heading">
						<strong>Upload Form</strong>
					</div>
					<div class="panel-body">
						<div class="panel panel-default">
							<div class="panel-heading">
								Upload file. Choose one or multiple files to upload to server (Spark Rest-API server)
							</div>
							<div class="panel-body">
								<!-- Progress Bar -->
								<div class="progress" id='progressBar'>
									<div id='bar' class="progress-bar" role="progressbar"
										aria-valuenow="1" aria-valuemin="0" aria-valuemax="100"
										style="width: 0%;"></div>
								</div>

								<div style="width: 100%">

									<form style="margin-bottom: 20px">
										<input type="file" id="files" multiple
											style="margin-bottom: 20px" /><br />
											
										<div class="form-group col-md-12">
											<div class="row well">
												<label >Selected files <span class="badge" id="totalSelected"></span></label>
												<div class="list-group">
													<output id="selectedFiles"></output>
												</div>
											</div>
											<div class="row well">
												<label >Uploaded files <span class="badge" id="totalUploadedFiles"></span></label>
												<div class="list-group">
													<output id="uploadedFiles"></output>
												</div>
											</div>
										</div>
										
										<input id="uploadButton" type="button" value="Upload files"
											class="btn btn-md btn-primary pull-right"
											style="margin-top: 20px" disabled="true" />
									</form>
								</div>
								
								
							</div>
							<div class="panel-footer"></div>
						</div>
					</div>
					<div class="panel-footer">File upload</div>
				</div>
			</div>
			<div id="indexToEsTab" class="tab-pane fade">
				<h2 class="algorithm-header">Index data to Elasticsearch</h2>
				<!-- <p>Choose uploaded file, specify parameters then index to Elasticsearch.</p> -->
				<div class="panel panel-main">
					<div class="panel-heading"> 
						Specify the options bellow (Note: These settings will be applied for all files uploaded)
					</div>
					<div class="panel-body">
						<form id="fieldOptionForm" onsubmit="return requestForIndex()"
							modelAttribute="indexEsInfo">
								<div class="row">
									<div class="form-group col-md-12">
										<div class="col-md-4">
											<label for="listUploadedFiles">Choose uploaded file(s)</label>
											<select name="listUploadedFiles" id="listUploadedFiles" multiple style="width: 100%"></select>
										</div>
										<div class="col-md-4">
											<label for="format">Data format</label> <select id="format"
												name="format" class="form-control" required>
												<option value="">Please select one</option>
												<!-- <option value="BASKET" onselect=>Basket data (for frequent pattern mining)</option> -->
												<!-- <option value="DENSE" onselect=>Labeled data
													(DENSE-format)</option> -->
												<!-- <option value="SPARSE">Classification
													(SPARSE-format)</option> -->
													<option value="CSV">CSV</option>
											</select>
										</div>
										<!-- For Classification format -->
										<!-- <div class="col-md-4 dense-only"
											style="display: none;">
											<label for="labeledIndex">Index of labeled field</label>
											<input name="labeledIndex" type="number" value="0"
												class="form-control input-md" required>
										</div> -->
									</div>
								</div>
								<div class="row">
									<div class="form-group col-md-12">
										<div class="col-md-4 sparse-only"
											style="display: none;">
											<label for="numberFeatures">Number of features</label>
											<input name="numberFeatures" type="number" value="0"
												class="form-control input-md" required>
										</div>
										<!-- For Basket format -->
										<div class="col-md-4 basket-only" style="display: none;">
											<label for="field-selection">Index of <strong>ID</strong>
												column
											</label> <input name="indexOfColumnID" type="number" value="0"
												placeholder="example: 0" class="form-control input-md" required>

										</div>
										<div class="col-md-4 basket-only" style="display: none;">
											<label for="field-selection">Index of <strong>Category</strong>
												column
											</label> <input name="indexOfColumnCategory" type="number" value="1"
												placeholder="example 1" class="form-control input-md" required>

										</div>
										<!-- <div class="col-md-4">
											<label for="delimiter">Delimiter</label>
											<input id="delimiter" name="delimiter" class="form-control" type="text" required></input>
										</div> -->
									</div>
								</div>

								<div class="row">
									<div class="form-group col-md-12">
										<div class="col-md-4">
											<label for="field-selection">Index</label> <input
												name="indexW" type="text"
												placeholder="_index to save to Elasticsearch"
												class="form-control input-md" required>
										</div>
										<div class="col-md-4">
											<label for="field-selection">Type</label> <input
												name="typeW" type="text"
												placeholder="_type to save to Elasticsearch"
												class="form-control input-md" required>
										</div>
										<div class="col-md-4">
											<label for="delimiter">Delimiter</label>
											<input id="delimiter" name="delimiter" class="form-control" type="text" required></input>
										</div>
										<!-- <div class="col-md-4">
											<label for="field-selection">Source</label> <input
												name="sourceName" type="text"
												placeholder="_souce name to save to Elasticsearch"
												class="form-control input-md" required>
										</div> -->
									</div>
								</div>
								<div class="row">
									<div class="form-group col-md-12">
										<div class="col-md-4">
											<div class="checkbox">
												<label class="checkbox-inline" > <input
													type="checkbox" checked="true" name="clearOldData"
													/> <strong>Delete old data</strong></label>
											</div>
										</div>
									</div>
								</div>

								<input id="indexButton" value="Start index" type="submit"
									class="btn btn-md btn-primary pull-right"
									style="margin-top: 1%" disabled="true" />
						</form>
					</div>
					<div class="panel-footer"></div>
				</div>
			</div>
			
			<!-- Lookup tab -->
			<div id="lookupData" class="tab-pane fade">
				<h2 class="algorithm-header">Data Lookup definition</h2>
				<p>Setting lookup for data from Elasticsearch, to use as input for algorithms.</p>
				
				<div>
					<button type="button" class="btn btn-primary pull-left" id="btnAddNewLookupDef">Add new</button>
				</div>
				
				<table class="table .table-hover table-responsive .table-striped" id="lookupTbl">
					<thead>
						<tr>
							<th>Lookup name</th>
							<th>Index</th>
							<th>Type</th>
							<th>Source</th>
							<th>Delimiter</th>
							<th>Index Of Labeled Column</th>
							<th>Edit</th>
							<th>Delete</th>
						</tr>
					</thead>
					<tbody></tbody>
				</table>
			</div>
		</div>

		<div id="responseView" style="margin-bottom: 6%"></div>

	</div>
	
	<!-- Modal for edit button -->
	<div class="modal fade" id="editLookupDef" tabindex="-1"
		role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span><span class="sr-only">Close</span>
					</button>
					<h3 class="modal-title">Lookup Definition</h3>
				</div>
				<div class="modal-body">
	
					<!-- all settings go here -->
					<form id="lookupSettingsForm" onsubmit="return false"
						modelAttribute="lookupSettings">
						
						<input type="text" name="action" required style="display: none;"></input>
						
						<div class="form-group has-feedback" id="lookupName">
							<label class="control-label" for="lookupName">Lookup Name (Unique)</label>
							<input type="text" class="form-control" name="lookupName" required>
						</div>
						<div class="form-group">
							<label class="control-label" for="index">Index</label>
							<input name="index" class="form-control" required></input>
						</div>
						<div class="form-group">
							<label class="control-label" for="type">Type</label>
							<input name="type" class="form-control" required></input>
						</div>
						<div class="form-group">
							<label class="control-label" for="source">Source</label>
							<input name="source" class="form-control" required></input>
						</div>
						<div class="form-group">
							<label class="control-label" for="delimiter">Delimiter</label>
							<input name="delimiter" class="form-control" required></input>
						</div>
						<div class="form-group">
							<label class="control-label" for="indexOfLabeledField">Index Of Labeled Column</label>
							<input type="number" min="0" name="indexOfLabeledField" class="form-control"></input>
						</div>
						
					</form>
	
				</div>
				<div class="modal-footer">
					<div class="btn-group btn-group-justified" role="group"
						aria-label="group button">
						<div class="btn-group" role="group">
							<button type="button" class="btn btn-md btn-primary pull-right"
								id="btnSubmitLookup" role="button">Submit</button>
						</div>
					</div>
				</div>	
			</div>
		</div>
	</div>
	
	<!-- Using ajax here to get response -->
	<div id="loaderContainer">
		<div id="loader"></div>
	</div>
	
	<!-- Modal for delete button -->
	<div class="modal fade" id="deleteLookupDef" tabindex="-1" role="dialog"
		aria-labelledby="edit" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">
						<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
					</button>
					<h4 class="modal-title custom_align" id="Heading">Delete this
						lookup definition?</h4>
				</div>
				<div class="modal-body">
					<div class="alert alert-danger">
						<span class="glyphicon glyphicon-warning-sign"></span> Are you
						sure you want to delete this lookup?
					</div>
				</div>
				<div class="modal-footer ">
					<button type="button" class="btn btn-primary" id="btnDelete">
						<span class="glyphicon glyphicon-ok-sign"></span> Yes
					</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">
						<span class="glyphicon glyphicon-remove"></span> No
					</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /container -->

	<!-- Footer -->
	<%@ include file="subPage-footer.jsp"%>

	<!-- Setting Page -->
	<%@ include file="subPage-modalSettingPage.jsp"%>
</body>
</html>