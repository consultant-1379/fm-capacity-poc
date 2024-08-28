/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2017
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson AB. The programs may be used and/or copied only with written
 * permission from Ericsson AB. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.oss.services.fm.testing.utils;

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
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.COMMENT_OPERATOR;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.COMMENT_TEXT;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.COMMENT_TIME;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.CORRELATEDVISIBILITY;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.CORRELATED_EVENT_PO_ID;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.EVENT_TIME;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.EVENT_TYPE;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.FDN;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.FMX_GENERATED;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.INSERT_TIME;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.LAST_ALARM_OPERATION;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.LAST_DELIVERED;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.MANUALCEASE;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.OBJECT_OF_REFERENCE;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.OSCILLATION_COUNT;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.PROBABLE_CAUSE;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.PROBLEM_DETAIL;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.PROBLEM_TEXT;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.PROCESSING_TYPE;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.PROPOSED_REPAIR_ACTION;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.PSEUDO_PRESENT_SEVERITY;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.PSEUDO_PREVIOUS_SEVERITY;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.RECORD_TYPE;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.REPEAT_COUNT;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.SPECIFIC_PROBLEM;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.SYNC_STATE;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.TREND_INDICATION;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttributes.LAST_UPDATED;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttributes.PRESENT_SEVERITY;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttributes.PREVIOUS_SEVERITY;
import static com.ericsson.oss.service.fm.testing.api.AlarmProcessorConstants.APS;
import static com.ericsson.oss.service.fm.testing.api.Constants.EMPTY_STRING;
import static com.ericsson.oss.service.fm.testing.api.Constants.UNDER_SCORE;
import static com.ericsson.oss.service.fm.testing.api.Constants.VISIBILITY;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.service.fm.testing.api.AlarmProcessorConstants;
import com.ericsson.oss.service.fm.testing.model.EventSeverity;
import com.ericsson.oss.service.fm.testing.model.EventState;
import com.ericsson.oss.service.fm.testing.model.LastAlarmOperation;
import com.ericsson.oss.service.fm.testing.model.ProcessedAlarmEvent;
import com.ericsson.oss.service.fm.testing.model.PseudoSeverities;

/**
 * Class for populating alarm attributes from {@link ProcessedAlarmEvent}.
 */
public class AlarmAttributesPopulator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmAttributesPopulator.class);

    /**
     * Method converts ProcessedAlarmEvent to a Map This method is applicable only for existing OpenAlarm Object.
     *
     * @param alarmRecord
     *            {@link ProcessedAlarmEvent}
     * @return openAlarmAttributes {@link Map}
     */
    public static Map<String, Object> populateUpdateAlarm(final ProcessedAlarmEvent alarmRecord) {
        final Map<String, Object> alarmAttributes = getAlarmAttributes(alarmRecord);
        alarmAttributes.put(LAST_ALARM_OPERATION, alarmRecord.getLastAlarmOperation().name());
        alarmAttributes.put(MANUALCEASE, alarmRecord.getManualCease());
        if (alarmRecord.getAdditionalInformation().get(REPEAT_COUNT) != null) {
            alarmAttributes.put(REPEAT_COUNT, Integer.valueOf(alarmRecord.getAdditionalInformation().get(REPEAT_COUNT)));
        }
        if (alarmRecord.getAdditionalInformation().get(OSCILLATION_COUNT) != null) {
            alarmAttributes.put(OSCILLATION_COUNT, Integer.valueOf(alarmRecord.getAdditionalInformation().get(OSCILLATION_COUNT)));
        }
        return alarmAttributes;
    }

    /**
     * Method populates alarm attributes for replacing an existing alarm in the database.<br>
     * RepeatCount is incremented by one except in the case of FMX hide operation.
     * <p>
     * CommentTime,InsertTime,CommentOperator, Visibility and CorrelatedVisibility values are restored to previous values present in
     * database.(Shouldn't be updated to the new value).
     * <p>
     * Attributes present in additionalInformation of an alarm are to be retained if and only if they are not present in incoming alarm.
     *
     * @param alarmRecord
     *            {@link ProcessedAlarmEvent}
     * @param poAttributes
     *            (@link Map}
     * @return map of attributes to replace in database.
     */
    public static Map<String, Object> populateUpdateAlarm(final ProcessedAlarmEvent alarmRecord, final Map<String, Object> poAttributes) {
        // Initializing to the default values
        boolean previousVisibility = true;
        boolean previousCorrelatedVisibility = false;

        Date previousCommentTime = new Date();
        String previousCommentOperator = EMPTY_STRING;
        String previousCommentText = EMPTY_STRING;
        int repeatCount = 0;
        if (poAttributes != null && !poAttributes.isEmpty()) {
            repeatCount = (int) poAttributes.get(REPEAT_COUNT);
            previousCommentTime = (Date) poAttributes.get(COMMENT_TIME);
            previousCommentOperator = (String) poAttributes.get(COMMENT_OPERATOR);
            previousCommentText = (String) poAttributes.get(COMMENT_TEXT);
            ++repeatCount;
            final Date oldInsertTime = (Date) poAttributes.get(INSERT_TIME);
            setInsertTime(alarmRecord, oldInsertTime);
            previousVisibility = (boolean) poAttributes.get(VISIBILITY);
            previousCorrelatedVisibility = (boolean) poAttributes.get(CORRELATEDVISIBILITY);
        }
        final Map<String, String> additionalInformation = alarmRecord.getAdditionalInformation();
        // for show request on hidden repeated alarm from FMX.we are simply restricting
        // not to increment repeat count and changing alarm visibility.
        if (additionalInformation != null) {
            alarmRecord.setVisibility(true);
            alarmRecord.setCorrelatedVisibility(false);
            additionalInformation.put(REPEAT_COUNT, Integer.toString(repeatCount));
            alarmRecord.setAdditionalInformation(additionalInformation);
        } else {
            alarmRecord.setCommentText(previousCommentText);
            alarmRecord.setRepeatCount(Integer.valueOf(repeatCount));
        }
        alarmRecord.setAdditionalInformation(additionalInformation);
        final Map<String, Object> alarmAttributes = populateUpdateAlarm(alarmRecord);
        alarmAttributes.put(COMMENT_TIME, previousCommentTime);
        alarmAttributes.put(COMMENT_OPERATOR, previousCommentOperator);
        LOGGER.trace("Alarm attributes:{}", alarmAttributes);
        return alarmAttributes;
    }

    /**
     * Update the InsertTime in ProcessedAlarmEvent.
     *
     * @param alarm
     *            {@link ProcessedAlarmEvent}
     * @param oldInsertTime
     *            {@link Date}
     */
    public static void setInsertTime(final ProcessedAlarmEvent alarm, final Date oldInsertTime) {
        alarm.setInsertTime(oldInsertTime);
    }

    /**
     * Method converts ProcessedAlarmEvent to a Map. This method is applicable only for new OpenAlarm Object.
     *
     * @param alarmRecord
     *            {@link ProcessedAlarmEvent}
     * @return {@link Map} openAlarmAttributesMap
     */
    public static Map<String, Object> populateNewAlarm(final ProcessedAlarmEvent alarmRecord) {
        final Date insertTime = new Date();
        alarmRecord.setInsertTime(insertTime);
        alarmRecord.setLastUpdated(insertTime);
        final Map<String, Object> alarmAttributes = getAlarmAttributes(alarmRecord);
        // To indicate New Alarm
        alarmAttributes.put(REPEAT_COUNT, 0);
        alarmAttributes.put(OSCILLATION_COUNT, 0);
        alarmRecord.getAdditionalInformation().put(LAST_DELIVERED, String.valueOf(insertTime.getTime()));
        alarmAttributes.put(LAST_DELIVERED, insertTime.getTime());
        return alarmAttributes;
    }

    /**
     * Method that builds alarm attributes when a clear is received on a hidden alarm.
     *
     * @param alarmRecord
     *            {@link ProcessedAlarmEvent}
     * @param alarmAttributes
     *            Map containing alarm attributes
     * @return {@link Map } of attributes
     */
    public static Map<String, Object> populateHiddenAlarm(final ProcessedAlarmEvent alarmRecord, final Map<String, Object> alarmAttributes) {
        alarmRecord.setProblemDetail(AlarmProcessorConstants.DELETE_ALARM_PROBLEM_DETAIL);
        // Setting the AlarmState to CLEARED_ACKNOWLEDGED as the alarm is being deleted
        // from DB, so should be at NMS.
        alarmAttributes.put(ALARM_STATE, EventState.CLEARED_ACKNOWLEDGED.name());
        alarmRecord.setAlarmState(EventState.CLEARED_ACKNOWLEDGED);
        setAckInformation(alarmRecord, alarmAttributes);
        alarmAttributes.put(PROBLEM_DETAIL, AlarmProcessorConstants.DELETE_ALARM_PROBLEM_DETAIL);
        alarmAttributes.put(PREVIOUS_SEVERITY, alarmAttributes.get(PRESENT_SEVERITY));
        alarmAttributes.put(PRESENT_SEVERITY, EventSeverity.CLEARED.name());
        alarmAttributes.put(CEASE_TIME, alarmRecord.getEventTime());
        final Date lastUpdated = new Date();
        alarmAttributes.put(LAST_UPDATED, lastUpdated);
        alarmAttributes.put(LAST_ALARM_OPERATION, LastAlarmOperation.CLEAR.name());
        alarmAttributes.put(CORRELATED_EVENT_PO_ID, alarmRecord.getCorrelatedPOId());
        alarmAttributes.put(CEASE_OPERATOR, alarmRecord.getCeaseOperator());
        alarmRecord.setInsertTime((Date) alarmAttributes.get(INSERT_TIME));
        alarmRecord.setLastUpdated(lastUpdated);
        final String pseudoPresentSeverity = alarmAttributes.get(PRESENT_SEVERITY) + UNDER_SCORE + EventSeverity.CLEARED.name();
        alarmAttributes.put(PSEUDO_PRESENT_SEVERITY, PseudoSeverities.PSEUDO_SEVERITIES.get(pseudoPresentSeverity));
        alarmAttributes.put(PSEUDO_PREVIOUS_SEVERITY, PseudoSeverities.PSEUDO_SEVERITIES.get(alarmAttributes.get(PRESENT_SEVERITY)));
        return alarmAttributes;
    }

    public static void updateLastDeliveredTime(final ProcessedAlarmEvent alarmRecord, final ProcessedAlarmEvent correlatedAlarmRecord,
                                               final Map<String, Object> alarmAttributes) {
        Long lastDeliveredTime = null;
        final long lastUpdatedTime = alarmRecord.getLastUpdated().getTime();
        boolean clearAlarm = false;
        if (correlatedAlarmRecord.getAdditionalInformation().get(LAST_DELIVERED) != null
                && !correlatedAlarmRecord.getAdditionalInformation().get(LAST_DELIVERED).isEmpty()) {
            try {
                lastDeliveredTime = Long.parseLong(correlatedAlarmRecord.getAdditionalInformation().get(LAST_DELIVERED));
                final Long diffTimeInMilliSeconds = Math.abs(lastUpdatedTime - lastDeliveredTime);
                if (EventState.CLEARED_ACKNOWLEDGED.name().equals(alarmRecord.getAlarmState().name())
                        || EventState.CLEARED_UNACKNOWLEDGED.name().equals(alarmRecord.getAlarmState().name())) {
                    clearAlarm = true;
                }
                if (diffTimeInMilliSeconds < 500) {
                    if (clearAlarm) {
                        lastDeliveredTime = lastUpdatedTime + 500;
                    } else {
                        lastDeliveredTime = lastUpdatedTime + 300;
                    }
                } else {
                    lastDeliveredTime = lastUpdatedTime;
                }
                LOGGER.debug("lastDeliveredTime is {} lastUpdatedTime is {} diffTimeInMillSeconds is {},isClearAlarm is {} ", lastDeliveredTime,
                        lastUpdatedTime, diffTimeInMilliSeconds, clearAlarm);
                alarmRecord.getAdditionalInformation().put(LAST_DELIVERED, String.valueOf(lastDeliveredTime));
                alarmAttributes.put(LAST_DELIVERED, lastDeliveredTime);
            } catch (final NumberFormatException numberFormatException) {
                LOGGER.error("Excpetion occurred for LAST DELIVERED TIME returning timeToDeliver as NULL : {}", numberFormatException);
                alarmRecord.getAdditionalInformation().put(LAST_DELIVERED, String.valueOf(lastUpdatedTime));
                alarmAttributes.put(LAST_DELIVERED, lastUpdatedTime);
            }
        } else {
            alarmRecord.getAdditionalInformation().put(LAST_DELIVERED, String.valueOf(lastUpdatedTime));
            alarmAttributes.put(LAST_DELIVERED, lastUpdatedTime);
        }
    }

    /**
     * AlarmRecord is built with ack information.
     *
     * @param alarmRecord
     *            {@link ProcessedAlarmEvent}
     * @param attributes
     *            map containing alarm attributes.
     * @param {@link
     *            Map } attributes
     */
    private static void setAckInformation(final ProcessedAlarmEvent alarmRecord, final Map<String, Object> attributes) {
        final String ackOperator = (String) attributes.get(ACK_OPERATOR);
        final Date ackTime = (Date) attributes.get(ACK_TIME);
        if (ackOperator != null && ackTime != null) {
            alarmRecord.setAckOperator(ackOperator);
            alarmRecord.setAckTime(ackTime);
        } else {
            final Date eventTime = alarmRecord.getEventTime();
            alarmRecord.setAckOperator(APS);
            alarmRecord.setAckTime(eventTime);
            attributes.put(ACK_OPERATOR, APS);
            attributes.put(ACK_TIME, eventTime);
        }
        alarmRecord.setAlarmState(EventState.CLEARED_ACKNOWLEDGED);
    }

    private static Map<String, Object> getAlarmAttributes(final ProcessedAlarmEvent alarmRecord) {
        final Map<String, Object> alarmAttributes = new HashMap<String, Object>();
        alarmAttributes.put(PROBLEM_TEXT, alarmRecord.getProblemText());
        alarmAttributes.put(PROBLEM_DETAIL, alarmRecord.getProblemDetail());
        alarmAttributes.put(FDN, alarmRecord.getFdn());
        alarmAttributes.put(SYNC_STATE, alarmRecord.getSyncState());
        alarmAttributes.put(ADDITIONAL_INFORMATION, alarmRecord.getAdditionalInformationString());
        alarmAttributes.put(COMMENT_TEXT, alarmRecord.getCommentText());
        alarmAttributes.put(OBJECT_OF_REFERENCE, alarmRecord.getObjectOfReference());
        alarmAttributes.put(EVENT_TYPE, alarmRecord.getEventType());
        alarmAttributes.put(EVENT_TIME, alarmRecord.getEventTime());
        alarmAttributes.put(PROBABLE_CAUSE, alarmRecord.getProbableCause());
        alarmAttributes.put(SPECIFIC_PROBLEM, alarmRecord.getSpecificProblem());
        alarmAttributes.put(BACKUP_STATUS, alarmRecord.getBackupStatus());
        alarmAttributes.put(BACKUP_OBJECT_INSTANCE, alarmRecord.getBackupObjectInstance());
        alarmAttributes.put(PROPOSED_REPAIR_ACTION, alarmRecord.getProposedRepairAction());
        alarmAttributes.put(ALARM_NUMBER, alarmRecord.getAlarmNumber());
        alarmAttributes.put(ALARM_ID, alarmRecord.getAlarmId());
        alarmAttributes.put(CEASE_TIME, alarmRecord.getCeaseTime());
        alarmAttributes.put(CEASE_OPERATOR, alarmRecord.getCeaseOperator());
        alarmAttributes.put(ACK_TIME, alarmRecord.getAckTime());
        alarmAttributes.put(ACK_OPERATOR, alarmRecord.getAckOperator());
        alarmAttributes.put(PRESENT_SEVERITY, alarmRecord.getPresentSeverity().name());
        alarmAttributes.put(PREVIOUS_SEVERITY, alarmRecord.getPreviousSeverity().name());
        alarmAttributes.put(PSEUDO_PRESENT_SEVERITY, alarmRecord.getPseudoPresentSeverity());
        alarmAttributes.put(PSEUDO_PREVIOUS_SEVERITY, alarmRecord.getPseudoPreviousSeverity());
        alarmAttributes.put(RECORD_TYPE, alarmRecord.getRecordType().name());
        alarmAttributes.put(ALARM_STATE, alarmRecord.getAlarmState().name());
        alarmAttributes.put(TREND_INDICATION, alarmRecord.getTrendIndication().name());
        alarmAttributes.put(CORRELATED_EVENT_PO_ID, alarmRecord.getCorrelatedPOId());
        alarmAttributes.put(VISIBILITY, alarmRecord.isVisibility());
        alarmAttributes.put(CORRELATEDVISIBILITY, alarmRecord.getCorrelatedVisibility());
        alarmAttributes.put(FMX_GENERATED, alarmRecord.getFmxGenerated());
        alarmAttributes.put(PROCESSING_TYPE, alarmRecord.getProcessingType());
        alarmAttributes.put(ALARMING_OBJECT, alarmRecord.getAlarmingObject());
        alarmAttributes.put(INSERT_TIME, alarmRecord.getInsertTime());
        alarmAttributes.put(LAST_UPDATED, alarmRecord.getLastUpdated());
        return alarmAttributes;
    }
}