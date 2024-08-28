package com.ericsson.oss.service.fm.testing.api;

public class AlarmProcessorConstants {

    public static final String FM_CORE_OUT_QUEUE = "//global/FmCoreOutQueue";
    public static final String FM_NB_QUEUE = "//global/FMNorthBoundQueue";
    public static final String ALARMS_EVENTS_CHANNEL_URI = "//global/ClusteredFMMediationChannel";

    public static final String FM_ALARM_PROCESSING_SUFFIX = "fmalarmprocessing";
    public static final String DATE_FORMAT = "yyyyMMddKKmmss.SSS";
    public static final String SIMPLE_DATE_FORMAT = "yyyyMMddKKmmss";
    public static final String PROPERTY_NODEID = "com.ericsson.oss.sdk.node.identifier";
    public static final String VERSANT_STATUS = "VERSANTSTATUS";
    public static final String GET_VERSANT_STATUS = "GET_VERSANTSTATUS";
    public static final String ALARM_PROCESSOR_CLUSTER_GROUP = "AlarmProcessorClusterGroup";
    public static final String FM_DB_AVAILABILITY_CACHE = "FmAvailabilityCache";
    public static final String CLEAR_ALARMS_CACHE = "ClearAlarmsCache";
    public static final String SERIALIZED_DATA = "serializedData";
    public static final String CLEARALARMSCACHE_KEY_DELIMITER = "@@@";
    public static final String INITIATE_SYNC = "InitiateSync";
    public static final String ALARM_TO_BE_PROCESSED = "alarmToBeProcessed";
    public static final String DISCARD_SYNC_PROBABALE_CAUSE = "This alarm is generated by APS to discard ongoing sync";
    public static final String ORIGINAL_EVENTTIME_FROM_NODE = "originalEventTimeFromNode";
    public static final String ROUTE_TO_NMS = "RouteToNMS";
    public static final String FLAG_ONE = "1";
    public static final String FLAG_ZERO = "0";
    // CorreletedAlarmConstants
    public static final long DEFAULT_EVENTPOID_VALUE = -2;
    public static final long ALREC_NO_VAL = -1;
    public static final int NUMBER_OF_CORRELATED_ALARMS_TO_PROCESS = 6;
    // Meta Data Constants
    public static final String APS_SERVICE_ID = "AlarmProcessor";
    public static final String DELETE_ALARM_PROBLEM_DETAIL = "Deleted the Hidden alarm from the Open Alarm DB as a Clear is received from the Node.";
    public static final String DELETE_ALARM_PROBLEM_DETAIL_FMX = "Deleted the Cleared alarm from the Open Alarm DB as Hide is received from FMX.";
    public static final String DELETE_NETWORK_ELEMENT_PROBLEM_TEXT =
            "Deleted the Stateless alarm from the Open Alarm DB as as corresponding NetworkElement is deleted.";
    public static final String CLEAR_UNCORRELATED_PROBLEM_TEXT =
            "This alarm is generated by AlarmProcessor during synchronization to Clear the Uncorrelated alarm";
    public static final String DELETE_DUPLICATE_ALARM_PROBLEM_DETAIL =
            "Deleted the duplicate alarm from the Open Alarm DB as it is duplicate of an existing alarm.";
    public static final String SP_SYNCABORTED = "SynchronizationAborted";
    public static final String PC_SYNCABORTED = "Synchronization";
    public static final String TECHNICIANPRESENT_SP = "FieldTechnicianPresent";
    public static final String ALARMSUPPRESSED_SP = "AlarmSuppressedMode";
    public static final String TECHNICIAN_PRESENT_STATE = "technicianPresentState";
    public static final String ALARM_SUPPRESSED_STATE = "alarmSuppressedState";
    public static final String APS = "APS";
    public static final String CURRENT_SERVICE_STATE = "currentServiceState";
    public static final String NE_TYPE = "neType";
    // Fm Availability Cache
    public static final int MAX_READ_ENTRIES_FROM_CACHE = 1000;
    public static final String LAST_DELIVERED = "lastDelivered";
    public static final String ALARM_RECEIVED_TIME = "alarmReceivedTime";

    public static final String OSSPREFIX_NOT_SET = "ossPrefixNotSet";
    public static final String OSSPREFIX = "ossPrefix";
    public static final String CATEGORY = "NODE";
    public static final String SOURCETYPE = "sourceType";

    public static final String ORIGINAL_RECORD_TYPE = "originalRecordType";

    private AlarmProcessorConstants() {}
}
