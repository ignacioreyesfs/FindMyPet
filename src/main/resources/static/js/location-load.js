import { setOptions } from './option-filler.js';

$(document).ready(loadRegions);
$(document).ready(loadSubRegions);
$('#country').change(loadRegions);
$('#region').change(loadSubRegions);

function loadRegions() {
	if($('#country').val() == ''){
		let html = '<option value="" hidden>Select</option>';
		$('#region').prop("disabled", "disable");
		$('#subregion').prop("disabled", "disable");
		$('#region').html(html);
		$('#subregion').html(html);
	}else{
		$.getJSON('/regions', {
			country: $('#country').val()
		}, (data) => setOptions($.map(data, (name) => ({text: name, value: name})), '#region'));
	}
}

function loadSubRegions() {
	if($('#region').val() == ''){
		$('#subregion').prop("disabled", "disable");
		$('#subregion').html('<option value="" hidden>Select</option>');
	}else{
		$.getJSON('/subregions', {
			country: $('#country').val(),
			region: $('#region').val()
		}, (data) => setOptions(
			$.map(data, (location) => ({text: location.subRegion, value: location.id})), '#subregion'));
	}
}