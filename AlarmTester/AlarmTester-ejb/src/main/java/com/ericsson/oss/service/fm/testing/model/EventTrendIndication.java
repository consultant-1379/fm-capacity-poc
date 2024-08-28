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

public enum EventTrendIndication {

	UNDEFINED(-1),

	LESS_SEVERE(0),

	NO_CHANGE(1),

	MORE_SEVERE(2);

	private int value;

	private EventTrendIndication(final int value) {
		this.setValue(value);
	}

	public int getValue() {
		return value;
	}

	public void setValue(final int value) {
		this.value = value;
	}
	
	public static EventTrendIndication valueOf(final Object value) {
		if (value instanceof String) {
			return EventTrendIndication.valueOf((String) value);
		}		
		return (EventTrendIndication)value;
	}
}
