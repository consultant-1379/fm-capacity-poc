/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.oss.service.fm.testing.model;

public enum AlarmRecordType {

	UNDEFINED(-1),

	ALARM(1),

	ERROR_MESSAGE(2),

	NON_SYNCHABLE_ALARM(3),

	REPEATED_ALARM(4),

	SYNCHRONIZATION_ALARM(5),

	HEARTBEAT_ALARM(6),

	SYNCHRONIZATION_STARTED(7),

	SYNCHRONIZATION_ENDED(8),

	SYNCHRONIZATION_ABORTED(9),

	SYNCHRONIZATION_IGNORED(10),

	CLEAR_LIST(11),

	REPEATED_ERROR_MESSAGE(12),

	REPEATED_NON_SYNCHABLE(13),

	UPDATE(14),

	NODE_SUSPENDED(15),

	HB_FAILURE_NO_SYNCH(16),

	SYNC_NETWORK(17),
	
	TECHNICIAN_PRESENT(18),

	ALARM_SUPPRESSED_ALARM(19),

	OSCILLATORY_HB_ALARM(20),

	UNKNOWN_RECORD_TYPE(21),

	CLEARALL(22),

	OUT_OF_SYNC(23),

	NO_SYNCHABLE_ALARM(24);

	private int value;

	private AlarmRecordType(final int value) {
		this.setValue(value);
	}

	public int getValue() {
		return value;
	}

	public void setValue(final int value) {
		this.value = value;
	}
	
	public static AlarmRecordType valueOf(final Object value) {
		if (value instanceof String) {
			return AlarmRecordType.valueOf((String) value);
		}		
		return (AlarmRecordType)value;
	}
}
