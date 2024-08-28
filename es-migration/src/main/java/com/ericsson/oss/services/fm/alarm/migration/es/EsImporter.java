package com.ericsson.oss.services.fm.alarm.migration.es;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.services.fm.alarm.migration.Range;
import com.ericsson.oss.services.fm.alarm.migration.Util;
import com.ericsson.oss.services.fm.alarm.migration.concurrent.Monitor;

public class EsImporter implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsImporter.class);

    private static String exportTempDir;
    private final EsManager esManager;
    private final int id;
    private final Monitor monitor;

    public EsImporter(int id, String exportTempDir, String esHost, Monitor monitor) {
        this.id = id;
        this.monitor = monitor;
        EsImporter.exportTempDir = exportTempDir;
        this.esManager = new EsManager(esHost);
    }

    @Override
    public void run() {
        Thread.currentThread().setName("EsImporter#" + id);
        do {
            String esDataFile = null;
            String esDataFileFullPath = null;
            long start = System.currentTimeMillis();
            try {
                LOGGER.debug("Getting ES input file to import");
                esDataFile = monitor.getEsFileForImporting(id);
                if (esDataFile == null) {
                    if (monitor.isExporterAlive()) {
                        LOGGER.debug("Waiting for {} seconds for ES input files... ", Util.RETRY_SLEEP_SECONDS);
                        Util.sleep(Util.RETRY_SLEEP_SECONDS);
                        continue;
                    }
                    LOGGER.info("No more files to import and Exporter threads are not alive. Exiting Importer thread [{}]", id);
                    return;
                }
                esDataFileFullPath = exportTempDir + "/" + esDataFile;
                final String esIndexName = checkAndCreateIndex(esDataFile);
                if (esIndexName != null) {
                    LOGGER.debug("Picked {} ...took {} milliseconds", esDataFile, ((System.currentTimeMillis() - start)));
                    bulkAdd(esIndexName, exportTempDir + "/" + esDataFile);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                LOGGER.error("Import failed for file {} Exception {}", esDataFile, ex.getCause());
                if (esDataFile != null) {
                    final String timeRangeString = Util.fromFileNameToRange(esDataFile);
                    final List<String> timeRanges = monitor.splitTimeRange(timeRangeString);
                    timeRanges.forEach(timeRange -> {
                        LOGGER.info("##### Retry with {}", timeRange);
                        monitor.addFailedTimeRange(new Range(timeRange));
                    });
                    if (timeRanges.isEmpty()) {
                        if (lastRecovery(esDataFileFullPath, timeRangeString) == null) {
                            if (!Util.createFailureFile(esDataFileFullPath, esDataFile)) {
                                // TODO Procedure Failed
                                LOGGER.error("!!! Range {} NOT IMPORTED AT ALL", Util.fromFileNameToRange(esDataFile));
                            }
                        }
                    }
                }
            } finally {
                if (esDataFileFullPath != null) {
                    File file = new File(esDataFileFullPath);
                    long filesize = file.length();
                    file.delete();
                    monitor.removeFileSize(filesize);
                }
                if (esDataFile != null) {
                    long delay = (System.currentTimeMillis() - start) / 1000;
                    monitor.addImportTime(delay);
                    LOGGER.info("Import took {} seconds OF {} seconds ", delay, monitor.getImportTime());
                    LOGGER.info("Imported {} records OF {} --> {}%", monitor.getMigratedRecords(), monitor.getToMigrateRecords(),
                                monitor.getMigratedPercentage());
                }
            }
        } while (true);
    }

    private Long lastRecovery(String esDataFileFullPath, final String timeRangeString) {
        long totalNumOfRecords = 0;

        String startPatternId = "\"id\":\"";
        String endPatternId = "\"}";
        String startPatternNumFound = "\"numFound\":";
        String endPatternNumFound = ",\"start\"";

        try {
            FileReader fr = new FileReader(esDataFileFullPath);
            BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("numFound")) {
                    String numFound = StringUtils.substringBetween(line, startPatternNumFound, endPatternNumFound);
                    totalNumOfRecords += Long.valueOf(numFound);
                } else if (line.contains(startPatternId)) {
                    String id = StringUtils.substringBetween(line, startPatternId, endPatternId);
                    monitor.addFailedTimeRange(new Range(timeRangeString, id));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return totalNumOfRecords;
    }

    private String checkAndCreateIndex(String file) throws EsManagerException {
        String[] parts = file.split("_");
        String dateTimeStr = parts[1];
        String esIndexName = Util.getEsIndexName(Util.DAILY_INDICES, dateTimeStr);
        //Check if the index is already existing and create if not existing
        synchronized (monitor) {
            if (!monitor.isIndexPresent(esIndexName)) {
                esManager.checkAndCreateIndex(esIndexName);
                LOGGER.debug("Creating ES index: {}", esIndexName);
                monitor.addEsIndex(esIndexName);
            }
        }
        return esIndexName;
    }

    private void bulkAdd(String esIndexName, String file) {
        List jsonDocs;
        try {
            jsonDocs = parse(file);
        } catch (Exception e) {
            LOGGER.error("BULK ADD has {} parse failure {}", esIndexName, e);
            throw new EsBulkAddException("Adding" + esIndexName + " parse failure ", e);
        }
        LOGGER.info("BULK ADD to {} {} records", esIndexName, jsonDocs.size());
        List<String> failedItems = esManager.bulkAdd(esIndexName, jsonDocs);
        monitor.addMigratedRecords(jsonDocs.size() - failedItems.size());
        if (!failedItems.isEmpty()) {
            LOGGER.error("BULK ADD has {} Some failed items {}", failedItems);
            throw new EsBulkAddException("Adding " + esIndexName + " Some failed items " + failedItems.toString());
        }
    }

    List parse(String file) throws IOException, ParseException {
        ContainerFactory containerFactory = new ContainerFactory() {
            public List creatArrayContainer() {
                return new LinkedList();
            }

            public Map createObjectContainer() {
                return new LinkedHashMap();
            }
        };
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(file), containerFactory);
        Map root = (Map) obj;
        Map jsonResponse = (Map) root.get("response");
        return (List) jsonResponse.get("docs");
    }

}
