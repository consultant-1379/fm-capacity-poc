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

import static javax.ejb.ConcurrencyManagementType.CONTAINER;

import java.util.Date;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.AccessTimeout;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.Local;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.sdk.core.util.ServiceIdentity;

@Singleton
@Startup
@Local(AlarmTesting.class)
@ConcurrencyManagement(CONTAINER)
@AccessTimeout(value = 60, unit = TimeUnit.SECONDS)
@Lock(LockType.READ)
public class AlarmTester implements AlarmTesting {

    private static final Logger logger = LoggerFactory.getLogger(AlarmTester.class);
    static final String startNormalAlarmFlowWork = "startNormalAlarmFlowWork";
    static final String startPeakAlarmFlowWork = "startPeakAlarmFlowWork";
    static final String startStormAlarmFlowWork = "startStormAlarmFlowWork";
    static final String stopNormalAlarmFlowWork = "stopNormalAlarmFlowWork";
    static final String stopPeakAlarmFlowWork = "stopPeakAlarmFlowWork";
    static final String stopStormAlarmFlowWork = "stopStormAlarmFlowWork";
    static final String startRemoveNormalAlarmFlowWork = "startRemoveNormalAlarmFlowWork";
    static final String startRemovePeakAlarmFlowWork = "startRemovePeakAlarmFlowWork";
    static final String startRemoveStormAlarmFlowWork = "startRemoveStormAlarmFlowWork";
    static final String stopRemoveNormalAlarmFlowWork = "stopRemoveNormalAlarmFlowWork";
    static final String stopRemovePeakAlarmFlowWork = "stopRemovePeakAlarmFlowWork";
    static final String stopRemoveStormAlarmFlowWork = "stopRemoveStormAlarmFlowWork";
    static final String startRemoveAllNormalAlarmsWork = "startRemoveAllNormalAlarmsWork";
    static final String stopRemoveAllNormalAlarmsWork = "stopRemoveAllNormalAlarmsWork";
    static final String startRemoveAllPeakAlarmsWork = "startRemoveAllPeakAlarmsWork";
    static final String stopRemoveAllPeakAlarmsWork = "stopRemoveAllPeakAlarmsWork";
    static final String startRemoveAllStormAlarmsWork = "startRemoveAllStormAlarmsWork";
    static final String stopRemoveAllStormAlarmsWork = "stopRemoveAllStormAlarmsWork";
    static final String startReadAlarmFlowWork = "startReadAlarmFlowWork";
    static final String stopReadAlarmFlowWork = "stopReadAlarmFlowWork";

    @Inject
    AlarmFlow alarmFlow;

    @Inject
    AlarmInjectorState alarmInjectorState;

    @Inject
    private TimerService timerService;
    BlockingDeque<String> queue = new LinkedBlockingDeque<>();
    Timer theTimer;

    @Inject
    ServiceIdentity serviceIdentity;
    
    @PostConstruct
    public void startAlarmTesting() {
        final TimerConfig timerConfig = new TimerConfig(null, false);
        theTimer = timerService.createIntervalTimer(2000, 4000, timerConfig);
        logger.info("-----> Starting AlarmTester with nodeId:{} serviceId:{}",serviceIdentity.getNodeId(),serviceIdentity.getServiceId());
    }

    @PreDestroy
    public void stopAlarmTesting() {
        if (theTimer != null) {
            theTimer.cancel();
        }
        alarmInjectorState.setStopNormalAlarmFlow(true);
        alarmInjectorState.setStopPeakAlarmFlow(true);
        alarmInjectorState.setStopStormAlarmFlow(true);
        alarmInjectorState.setStopRemoveNormalFlow(true);
        alarmInjectorState.setStopRemovePeakFlow(true);
        alarmInjectorState.setStopRemoveStormFlow(true);
        logger.info("<----- Stopping AlarmTester...");
    }

    @Timeout
    void processQueueCommands() {
        logger.debug(">>>>>>>>>>>>>> Queue processing start timeout...");
        try {
            String command = queue.poll(1, TimeUnit.SECONDS);
            if (command != null) {
                logger.info("Found a command inside the queue: {}", command);

                String param = null;
                final String[] parameters = command.split(":");
                command = parameters[0];
                if (parameters.length == 2) {
                    param = parameters[1];
                }

                switch (command) {
                    case startNormalAlarmFlowWork:
                        if (alarmInjectorState.isStopNormalAlarmFlow() == true) {
                            alarmInjectorState.setStopNormalAlarmFlow(false);
                            this.alarmFlow.startNormalAlarmFlowWork();
                        } else {
                            logger.error("ALREADY STARTED {}", command);
                        }
                        break;
                    case startPeakAlarmFlowWork:
                        if (alarmInjectorState.isStopPeakAlarmFlow() == true) {
                            alarmInjectorState.setStopPeakAlarmFlow(false);
                            this.alarmFlow.startPeakAlarmFlowWork();
                        } else {
                            logger.error("ALREADY STARTED {}", command);
                        }
                        break;
                    case startStormAlarmFlowWork:
                        if (alarmInjectorState.isStopStormAlarmFlow() == true) {
                            alarmInjectorState.setStopStormAlarmFlow(false);
                            this.alarmFlow.startStormAlarmFlowWork();
                        } else {
                            logger.error("ALREADY STARTED {}", command);
                        }
                        break;
                    case stopNormalAlarmFlowWork:
                        logger.info("######### setStopNormalAlarmFlow ");
                        alarmInjectorState.setStopNormalAlarmFlow(true);
                        break;
                    case stopPeakAlarmFlowWork:
                        logger.info("######### setStopPeakAlarmFlow ");
                        alarmInjectorState.setStopPeakAlarmFlow(true);
                        break;
                    case stopStormAlarmFlowWork:
                        logger.info("######### setStopStormAlarmFlow ");
                        alarmInjectorState.setStopStormAlarmFlow(true);
                        break;
                    case startRemoveAllNormalAlarmsWork:
                        alarmInjectorState.setStopRemoveAllNormalFlow(false);
                        this.alarmFlow.startRemoveAllNormalAlarmsWork();
                        break;
                    case stopRemoveAllNormalAlarmsWork:
                        logger.info("######### setStopRemoveAllNormalAlarmFlow ");
                        alarmInjectorState.setStopRemoveAllNormalFlow(true);
                        break;
                    case startRemoveAllPeakAlarmsWork:
                        alarmInjectorState.setStopRemoveAllPeakFlow(false);
                        this.alarmFlow.startRemoveAllPeakAlarmsWork();
                        break;
                    case stopRemoveAllPeakAlarmsWork:
                        logger.info("######### setStopRemoveAllPeakAlarmFlow ");
                        alarmInjectorState.setStopRemoveAllPeakFlow(true);
                        break;
                    case startRemoveAllStormAlarmsWork:
                        alarmInjectorState.setStopRemoveAllStormFlow(false);
                        this.alarmFlow.startRemoveAllStormAlarmsWork();
                        break;
                    case stopRemoveAllStormAlarmsWork:
                        logger.info("######### setStopRemoveAllStormAlarmFlow ");
                        alarmInjectorState.setStopRemoveAllStormFlow(true);
                        break;
                    case startRemoveNormalAlarmFlowWork:
                        if (param != null) {
                            for (int i = Integer.valueOf(param); i < this.alarmInjectorState.getParallelAlarmNormalNumber(); i++) {
                                this.alarmFlow.startRemoveNormalAlarmFlowWork(i);
                            }
                        } else {
                            if (alarmInjectorState.isStopRemoveNormalFlow(0) == true) {
                                alarmInjectorState.setStopRemoveNormalFlow(false);
                                for (int i = 0; i < this.alarmInjectorState.getParallelAlarmNormalNumber(); i++) {
                                    this.alarmFlow.startRemoveNormalAlarmFlowWork(i);
                                }
                            } else {
                                logger.error("ALREADY STARTED {}", command);
                            }
                        }
                        break;
                    case startRemovePeakAlarmFlowWork:
                        if (alarmInjectorState.isStopRemovePeakFlow(0) == true) {
                            alarmInjectorState.setStopRemovePeakFlow(false);
                            for (int i = 0; i < this.alarmInjectorState.getParallelAlarmPeakNumber(); i++) {
                                this.alarmFlow.startRemovePeakAlarmFlowWork(i);
                            }
                        } else {
                            logger.error("ALREADY STARTED {}", command);
                        }
                        break;
                    case startRemoveStormAlarmFlowWork:
                        if (alarmInjectorState.isStopRemoveStormFlow(0) == true) {
                            alarmInjectorState.setStopRemoveStormFlow(false);
                            for (int i = 0; i < this.alarmInjectorState.getParallelAlarmStormNumber(); i++) {
                                this.alarmFlow.startRemoveStormAlarmFlowWork(i);
                            }
                        } else {
                            logger.error("ALREADY STARTED {}", command);
                        }
                        break;
                    case stopRemoveNormalAlarmFlowWork:
                        logger.info("######### stopRemoveNormalAlarmFlowWork ");
                        alarmInjectorState.setStopRemoveNormalFlow(true);
                        break;
                    case stopRemovePeakAlarmFlowWork:
                        logger.info("######### stopRemovePeakAlarmFlowWork ");
                        alarmInjectorState.setStopRemovePeakFlow(true);
                        break;
                    case stopRemoveStormAlarmFlowWork:
                        logger.info("######### stopRemoveStormAlarmFlowWork ");
                        alarmInjectorState.setStopRemoveStormFlow(true);
                        break;
                    case startReadAlarmFlowWork:
                        if (alarmInjectorState.isStopReadAlarmFlow(0) == true) {
                            alarmInjectorState.setStopReadAlarmFlow(false);
                            for (int i = 0; i < this.alarmInjectorState.getReadParallelNumber(); i++) {
                                this.alarmFlow.startReadAlarmFlowWork(i);
                            }
                        } else {
                            logger.error("ALREADY STARTED {}", command);
                        }
                        break;
                    case stopReadAlarmFlowWork:
                        alarmInjectorState.setStopReadAlarmFlow(true);
                        break;
                }
            }
        } catch (final Exception e) {
            logger.error("ERROR with polling of command queue ", e);
        }
        logger.debug(">>>>>>>>>>>>>> Queue processing stop timeout...");
    }

    protected AlarmTestingDataResponse buildResponse(final String msg) {
        final AlarmTestingDataResponse response = new AlarmTestingDataResponse();
        response.setMessage(msg);
        return response;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse changeParallelAlarmNormalNumber(final Integer parallelAlarmNormalNumber) {
        final Integer oldValue = this.alarmInjectorState.getParallelAlarmNormalNumber();
        this.alarmInjectorState.setParallelAlarmNormalNumber(parallelAlarmNormalNumber);
        if (alarmInjectorState.isStopRemoveNormalFlow(0) == false && oldValue < parallelAlarmNormalNumber) {
            queue.offer(startRemoveNormalAlarmFlowWork + ":" + oldValue);
        }
        return this.buildResponse("ParallelAlarmNormalNumber: oldValue " + oldValue + " newValue " + parallelAlarmNormalNumber);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse changePackingAlarmNormalNumber(final Integer packingAlarmNormalNumber) {
        final Integer oldValue = this.alarmInjectorState.getPackingAlarmNormalNumber();
        this.alarmInjectorState.setPackingAlarmNormalNumber(packingAlarmNormalNumber);
        return this.buildResponse("PackingAlarmNormalNumber: oldValue " + oldValue + " newValue " + packingAlarmNormalNumber);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse changeNormalAlarmThroughput(final Integer normalAlarmThroughput) {
        final Integer oldValue = this.alarmInjectorState.getNormalAlarmThroughput();
        this.alarmInjectorState.setNormalAlarmThroughput(normalAlarmThroughput);
        return this.buildResponse("NormalAlarmThroughput: oldValue " + oldValue + " newValue " + normalAlarmThroughput);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse changeNormalEnableCeaseAlarmPercentage(final Boolean normalEnableCeaseAlarmPercentage) {
        final boolean oldValue = this.alarmInjectorState.isNormalEnableCeaseAlarmPercentage();
        this.alarmInjectorState.setNormalEnableCeaseAlarmPercentage(normalEnableCeaseAlarmPercentage);
        return this.buildResponse("NormalCeaseAlarmPercentage: oldValue " + oldValue + " newValue " + normalEnableCeaseAlarmPercentage);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse changeNormalCeaseAlarmPercentage(final Integer normalCeaseAlarmPercentage) {
        final Integer oldValue = this.alarmInjectorState.getNormalCeaseAlarmPercentage();
        this.alarmInjectorState.setNormalCeaseAlarmPercentage(normalCeaseAlarmPercentage);
        return this.buildResponse("NormalCeaseAlarmPercentage: oldValue " + oldValue + " newValue " + normalCeaseAlarmPercentage);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse changeParallelAlarmPeakNumber(final Integer parallelAlarmPeakNumber) {
        final Integer oldValue = this.alarmInjectorState.getParallelAlarmPeakNumber();
        this.alarmInjectorState.setParallelAlarmNormalNumber(parallelAlarmPeakNumber);
        return this.buildResponse("ParallelAlarmPeakNumber: oldValue " + oldValue + " newValue " + parallelAlarmPeakNumber);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse changePackingAlarmPeakNumber(final Integer packingAlarmPeakNumber) {
        final Integer oldValue = this.alarmInjectorState.getPackingAlarmPeakNumber();
        this.alarmInjectorState.setPackingAlarmPeakNumber(packingAlarmPeakNumber);
        return this.buildResponse("PackingAlarmPeakNumber: oldValue " + oldValue + " newValue " + packingAlarmPeakNumber);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse changePeakAlarmThroughput(final Integer peakAlarmThroughput) {
        final Integer oldValue = this.alarmInjectorState.getPeakAlarmThroughput();
        this.alarmInjectorState.setPeakAlarmThroughput(peakAlarmThroughput);
        return this.buildResponse("PeakAlarmThroughput: oldValue " + oldValue + " newValue " + peakAlarmThroughput);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse changePeakEnableCeaseAlarmPercentage(final Boolean peakEnableCeaseAlarmPercentage) {
        final boolean oldValue = this.alarmInjectorState.isPeakEnableCeaseAlarmPercentage();
        this.alarmInjectorState.setPeakEnableCeaseAlarmPercentage(peakEnableCeaseAlarmPercentage);
        return this.buildResponse("PeakCeaseAlarmPercentage: oldValue " + oldValue + " newValue " + peakEnableCeaseAlarmPercentage);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse changePeakCeaseAlarmPercentage(final Integer peakCeaseAlarmPercentage) {
        final Integer oldValue = this.alarmInjectorState.getPeakCeaseAlarmPercentage();
        this.alarmInjectorState.setPeakCeaseAlarmPercentage(peakCeaseAlarmPercentage);
        return this.buildResponse("PeakCeaseAlarmPercentage: oldValue " + oldValue + " newValue " + peakCeaseAlarmPercentage);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse changePeakOnAlarmPeriod(final Integer peakOnAlarmPeriod) {
        final Integer oldValue = this.alarmInjectorState.getPeakOnAlarmPeriod();
        this.alarmInjectorState.setPeakOnAlarmPeriod(peakOnAlarmPeriod);
        return this.buildResponse("PeakOnAlarmPeriod: oldValue " + oldValue + " newValue " + peakOnAlarmPeriod);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse changePeakOffAlarmPeriod(final Integer peakOffAlarmPeriod) {
        final Integer oldValue = this.alarmInjectorState.getPeakOffAlarmPeriod();
        this.alarmInjectorState.setPeakOffAlarmPeriod(peakOffAlarmPeriod);
        return this.buildResponse("PeakOffAlarmPeriod: oldValue " + oldValue + " newValue " + peakOffAlarmPeriod);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse changeParallelAlarmStormNumber(final Integer parallelAlarmStormNumber) {
        final Integer oldValue = this.alarmInjectorState.getParallelAlarmStormNumber();
        this.alarmInjectorState.setParallelAlarmStormNumber(parallelAlarmStormNumber);
        return this.buildResponse("ParallelAlarmStormNumber: oldValue " + oldValue + " newValue " + parallelAlarmStormNumber);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse changePackingAlarmStormNumber(final Integer packingAlarmStormNumber) {
        final Integer oldValue = this.alarmInjectorState.getPackingAlarmStormNumber();
        this.alarmInjectorState.setPackingAlarmStormNumber(packingAlarmStormNumber);
        return this.buildResponse("PackingAlarmStormNumber: oldValue " + oldValue + " newValue " + packingAlarmStormNumber);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse changeStormAlarmThroughput(final Integer stormAlarmThroughput) {
        final Integer oldValue = this.alarmInjectorState.getStormAlarmThroughput();
        this.alarmInjectorState.setStormAlarmThroughput(stormAlarmThroughput);
        return this.buildResponse("StormAlarmThroughput: oldValue " + oldValue + " newValue " + stormAlarmThroughput);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse changeStormEnableCeaseAlarmPercentage(final Boolean stormEnableCeaseAlarmPercentage) {
        final boolean oldValue = this.alarmInjectorState.isStormEnableCeaseAlarmPercentage();
        this.alarmInjectorState.setStormEnableCeaseAlarmPercentage(stormEnableCeaseAlarmPercentage);
        return this.buildResponse("StormCeaseAlarmPercentage: oldValue " + oldValue + " newValue " + stormEnableCeaseAlarmPercentage);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse changeStormCeaseAlarmPercentage(final Integer stormCeaseAlarmPercentage) {
        final Integer oldValue = this.alarmInjectorState.getStormCeaseAlarmPercentage();
        this.alarmInjectorState.setStormCeaseAlarmPercentage(stormCeaseAlarmPercentage);
        return this.buildResponse("StormCeaseAlarmPercentage: oldValue " + oldValue + " newValue " + stormCeaseAlarmPercentage);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse changeStormOnAlarmPeriod(final Integer stormOnAlarmPeriod) {
        final Integer oldValue = this.alarmInjectorState.getStormOnAlarmPeriod();
        this.alarmInjectorState.setStormOnAlarmPeriod(stormOnAlarmPeriod);
        return this.buildResponse("StormOnAlarmPeriod: oldValue " + oldValue + " newValue " + stormOnAlarmPeriod);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse changeStormOffAlarmPeriod(final Integer stormOffAlarmPeriod) {
        final Integer oldValue = this.alarmInjectorState.getStormOffAlarmPeriod();
        this.alarmInjectorState.setStormOffAlarmPeriod(stormOffAlarmPeriod);
        return this.buildResponse("StormOffAlarmPeriod: oldValue " + oldValue + " newValue " + stormOffAlarmPeriod);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse startNormalAlarmFlow() {
        queue.offer(startNormalAlarmFlowWork);
        return this.buildResponse("Normal Alarm Flow is going to be started!");
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse startPeakAlarmFlow() {
        queue.offer(startPeakAlarmFlowWork);
        return this.buildResponse("Peak Alarm Flow is going to be started!");
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse startStormAlarmFlow() {
        queue.offer(startStormAlarmFlowWork);
        return this.buildResponse("Storm Alarm Flow is going to be started!");
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse stopNormalAlarmFlow() {
        queue.offer(stopNormalAlarmFlowWork);
        return this.buildResponse("Normal Alarm Flow is going to be stopped!");
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse stopPeakAlarmFlow() {
        queue.offer(stopPeakAlarmFlowWork);
        return this.buildResponse("Peak Alarm Flow is going to be stopped!");
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse stopStormAlarmFlow() {
        queue.offer(stopStormAlarmFlowWork);
        return this.buildResponse("Storm Alarm Flow is going to be stopped!");
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse startRemoveAllNormalAlarmFlow() {
        queue.offer(startRemoveAllNormalAlarmsWork);
        return this.buildResponse("Start Remove All Normal Alarm Flow is going to be done!");
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse stopRemoveAllNormalAlarmFlow() {
        queue.offer(stopRemoveAllNormalAlarmsWork);
        return this.buildResponse("Stop Remove All Normal Alarm Flow is going to be done!");
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse startRemoveAllPeakAlarmFlow() {
        queue.offer(startRemoveAllPeakAlarmsWork);
        return this.buildResponse("Start Remove All Peak Alarm Flow is going to be done!");
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse stopRemoveAllPeakAlarmFlow() {
        queue.offer(stopRemoveAllPeakAlarmsWork);
        return this.buildResponse("Stop Remove All Peak Alarm Flow is going to be done!");
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse startRemoveAllStormAlarmFlow() {
        queue.offer(startRemoveAllStormAlarmsWork);
        return this.buildResponse("Start Remove All Storm Alarm Flow is going to be done!");
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public AlarmTestingDataResponse stopRemoveAllStormAlarmFlow() {
        queue.offer(stopRemoveAllStormAlarmsWork);
        return this.buildResponse("Stop Remove All Storm Alarm Flow is going to be done!");
    }

    @Override
    public AlarmTestingDataResponse startRemoveNormalAlarmFlow() {
        queue.offer(startRemoveNormalAlarmFlowWork);
        return this.buildResponse("Remove Normal Alarm Flow is going to be started!");
    }

    @Override
    public AlarmTestingDataResponse stopRemoveNormalAlarmFlow() {
        queue.offer(stopRemoveNormalAlarmFlowWork);
        return this.buildResponse("Remove Normal Alarm Flow is going to be stopped!");
    }

    @Override
    public AlarmTestingDataResponse changeThresholdAlarmNormalPercentage(final Integer thresholdAlarmNormalPercentage) {
        final Integer oldValue = this.alarmInjectorState.getThresholdAlarmNormalPercentage();
        this.alarmInjectorState.setThresholdAlarmNormalPercentage(thresholdAlarmNormalPercentage);
        return this.buildResponse("Threshold Normal Alarm Percentage: oldValue " + oldValue + " newValue " + thresholdAlarmNormalPercentage);
    }

    @Override
    public AlarmTestingDataResponse changeThresholdAlarmNormalNumber(final Long thresholdAlarmNormalNumber) {
        final Long oldValue = this.alarmInjectorState.getThresholdAlarmNormalNumber();
        this.alarmInjectorState.setThresholdAlarmNormalNumber(thresholdAlarmNormalNumber);
        return this.buildResponse("Threshold Normal Alarm Number: oldValue " + oldValue + " newValue " + thresholdAlarmNormalNumber);
    }

    @Override
    public AlarmTestingDataResponse startRemovePeakAlarmFlow() {
        queue.offer(startRemovePeakAlarmFlowWork);
        return this.buildResponse("Remove Peak Alarm Flow is going to be started!");
    }

    @Override
    public AlarmTestingDataResponse stopRemovePeakAlarmFlow() {
        queue.offer(stopRemovePeakAlarmFlowWork);
        return this.buildResponse("Remove Peak Alarm Flow is going to be stopped!");
    }

    @Override
    public AlarmTestingDataResponse changeThresholdAlarmPeakPercentage(final Integer thresholdAlarmPeakPercentage) {
        final Integer oldValue = this.alarmInjectorState.getThresholdAlarmPeakPercentage();
        this.alarmInjectorState.setThresholdAlarmPeakPercentage(thresholdAlarmPeakPercentage);
        return this.buildResponse("Threshold Peak Alarm Percentage: oldValue " + oldValue + " newValue " + thresholdAlarmPeakPercentage);
    }

    @Override
    public AlarmTestingDataResponse changeThresholdAlarmPeakNumber(final Long thresholdAlarmPeakNumber) {
        final Long oldValue = this.alarmInjectorState.getThresholdAlarmPeakNumber();
        this.alarmInjectorState.setThresholdAlarmPeakNumber(thresholdAlarmPeakNumber);
        return this.buildResponse("Threshold Peak Alarm Number: oldValue " + oldValue + " newValue " + thresholdAlarmPeakNumber);
    }

    @Override
    public AlarmTestingDataResponse startRemoveStormAlarmFlow() {
        queue.offer(startRemoveStormAlarmFlowWork);
        return this.buildResponse("Remove Storm Alarm Flow is going to be started!");
    }

    @Override
    public AlarmTestingDataResponse stopRemoveStormAlarmFlow() {
        queue.offer(stopRemoveStormAlarmFlowWork);
        return this.buildResponse("Remove Storm Alarm Flow is going to be stopped!");
    }

    @Override
    public AlarmTestingDataResponse changeThresholdAlarmStormPercentage(final Integer thresholdAlarmStormPercentage) {
        final Integer oldValue = this.alarmInjectorState.getThresholdAlarmStormPercentage();
        this.alarmInjectorState.setThresholdAlarmStormPercentage(thresholdAlarmStormPercentage);
        return this.buildResponse("Threshold Storm Alarm Percentage: oldValue " + oldValue + " newValue " + thresholdAlarmStormPercentage);
    }

    @Override
    public AlarmTestingDataResponse changeThresholdAlarmStormNumber(final Long thresholdAlarmStormNumber) {
        final Long oldValue = this.alarmInjectorState.getThresholdAlarmStormNumber();
        this.alarmInjectorState.setThresholdAlarmStormNumber(thresholdAlarmStormNumber);
        return this.buildResponse("Threshold Storm Alarm Number: oldValue " + oldValue + " newValue " + thresholdAlarmStormNumber);
    }

    @Override
    public AlarmTestingDataResponse startReadAlarmFlow() {
        queue.offer(startReadAlarmFlowWork);
        return this.buildResponse("Read Alarm Flow is going to be started!");
    }

    @Override
    public AlarmTestingDataResponse stopReadAlarmFlow() {
        queue.offer(stopReadAlarmFlowWork);
        return this.buildResponse("Read Alarm Flow is going to be stopped!");
    }

    @Override
    public AlarmTestingDataResponse changeReadParallelNumber(final Long readParallelNumber) {
        final Long oldValue = this.alarmInjectorState.getReadParallelNumber();
        this.alarmInjectorState.setReadParallelNumber(readParallelNumber);
        return this.buildResponse("Read Parallel Number: oldValue " + oldValue + " newValue " + readParallelNumber);
    }

    @Override
    public AlarmTestingDataResponse changeReadPausePeriod(final Long readPausePeriod) {
        final Long oldValue = this.alarmInjectorState.getReadPausePeriod();
        this.alarmInjectorState.setReadPausePeriod(readPausePeriod);
        return this.buildResponse("Read Pause Period: oldValue " + oldValue + " newValue " + readPausePeriod);
    }

    @Override
    public AlarmTestingDataResponse getCurrentAlarmNumber() {
        return this.buildResponse(this.alarmInjectorState.getNormalCount() + " on " + new Date());
    }

}
