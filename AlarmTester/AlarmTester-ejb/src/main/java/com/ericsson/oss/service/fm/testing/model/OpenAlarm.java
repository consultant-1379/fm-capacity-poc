/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2012
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.oss.service.fm.testing.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//@EModel(description = "OpenAlarm", name = "OpenAlarm", namespace = "FM", version = "1.0.1")
//@PrimaryTypeDefinition
//@OnReadWrite(onWrite = WRITE_TO_PERSISTENT_STORAGE_ONLY)
@Entity(name = "OpenAlarm")
@Table
public class OpenAlarm extends AuditModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3789649734880779580L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "objectOfReference")
	private String objectOfReference;

	@Column(name = "fdn")
	private String fdn;

	@Column(name = "eventTime")
	private Date eventTime;

	@Column(name = "lastDelivered")
	private Long lastDelivered;

	@Column(name = "presentSeverity")
	private EventSeverity presentSeverity;

	@Column(name = "probableCause")
	private String probableCause;

	@Column(name = "specificProblem")
	private String specificProblem;

	@Column(name = "alarmNumber")
	private Long alarmNumber;

	@Column(name = "eventType")
	private String eventType;

	@Column(name = "backupObjectInstance")
	private String backupObjectInstance;

	@Column(name = "recordType")
	private AlarmRecordType recordType;

	@Column(name = "backupStatus")
	private Boolean backupStatus;

	@Column(name = "trendIndication")
	private EventTrendIndication trendIndication;

	@Column(name = "previousSeverity")
	private EventSeverity previousSeverity;

	@Column(name = "proposedRepairAction")
	private String proposedRepairAction;

	@Column(name = "alarmId")
	private Long alarmId;

	@Column(name = "correlatedeventPOId")
	private Long correlatedeventPOId = -2L;

	@Column(name = "alarmState")
	private EventState alarmState = EventState.ACTIVE_UNACKNOWLEDGED;

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

	@Column(name = "syncState")
	private Boolean syncState;

	@Column(name = "historyAlarmPOId")
	private Long historyAlarmPOId;

	@Column(name = "commentText")
	private String commentText;

	@Column(name = "commentTime")
	private Date commentTime;

	@Column(name = "commentOperator")
	private String commentOperator;

	@Column(name = "repeatCount")
	private Integer repeatCount;

	@Column(name = "oscillationCount")
	private Integer oscillationCount;

	@Column(name = "ceaseRecordType")
	private AlarmRecordType ceaseRecordType;

	@Column(name = "visibility")
	private Boolean visibility = true;

	@Column(name = "processingType")
	private String processingType = "NOT_SET";

	@Column(name = "fmxGenerated")
	private String fmxGenerated = "NOT_SET";

	@Column(name = "type")	
	private String type = "FM_AlarmHistory";

	@Column(name = "problemText")
	private String problemText;

	@Column(name = "problemDetail")
	private String problemDetail;

	@Column(name = "additionalInformation")
	private String additionalInformation;

	@Column(name = "manualCease")	
	private Boolean manualCease = false;

	@Column(name = "correlatedVisibility")	
	private Boolean correlatedVisibility = true;

	@Column(name = "timeZone")
	private String timeZone;

	@Column(name = "alarmingObject")
	private String alarmingObject;

	@Column(name = "pseudoPresentSeverity")
	private Integer pseudoPresentSeverity;

	@Column(name = "pseudoPreviousSeverity")
	private Integer pseudoPreviousSeverity;

	@Column(name = "root")
	private CorrelationType root = CorrelationType.NOT_APPLICABLE;

	@Column(name = "ciFirstGroup")	
	private String ciFirstGroup = "";

	@Column(name = "ciSecondGroup")	
	private String ciSecondGroup = "";	

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Boolean getVisibility() {
		return visibility;
	}

	public void setVisibility(final Boolean visibility) {
		this.visibility = visibility;
	}

	public Boolean getManualCease() {
		return manualCease;
	}

	public void setManualCease(final Boolean manualCease) {
		this.manualCease = manualCease;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(final String timeZone) {
		this.timeZone = timeZone;
	}

	public LastAlarmOperation getLastAlarmOperation() {
		return lastAlarmOperation;
	}

	public void setLastAlarmOperation(final LastAlarmOperation lastAlarmOperation) {
		this.lastAlarmOperation = lastAlarmOperation;
	}

	public Boolean getOperatorCease() {
		return manualCease;
	}

	public void setOperatorCease(final Boolean manualCease) {
		this.manualCease = manualCease;
	}

	public Boolean getCorrelatedVisibility() {
		return correlatedVisibility;
	}

	public void setCorrelatedVisibility(final Boolean correlatedVisibility) {
		this.correlatedVisibility = correlatedVisibility;
	}

	public AlarmRecordType getCeaseRecordType() {
		return ceaseRecordType;
	}

	public void setCeaseRecordType(final AlarmRecordType ceaseRecordType) {
		this.ceaseRecordType = ceaseRecordType;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(final String commentText) {
		this.commentText = commentText;
	}

	public Date getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(final Date commentTime) {
		this.commentTime = commentTime;
	}

	public String getCommentOperator() {
		return commentOperator;
	}

	public void setCommentOperator(final String commentOperator) {
		this.commentOperator = commentOperator;
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

	public Long getCorrelatedeventPOId() {
		return correlatedeventPOId;
	}
	
	public void setCorrelatedeventPOId(final Long correlatedeventPOId) {
		this.correlatedeventPOId = correlatedeventPOId;
	}

	public Long getLastDelivered() {
		return lastDelivered;
	}

	public void setLastDelivered(final Long lastDelivered) {
		this.lastDelivered = lastDelivered;
	}

	public Boolean getSyncState() {
		return syncState;
	}

	public void setSyncState(final Boolean syncState) {
		this.syncState = syncState;
	}

	public String getObjectOfReference() {
		return objectOfReference;
	}

	public void setObjectOfReference(final String objectOfReference) {
		this.objectOfReference = objectOfReference;
	}

	public Date getEventTime() {
		return eventTime;
	}

	public void setEventTime(final Date eventTime) {
		this.eventTime = eventTime;
	}

	public EventSeverity getPresentSeverity() {
		return presentSeverity;
	}

	public void setPresentSeverity(final EventSeverity presentSeverity) {
		this.presentSeverity = presentSeverity;
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

	public Long getAlarmNumber() {
		return alarmNumber;
	}

	public void setAlarmNumber(final Long alarmNumber) {
		this.alarmNumber = alarmNumber;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(final String eventType) {
		this.eventType = eventType;
	}

	public String getBackupObjectInstance() {
		return backupObjectInstance;
	}

	public void setBackupObjectInstance(final String backupObjectInstance) {
		this.backupObjectInstance = backupObjectInstance;
	}

	public AlarmRecordType getRecordType() {
		return recordType;
	}

	public void setRecordType(final AlarmRecordType recordType) {
		this.recordType = recordType;
	}

	public Boolean getBackupStatus() {
		return backupStatus;
	}

	public void setBackupStatus(final Boolean backupStatus) {
		this.backupStatus = backupStatus;
	}

	public EventTrendIndication getTrendIndication() {
		return trendIndication;
	}

	public void setTrendIndication(final EventTrendIndication trendIndication) {
		this.trendIndication = trendIndication;
	}

	public EventSeverity getPreviousSeverity() {
		return previousSeverity;
	}

	public void setPreviousSeverity(final EventSeverity previousSeverity) {
		this.previousSeverity = previousSeverity;
	}

	public String getProposedRepairAction() {
		return proposedRepairAction;
	}

	public void setProposedRepairAction(final String proposedRepairAction) {
		this.proposedRepairAction = proposedRepairAction;
	}

	public Long getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(final Long alarmId) {
		this.alarmId = alarmId;
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

	public Long getHistoryAlarmPOId() {
		return historyAlarmPOId;
	}

	public void setHistoryAlarmPOId(final Long historyPOId) {
		historyAlarmPOId = historyPOId;
	}

	public String getFdn() {
		return fdn;
	}

	public void setFdn(final String fdn) {
		this.fdn = fdn;
	}
	
	public LastAlarmOperation getEventActionState() {
		return lastAlarmOperation;
	}

	public void setEventActionState(final LastAlarmOperation eventActionState) {
		lastAlarmOperation = eventActionState;
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

	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
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

	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public void setAdditionalInformation(final String additionalInformation) {
		this.additionalInformation = additionalInformation;
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
		this.pseudoPresentSeverity = pseudoPresentSeverity;
	}

	public Integer getPseudoPreviousSeverity() {
		return pseudoPreviousSeverity;
	}

	public void setPseudoPreviousSeverity(final Integer pseudoPreviousSeverity) {
		this.pseudoPreviousSeverity = pseudoPreviousSeverity;
	}

	public CorrelationType getRoot() {
		return root;
	}

	public void setRoot(final CorrelationType root) {
		this.root = root;
	}

	public String getCiFirstGroup() {
		return ciFirstGroup;
	}

	public void setCiFirstGroup(final String ciFirstGroup) {
		this.ciFirstGroup = ciFirstGroup;
	}

	public String getCiSecondGroup() {
		return ciSecondGroup;
	}

	public void setCiSecondGroup(final String ciSecondGroup) {
		this.ciSecondGroup = ciSecondGroup;
	}	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((ackOperator == null) ? 0 : ackOperator.hashCode());
		result = prime * result + ((ackTime == null) ? 0 : ackTime.hashCode());
		result = prime * result + ((additionalInformation == null) ? 0 : additionalInformation.hashCode());
		result = prime * result + ((alarmId == null) ? 0 : alarmId.hashCode());
		result = prime * result + ((alarmNumber == null) ? 0 : alarmNumber.hashCode());
		result = prime * result + ((alarmState == null) ? 0 : alarmState.hashCode());
		result = prime * result + ((alarmingObject == null) ? 0 : alarmingObject.hashCode());
		result = prime * result + ((backupObjectInstance == null) ? 0 : backupObjectInstance.hashCode());
		result = prime * result + ((backupStatus == null) ? 0 : backupStatus.hashCode());
		result = prime * result + ((ceaseOperator == null) ? 0 : ceaseOperator.hashCode());
		result = prime * result + ((ceaseRecordType == null) ? 0 : ceaseRecordType.hashCode());
		result = prime * result + ((ceaseTime == null) ? 0 : ceaseTime.hashCode());
		result = prime * result + ((ciFirstGroup == null) ? 0 : ciFirstGroup.hashCode());
		result = prime * result + ((ciSecondGroup == null) ? 0 : ciSecondGroup.hashCode());
		result = prime * result + ((commentOperator == null) ? 0 : commentOperator.hashCode());
		result = prime * result + ((commentText == null) ? 0 : commentText.hashCode());
		result = prime * result + ((commentTime == null) ? 0 : commentTime.hashCode());
		result = prime * result + ((correlatedVisibility == null) ? 0 : correlatedVisibility.hashCode());
		result = prime * result + ((correlatedeventPOId == null) ? 0 : correlatedeventPOId.hashCode());
		result = prime * result + ((eventTime == null) ? 0 : eventTime.hashCode());
		result = prime * result + ((eventType == null) ? 0 : eventType.hashCode());
		result = prime * result + ((fdn == null) ? 0 : fdn.hashCode());
		result = prime * result + ((fmxGenerated == null) ? 0 : fmxGenerated.hashCode());
		result = prime * result + ((historyAlarmPOId == null) ? 0 : historyAlarmPOId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lastAlarmOperation == null) ? 0 : lastAlarmOperation.hashCode());
		result = prime * result + ((lastDelivered == null) ? 0 : lastDelivered.hashCode());
		result = prime * result + ((manualCease == null) ? 0 : manualCease.hashCode());
		result = prime * result + ((objectOfReference == null) ? 0 : objectOfReference.hashCode());
		result = prime * result + ((oscillationCount == null) ? 0 : oscillationCount.hashCode());
		result = prime * result + ((presentSeverity == null) ? 0 : presentSeverity.hashCode());
		result = prime * result + ((previousSeverity == null) ? 0 : previousSeverity.hashCode());
		result = prime * result + ((probableCause == null) ? 0 : probableCause.hashCode());
		result = prime * result + ((problemDetail == null) ? 0 : problemDetail.hashCode());
		result = prime * result + ((problemText == null) ? 0 : problemText.hashCode());
		result = prime * result + ((processingType == null) ? 0 : processingType.hashCode());
		result = prime * result + ((proposedRepairAction == null) ? 0 : proposedRepairAction.hashCode());
		result = prime * result + ((pseudoPresentSeverity == null) ? 0 : pseudoPresentSeverity.hashCode());
		result = prime * result + ((pseudoPreviousSeverity == null) ? 0 : pseudoPreviousSeverity.hashCode());
		result = prime * result + ((recordType == null) ? 0 : recordType.hashCode());
		result = prime * result + ((repeatCount == null) ? 0 : repeatCount.hashCode());
		result = prime * result + ((root == null) ? 0 : root.hashCode());
		result = prime * result + ((specificProblem == null) ? 0 : specificProblem.hashCode());
		result = prime * result + ((syncState == null) ? 0 : syncState.hashCode());
		result = prime * result + ((timeZone == null) ? 0 : timeZone.hashCode());
		result = prime * result + ((trendIndication == null) ? 0 : trendIndication.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((visibility == null) ? 0 : visibility.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		final OpenAlarm other = (OpenAlarm) obj;
		if (ackOperator == null) {
			if (other.ackOperator != null)
				return false;
		} else if (!ackOperator.equals(other.ackOperator))
			return false;
		if (ackTime == null) {
			if (other.ackTime != null)
				return false;
		} else if (!ackTime.equals(other.ackTime))
			return false;
		if (additionalInformation == null) {
			if (other.additionalInformation != null)
				return false;
		} else if (!additionalInformation.equals(other.additionalInformation))
			return false;
		if (alarmId == null) {
			if (other.alarmId != null)
				return false;
		} else if (!alarmId.equals(other.alarmId))
			return false;
		if (alarmNumber == null) {
			if (other.alarmNumber != null)
				return false;
		} else if (!alarmNumber.equals(other.alarmNumber))
			return false;
		if (alarmState != other.alarmState)
			return false;
		if (alarmingObject == null) {
			if (other.alarmingObject != null)
				return false;
		} else if (!alarmingObject.equals(other.alarmingObject))
			return false;
		if (backupObjectInstance == null) {
			if (other.backupObjectInstance != null)
				return false;
		} else if (!backupObjectInstance.equals(other.backupObjectInstance))
			return false;
		if (backupStatus == null) {
			if (other.backupStatus != null)
				return false;
		} else if (!backupStatus.equals(other.backupStatus))
			return false;
		if (ceaseOperator == null) {
			if (other.ceaseOperator != null)
				return false;
		} else if (!ceaseOperator.equals(other.ceaseOperator))
			return false;
		if (ceaseRecordType != other.ceaseRecordType)
			return false;
		if (ceaseTime == null) {
			if (other.ceaseTime != null)
				return false;
		} else if (!ceaseTime.equals(other.ceaseTime))
			return false;
		if (ciFirstGroup == null) {
			if (other.ciFirstGroup != null)
				return false;
		} else if (!ciFirstGroup.equals(other.ciFirstGroup))
			return false;
		if (ciSecondGroup == null) {
			if (other.ciSecondGroup != null)
				return false;
		} else if (!ciSecondGroup.equals(other.ciSecondGroup))
			return false;
		if (commentOperator == null) {
			if (other.commentOperator != null)
				return false;
		} else if (!commentOperator.equals(other.commentOperator))
			return false;
		if (commentText == null) {
			if (other.commentText != null)
				return false;
		} else if (!commentText.equals(other.commentText))
			return false;
		if (commentTime == null) {
			if (other.commentTime != null)
				return false;
		} else if (!commentTime.equals(other.commentTime))
			return false;
		if (correlatedVisibility == null) {
			if (other.correlatedVisibility != null)
				return false;
		} else if (!correlatedVisibility.equals(other.correlatedVisibility))
			return false;
		if (correlatedeventPOId == null) {
			if (other.correlatedeventPOId != null)
				return false;
		} else if (!correlatedeventPOId.equals(other.correlatedeventPOId))
			return false;
		if (eventTime == null) {
			if (other.eventTime != null)
				return false;
		} else if (!eventTime.equals(other.eventTime))
			return false;
		if (eventType == null) {
			if (other.eventType != null)
				return false;
		} else if (!eventType.equals(other.eventType))
			return false;
		if (fdn == null) {
			if (other.fdn != null)
				return false;
		} else if (!fdn.equals(other.fdn))
			return false;
		if (fmxGenerated == null) {
			if (other.fmxGenerated != null)
				return false;
		} else if (!fmxGenerated.equals(other.fmxGenerated))
			return false;
		if (historyAlarmPOId == null) {
			if (other.historyAlarmPOId != null)
				return false;
		} else if (!historyAlarmPOId.equals(other.historyAlarmPOId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lastAlarmOperation != other.lastAlarmOperation)
			return false;
		if (lastDelivered == null) {
			if (other.lastDelivered != null)
				return false;
		} else if (!lastDelivered.equals(other.lastDelivered))
			return false;
		if (manualCease == null) {
			if (other.manualCease != null)
				return false;
		} else if (!manualCease.equals(other.manualCease))
			return false;
		if (objectOfReference == null) {
			if (other.objectOfReference != null)
				return false;
		} else if (!objectOfReference.equals(other.objectOfReference))
			return false;
		if (oscillationCount == null) {
			if (other.oscillationCount != null)
				return false;
		} else if (!oscillationCount.equals(other.oscillationCount))
			return false;
		if (presentSeverity != other.presentSeverity)
			return false;
		if (previousSeverity != other.previousSeverity)
			return false;
		if (probableCause == null) {
			if (other.probableCause != null)
				return false;
		} else if (!probableCause.equals(other.probableCause))
			return false;
		if (problemDetail == null) {
			if (other.problemDetail != null)
				return false;
		} else if (!problemDetail.equals(other.problemDetail))
			return false;
		if (problemText == null) {
			if (other.problemText != null)
				return false;
		} else if (!problemText.equals(other.problemText))
			return false;
		if (processingType == null) {
			if (other.processingType != null)
				return false;
		} else if (!processingType.equals(other.processingType))
			return false;
		if (proposedRepairAction == null) {
			if (other.proposedRepairAction != null)
				return false;
		} else if (!proposedRepairAction.equals(other.proposedRepairAction))
			return false;
		if (pseudoPresentSeverity == null) {
			if (other.pseudoPresentSeverity != null)
				return false;
		} else if (!pseudoPresentSeverity.equals(other.pseudoPresentSeverity))
			return false;
		if (pseudoPreviousSeverity == null) {
			if (other.pseudoPreviousSeverity != null)
				return false;
		} else if (!pseudoPreviousSeverity.equals(other.pseudoPreviousSeverity))
			return false;
		if (recordType != other.recordType)
			return false;
		if (repeatCount == null) {
			if (other.repeatCount != null)
				return false;
		} else if (!repeatCount.equals(other.repeatCount))
			return false;
		if (root != other.root)
			return false;
		if (specificProblem == null) {
			if (other.specificProblem != null)
				return false;
		} else if (!specificProblem.equals(other.specificProblem))
			return false;
		if (syncState == null) {
			if (other.syncState != null)
				return false;
		} else if (!syncState.equals(other.syncState))
			return false;
		if (timeZone == null) {
			if (other.timeZone != null)
				return false;
		} else if (!timeZone.equals(other.timeZone))
			return false;
		if (trendIndication != other.trendIndication)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (visibility == null) {
			if (other.visibility != null)
				return false;
		} else if (!visibility.equals(other.visibility))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OpenAlarm [id=" + id + ", objectOfReference=" + objectOfReference + ", fdn=" + fdn + ", eventTime="
				+ eventTime + ", lastDelivered=" + lastDelivered + ", presentSeverity=" + presentSeverity
				+ ", probableCause=" + probableCause + ", specificProblem=" + specificProblem + ", alarmNumber="
				+ alarmNumber + ", eventType=" + eventType + ", backupObjectInstance=" + backupObjectInstance
				+ ", recordType=" + recordType + ", backupStatus=" + backupStatus + ", trendIndication="
				+ trendIndication + ", previousSeverity=" + previousSeverity + ", proposedRepairAction="
				+ proposedRepairAction + ", alarmId=" + alarmId + ", correlatedeventPOId=" + correlatedeventPOId
				+ ", alarmState=" + alarmState + ", lastAlarmOperation=" + lastAlarmOperation + ", ceaseTime="
				+ ceaseTime + ", ceaseOperator=" + ceaseOperator + ", ackTime=" + ackTime + ", ackOperator="
				+ ackOperator + ", syncState=" + syncState + ", historyAlarmPOId=" + historyAlarmPOId + ", commentText="
				+ commentText + ", commentTime=" + commentTime + ", commentOperator=" + commentOperator
				+ ", repeatCount=" + repeatCount + ", oscillationCount=" + oscillationCount + ", ceaseRecordType="
				+ ceaseRecordType + ", visibility=" + visibility + ", processingType=" + processingType
				+ ", fmxGenerated=" + fmxGenerated + ", type=" + type + ", problemText=" + problemText
				+ ", problemDetail=" + problemDetail + ", additionalInformation=" + additionalInformation
				+ ", manualCease=" + manualCease + ", correlatedVisibility=" + correlatedVisibility + ", timeZone="
				+ timeZone + ", alarmingObject=" + alarmingObject + ", pseudoPresentSeverity=" + pseudoPresentSeverity
				+ ", pseudoPreviousSeverity=" + pseudoPreviousSeverity + ", root=" + root + ", ciFirstGroup="
				+ ciFirstGroup + ", ciSecondGroup=" + ciSecondGroup + "]";
	}



}
