package com.ericsson.oss.services.fm.alarm.migration.solr;

public class SolrManagerException extends RuntimeException  {
    public SolrManagerException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public SolrManagerException(String errorMessage) {
        super(errorMessage);
    }
}
