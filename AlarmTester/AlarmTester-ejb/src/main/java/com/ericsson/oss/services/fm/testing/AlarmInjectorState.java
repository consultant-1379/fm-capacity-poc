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

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.enterprise.context.ApplicationScoped;

import com.ericsson.oss.service.fm.testing.model.AlarmTestingInfo;

@ApplicationScoped
public class AlarmInjectorState {
    // alarms per second ...
    volatile int normalAlarmThroughput = 30;
    volatile int peakAlarmThroughput = 50;
    volatile int stormAlarmThroughput = 300;

    // alarm cease ...
    volatile boolean normalEnableCeaseAlarmPercentage = false;
    volatile int normalCeaseAlarmPercentage = 10;
    volatile boolean peakEnableCeaseAlarmPercentage = false;
    volatile int peakCeaseAlarmPercentage = 10;
    volatile boolean stormEnableCeaseAlarmPercentage = false;
    volatile int stormCeaseAlarmPercentage = 10;

    // period ON in minutes
    volatile int peakOnAlarmPeriod = 15;
    volatile int stormOnAlarmPeriod = 4;
    // period OFF in minutes
    volatile int peakOffAlarmPeriod = 15;
    volatile int stormOffAlarmPeriod = 176;

    //volatile AtomicLong actualAlarmNormalNumber = new AtomicLong();
    volatile int parallelAlarmNormalNumber = 20;
    volatile int packingAlarmNormalNumber = 10;
    //volatile AtomicLong actualAlarmPeakNumber = new AtomicLong();
    volatile int parallelAlarmPeakNumber = 20;
    volatile int packingAlarmPeakNumber = 10;
    //volatile AtomicLong actualAlarmStormNumber = new AtomicLong();
    volatile int parallelAlarmStormNumber = 20;
    volatile int packingAlarmStormNumber = 10;

    // removing thresholds...
    volatile int thresholdAlarmNormalPercentage = 50;
    volatile int thresholdAlarmPeakPercentage = 50;
    volatile int thresholdAlarmStormPercentage = 50;
    volatile long thresholdAlarmNormalNumber = 0;
    volatile long thresholdAlarmPeakNumber = 0;
    volatile long thresholdAlarmStormNumber = 0;

    // stop flags...
    volatile boolean stopNormalAlarmFlow = true;
    volatile boolean stopPeakAlarmFlow = true;
    volatile boolean stopStormAlarmFlow = true;
    volatile boolean stopRemoveNormalFlow = true;
    volatile boolean stopRemoveAllNormalFlow = true;
    volatile boolean stopRemovePeakFlow = true;
    volatile boolean stopRemoveAllPeakFlow = true;
    volatile boolean stopRemoveStormFlow = true;
    volatile boolean stopRemoveAllStormFlow = true;
    volatile boolean stopReadAlarmFlow = true;

    Object normalRemoveLock = new Object();
    ConcurrentLinkedQueue<AlarmTestingInfo> normalRemoveQueue = new ConcurrentLinkedQueue<>();
    Object peakRemoveLock = new Object();
    ConcurrentLinkedQueue<AlarmTestingInfo> peakRemoveQueue = new ConcurrentLinkedQueue<>();
    Object stormRemoveLock = new Object();
    ConcurrentLinkedQueue<AlarmTestingInfo> stormRemoveQueue = new ConcurrentLinkedQueue<>();

    volatile long readParallelNumber;
    volatile long readPausePeriod;

    public long getThresholdAlarmNormalNumber() {
        return thresholdAlarmNormalNumber;
    }

    public int getThresholdAlarmNormalPercentage() {
        return thresholdAlarmNormalPercentage;
    }

    public long getNormalLimit() {
        return (long) ((double) (100 * thresholdAlarmNormalNumber + thresholdAlarmNormalPercentage * thresholdAlarmNormalNumber) / 100);
    }

    public long getPeakLimit() {
        return (long) ((double) (100 * thresholdAlarmPeakNumber + thresholdAlarmPeakPercentage * thresholdAlarmPeakNumber) / 100);
    }

    public long getStormLimit() {
        return (long) ((double) (100 * thresholdAlarmStormNumber + thresholdAlarmStormPercentage * thresholdAlarmStormNumber) / 100);
    }

    public void setThresholdAlarmNormalNumber(final long thresholdAlarmNormalNumber) {
        this.thresholdAlarmNormalNumber = thresholdAlarmNormalNumber;
    }

    public long getThresholdAlarmPeakNumber() {
        return thresholdAlarmPeakNumber;
    }

    public void setThresholdAlarmPeakNumber(final long thresholdAlarmPeakNumber) {
        this.thresholdAlarmPeakNumber = thresholdAlarmPeakNumber;
    }

    public long getThresholdAlarmStormNumber() {
        return thresholdAlarmStormNumber;
    }

    public void setThresholdAlarmStormNumber(final long thresholdAlarmStormNumber) {
        this.thresholdAlarmStormNumber = thresholdAlarmStormNumber;
    }

    public boolean isStopRemoveNormalFlow(final int parallelId) {
        if (parallelId >= parallelAlarmNormalNumber) {
            return true;
        }
        return stopRemoveNormalFlow;
    }

    public void setStopRemoveNormalFlow(final boolean stopRemoveNormalFlow) {
        this.stopRemoveNormalFlow = stopRemoveNormalFlow;
        notifyForNormalToRemove();
    }

    public boolean isStopRemovePeakFlow(final int parallelId) {
        if (parallelId >= parallelAlarmPeakNumber) {
            return true;
        }
        return stopRemovePeakFlow;
    }

    public void setStopRemovePeakFlow(final boolean stopRemovePeakFlow) {
        this.stopRemovePeakFlow = stopRemovePeakFlow;
    }

    public boolean isStopRemoveStormFlow(final int parallelId) {
        if (parallelId >= parallelAlarmStormNumber) {
            return true;
        }
        return stopRemoveStormFlow;
    }

    public void setStopRemoveStormFlow(final boolean stopRemoveStormFlow) {
        this.stopRemoveStormFlow = stopRemoveStormFlow;
    }

    public int getNormalAlarmThroughput() {
        return normalAlarmThroughput;
    }

    public void setNormalAlarmThroughput(final int normalAlarmThroughput) {
        this.normalAlarmThroughput = normalAlarmThroughput;
    }

    public int getPeakAlarmThroughput() {
        return peakAlarmThroughput;
    }

    public void setPeakAlarmThroughput(final int peakAlarmThroughput) {
        this.peakAlarmThroughput = peakAlarmThroughput;
    }

    public int getPeakOnAlarmPeriod() {
        return peakOnAlarmPeriod;
    }

    public void setPeakOnAlarmPeriod(final int peakOnAlarmPeriod) {
        this.peakOnAlarmPeriod = peakOnAlarmPeriod;
    }

    public int getPeakOffAlarmPeriod() {
        return peakOffAlarmPeriod;
    }

    public void setPeakOffAlarmPeriod(final int peakOffAlarmPeriod) {
        this.peakOffAlarmPeriod = peakOffAlarmPeriod;
    }

    public int getStormAlarmThroughput() {
        return stormAlarmThroughput;
    }

    public void setStormAlarmThroughput(final int stormAlarmThroughput) {
        this.stormAlarmThroughput = stormAlarmThroughput;
    }

    public int getStormOnAlarmPeriod() {
        return stormOnAlarmPeriod;
    }

    public void setStormOnAlarmPeriod(final int stormOnAlarmPeriod) {
        this.stormOnAlarmPeriod = stormOnAlarmPeriod;
    }

    public int getStormOffAlarmPeriod() {
        return stormOffAlarmPeriod;
    }

    public void setStormOffAlarmPeriod(final int stormOffAlarmPeriod) {
        this.stormOffAlarmPeriod = stormOffAlarmPeriod;
    }

    public boolean isStopNormalAlarmFlow() {
        return stopNormalAlarmFlow;
    }

    public void setStopNormalAlarmFlow(final boolean stopNormalAlarmFlow) {
        this.stopNormalAlarmFlow = stopNormalAlarmFlow;
        notifyForNormalToRemove();
    }

    public boolean isStopPeakAlarmFlow() {
        return stopPeakAlarmFlow;
    }

    public void setStopPeakAlarmFlow(final boolean stopPeakAlarmFlow) {
        this.stopPeakAlarmFlow = stopPeakAlarmFlow;
    }

    public boolean isStopStormAlarmFlow() {
        return stopStormAlarmFlow;
    }

    public void setStopStormAlarmFlow(final boolean stopStormAlarmFlow) {
        this.stopStormAlarmFlow = stopStormAlarmFlow;
    }

    public long incrementActualAlarmNormalNumber(final AlarmTestingInfo alarmTestingInfo) {
        this.normalRemoveQueue.offer(alarmTestingInfo);
        return this.normalRemoveQueue.size();
    }

    public AlarmTestingInfo getNextNormalAlarmTestingInfo() {
        return this.normalRemoveQueue.poll();
    }

    public int getNormalCount() {
        return this.normalRemoveQueue.size();
    }

    public void resetAlarmNormalNumber() {
        this.normalRemoveQueue.clear();
    }

    public long incrementActualAlarmPeakNumber(final AlarmTestingInfo alarmTestingInfo) {
        this.peakRemoveQueue.offer(alarmTestingInfo);
        return this.peakRemoveQueue.size();
    }

    public AlarmTestingInfo getNextPeakAlarmTestingInfo() {
        return this.peakRemoveQueue.poll();
    }

    public long getPeakCount() {
        return this.peakRemoveQueue.size();
    }

    public void resetAlarmPeakNumber() {
        this.peakRemoveQueue.clear();
    }

    public long incrementActualAlarmStormNumber(final AlarmTestingInfo alarmTestingInfo) {
        this.stormRemoveQueue.offer(alarmTestingInfo);
        return this.stormRemoveQueue.size();
    }

    public AlarmTestingInfo getNextStormAlarmTestingInfo() {
        return this.stormRemoveQueue.poll();
    }

    public long getStormCount() {
        return this.stormRemoveQueue.size();
    }

    public void resetAlarmStormNumber() {
        this.stormRemoveQueue.clear();
    }

    public void notifyForNormalToRemove() {
        try {
            synchronized (normalRemoveLock) {
                this.normalRemoveLock.notifyAll();
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    public void waitForNormalToRemove() {
        try {
            synchronized (normalRemoveLock) {
                this.normalRemoveLock.wait(5000);
            }
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void notifyForPeakToRemove() {
        try {
            synchronized (peakRemoveLock) {
                this.peakRemoveLock.notifyAll();
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    public void waitForPeakToRemove() {
        try {
            synchronized (peakRemoveLock) {
                this.peakRemoveLock.wait(5000);
            }
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void notifyForStormToRemove() {
        try {
            synchronized (stormRemoveLock) {
                this.stormRemoveLock.notifyAll();
            }
        } catch (final Exception ex) {
            ex.printStackTrace();
        }
    }

    public void waitForStormToRemove() {
        try {
            synchronized (stormRemoveLock) {
                this.stormRemoveLock.wait(5000);
            }
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean isStopReadAlarmFlow(final int parallelId) {
        if (parallelId >= readParallelNumber) {
            return true;
        }
        return stopReadAlarmFlow;
    }

    public void setStopReadAlarmFlow(final boolean stopReadAlarmFlow) {
        this.stopReadAlarmFlow = stopReadAlarmFlow;
    }

    public long getReadParallelNumber() {
        return readParallelNumber;
    }

    public void setReadParallelNumber(final long readParallelNumber) {
        this.readParallelNumber = readParallelNumber;
    }

    public long getReadPausePeriod() {
        return readPausePeriod;
    }

    public void setReadPausePeriod(final long readPausePeriod) {
        this.readPausePeriod = readPausePeriod;
    }

    public int getNormalCeaseAlarmPercentage() {
        return normalCeaseAlarmPercentage;
    }

    public void setNormalCeaseAlarmPercentage(final int normalCeaseAlarmPercentage) {
        this.normalCeaseAlarmPercentage = normalCeaseAlarmPercentage;
    }

    public boolean isNormalEnableCeaseAlarmPercentage() {
        return normalEnableCeaseAlarmPercentage;
    }

    public void setNormalEnableCeaseAlarmPercentage(final boolean normalEnableCeaseAlarmPercentage) {
        this.normalEnableCeaseAlarmPercentage = normalEnableCeaseAlarmPercentage;
    }

    public int getPeakCeaseAlarmPercentage() {
        return peakCeaseAlarmPercentage;
    }

    public void setPeakCeaseAlarmPercentage(final int peakCeaseAlarmPercentage) {
        this.peakCeaseAlarmPercentage = peakCeaseAlarmPercentage;
    }

    public boolean isPeakEnableCeaseAlarmPercentage() {
        return peakEnableCeaseAlarmPercentage;
    }

    public void setPeakEnableCeaseAlarmPercentage(final boolean peakEnableCeaseAlarmPercentage) {
        this.peakEnableCeaseAlarmPercentage = peakEnableCeaseAlarmPercentage;
    }

    public int getStormCeaseAlarmPercentage() {
        return stormCeaseAlarmPercentage;
    }

    public void setStormCeaseAlarmPercentage(final int stormCeaseAlarmPercentage) {
        this.stormCeaseAlarmPercentage = stormCeaseAlarmPercentage;
    }

    public boolean isStormEnableCeaseAlarmPercentage() {
        return stormEnableCeaseAlarmPercentage;
    }

    public void setStormEnableCeaseAlarmPercentage(final boolean stormEnableCeaseAlarmPercentage) {
        this.stormEnableCeaseAlarmPercentage = stormEnableCeaseAlarmPercentage;
    }

    public void setThresholdAlarmNormalPercentage(final int thresholdAlarmNormalPercentage) {
        this.thresholdAlarmNormalPercentage = thresholdAlarmNormalPercentage;
    }

    public int getThresholdAlarmPeakPercentage() {
        return thresholdAlarmPeakPercentage;
    }

    public void setThresholdAlarmPeakPercentage(final int thresholdAlarmPeakPercentage) {
        this.thresholdAlarmPeakPercentage = thresholdAlarmPeakPercentage;
    }

    public int getThresholdAlarmStormPercentage() {
        return thresholdAlarmStormPercentage;
    }

    public void setThresholdAlarmStormPercentage(final int thresholdAlarmStormPercentage) {
        this.thresholdAlarmStormPercentage = thresholdAlarmStormPercentage;
    }

    public int getParallelAlarmNormalNumber() {
        return parallelAlarmNormalNumber;
    }

    public void setParallelAlarmNormalNumber(final int parallelAlarmNormalNumber) {
        this.parallelAlarmNormalNumber = parallelAlarmNormalNumber;
    }

    public int getParallelAlarmPeakNumber() {
        return parallelAlarmPeakNumber;
    }

    public void setParallelAlarmPeakNumber(final int parallelAlarmPeakNumber) {
        this.parallelAlarmPeakNumber = parallelAlarmPeakNumber;
    }

    public int getParallelAlarmStormNumber() {
        return parallelAlarmStormNumber;
    }

    public void setParallelAlarmStormNumber(final int parallelAlarmStormNumber) {
        this.parallelAlarmStormNumber = parallelAlarmStormNumber;
    }

    public boolean isStopRemoveAllNormalFlow() {
        return stopRemoveAllNormalFlow;
    }

    public void setStopRemoveAllNormalFlow(final boolean stopRemoveAllNormalFlow) {
        this.stopRemoveAllNormalFlow = stopRemoveAllNormalFlow;
    }

    public boolean isStopRemoveAllPeakFlow() {
        return stopRemoveAllPeakFlow;
    }

    public void setStopRemoveAllPeakFlow(final boolean stopRemoveAllPeakFlow) {
        this.stopRemoveAllPeakFlow = stopRemoveAllPeakFlow;
    }

    public boolean isStopRemoveAllStormFlow() {
        return stopRemoveAllStormFlow;
    }

    public void setStopRemoveAllStormFlow(final boolean stopRemoveAllStormFlow) {
        this.stopRemoveAllStormFlow = stopRemoveAllStormFlow;
    }

    public int getPackingAlarmNormalNumber() {
        return packingAlarmNormalNumber;
    }

    public void setPackingAlarmNormalNumber(final int packingAlarmNormalNumber) {
        this.packingAlarmNormalNumber = packingAlarmNormalNumber;
    }

    public int getPackingAlarmPeakNumber() {
        return packingAlarmPeakNumber;
    }

    public void setPackingAlarmPeakNumber(final int packingAlarmPeakNumber) {
        this.packingAlarmPeakNumber = packingAlarmPeakNumber;
    }

    public int getPackingAlarmStormNumber() {
        return packingAlarmStormNumber;
    }

    public void setPackingAlarmStormNumber(final int packingAlarmStormNumber) {
        this.packingAlarmStormNumber = packingAlarmStormNumber;
    }

}
