var requestTrainUrl = "request-FSChisqTrain";
var requestScheduleUrl ="schedule-fsChisq";
var requestModelDetail = "request-ModelDetail/";
var requestDeleteModel = "deleteModel/";
/*var algorithmName ="";*/ // #PC0012 

$(document).ready(function() {
	algorithmName = $('#algorithmName').val();
	document.title = algorithmName;
	requestModelDetail = requestModelDetail + algorithmName;
	requestDeleteModel = requestDeleteModel + algorithmName + "/";
});


$(function() {
	// activate tab name
	$("ul").find("li").removeClass("active");
	$("#featuresSelNav").addClass("active");

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
	$("#brnTrainModel").click(
			function() {
				if ($('#algorithmSettingForm').valid()) {
					requestRunAlgorithm(requestTrainUrl, algorithmSettingForm,
							responseViewer_1);
					$('#visualizer_1').show();
				} else {
					var validator = $("#algorithmSettingForm").validate();
					$('.error').css("color", "red");
					validator.showErrors();
				}

			});

	// to set Elasticsearch _type to be the same with modelName
	$('#modelName').change(function() {
		var modelNameVal = $('#modelName').val();
		$('#typeW').val(modelNameVal);
	});

	// event when selected model name is changed
	$('#listModels').change(function(){
		requestModelInfo(requestModelDetail, predictionForm, responseViewer_3);
		$('#deleteModel').removeAttr('disabled');
	});
	
	// when click to prediction tab
	$('#linkPredictionTab').click(function(){
		listModels(algorithmName);	
	});

	/// button delete click, open warning modal
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
	$('#btnShowScheduler_1').click(function() {
		if ($(this).attr('disabled')) {
			return false;
		} else {
			// continue snippet bellow
		}
		var dataForm = '#algorithmSettingForm';
		if ($(dataForm).valid()) {
			$('#scheduler').modal('toggle');
		} else {
			var validator = $(dataForm).validate();
			$('.error').css("color", "red");
			validator.showErrors();
		}
	});
});

//to send the request for scheduling job
function requestSchedule() {
	var dataForm;
	dataForm = "algorithmSettingForm";
	requestScheduleAlgorithm(requestScheduleUrl, dataForm, "scheduleSettingsForm");
}