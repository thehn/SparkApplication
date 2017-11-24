var requestTrainUrl = "request-SvcTrain";
var requestPredictUrl = "request-SvcPredict";
var requestModelDetail = "request-SvcModelDetail";
var requestUploadUnlabeledFile = "request-SendFileToRestServer";
var requestDeleteModel = "deleteModel/SvcClassifier/";
var isTrainingTab = true;
var isPredictionTab = false;
var requestScheduleUrl ="schedule-svc";
/*var algorithmName = "SvcClassifier";*/ // #PC0012 

$(function() {
	
	// set algorithm name
	algorithmName = "SvcClassifier"; // #PC0012
	
	// activate tab name
	$("ul").find("li").removeClass("active");
	$("#classificationNav").addClass("active");

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

	//btnPredict
	/* Button "run program" click */
	$("#btnPredict").click(
			function() {
				if ($('#predictionForm').valid()) {
					requestRunAlgorithm(requestPredictUrl, predictionForm,
							responseViewer_2);
					$('#visualizer_2').show();
				} else {
					var validator = $("#predictionForm").validate();
					$('.error').css("color", "red");
					validator.showErrors();
				}

			});

	// to show the change of fraction
	$('#rangeData')
			.change(
					function() {
						var fraction_value = document
								.getElementById("rangeData").value;
						$('#range_value').text(
								fraction_value + " : "
										+ (100 - fraction_value));
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
		isTrainingTab = false;
		isPredictionTab = true;
		listModels(algorithmName);	
	});
	// when click to training tab
	$('#trainingTabPaneBtn').click(function() {
		isTrainingTab = true;
		isPredictionTab = false;
	});
	
	/* button upload unlabeled data */
	$('#btnUploadUnlabeledFile').on('click', function() {
		if($('#dataDelimiter').val() != ''){
			$('#delimiter').val($('#dataDelimiter').val());
			$.ajax({
				url : requestUploadUnlabeledFile,
				type : "POST",
				data : new FormData(document.getElementById("unlabeledFileForm")),
				enctype : 'multipart/form-data',
				processData : false,
				contentType : false
			}).done(function(data) {
				var fileName = data.fileName; // #PC0005
				$('#uploadedFileName').val(fileName); // #PC0005
				$("#btnPredict").removeAttr('disabled');
				$("#btnShowScheduler_2").removeAttr('disabled');
				alert("File upload Success!");
			}).fail(function(jqXHR, textStatus) {
				alert(jqXHR.responseText);
				alert('File upload failed.');
			});	
		}else{
			alert("Delimiter cannot be empty!");
		}
	});
	
	// validate file uploaded or not?
	$('#inputFile').change(function(){
		var chosenFileName = $(this).val();
		if(chosenFileName != ''){
			$('#btnUploadUnlabeledFile').removeAttr('disabled');
		}else{
			$('#btnUploadUnlabeledFile').prop("disabled", true);
			$('#btnPredict').prop("disabled", true);
			$('#btnShowScheduler_2').prop("disabled", true);
		}
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
	
	// event when dataDelimiter is changed
	$('#dataDelimiter').change(function () {
		if($('#dataDelimiter').val() != ''){
			$('#delimiter').val($('#dataDelimiter').val());
		}else{
			alert("Delimiter cannot be empty!");
		}
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
		}else if(isPredictionTab){
			dataForm = '#predictionForm';
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

//to send the request for scheduling job
function requestSchedule() {
	var dataForm;
	if(isTrainingTab){
		dataForm = "algorithmSettingForm";
	}else if(linkPredictionTab){
		dataForm = "predictionForm";
	};
	requestScheduleAlgorithm(requestScheduleUrl, dataForm, "scheduleSettingsForm");
}