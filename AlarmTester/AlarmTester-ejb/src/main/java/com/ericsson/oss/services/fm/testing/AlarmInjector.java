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

import static com.ericsson.oss.services.fm.testing.utils.AlarmProcessorConstants.NORMAL;
import static com.ericsson.oss.services.fm.testing.utils.AlarmProcessorConstants.PEAK;
import static com.ericsson.oss.services.fm.testing.utils.AlarmProcessorConstants.STORM;

import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.service.fm.testing.model.AlarmTestingInfo;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AlarmInjector {
    private static final Logger logger = LoggerFactory.getLogger(AlarmInjector.class);

    @Inject
    AlarmInjectorState alarmInjectorState;

    @Inject
    private AlarmInjectorDps alarmInjectorDps;

    @Inject
    private Event<AlarmTestingInfo> ceaseEvent;

    /**
     * Just perform one get operation before inserting one alarm into DPS
     */
    @Asynchronous
    public Future<String> insertAlarm(final AlarmTestingInfo alarmTestingInfo, final boolean ceaseAlarm, final long limit) {
        final boolean inserted = alarmInjectorDps.insertAlarm(alarmTestingInfo);
        if (inserted) {
            switch (alarmTestingInfo.getType()) {
                case NORMAL:
                    if (alarmInjectorState.incrementActualAlarmNormalNumber(alarmTestingInfo) > limit) {
                        logger.info("Base Alarm Flow alarms: {} max limit: {}", alarmInjectorState.getNormalCount(), limit);
                        alarmInjectorState.notifyForNormalToRemove();
                    }
                    break;
                case PEAK:
                    if (alarmInjectorState.incrementActualAlarmPeakNumber(alarmTestingInfo) > limit) {
                        logger.info("Peak Alarm Flow alarms: {} max limit: {}", alarmInjectorState.getPeakCount(), limit);
                        alarmInjectorState.notifyForPeakToRemove();
                    }
                    break;
                case STORM:
                    if (alarmInjectorState.incrementActualAlarmStormNumber(alarmTestingInfo) > limit) {
                        logger.info("Storm Alarm Flow alarms: {} max limit: {}", alarmInjectorState.getStormCount(), limit);
                        alarmInjectorState.notifyForStormToRemove();
                    }
                    break;
            }
            if (ceaseAlarm) {
                ceaseEvent.fire(alarmTestingInfo);
            }
        }
        return new AsyncResult<String>("Done");
    }

    /**
     * Just perform one cease alarm operation into DPS
     */
    @Asynchronous
    public void ceaseAlarm(final AlarmTestingInfo alarmTestingInfo) {
        alarmInjectorDps.ceaseAlarm(alarmTestingInfo);
    }
}
