/**
 * Logging functions...
 */

function timeStamp(milliseconds) {
	var d = new Date();
	if ( milliseconds != undefined ) {
		d = new Date(milliseconds);
	}
	var timeStamp = d.getDay() + '/' + d.getMonth() + '/' + d.getFullYear()
			+ ' - ' + d.getHours() + ':' + d.getMinutes() + ':'
			+ d.getSeconds() + ':' + d.getMilliseconds();
	return timeStamp;
}

var logHtmlElement;

console.log = function(message) {
	if ( logHtmlElement ) {
		if (typeof message == 'object') {
			logHtmlElement.append(timeStamp() + ' -> ' + (JSON && JSON.stringify ? JSON.stringify(message) : String(message)) + '<br/>');
		} else {
			logHtmlElement.append(timeStamp() + ' -> ' + message + '<br/>');
		}
	}
	var logContainer = document.querySelector(".logContainer");
	if(logContainer){
		var scrollHeight = document.querySelector(".logContainer").scrollHeight;
		if(scrollHeight){
			logContainer.scrollTo(0,scrollHeight);
		}
	}
};


console.htmlLog = function(htmlContent) {
	var logData = htmlContent;
	logData = logData.replace(/</g, '[');
	logData = logData.replace(/>/g, ']');
	console.log(logData);
};