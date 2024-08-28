package com.ericsson.oss.services.fm.alarm.migration.solr;

import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.util.Date;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.services.fm.alarm.migration.Range;
import com.ericsson.oss.services.fm.alarm.migration.SolrCoreStats;
import com.ericsson.oss.services.fm.alarm.migration.Util;
import com.ericsson.oss.services.fm.alarm.migration.concurrent.Monitor;

public class RangesCreator implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(RangesCreator.class);
    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    private static final String solrCore = "collection1";
    private final SolrManager solrManager;
    private final int numOfRecsToExportPerIteration;
    private final Monitor monitor;
    private final int indexType;
    private Date minInsertTime;
    private Date maxInsertTime;

    public RangesCreator(String solrHost, int indexType, int numOfRecsToExportPerIteration, Monitor monitor) {
        this.numOfRecsToExportPerIteration = numOfRecsToExportPerIteration;
        this.monitor = monitor;
        this.indexType = indexType;
        solrManager = new SolrManager(solrCore, solrHost);
    }

    public RangesCreator(String solrHost, int indexType, int numOfRecsToExportPerIteration, Monitor monitor, Date minInsertTime, Date maxInsertTime) {
        this(solrHost, indexType, numOfRecsToExportPerIteration, monitor);
        this.minInsertTime = minInsertTime;
        this.maxInsertTime = maxInsertTime;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("RangesCreator");
        long start = System.currentTimeMillis();
        try {
            prepareTimeRanges(monitor);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("PrepareTimeRanges exception {}", e);
        }
        LOGGER.info("Range creation took {} seconds", ((System.currentTimeMillis() - start) / 1000));
        monitor.rangesCreatorExiting();
    }

    public void prepareTimeRanges(Monitor monitor) {
        LOGGER.info("Getting {} statistics", solrCore);
        final long totalNumOfRecords = 0;
        if (minInsertTime == null && maxInsertTime == null) {
            SolrCoreStats stats = solrManager.getStats();
            minInsertTime = stats.minInsertTime;
            maxInsertTime = stats.maxInsertTime;
        }
        if (minInsertTime != null && maxInsertTime != null) {
            prepareTimeRanges(indexType, monitor, minInsertTime, maxInsertTime, totalNumOfRecords, Util.rangeSize);
        } else {
            LOGGER.error("PrepareTimeRanges invalid minInsertTime OR maxInsertTime {} {}", minInsertTime, maxInsertTime);
            throw new InvalidParameterException("Null minInsertTime OR maxInsertTime");
        }
    }

    public void prepareTimeRanges(int indexType, Monitor monitor, Date minInsertTime, Date maxInsertTime, long totalNumOfRecords,
                                  int[] rangeSize) {
        final DateFormat dateFormat = Util.getDateFormat();

        long startCal = minInsertTime.getTime();
        long endCal = maxInsertTime.getTime();

        if (totalNumOfRecords == 0) {
            // String totalTimeRange = dateFormat.format(startCal.getTime()) + "Z TO " + dateFormat.format(endCal.getTime()) + "Z";
            String totalTimeRange = dateFormat.format(minInsertTime.getTime()) + "Z TO " + dateFormat.format(maxInsertTime.getTime()) + "Z";
            totalNumOfRecords = solrManager.getNumOfRecordsInTimeRange(totalTimeRange);
        }
        LOGGER.info("Total number of records to migrate: {} , Min insertTime: {}, Max insertTime: {}", totalNumOfRecords,
                    dateFormat.format(minInsertTime.getTime()), dateFormat.format(maxInsertTime.getTime()));
        monitor.setToMigrateRecords(totalNumOfRecords);

        String esIndexName;
        long numOfRecordTotal = 0;
        while (startCal <= endCal) {
            int i = 0;
            int amount = rangeSize[i];
            long endRange = startCal + (amount - 1);
            LOGGER.debug(" --- {} {}", amount, dateFormat.format(new Date(startCal)));
            while (startCal <= endRange) {
                long endCursor = startCal + (amount - 1);
                String fromTime = dateFormat.format(new Date(startCal));
                String toTime = dateFormat.format(new Date(endCursor));
                String timeRange = fromTime + "Z TO " + toTime + "Z";
                long numOfRecs = solrManager.getNumOfRecordsInTimeRange(timeRange);
                if (numOfRecs > numOfRecsToExportPerIteration && i < rangeSize.length - 1) {
                    LOGGER.debug("!! {} Num of numOfRecs {} in {}", amount, numOfRecs, timeRange);
                    i = i + 1;
                    amount = rangeSize[i];
                } else {
                    // ADD to enqueueTimeRangeForExporting
                    if (numOfRecs > 0) {
                        LOGGER.info("{} Num of numOfRecs {} in {}", amount, numOfRecs, timeRange);
                        esIndexName = Util.getEsIndexName(indexType, fromTime + "Z");
                        monitor.enqueueTimeRangeForExporting(esIndexName, new Range(timeRange, numOfRecs));
                    } else {
                        LOGGER.debug("## {} NO record {} in {}", amount, numOfRecs, timeRange);
                    }
                    numOfRecordTotal = numOfRecordTotal + numOfRecs;
                    LOGGER.debug("## Previous Start {} add {}", dateFormat.format(new Date(startCal)), amount);
                    startCal = startCal + amount;
                    String nextStart = dateFormat.format(new Date(startCal));
                    LOGGER.debug("## Next Start {}", nextStart);
                    break;
                }
            }
        }
        if (totalNumOfRecords != numOfRecordTotal) {
            LOGGER.error("Num Of Record mismatch: totalNumOfRecords {} calculatedNumOfRecord {}", totalNumOfRecords, numOfRecordTotal);
        } else {
            LOGGER.info("Calculated NumOfRecord {}", numOfRecordTotal);
        }
    }
}
