$(function(){ 
	$("#analysis").click(function(){ 
		$('#results').show();
		
		$.ajax({ 
			type: 'POST' , 
			url: '/ceh/morph/analysis' , 
			data: $('#inputForm').serialize() ,
			dataType : 'json' , 
			beforeSend : function() {
				$('#loaderContainer').show();
			}
			}).done(function(data) {
				
				$('#loaderContainer').hide();
				
				$(document).ready(function() {
					$('#result').DataTable({
						destroy: true,
						data : data,
						columns : [ {
							title : "No"
						}, {
							title : "Morpheme"
						}, {
							title : "Tag"
						}, {
							title : "Part of speech"
						}],
						responsive: true
					});
					
				});
			}).fail(function() {
				console.log("error");
			}).always(function() {
				$('#loaderContainer').hide();
			});
	})	
})
		
