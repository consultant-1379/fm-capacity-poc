function commandAction(key,graphic,rest) {
	$( "#"+graphic ).effect( "highlight");
	switch (key) {
	case "startNormalFlow":
		manageStartNormalAlarmFlow(rest);
		break;
	case "stopNormalFlow":
		manageStopNormalAlarmFlow(rest);
		break;
	case "startPeakFlow":
		manageStartPeakAlarmFlow(rest);
		break;
	case "stopPeakFlow":
		manageStopPeakAlarmFlow(rest);
		break;
	case "startStormFlow":
		manageStartStormAlarmFlow(rest);
		break;
	case "stopStormFlow":
		manageStopStormAlarmFlow(rest);
		break;
	case "changeParallelAlarmNormalNumber":
		manageChangeParallelAlarmNormalNumber(rest);
		break;
	case "changePackingAlarmNormalNumber":
		manageChangePackingAlarmNormalNumber(rest);
		break;
	case "changeNormalThroughput":
		manageChangeNormalAlarmThroughput(rest);
		break;
	case "changeNormalEnableCeasePercentage":
		manageChangeNormalEnableCeaseAlarmPercentage(rest);
		break;
	case "changeNormalCeasePercentage":
		manageChangeNormalCeaseAlarmPercentage(rest);
		break;
	case "changeParallelAlarmPeakNumber":
		manageChangeParallelAlarmPeakNumber(rest);
		break;
	case "changePackingAlarmPeakNumber":
		manageChangePackingAlarmPeakNumber(rest);
		break;
	case "changePeakThroughput":
		manageChangePeakAlarmThroughput(rest);
		break;
	case "changePeakEnableCeasePercentage":
		manageChangePeakEnableCeaseAlarmPercentage(rest);
		break;
	case "changePeakCeasePercentage":
		manageChangePeakCeaseAlarmPercentage(rest);
		break;
	case "changeParallelAlarmStormNumber":
		manageChangeParallelAlarmStormNumber(rest);
		break;
	case "changePackingAlarmStormNumber":
		manageChangePackingAlarmStormNumber(rest);
		break;
	case "changeStormThroughput":
		manageChangeStormAlarmThroughput(rest);
		break;
	case "changeStormEnableCeasePercentage":
		manageChangeStormEnableCeaseAlarmPercentage(rest);
		break;
	case "changeStormCeasePercentage":
		manageChangeStormCeaseAlarmPercentage(rest);
		break;
	case "startRemoveAllNormalAlarmFlow":
		manageStartRemoveAllNormalAlarmFlow(rest);
		break;
	case "stopRemoveAllNormalAlarmFlow":
		manageStopRemoveAllNormalAlarmFlow(rest);
		break;
	case "startRemoveAllPeakAlarmFlow":
		manageStartRemoveAllPeakAlarmFlow(rest);
		break;
	case "stopRemoveAllPeakAlarmFlow":
		manageStopRemoveAllPeakAlarmFlow(rest);
		break;
	case "startRemoveAllStormAlarmFlow":
		manageStartRemoveStormAlarmFlow(rest);
		break;
	case "stopRemoveAllStormAlarmFlow":
		manageStopRemoveStormAlarmFlow(rest);
		break;
	case "startRemoveNormalFlow":
		manageStartRemoveNormalFlow(rest);
		break;
	case "stopRemoveNormalFlow":
		manageStopRemoveNormalFlow(rest);
		break;
	case "changeThresholdAlarmNormalPercentage":
		manageChangeThresholdAlarmNormalPercentage(rest);
		break;
	case "changeThresholdAlarmNormalNumber":
		manageChangeThresholdAlarmNormalNumber(rest);
		break;
	case "startRemovePeakFlow":
		manageStartRemovePeakFlow(rest);
		break;
	case "stopRemovePeakFlow":
		manageStopRemovePeakFlow(rest);
		break;
	case "changeThresholdAlarmPeakPercentage":
		manageChangeThresholdAlarmPeakPercentage(rest);
		break;
	case "changeThresholdAlarmPeakNumber":
		manageChangeThresholdAlarmPeakNumber(rest);
		break;
	case "changePeakOnPeriod":
		manageChangePeakOnAlarmPeriod(rest);
		break;
	case "changePeakOffPeriod":
		manageChangePeakOffAlarmPeriod(rest);
		break;
	case "startRemoveStormFlow":
		manageStartRemoveStormFlow(rest);
		break;
	case "stopRemoveStormFlow":
		manageStopRemoveStormFlow(rest);
		break;
	case "changeThresholdAlarmStormPercentage":
		manageChangeThresholdAlarmStormPercentage(rest);
		break;
	case "changeThresholdAlarmStormNumber":
		manageChangeThresholdAlarmStormNumber(rest);
		break;
	case "changeStormOnPeriod":
		manageChangeStormOnAlarmPeriod(rest);
		break;
	case "changeStormOffPeriod":
		manageChangeStormOffAlarmPeriod(rest);
		break;
	case "startReadAlarmFlow":
		manageStartReadAlarmFlow(rest);
		break;
	case "stopReadAlarmFlow":
		manageStopReadAlarmFlow(rest);
		break;
	case "changeReadParallelNumber":
		manageChangeReadParallelNumber(rest);
		break;
	case "changeReadPausePeriod":
		manageChangeReadPausePeriod(rest);
		break;
	case "getCurrentAlarmNumber":
		manageGetCurrentAlarmNumber(rest,graphic);
		break;
	case "getCurrentAlarmNumberTimerStart":
		getCurrentAlarmNumberTimer = manageGetCurrentAlarmNumberTimerStart(rest,graphic);
		break;
	case "getCurrentAlarmNumberTimerStop":
		manageGetCurrentAlarmNumberTimerStop(getCurrentAlarmNumberTimer);
		break;
	default:
		console.log("NO COMMAND ACTION FOUND ! ERROR !");
	}
}

function getSelectedNormalEnableCeaseAlarmPercentageValue() {
	var value = $('#ChangeNormalEnableCeasePercentageCheckbox').is(':checked');
	console.log("Normal Enable Cease Alarm Percentage VALUE is " + value);
	return value;
}

function getSelectedNormalCeaseAlarmPercentageValue() {
	var value = $('#ChangeNormalCeasePercentageInput').val();
	console.log("Normal Cease Alarm Percentage VALUE is " + value);
	return value;
}

function getSelectedThresholdAlarmNormalPercentageValue() {
	var value = $('#ThresholdAlarmNormalPercentage').val();
	console.log("Threshold Alarm Normal Percentage VALUE is " + value);
	return value;
}

function getSelectedPeakEnableCeaseAlarmPercentageValue() {
	var value = $('#ChangePeakEnableCeasePercentageCheckbox').is(':checked');
	console.log("Peak Enable Cease Alarm Percentage VALUE is " + value);
	return value;
}

function getSelectedPeakCeaseAlarmPercentageValue() {
	var value = $('#ChangePeakCeasePercentageInput').val();
	console.log("Peak Cease Alarm Percentage VALUE is " + value);
	return value;
}

function getSelectedThresholdAlarmPeakPercentageValue() {
	var value = $('#ThresholdAlarmPeakPercentage').val();
	console.log("Threshold Alarm Peak Percentage VALUE is " + value);
	return value;
}

function getSelectedStormEnableCeaseAlarmPercentageValue() {
	var value = $('#ChangeStormEnableCeasePercentageCheckbox').is(':checked');
	console.log("Storm Enable Cease Alarm Percentage VALUE is " + value);
	return value;
}

function getSelectedStormCeaseAlarmPercentageValue() {
	var value = $('#ChangeStormCeasePercentageInput').val();
	console.log("Storm Cease Alarm Percentage VALUE is " + value);
	return value;
}

function getSelectedThresholdAlarmStormPercentageValue() {
	var value = $('#ThresholdAlarmStormPercentage').val();
	console.log("Threshold Alarm Storm Percentage VALUE is " + value);
	return value;
}

/**
 * NormalAlarmThroughput VALUE
 */
function getSelectedNormalAlarmThroughputValue() {
	var normal = $('#ChangeNormalThroughputInput').val();
	console.log("Normal Alarm Throughput VALUE is " + normal);
	return normal;
}

/**
 * NormalAlarmThroughput MINIMUM
 */
function getSelectedNormalAlarmThroughputMinimum() {
	var min = $('#ChangeNormalThroughputInput').attr('min');
	console.log("Normal Alarm Throughput MINIMUM is " + min);
	return parseFloat(min);
}

/**
 * NormalAlarmThroughput MAXIMUM
 */
function getSelectedNormalAlarmThroughputMaximum() {
	var max = $('#ChangeNormalThroughputInput').attr('max');
	console.log("Normal Alarm Throughput MAXIMUM is " + max);
	return parseFloat(max);
}

/**
 * PeakAlarmThroughput VALUE
 */
function getSelectedPeakAlarmThroughputValue() {
	var value = $('#ChangePeakThroughputInput').val();
	console.log("Peak Alarm Throughput VALUE is " + value);
	return value;
}

/**
 * PeakAlarmThroughput MINIMUM
 */
function getSelectedPeakAlarmThroughputMinimum() {
	var min = $('#ChangePeakThroughputInput').attr('min');
	console.log("Peak Alarm Throughput MINIMUM is " + min);
	return parseFloat(min);
}

/**
 * PeakAlarmThroughput MAXIMUM
 */
function getSelectedPeakAlarmThroughputMaximum() {
	var max = $('#ChangePeakThroughputInput').attr('max');
	console.log("Peak Alarm Throughput MAXIMUM is " + max);
	return parseFloat(max);
}

/**
 * PeakOnAlarmPeriodInHour VALUE
 */
function getSelectedPeakOnAlarmPeriodInHourValue() {
	var value = $('#ChangePeakOnPeriodInHourInput').val();
	console.log("Peak Alarm On Period in Hour VALUE is " + value);
	return value;
}

/**
 * PeakOnAlarmPeriodInHour MINIMUM
 */
function getSelectedPeakOnAlarmPeriodInHourMinimum() {
	var min = $('#ChangePeakOnPeriodInHourInput').attr('min');
	console.log("Peak Alarm On Period in Hour MINIMUM is " + min);
	return parseFloat(min);
}

/**
 * PeakOnAlarmPeriodInHour MAXIMUM
 */
function getSelectedPeakOnAlarmPeriodInHourMaximum() {
	var max = $('#ChangePeakOnPeriodInHourInput').attr('max');
	console.log("Peak Alarm On Period in Hour MAXIMUM is " + max);
	return parseFloat(max);
}

/**
 * PeakOnAlarmPeriodInMin VALUE
 */
function getSelectedPeakOnAlarmPeriodInMinValue() {
	var value = $('#ChangePeakOnPeriodInMinInput').val();
	console.log("Peak Alarm On Period in Min VALUE is " + value);
	return value;
}

/**
 * PeakOnAlarmPeriodInMin MINIMUM
 */
function getSelectedPeakOnAlarmPeriodInMinMinimum() {
	var min = $('#ChangePeakOnPeriodInMinInput').attr('min');
	console.log("Peak Alarm On Period in Min MINIMUM is " + min);
	return parseFloat(min);
}

/**
 * PeakOnAlarmPeriodInMin MAXIMUM
 */
function getSelectedPeakOnAlarmPeriodInMinMaximum() {
	var max = $('#ChangePeakOnPeriodInMinInput').attr('max');
	console.log("Peak Alarm On Period in Min MAXIMUM is " + max);
	return parseFloat(max);
}

/**
 * PeakOffAlarmPeriodInHour VALUE
 */
function getSelectedPeakOffAlarmPeriodInHourValue() {
	var value = $('#ChangePeakOffPeriodInHourInput').val();
	console.log("Peak Alarm Off Period in Hour VALUE is " + value);
	return value;
}

/**
 * PeakOffAlarmPeriodInHour MINIMUM
 */
function getSelectedPeakOffAlarmPeriodInHourMinimum() {
	var min = $('#ChangePeakOffPeriodInHourInput').attr('min');
	console.log("Peak Alarm Off Period in Hour MINIMUM is " + min);
	return parseFloat(min);
}

/**
 * PeakOffAlarmPeriodInHour MAXIMUM
 */
function getSelectedPeakOffAlarmPeriodInHourMaximum() {
	var max = $('#ChangePeakOffPeriodInHourInput').attr('max');
	console.log("Peak Alarm Off Period in Hour MAXIMUM is " + max);
	return parseFloat(max);
}

/**
 * PeakOffAlarmPeriodInMin VALUE 
 */
function getSelectedPeakOffAlarmPeriodInMinValue() {
	var value = $('#ChangePeakOffPeriodInMinInput').val();
	console.log("Peak Alarm Off Period in Min VALUE is " + value);
	return value;
}

/**
 * PeakOffAlarmPeriodInMin MINIMUM 
 */
function getSelectedPeakOffAlarmPeriodInMinMinimum() {
	var min = $('#ChangePeakOffPeriodInMinInput').attr('min');
	console.log("Peak Alarm Off Period in Min MINIMUM is " + min);
	return parseFloat(min);
}

/**
 * PeakOffAlarmPeriodInMin MAXIMUM 
 */
function getSelectedPeakOffAlarmPeriodInMinMaximum() {
	var max = $('#ChangePeakOffPeriodInMinInput').attr('max');
	console.log("Peak Alarm Off Period in Min MAXIMUM is " + max);
	return parseFloat(max);
}

/**
 * StormAlarmThroughput VALUE
 */
function getSelectedStormAlarmThroughputValue() {
	var value = $('#ChangeStormThroughputInput').val();
	console.log("Storm Alarm Throughput VALUE is " + value);
	return value;
}

/**
 * StormAlarmThroughput MINIMUM
 */
function getSelectedStormAlarmThroughputMinimum() {
	var min = $('#ChangeStormThroughputInput').attr('min');
	console.log("Storm Alarm Throughput MINIMUM is " + min);
	return parseFloat(min);
}

/**
 * StormAlarmThroughput MAXIMUM
 */
function getSelectedStormAlarmThroughputMaximum() {
	var max = $('#ChangeStormThroughputInput').attr('max');
	console.log("Storm Alarm Throughput MAXIMUM is " + max);
	return parseFloat(max);
}

/**
 * StormOnAlarmPeriodInHour VALUE
 */
function getSelectedStormOnAlarmPeriodInHourValue() {
	var value = $('#ChangeStormOnPeriodInHourInput').val();
	console.log("Storm Alarm On Period in Hour VALUE is " + value);
	return value;
}

/**
 * StormOnAlarmPeriodInHour MINIMUM
 */
function getSelectedStormOnAlarmPeriodInHourMinimum() {
	var min = $('#ChangeStormOnPeriodInHourInput').attr('min');
	console.log("Storm Alarm On Period in Hour MINIMUM is " + min);
	return parseFloat(min);
}

/**
 * StormOnAlarmPeriodInHour MAXIMUM
 */
function getSelectedStormOnAlarmPeriodInHourMaximum() {
	var max = $('#ChangeStormOnPeriodInHourInput').attr('max');
	console.log("Storm Alarm On Period in Hour MAXIMUM is " + max);
	return parseFloat(max);
}

/**
 * StormOnAlarmPeriodInMin VALUE
 */
function getSelectedStormOnAlarmPeriodInMinValue() {
	var value = $('#ChangeStormOnPeriodInMinInput').val();
	console.log("Storm Alarm On Period in Min VALUE is " + value);
	return value;
}

/**
 * StormOnAlarmPeriodInMin MINIMUM
 */
function getSelectedStormOnAlarmPeriodInMinMinimum() {
	var min = $('#ChangeStormOnPeriodInMinInput').attr('min');
	console.log("Storm Alarm On Period in Min MINIMUM is " + min);
	return parseFloat(min);
}

/**
 * StormOnAlarmPeriodInMin MAXIMUM
 */
function getSelectedStormOnAlarmPeriodInMinMaximum() {
	var max = $('#ChangeStormOnPeriodInMinInput').attr('max');
	console.log("Storm Alarm On Period in Min MAXIMUM is " + max);
	return parseFloat(max);
}

/**
 * StormOffAlarmPeriodInHour VALUE
 */
function getSelectedStormOffAlarmPeriodInHourValue() {
	var value = $('#ChangeStormOffPeriodInHourInput').val();
	console.log("Storm Alarm Off Period in Hour VALUE is " + value);
	return value;
}

/**
 * StormOffAlarmPeriodInHour MINIMUM
 */
function getSelectedStormOffAlarmPeriodInHourMinimum() {
	var min = $('#ChangeStormOffPeriodInHourInput').attr('min');
	console.log("Storm Alarm Off Period in Hour MINIMUM is " + min);
	return parseFloat(min);
}

/**
 * StormOffAlarmPeriodInHour MAXIMUM
 */
function getSelectedStormOffAlarmPeriodInHourMaximum() {
	var max = $('#ChangeStormOffPeriodInHourInput').attr('max');
	console.log("Storm Alarm Off Period in Hour MAXIMUM is " + max);
	return parseFloat(max);
}

/**
 * StormOffAlarmPeriodInMin VALUE 
 */
function getSelectedStormOffAlarmPeriodInMinValue() {
	var value = $('#ChangeStormOffPeriodInMinInput').val();
	console.log("Storm Alarm Off Period in Min VALUE is " + value);
	return value;
}

/**
 * StormOffAlarmPeriodInMin MINIMUM 
 */
function getSelectedStormOffAlarmPeriodInMinMinimum() {
	var min = $('#ChangeStormOffPeriodInMinInput').attr('min');
	console.log("Storm Alarm Off Period in Min MINIMUM is " + min);
	return parseFloat(min);
}

/**
 * StormOffAlarmPeriodInMin MAXIMUM 
 */
function getSelectedStormOffAlarmPeriodInMinMaximum() {
	var max = $('#ChangeStormOffPeriodInMinInput').attr('max');
	console.log("Storm Alarm Off Period in Min MAXIMUM is " + max);
	return parseFloat(max);
}

function getSelectedReadPausePeriodInHourValue() {
	var value = $('#ChangeReadPausePeriodInHourInput').val();
	console.log("Read Pause Period in Hour VALUE is " + value);
	return value;
}

function getSelectedReadPausePeriodInMinValue() {
	var value = $('#ChangeReadPausePeriodInMinInput').val();
	console.log("Read Pause Period in Min VALUE is " + value);
	return value;
}

function getSelectedReadPausePeriodInSecValue() {
	var value = $('#ChangeReadPausePeriodInSecInput').val();
	console.log("Read Pause Period in Sec VALUE is " + value);
	return value;
}

function getSelectedParallelAlarmNormalNumberValue() {
	var value = $('#ParallelAlarmNormalNumber').val();
	console.log("Parallel Alarm Normal VALUE is " + value);
	return value;
}

function getSelectedPackingAlarmNormalNumberValue() {
	var value = $('#PackingAlarmNormalNumber').val();
	console.log("Packing Alarm Normal VALUE is " + value);
	return value;
}

function getSelectedParallelAlarmPeakNumberValue() {
	var value = $('#ParallelAlarmPeakNumber').val();
	console.log("Parallel Alarm Peak VALUE is " + value);
	return value;
}

function getSelectedPackingAlarmPeakNumberValue() {
	var value = $('#PackingAlarmPeakNumber').val();
	console.log("Packing Alarm Peak VALUE is " + value);
	return value;
}

function getSelectedParallelAlarmStormNumberValue() {
	var value = $('#ParallelAlarmStormNumber').val();
	console.log("Parallel Alarm Storm VALUE is " + value);
	return value;
}

function getSelectedPackingAlarmStormNumberValue() {
	var value = $('#PackingAlarmStormNumber').val();
	console.log("Packing Alarm Storm VALUE is " + value);
	return value;
}

function manageStartNormalAlarmFlow(rest) {
	console.log("StartNormalAlarmFlow: sending rest " + rest);
	sendRest(rest);
}

function manageStopNormalAlarmFlow(rest) {
	console.log("StopNormalAlarmFlow: sending rest " + rest);
	sendRest(rest);
}

function manageStartPeakAlarmFlow(rest) {
	console.log("StartPeakAlarmFlow: sending rest " + rest);
	sendRest(rest);
}

function manageStopPeakAlarmFlow(rest) {
	console.log("StopPeakAlarmFlow: sending rest " + rest);
	sendRest(rest);
}

function manageStartStormAlarmFlow(rest) {
	console.log("StartStormAlarmFlow: sending rest " + rest);
	sendRest(rest);
}

function manageStopStormAlarmFlow(rest) {
	console.log("StopStormAlarmFlow: sending rest " + rest);
	sendRest(rest);
}

function manageChangeNormalAlarmThroughput(rest) {
	var value = getSelectedNormalAlarmThroughputValue();
	if (!isNumber(value)) {
		var msg = "Base Alarm Throughput must be a valid number ! "+ value;
		window.alert(msg);
		console.log(msg);
		return;
	}
	value = parseFloat(value);
	var min = getSelectedNormalAlarmThroughputMinimum();
	var max = getSelectedNormalAlarmThroughputMaximum();
	if (value < min || value > max) {
		var msg = 'Base Alarm Throughput is out of range (' + min + '-'+ max + ') ! '+ value;
		window.alert(msg);
		console.log(msg);
		return;
	}
	rest = rest + "?normalAlarmThroughput=" + value;
	console.log("ChangeNormalAlarmThroughput: sending rest " + rest);
	sendRest(rest);
}

function manageChangeNormalEnableCeaseAlarmPercentage(rest){
	var value = getSelectedNormalEnableCeaseAlarmPercentageValue();
	enableNormalEnableCeaseAlarmPercentage(value);
	rest = rest + "?normalEnableCeaseAlarmPercentage=" + value;
	console.log("ChangeNormalEnableCeaseAlarmPercentage: sending rest " + rest);
	sendRest(rest);
}

function manageChangeNormalCeaseAlarmPercentage(rest){
	var value = getSelectedNormalCeaseAlarmPercentageValue();
	if (!isNumber(value)) {
		var msg = "Base Cease Alarm Percentage must be a valid % ! "+ value;
		window.alert(msg);
		console.log(msg);
		return;
	}
	value = parseFloat(value);
	var min = 1;
	var max = 50;
	if (value < min || value > max) {
		var msg = 'Base Cease Alarm Percentage is out of range (' + min + '-'+ max + ') ! '+ value;
		window.alert(msg);
		console.log(msg);
		return;
	}
	rest = rest + "?normalCeaseAlarmPercentage=" + value;
	console.log("ChangeNormalCeaseAlarmPercentage: sending rest " + rest);
	sendRest(rest);
}

function manageChangePeakAlarmThroughput(rest) {
	var value = getSelectedPeakAlarmThroughputValue();
	if (!isNumber(value)) {
		var msg = "Peak Alarm Throughput must be a valid number ! "+ value;
		window.alert(msg);
		console.log(msg);
		return;
	}
	value = parseFloat(value);
	var min = getSelectedPeakAlarmThroughputMinimum();
	var max = getSelectedPeakAlarmThroughputMaximum();
	if (value < min || value > max) {
		var msg = 'Peak Alarm Throughput is out of range ('+ min + '-' + max + ') ! '+ value;
		window.alert(msg);
		console.log(msg);
		return;
	}
	rest = rest + "?peakAlarmThroughput=" + value;
	console.log("ChangePeakAlarmThroughput: sending rest " + rest);
	sendRest(rest);
}

function manageChangePeakEnableCeaseAlarmPercentage(rest){
	var value = getSelectedPeakEnableCeaseAlarmPercentageValue();
	enablePeakEnableCeaseAlarmPercentage(value);
	rest = rest + "?peakEnableCeaseAlarmPercentage=" + value;
	console.log("ChangePeakEnableCeaseAlarmPercentage: sending rest " + rest);
	sendRest(rest);
}

function manageChangePeakCeaseAlarmPercentage(rest){
	var value = getSelectedPeakCeaseAlarmPercentageValue();
	if (!isNumber(value)) {
		var msg = "Peak Cease Alarm Percentage must be a valid % ! "+ value;
		window.alert(msg);
		console.log(msg);
		return;
	}
	value = parseFloat(value);
	var min = 1;
	var max = 50;
	if (value < min || value > max) {
		var msg = 'Peak Cease Alarm Percentage is out of range (' + min + '-'+ max + ') ! '+ value;
		window.alert(msg);
		console.log(msg);
		return;
	}
	rest = rest + "?peakCeaseAlarmPercentage=" + value;
	console.log("ChangePeakCeaseAlarmPercentage: sending rest " + rest);
	sendRest(rest);
}

function manageChangeStormAlarmThroughput(rest) {
	var value = getSelectedStormAlarmThroughputValue();
	if (!isNumber(value)) {
		var msg = "Storm Alarm Throughput must be a valid number ! "+ value;
		window.alert(msg);
		console.log(msg);
		return;
	}
	value = parseFloat(value);
	var min = getSelectedStormAlarmThroughputMinimum();
	var max = getSelectedStormAlarmThroughputMaximum();
	if (value < min || value > max) {
		var msg = 'Storm Alarm Throughput is out of range ('+ min + '-' + max + ') ! '+ value;
		window.alert(msg);
		console.log(msg);
		return;
	}
	rest = rest + "?stormAlarmThroughput=" + value;
	console.log("ChangeStormAlarmThroughput: sending rest " + rest);
	sendRest(rest);
}

function manageChangeStormEnableCeaseAlarmPercentage(rest){
	var value = getSelectedStormEnableCeaseAlarmPercentageValue();
	enableStormEnableCeaseAlarmPercentage(value);
	rest = rest + "?stormEnableCeaseAlarmPercentage=" + value;
	console.log("ChangeStormEnableCeaseAlarmPercentage: sending rest " + rest);
	sendRest(rest);
}

function manageChangeStormCeaseAlarmPercentage(rest){
	var value = getSelectedStormCeaseAlarmPercentageValue();
	if (!isNumber(value)) {
		var msg = "Storm Cease Alarm Percentage must be a valid % ! "+ value;
		window.alert(msg);
		console.log(msg);
		return;
	}
	value = parseFloat(value);
	var min = 1;
	var max = 50;
	if (value < min || value > max) {
		var msg = 'Storm Cease Alarm Percentage is out of range (' + min + '-'+ max + ') ! '+ value;
		window.alert(msg);
		console.log(msg);
		return;
	}
	rest = rest + "?stormCeaseAlarmPercentage=" + value;
	console.log("ChangeStormCeaseAlarmPercentage: sending rest " + rest);
	sendRest(rest);
}

function manageStartRemoveAllNormalAlarmFlow(rest) {
	console.log("StartRemoveAllNormalAlarmFlow: sending rest " + rest);
	sendRest(rest);
}

function manageStopRemoveAllNormalAlarmFlow(rest) {
	console.log("StopRemoveAllNormalAlarmFlow: sending rest " + rest);
	sendRest(rest);
}

function manageStartRemoveAllPeakAlarmFlow(rest) {
	console.log("StartRemoveAllPeakAlarmFlow: sending rest " + rest);
	sendRest(rest);
}

function manageStopRemoveAllPeakAlarmFlow(rest) {
	console.log("StopRemoveAllPeakAlarmFlow: sending rest " + rest);
	sendRest(rest);
}

function manageStartRemoveAllStormAlarmFlow(rest) {
	console.log("StartRemoveAllStormAlarmFlow: sending rest " + rest);
	sendRest(rest);
}

function manageStopRemoveAllStormAlarmFlow(rest) {
	console.log("StopRemoveAllStormAlarmFlow: sending rest " + rest);
	sendRest(rest);
}

function manageStartRemoveNormalFlow(rest){
	console.log("StartRemoveNormalFlow: sending rest " + rest);
	sendRest(rest);
}

function manageStopRemoveNormalFlow(rest){
	console.log("StopRemoveNormalFlow: sending rest " + rest);
	sendRest(rest);
}

function manageChangeThresholdAlarmNormalPercentage(rest){
	var value = getSelectedThresholdAlarmNormalPercentageValue();
	if (!isNumber(value)) {
		var msg = "Base Threshold Alarm Percentage must be a valid % ! "+ value;
		window.alert(msg);
		console.log(msg);
		return;
	}
	value = parseFloat(value);
	var min = 1;
	var max = 50;
	if (value < min || value > max) {
		var msg = 'Base Threshold Alarm Percentage is out of range (' + min + '-'+ max + ') ! '+ value;
		window.alert(msg);
		console.log(msg);
		return;
	}
	rest = rest + "?thresholdAlarmNormalPercentage=" + value;
	console.log("ChangeThresholdAlarmNormalPercentage: sending rest " + rest);
	sendRest(rest);
}

function manageChangeThresholdAlarmNormalNumber(rest){
	console.log("ChangeThresholdAlarmNormalNumber: sending rest " + rest);
	var num = $('#ThresholdAlarmNormalNumber').val();
	rest = rest + "?thresholdAlarmNormalNumber=" + num;
	sendRest(rest);
}

function manageStartRemovePeakFlow(rest){
	console.log("StartRemovePeakFlow: sending rest " + rest);
	sendRest(rest);
}

function manageStopRemovePeakFlow(rest){
	console.log("StopRemovePeakFlow: sending rest " + rest);
	sendRest(rest);
}

function manageChangeThresholdAlarmPeakPercentage(rest){
	var value = getSelectedThresholdAlarmPeakPercentageValue();
	if (!isNumber(value)) {
		var msg = "Peak Threshold Alarm Percentage must be a valid % ! "+ value;
		window.alert(msg);
		console.log(msg);
		return;
	}
	value = parseFloat(value);
	var min = 1;
	var max = 50;
	if (value < min || value > max) {
		var msg = 'Peak Threshold Alarm Percentage is out of range (' + min + '-'+ max + ') ! '+ value;
		window.alert(msg);
		console.log(msg);
		return;
	}
	rest = rest + "?thresholdAlarmPeakPercentage=" + value;
	console.log("ChangeThresholdAlarmPeakPercentage: sending rest " + rest);
	sendRest(rest);
}

function manageChangeThresholdAlarmPeakNumber(rest){
	var num = $('#ThresholdAlarmPeakNumber').val();
	rest = rest + "?thresholdAlarmPeakNumber=" + num;
	console.log("ChangeThresholdAlarmPeakNumber: sending rest " + rest);
	sendRest(rest);
}

function manageChangePeakOnAlarmPeriod(rest) {
	var valueInHour = getSelectedPeakOnAlarmPeriodInHourValue();
	var valueInMin = getSelectedPeakOnAlarmPeriodInMinValue();
	if (!isNumber(valueInHour)) {
		var msg = "Peak Alarm On Period in Hour must be a valid number ! " + valueInHour;
		window.alert(msg);
		console.log(msg);
		return;
	}
	valueInHour = parseFloat(valueInHour);
	if (!isNumber(valueInMin)) {
		var msg = "Peak Alarm On Period in Min must be a valid number ! " + valueInMin;
		window.alert(msg);
		console.log(msg);
		return;
	}
	valueInMin = parseFloat(valueInMin);
	var min = getSelectedPeakOnAlarmPeriodInHourMinimum();
	var max = getSelectedPeakOnAlarmPeriodInHourMaximum();
	if (valueInHour < min || valueInHour > max) {
		var msg = 'Peak Alarm On Period in Hour is out of range ('+ min + '-' + max + ') ! ' + valueInHour;
		window.alert(msg);
		console.log(msg);
		return;
	}
	min = getSelectedPeakOnAlarmPeriodInMinMinimum();
	max = getSelectedPeakOnAlarmPeriodInMinMaximum();
	if (valueInMin < min || valueInMin > max) {
		var msg = 'Peak Alarm On Period in Min is out of range ('+ min + '-' + max + ') ! ' + valueInMin;
		window.alert(msg);
		console.log(msg);
		return;
	}
	var value = valueInHour * 60 + valueInMin;
	rest = rest + "?peakOnAlarmPeriod=" + value;
	console.log("ChangePeakOnAlarmPeriod: sending rest " + rest);
	sendRest(rest);
}

function manageChangePeakOffAlarmPeriod(rest) {
	var valueInHour = getSelectedPeakOffAlarmPeriodInHourValue();
	var valueInMin = getSelectedPeakOffAlarmPeriodInMinValue();
	if (!isNumber(valueInHour)) {
		var msg = "Peak Alarm Off Period in Hour must be a valid number ! " + valueInHour;
		window.alert(msg);
		console.log(msg);
		return;
	}
	valueInHour = parseFloat(valueInHour);
	if (!isNumber(valueInMin)) {
		var msg = "Peak Alarm Off Period in Min must be a valid number ! " + valueInMin;
		window.alert(msg);
		console.log(msg);
		return;
	}
	valueInMin = parseFloat(valueInMin);
	var min = getSelectedPeakOffAlarmPeriodInHourMinimum();
    var max = getSelectedPeakOffAlarmPeriodInHourMaximum();
	if (valueInHour < min || valueInHour > max) {
		var msg = "Peak Alarm Off Period in Hour is out of range (0-11) ! " + valueInHour;
		window.alert(msg);
		console.log(msg);
		return;
	}
	min = getSelectedPeakOffAlarmPeriodInMinMinimum();
    max = getSelectedPeakOffAlarmPeriodInMinMaximum();
	if (valueInMin < min || valueInMin > max) {
		var msg = "Peak Alarm Off Period in Min is out of range (1-59) ! " + valueInMin;
		window.alert(msg);
		console.log(msg);
		return;
	}
	var value = valueInHour * 60 + valueInMin;
	rest = rest + "?peakOffAlarmPeriod=" + value;
	console.log("ChangePeakOffAlarmPeriod: sending rest " + rest);
	sendRest(rest);
}

function manageStartRemoveStormFlow(rest){
	console.log("StartRemoveStormFlow: sending rest " + rest);
	sendRest(rest);
}

function manageStopRemoveStormFlow(rest){
	console.log("StopRemoveStormFlow: sending rest " + rest);
	sendRest(rest);
}

function manageChangeThresholdAlarmStormPercentage(rest){
	var value = getSelectedThresholdAlarmStormPercentageValue();
	if (!isNumber(value)) {
		var msg = "Storm Threshold Alarm Percentage must be a valid % ! "+ value;
		window.alert(msg);
		console.log(msg);
		return;
	}
	value = parseFloat(value);
	var min = 1;
	var max = 50;
	if (value < min || value > max) {
		var msg = 'Storm Threshold Alarm Percentage is out of range (' + min + '-'+ max + ') ! '+ value;
		window.alert(msg);
		console.log(msg);
		return;
	}
	rest = rest + "?thresholdAlarmStormPercentage=" + value;
	console.log("ChangeThresholdAlarmStormPercentage: sending rest " + rest);
	sendRest(rest);
}

function manageChangeThresholdAlarmStormNumber(rest){
	var num = $('#ThresholdAlarmStormNumber').val();
	rest = rest + "?thresholdAlarmStormNumber=" + num;
	console.log("ChangeThresholdAlarmStormNumber: sending rest " + rest);
	sendRest(rest);
}

function manageChangeStormOnAlarmPeriod(rest) {
	var valueInHour = getSelectedStormOnAlarmPeriodInHourValue();
	var valueInMin = getSelectedStormOnAlarmPeriodInMinValue();
	if (!isNumber(valueInHour)) {
		var msg = "Storm Alarm On Period in Hour must be a valid number ! " + valueInHour;
		window.alert(msg);
		console.log(msg);
		return;
	}
	valueInHour = parseFloat(valueInHour);
	if (!isNumber(valueInMin)) {
		var msg = "Storm Alarm On Period in Min must be a valid number ! " + valueInMin;
		window.alert(msg);
		console.log(msg);
		return;
	}
	valueInMin = parseFloat(valueInMin);
	var min = getSelectedStormOnAlarmPeriodInHourMinimum();
	var max = getSelectedStormOnAlarmPeriodInHourMaximum();
	if (valueInHour < min || valueInHour > max) {
		var msg = 'Storm Alarm On Period in Hour is out of range ('+ min + '-' + max + ') ! ' + valueInHour;
		window.alert(msg);
		console.log(msg);
		return;
	}
	min = getSelectedStormOnAlarmPeriodInMinMinimum();
	max = getSelectedStormOnAlarmPeriodInMinMaximum();
	if (valueInMin < min || valueInMin > max) {
		var msg = 'Storm Alarm On Period in Min is out of range ('+ min + '-' + max + ') ! ' + valueInMin;
		window.alert(msg);
		console.log(msg);
		return;
	}
	var value = valueInHour * 60 + valueInMin;
	rest = rest + "?stormOnAlarmPeriod=" + value;
	console.log("ChangeStormOnAlarmPeriod: sending rest " + rest);
	sendRest(rest);
}

function manageChangeStormOffAlarmPeriod(rest) {
	var valueInHour = getSelectedStormOffAlarmPeriodInHourValue();
	var valueInMin = getSelectedStormOffAlarmPeriodInMinValue();
	if (!isNumber(valueInHour)) {
		var msg = "Storm Alarm Off Period in Hour must be a valid number ! " + valueInHour;
		window.alert(msg);
		console.log(msg);
		return;
	}
	valueInHour = parseFloat(valueInHour);
	if (!isNumber(valueInMin)) {
		var msg = "Storm Alarm Off Period in Min must be a valid number ! " + valueInMin;
		window.alert(msg);
		console.log(msg);
		return;
	}
	valueInMin = parseFloat(valueInMin);
	var min = getSelectedStormOffAlarmPeriodInHourMinimum();
    var max = getSelectedStormOffAlarmPeriodInHourMaximum();
	if (valueInHour < min || valueInHour > max) {
		var msg = "Storm Alarm Off Period in Hour is out of range (0-11) ! " + valueInHour;
		window.alert(msg);
		console.log(msg);
		return;
	}
	min = getSelectedStormOffAlarmPeriodInMinMinimum();
    max = getSelectedStormOffAlarmPeriodInMinMaximum();
	if (valueInMin < min || valueInMin > max) {
		var msg = "Storm Alarm Off Period in Min is out of range (1-59) ! " + valueInMin;
		window.alert(msg);
		console.log(msg);
		return;
	}
	var value = valueInHour * 60 + valueInMin;
	rest = rest + "?stormOffAlarmPeriod=" + value;
	console.log("ChangeStormOffAlarmPeriod: sending rest " + rest);
	sendRest(rest);
}

function manageStartReadAlarmFlow(rest){
	console.log("StartReadAlarmFlow: sending rest " + rest);
	sendRest(rest);
}

function manageStopReadAlarmFlow(rest){
	console.log("StopReadAlarmFlow: sending rest " + rest);
	sendRest(rest);
}

function manageChangeReadParallelNumber(rest){
	console.log("ChangeReadParallelNumber: sending rest " + rest);
	var num = $('#ChangeReadParallelNumberInput').val();
	rest = rest + "?readParallelNumber=" + num;
	sendRest(rest);
}

function manageChangeReadPausePeriod(rest){
	var valueInHour = getSelectedReadPausePeriodInHourValue();
	var valueInMin = getSelectedReadPausePeriodInMinValue();
	var valueInSec = getSelectedReadPausePeriodInSecValue();
	if (!isNumber(valueInHour)) {
		var msg = "Read Pause Period in Hour must be a valid number ! " + valueInHour;
		window.alert(msg);
		console.log(msg);
		return;
	}
	valueInHour = parseFloat(valueInHour);
	if (!isNumber(valueInMin)) {
		var msg = "Read Pause Period in Min must be a valid number ! " + valueInMin;
		window.alert(msg);
		console.log(msg);
		return;
	}
	valueInMin = parseFloat(valueInMin);
	if (!isNumber(valueInSec)) {
		var msg = "Read Pause Period in Sec must be a valid number ! " + valueInSec;
		window.alert(msg);
		console.log(msg);
		return;
	}
	valueInSec = parseFloat(valueInSec);
	var value = valueInHour * 60 * 60 + valueInMin * 60 + valueInSec;
	rest = rest + "?readPausePeriod=" + value;
	console.log("ChangeReadPausePeriod: sending rest " + rest);
	sendRest(rest);
}

function manageChangeParallelAlarmNormalNumber(rest){
	var value = getSelectedParallelAlarmNormalNumberValue();
	rest = rest + "?parallelAlarmNormalNumber=" + value;
	console.log("ChangeParallelAlarmNormalNumber: sending rest " + rest);
	sendRest(rest);
}

function manageChangePackingAlarmNormalNumber(rest){
	var value = getSelectedPackingAlarmNormalNumberValue();
	rest = rest + "?packingAlarmNormalNumber=" + value;
	console.log("ChangePackingAlarmNormalNumber: sending rest " + rest);
	sendRest(rest);
}

function manageChangeParallelAlarmPeakNumber(rest){
	var value = getSelectedParallelAlarmPeakNumberValue();
	rest = rest + "?parallelAlarmPeakNumber=" + value;
	console.log("ChangeParallelAlarmPeakNumber: sending rest " + rest);
	sendRest(rest);
}

function manageChangePackingAlarmPeakNumber(rest){
	var value = getSelectedPackingAlarmPeakNumberValue();
	rest = rest + "?packingAlarmPeakNumber=" + value;
	console.log("ChangePackingAlarmPeakNumber: sending rest " + rest);
	sendRest(rest);
}

function manageChangeParallelAlarmStormNumber(rest){
	var value = getSelectedParallelAlarmStormNumberValue();
	rest = rest + "?parallelAlarmStormNumber=" + value;
	console.log("ChangeParallelAlarmStormNumber: sending rest " + rest);
	sendRest(rest);
}

function manageChangePackingAlarmStormNumber(rest){
	var value = getSelectedPackingAlarmStormNumberValue();
	rest = rest + "?packingAlarmStormNumber=" + value;
	console.log("ChangePackingAlarmStormNumber: sending rest " + rest);
	sendRest(rest);
}


function manageGetCurrentAlarmNumber(rest,graphic){
	console.log("Requesting CurrentAlarmNumber ");
	sendRest(rest,function(data){
		$('#CurrentAlarmNumberLabel').text(data.message);
		$( "#"+graphic ).effect( "highlight");
	});
}

function manageGetCurrentAlarmNumberTimerStart(rest,graphic){
	console.log("Starting timer for getCurrentAlarmNumber on 1 sec...")
	return setInterval(
			function() {
				sendRest(rest,function(data){
					$('#CurrentAlarmNumberLabel').text(data.message);
					$( "#"+graphic ).effect( "highlight");
				});				
			}, 1000);
}

function manageGetCurrentAlarmNumberTimerStop(getCurrentAlarmNumberTimerVar){
	clearInterval(getCurrentAlarmNumberTimerVar);
}	
		
function sendRest(rest, callback) {
	console.log('Sending GET request : ' + rest);
	$.ajax({
		type : "GET",
		url : rest,
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		success : function(res) {
			if (res.length && res[0].serverConfigurationContent) {
				res[0].serverConfigurationContent = "XML-CONFIGURATION-DATA";
			}
			console.log("Response json : " + JSON.stringify(res));
			if (callback)
				callback(res);
		},
		error : function(xhr, status) {
			console.log("Bad response : " + status + " ->  from request : "
					+ rest);
			console.htmlLog(xhr.responseText);
		}
	});
}

function isNumber(n) {
	return !isNaN(parseFloat(n)) && isFinite(n);
}
