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

public enum LastAlarmOperation {

	UNDEFINED(-1),

	NEW(0),

	CLEAR(1),

	CHANGE(2),

	ACKSTATE_CHANGE(3),

	COMMENT(4);

	private int value;

	private LastAlarmOperation(final int value) {
		this.setValue(value);
	}

	public int getValue() {
		return value;
	}

	public void setValue(final int value) {
		this.value = value;
	}
	
	public static LastAlarmOperation valueOf(final Object value) {
		if (value instanceof String) {
			return LastAlarmOperation.valueOf((String) value);
		}		
		return (LastAlarmOperation)value;
	}
}
