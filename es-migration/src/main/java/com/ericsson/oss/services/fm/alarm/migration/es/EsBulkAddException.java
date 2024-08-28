package com.ericsson.oss.services.fm.alarm.migration.es;

public class EsBulkAddException extends RuntimeException {

    public EsBulkAddException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public EsBulkAddException(String errorMessage) {
        super(errorMessage);
    }
}
