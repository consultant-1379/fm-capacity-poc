package com.ericsson.oss.service.fm.testing.model;

import static com.ericsson.oss.service.fm.testing.api.Constants.COMMA_DELIMITER;
import static com.ericsson.oss.service.fm.testing.api.Constants.EMPTY_STRING;
import static com.ericsson.oss.service.fm.testing.api.Constants.EQUAL_DELIMITER;
import static com.ericsson.oss.service.fm.testing.api.Constants.HASH_DELIMITER;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//
//
//@EModel(description = "Processed Alarm Model from APS", name = "ProcessedAlarmEvent", namespace = "FM", version = "1.0.1")
//@EventTypeDefinition(persistenceType = PersistenceType.PERSISTENT, channelUrn = "//global/FmProcessedEventChannel")
public class ProcessedAlarmEvent extends AuditModel {

	private static final long serialVersionUID = 7050275605371920950L;
	// This is to replace "#" present in any of the additional Attribute value as
	// delimiter between additional Attributes is "#"
	private static final String ESCAPE_CHARACTERS_FOR_HASH = "¡¿§";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "objectOfReference")
	
	private String objectOfReference;

	@Column(name = "fdn")
	private String fdn;

	@Column(name = "eventTime")	
	private Date eventTime;

	@Column(name = "presentSeverity")
	
	private EventSeverity presentSeverity;

	@Column(name = "probableCause")
	
	private String probableCause;

	@Column(name = "specificProblem")
	
	private String specificProblem;

	@Column(name = "eventType")
	
	private String eventType;

	@Column(name = "alarmNumber")
	
	private Long alarmNumber;

	@Column(name = "backupObjectInstance")
	
	private String backupObjectInstance;

	@Column(name = "backupStatus")
	
	private Boolean backupStatus;

	@Column(name = "recordType")
	
	private AlarmRecordType recordType;

	@Column(name = "trendIndication")
	
	private EventTrendIndication trendIndication;

	@Column(name = "previousSeverity")
	
	private EventSeverity previousSeverity;

	@Column(name = "proposedRepairAction")
	
	private String proposedRepairAction;

	@Column(name = "alarmId")
	
	private Long alarmId;

	@Column(name = "alarmState")
	
	private EventState alarmState;

	@Column(name = "lastAlarmOperation")
	
	private LastAlarmOperation lastAlarmOperation;

	@Column(name = "ceaseTime")
	
	private Date ceaseTime;

	@Column(name = "ceaseOperator")
	
	private String ceaseOperator;

	@Column(name = "ackTime")
	
	private Date ackTime;

	@Column(name = "ackOperator")
	
	private String ackOperator;

	@Column(name = "commentText")
	
	private String commentText;

	@Column(name = "problemText")
	
	private String problemText;

	@Column(name = "problemDetail")
	
	private String problemDetail;

	@Column(name = "correlatedPOId")
	
	private Long correlatedPOId;

	@Column(name = "additionalInformation")
	
	private Map<String, String> additionalInformation;

	@Column(name = "syncState")
	
	private Boolean syncState;

	@Column(name = "historyPOId")
	
	private Long historyPOId;

	@Column(name = "managedObject")
	
	private String managedObject;

	@Column(name = "visibility")
	
	private Boolean visibility;

	@Column(name = "processingType")
	
	private String processingType;

	@Column(name = "fmxGenerated")
	
	private String fmxGenerated;

	@Column(name = "repeatCount")
	
	private Integer repeatCount;

	@Column(name = "oscillationCount")
	
	private Integer oscillationCount;

	@Column(name = "DiscriminatorIDList")
	
	private List<String> discriminatorIdList;

	@Column(name = "correlatedVisibility")

	private Boolean correlatedVisibility = false;

	@Column(name = "manualCease")
	private Boolean manualCease = false;

	@Column(name = "timeZone")
	
	private String timeZone;

	@Column(name = "alarmingObject")
	
	private String alarmingObject;

	@Column(name = "pseudoPresentSeverity")
	
	private Integer pseudoPresentSeverity;

	@Column(name = "pseudoPreviousSeverity")
	
	private Integer pseudoPreviousSeverity;

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(final String timeZone) {
		this.timeZone = timeZone;
	}

	public Boolean getCorrelatedVisibility() {
		return correlatedVisibility;
	}

	public void setCorrelatedVisibility(final Boolean correlatedVisibility) {
		this.correlatedVisibility = correlatedVisibility;
	}

	public Boolean getManualCease() {
		return manualCease;
	}

	public void setManualCease(final Boolean manualCease) {
		this.manualCease = manualCease;
	}

	public Boolean getVisibility() {
		return visibility;
	}

	public ProcessedAlarmEvent() {
		super();
		id = -2L;
		objectOfReference = "";
		fdn = "";
		// this.eventTime = ;
		// this.insertTime = "";
		presentSeverity = EventSeverity.UNDEFINED;
		probableCause = "";
		specificProblem = "";
		eventType = "";
		alarmNumber = -2L;
		backupObjectInstance = "";
		backupStatus = false;
		recordType = AlarmRecordType.UNDEFINED;
		trendIndication = EventTrendIndication.UNDEFINED;
		previousSeverity = EventSeverity.UNDEFINED;
		proposedRepairAction = "";
		alarmId = -2L;
		alarmState = EventState.ACTIVE_UNACKNOWLEDGED;
		lastAlarmOperation = LastAlarmOperation.UNDEFINED;
		// this.ceaseTime = "";
		ceaseOperator = "";
		// this.ackTime = "";
		ackOperator = "";
		commentText = "";
		problemText = "";
		problemDetail = "";
		correlatedPOId = -2L;
		additionalInformation = new HashMap<String, String>();
		syncState = true;
		historyPOId = -2L;
		managedObject = "";
		visibility = true;
		processingType = "NOT_SET";
		fmxGenerated = "NOT_SET";
		repeatCount = 0;
		oscillationCount = 0;
		discriminatorIdList = new ArrayList<String>();
		correlatedVisibility = false;
		manualCease = false;
		alarmingObject = "";
	}

	public Integer getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(final Integer repeatCount) {
		this.repeatCount = repeatCount;
	}

	public Integer getOscillationCount() {
		return oscillationCount;
	}

	public void setOscillationCount(final Integer oscillationCount) {
		this.oscillationCount = oscillationCount;
	}

	public String getFdn() {
		return fdn;
	}

	public void setFdn(final String fdn) {
		this.fdn = fdn;
	}

	public Long getEventPOId() {
		return id;
	}

	public void setEventPOId(final Long eventPOId) {
		this.id = eventPOId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(final String eventType) {
		this.eventType = eventType;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(final Date eventTime) {
		this.eventTime = eventTime;
	}

	public AlarmRecordType getRecordType() {
		return recordType;
	}

	public void setRecordType(final AlarmRecordType recordType) {
		this.recordType = recordType;
	}

	public String getObjectOfReference() {
		return objectOfReference;
	}

	public void setObjectOfReference(final String objectOfReference) {
		this.objectOfReference = objectOfReference;
		this.managedObject = initializeManagedObject(objectOfReference);
	}

	public EventSeverity getPresentSeverity() {
		return presentSeverity;
	}

	public void setPresentSeverity(final EventSeverity presentSeverity) {
		this.presentSeverity = presentSeverity;
		setPseudoPresentSeverity(0);
		setPseudoPreviousSeverity(0);
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

	public Boolean getBackupStatus() {
		return backupStatus;
	}

	public void setBackupStatus(final Boolean backupStatus) {
		this.backupStatus = backupStatus;
	}

	public String getProposedRepairAction() {
		return proposedRepairAction;
	}

	public void setProposedRepairAction(final String proposedRepairAction) {
		this.proposedRepairAction = proposedRepairAction;
	}

	public EventTrendIndication getTrendIndication() {
		return trendIndication;
	}

	public void setTrendIndication(final EventTrendIndication trendIndication) {
		this.trendIndication = trendIndication;
	}

	public Long getAlarmNumber() {
		return alarmNumber;
	}

	public void setAlarmNumber(final Long alarmNumber) {
		this.alarmNumber = alarmNumber;
	}

	public Long getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(final Long alarmId) {
		this.alarmId = alarmId;
	}

	public EventSeverity getPreviousSeverity() {
		return previousSeverity;
	}

	public void setPreviousSeverity(final EventSeverity previousSeverity) {
		this.previousSeverity = previousSeverity;
		setPseudoPresentSeverity(0);
		setPseudoPreviousSeverity(0);
	}

	public EventState getAlarmState() {
		return alarmState;
	}

	public void setAlarmState(final EventState alarmState) {
		this.alarmState = alarmState;
	}

	public Date getCeaseTime() {
		return ceaseTime;
	}

	public void setCeaseTime(final Date ceaseTime) {
		this.ceaseTime = ceaseTime;
	}

	public String getCeaseOperator() {
		return ceaseOperator;
	}

	public void setCeaseOperator(final String ceaseOperator) {
		this.ceaseOperator = ceaseOperator;
	}

	public Date getAckTime() {
		return ackTime;
	}

	public void setAckTime(final Date ackTime) {
		this.ackTime = ackTime;
	}

	public String getAckOperator() {
		return ackOperator;
	}

	public void setAckOperator(final String ackOperator) {
		this.ackOperator = ackOperator;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(final String commentText) {
		this.commentText = commentText;
	}

	public String getProblemText() {
		return problemText;
	}

	public void setProblemText(final String problemText) {
		this.problemText = problemText;
	}

	public String getProblemDetail() {
		return problemDetail;
	}

	public void setProblemDetail(final String problemDetail) {
		this.problemDetail = problemDetail;
	}

	public Long getCorrelatedPOId() {
		return correlatedPOId;
	}

	public void setCorrelatedPOId(final Long correlatedPOId) {
		this.correlatedPOId = correlatedPOId;
	}

	public String getBackupObjectInstance() {
		return backupObjectInstance;
	}

	public void setBackupObjectInstance(final String backupObjectInstance) {
		this.backupObjectInstance = backupObjectInstance;
	}

	public Map<String, String> getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(final Map<String, String> additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

	public String getAdditionalInformationString() {
		final StringBuffer additionalInformationString = new StringBuffer();
		if (additionalInformation != null) {
			int i = 1;
			for (final String key : additionalInformation.keySet()) {
				String value = additionalInformation.get(key);
				if (value == null) {
					value = "";
				}
				++i;
				if (i > 2) {
					additionalInformationString.append(HASH_DELIMITER);
				}
				additionalInformationString.append(key);
				additionalInformationString.append(":");
				value = checkForPresenceOfHashValueAndEscapeIt(value);
				additionalInformationString.append(value);
				// additionalInformationString.append(",");
			}
		}
		return additionalInformationString.toString();
	}

	public void setAdditionalInformationToMap(final String additionalInformationString) {
		if (additionalInformationString != null && additionalInformationString.length() > 0) {
			final String[] attributes = additionalInformationString.split(HASH_DELIMITER);
			for (final String string : attributes) {
				// Splits string into key and value .This holds good even in case of value
				// containing ":"
				final String[] keyValue = string.split(":", 2);
				if (keyValue.length == 1) {
					additionalInformation.put(keyValue[0], null);
				} else {
					final String value = replaceEscapeSequenceWithHashValue(keyValue[1]);
					additionalInformation.put(keyValue[0], value);
				}
			}
		}
	}

	public Long getHistoryPOId() {
		return historyPOId;
	}

	public void setHistoryPOId(final Long historyPOId) {
		this.historyPOId = historyPOId;
	}

	public Boolean getSyncState() {
		return syncState;
	}

	public void setSyncState(final Boolean syncState) {
		this.syncState = syncState;
	}

	public LastAlarmOperation getActionState() {
		return lastAlarmOperation;
	}

	public void setActionState(final LastAlarmOperation actionState) {
		lastAlarmOperation = actionState;
	}

	public LastAlarmOperation getLastAlarmOperation() {
		return lastAlarmOperation;
	}

	public void setLastAlarmOperation(final LastAlarmOperation lastAlarmOperation) {
		this.lastAlarmOperation = lastAlarmOperation;
	}

	public String getManagedObject() {
		return managedObject;
	}

	public void setManagedObject(final String managedObject) {
		this.managedObject = managedObject;
	}

	public Boolean isVisibility() {
		return visibility;
	}

	public void setVisibility(final Boolean visibility) {
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

	public List<String> getDiscriminatorList() {
		return discriminatorIdList;
	}

	public void setDiscriminatorList(final List<String> discriminatorList) {
		discriminatorIdList = discriminatorList;
	}

	public String getAlarmingObject() {
		return alarmingObject;
	}

	public void setAlarmingObject(final String alarmingObject) {
		this.alarmingObject = alarmingObject;
	}

	public Integer getPseudoPresentSeverity() {
		return pseudoPresentSeverity;
	}

	public void setPseudoPresentSeverity(final Integer pseudoPresentSeverity) {
		// Argument pseudoPresentSeverity in method is not being used. It is kept to
		// avoid WARN coming for setters.
		final String pseudoPresentSeverityString = previousSeverity + "_" + presentSeverity;
		this.pseudoPresentSeverity = PseudoSeverities.PSEUDO_SEVERITIES.get(pseudoPresentSeverityString);
	}

	public Integer getPseudoPreviousSeverity() {
		return pseudoPreviousSeverity;
	}

	public void setPseudoPreviousSeverity(final Integer pseudoPreviousSeverity) {
		// Argument pseudoPreviousSeverity in method is not being used. It is kept to
		// avoid WARN coming for setters.
		this.pseudoPreviousSeverity = PseudoSeverities.PSEUDO_SEVERITIES.get(previousSeverity);
	}

	/**
	 * Method to retrieve managedObject subtoken from a given String.
	 */
	public String initializeManagedObject(final String objectOfReference) {
		String result = EMPTY_STRING;
		try {
			final int lastIndex = objectOfReference.lastIndexOf(COMMA_DELIMITER) + 1;
			final String lastAttribute = objectOfReference.substring(lastIndex, objectOfReference.length());
			result = lastAttribute.split(EQUAL_DELIMITER)[0];
		} catch (final Exception exception) {
			// Do nothing.
		}
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("ProcessedAlarmEvent [eventPOId=").append(id).append(", objectOfReference=")
				.append(objectOfReference).append(", fdn=").append(fdn).append(", eventTime=").append(eventTime)
				.append(", insertTime=").append(insertTime).append(", lastUpdatedTime=").append(lastUpdated)
				.append(", presentSeverity=").append(presentSeverity).append(", probableCause=").append(probableCause)
				.append(", specificProblem=").append(specificProblem).append(", eventType=").append(eventType)
				.append(", alarmNumber=").append(alarmNumber).append(", backupObjectInstance=")
				.append(backupObjectInstance).append(", backupStatus=").append(backupStatus).append(", recordType=")
				.append(recordType).append(", trendIndication=").append(trendIndication).append(", previousSeverity=")
				.append(previousSeverity).append(", proposedRepairAction=").append(proposedRepairAction)
				.append(", alarmId=").append(alarmId).append(", alarmState=").append(alarmState)
				.append(", lastAlarmOperation=").append(lastAlarmOperation).append(", ceaseTime=").append(ceaseTime)
				.append(", ceaseOperator=").append(ceaseOperator).append(", ackTime=").append(ackTime)
				.append(", ackOperator=").append(ackOperator).append(", commentText=").append(commentText)
				.append(", problemText=").append(problemText).append(", problemDetail=").append(problemDetail)
				.append(", correlatedPOId=").append(correlatedPOId).append(", additionalInformation=")
				.append(additionalInformation).append(", syncState=").append(syncState).append(", historyPOId=")
				.append(historyPOId).append(", managedObject=").append(managedObject).append(", visibility=")
				.append(visibility).append(", processingType=").append(processingType).append(", fmxGenerated=")
				.append(fmxGenerated).append(", repeatCount=").append(repeatCount).append(", oscillationCount=")
				.append(oscillationCount).append(", discriminatorIdList=").append(discriminatorIdList)
				.append(", correlatedVisibility=").append(correlatedVisibility).append(", manualCease=")
				.append(manualCease).append(", timeZone=").append(timeZone).append(", alarmingObject=")
				.append(alarmingObject).append(", pseudoPresentSeverity=").append(pseudoPresentSeverity)
				.append(", pseudoPreviousSeverity=").append(pseudoPreviousSeverity).append("]");
		return builder.toString();
	}

	public String toFormattedString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("Alarm\n").append("[eventPOId=").append(id).append(",\nobjectOfReference=")
				.append(objectOfReference).append(",\nfdn=").append(fdn).append(",\neventTime=").append(eventTime)
				.append(",\ninsertTime=").append(insertTime).append(",\nlastUpdatedTime=").append(lastUpdated)
				.append(",\npresentSeverity=").append(presentSeverity).append(",\nprobableCause=").append(probableCause)
				.append(",\nspecificProblem=").append(specificProblem).append(",\neventType=").append(eventType)
				.append(",\nalarmNumber=").append(alarmNumber).append(",\nbackupObjectInstance=")
				.append(backupObjectInstance).append(",\nbackupStatus=").append(backupStatus).append(",\nrecordType=")
				.append(recordType).append(",\ntrendIndication=").append(trendIndication).append(",\npreviousSeverity=")
				.append(previousSeverity).append(",\nproposedRepairAction=").append(proposedRepairAction)
				.append(",\nalarmId=").append(alarmId).append(",\nalarmState=").append(alarmState)
				.append(",\nlastAlarmOperation=").append(lastAlarmOperation).append(",\nceaseTime=").append(ceaseTime)
				.append(",\nceaseOperator=").append(ceaseOperator).append(",\nackTime=").append(ackTime)
				.append(",\nackOperator=").append(ackOperator).append(",\ncommentText=").append(commentText)
				.append(",\nproblemText=").append(problemText).append(",\nproblemDetail=").append(problemDetail)
				.append(",\ncorrelatedPOId=").append(correlatedPOId).append(",\nadditionalInformation=")
				.append(additionalInformation).append(",\nsyncState=").append(syncState).append(",\nhistoryPOId=")
				.append(historyPOId).append(",\nmanagedObject=").append(managedObject).append(",\nvisibility=")
				.append(visibility).append(",\nprocessingType=").append(processingType).append(",\nfmxGenerated=")
				.append(fmxGenerated).append(",\nrepeatCount=").append(repeatCount).append(",\noscillationCount=")
				.append(oscillationCount).append(",\ndiscriminatorIdList=").append(discriminatorIdList)
				.append(",\ncorrelatedVisibility=").append(correlatedVisibility).append(",\nmanualCease=")
				.append(manualCease).append(",\ntimeZone=").append(timeZone).append(",\nalarmingObject=")
				.append(alarmingObject).append(",\npseudoPresentSeverity=").append(pseudoPresentSeverity)
				.append(",\npseudoPreviousSeverity=").append(pseudoPreviousSeverity).append("]");
		return builder.toString();
	}

	/**
	 * Checks for presence of Hash in the value of any additional Attribute and
	 * escapes it with sequence of characters.
	 *
	 * @param value String value of an additional attribute.
	 * @return String value embedded with predefined characters for hash if any
	 */
	private String checkForPresenceOfHashValueAndEscapeIt(final String value) {
		String result = value;
		if (value.contains(HASH_DELIMITER)) {
			result = value.replace(HASH_DELIMITER, ESCAPE_CHARACTERS_FOR_HASH);
		}
		return result;
	}

	private String replaceEscapeSequenceWithHashValue(final String value) {
		String result = value;
		if (result.contains(ESCAPE_CHARACTERS_FOR_HASH)) {
			result = result.replace(ESCAPE_CHARACTERS_FOR_HASH, HASH_DELIMITER);
		}
		return result;
	}

}
