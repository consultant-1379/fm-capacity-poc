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
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.CORRELATED_EVENT_PO_ID;
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
public class ProcessedAlarmEventBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessedAlarmEventBuilder.class);

	public static final String EVENT_PO_ID = "eventPoId";

	public static ProcessedAlarmEvent buildProcessedAlarm(final Map<String, Object> map) {
		final ProcessedAlarmEvent processedAlarmEvent = new ProcessedAlarmEvent();
		final Set<String> set = map.keySet();
		for (final String attribute : set) {
			switch (attribute) {
			case OBJECT_OF_REFERENCE:
				processedAlarmEvent.setObjectOfReference((String) map.get(attribute));
				break;
			case FDN:
				processedAlarmEvent.setFdn((String) map.get(attribute));
				break;
			case EVENT_TYPE:
				processedAlarmEvent.setEventType((String) map.get(attribute));
				break;
			case EVENT_TIME:
				processedAlarmEvent.setEventTime((Date) map.get(attribute));
				break;
			case PROBABLE_CAUSE:
				processedAlarmEvent.setProbableCause((String) map.get(attribute));
				break;
			case SPECIFIC_PROBLEM:
				processedAlarmEvent.setSpecificProblem((String) map.get(attribute));
				break;
			case BACKUP_STATUS:
				processedAlarmEvent.setBackupStatus((Boolean) map.get(attribute));
				break;
			case BACKUP_OBJECT_INSTANCE:
				processedAlarmEvent.setBackupObjectInstance((String) map.get(attribute));
				break;
			case PROPOSED_REPAIR_ACTION:
				processedAlarmEvent.setProposedRepairAction((String) map.get(attribute));
				break;
			case ALARM_NUMBER:
				processedAlarmEvent.setAlarmNumber((Long) map.get(attribute));
				break;
			case ALARM_ID:
				processedAlarmEvent.setAlarmId((Long) map.get(attribute));
				break;
			case CEASE_TIME:
				processedAlarmEvent.setCeaseTime((Date) map.get(attribute));
				break;
			case MANUALCEASE:
				processedAlarmEvent.setManualCease((Boolean) map.get(attribute));
				break;
			case CEASE_OPERATOR:
				processedAlarmEvent.setCeaseOperator((String) map.get(attribute));
				break;
			case ACK_TIME:
				processedAlarmEvent.setAckTime((Date) map.get(attribute));
				break;
			case ACK_OPERATOR:
				processedAlarmEvent.setAckOperator((String) map.get(attribute));
				break;
			case INSERT_TIME:
				processedAlarmEvent.setInsertTime((Date) map.get(attribute));
				break;
			case CORRELATED_EVENT_PO_ID:
				processedAlarmEvent.setCorrelatedPOId((Long) map.get(attribute));
				break;
			case EVENT_PO_ID:
				processedAlarmEvent.setEventPOId((Long) map.get(attribute));
				break;
			case COMMENT_TEXT:
				processedAlarmEvent.setCommentText((String) map.get(attribute));
				break;
			case LAST_UPDATED:
				processedAlarmEvent.setLastUpdated((Date) map.get(attribute));
				break;
			case CORRELATEDVISIBILITY:
				processedAlarmEvent.setCorrelatedVisibility((Boolean) map.get(attribute));
				break;
			case FMX_GENERATED:
				processedAlarmEvent.setFmxGenerated((String) map.get(attribute));
				break;
			case PROCESSING_TYPE:
				processedAlarmEvent.setProcessingType((String) map.get(attribute));
				break;
			case LAST_ALARM_OPERATION:
				if (map.get(attribute) != null) {
					processedAlarmEvent.setLastAlarmOperation(LastAlarmOperation.valueOf(map.get(attribute)));
				}
				break;
			case REPEAT_COUNT:
				processedAlarmEvent.setRepeatCount((int) map.get(attribute));
				break;
			case OSCILLATION_COUNT:
				processedAlarmEvent.setOscillationCount((int) map.get(attribute));
				break;
			case PROBLEM_TEXT:
				processedAlarmEvent.setProblemText((String) map.get(attribute));
				break;
			case PROBLEM_DETAIL:
				processedAlarmEvent.setProblemDetail((String) map.get(attribute));
				break;
			case ADDITIONAL_INFORMATION:
				final String correlationInformation = rebuildCorrelationInformation(map);
				if (correlationInformation != null) {
					processedAlarmEvent.setAdditionalInformationToMap(correlationInformation);
				} else {
					processedAlarmEvent.setAdditionalInformationToMap((String) map.get(attribute));
				}
				break;
			case PRESENT_SEVERITY:
				processedAlarmEvent.setPresentSeverity(EventSeverity.valueOf(map.get(attribute)));
				break;
			case PREVIOUS_SEVERITY:
				if (map.get(attribute) != null) {
					processedAlarmEvent.setPreviousSeverity(EventSeverity.valueOf(map.get(attribute)));
				}
				break;
			case ALARMING_OBJECT:
				processedAlarmEvent.setAlarmingObject((String) map.get(attribute));
				break;
			case RECORD_TYPE:
				processedAlarmEvent.setRecordType(AlarmRecordType.valueOf(map.get(attribute)));
				break;
			case ALARM_STATE:
				processedAlarmEvent.setAlarmState(EventState.valueOf(map.get(attribute)));
				break;
			case TREND_INDICATION:
				if (map.get(attribute) != null) {
					processedAlarmEvent.setTrendIndication(EventTrendIndication.valueOf(map.get(attribute)));
				}
				break;
			}
		}
		return processedAlarmEvent;
	}

	private static String rebuildCorrelationInformation(final Map<String, Object> alarmAttributeMap) {
		LOGGER.debug("Rebuilding Correlation Information of alarmAttributeMap = {}", alarmAttributeMap);
		String enrichedAdditionalInfo = null;
		
		enrichedAdditionalInfo = alarmAttributeMap.get(ADDITIONAL_INFORMATION).toString();
		LOGGER.debug("Correlation Information rebuilt. enrichedAdditionalInfo = {}", enrichedAdditionalInfo);
		return enrichedAdditionalInfo;
	}
}
