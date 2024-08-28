package com.ericsson.oss.service.fm.testing.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public abstract class AuditModel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6656402828013683884L;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date insertTime;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastUpdated;

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(final Date insertTime) {
        this.insertTime = insertTime;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(final Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((insertTime == null) ? 0 : insertTime.hashCode());
        result = prime * result + ((lastUpdated == null) ? 0 : lastUpdated.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AuditModel other = (AuditModel) obj;
        if (insertTime == null) {
            if (other.insertTime != null) {
                return false;
            }
        } else if (!insertTime.equals(other.insertTime)) {
            return false;
        }
        if (lastUpdated == null) {
            if (other.lastUpdated != null) {
                return false;
            }
        } else if (!lastUpdated.equals(other.lastUpdated)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AuditModel [insertTime=" + insertTime + ", lastUpdated=" + lastUpdated + "]";
    }

}
