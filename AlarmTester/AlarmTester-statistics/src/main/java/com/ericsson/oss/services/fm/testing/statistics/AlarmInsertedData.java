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

public class AlarmInsertedData {

    String alarmTime;
    String elapsedTime;
    String alarmPoId;

    public AlarmInsertedData(final String alarmTime, final String elapsedTime, final String alarmPoId) {
        this.alarmTime = alarmTime;
        this.elapsedTime = elapsedTime;
        this.alarmPoId = alarmPoId;
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

    /**
     * @return the elapsedTime
     */
    public String getElapsedTime() {
        return elapsedTime;
    }

    /**
     * @param elapsedTime
     *            the elapsedTime to set
     */
    public void setElapsedTime(final String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    /**
     * @return the alarmPoId
     */
    public String getAlarmPoId() {
        return alarmPoId;
    }

    /**
     * @param alarmPoId
     *            the alarmPoId to set
     */
    public void setAlarmPoId(final String alarmPoId) {
        this.alarmPoId = alarmPoId;
    }

}
