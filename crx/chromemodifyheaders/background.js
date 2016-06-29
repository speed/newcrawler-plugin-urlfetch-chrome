function callbackFn(details) {
	var remove_headers = [];
	var add_or_modify_headers = {"User-Agent": 'Chrome driver', 'X-Vimm': 'Vimmaniac Pvt. Ltd.'};
	
	function inarray(arr, obj) {
		return (arr.indexOf(obj) != -1);
	}

	// remove headers
	for (var i = 0; i < details.requestHeaders.length; ++i) {
		if (inarray(remove_headers, details.requestHeaders[i].name)) {
			details.requestHeaders.splice(i, 1);
			var index = remove_headers.indexOf(5);
			remove_headers.splice(index, 1);
		}
		if (!remove_headers.length) break;
	}

	// modify headers
	for (var i = 0; i < details.requestHeaders.length; ++i) {
		if (add_or_modify_headers.hasOwnProperty(details.requestHeaders[i].name)) {
			details.requestHeaders[i].value = add_or_modify_headers[details.requestHeaders[i].name];
			delete add_or_modify_headers[details.requestHeaders[i].name];
		}
	}

	// add modify
	for (var prop in add_or_modify_headers) {
		details.requestHeaders.push(
			{name: prop, value: add_or_modify_headers[prop]}
		);
	}

	return {requestHeaders: details.requestHeaders};
}

chrome.webRequest.onBeforeSendHeaders.addListener(
			callbackFn,
			{urls: ["<all_urls>"]},
			['blocking', 'requestHeaders']
);