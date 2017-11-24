var isFirstTime = true;

/**
 * to get chart rersource at the first time
 * 
 * @returns
 */
function getChartResource() {
	if (isFirstTime) {
		isFirstTime = false;
		$.ajax({
			type : "GET",
			url : "getChartSourceContent",
			success : function(data) {
				$('#chartContents').html(data);
			},
			failure : function(errMsg) {
				alert(errMsg);
			}
		});
	}
};

/**
 * to get chart resource by time range
 * 
 * @returns
 */
function getChartResourceByTimeRange() {
	$.ajax({
		type : "POST",
		url : "getChartResourceByTimeRange",
		data : $(dateRangeForm).serialize(),
		beforeSend : function() {
			//$('#loaderContainer').show();
		}
	}).done(function(response) {
		$('#chartContents').html(response);
	}).fail(function() {
		console.log("error");
	}).always(function() {
		//$('#loaderContainer').hide();
	});
	return false;
};