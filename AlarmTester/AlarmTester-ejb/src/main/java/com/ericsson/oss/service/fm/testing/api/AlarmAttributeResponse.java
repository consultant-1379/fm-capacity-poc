package com.ericsson.oss.service.fm.testing.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
*
* A response class that encapsulates both the alarms ({@link AlarmRecord}) and the response status. <br>
* An AlarmRecord may have all attributes of an alarm or only the attributes that are set as part of {@link ExpectedOutputAttributes}.
*
*
*/
public class AlarmAttributeResponse implements Serializable {

   private static final long serialVersionUID = 1L;
   private final String response;
   private List<AlarmRecord> alarmRecords = new ArrayList<AlarmRecord>();
   private Long alarmCountForSearchCriteria;

   public AlarmAttributeResponse(final List<AlarmRecord> alarmRecords, final String response) {
       this.alarmRecords = alarmRecords;
       this.response = response;
   }

   public String getResponse() {
       return response;
   }

   public List<AlarmRecord> getAlarmRecords() {
       return alarmRecords;
   }

   public Long getAlarmCountForSearchCriteria() {
       return alarmCountForSearchCriteria;
   }

   public void setAlarmCountForSearchCriteria(final Long alarmCountForSearchCriteria) {
       this.alarmCountForSearchCriteria = alarmCountForSearchCriteria;
   }

   @Override
   public String toString() {
       final StringBuilder stringBuilder = new StringBuilder();
       stringBuilder.append("AlarmQueryResponse [response=");
       stringBuilder.append(response);
       stringBuilder.append(", alarmRecords=");
       stringBuilder.append(alarmRecords);
       stringBuilder.append("]");
       return stringBuilder.toString();
   }
}