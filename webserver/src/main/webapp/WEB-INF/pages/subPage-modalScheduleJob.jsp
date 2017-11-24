<!-- Setting Page -->
<div class="modal fade" id="scheduler" tabindex="-1"
	role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">×</span><span class="sr-only">Close</span>
				</button>
				<h2 class="modal-title algorithm-header">Scheduler settings</h2>
			</div>
			<div class="modal-body">

				<!-- all settings go here -->
				<form id="scheduleSettingsForm" onsubmit="return false"
					modelAttribute="scheduleSettings">
					<div class="form-group">
						<label class="control-label" for="title">Title</label>
						<input type="text" class="form-control" name="title" required>
					</div>
					<div class="form-group">
						<label class="control-label" for="description">Description</label>
						<textarea name="description" class="form-control" placeholder="Optional"></textarea>
					</div>
					<div class="form-group">
						<label class="control-label" for="interval">Schedule<br></label>
						<div>
							<select id="interval" name="interval" class="form-control" required>
								<option value="">Please select one</option>
								<option value="hourly">Run every hour</option>
								<option value="daily">Run every day</option>
								<option value="weekly">Run every week</option>
								<option value="monthly">Run every month</option>
								<option value="cron">Run on Cron Schedule</option>
							</select>
						</div>
					</div>
					<div class="form-group interval-supplement" id="min" style="display: none">
						<label class="control-label" for="description">At</label>
						<input type="number" name="min" style="display: none">
						<div class="btn-group">
							<button type="button" class="btn btn-default dropdown-toggle"
								data-toggle="dropdown"><span class="caret"></span>
							</button>
							<ul class="dropdown-menu scrollable-menu" role="menu">
								<li class="list-items"><a role="menuitem" tabindex="-1" >0</a></li>
								<li class="list-items"><a role="menuitem" tabindex="-1" >15</a></li>
								<li class="list-items"><a role="menuitem" tabindex="-1" >30</a></li>
								<li class="list-items"><a role="menuitem" tabindex="-1" >45</a></li>
							</ul>
						</div>
						minutes past the hour.
					</div><div class="form-group interval-supplement" id="wday" style="display: none">
						<label class="control-label" for="description">On</label>
						<input type="text" name="wday" style="display: none">
						<div class="btn-group">
							<button type="button" class="btn btn-default dropdown-toggle"
								data-toggle="dropdown"><span class="caret"></span>
							</button>
							<ul class="dropdown-menu scrollable-menu" role="menu" style="max-height: 200px; overflow-y: scroll">
								<li class="list-items"><a role="menuitem" tabindex="-1" >Monday</a></li>
								<li class="list-items"><a role="menuitem" tabindex="-1" >Tuesday</a></li>
								<li class="list-items"><a role="menuitem" tabindex="-1" >Wednesday</a></li>
								<li class="list-items"><a role="menuitem" tabindex="-1" >Thursday</a></li>
								<li class="list-items"><a role="menuitem" tabindex="-1" >Friday</a></li>
								<li class="list-items"><a role="menuitem" tabindex="-1" >Saturday</a></li>
								<li class="list-items"><a role="menuitem" tabindex="-1" >Sunday</a></li>
							</ul>
						</div>
					</div>
					<div class="form-group interval-supplement" id="mday" style="display: none">
						<label class="control-label" for="description">On</label>
						<input type="number" name="mday" style="display: none">
						<div class="btn-group">
							<button type="button" class="btn btn-default dropdown-toggle"
								data-toggle="dropdown"><span class="caret"></span>
							</button>
							<ul class="dropdown-menu scrollable-menu" role="menu" style="max-height: 200px; overflow-y: scroll"></ul>
						</div>
					</div>
					<div class="form-group interval-supplement" id="hour" style="display: none">
						<label class="control-label" for="description">At</label>
						<input type="number" name="hour" style="display: none">
						<div class="btn-group">
							<button type="button" class="btn btn-default dropdown-toggle"
								data-toggle="dropdown"><span class="caret"></span>
							</button>
							<ul class="dropdown-menu scrollable-menu" role="menu" style="max-height: 200px; overflow-y: scroll"></ul>
						</div>
					</div>
					<div class="form-group interval-supplement" id="cronExpression" style="display: none">
						<label class="control-label" for="description">Cron Expression</label>
						<input type="text" class="form-control" name="cronExpression"  value="0 6 * * 1">
					</div>
					
					<!-- hidden input -->
					<input type="checkbox" name="isReschedule" style="display: none">
				</form>

			</div>
			<div class="modal-footer">
				<div class="btn-group btn-group-justified" role="group"
					aria-label="group button">
					<div class="btn-group" role="group">
						<button type="button" class="btn btn-md btn-primary pull-right"
							id="btnRequestSchedule" role="button">Schedule</button>
					</div>
				</div>
			</div>

			<div id="responseView"></div>

		</div>
	</div>
</div>

<style>
	.list-items{
		cursor: pointer;
	}
</style>

<script type="text/javascript">
	$(document).ready(function() {
		
		// fill data for list of hours
		for(var i=0; i<24; i++){
			$('#hour>div>ul').append('<li class="list-items"><a role="menuitem" tabindex="-1" >'+ i + ':00</a></li>');
		}
		
		// fill data for list of days in month
		for(var i=1; i<=31; i++){
			$('#mday>div>ul').append('<li class="list-items"><a role="menuitem" tabindex="-1" >'+ i + '</a></li>');
		}
		
		// show options coressponding to interval value
		$('#interval').change(function(){
			var interval = $(this).val();
			if (interval == 'cron'){
				$('.interval-supplement').hide();
				$('#cronExpression>input').attr('required', 'true');
				$('#cronExpression').show();
			}else if(interval == 'hourly'){
				$('.interval-supplement').hide();
				$('#min').show();
			}else if(interval == 'daily'){
				$('.interval-supplement').hide();
				$('#hour').show();
			}else if(interval == 'weekly'){
				$('.interval-supplement').hide();
				$('#wday').show();
				$('#hour').show();
			}else if(interval == 'monthly'){
				$('.interval-supplement').hide();
				$('#mday').show();
				$('#hour').show();
			}else{
				$('.interval-supplement').hide();
			}
		});
		
		// display value of minute
		$('#min>div>ul>li').click(function(){
			var value = $(this).text();
			$('#min>div>button').text(value);
			$('#min>input[name="min"]').val(value);
		});
		
		// display value of hour
		$('#hour>div>ul>li').click(function(){
			var value = $(this).text();
			$('#hour>div>button').text(value);
			$('#hour>input[name="hour"]').val(value.split(":")[0]);
		});
		
		// display value of week days
		$('#wday>div>ul>li').click(function(){
			var value = $(this).text();
			$('#wday>div>button').text(value);
			$('#wday>input[name="wday"]').val(value);
		});
		
		// display value of days in month
		$('#mday>div>ul>li').click(function(){
			var value = $(this).text();
			$('#mday>div>button').text(value);
			$('#mday>input[name="mday"]').val(value);
		});
		
		/* Button "Save" for updating some settings */
		$('#btnRequestSchedule').click(function() {
			if ($('#scheduleSettingsForm').valid()) {
				requestSchedule();
				 $('#scheduler').modal('toggle');
			} else {
				var validator = $("#scheduleSettingsForm").validate();
				$('.error').css("color", "red");
				validator.showErrors();
			}
		});

	});
</script>