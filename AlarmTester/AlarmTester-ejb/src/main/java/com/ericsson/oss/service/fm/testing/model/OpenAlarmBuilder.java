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

package com.ericsson.oss.service.fm.testing.model;

import static com.ericsson.oss.service.fm.testing.api.AdditionalAttrConstants.TREND_INDICATION;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.ACK_OPERATOR;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.ACK_TIME;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.ADDITIONAL_INFORMATION;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.ALARMING_OBJECT;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.ALARM_ID;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.ALARM_NUMBER;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.ALARM_STATE;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.BACKUP_OBJECT_INSTANCE;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.BACKUP_STATUS;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.CEASE_OPERATOR;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.CEASE_TIME;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.COMMENT_TEXT;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.CORRELATEDVISIBILITY;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.EVENT_TIME;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.EVENT_TYPE;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.FDN;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.FMX_GENERATED;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.INSERT_TIME;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.LAST_ALARM_OPERATION;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.LAST_UPDATED;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.MANUALCEASE;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.OBJECT_OF_REFERENCE;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.OSCILLATION_COUNT;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.PRESENT_SEVERITY;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.PREVIOUS_SEVERITY;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.PROBABLE_CAUSE;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.PROBLEM_DETAIL;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.PROBLEM_TEXT;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.PROCESSING_TYPE;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.PROPOSED_REPAIR_ACTION;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.RECORD_TYPE;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.REPEAT_COUNT;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.SPECIFIC_PROBLEM;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to create ProcessedAlarm object with values set from the given
 * map.
 *
 */
public class OpenAlarmBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(OpenAlarmBuilder.class);	

	public static OpenAlarm buildProcessedAlarm(final Map<String, Object> map) {
		final OpenAlarm openAlarm = new OpenAlarm();
		return buildProcessedAlarm(openAlarm, map);
	}
	
	public static OpenAlarm buildProcessedAlarm(final OpenAlarm openAlarm, final Map<String, Object> map) {		
		
		final Set<String> set = map.keySet();
		for (final String attribute : set) {
			switch (attribute) {
			case OBJECT_OF_REFERENCE:
				openAlarm.setObjectOfReference((String) map.get(attribute));
				break;
			case FDN:
				openAlarm.setFdn((String) map.get(attribute));
				break;
			case EVENT_TYPE:
				openAlarm.setEventType((String) map.get(attribute));
				break;
			case EVENT_TIME:
				openAlarm.setEventTime((Date) map.get(attribute));
				break;
			case PROBABLE_CAUSE:
				openAlarm.setProbableCause((String) map.get(attribute));
				break;
			case SPECIFIC_PROBLEM:
				openAlarm.setSpecificProblem((String) map.get(attribute));
				break;
			case BACKUP_STATUS:
				openAlarm.setBackupStatus((Boolean) map.get(attribute));
				break;
			case BACKUP_OBJECT_INSTANCE:
				openAlarm.setBackupObjectInstance((String) map.get(attribute));
				break;
			case PROPOSED_REPAIR_ACTION:
				openAlarm.setProposedRepairAction((String) map.get(attribute));
				break;
			case ALARM_NUMBER:
				openAlarm.setAlarmNumber((Long) map.get(attribute));
				break;
			case ALARM_ID:
				openAlarm.setAlarmId((Long) map.get(attribute));
				break;
			case CEASE_TIME:
				openAlarm.setCeaseTime((Date) map.get(attribute));
				break;
			case MANUALCEASE:
				openAlarm.setManualCease((Boolean) map.get(attribute));
				break;
			case CEASE_OPERATOR:
				openAlarm.setCeaseOperator((String) map.get(attribute));
				break;
			case ACK_TIME:
				openAlarm.setAckTime((Date) map.get(attribute));
				break;
			case ACK_OPERATOR:
				openAlarm.setAckOperator((String) map.get(attribute));
				break;
			case INSERT_TIME:
				openAlarm.setInsertTime((Date) map.get(attribute));
				break;
			case COMMENT_TEXT:
				openAlarm.setCommentText((String) map.get(attribute));
				break;
			case LAST_UPDATED:
				openAlarm.setLastUpdated((Date) map.get(attribute));
				break;
			case CORRELATEDVISIBILITY:
				openAlarm.setCorrelatedVisibility((Boolean) map.get(attribute));
				break;
			case FMX_GENERATED:
				openAlarm.setFmxGenerated((String) map.get(attribute));
				break;
			case PROCESSING_TYPE:
				openAlarm.setProcessingType((String) map.get(attribute));
				break;
			case LAST_ALARM_OPERATION:
				if (map.get(attribute) != null) {
					openAlarm.setLastAlarmOperation(LastAlarmOperation.valueOf(map.get(attribute)));
				}
				break;
			case REPEAT_COUNT:
				openAlarm.setRepeatCount((int) map.get(attribute));
				break;
			case OSCILLATION_COUNT:
				openAlarm.setOscillationCount((int) map.get(attribute));
				break;
			case PROBLEM_TEXT:
				openAlarm.setProblemText((String) map.get(attribute));
				break;
			case PROBLEM_DETAIL:
				openAlarm.setProblemDetail((String) map.get(attribute));
				break;
			case ADDITIONAL_INFORMATION:
				final String correlationInformation = rebuildCorrelationInformation(map);
				if (correlationInformation != null) {
					openAlarm.setAdditionalInformation(correlationInformation);
				} else {
					openAlarm.setAdditionalInformation((String) map.get(attribute));
				}
				break;
			case PRESENT_SEVERITY:
				openAlarm.setPresentSeverity(EventSeverity.valueOf(map.get(attribute)));
				break;
			case PREVIOUS_SEVERITY:
				if (map.get(attribute) != null) {
					openAlarm.setPreviousSeverity(EventSeverity.valueOf(map.get(attribute)));
				}
				break;
			case ALARMING_OBJECT:
				openAlarm.setAlarmingObject((String) map.get(attribute));
				break;
			case RECORD_TYPE:
				openAlarm.setRecordType(AlarmRecordType.valueOf(map.get(attribute)));
				break;
			case ALARM_STATE:
				openAlarm.setAlarmState(EventState.valueOf(map.get(attribute)));
				break;
			case TREND_INDICATION:
				if (map.get(attribute) != null) {
					openAlarm.setTrendIndication(EventTrendIndication.valueOf(map.get(attribute)));
				}
				break;
			}
		}
		return openAlarm;
	}

	private static String rebuildCorrelationInformation(final Map<String, Object> alarmAttributeMap) {
		LOGGER.debug("Rebuilding Correlation Information of alarmAttributeMap = {}", alarmAttributeMap);
		String enrichedAdditionalInfo = null;
		
		enrichedAdditionalInfo = alarmAttributeMap.get(ADDITIONAL_INFORMATION).toString();
		LOGGER.debug("Correlation Information rebuilt. enrichedAdditionalInfo = {}", enrichedAdditionalInfo);
		return enrichedAdditionalInfo;
	}
}
