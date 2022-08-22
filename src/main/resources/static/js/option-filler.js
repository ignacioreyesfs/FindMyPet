export function setOptions(data, itemSearch){
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