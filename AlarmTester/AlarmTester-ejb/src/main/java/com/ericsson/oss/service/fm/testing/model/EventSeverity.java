package com.ericsson.oss.service.fm.testing.model;

public enum EventSeverity {

	UNDEFINED(-1), INDETERMINATE(0), CRITICAL(1),

	MAJOR(2),

	MINOR(3),

	WARNING(4),

	CLEARED(5);

	private int value;

	private EventSeverity(final int value) {
		this.setValue(value);
	}

	public int getValue() {
		return value;
	}

	public void setValue(final int value) {
		this.value = value;
	}
	
	public static EventSeverity valueOf(final Object value) {
		if (value instanceof String) {
			return EventSeverity.valueOf((String) value);
		}		
		return (EventSeverity)value;
	}

}
