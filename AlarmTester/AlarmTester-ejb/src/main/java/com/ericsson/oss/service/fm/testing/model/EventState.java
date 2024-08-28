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

public enum EventState {

	ACTIVE_UNACKNOWLEDGED(0),

	ACTIVE_ACKNOWLEDGED(1),

	CLEARED_UNACKNOWLEDGED(2),

	CLEARED_ACKNOWLEDGED(3),

	CLOSED(4);

	private int value;

	private EventState(final int value) {
		this.setValue(value);
	}

	public int getValue() {
		return value;
	}

	public void setValue(final int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		switch (this) {
		case ACTIVE_UNACKNOWLEDGED:
			return "ACTIVE_UNACKNOWLEDGED";
		case ACTIVE_ACKNOWLEDGED:
			return "ACTIVE_ACKNOWLEDGED";
		case CLEARED_UNACKNOWLEDGED:
			return "CLEARED_UNACKNOWLEDGED";
		case CLEARED_ACKNOWLEDGED:
			return "CLEARED_ACKNOWLEDGED";
		case CLOSED:
			return "CLOSED";
		}
		return null;
	}

	public static EventState valueOf(final Object value) {
		if (value instanceof String) {
			return EventState.valueOf((String) value);
		}		
		return (EventState)value;
	}

}
