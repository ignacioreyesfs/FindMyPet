import './breed-load.js';
import './location-load.js';

$(document).ready(displayExtraFields);
$(document).ready(() => {
	$('#date').prop("max", new Date().toISOString().split("T")[0]);
});

$('#postType').change(displayExtraFields);

function displayExtraFields(){
	if($('#postType').val() == 'search'){
		$('#searchGroup').removeAttr("hidden");
		$('#foundGroup').prop("hidden", "hidden");
	}else if($('#postType').val() == 'found'){
		$('#foundGroup').removeAttr("hidden");
		$('#searchGroup').prop("hidden", "hidden");
	}
}




