package com.ericsson.oss.services.fm.alarm.migration.concurrent;

import java.io.File;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.services.fm.alarm.migration.CountUpAndDownLatch;
import com.ericsson.oss.services.fm.alarm.migration.Range;
import com.ericsson.oss.services.fm.alarm.migration.Util;

public class Monitor implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Monitor.class);

    private static final int maxFiles = 20;
    private static final int sleepSeconds = 3;
    private final String[] dirs;
    private final HashSet<String> esIndices;
    private final Deque<Range> failedTimeRanges;
    private final int indexType;
    private final Map<String, ConcurrentLinkedQueue<Range>> exportTimeRanges;
    private final CountUpAndDownLatch exportedTimeRangesCount;
    private final Map<Integer, String> exportThreadIdIndexNameMap;
    private final Map<String, ConcurrentLinkedQueue<String>> importFiles;
    private final Map<Integer, String> importThreadIdIndexNameMap;
    private final AtomicLong toMigrateRecords;
    private final AtomicLong migratedRecords;
    private final BigInteger filesize;
    private final AtomicLong importTime;
    private final AtomicLong exportTime;
    private boolean monitorRunStatus = true;
    private boolean allowExport = true;
    private boolean exporterAlive = true;
    private boolean rangesCreatorAlive = true;
    private int exporterThreadsCount;
    private int rangeCreatorThreadsCount;

    public Monitor(int exportThreadsCount, int rangeCreatorThreadsCount, int indexType, String... dirs) {
        LOGGER.debug("Monitor Thread: Initializing...");
        this.exporterThreadsCount = exportThreadsCount;
        this.rangeCreatorThreadsCount = rangeCreatorThreadsCount;
        this.dirs = dirs;
        esIndices = new HashSet<>();
        failedTimeRanges = new ConcurrentLinkedDeque<>();
        this.indexType = indexType;

        exportTimeRanges = new LinkedHashMap<>();
        exportThreadIdIndexNameMap = new HashMap<>();
        exportedTimeRangesCount = new CountUpAndDownLatch(0);

        importFiles = new LinkedHashMap<>();
        importThreadIdIndexNameMap = new HashMap<>();

        toMigrateRecords = new AtomicLong();
        migratedRecords = new AtomicLong();
        importTime = new AtomicLong();
        exportTime = new AtomicLong();
        filesize = new BigInteger("0");
    }

    public long setToMigrateRecords(long value) {
        return toMigrateRecords.getAndSet(value);
    }

    public long getToMigrateRecords() {
        return toMigrateRecords.get();
    }

    public long addMigratedRecords(long value) {
        return migratedRecords.addAndGet(value);
    }

    public long getMigratedRecords() {
        return migratedRecords.get();
    }

    public BigInteger addFileSize(final long value) {
        synchronized (filesize) {
            return filesize.add(BigInteger.valueOf(value));
        }
    }

    public BigInteger removeFileSize(final long value) {
        synchronized (filesize) {
            return filesize.subtract(BigInteger.valueOf(value));
        }
    }

    public boolean fileDirTooBig() {
        synchronized (filesize) {
            BigInteger maxDirSize = new BigInteger("2147483648");
            return (filesize.compareTo(maxDirSize) > 0);
        }
    }

    public String getFileSize() {
        synchronized (filesize) {
            return filesize.toString();
        }
    }

    public String getMigratedPercentage() {
        Double percentage = ((double) migratedRecords.get() / toMigrateRecords.get()) * 100;
        DecimalFormat df = new DecimalFormat("#.####");
        return df.format(percentage);
    }

    public long addImportTime(long value) {
        return importTime.addAndGet(value);
    }

    public long getImportTime() {
        return importTime.get();
    }

    public long getExportTime() {
        return exportTime.get();
    }

    public long addExportTime(long value) {
        return exportTime.addAndGet(value);
    }

    private void stopMonitor() {
        LOGGER.info("Monitor Thread: Stopping...");
        this.monitorRunStatus = false;
    }

    public boolean canExport() {
        return allowExport;
    }

    public synchronized void exporterExiting() {
        exporterThreadsCount--;
        if (exporterThreadsCount == 0) {
            LOGGER.info("Monitor Thread: Setting exporterAlive = false");
            exporterAlive = false;
            stopMonitor();
            LOGGER.info("Time ranges failed in retry: {}", getFailedTimeRanges().toString());
        }
    }

    public synchronized void rangesCreatorExiting() {
        rangeCreatorThreadsCount--;
        if (rangeCreatorThreadsCount == 0) {
            LOGGER.debug("Monitor Thread: Setting rangesCreatorAlive = false");
            rangesCreatorAlive = false;
        }
    }

    public boolean isExporterAlive() {
        return exporterAlive;
    }

    public boolean isRangesCreatorAlive() {
        return rangesCreatorAlive;
    }

    public boolean isIndexPresent(String indexName) {
        return esIndices.contains(indexName);
    }

    public void addEsIndex(String indexName) {
        esIndices.add(indexName);
    }

    public void addFailedTimeRange(Range timeRange) {
        failedTimeRanges.addLast(timeRange);
    }

    public Deque<Range> getFailedTimeRanges() {
        return failedTimeRanges;
    }

    public synchronized boolean isTimeRangesEmpty() {
        return exportTimeRanges.isEmpty();
    }

    public synchronized void prepareFailedTimeRangesForRetry(int threadId) {
        Range timeRange;
        String esIndexName;
        if (!failedTimeRanges.isEmpty()) {
            LOGGER.info("Monitor Thread: Preparing to retry failed timeRanges ("
                                + failedTimeRanges.size() + "): " + failedTimeRanges);
            while ((timeRange = failedTimeRanges.poll()) != null) {
                final String timeRangeString = timeRange.getTimestamp().replaceAll("\\s+", "");
                esIndexName = Util.getEsIndexName(indexType, timeRangeString.substring(0, timeRangeString.indexOf("TO")));
                enqueueTimeRangeForExporting(esIndexName, timeRange);
            }
            // isRetryPhase = true;
        } else {
            LOGGER.info("failedTimeRanges is empty for thread {}", threadId);
        }
    }

    public synchronized List<String> splitTimeRange(final String rangeString) {
        List<String> timeRanges = new ArrayList<>();
        try {
            final DateFormat dateFormat = Util.getDateFormat();
            final String[] dateArray = rangeString.split("TO");
            final Date minInsertTime = dateFormat.parse(dateArray[0]);
            final Date maxInsertTime = dateFormat.parse(dateArray[1]);
            int diff = (int) (maxInsertTime.getTime() - minInsertTime.getTime()) + 1;
            int i = 0;
            for (i = 0; i < Util.rangeSize.length; i++) {
                if (Util.rangeSize[i] < diff) {
                    break;
                }
            }
            if (i < Util.rangeSize.length) {
                int amount = Util.rangeSize[i];
                long startCal = minInsertTime.getTime();
                long endCal = maxInsertTime.getTime();

                while (startCal <= endCal) {
                    long endCursor = startCal + (amount - 1);
                    if (endCursor > endCal) {
                        endCursor = endCal;
                    }
                    String fromTime = dateFormat.format(new Date(startCal));
                    String toTime = dateFormat.format(new Date(endCursor));
                    String timeRange = fromTime + "Z TO " + toTime + "Z";
                    timeRanges.add(timeRange);
                    startCal = endCursor + 1;
                }
            }
        } catch (Exception e) {
            LOGGER.error("!!! splitTimeRange {} exception {}", rangeString, e);
        }
        return timeRanges;
    }

    @Override
    public void run() {
        LOGGER.debug("Monitor Thread: Started");
        boolean flag;
        while (monitorRunStatus == true) {
            flag = true;
            if (fileDirTooBig()) {
                LOGGER.debug("Monitor Thread: Exported directory size {}. Changing the ExportStatus to FALSE.", getFileSize());
                flag = false;
            }
            //            for (int i = 0; i < dirs.length; i++) {
            //                int numFiles = new File(dirs[i]).list().length;
            //                LOGGER.debug("Monitor Thread: Number of files in {}: {}", dirs[i], numFiles);
            //                if (numFiles > maxFiles) {
            //                    LOGGER.debug("Monitor Thread: Number of files in {} > {} . Changing the ExportStatus to FALSE.", dirs[i], maxFiles);
            //                    flag = false;
            //                    break;
            //                }
            //            }

            if (allowExport == false && flag == true) {
                LOGGER.debug("Monitor Thread: Changing the ExportStatus to TRUE");
            }
            allowExport = flag;

            try {
                Thread.sleep(sleepSeconds * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void enqueueTimeRangeForExporting(String esIndexName, Range timeRange) {
        LOGGER.debug("Enqueue {} {}", esIndexName, timeRange);
        // set(esIndexName, timeRange, exportTimeRanges);
        set(timeRange.getTimestamp(), timeRange, exportTimeRanges);
        exportedTimeRangesCount.countUp();
    }

    public void enqueueEsFileForImporting(String esIndexName, String esDataFile) {
        LOGGER.debug("Enqueue {} {}", esIndexName, esDataFile);
        set(esIndexName, esDataFile, importFiles);
    }

    private <T> void set(String esIndexName, T esDataFile,
                         Map<String, ConcurrentLinkedQueue<T>> indexNameQueueMap) {
        synchronized (indexNameQueueMap) {
            ConcurrentLinkedQueue<T> queue = indexNameQueueMap.get(esIndexName);
            if (queue == null) {
                queue = new ConcurrentLinkedQueue<>();
                indexNameQueueMap.put(esIndexName, queue);
            }
            queue.offer(esDataFile);
            //			System.out.println("indexNameQueueMap: " + indexNameQueueMap);
        }
    }

    public Range getTimeRangeForExporting(int exportThreadId) {
        return get(exportThreadId, exportTimeRanges, exportThreadIdIndexNameMap, "Export");
    }

    public String getEsFileForImporting(int importThreadId) {
        return get(importThreadId, importFiles, importThreadIdIndexNameMap, "Import");
    }

    /*
     * This implementation is to make sure that only one thread loads data intojava
     * one index at a time to avoid index corruption issue in ES.
     */
    private <T> T get(int threadId,
                      Map<String, ConcurrentLinkedQueue<T>> indexNameQueueMap,
                      Map<Integer, String> threadIdIndexNameMap, String type) {

        T element = null;
        synchronized (indexNameQueueMap) {
            String indexName = threadIdIndexNameMap.get(threadId);
            if (indexName == null) {
                LOGGER.debug("{} Thread [{}]: is not assigned with an index.", type, threadId);
                Iterator<String> indexNames = indexNameQueueMap.keySet().iterator();
                String str;
                while (indexNames.hasNext()) {
                    str = indexNames.next();
                    if (!threadIdIndexNameMap.containsValue(str)) {
                        indexName = str;
                        LOGGER.debug("{} Thread [{}]: Assigned index {} with queue size {}", type, threadId, indexName,
                                     indexNameQueueMap.get(indexName).size());
                        if (indexNameQueueMap.get(indexName).size() > 200) {
                            LOGGER.info("Problem in queue? : {}", indexNameQueueMap.get(indexName).toString());
                        }
                        threadIdIndexNameMap.put(threadId, indexName);
                        break;
                    }
                }
            }
            if (indexName == null) {
                LOGGER.debug("{} Thread [{}]: No free index found for thread. Current threadIdIndexNameMap: {} ", type, threadId,
                             threadIdIndexNameMap.toString());
            } else {
                ConcurrentLinkedQueue<T> queue = indexNameQueueMap.get(indexName);
                element = queue.poll();
                LOGGER.debug("{} queue.poll() returned fileName {}", type, element);
                //If queue is empty after polling...
                if (queue.isEmpty()) {
                    LOGGER.debug("{} Thread [{}}]: Queue is empty for indexName {}", type, threadId, indexName);
                    //Remove the empty queue from importFiles map
                    indexNameQueueMap.remove(indexName);
                    //Also remove the importThreadId-IndexName Mapping
                    threadIdIndexNameMap.remove(threadId);
                }
            }
        }
        return element;
    }

    public Boolean waitForImportCompletion(String sDir, int threadId) {
        while (!importFiles.isEmpty()) {
            LOGGER.info("Thread [{}] Waiting for importFiles ({}) to become empty", threadId, importFiles.size());
            if (!failedTimeRanges.isEmpty() || !exportTimeRanges.isEmpty()) {
                return false;
            }
            Util.sleep(3);
        }
        File dir = new File(sDir);
        while (dir.list() != null && dir.list().length > 0) {
            LOGGER.info("Waiting for {} to become empty", sDir);
            if (!failedTimeRanges.isEmpty() || !exportTimeRanges.isEmpty()) {
                return false;
            }
            Util.sleep(3);
        }
        return true;
    }
}
