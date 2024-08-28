package com.ericsson.oss.services.fm.alarm.migration;

import java.util.Objects;

public class Range {
    String timestamp;
    Long numFound;
    String id;

    public Range(final String timestamp, final Long numFound, final String id) {
        this.timestamp = timestamp;
        this.numFound = numFound;
        this.id = id;
    }

    public Range(final String timestamp, final String id) {
        this.timestamp = timestamp;
        this.numFound = 1L;
        this.id = id;
    }

    public Range(final String timestamp, final Long numFound) {
        this(timestamp, numFound, null);
    }

    public Range(final String timestamp) {
        this(timestamp, null, null);
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Long getNumFound() {
        return numFound;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Range range = (Range) o;
        return Objects.equals(timestamp, range.timestamp) &&
                Objects.equals(numFound, range.numFound);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, numFound);
    }

    @Override
    public String toString() {
        return "Range{" +
                "timestamp='" + timestamp + '\'' +
                ", numFound=" + numFound +
                '}';
    }
}
