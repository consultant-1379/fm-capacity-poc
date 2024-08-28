/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2017
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.services.fm.testing.statistics;

public class AlarmInfo {
    String alarmThreadId;
    String alarmObject;
    String alarmNumber;
    String alarmType;
    String alarmTime;

    public AlarmInfo(final String alarmThreadId, final String alarmObject, final String alarmNumber, final String alarmType, final String alarmTime) {
        this.alarmThreadId = alarmThreadId;
        this.alarmObject = alarmObject;
        this.alarmNumber = alarmNumber;
        this.alarmType = alarmType;
        this.alarmTime = alarmTime;

    }

    /**
     * @return the alarmThreadId
     */
    public String getAlarmThreadId() {
        return alarmThreadId;
    }

    /**
     * @param alarmThreadId
     *            the alarmThreadId to set
     */
    public void setAlarmThreadId(final String alarmThreadId) {
        this.alarmThreadId = alarmThreadId;
    }

    /**
     * @return the alarmObject
     */
    public String getAlarmObject() {
        return alarmObject;
    }

    /**
     * @param alarmObject
     *            the alarmObject to set
     */
    public void setAlarmObject(final String alarmObject) {
        this.alarmObject = alarmObject;
    }

    /**
     * @return the alarmNumber
     */
    public String getAlarmNumber() {
        return alarmNumber;
    }

    /**
     * @param alarmNumber
     *            the alarmNumber to set
     */
    public void setAlarmNumber(final String alarmNumber) {
        this.alarmNumber = alarmNumber;
    }

    /**
     * @return the alarmType
     */
    public String getAlarmType() {
        return alarmType;
    }

    /**
     * @param alarmType
     *            the alarmType to set
     */
    public void setAlarmType(final String alarmType) {
        this.alarmType = alarmType;
    }

    /**
     * @return the alarmTime
     */
    public String getAlarmTime() {
        return alarmTime;
    }

    /**
     * @param alarmTime
     *            the alarmTime to set
     */
    public void setAlarmTime(final String alarmTime) {
        this.alarmTime = alarmTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((alarmNumber == null) ? 0 : alarmNumber.hashCode());
        result = prime * result + ((alarmObject == null) ? 0 : alarmObject.hashCode());
        result = prime * result + ((alarmThreadId == null) ? 0 : alarmThreadId.hashCode());
        result = prime * result + ((alarmTime == null) ? 0 : alarmTime.hashCode());
        result = prime * result + ((alarmType == null) ? 0 : alarmType.hashCode());
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
        final AlarmInfo other = (AlarmInfo) obj;
        if (alarmNumber == null) {
            if (other.alarmNumber != null) {
                return false;
            }
        } else if (!alarmNumber.equals(other.alarmNumber)) {
            return false;
        }
        if (alarmObject == null) {
            if (other.alarmObject != null) {
                return false;
            }
        } else if (!alarmObject.equals(other.alarmObject)) {
            return false;
        }
        if (alarmThreadId == null) {
            if (other.alarmThreadId != null) {
                return false;
            }
        } else if (!alarmThreadId.equals(other.alarmThreadId)) {
            return false;
        }
        if (alarmTime == null) {
            if (other.alarmTime != null) {
                return false;
            }
        } else if (!alarmTime.equals(other.alarmTime)) {
            return false;
        }
        if (alarmType == null) {
            if (other.alarmType != null) {
                return false;
            }
        } else if (!alarmType.equals(other.alarmType)) {
            return false;
        }
        return true;
    }

}
