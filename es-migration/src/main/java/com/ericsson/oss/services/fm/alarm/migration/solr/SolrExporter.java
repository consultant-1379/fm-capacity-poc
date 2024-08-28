package com.ericsson.oss.services.fm.alarm.migration.solr;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.services.fm.alarm.migration.Range;
import com.ericsson.oss.services.fm.alarm.migration.Util;
import com.ericsson.oss.services.fm.alarm.migration.concurrent.Monitor;

public class SolrExporter implements Runnable {
    protected static final int OUTPUT_BUFFER_SIZE = 4096;
    private static final Logger LOGGER = LoggerFactory.getLogger(SolrExporter.class);
    private final int id;
    private final String exportTempDir;
    private final int numOfRecsToExportPerIteration;
    private final SolrManager solrManager;
    private final Monitor monitor;

    public SolrExporter(int id, String solrHost, String solrCore, String exportTempDir,
                        int numOfRecsToExportPerIteration, Monitor monitor) {
        this.id = id;
        this.exportTempDir = exportTempDir;
        this.numOfRecsToExportPerIteration = numOfRecsToExportPerIteration;
        this.monitor = monitor;
        solrManager = new SolrManager(solrCore, solrHost);
    }

    @Override
    public void run() {
        Thread.currentThread().setName("SolrExporter#" + id);
        Range timeRange = null;
        long numOfRecordsInTimeRange;
        int pageNo;
        int startIndex;
        String solrDataFileNamePrefix;
        do {
            String timeRangeString = null;
            String idString = null;
            long start = System.currentTimeMillis();
            try {
                timeRange = monitor.getTimeRangeForExporting(id);
                if (timeRange == null) {
                    if (monitor.isRangesCreatorAlive()) {
                        LOGGER.debug("Waiting for {}  seconds for time ranges...", Util.RETRY_SLEEP_SECONDS);
                        Util.sleep(Util.RETRY_SLEEP_SECONDS);
                        continue;
                    }
                    LOGGER.info("No more time range elements for this thread.");
                    final Boolean complete = monitor.waitForImportCompletion(exportTempDir, id);
                    monitor.prepareFailedTimeRangesForRetry(id);
                    if (monitor.isTimeRangesEmpty() && complete) {
                        monitor.exporterExiting();
                        break;
                    }
                    continue;
                }

                pageNo = 1;
                startIndex = 0;
                numOfRecordsInTimeRange = getNumOfRecordsInTimeRange(timeRange);
                if (numOfRecordsInTimeRange == 0) {
                    LOGGER.debug("No records found for time range {}", timeRange);
                    continue;
                } else {
                    LOGGER.debug("Going to migrate {} records of time range {} tot {}",
                                 numOfRecordsInTimeRange,
                                 timeRange,
                                 monitor.getToMigrateRecords());
                }
                //e.g timeRange: "2020-01-19T06:00:00.000Z TO 2020-01-19T06:59:59.999Z"
                solrDataFileNamePrefix = exportTempDir + "/solrData_" + timeRange.getTimestamp().replaceAll("\\s+", "");
                //With in hour time range, pages loop
                if (timeRange.getId() != null) {
                    idString = timeRange.getId();
                    checkAndWait();
                    LOGGER.info("Get id {} from timeRange {}", timeRange.getId(), timeRange.getTimestamp());
                    final String solrFile = solrDataFileNamePrefix + "_" + timeRange.getId() + ".json";
                    solrManager.exportToFile(solrFile, timeRange.getId());
                    enqueueEsFileForImporting(solrFile);
                } else {
                    timeRangeString = timeRange.getTimestamp().replaceAll("\\s+", "");
                    while (startIndex < numOfRecordsInTimeRange) {
                        checkAndWait();
                        LOGGER.debug("Iteration: {} . Exporting {} records from solr...", pageNo, numOfRecsToExportPerIteration);
                        final String solrFile = solrDataFileNamePrefix + "_" + pageNo + ".json";
                        long filesize = solrManager.exportToFile(solrFile, startIndex, numOfRecsToExportPerIteration, timeRange.getTimestamp());
                        enqueueEsFileForImporting(solrFile);
                        monitor.addFileSize(filesize);
                        startIndex = startIndex + numOfRecsToExportPerIteration;
                        pageNo++;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                LOGGER.error("Solr Exported {} exception {}", timeRange, ex.getCause());
                if (timeRangeString != null) {
                    final List<String> timeRanges = monitor.splitTimeRange(timeRangeString);
                    timeRanges.forEach(range -> {
                        LOGGER.info("##### Retry with {}", range);
                        monitor.addFailedTimeRange(new Range(range));
                    });
                    if (timeRanges.isEmpty()) {
                        // TODO Procedure Failed
                        LOGGER.error("!!! Range {} NOT EXPORTED AT ALL", timeRangeString);
                    }
                }
                if (idString != null) {
                    LOGGER.error("!!! Id {} NOT EXPORTED AT ALL", timeRangeString);
                }
            } finally {
                if (timeRange != null) {
                    long delay = (System.currentTimeMillis() - start) / 1000;
                    monitor.addExportTime(delay);
                    LOGGER.info("Export took {} seconds OF {} seconds ", delay, monitor.getExportTime());
                }
            }
        } while (true);
    }

    private void enqueueEsFileForImporting(String file) {
        String esDataFile = file.substring(file.lastIndexOf(File.separator) + 1);
        int i = esDataFile.indexOf("_");
        i = esDataFile.indexOf("_", i + 1);
        i = esDataFile.indexOf("_", i + 1);
        String esIndexName = esDataFile.substring(i + 1, esDataFile.indexOf(".json"));
        monitor.enqueueEsFileForImporting(esIndexName, esDataFile);
    }

    private void checkAndWait() {
        while (!monitor.canExport()) {
            LOGGER.info("Waiting for already exported file to get consumed. {}", monitor.getFileSize());
            try {
                Thread.sleep(3000);
                monitor.addExportTime(-3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //Get the number of records available in that time range
    private long getNumOfRecordsInTimeRange(Range timeRange) throws SolrManagerException {
        if (timeRange.getId() != null) {
            return 1L;
        }
        if (timeRange.getNumFound() != null) {
            return timeRange.getNumFound();
        }
        return solrManager.getNumOfRecordsInTimeRange(timeRange.getTimestamp());
    }
}
