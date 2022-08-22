import { setOptions } from './option-filler.js';

function loadBreeds() {
	if($('#petType').val() == ''){
		$('#breed').prop("disabled", "disable");
		$('#breed').html('<option value="" hidden>Select</option>');
	}else{
		$.getJSON('/breeds', {
			petType: $('#petType').val()
		}, (data) => setOptions($.map(data, (breed) => ({text: breed.name, value: breed.id})), '#breed'));
	}
}

$(document).ready(loadBreeds);
$('#petType').change(loadBreeds);