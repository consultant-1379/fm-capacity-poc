package com.ericsson.oss.services.fm.alarm.migration.concurrent;

import java.io.File;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.services.fm.alarm.migration.Util;

public class Monitor implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Monitor.class);

    private static final int maxFiles = 10;
    private static final int sleepSeconds = 3;
    private final String[] dirs;
    private final HashSet<String> esIndices;
    private final Deque<String> failedTimeRanges;
    private final int indexType;
    private final Map<String, ConcurrentLinkedQueue<String>> exportTimeRanges;
    private final Map<Integer, String> exportThreadIdIndexNameMap;
    private final Map<Integer, String> convertThreadIdIndexNameMap;
    private final Map<String, ConcurrentLinkedQueue<String>> importFiles;
    private final Map<String, ConcurrentLinkedQueue<String>> convertFiles;
    private final Map<Integer, String> importThreadIdIndexNameMap;
    public boolean isRetryPhase;
    private boolean monitorRunStatus = true;
    private boolean allowExport = true;
    private boolean exporterAlive = true;
    private boolean converterAlive = true;
    private int exporterThreadsCount;
    private int converterThreadsCount;

    public Monitor(int exportThreadsCount, int converterThreadsCount, int indexType, String... dirs) {
        LOGGER.debug("Monitor Thread: Initializing...");
        this.exporterThreadsCount = exportThreadsCount;
        this.converterThreadsCount = converterThreadsCount;
        this.dirs = dirs;
        esIndices = new HashSet<String>();
        failedTimeRanges = new ConcurrentLinkedDeque<String>();
        this.indexType = indexType;

        exportTimeRanges = new LinkedHashMap<String, ConcurrentLinkedQueue<String>>();
        exportThreadIdIndexNameMap = new HashMap<Integer, String>();

        convertFiles = new LinkedHashMap<String, ConcurrentLinkedQueue<String>>();
        convertThreadIdIndexNameMap = new HashMap<Integer, String>();

        importFiles = new LinkedHashMap<String, ConcurrentLinkedQueue<String>>();
        importThreadIdIndexNameMap = new HashMap<Integer, String>();

        this.isRetryPhase = false;
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
            LOGGER.debug("Time ranges failed in retry: " + getFailedTimeRanges().toString());
        }
    }

    public synchronized void converterExiting() {
        converterThreadsCount--;
        if (converterThreadsCount == 0) {
            LOGGER.debug("Monitor Thread: Setting converterAlive = false");
            converterAlive = false;
        }
    }

    public boolean isExporterAlive() {
        return exporterAlive;
    }

    public boolean isConverterAlive() {
        return converterAlive;
    }

    public boolean isIndexPresent(String indexName) {
        return esIndices.contains(indexName);
    }

    public void addEsIndex(String indexName) {
        esIndices.add(indexName);
    }

    public void addFailedTimeRange(String timeRange) {
        failedTimeRanges.addLast(timeRange);
    }

    public Deque<String> getFailedTimeRanges() {
        return failedTimeRanges;
    }

    public synchronized void prepareFailedTimeRangesForRetry(int threadId) {
        String timeRange;
        String esIndexName;
        if (!failedTimeRanges.isEmpty()) {
            LOGGER.debug("Monitor Thread: Preparing to retry failed timeRanges ("
                                       + failedTimeRanges.size() + "): " + failedTimeRanges);
            while ((timeRange = failedTimeRanges.poll()) != null) {
                esIndexName = Util.getEsIndexName(indexType, timeRange.substring(0, timeRange.indexOf(" TO ")));
                enqueueTimeRangeForExporting(esIndexName, timeRange);
            }
            isRetryPhase = true;
        } else {
            LOGGER.error("failedTimeRanges is empty for thread " + threadId);
        }
    }

    @Override
    public void run() {
        LOGGER.debug("Monitor Thread: Started");
        boolean flag;
        while (monitorRunStatus == true) {
            flag = true;
            for (int i = 0; i < dirs.length; i++) {
                int numFiles = new File(dirs[i]).list().length;
                LOGGER.debug("Monitor Thread: Number of files in " + dirs[i] + ": " + numFiles);
                if (numFiles > maxFiles) {
                    LOGGER.debug("Monitor Thread: Number of files in " + dirs[i] + " > " + maxFiles
                                               + ". Changing the ExportStatus to FALSE.");
                    flag = false;
                    break;
                }
            }

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

    public void enqueueTimeRangeForExporting(String esIndexName, String timeRange) {
        set(esIndexName, timeRange, exportTimeRanges);
    }

    public void enqueueEsFileForImporting(String esIndexName, String esDataFile) {
        LOGGER.debug("Enqueue {} {}", esIndexName, esDataFile);
        set(esIndexName, esDataFile, importFiles);
    }

    public void enqueueEsFileForConverting(String solrIndexName, String solrDataFile) {
        LOGGER.debug("Enqueue {} {}", solrIndexName, solrDataFile);
        set(solrIndexName, solrDataFile, convertFiles);
    }

    private void set(String esIndexName, String esDataFile,
                     Map<String, ConcurrentLinkedQueue<String>> indexNameQueueMap) {
        synchronized (indexNameQueueMap) {
            ConcurrentLinkedQueue<String> queue = indexNameQueueMap.get(esIndexName);
            if (queue == null) {
                queue = new ConcurrentLinkedQueue<String>();
                indexNameQueueMap.put(esIndexName, queue);
            }
            queue.offer(esDataFile);
            //			System.out.println("indexNameQueueMap: " + indexNameQueueMap);
        }
    }

    public String getTimeRangeForExporting(int exportThreadId) {
        return get(exportThreadId, exportTimeRanges, exportThreadIdIndexNameMap, "Export");
    }

    public String getSolrFileForConverting(int convertThreadId) {
        return get(convertThreadId, convertFiles, convertThreadIdIndexNameMap, "Convert");
    }

    public String getEsFileForImporting(int importThreadId) {
        return get(importThreadId, importFiles, importThreadIdIndexNameMap, "Import");
    }

    /*
     * This implementation is to make sure that only one thread loads data into
     * one index at a time to avoid index corruption issue in ES.
     */
    private String get(int threadId,
                       Map<String, ConcurrentLinkedQueue<String>> indexNameQueueMap,
                       Map<Integer, String> threadIdIndexNameMap, String type) {

        String element = null;
        synchronized (indexNameQueueMap) {
            String indexName = threadIdIndexNameMap.get(threadId);
            if (indexName == null) {
                LOGGER.debug(type + "Thread [" + threadId + "]: is not assigned with an index.");
                Iterator<String> indexNames = indexNameQueueMap.keySet().iterator();
                String str;
                while (indexNames.hasNext()) {
                    str = indexNames.next();
                    if (!threadIdIndexNameMap.containsValue(str)) {
                        indexName = str;
                        LOGGER.debug(type + "Thread [" + threadId + "]: Assigned index "
                                                   + indexName + " with queue size " + indexNameQueueMap.get(indexName).size());
                        if (indexNameQueueMap.get(indexName).size() > 24) {
                            LOGGER.info("Problem in queue? : " + indexNameQueueMap.get(indexName).toString());
                        }
                        threadIdIndexNameMap.put(threadId, indexName);
                        break;
                    }
                }
            }

            if (indexName == null) {
                LOGGER.debug(type + "Thread [" + threadId + "]: No free index found for thread " + threadId
                                           + ". Current threadIdIndexNameMap: " + threadIdIndexNameMap.toString());
            } else {
                ConcurrentLinkedQueue<String> queue = indexNameQueueMap.get(indexName);
                element = queue.poll();
                LOGGER.debug(type + " queue.poll() returned fileName " + element);
                //If queue is empty after polling...
                if (queue.isEmpty()) {
                    LOGGER.debug(type + "Thread [" + threadId + "]: Queue is empty for indexName " + indexName);
                    //Remove the empty queue from importFiles map
                    indexNameQueueMap.remove(indexName);
                    //Also remove the importThreadId-IndexName Mapping
                    threadIdIndexNameMap.remove(threadId);
                }
            }
        }
        return element;
    }

    public void waitForExportCompletion(String sDir, int threadId) {
        while (!exportTimeRanges.isEmpty()) {
            LOGGER.debug("Thread [" + threadId + "] Waiting for exportTimeRanges ("
                                       + exportTimeRanges.size() + ") to become empty");
            Util.sleep(3);
        }
        File dir = new File(sDir);
        while (dir.list().length > 0) {
            LOGGER.info("Waiting for " + sDir + " to become empty");
            Util.sleep(3);
        }
    }
}
