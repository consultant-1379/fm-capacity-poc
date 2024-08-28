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
package com.ericsson.oss.services.fm.testing;

import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.ALARM_STATE;
import static com.ericsson.oss.service.fm.testing.api.AlarmAttrConstants.SYNC_STATE;
import static com.ericsson.oss.services.fm.testing.utils.AlarmProcessorConstants.OPEN_ALARM;
import static com.ericsson.oss.services.fm.testing.utils.AlarmProcessorConstants.OSS_FM;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.datalayer.dps.DataBucket;
import com.ericsson.oss.itpf.datalayer.dps.persistence.PersistenceObject;
import com.ericsson.oss.service.fm.testing.model.AlarmRecordType;
import com.ericsson.oss.service.fm.testing.model.AlarmTestingInfo;
import com.ericsson.oss.service.fm.testing.model.EventSeverity;
import com.ericsson.oss.service.fm.testing.model.EventState;
import com.ericsson.oss.service.fm.testing.model.ProcessedAlarmEvent;
import com.ericsson.oss.services.fm.testing.utils.AlarmAttributesPopulator;
import com.ericsson.oss.services.fm.testing.utils.AlarmProcessorConstants;
import com.ericsson.oss.services.fm.testing.utils.DpsProxyProvider;

@Stateless
public class AlarmInjectorDps {
    private static final Logger logger = LoggerFactory.getLogger(AlarmInjectorDps.class);

    static final String problemText = "f2ECv5sx6ZNFt6sRBwCVeE9EFrnxWQmUEYIzGfpd8Wk5idTBHatkPEU7GMxFWgbUym5fO2bOOwxn3MIBPu30MtfCa4vCLgNLJUHoEcWPGNBkDr2yPFL0pfPMWhP4qHoO5l2sWPsTJkTY65wZaeEIqNkcuvhBwCesYcVEYqNpZzGGkS42sVY9hf7S7FYgk2vwSmD6uDzvCnJRShiQpJvklprnxsxXNIe97Buy2lCwq6xgnb5oA6b2l7lJogYxuSdETTehxeSHqzytfT1Y8ySvVkafUpG2eW9GggVF0vqdmyHipyWrYR9FjF2iQi3rFonuV7Dn97spAmyp68NyWlji34L8lZk57weRj7l9HP58DodX02I8d4ACz4qYsYhoRERD5CiS4Zm5rtfGrpLLNJjnVPzEE1WrJA3tvYdith7mkqEnSa5zSjMumoBkMVjJ8o5p97DymIqupSd04ZP800aO5xnByDvqhRttoHF8uvWsVk4uwEJweNk6kQyP05qwq9tsnvx6tMJLN7udOiaoYzNhwHDrQf2zD59OrD4w0O5XupzZ2WVBOS3c5de7B6q1itz0kmaA7a2kPX4pcReSIoHo13W3QPgBvMdrWlk4cMF2WYJCdw5p0kqwm0VFHuv36OarXR3ze0rAGLxhFORjBHSQep6LdccuzltTMBEDJCuWFr3QmYg0mJfCDHeOJGnu6L1UGsJqexNNOuasftVUHPitNfcsqTSH4xLVFc3aOcFOuz4PPcgxodQOUATURDe3WodfLJdbCSLZLtgaMV2ZUTBxUXFQOtC7jocWPN2wYEVr0ogxoCUqyL1cjggrQJBSbChhJbUl63TZfxVFYZiY3AtZdUX04MHVkQwLBvyGUSn5LergOZPOgDbTifE0lgy6MeEu2yYjcbfbPpLj1Psm8kJZGcPpuypcZYimuMbvVBSSijdHxBXhX6sN0DsqBu2YTLq5pGJRXdl5rdxvTavhNcT3hhH5E5mVjwHdgk96qEpT";

    @Inject
    private DpsProxyProvider dpsProxyProvider;

    @Inject
    private AlarmReader alarmReader;

    ProcessedAlarmEvent buildNewProcessedAlarmEvent(final String type, final long alarmNumber, final String fdn, final String oor) {
        final ProcessedAlarmEvent alarmRecord = new ProcessedAlarmEvent();
        final Map<String, String> additionalInfo = new HashMap<String, String>();
        alarmRecord.setAlarmNumber(alarmNumber);
        alarmRecord.setRecordType(AlarmRecordType.ALARM);
        alarmRecord.setPresentSeverity(EventSeverity.CRITICAL);
        alarmRecord.setFdn(fdn);
        alarmRecord.setObjectOfReference(oor);
        alarmRecord.setAlarmId(new Date().getTime());
        alarmRecord.setProbableCause(type);
        alarmRecord.setProblemText(problemText);
        additionalInfo.put("generatedBy", "fm-capacity-poc");
        additionalInfo.put("objectOfReference", alarmRecord.getObjectOfReference());
        additionalInfo.put("fdn", alarmRecord.getFdn());
        additionalInfo.put("managedObject", "Equipment");
        additionalInfo.put("manualCease", "false");
        additionalInfo.put("timeZone", "UTC");
        alarmRecord.setAdditionalInformation(additionalInfo);
        return alarmRecord;
    }

    /**
     * Just perform one get operation before inserting one alarm into DPS
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean insertAlarm(final AlarmTestingInfo alarmTestingInfo) {
        final ProcessedAlarmEvent newAlarm = buildNewProcessedAlarmEvent(alarmTestingInfo.getType(), alarmTestingInfo.getId(),
                alarmTestingInfo.getFdn(), alarmTestingInfo.getOor());
        // before inserting the alarm check if it already exists in the DB
        final double startTime = System.currentTimeMillis();
        if (!alarmReader.isExistingAlarm(alarmTestingInfo)) {
            final Map<String, Object> alarmAttributeMap = AlarmAttributesPopulator.populateNewAlarm(newAlarm);
            long eventId = AlarmProcessorConstants.DEFAULT_EVENTPOID_VALUE;
            logger.info("INSERTING into DPS: {} ", alarmTestingInfo);
            final DataBucket liveBucket = dpsProxyProvider.getLiveBucket();
            final PersistenceObject alarmFromDatabase = liveBucket.getPersistenceObjectBuilder().namespace(OSS_FM).type(OPEN_ALARM)
                    .addAttributes(alarmAttributeMap).create();
            eventId = alarmFromDatabase.getPoId();
            alarmTestingInfo.setPoid(eventId);
            final double endTime = System.currentTimeMillis();
            logger.info("elapsedTime={}, INSERTED into DPS: poId={}, {} ", (endTime - startTime) / 1000, eventId, alarmTestingInfo);
            return true;
        }
        logger.info("ALARM NUMBER {} and OOR {} ALREADY EXIST into DPS", newAlarm.getAlarmNumber(), newAlarm.getObjectOfReference());
        return false;
    }

    /**
     * Just perform one cease alarm operation into DPS
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void ceaseAlarm(final AlarmTestingInfo alarmTestingInfo) {
        // before ceasing the alarm check if the correlated alarm exists in the DB
        final double startTime = System.currentTimeMillis();
        final PersistenceObject existingAlarm = alarmReader.getExistingAlarm(alarmTestingInfo);
        if (existingAlarm != null) {
            logger.info("PO ID: {}", existingAlarm.getPoId());
            // read all attributes of the correlated alarm in the DB
            final Map<String, Object> correlatedAlarmAttributes = readAllAttributes(existingAlarm.getPoId());
            logger.info("ALARM CEASING  {} ", correlatedAlarmAttributes);
            // set new attributes for the correlated alarm in the DB
            correlatedAlarmAttributes.put(ALARM_STATE, EventState.CLEARED_UNACKNOWLEDGED.name());
            final PersistenceObject ceaseAlarm = updateAlarm(existingAlarm.getPoId(), correlatedAlarmAttributes);
            final double endTime = System.currentTimeMillis();
            logger.info("elapsedTime={}, ALARM CEASED into DPS: {}", (endTime - startTime) / 1000, ceaseAlarm.getAllAttributes());
        } else {
            logger.info("ALARM CEASING : NOT EXISTING into DPS : {}", alarmTestingInfo);
        }
    }

    Map<String, Object> readAllAttributes(final long eventPoId) {
        final DataBucket liveBucket = dpsProxyProvider.getLiveBucket();
        final PersistenceObject alarmFromDataBase = liveBucket.findPoById(eventPoId);
        Map<String, Object> alarmAttributes = new HashMap<String, Object>();
        if (alarmFromDataBase != null) {
            alarmAttributes = alarmFromDataBase.getAllAttributes();
        }
        return alarmAttributes;
    }

    PersistenceObject updateAlarm(final Long poId, final Map<String, Object> newAttributes) {
        final DataBucket liveBucket = dpsProxyProvider.getLiveBucket();
        final PersistenceObject alarmFromDatabase = liveBucket.findPoById(poId);
        newAttributes.put(SYNC_STATE, true);
        if (alarmFromDatabase != null) {
            alarmFromDatabase.setAttributes(newAttributes);
        } else {
            logger.info("There exists no PO with POId: {} in database. Attributes: {} is not updated.", poId, newAttributes);
        }
        return alarmFromDatabase;
    }

}
