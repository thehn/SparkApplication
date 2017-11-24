/**
 * this is Javascript source file for FP-Growth page
 */

// variable
var requestUploadMatchingTable = "uploadMatchingTable";
var requestListModels = "getListFpGrowthModels";
var requestDeleteModel = "deleteModel/FpGrowth/";
var requestUrl = "request-FpGrowth";
var requestAsRulesUrl = "request-asRules";
var requestScheduleUrl ="schedule-fpGrowth";
var isTrainingTab = true;
var isAsRulesTab = false;
/*var algorithmName = "FpGrowth";*/ // #PC0012
	
/*$(document).ready(function() {
	$('[data-toggle="tooltip"]').tooltip();
});*/ // redundant

$(function() {
	
	// update algorithm name
	algorithmName = "FpGrowth"; // #PC0012
	
	// activate tab name
	$("ul").find("li").removeClass("active");
	$("#patternMiningNav").addClass("active");

	// for "output-ES" panel
	$('#outputEsCheck_1').change(function() {
		if ($(this).prop('checked') == true) {
			$('.outputEs_1').prop('required', true);
		} else {
			$('.outputEs_1').removeAttr('required');
		}
	});
	$('#outputEsCheck_2').change(function() {
		if ($(this).prop('checked') == true) {
			$('.outputEs_2').prop('required', true);
		} else {
			$('.outputEs_2').removeAttr('required');
		}
	});

	/* Button "run program" click */
	$("#btnTrainModel").click(
			function() {
				if ($('#algorithmSettingForm').valid()) {
					requestRunAlgorithm(requestUrl, algorithmSettingForm,
							responseViewer_1);
					$('#visualizer_1').show();
				} else {
					var validator = $("#algorithmSettingForm").validate();
					$('.error').css("color", "red");
					validator.showErrors();
				}

			});

	// btnGenAsRules
	/* Button "run program" click */
	$("#btnGenAsRules").click(
			function() {
				if ($('#requestAsRulesForm').valid()) {
					requestRunAlgorithm(requestAsRulesUrl, requestAsRulesForm,
							responseViewer_2);
					$('#visualizer_2').show();
				} else {
					var validator = $("#requestAsRulesForm").validate();
					$('.error').css("color", "red");
					validator.showErrors();
				}
			});

	/* button upload matching table */
	$('#btnUploadMatchingTbl').on('click', function() {

		$.ajax({
			url : requestUploadMatchingTable,
			type : "POST",
			data : new FormData(document.getElementById("fileForm")),
			enctype : 'multipart/form-data',
			processData : false,
			contentType : false
		}).done(function(data) {
			alert("File upload Success!");
		}).fail(function(jqXHR, textStatus) {
			alert(jqXHR.responseText);
			alert('File upload failed.');
		});
	});

	// when click to Association Rules tab
	$('#asRulesTabPaneBtn').click(function() {
		isTrainingTab = false;
		isAsRulesTab = true;
		
		// get list of models
		listModels(algorithmName);
		
		// validate to active / deactivate button delete model
		if ($("#listModels option:selected").length) {
			$('#deleteModel').removeAttr('disabled');
		} else {
			$('#deleteModel').prop('disabled', true);
		}
	});
	
	$('#listModels').change(function(){
		// validate to active / deactivate button delete model
		if ($("#listModels option:selected").length) {
			$('#deleteModel').removeAttr('disabled');
		} else {
			$('#deleteModel').prop('disabled', true);
		}
	});
	
	// when click to training tab
	$('#trainingTabPaneBtn').click(function() {
		isTrainingTab = true;
		isAsRulesTab = false;
	});
	
	// button delete click, open warning modal
	$('#deleteModel').click(function() {
		$('#modelDelete').modal('toggle');
	});

	// delete selected model
	$('#btnYes').click(function() {
		var selectedModelName = $('option:selected', $('#listModels')).val();
		var url = requestDeleteModel + selectedModelName;
		$.get(url, function(data, status) {
			if (data == 'true'){
				$('option:selected', $('#listModels')).remove();
				$('#modelDelete').modal('toggle');
			}else{
				alert(data);
			}
			
			// validate to active / deactivate button delete model
			if ($("#listModels option:selected").length) {
				$('#deleteModel').removeAttr('disabled');
			} else {
				$('#deleteModel').prop('disabled', true);
			}
		});
	});

	// to show Scheduler-modal
	$('#btnShowScheduler_1, #btnShowScheduler_2').click(function() {
		if ($(this).attr('disabled')) {
			return false;
		} else {
			// continue snippet bellow
		}
		var dataForm;
		if(isTrainingTab){
			dataForm = '#algorithmSettingForm';
		}else if(isAsRulesTab){
			dataForm = '#requestAsRulesForm';
		};
		if ($(dataForm).valid()) {
			$('#scheduler').modal('toggle');
		} else {
			var validator = $(dataForm).validate();
			$('.error').css("color", "red");
			validator.showErrors();
		}
	});
});

// to send the request for scheduling job
function requestSchedule() {
	var dataForm;
	if(isTrainingTab){
		dataForm = "algorithmSettingForm";
	}else if(isAsRulesTab){
		dataForm = "requestAsRulesForm";
	};
	requestScheduleAlgorithm(requestScheduleUrl, dataForm, "scheduleSettingsForm");
}