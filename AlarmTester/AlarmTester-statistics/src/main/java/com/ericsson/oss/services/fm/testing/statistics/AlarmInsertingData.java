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

public class AlarmInsertingData {
    String alarmTime;

    public AlarmInsertingData(final String alarmTime) {
        this.alarmTime = alarmTime;
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

}
