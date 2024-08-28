package com.ericsson.oss.services.fm.alarm.migration.es;

public class EsManagerException extends RuntimeException {
    public EsManagerException(final String errorMessage, final Exception err) {
        super(errorMessage, err);
    }
}
