/**
 * Global variables
 */
var algorithmName = "";

/**
 * to send request for updating setting from web
 * 
 * @param requestUrl
 * @param dataFormId
 * @param responseId
 * @returns
 */
function requestRunAlgorithm(requestUrl, dataFormId, responseId) {
	$.ajax({
		// async : false,
		type : "POST",
		url : requestUrl,
		data : $(dataFormId).serialize(),
		beforeSend : function() {
			// alert("Send request ...");
			$('#loaderContainer').show();
		}
	}).done(function(response) {
		$('#loaderContainer').hide();
		$(responseId).html(response);
		// $("#responseViewer").css('display', 'block');
	}).fail(function() {
		console.log("error");
	}).always(function() {
		$('#loaderContainer').hide();
	});
	return false;
}

/**
 * to send request for schedule running setting from web
 * 
 * @param requestUrl
 * @param dataFormId
 * @param responseId
 * @returns
 */
function requestScheduleAlgorithm(requestUrl, dataFormId, scheduleFormId) {
	var data = '#' + dataFormId + ',' + '#' + scheduleFormId;
	$.ajax({
		type : "POST",
		url : requestUrl,
		data : $(data).serialize(),
		beforeSend : function() {
			//$('#loaderContainer').show();
		}
	}).done(function(response) {
		//$('#loaderContainer').hide();
		alert(response);
	}).fail(function() {
		console.log("error");
	}).always(function() {
		//$('#loaderContainer').hide();
	});
	return false;
}

/**
 * to send request for updating setting from web
 * 
 * @param requestUrl
 * @param dataFormId
 * @param responseId
 * @returns
 */
function requestModelInfo(requestUrl, dataFormId, responseId) {
	$.ajax({
		type : "POST",
		url : requestUrl,
		data : $(dataFormId).serialize(),
		beforeSend : function() {
			$(responseId).html("Please wait ...");
			$(responseId).css('text-align','center');
			$(responseId).css('vertical-align','middle');
		}
	}).done(function(response) {
		// $('#loaderContainer').hide();
		$(responseId).html(response);
		// $("#responseViewer").css('display', 'block');
	}).fail(function() {
		console.log("error");
	}).always(function() {
		$('#loaderContainer').hide();
	});
	return false;
}

/**
 * to update settings from Modal viewer
 * 
 * @returns
 */
function updateConfig(settingsFormId) {
	$.ajax({
		// async : false,
		type : "POST",
		url : "updateConfig",
		data : $(settingsForm).serialize(),
		success : function(response) {
			$("#responseViewer").html(response);
		}
	});
	return false;
}

/**
 * to upload a file into specified url
 * @param url
 * @param fileForm
 * @returns
 */
function uploadSingleFile(url, fileFormID){
	
	$.ajax({
		url : url,
		type : "POST",
		data : new FormData(document.getElementById(fileForm)),
		enctype : 'multipart/form-data',
		processData : false,
		contentType : false
	}).done(function(data) {
		alert("File upload Success!");
	}).fail(function(jqXHR, textStatus) {
		alert(jqXHR.responseText);
		alert('File upload failed.');
	});	
	
	return false;
}

/**
 * to fill data from an array to specified select form
 * 
 * @param arrayOptions
 * @param selectFormId
 * @returns
 */
function fillOptionsForSelectForm(arrayOptions, selectFormId){

	var sel = document.getElementById(selectFormId);
	
	// first clear all old options
	sel.options.length = 0;
	
	// fill new options
	for(var i = 0; i < arrayOptions.length; i++) {
		var opt = document.createElement('option');
		opt.innerHTML = arrayOptions[i];
		opt.value = arrayOptions[i];
		sel.appendChild(opt);
	}
}

/**
 * to get data with POST method, by passing JSON request
 * 
 * @param url
 * @param jsonRequest
 * @param responseId
 * @returns
 */
function postAjaxRequestWithJson(url){
	$.ajax({
		type : "GET",
		url : url,
		success : function(data) {
			alert(data);
		},
		failure : function(errMsg) {
			alert(errMsg);
		}
	});
}

/**
 * to list all trained models
 * 
 * @param algorithmName
 * @returns
 */
function listModels(algorithmName){
	$.ajax({
		type : "GET",
		url : "getListModels/" + algorithmName,
		success : function(data) {
			fillOptionsForSelectForm(data, "listModels");
		},
		failure : function(errMsg) {
			alert(errMsg);
		}
	});	
}

/**
 * update selection fields for predicting
 */
function updateSelectionFieldsForPredict(header, delimiter){
	var columnsList = header.split(delimiter);
	var select2DataForSelectionBox = [];
	var field;
	for (field of columnsList){
		var element = {id: field, text: field};
		select2DataForSelectionBox.push(element);
	}
	$('#fieldsForPredict').empty();
	$('#fieldsForPredict').select2({width: '100%', data: select2DataForSelectionBox, multiple: true});
}

/**
 * when document ready
 * @returns
 */
$(document).ready(function(){
	$('[data-toggle="tooltip"]').tooltip();
	
	// #PC0018 - Start
	$.fn.bootstrapSwitch.defaults.size = 'mini';
	$.fn.bootstrapSwitch.defaults.onColor = 'success';
	$.fn.bootstrapSwitch.defaults.offColor = 'warning';
	$.fn.bootstrapSwitch.defaults.onText = 'Yes';
	$.fn.bootstrapSwitch.defaults.offText = 'No';
	$.fn.bootstrapSwitch.defaults.onSwitchChange = function(event, state) {
		$('#featureSelectionCheckBox').val(state);
		if (state == true) {
			$('#collapseFeatureSelection').collapse('show');
			$('.feature-selection-input').prop('required', true);
		} else {
			$('#collapseFeatureSelection').collapse('hide');
			$('.feature-selection-input').removeAttr('required');
		}
	};
	$(".bootstrap-switch-btn").bootstrapSwitch();
	// #PC0018 - End
	
	/**********************************************************************************************************
	 * This section using for lookup data, class & fields selection
	 **********************************************************************************************************/
	// for select2
	$.get("getListLookupDefs", function(response){
		$('#listLookups').select2({width: '100%', data: response, multiple: true, maximumSelectionLength: 1});
		$('#classCol').select2({width: '100%', multiple: true, maximumSelectionLength: 1});
		$('#featureCols').select2({width: '100%', multiple: true});
	});
	
	/*
	 * to get all header fields
	 */
	var header = [];
	var featureFields = [];
	$('#listLookups').change(function(){
		var indexOfLookup = $(this).val();
		var url = 'getHeaderFields/' + indexOfLookup;
		$('#classCol').empty();
		$('#featureCols').empty();
		$.get(url, function(response){
			header = response;
			$('#classCol').select2({width: '100%', data: response, multiple: true, maximumSelectionLength: 1});
		});
	});
	/*
	 * to get all feature fields
	 */
	$('#classCol').change(function(){
		var classField = $(this).val();
		// copy values from header
		featureFields = header.slice();
		// remove classed-field
		for(var index = 0; index < featureFields.length; index++){
			if(featureFields[index].id == classField){
				featureFields.splice(index, 1);
				break;
			}else{
				continue;
			}
		}
		$('#featureCols').empty();
		$('#featureCols').select2({width: '100%', data: featureFields, multiple: true});
	});
	
	$('#featureCols').change(function(){
		if(algorithmName == 'MLPClassifier'){
			var count = $("#featureCols :selected").length;
			$('#numFeatures').val(count);
		}else{
			// do nothing
		};
	});
	
	// Select all
	$("#selectAll").click(function(){
		$("#featureCols > option").prop("selected","selected");
        $("#featureCols").trigger("change");
	});
	// Clear all
	$("#clearAll").click(function(){
		$('#featureCols').val(null).trigger("change"); 
        $("#featureCols").trigger("change");
	});
	
	// #P00005 - Start
	/*
	// Select all fields for predict
	$("#selectAllFieldsForPredict").click(function(){
		$("#fieldsForPredict > option").prop("selected","selected");
        $("#fieldsForPredict").trigger("change");
	});
	// Clear all fields for predict
	$("#clearAllFieldsForPredict").click(function(){
		$('#fieldsForPredict').val(null).trigger("change"); 
        $("#fieldsForPredict").trigger("change");
	});*/
	// #P00005 - End
	
	/**********************************************************************************************************
	 * End section
	 **********************************************************************************************************/

});