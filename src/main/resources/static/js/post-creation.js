$(document).ready(loadBreeds);
$(document).ready(loadRegions);
$(document).ready(loadSubRegions);

$('#petType').change(loadBreeds);

function loadBreeds() {
	if($('#petType').val() == ''){
		$('#breed').prop("disabled", "disable");
		$('#breed').html('<option value="" hidden>Select</option>');
	}else{
		$.getJSON('http://localhost:8080/breeds', {
			petType: $('#petType').val()
		}, (data) => setOptions($.map(data, (breed) => ({text: breed.name, value: breed.id})), '#breed'));
	}
}

$('#country').change(loadRegions);

function loadRegions() {
	if($('#country').val() == ''){
		let html = '<option value="" hidden>Select</option>';
		$('#region').prop("disabled", "disable");
		$('#subregion').prop("disabled", "disable");
		$('#region').html(html);
		$('#subregion').html(html);
	}else{
		$.getJSON('http://localhost:8080/regions', {
			country: $('#country').val()
		}, (data) => setOptions($.map(data, (name) => ({text: name, value: name})), '#region'));
	}
}

$('#region').change(loadSubRegions);

function loadSubRegions() {
	if($('#region').val() == ''){
		$('#subregion').prop("disabled", "disable");
		$('#subregion').html('<option value="" hidden>Select</option>');
	}else{
		$.getJSON('http://localhost:8080/subregions', {
			country: $('#country').val(),
			region: $('#region').val()
		}, (data) => setOptions(
			$.map(data, (location) => ({text: location.subRegion, value: location.id})), '#subregion'));
	}
}

function setOptions(data, itemSearch){
	let html = '<option value="" hidden>Select</option>';
	let actualVal = $(itemSearch).val();
	let len = data.length;
	for (let i = 0; i < len; i++) {
		html += '<option value="' + data[i].value+ '"';
		
		if(actualVal != null && actualVal == data[i].value){
			html+=' selected';
		}
		
		html+='>' + data[i].text + '</option>';
		
	}
	html += '</option>';
	$(itemSearch).removeAttr("disabled");
	$(itemSearch).html(html);
}

$('#postType').change(function(){
	if($(this).val() == 'search'){
		$('#searchGroup').removeAttr("hidden");
		$('#foundGroup').prop("hidden", "hidden");
	}else if($(this).val() == 'found'){
		$('#foundGroup').removeAttr("hidden");
		$('#searchGroup').prop("hidden", "hidden");
	}
});






