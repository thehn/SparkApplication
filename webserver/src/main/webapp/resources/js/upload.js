/**
 * 
 */
	// Event listeners for button clicks
	$(document).ready(function() {
		// activate tab
		$("#uploadNav").addClass("active");
		$("#homeNav").removeClass("active");
		
		document.getElementById('files').addEventListener('change',
				onFileSelect, false);
		document.getElementById('uploadButton').addEventListener('click',
				startUpload, false);
		$('[data-toggle="tooltip"]').tooltip();
	});
	

	var totalFileLength, totalUploaded, fileCount, filesUploaded;
	// list of files which successfully uploaded to Rest server
	var listUploadedFiles = [];
	var listSelectedFiles = [];
	var totalUploadedFiles = 0;

	//Will be called when upload is completed
	function onUploadComplete() {
		totalUploaded += document.getElementById('files').files[filesUploaded].size;
		filesUploaded++;

		if (filesUploaded < fileCount) {
			var percentComplete = parseInt((totalUploaded * 100) / totalFileLength);
			var bar = document.getElementById('bar');
			bar.style.width = percentComplete + '%';
			bar.innerHTML = percentComplete + ' % complete';
			uploadNext();
		} else {
			var bar = document.getElementById('bar');
			bar.style.width = '100%';
			bar.innerHTML = '100% complete';
			document.getElementById('indexButton').removeAttribute('disabled');
			fillSelectForm(listSelectedFiles, listUploadedFiles, "listUploadedFiles");
		}
		totalUploadedFiles += 1;
		$('#totalUploadedFiles').text(totalUploadedFiles);
	}

	//Will be called when user select the files in file control
	function onFileSelect(e) {
		var files = e.target.files; // FileList object
		var output = [];
		fileCount = files.length;
		totalFileLength = 0;
		listSelectedFiles = [];
		for (var i = 0; i < fileCount; i++) {
			var file = files[i];
			output
					.push('<a href="#" class="list-group-item list-group-item-info"><span class="badge alert-info pull-right">Selected</span>');
			output.push(file.name, ' (', file.size, ' bytes, ',
					file.lastModifiedDate.toLocaleDateString(), ')');
			output.push('</a>');
			totalFileLength += file.size;
			listSelectedFiles.push(file.name);
		}

		$('#totalSelected').text(fileCount);
		document.getElementById('selectedFiles').innerHTML = output.join('');
		if (fileCount > 0){
			$('#uploadButton').removeAttr('disabled');
		}else{
			$('#uploadButton').prop('disabled', true);
		}
		

	}
	
	// to update UI for list of uploaded files
	function showUploadedFile(listFiles){
		var output = [];
		
		for(var i = 0; i < listFiles.length; i++){
			fileName = listFiles[i];
			output.push('<a href="#" class="list-group-item list-group-item-success"><span class="badge alert-success pull-right">Uploaded</span>');
			output.push(fileName);
			output.push('</a>');
		}
		document.getElementById('uploadedFiles').innerHTML = output.join('');
	}

	//Pick the next file in queue and upload it to remote server
	function uploadNext() {
		
		var fd = new FormData();
		var file = document.getElementById('files').files[filesUploaded];
		fd.append("multipartFile", file);
		
		$.ajax({
			url : "request-SendMultipleFileToRestServer",
			type : "POST",
			data : fd,
			enctype : 'multipart/form-data',
			processData : false,
			contentType : false
		}).done(function(data) {
			listUploadedFiles.push(data);
			onUploadComplete();
			showUploadedFile(listUploadedFiles);
		}).fail(function(jqXHR, textStatus) {
			alert(jqXHR.responseText);
			alert('File upload failed.');
		}).always(function() {
			$('#loaderContainer').hide();
		});	
	}

	//Let's begin the upload process
	function startUpload() {
		$('#loaderContainer').show();
		totalUploaded = filesUploaded = 0;
		uploadNext();
	}

	// request index upload data to ElasticSearch
	function requestForIndex() {
		var listValue = listUploadedFiles.toString();
		$.ajax({
			// async : false,
			type : "POST",
			url : "indexDataToEs",
			data : $("#fieldOptionForm").serialize(),
			beforeSend : function() {
				$('#loaderContainer').show();
			}
		}).done(function(response) {
			$('#loaderContainer').hide();
			$("#responseView").html(response);
		}).fail(function() {
			console.log("error: " + response);
		}).always(function() {
			$('#loaderContainer').hide();
		});

		return false;
	}

	// to change input corresponding to chosen format
	$(function() {
		
		$('.dense-only').hide();
		$('.sparse-only').hide();
		$('.basket-only').hide();
		$('.dense-only input').removeAttr('required');
		$('.sparse-only input').removeAttr('required');
		$('.basket-only input').removeAttr('required');
		
		$('#format').change(function() {
			var formatVal = $('#format').val();
			
			switch(formatVal){
			default: {
				$('.dense-only').hide();
				$('.sparse-only').hide();
				$('.basket-only').hide();
				$('.dense-only input').removeAttr('required');
				$('.sparse-only input').removeAttr('required');
				$('.basket-only input').removeAttr('required');
				break;
			}
			case 'BASKET':{
				$('.basket-only').show();
				$('.dense-only').hide();
				$('.sparse-only').hide();
				$('.dense-only input').removeAttr('required');
				$('.sparse-only input').removeAttr('required');
				$('.basket-only input').prop('required','true');
				break;
			}
			case 'DENSE':{
				$('.basket-only').hide();
				$('.dense-only').show();
				$('.sparse-only').hide();
				$('.sparse-only input').removeAttr('required');
				$('.basket-only input').removeAttr('required');
				$('.dense-only input').prop('required','true');
				break;
			}
			case 'SPARSE':{
				$('.basket-only').hide();
				$('.dense-only').hide();
				$('.sparse-only').show();
				$('.dense-only input').removeAttr('required');
				$('.basket-only input').removeAttr('required');
				$('.sparse-only input').prop('required','true');
				break;
			}
			}
			
		});
	});
	
	// to fill select option
	function fillSelectForm(arrayNames, arrayValues, selectFormId){

		var sel = document.getElementById(selectFormId);
		
		// first clear all old options
		sel.options.length = 0;
		
		// fill new options
		for(var i = 0; i < arrayValues.length; i++) {
			var opt = document.createElement('option');
			opt.innerHTML = arrayNames[i];
			opt.value = arrayValues[i];
			sel.appendChild(opt);
		}
	}
	
	
	// *****************************************************************************************
	// *******************************************LOOKUP TABLE**********************************
	// *****************************************************************************************
	var rowIndex = 0;
	var urlRequestDeleteLookup = "requestDeleteLookup";
	var urlRequestEditLookup = "requestEditLookup";
	var urlCheckLookupNameIsValid = "isValidLookupName/";
	
	$(function() {
		// when "Data Lookup" tab is activated
		$('#lookupTab').click(function(){
			showLookupTable();
		});
		
		// submit button clicks - request for editing Lookup definition
		$('#btnSubmitLookup').click(function(){
			var isValidForm = $('#lookupSettingsForm').valid();
			
			if(isValidForm){
				var action = $('#lookupSettingsForm > input[name="action"]').val();
				$.ajax({
					type : "POST",
					url : urlRequestEditLookup,
					data : $('#lookupSettingsForm').serialize(),
					beforeSend : function() {
						//$('#loaderContainer').show();
					}
				}).done(function(data) {
					if(action == "edit"){
						updateExistedRow(data);
					}else if(action == "add"){
						var jsonObj = JSON.parse(data);
						addNewRow(jsonObj);
					}else{
						// do nothing
					}
					$('#editLookupDef').modal('toggle');
				}).fail(function() {
					console.log("error");
				}).always(function() {
					//$('#loaderContainer').hide();
				});
			}else{
				var validator = $("#lookupSettingsForm").validate();
				$('.error').css("color", "red");
			}
			
			return false;
		});
		
		// delete lookup definition
		$('#btnDelete').click(function(){
			// send request to delete schedule
			$.ajax({
				type : "POST",
				url : urlRequestDeleteLookup,
				data : $('#lookupSettingsForm').serialize(),
				success : function(response) {
					if (response == "true"){
						$('#deleteLookupDef').modal('toggle');
						$('#lookupTbl > tbody > tr').eq(rowIndex).remove();
					}else{
						alert(response);
					}
				}
			});
			return false;
		});
		
		// button add new
		$('#btnAddNewLookupDef').click(function(){
			// reset form values
			$('#lookupSettingsForm')[0].reset();
			// clear attribute read only
			$('#lookupSettingsForm > div > input[name="lookupName"]').removeAttr("readonly");
			// set action is "add"
			$('#lookupSettingsForm > input[name="action"]').val("add");
			// show modal
			$('#editLookupDef').modal('toggle');
		});
		
		// to check lookup name is existed or not
		$('#lookupName > input').change(function(){
			var lookupName = $(this).val();
			if(lookupName != ''){
				var targetUrl = urlCheckLookupNameIsValid + lookupName;
				$.ajax({
					type: "GET",
					url: targetUrl,
					success: function(response){
						if(response){
							$('#btnSubmitLookup').prop("disabled", false);
							$('#lookupName').removeClass('has-error').addClass('has-success');
						}else{
							alert("Lookup name is already existed!");
							$('#btnSubmitLookup').prop("disabled", true);
							$('#lookupName').removeClass('has-success').addClass('has-error');
						};
					},
					failure: function(err){
						alert(err);
					}
				});
			}
		});
	});
	
	function showLookupTable(){
		$("#lookupTbl > tbody").empty();
		$.ajax({
			type : "GET",
			url : "getLookupDef",
			success : function(data) {
				fillDataToTable(data);
			},
			failure : function(errMsg) {
				alert(errMsg);
			}
		});	
	};
	
	function fillDataToTable(json){
		var jsonObj = JSON.parse(json);
		for(i in jsonObj)
		{
			addNewRow(jsonObj[i]);
		};
	}
	
	// to add new row to table, use data from response
	function addNewRow(rowData){
		var data = [];
		// add new row
		data.push('<tr>');
		
		// add data using data from JSON object
		// 1. lookupName
		data.push('<td>');
		data.push(rowData.lookupName);
		data.push('</td>');
		
		// 2. index
		data.push('<td>');
		data.push(rowData.index);
		data.push('</td>');
		
		// 3. type
		data.push('<td>');
		data.push(rowData.type);
		data.push('</td>');
		
		// 4. source
		data.push('<td>');
		data.push(rowData.source);
		data.push('</td>');
		
		// 5. delimiter
		data.push('<td>');
		data.push(rowData.delimiter);
		data.push('</td>');
		
		// 6. indexOfLabeledField
		data.push('<td>');
		data.push(rowData.indexOfLabeledField);
		data.push('</td>');
		
		// 7. column edit
		data.push('<td> <p data-placement="top" data-toggle="tooltip" title="Edit this lookup definition"> <button class="btn-edit btn btn-info btn-xs" data-title="Edit" data-toggle="modal" data-target="#editLookupDef"> <span class="glyphicon glyphicon-edit"></span> </button> </p> </td>')
		
		// 8. column delete
		data.push('<td> <p data-placement="top" data-toggle="tooltip" title="Delete this lookup definition"> <button class="btn-delete btn btn-danger btn-xs" data-title="Delete" data-toggle="modal" data-target="#deleteLookupDef"> <span class="glyphicon glyphicon-trash"></span> </button> </p> </td>')

		// end up row
		data.push('</tr>');
		$("#lookupTbl>tbody:last-child").append(data.join(""));
		
		// add event again
		fireEventWhenClickEditOrDelBtn();
	};
	
	// upated modal when click button Edit
	function fireEventWhenClickEditOrDelBtn(){
		$('.btn-edit, .btn-delete').click(function() {
			
			// activate button submit
			$('#btnSubmitLookup').prop("disabled", false);
			$('#lookupName').removeClass('has-error').removeClass('has-success');
			
			// store row index
			rowIndex = $(this).closest('tr').index();
			var lookupName = $(this).closest('tr').find('td:first').text();
			var index = $(this).closest('tr').find('td:eq(1)').text();
			var type = $(this).closest('tr').find('td:eq(2)').text();
			var source = $(this).closest('tr').find('td:eq(3)').text();
			var delimiter = $(this).closest('tr').find('td:eq(4)').text();
			var indexOfLabeledField = $(this).closest('tr').find('td:eq(5)').text();
			
			// setting lookup definition information for lookup definition Editor
			$('#lookupSettingsForm > div > input[name="lookupName"]').val(lookupName);
			$('#lookupSettingsForm > div > input[name="index"]').val(index);
			$('#lookupSettingsForm > div > input[name="type"]').val(type);
			$('#lookupSettingsForm > div > input[name="source"]').val(source);
			$('#lookupSettingsForm > div > input[name="delimiter"]').val(delimiter);
			$('#lookupSettingsForm > div > input[name="indexOfLabeledField"]').val(indexOfLabeledField);
			
			// set lookupName is read only
			$('#lookupSettingsForm > div > input[name="lookupName"]').prop("readonly", true);
			// set action is "edit"
			$('#lookupSettingsForm > input[name="action"]').val("edit");
		});
	}
	
	// to update existed row
	// note that it will only update from clumn index 4 to 7
	function updateExistedRow(rowData){
		var jsonObj = JSON.parse(rowData);
		$('#lookupTbl > tbody > tr:eq(' + rowIndex + ') > td:eq(0)').html(jsonObj.lookupName);
		$('#lookupTbl > tbody > tr:eq(' + rowIndex + ') > td:eq(1)').html(jsonObj.index);
		$('#lookupTbl > tbody > tr:eq(' + rowIndex + ') > td:eq(2)').html(jsonObj.type);
		$('#lookupTbl > tbody > tr:eq(' + rowIndex + ') > td:eq(3)').html(jsonObj.type);
		$('#lookupTbl > tbody > tr:eq(' + rowIndex + ') > td:eq(4)').html(jsonObj.delimiter);
		$('#lookupTbl > tbody > tr:eq(' + rowIndex + ') > td:eq(5)').html(jsonObj.indexOfLabeledField);
	};
	