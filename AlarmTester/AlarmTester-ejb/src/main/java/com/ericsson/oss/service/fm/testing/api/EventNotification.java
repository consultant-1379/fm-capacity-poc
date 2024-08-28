package com.ericsson.oss.service.fm.testing.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The event forwarded from mediation to service layer after converting into ENM
 * normalized format.
 *
 */
public class EventNotification implements Serializable {

	private static final long serialVersionUID = 1L;

	private String eventType;

	private Map<String, String> additionalAttributes;

	private String probableCause;

	private String perceivedSeverity;

	private String recordType;

	private String specificProblem;

	private String eventAgentId;

	private String timeZone;

	private String eventTime;

	private String managedObjectInstance;

	private String externalEventId;

	private String translateResult;

	private boolean acknowledged;

	private String ackTime;

	private String operator;

	private boolean visibility;

	private String processingType;

	private String fmxGenerated;

	private List<String> discriminatorIdList;

	public List<String> getDiscriminatorList() {
		return discriminatorIdList;
	}

	public void setDiscriminatorList(final List<String> discriminatorList) {
		this.discriminatorIdList = discriminatorList;
	}

	public EventNotification() {
		this.eventType = "";
		this.additionalAttributes = new HashMap<String, String>();
		this.probableCause = "";
		this.perceivedSeverity = "INDETERMINATE";
		this.recordType = "";
		this.specificProblem = "";
		this.eventAgentId = "";
		this.timeZone = "";
		this.managedObjectInstance = "";
		this.externalEventId = "";

		this.translateResult = "FORWARD_ALARM";
		this.acknowledged = false;
		this.ackTime = "";
		this.operator = "";
		this.visibility = true;
		this.processingType = "NOT_SET";
		this.fmxGenerated = "NOT_SET";
		this.discriminatorIdList = new ArrayList<String>();

	}

	public String getTranslateResult() {
		return translateResult;
	}

	public void setTranslateResult(final String translateResult) {
		this.translateResult = translateResult;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(final String eventType) {
		this.eventType = eventType;
	}

	public String getProbableCause() {
		return probableCause;
	}

	public void setProbableCause(final String probableCause) {
		this.probableCause = probableCause;
	}

	public String getSpecificProblem() {
		return specificProblem;
	}

	public void setSpecificProblem(final String specificProblem) {
		this.specificProblem = specificProblem;
	}

	public String getEventAgentId() {
		return eventAgentId;
	}

	public void setEventAgentId(final String eventAgentId) {
		this.eventAgentId = eventAgentId;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(final String timeZone) {
		this.timeZone = timeZone;
	}

	public String getManagedObjectInstance() {
		return managedObjectInstance;
	}

	public void setManagedObjectInstance(final String managedObjectInstance) {
		this.managedObjectInstance = managedObjectInstance;
	}

	public String getExternalEventId() {
		return externalEventId;
	}

	public void setExternalEventId(final String externalEventId) {
		this.externalEventId = externalEventId;
	}

	public void setAdditionalAttributes(final Map<String, String> additionalAttributes) {
		this.additionalAttributes = additionalAttributes;
	}

	public Map<String, String> getAdditionalAttributes() {
		return additionalAttributes;
	}

	public void addAdditionalAttribute(final String name, final String value) {
		if (value != null) {
			additionalAttributes.put(name, value);
		}
	}

	/**
	 * Get the value of an attribute that has earlier been added to the list of
	 * optional attributes.
	 *
	 * @param keyname a name identifying the attribute for which the value should be
	 *                returned
	 *
	 * @return the string value for the optional attribute identified by keyname
	 *         (possibly null, if the attribute identitied by keyname does not point
	 *         to an attribute that has been added earlier).
	 *
	 * @see #getAdditionalAttributeNames()
	 * @see #addAdditionalAttribute(String, String)
	 */
	public String getAdditionalAttribute(final String keyname) {
		final Object value = additionalAttributes.get(keyname);

		if (value instanceof String) {
			return (String) value;
		} else {
			return null;
		}
	}

	public boolean isAcknowledged() {
		return acknowledged;
	}

	public void setAcknowledged(final boolean acknowledged) {
		this.acknowledged = acknowledged;
	}

	public String getAckTime() {
		return ackTime;
	}

	public void setAckTime(final String ackTime) {
		this.ackTime = ackTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(final String operator) {
		this.operator = operator;
	}

	public String getPerceivedSeverity() {
		return perceivedSeverity;
	}

	public void setPerceivedSeverity(final String perceivedSeverity) {
		this.perceivedSeverity = perceivedSeverity;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(final String recordType) {
		this.recordType = recordType;
	}

	public String getEventTime() {
		return eventTime;
	}

	public void setEventTime(final String eventTime) {
		this.eventTime = eventTime;
	}

	public boolean isVisibility() {
		return visibility;
	}

	public void setVisibility(final boolean visibility) {
		this.visibility = visibility;
	}

	public String getProcessingType() {
		return processingType;
	}

	public void setProcessingType(final String processingType) {
		this.processingType = processingType;
	}

	public String getFmxGenerated() {
		return fmxGenerated;
	}

	public void setFmxGenerated(final String fmxGenerated) {
		this.fmxGenerated = fmxGenerated;
	}

	@Override
	public String toString() {
		final StringBuffer addlAttributeString = new StringBuffer();
		for (final String key : additionalAttributes.keySet()) {
			addlAttributeString.append(" name=");
			addlAttributeString.append(key);
			addlAttributeString.append(" value=");
			addlAttributeString.append(additionalAttributes.get(key));
		}
		return "EventNotification [eventType=" + eventType + ", probableCause=" + probableCause + ", perceivedSeverity="
				+ perceivedSeverity + ", fmEventType=" + recordType + ", specificProblem=" + specificProblem
				+ ", eventAgentId=" + eventAgentId + ", timeZone=" + timeZone + ", time=" + eventTime
				+ ", managedObjectInstance=" + managedObjectInstance + ", externalEventId=" + externalEventId
				+ ", translateResult=" + translateResult + ", isAcknowledged=" + acknowledged + ", ackTime=" + ackTime
				+ ", operator=" + operator + ", visibility=" + visibility + ",processingType=" + processingType
				+ ",fmxGenerated" + fmxGenerated + ", additionalAttributes=" + addlAttributeString.toString()
				+ ", discriminatorIdList=" + discriminatorIdList + "]";
	}
}
