$(document).ready(loadBreeds);
$(document).ready(loadRegions);
$(document).ready(displayExtraFields);

$("#filterForm").submit(function() {
    $(this).find(":input").filter(function () {
        return !this.value;
    }).attr("disabled", true);

    return true;
});

$('#petType').change(loadBreeds);

function loadBreeds() {
	if($('#petType').val() == ''){
		$('#breed').prop("disabled", "disable");
		$('#breed').html('<option value="">All</option>');
	}else{
		$.getJSON('http://localhost:8080/breeds', {
			petType: $('#petType').val()
		}, (data) => setOptions($.map(data, (breed) => breed.name), '#breed', 'breed'));
	}
}

$('#country').change(loadRegions);

function loadRegions() {
	if($('#country').val() == ''){
		let html = '<option value="">All</option>';
		$('#region').prop("disabled", "disable");
		$('#subregion').prop("disabled", "disable");
		$('#region').html(html);
		$('#subregion').html(html);
	}else{
		$.getJSON('http://localhost:8080/regions', {
			country: $('#country').val()
		}, function(data) {
			setOptions(data, '#region', 'region');
			loadSubRegions();
		})
	}
}

$('#region').change(loadSubRegions);

function loadSubRegions() {
	if($('#region').val() == ''){
		$('#subregion').prop("disabled", "disable");
		$('#subregion').html('<option value="">All</option>');
	}else{
		$.getJSON('http://localhost:8080/subregions', {
			country: $('#country').val(),
			region: $('#region').val()
		}, (data) => setOptions($.map(data, (location) => location.subRegion), '#subregion', 'subRegion'));
	}
}

function setOptions(data, itemSeach, urlParam){
	let html = '<option value="">All</option>';
	let len = data.length;
	let urlParams = new URLSearchParams(window.location.search);
	for (let i = 0; i < len; i++) {
		html += '<option value="' + data[i]+ '"';
		
		if(urlParam != null && urlParams.has(urlParam) && urlParams.get(urlParam) == data[i]){
			html+=' selected';
		}
		
		html+='>' + data[i] + '</option>';
		
	}
	html += '</option>';
	$(itemSeach).removeAttr("disabled");
	$(itemSeach).html(html);
}

$('#postType').change(displayExtraFields);

function displayExtraFields(){
	if($('#postType').val() == 'found'){
		$('#foundGroup').removeAttr("hidden");
		$('#relocation').removeAttr("disabled");
	}else{
		$('#foundGroup').prop("hidden", "hidden");
		$('#relocation').prop("disabled", "disabled");
	}
}
