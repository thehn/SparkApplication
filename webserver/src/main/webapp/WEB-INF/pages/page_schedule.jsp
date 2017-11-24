<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Scheduler Management</title>
<!-- all libraries -->
<%@ include file="subPage-libraries.jsp"%>
<%-- <%@ include file="subPage-common-css.jsp"%> --%>
<link rel="stylesheet" type="text/css" href="<spring:url value="/resources/css/commonStyle.css"/>">

<body>
	<!-- Navigation bar -->
	<%@ include file="subPage-navigationBar.jsp"%>
	<div class="container" id="container">
		<div class="row">
			<div class="col-md-12">
				<div class="row">
					<div class="col-md-6">
						<h2 class="algorithm-header">Scheduled Jobs</h2>
					</div>
					<div class="col-md-6">
						<p>
							<a href="/ceh/scheduledJobs"
								class="btn btn-info btn-md pull-right"> <span
								class="glyphicon glyphicon-refresh"></span> Refresh
							</a>
						</p>
					</div>
				</div>
				
				<div class="table-responsive">
					<table id="tblListScheduledJobs" class="table table-bordred table-striped">
						<thead>
							<tr>
								<th>Title</th>
							<th>Algorithm</th>
							<th>Action</th>
							<th>Description</th>
							<th>Trigger</th>
							<th>Start time</th>
							<th>Previous fire</th>
							<th>Next fire</th>
							<th>State</th>
							<th>Edit</th>
							<th>Delete</th>
							</tr>
						</thead>
						<tbody></tbody>
					</table>
					<div class="clearfix"></div>
					<!-- <ul class="pagination pull-right">
						<li class="disabled"><a href="#"><span
								class="glyphicon glyphicon-chevron-left"></span></a></li>
						<li class="active"><a href="#">1</a></li>
						<li><a href="#">2</a></li>
						<li><a href="#">3</a></li>
						<li><a href="#">4</a></li>
						<li><a href="#">5</a></li>
						<li><a href="#"><span
								class="glyphicon glyphicon-chevron-right"></span></a></li>
					</ul> -->
				</div>
			</div>
		</div>
	</div>
	
	<form id="scheduleForm" style="display: none;">
		<input type="text" name="jobTitle">
	</form>
	
	<div id="responseViewer"></div>

	<div class="modal fade" id="modelDelete" tabindex="-1" role="dialog"
		aria-labelledby="edit" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">
						<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
					</button>
					<h4 class="modal-title custom_align" id="Heading">Delete this
						Schedule?</h4>
				</div>
				<div class="modal-body">
					<div class="alert alert-danger">
						<span class="glyphicon glyphicon-warning-sign"></span> Are you
						sure you want to delete this Schedule?
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

	<!-- Footer -->
	<%@ include file="subPage-footer.jsp"%>

	<!-- Setting Page -->
	<%@ include file="subPage-modalSettingPage.jsp"%>
	
	<!-- Model page -->
	<%@ include file="subPage-modalRescheduleJob.jsp"%>

</body>

<script type="text/javascript">
	var listAllInfo = ${listAllInfo};
	var urlChangeScheduleState = "changeScheduleState";
	var urlDeleteSchedule = "deleteSchedule";
	var urlRequestReschedule = "rescheduledJobs";
	var rowIndex = 0;
	
	$(document).ready(function() {
		$("ul").find("li").removeClass("active");
		$("#schedulerNav").addClass("active");
		
		listAllInfo.forEach(function(data){
			addNewRow(data);
		});

		$("[data-toggle=tooltip]").tooltip();
		
		// buttons was click to change state
		$('.btn-change-state').click(function() {
			// store row index
			rowIndex = $(this).closest('tr').index();
			
			// update value for data form
			var jobTitle = $(this).closest('tr').find('td:first').text();
			$('#scheduleForm > input[name="jobTitle"]').val(jobTitle);
			
			// send request to change state
			$.ajax({
				// async : false,
				type : "POST",
				url : urlChangeScheduleState,
				data : $('#scheduleForm').serialize(),
				success : function(response) {
					var data = [];
					if(response == 'NORMAL'){
						$('#tblListScheduledJobs > tbody > tr:eq(' + rowIndex + ') > td:eq(8) > p > button.btn-change-state').removeClass('btn-warning').addClass('btn-primary');
						$('#tblListScheduledJobs > tbody > tr:eq(' + rowIndex + ') > td:eq(8) > p > button.btn-change-state > span').removeClass('glyphicon-play').addClass('glyphicon-pause');
					}else if(response == 'PAUSED'){
						$('#tblListScheduledJobs > tbody > tr:eq(' + rowIndex + ') > td:eq(8) > p > button.btn-change-state').removeClass('btn-primary').addClass('btn-warning');
						$('#tblListScheduledJobs > tbody > tr:eq(' + rowIndex + ') > td:eq(8) > p > button.btn-change-state > span').removeClass('glyphicon-pause').addClass('glyphicon-play');
					}else{
						data.push('<td>');
						data.push(response);
						data.push('</td>');
						$('#tblListScheduledJobs > tbody > tr:eq(' + rowIndex + ') > td').eq(8).html(data.join(""));
					}
				}
			});
			return false;
		});
		
		// update value for data form
		$('.btn-delete').click(function() {
			// store row index
			rowIndex = $(this).closest('tr').index();
			
			var jobTitle = $(this).closest('tr').find('td:first').text();
			$('#scheduleForm > input[name="jobTitle"]').val(jobTitle);
		});
		
		// update value for data form
		$('.btn-edit').click(function() {
			// store row index
			rowIndex = $(this).closest('tr').index();
			var jobTitle = $(this).closest('tr').find('td:first').text();
			$('#scheduleForm > input[name="jobTitle"]').val(jobTitle);

			var jobDescription = $(this).closest('tr').find('td:eq(3)').text();

			// setting schedule information for Schedule Editor
			$('#scheduleSettingsForm > div > input[name="title"]').val(jobTitle);
			$('#scheduleSettingsForm > div > textarea[name="description"]').val(jobDescription);
			
		});
		
		// delete schedule
		$('#btnDelete').click(function(){
			// send request to delete schedule
			$.ajax({
				// async : false,
				type : "POST",
				url : urlDeleteSchedule,
				data : $('#scheduleForm').serialize(),
				success : function(response) {
					if (response == "true"){
						$('#modelDelete').modal('toggle');
						$('#tblListScheduledJobs > tbody > tr').eq(rowIndex).remove();
					}else{
						alert(response);
					}
				}
			});
			return false;
		});
	});
	
	// to add new row to table, use data from response
	function addNewRow(rowData){
		var data = [];
		data.push('<tr>');
		var i = 0;
		rowData.forEach(function(subElement){
			if(i != 8){
				data.push('<td>');
				data.push(subElement);
				data.push('</td>');
			}else{
				if(subElement == 'NORMAL'){
					data.push('<td> <p data-placement="top" data-toggle="tooltip" title="It is running, click to pause"> <button class="btn-change-state btn btn-primary btn-xs"> <span class="glyphicon glyphicon-pause"></span> </button> </p> </td>');
				}else if(subElement == 'PAUSED'){
					data.push('<td> <p data-placement="top" data-toggle="tooltip" title="It is paused, click to resume"> <button class="btn-change-state btn btn-warning btn-xs"> <span class="glyphicon glyphicon-play"></span> </button> </p> </td>');
				}else{
					data.push('<td>');
					data.push(subElement);
					data.push('</td>');
				}
			}
			i++;
		});
		// column edit
		data.push('<td> <p data-placement="top" data-toggle="tooltip" title="Edit this schedule"> <button class="btn-edit btn btn-info btn-xs" data-title="Edit" data-toggle="modal" data-target="#rescheduler"> <span class="glyphicon glyphicon-edit"></span> </button> </p> </td>')
		// column delete
		data.push('<td> <p data-placement="top" data-toggle="tooltip" title="Delete this schedule"> <button class="btn-delete btn btn-danger btn-xs" data-title="Delete" data-toggle="modal" data-target="#modelDelete"> <span class="glyphicon glyphicon-trash"></span> </button> </p> </td>')
		data.push('</tr>');
		$("#tblListScheduledJobs>tbody:last-child").append(data.join(""));
	};
	
	// to update existed row
	// note that it will only update from clumn index 4 to 7
	function updateExistedRow(rowData){
		for (var i = 4; i < 8; i++){
			$('#tblListScheduledJobs > tbody > tr:eq(' + rowIndex + ') > td:eq(' + i + ')').html(rowData[i]);
		}
	};
	
	// to request reschedule
	function requestReschedule(){
		$.ajax({
			type : "POST",
			url : urlRequestReschedule,
			data : $('#scheduleSettingsForm').serialize(),
			beforeSend : function() {
				//$('#loaderContainer').show();
			}
		}).done(function(data) {
			updateExistedRow(data);
		}).fail(function() {
			console.log("error");
		}).always(function() {
			//$('#loaderContainer').hide();
		});
		return false;
	};

</script>
</body>
</html>