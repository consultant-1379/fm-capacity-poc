/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2016
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.oss.service.fm.testing.api;

import java.util.HashMap;
import java.util.Map;

public final class AlarmAttributes {
    public static final String HISTORY = "history";
    public static final String DELIMETER = ";";
    public static final String UNKNOWN_RESPONSE = "Unknown Response";
    public static final String RBAC_EXCEPTION = "rbac exception";
    public static final String POID_INFORMATION_NOT_FOUND = "PoIds information not found";
    public static final String FAILED_TO_EXPORT = "Failed to export due to exception, see logs for more details";
    public static final String FAILED_TO_STORE_EXPORTED_INFORMATION = "Failed to exported Information due to exception, see logs for more details";
    public static final String ERRORMESSAGE = "Insufficient access rights to perform the operation.";
    public static final String SUPERVISION = "supervision";
    public static final String SYNCH_STATUS = "synchStatus";
    public static final String ON = "ON";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String ALARMS = "alarms";
    public static final String SUCCESS = "Success";
    public static final String TOTAL_COUNT = "totalCount";
    public static final String TOTAL = "total";
    public static final String FIELDS = "fieldsnew";
    public static final String ALARM_SUPERVISION_STATE = "alarmSupervisionState";
    public static final String SUPERVISONSTATUS = "supervisonstatus";
    public static final String FM_ALARM_SUPERVISION = "FmAlarmSupervision";
    public static final String EXCEPTION = "exception";
    public static final String CAUSE = "Cause";
    public static final String SYNC = "SYNC";
    public static final String CURRENT_SERVICE_STATE = "currentServiceState";
    public static final String EQUALS_DELIMETER = "=";
    public static final String NETWORK_ELEMENT = "NetworkElement";
    public static final String NETWORK_ELEMENT_PREFIX = NETWORK_ELEMENT.concat(EQUALS_DELIMETER);
    public static final String MANAGEMENT_SYSTEM_PREFIX = "ManagementSystem=";
    public static final String VNFM = "VirtualNetworkFunctionManager";
    public static final String VIRTUAL_NETWORK_FUNCTION_MANAGER_PREFIX = VNFM.concat(EQUALS_DELIMETER);
    public static final String OSS_NE_DEF = "OSS_NE_DEF";
    public static final String NO_NE_FOUND = "No NetworkElement found with the given criteria";
    public static final String NODE_DOESNOT_EXISTS = "Node doesn't exists";
    public static final String DISCARDED_SYNC_REQUEST_BY_NODE = "Discarded the Synch request because NetworkElement doesn't support it";
    public static final String EVENT_POID = "eventPoId";
    public static final String EVENT_POIDS = "eventPoIds";
    public static final String SEV_CLEARED = "Cleared";
    public static final String SEV_INDETERMINATE = "Indeterminate";
    public static final String SEV_WARNING = "Warning";
    public static final String SEV_MINOR = "Minor";
    public static final String SEV_MAJOR = "Major";
    public static final String SEV_CRITICAL = "Critical";
    public static final String LAST_UPDATED = "lastUpdated";
    public static final String NODES = "nodes";

    public static final String ENM = "ENM";
    public static final String NETWORK_ALARM = "networkAlarm";
    public static final String CHECKED = "true";
    public static final String UNCHECKED = "false";

    public final static String ENM_DELIMETER = "ENM#true";
    public final static String NETWORK_ELEMENT_DELIMETER = "networkAlarm#true";

    public static final String DELIMETER_HASH = "#";
    public static final String DELIMETER_COMA = ",";
    public static final String ALARM_ATTRIBUTE_VALUE_DELIMETER = "\\Â¡\\Â¿\\Â§";
    public static final String ALARM_ATTRIBUTES_DELIMETER = "\\Â§\\Â¿\\Â¡";
    public static final String ALARM_ATTRIBUTE_VALUE_DELIMITER_WITHOUT_SLASHES = "Â¡Â¿Â§";
    public static final String REPEAT_COUNT = "repeatCount";
    public static final String SEVERITY = "Severity";
    public static final String ACKNOWLEDGEMENT = "Acknowledgement";
    public static final String ACKNOWLEDGETIME = "Acknowledge Time";
    public static final String ADDITIONAL_INFORMATION = "additionalInformation";

    public static final String CATEGORY_ALARMS_RECORDTYPES = "recordTypeÂ¡Â¿Â§ERROR_MESSAGEÂ¡Â¿Â§!=Â§Â¿Â¡recordTypeÂ¡Â¿Â§SYNCHRONIZATION_ABORTEDÂ¡Â¿Â§!=Â§Â¿Â¡recordTypeÂ¡Â¿Â§SYNCHRONIZATION_IGNOREDÂ¡Â¿Â§!=Â§Â¿Â¡recordTypeÂ¡Â¿Â§REPEATED_ERROR_MESSAGEÂ¡Â¿Â§!=";
    public static final String CATEGORY_ALERTS_RECORDTYPES = "recordTypeÂ¡Â¿Â§ERROR_MESSAGEÂ¡Â¿Â§=Â§Â¿Â¡recordTypeÂ¡Â¿Â§SYNCHRONIZATION_ABORTEDÂ¡Â¿Â§=Â§Â¿Â¡recordTypeÂ¡Â¿Â§SYNCHRONIZATION_IGNOREDÂ¡Â¿Â§=Â§Â¿Â¡recordTypeÂ¡Â¿Â§REPEATED_ERROR_MESSAGEÂ¡Â¿Â§=";
    public static final String ALARM_ATTRIBUTES_DELIMITER_WITHOUT_SLASHES = "Â§Â¿Â¡";
    public static final String ALERTS = "alerts";
    public static final String ALARMRECORDSERIALIZER = "AlarmRecordSerializer";
    public static final int MAJOR = 1;
    public static final int MINOR = 0;
    public static final int PATCHLEVEL = 0;

    public static final String REPEATED = "Repeated";
    public static final String HEADERS = "headers";
    public static final String ASCENDING = "asc";
    public static final String DESCENDING = "desc";
    public static final String ALL = "All";
    public static final String SECURITY_VIOLATION_EXCEPTION = "com.ericsson.oss.itpf.sdk.security.accesscontrol.SecurityViolationException";
    public static final String LIMIT_EXCEEDED = "Number of Alarms for the given search criteria are greater than 5000. Please refine search criteria and perform the serach again";
    public static final String SERVER_REFUSED_CONNECTION = "Server refused connection";
    public static final String REPEAT_COUNT_HEADER = "Repeat Count";
    public static final String PRESENT_SEVERITY = "presentSeverity";
    public static final String PSUEDO_PRESENT_SEVERITY = "psuedoPresentSeverity";
    public static final String PSUEDO_PREVIOUS_SEVERITY = "psuedoPreviousSeverity";
    public static final String PREVIOUS_SEVERITY = "previousSeverity";
    public static final String SORTING_MODE = "mode";
    public static final String SORTING_ATTRIBUTE = "attribute";
    public static final String IDLIST = "IdList";
    public static final String SEVERITYCOUNTS = "severityCounts";
    public static final String HEART_BEAT_FAILURE = "HEART_BEAT_FAILURE";
    public static final String NODE_SUSPENDED = "NODE_SUSPENDED";
    public static final String CI_FIRST_GROUP = "ciFirstGroup";
    public static final String CI_SECOND_GROUP = "ciSecondGroup";
    public static final String SERVICE_STATE_NOT_FOUND = "Current service state is not found for Node name \"%s\" in Cache";

    public static Map<String, Long> getDefaultFilterCounts() {
        final Map<String, Long> defaultFilterCounts = new HashMap<String, Long>(6);
        defaultFilterCounts.put(SEV_CRITICAL, 0L);
        defaultFilterCounts.put(SEV_MAJOR, 0L);
        defaultFilterCounts.put(SEV_MINOR, 0L);
        defaultFilterCounts.put(SEV_WARNING, 0L);
        defaultFilterCounts.put(SEV_INDETERMINATE, 0L);
        defaultFilterCounts.put(SEV_CLEARED, 0L);
        return defaultFilterCounts;
    }

    // To avoid PMD warning.
    private AlarmAttributes() {
    }
}
