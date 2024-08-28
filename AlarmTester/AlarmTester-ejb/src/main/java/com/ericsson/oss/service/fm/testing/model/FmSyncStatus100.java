package com.ericsson.oss.service.fm.testing.model;

public enum FmSyncStatus100 {

	IN_SERVICE(1),

	HEART_BEAT_FAILURE(2),

	NODE_SUSPENDED(3),

	SYNCHRONIZATION(4),

	SYNC_ONGOING(5),

	IDLE(6),

	ALARM_SUPPRESSED(7),

	TECHNICIAN_PRESENT(8),

	OUT_OF_SYNC(9);

	private int value;

	private FmSyncStatus100(final int value) {
		this.setValue(value);
	}

	public int getValue() {
		return value;
	}

	public void setValue(final int value) {
		this.value = value;
	}
}
