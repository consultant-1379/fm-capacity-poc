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
import static com.ericsson.oss.services.fm.testing.utils.AlarmProcessorConstants.SLEEP_DURING_REMOVE;
import static com.ericsson.oss.services.fm.testing.utils.AlarmProcessorConstants.STORM;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.sdk.core.util.ServiceIdentity;
import com.ericsson.oss.service.fm.testing.model.AlarmTestingInfo;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class AlarmFlow {
    private static final Logger logger = LoggerFactory.getLogger(AlarmFlow.class);

    @Inject
    AlarmInjector alarmInjector;

    @Inject
    AlarmReader alarmReader;

    @Inject
    AlarmRemover alarmRemover;

    @Inject
    AlarmInjectorState alarmInjectorState;

    Random random = new Random();

    @Inject
    ServiceIdentity serviceIdentity;
    
    @Asynchronous
    public void startNormalAlarmFlowWork() {
        logger.info("######### STARTING BASE ALARM FLOW on blade:{}",serviceIdentity.getNodeId());
        long insNumber = 100, ceaseNumber = 0, ceaseCounter = 0;
        final boolean alarmCeaseEnabled = alarmInjectorState.isNormalEnableCeaseAlarmPercentage();
        if (alarmCeaseEnabled) {
            ceaseNumber = alarmInjectorState.getNormalCeaseAlarmPercentage();
            insNumber -= ceaseNumber;
        }
        long limit = alarmInjectorState.getNormalLimit();
        logger.info("Base alarm flow data-> throughput: {} alarms/sec, max limit of written alarm number on DB: {}, insert: {}%, cease: {}%",
                alarmInjectorState.getNormalAlarmThroughput(), limit, insNumber, ceaseNumber);
        long counter = !isSecondInstance() ? 1 : Long.MAX_VALUE/2;
        final List<Future<String>> jobs = new ArrayList<>();
        while (!alarmInjectorState.isStopNormalAlarmFlow()) {
            limit = alarmInjectorState.getNormalLimit();
            final long startThroughputTime = System.currentTimeMillis();
            for (int alarm = 0; alarm < alarmInjectorState.getNormalAlarmThroughput(); alarm++) {
                final long rNumber = getNextRandomNumber();
                final String fdn = getNextFdn(rNumber);
                final String oor = getNextObjectOfReference(rNumber);
                ceaseCounter++;
                logger.info("BASE FLOW-> INSERT ALARM: {}", counter);
                final AlarmTestingInfo ati = new AlarmTestingInfo(NORMAL, counter, fdn, oor);
                jobs.add(alarmInjector.insertAlarm(ati, ceaseCounter <= ceaseNumber, limit));
                if (ceaseCounter <= ceaseNumber) {
                    alarm++;
                }
                if (counter % insNumber == 0) {
                    ceaseCounter = 0;
                }
                counter++;
            }
            final long endThroughputTime = System.currentTimeMillis() - startThroughputTime;
            try {
                final long throughputDiff = 1000L - endThroughputTime;
                if (throughputDiff > 0) {
                    Thread.sleep(throughputDiff);
                }
            } catch (final InterruptedException e) {
                logger.error("ERROR on base alarm flow with thread sleeping", e);
            }

            jobs.removeIf(f -> f.isDone());

            if (jobs.size() > 0) {
                logger.info("FOUND incomplete {} jobs.", jobs.size());
            }

            if (jobs.size() > alarmInjectorState.getNormalAlarmThroughput()) {
                logger.info("CANCELLING {} jobs.", jobs.size());
                jobs.forEach(j -> j.cancel(false));
                jobs.clear();
            }
        }
        jobs.clear();
        logger.info("######### STOPPING BASE ALARM FLOW on blade:{}",serviceIdentity.getNodeId());
    }

    @Asynchronous
    public void startPeakAlarmFlowWork() {
        logger.info("######### STARTING PEAK ALARM FLOW");
        long insNumber = 100, ceaseNumber = 0, ceaseCounter = 0;
        final boolean alarmCeaseEnabled = alarmInjectorState.isPeakEnableCeaseAlarmPercentage();
        if (alarmCeaseEnabled) {
            ceaseNumber = alarmInjectorState.getPeakCeaseAlarmPercentage();
            insNumber -= ceaseNumber;
        }
        // peak ON period in milliseconds
        final long peakOnAlarmPeriod = 60 * 1000 * alarmInjectorState.getPeakOnAlarmPeriod();
        // peak OFF period in milliseconds
        final long peakOffAlarmPeriod = 60 * 1000 * alarmInjectorState.getPeakOffAlarmPeriod();
        // peak max limit number of alarms
        long limit = alarmInjectorState.getPeakLimit();
        logger.info(
                "Peak alarm flow data-> throughput: {} alarms/sec, ON period: {}, OFF period: {}, max limit of written alarm number on DB: {}, insert: {}%, cease: {}%",
                alarmInjectorState.getPeakAlarmThroughput(), peakOnAlarmPeriod, peakOffAlarmPeriod, limit, insNumber, ceaseNumber);
        long counter = 1;
        // loop for simulating PEAK ON and PEAK OFF period
        while (!alarmInjectorState.isStopPeakAlarmFlow()) {
            // PEAK ON : flow of alarms...
            limit = alarmInjectorState.getPeakLimit();
            final long startPeakOnTime = System.currentTimeMillis();
            logger.info("STARTING Peak alarm ON period !");
            while (!alarmInjectorState.isStopPeakAlarmFlow()) {
                final long startThroughoutTime = System.currentTimeMillis();
                for (int alarm = 0; alarm < alarmInjectorState.getPeakAlarmThroughput(); alarm++) {
                    final long rNumber = getNextRandomNumber();
                    final String fdn = getNextFdn(rNumber);
                    final String oor = getNextObjectOfReference(rNumber);
                    ceaseCounter++;
                    logger.info("PEAK FLOW-> INSERT: {}", counter);
                    final AlarmTestingInfo ati = new AlarmTestingInfo(PEAK, counter, fdn, oor);
                    alarmInjector.insertAlarm(ati, ceaseCounter <= ceaseNumber, limit);
                    if (ceaseCounter <= ceaseNumber) {
                        alarm++;
                    }
                    if (counter % insNumber == 0) {
                        ceaseCounter = 0;
                    }
                    counter++;
                }
                final long endThroughputTime = System.currentTimeMillis() - startThroughoutTime;
                try {
                    final long throughputDiff = 1000L - endThroughputTime;
                    if (throughputDiff > 0) {
                        Thread.sleep(throughputDiff);
                    }
                } catch (final InterruptedException e) {
                    logger.error("ERROR on peak alarm flow with thread sleeping", e);
                }

                if ((System.currentTimeMillis() - startPeakOnTime) >= peakOnAlarmPeriod) {
                    logger.info("ENDING Peak alarm ON period !");
                    break;
                }
            }
            // PEAK OFF : waiting period...
            final long startPeakOffTime = System.currentTimeMillis();
            logger.info("STARTING Peak alarm OFF period !");
            while (!alarmInjectorState.isStopPeakAlarmFlow()) {
                try {
                    Thread.sleep(5000);
                } catch (final InterruptedException e) {
                    logger.error("ERROR: peak OFF alarm flow thread sleeping ", e);
                }
                if ((System.currentTimeMillis() - startPeakOffTime) >= peakOffAlarmPeriod) {
                    logger.info("ENDING Peak alarm OFF period !");
                    break;
                }
            }
        }
        logger.info("######### STOPPING PEAK ALARM FLOW");
    }

    @Asynchronous
    public void startStormAlarmFlowWork() {
        logger.info("######### STARTING STORM ALARM FLOW");
        long insNumber = 100, ceaseNumber = 0, ceaseCounter = 0;
        final boolean alarmCeaseEnabled = alarmInjectorState.isStormEnableCeaseAlarmPercentage();
        if (alarmCeaseEnabled) {
            ceaseNumber = alarmInjectorState.getStormCeaseAlarmPercentage();
            insNumber -= ceaseNumber;
        }
        // storm ON period in milliseconds
        final long stormOnAlarmPeriod = 60 * 1000 * alarmInjectorState.getStormOnAlarmPeriod();
        // storm OFF period in milliseconds
        final long stormOffAlarmPeriod = 60 * 1000 * alarmInjectorState.getStormOffAlarmPeriod();
        // storm max limit number of alarms
        long limit = alarmInjectorState.getStormLimit();
        logger.info(
                "Storm alarm flow data-> throughput: {} alarms/sec, ON period: {}, OFF period: {}, max limit of written alarm number on DB: {}, insert: {}%, cease: {}%",
                alarmInjectorState.getStormAlarmThroughput(), stormOnAlarmPeriod, stormOffAlarmPeriod, limit, insNumber, ceaseNumber);
        long counter = 1;
        // loop for simulating STORM ON and STORM OFF period
        while (!alarmInjectorState.isStopStormAlarmFlow()) {
            // STORM ON : flow of alarms...
            limit = alarmInjectorState.getStormLimit();
            final long startStormOnTime = System.currentTimeMillis();
            logger.info("STARTING Storm alarm ON period !");
            while (!alarmInjectorState.isStopStormAlarmFlow()) {
                final long startThroughoutTime = System.currentTimeMillis();
                for (int alarm = 0; alarm < alarmInjectorState.getStormAlarmThroughput(); alarm++) {
                    final long rNumber = getNextRandomNumber();
                    final String fdn = getNextFdn(rNumber);
                    final String oor = getNextObjectOfReference(rNumber);
                    ceaseCounter++;
                    logger.info("STORM FLOW-> INSERT: {}", counter);
                    final AlarmTestingInfo ati = new AlarmTestingInfo(STORM, counter, fdn, oor);
                    alarmInjector.insertAlarm(ati, ceaseCounter <= ceaseNumber, limit);
                    if (ceaseCounter <= ceaseNumber) {
                        alarm++;
                    }
                    if (counter % insNumber == 0) {
                        ceaseCounter = 0;
                    }
                    counter++;
                }
                final long endThroughputTime = System.currentTimeMillis() - startThroughoutTime;
                try {
                    final long throughputDiff = 1000L - endThroughputTime;
                    if (throughputDiff > 0) {
                        Thread.sleep(throughputDiff);
                    }
                } catch (final InterruptedException e) {
                    logger.error("ERROR on storm alarm flow with thread sleeping", e);
                }

                if ((System.currentTimeMillis() - startStormOnTime) >= stormOnAlarmPeriod) {
                    logger.info("ENDING Storm alarm ON period !");
                    break;
                }
            }
            // STORM OFF : waiting period...
            final long startStormOffTime = System.currentTimeMillis();
            logger.info("STARTING Storm alarm OFF period !");
            while (!alarmInjectorState.isStopStormAlarmFlow()) {
                try {
                    Thread.sleep(5000);
                } catch (final InterruptedException e) {
                    logger.error("ERROR: storm OFF alarm flow thread sleeping ", e);
                }
                if ((System.currentTimeMillis() - startStormOffTime) >= stormOffAlarmPeriod) {
                    logger.info("ENDING Storm alarm OFF period !");
                    break;
                }
            }
        }
        logger.info("######### STOPPING STORM ALARM FLOW");
    }

    @Asynchronous
    public void startRemoveAllNormalAlarmsWork() {
        try {
            logger.info("######### STARTING REMOVING ALL \"NORMAL\" ALARMS ");
            final List<Long> poIdList = alarmRemover.getAllAlarmToRemove(NORMAL);
            long counter = 0;
            for (final Long poId : poIdList) {
                try {
                    alarmRemover.removeAlarm(poId);
                    counter++;
                } catch (final Exception e) {
                    logger.error("Error while removing peak alarm ...", e);
                }
                if (this.alarmInjectorState.isStopRemoveAllNormalFlow()) {
                    break;
                }
            }
            logger.info("Removed Normal alarms: {} ", counter);
            alarmInjectorState.resetAlarmNormalNumber();
            logger.info("######### STOPPING REMOVING ALL \"NORMAL\" ALARMS ");
        } catch (final Exception ex) {
            logger.error("ERROR", ex);
        }
    }

    @Asynchronous
    public void startRemoveAllPeakAlarmsWork() {
        logger.info("######### STARTING REMOVING ALL \"PEAK\" ALARMS ");
        final List<Long> poIdList = alarmRemover.getAllAlarmToRemove(PEAK);
        long counter = 0;
        for (final Long poId : poIdList) {
            try {
                alarmRemover.removeAlarm(poId);
                counter++;
            } catch (final Exception e) {
                logger.error("Error while removing peak alarm ...", e);
            }
            if (this.alarmInjectorState.isStopRemoveAllPeakFlow()) {
                break;
            }
        }
        logger.info("Removed Peak alarms: {}", counter);
        alarmInjectorState.resetAlarmPeakNumber();
        logger.info("######### STOPPING REMOVING ALL \"PEAK\" ALARMS ");
    }

    @Asynchronous
    public void startRemoveAllStormAlarmsWork() {
        logger.info("######### STARTING REMOVING ALL \"STORM\" ALARMS ");
        final List<Long> poIdList = alarmRemover.getAllAlarmToRemove(STORM);
        long counter = 0;
        for (final Long poId : poIdList) {
            try {
                alarmRemover.removeAlarm(poId);
                counter++;
            } catch (final Exception e) {
                logger.error("Error while removing storm alarm ...", e);
            }
            if (this.alarmInjectorState.isStopRemoveAllStormFlow()) {
                break;
            }
        }
        logger.info("Removed Storm alarms: {}", counter);
        alarmInjectorState.resetAlarmStormNumber();
        logger.info("######### STOPPING REMOVING ALL \"STORM\" ALARMS ");
    }

    @Asynchronous
    public void startRemoveNormalAlarmFlowWork(final int parallelId) {
        logger.info("######### STARTING REMOVAL OF BASE ALARM FLOW WITH ID: {} on blade:{}", parallelId,serviceIdentity.getNodeId());
        final List<AlarmTestingInfo> removingList = new ArrayList<>();
        while (!alarmInjectorState.isStopRemoveNormalFlow(parallelId)) {
            final long threshold = alarmInjectorState.getThresholdAlarmNormalNumber();
            final long percentage = alarmInjectorState.getThresholdAlarmNormalPercentage();
            final long upLimit = alarmInjectorState.getNormalLimit();
            final long downLimit = (long) ((double) (100 * threshold - percentage * threshold) / 100);
            final int packingNumber = this.alarmInjectorState.getPackingAlarmNormalNumber();

            // lock for normal...
            alarmInjectorState.waitForNormalToRemove();
            if (alarmInjectorState.getNormalCount() > upLimit) {
                while (alarmInjectorState.getNormalCount() > downLimit) {
                    removingList.clear();
                    AlarmTestingInfo ati = null;
                    while ((ati = alarmInjectorState.getNextNormalAlarmTestingInfo()) != null) {
                        removingList.add(ati);
                        if (removingList.size() >= packingNumber || (alarmInjectorState.getNormalCount() <= downLimit)) {
                            break;
                        }
                        if (removingList.size() % 20 == 0) {
                            try {
                                Thread.sleep(SLEEP_DURING_REMOVE);
                            } catch (final InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if (alarmInjectorState.isStopRemoveNormalFlow(parallelId)) {
                            break;
                        }
                    }
                    alarmRemover.removeAlarm(removingList);
                }
            }
        }
        removingList.clear();
        logger.info("######### STOPPING REMOVAL OF BASE ALARM FLOW WITH ID: {} on blade: {}", parallelId,serviceIdentity.getNodeId());
    }

    @Asynchronous
    public void startRemovePeakAlarmFlowWork(final int parallelId) {
        logger.info("######### STARTING REMOVAL OF PEAK ALARM FLOW WITH ID: {}", parallelId);
        final List<AlarmTestingInfo> removingList = new ArrayList<>();
        while (!alarmInjectorState.isStopRemovePeakFlow(parallelId)) {
            final long threshold = alarmInjectorState.getThresholdAlarmPeakNumber();
            final long percentage = alarmInjectorState.getThresholdAlarmPeakPercentage();
            final long upLimit = alarmInjectorState.getPeakLimit();
            final long downLimit = (long) ((double) (100 * threshold - percentage * threshold) / 100);
            final int packingNumber = this.alarmInjectorState.getPackingAlarmPeakNumber();

            // lock for normal...
            alarmInjectorState.waitForPeakToRemove();
            if (alarmInjectorState.getPeakCount() > upLimit) {
                while (alarmInjectorState.getPeakCount() > downLimit) {
                    removingList.clear();
                    AlarmTestingInfo ati = null;
                    while ((ati = alarmInjectorState.getNextPeakAlarmTestingInfo()) != null) {
                        removingList.add(ati);
                        if (removingList.size() >= packingNumber || (alarmInjectorState.getPeakCount() <= downLimit)) {
                            break;
                        }
                        if (removingList.size() % 20 == 0) {
                            try {
                                Thread.sleep(SLEEP_DURING_REMOVE);
                            } catch (final InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if (alarmInjectorState.isStopRemovePeakFlow(parallelId)) {
                            break;
                        }
                    }
                    alarmRemover.removeAlarm(removingList);
                }
            }
        }
        removingList.clear();
        logger.info("######### STOPPING REMOVAL OF PEAK ALARM FLOW WITH ID: {}", parallelId);
    }

    @Asynchronous
    public void startRemoveStormAlarmFlowWork(final int parallelId) {
        logger.info("######### STARTING REMOVAL OF STORM ALARM FLOW WITH ID: {}", parallelId);
        final List<AlarmTestingInfo> removingList = new ArrayList<>();
        while (!alarmInjectorState.isStopRemoveStormFlow(parallelId)) {
            final long threshold = alarmInjectorState.getThresholdAlarmStormNumber();
            final long percentage = alarmInjectorState.getThresholdAlarmStormPercentage();
            final long upLimit = alarmInjectorState.getStormLimit();
            final long downLimit = (long) ((double) (100 * threshold - percentage * threshold) / 100);
            final int packingNumber = this.alarmInjectorState.getPackingAlarmStormNumber();

            // lock for normal...
            alarmInjectorState.waitForStormToRemove();
            if (alarmInjectorState.getStormCount() > upLimit) {
                while (alarmInjectorState.getStormCount() > downLimit) {
                    removingList.clear();
                    AlarmTestingInfo ati = null;
                    while ((ati = alarmInjectorState.getNextStormAlarmTestingInfo()) != null) {
                        removingList.add(ati);
                        if (removingList.size() >= packingNumber || (alarmInjectorState.getStormCount() <= downLimit)) {
                            break;
                        }
                        if (removingList.size() % 20 == 0) {
                            try {
                                Thread.sleep(SLEEP_DURING_REMOVE);
                            } catch (final InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if (alarmInjectorState.isStopRemoveStormFlow(parallelId)) {
                            break;
                        }
                    }
                    alarmRemover.removeAlarm(removingList);
                }
            }
        }
        removingList.clear();
        logger.info("######### STOPPING REMOVAL OF STORM ALARM FLOW WITH ID: {}", parallelId);
    }

    @Asynchronous
    public void startReadAlarmFlowWork(final int parallelId) {
        logger.info("######### STARTING READ ALARM FLOW {}", parallelId);
        while (!alarmInjectorState.isStopReadAlarmFlow(parallelId)) {
            alarmReader.readAllAlarms(parallelId);
            // waiting period...
            logger.info("STARTING Read alarm OFF period !");
            final long startReadOffTime = System.currentTimeMillis();
            while (!alarmInjectorState.isStopReadAlarmFlow(parallelId)) {
                try {
                    Thread.sleep(1000);
                } catch (final InterruptedException e) {
                    logger.error("ERROR: Read OFF alarm flow thread sleeping ", e);
                }
                if ((System.currentTimeMillis() - startReadOffTime) >= (1000 * this.alarmInjectorState.getReadPausePeriod())) {
                    logger.info("ENDING Read alarm OFF period !");
                    break;
                }
            }
        }
        logger.info("######### STOPPING READ ALARM FLOW {}", parallelId);
    }

    public void observeEvent(@Observes final AlarmTestingInfo message) {
        alarmInjector.ceaseAlarm(message);
    }

    protected long getNextRandomNumber() {
        return random.nextInt() & Integer.MAX_VALUE;
    }

    protected String getNextFdn(final long rNumber) {
        return "ManagedObject=" + rNumber;
    }

    protected String getNextObjectOfReference(final long rNumber) {
        return "SubNetwork=" + rNumber + ",MeContext=" + rNumber + ",ManagedElement=1,Equipment=1";
    }
    
    protected boolean isSecondInstance() {
        try {
            String nodeId = serviceIdentity.getNodeId();
            if(nodeId.endsWith("1")){
                return true;
            }
        } catch(Exception ex) {
            logger.error("ERROR : ",ex);
        }
        return false;
    }
}
