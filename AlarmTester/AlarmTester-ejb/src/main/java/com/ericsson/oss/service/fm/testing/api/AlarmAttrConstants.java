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
 * Class containing constants related to FM alarm attributes.
 */
public final class AlarmAttrConstants {
    public static final String OBJECT_OF_REFERENCE = "objectOfReference";
    public static final String CORRELATED_EVENT_PO_ID = "correlatedeventPOId";
    public static final String FDN = "fdn";

    public static final String ALARM_NUMBER = "alarmNumber";
    public static final String PRESENT_SEVERITY = "presentSeverity";
    public static final String PROBABLE_CAUSE = "probableCause";
    public static final String SPECIFIC_PROBLEM = "specificProblem";
    public static final String RECORD_TYPE = "recordType";
    public static final String EVENT_TYPE = "eventType";

    public static final String EVENT_TIME = "eventTime";
    public static final String INSERT_TIME = "insertTime";
    public static final String LAST_UPDATED = "lastUpdated";
    public static final String PREVIOUS_SEVERITY = "previousSeverity";
    public static final String PROPOSED_REPAIR_ACTION = "proposedRepairAction";
    public static final String ALARM_ID = "alarmId";
    public static final String ALARM_STATE = "alarmState";
    public static final String ACTION_STATE = "actionState";
    public static final String CEASE_TIME = "ceaseTime";
    public static final String CEASE_OPERATOR = "ceaseOperator";
    public static final String ACK_TIME = "ackTime";
    public static final String ACK_OPERATOR = "ackOperator";
    public static final String BACKUP_STATUS = "backupStatus";
    public static final String BACKUP_OBJECT_INSTANCE = "backupObjectInstance";
    public static final String ADDITIONAL_INFORMATION = "additionalInformation";
    public static final String ADDITIONAL_INFORMATION_MAP = "additionalInformationMap";
    public static final String ROOT = "root";
    public static final String CI_GROUP_1 = "ciFirstGroup";
    public static final String CI_GROUP_2 = "ciSecondGroup";

    public static final String PROBLEM_TEXT = "problemText";
    public static final String PROBLEM_DETAIL = "problemDetail";
    public static final String COMMENT_TEXT = "commentText";
    public static final String COMMENTS = "comments";

    public static final String REPEAT_COUNT = "repeatCount";
    public static final String OSCILLATION_COUNT = "oscillationCount";
    public static final String LAST_ALARM_OPERATION = "lastAlarmOperation";
    public static final String MANUALCEASE = "manualCease";
    public static final String ALARMING_OBJECT = "alarmingObject";
    public static final String COMMENT_TIME = "commentTime";
    public static final String COMMENT_OPERATOR = "commentOperator";

    
    public static final String CORRELATEDVISIBILITY = "correlatedVisibility";
    public static final String PROCESSING_TYPE = "processingType";
    public static final String FMX_GENERATED = "fmxGenerated";

    public static final String LASTUPDATEDTIMESTAMP = "lastUpdatedTimeStamp";

    public static final String TREND_INDICATION = "trendIndication";
    public static final String SYNC_STATE = "syncState";

    public static final String PSEUDO_PRESENT_SEVERITY = "pseudoPresentSeverity";
    public static final String PSEUDO_PREVIOUS_SEVERITY = "pseudoPreviousSeverity";
    public static final String LAST_DELIVERED = "lastDelivered";

    private AlarmAttrConstants() {}

}
