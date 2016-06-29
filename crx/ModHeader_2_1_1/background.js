var SPECIAL_CHARS = '^$&+?.()|{}[]/'.split('');
var currentProfile;

/**
 * Check whether the current request url pass the given list of filters.
 */
function passFilters_(details, filters) {
  if (!filters) {
    return true;
  }
  var allowUrls = false;
  var hasUrlFilters = false;
  var allowTypes = false;
  var hasResourceTypeFilters = false;
  angular.forEach(filters, function(filter) {
    if (filter.enabled) {
      switch (filter.type) {
        case 'urls':
          hasUrlFilters = true;
          if (details.url.search(filter.urlPattern) == 0) {
            allowUrls = true;
          }
          break;
        case 'types':
          hasResourceTypeFilters = true;
          if (filter.resourceType.indexOf(details.type) >= 0) {
            allowTypes = true;
          }
          break;
      }
    }
  });
  return (!hasUrlFilters || allowUrls)
      && (!hasResourceTypeFilters || allowTypes);
};

function loadSelectedProfile_() {
  var appendMode = false;
  var headers = [];
  var respHeaders = [];
  var filters = [];
  if (localStorage.profiles) {
    var profiles = angular.fromJson(localStorage.profiles);
    if (!localStorage.selectedProfile) {
      localStorage.selectedProfile = 0;
    }
    var selectedProfile = profiles[localStorage.selectedProfile];

    function filterEnabledHeaders_(headers) {
      var output = [];
      angular.forEach(headers, function(header) {
        // Overrides the header if it is enabled and its name is not empty.
        if (header.enabled && header.name) {
          output.push({name: header.name, value: header.value});
        }
      });
      return output;
    };
    angular.forEach(selectedProfile.filters, function(filter) {
      if (filter.urlPattern) {
        var urlPattern = filter.urlPattern;
        var joiner = [];
        for (var i = 0; i < urlPattern.length; ++i) {
          var c = urlPattern.charAt(i);
          if (SPECIAL_CHARS.indexOf(c) >= 0) {
            c = '\\' + c;
          } else if (c == '\\') {
            c = '\\\\';
          } else if (c == '*') {
            c = '.*';
          }
          joiner.push(c);
        }
        filter.urlPattern = joiner.join('');
      }
      filters.push(filter);
    });
    appendMode = selectedProfile.appendMode;
    headers = filterEnabledHeaders_(selectedProfile.headers);
    respHeaders = filterEnabledHeaders_(selectedProfile.respHeaders);
  }
  return {
      appendMode: appendMode,
      headers: headers,
      respHeaders: respHeaders,
      filters: filters
  };
};

function modifyHeader(source, dest) {
  if (!source.length) {
    return;
  }
  // Create an index map so that we can more efficiently override
  // existing header.
  var indexMap = {};
  angular.forEach(dest, function(header, index) {
    indexMap[angular.lowercase(header.name)] = index;
  });
  angular.forEach(source, function(header) {
    var index = indexMap[angular.lowercase(header.name)];
    if (index !== undefined) {
      if (!currentProfile.appendMode) {
        dest[index].value = header.value;
      } else if (currentProfile.appendMode == 'comma') {
        if (dest[index].value) {
          dest[index].value += ',';
        }
        dest[index].value += header.value;
      } else {
        dest[index].value += header.value;
      }
    } else {
      dest.push({name: header.name, value: header.value});
      indexMap[angular.lowercase(header.name)] = dest.length - 1;
    }
  });
};

function modifyRequestHeaderHandler_(details) {
  currentProfile = loadSelectedProfile_();
  if (currentProfile && passFilters_(details, currentProfile.filters)) {
    modifyHeader(currentProfile.headers, details.requestHeaders);
  }
  return {requestHeaders: details.requestHeaders};
};

function modifyResponseHeaderHandler_(details) {
  var responseHeaders = angular.copy(details.responseHeaders);
  if (currentProfile && passFilters_(details, currentProfile.filters)) {
    modifyHeader(currentProfile.respHeaders, responseHeaders);
  }
  if (!angular.equals(responseHeaders, details.responseHeaders)) {
    return {responseHeaders: responseHeaders};
  }
};

chrome.webRequest.onBeforeSendHeaders.addListener(
  modifyRequestHeaderHandler_,
  {urls: []},
  ['requestHeaders', 'blocking']
);

chrome.webRequest.onHeadersReceived.addListener(
  modifyResponseHeaderHandler_,
  {urls: []},
  ['responseHeaders', 'blocking']
);
