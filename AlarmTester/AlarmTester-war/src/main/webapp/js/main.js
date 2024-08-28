
// it happens when the document of the window has been loaded ...
function documentReady(){
	// set console variable for output ...
	logHtmlElement = $('#log');
	console.log('Loading Alarm Tester ... ');
	$( "#tabs" ).tabs();
	$('body').layout({

		//	reference only - these options are NOT required because 'true' is the default
			closable:					true	// pane can open & close
		,	resizable:					true	// when open, pane can be resized 
		,	slidable:					true	// when closed, pane can 'slide' open over other panes - closes on mouse-out
		,	livePaneResizing:			true

		,	north__togglerLength_closed: '100%'	// toggle-button is full-width of resizer-bar
		,	north__spacing_closed:		10		// big resizer-bar when open (zero height)
		,	north__minHeight:			100
		,	center__minHeight:			100

		//	enable state management
		,	stateManagement__enabled:	true // automatic cookie load & save enabled by default
	});
	
	init();
}


function init() {
    enableNormalEnableCeaseAlarmPercentage(false);
    enablePeakEnableCeaseAlarmPercentage(false);
    enableStormEnableCeaseAlarmPercentage(false);

    // base
    // default normal throughput set to 30
    $('#ChangeNormalThroughputInput').val("30");
    $('#ChangeNormalThroughput').click();
    // default disable normal cease alarm percentage
    $('#ChangeNormalEnableCeasePercentageCheckbox').prop('checked', true);
    $('#ChangeNormalEnableCeasePercentageCheckbox').click();
    // default normal cease alarm percentage set to 10
    $('#ChangeNormalCeasePercentageInput').val("10");
    $('#ChangeNormalCeasePercentage').click();
    // default normal parallel number set to 20
    $('#ParallelAlarmNormalNumber').val("20");
    $('#ChangeParallelAlarmNormalNumber').click();
    // default normal packing alarm number set to 10
    $('#PackingAlarmNormalNumber').val("10");
    $('#ChangePackingAlarmNormalNumber').click();
    // default normal threshold percentage set to 30
    $('#ThresholdAlarmNormalPercentage').val("30");
    $('#ChangeThresholdAlarmNormalPercentage').click();
    // default normal threshold limit number set to 1000
    $('#ThresholdAlarmNormalNumber').val("200000");
    $('#ChangeThresholdAlarmNormalNumber').click();
    
    // peak
    // default peak throughput set to 50
    $('#ChangePeakThroughputInput').val("50");
    $('#ChangePeakThroughput').click();
    // default disable peak cease alarm percentage
    $('#ChangePeakEnableCeasePercentageCheckbox').prop('checked',true);
    $('#ChangePeakEnableCeasePercentageCheckbox').click();
    // default peak alarm percentage set to 10
    $('#ChangePeakCeasePercentageInput').val("10");
    $('#ChangePeakCeasePercentage').click();
    // default peak hour 0 and min 15 ON period
    $('#ChangePeakOnPeriodInHourInput').val("0");
    $('#ChangePeakOnPeriodInMinInput').val("15");
    $('#ChangePeakOnPeriod').click();
    // default peak hour 0 and min 15 OFF period
    $('#ChangePeakOffPeriodInHourInput').val("0");
    $('#ChangePeakOffPeriodInMinInput').val("15");
    $('#ChangePeakOffPeriod').click();
    // default peak parallel number set to 20
    $('#ParallelAlarmPeakNumber').val("20");
    $('#ChangeParallelAlarmPeakNumber').click();
    // default peak packing alarm number set to 10
    $('#PackingAlarmPeakNumber').val("10");
    $('#ChangePackingAlarmPeakNumber').click();
    // default peak threshold percentage set to 30
    $('#ThresholdAlarmPeakPercentage').val("30");
    $('#ChangeThresholdAlarmPeakPercentage').click();
    // default peak threshold limit number set to 1000
    $('#ThresholdAlarmPeakNumber').val("200000");
    $('#ChangeThresholdAlarmPeakNumber').click();

    
    // storm
    // default storm throughput set to 50
    $('#ChangeStormThroughputInput').val("50");
    $('#ChangeStormThroughput').click();
    // default disable storm cease alarm percentage
    $('#ChangeStormEnableCeasePercentageCheckbox').prop('checked',true);
    $('#ChangeStormEnableCeasePercentageCheckbox').click();
    // default storm alarm percentage set to 10
    $('#ChangeStormCeasePercentageInput').val("10");
    $('#ChangeStormCeasePercentage').click();
    // default storm hour 0 and min 15 ON period
    $('#ChangeStormOnPeriodInHourInput').val("0");
    $('#ChangeStormOnPeriodInMinInput').val("15");
    $('#ChangeStormOnPeriod').click();
    // default storm hour 0 and min 15 OFF period
    $('#ChangeStormOffPeriodInHourInput').val("0");
    $('#ChangeStormOffPeriodInMinInput').val("15");
    $('#ChangeStormOffPeriod').click();
    // default storm parallel number set to 20
    $('#ParallelAlarmStormNumber').val("20");
    $('#ChangeParallelAlarmStormNumber').click();
    // default storm packing alarm number set to 10
    $('#PackingAlarmStormNumber').val("10");
    $('#ChangePackingAlarmStormNumber').click();
    // default storm threshold percentage set to 30
    $('#ThresholdAlarmStormPercentage').val("30");
    $('#ChangeThresholdAlarmStormPercentage').click();
    // default storm threshold limit number set to 1000
    $('#ThresholdAlarmStormNumber').val("200000");
    $('#ChangeThresholdAlarmStormNumber').click();
    
    // read
    // default read parallel number is set to 10
    $('#ChangeReadParallelNumberInput').val("10");
    $('#ChangeReadParallelNumber').click();
    // default read pause hour 0 min 0 sec 0
    $('#ChangeReadPausePeriodInHourInput').val("0");
    $('#ChangeReadPausePeriodInMinInput').val("0");
    $('#ChangeReadPausePeriodInSecInput').val("0");
    $('#ChangeReadPausePeriod').click();
}

function enableNormalEnableCeaseAlarmPercentage(state) {
	// enable/disable percentage controls
	$('#ChangeNormalCeasePercentageInput').prop('disabled', !state);
	$('#ChangeNormalCeasePercentage').prop('disabled', !state);

}

function enablePeakEnableCeaseAlarmPercentage(state) {
	// enable/disable percentage controls
	$('#ChangePeakCeasePercentageInput').prop('disabled', !state);
	$('#ChangePeakCeasePercentage').prop('disabled', !state);

}

function enableStormEnableCeaseAlarmPercentage(state) {
	// enable/disable percentage controls
	$('#ChangeStormCeasePercentageInput').prop('disabled', !state);
	$('#ChangeStormCeasePercentage').prop('disabled', !state);

}

function onCommandAction(object,graphic){
    // "key" name used to identify command
    var key = $.map(object.attributes,function(val){
        if(val.name === "key"){
            return val.value;
        }
    })[0];
    console.log("Key: " + key);
	// rest url
    var rest = $.map(object.attributes,function(val){
        if(val.name === "rest"){
            return val.value;
        }
    })[0];
	rest = window.location.href + rest;
    console.log("Rest url: " + rest);
    commandAction(key,graphic,rest);
}
