/*------------------------------------------------------------------------------
*******************************************************************************
* COPYRIGHT Ericsson 2016
*
* The copyright to the computer program(s) herein is the property of
* Ericsson AB. The programs may be used and/or copied only with written
* permission from Ericsson Inc. or in accordance with the terms and
* conditions stipulated in the agreement/contract under which the
* program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.oss.service.fm.testing.api;

/**
 * Class containing constants related to FM alarm additional attributes.
 */
public final class AdditionalAttrConstants {

	public static final String ADDITIONAL_TEXT = "additionalText";
	public static final String BACKEDUP_STATUS = "backedUpStatus";
	public static final String BACKUP_OBJECT = "backUpObject";
	public static final String TREND_INDICATION = "trendIndication";
	public static final String ACKNOWLEDGER = "acknowledger";
	public static final String ACKNOWLEDGE_TIME = "acknowledgeTime";
	public static final String MANAGED_OBJECT = "managedObject";
	public static final String GENERATED_ALARM_ID = "generatedAlarmId";
	public static final String NOTIFY_CHANGED_ALARM = "notifyChangedAlarm";
	public static final String SOURCE_TYPE = "sourceType";
	public static final String EVENT_PO_ID = "eventPoId";
	public static final String EVENT_AGENT_ID = "eventAgentId";
	public static final String EXTERNAL_EVENT_ID = "externalEventId";
	public static final String TRANSLATE_RESULT = "translateResult";
	public static final String OPERATOR = "operator";

	public static final String PROPOSED_REPAIR_ACTIONS = "proposedRepairActions";
	public static final String EXT_ACKNOWLEDGER = "extacknowledger";
	public static final String EXT_ACKNOWLEDGE_TIME = "extacknowledgetime";
	public static final String EXT_PRA = "extproposedRepairActions";

	public static final String OSSPREFIX_NOT_SET = "ossPrefixNotSet";
	public static final String ORIGINAL_EVENTTIME_FROM_NODE = "originalEventTimeFromNode";
	public static final String ORIGINAL_RECORD_TYPE = "originalRecordType";

	private AdditionalAttrConstants() {
	}
}
