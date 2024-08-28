package com.ericsson.oss.services.fm.alarm.migration.concurrent;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.services.fm.alarm.migration.Util;

public class Converter implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Converter.class);
    private static String exportTempDir;
    private final int id;
    private final String convertedTempDir;
    private final Monitor monitor;
    private final int indexType;
    // private final int sleepTime;

    public Converter(int id, String exportTempDir, String convertedTempDir,
                     Monitor monitor, int indexType) {
        this.id = id;
        Converter.exportTempDir = exportTempDir;
        this.convertedTempDir = convertedTempDir;
        this.monitor = monitor;
        this.indexType = indexType;
        // this.sleepTime = sleepTime;
    }

    //    private static synchronized String getSOLRFile() {
    //
    //        File dir = new File(Converter.exportTempDir);
    //        String[] exportedFiles = dir.list(new ExportedFileNameFilter());
    //        File file;
    //        String absoluteFileName;
    //        if (exportedFiles.length > 0) {
    //            absoluteFileName = Converter.exportTempDir + File.separator + exportedFiles[0];
    //            file = new File(absoluteFileName);
    //            file.renameTo(new File(absoluteFileName + Util.CONVERTING_EXT));
    //            return absoluteFileName + Util.CONVERTING_EXT;
    //        }
    //
    //        return null;
    //    }

    private String getSOLRFile() {
        final String solrFile = monitor.getSolrFileForConverting(id);
        if (solrFile != null) {

            return Converter.exportTempDir + File.separator + solrFile;
        }
        return null;
    }

    @Override
    public void run() {
        try {
            do {
                long t = System.currentTimeMillis();
                LOGGER.info("Getting SOLR exported file to convert");
                // String solrFile = getSOLRFile();
                final String solrFile = getSOLRFile();
                if (solrFile == null) {
                    if (monitor.isExporterAlive()) {
                        LOGGER.debug("Waiting for " + Util.RETRY_SLEEP_SECONDS + " seconds for SOLR exported files...");
                        Util.sleep(Util.RETRY_SLEEP_SECONDS);
                        continue;
                    }
                    LOGGER.debug("SOLR exported file is not found to convert and Exporter threads are not alive."
                                         + " Exiting Converter thread [" + id + "]");
                    monitor.converterExiting();
                    break;
                }
                LOGGER.debug("Picked " + solrFile + " ...took " + ((System.currentTimeMillis() - t) + " milliseconds"));

                String[] parts = solrFile.split("_");
                String dateTimeStr = parts[1];
                String pageNo = parts[2].substring(0, parts[2].indexOf(".json"));
                String esIndexName = Util.getEsIndexName(indexType, dateTimeStr);
                String esDataFile = convertedTempDir + File.separator + "esData_" +
                        dateTimeStr + "_" + pageNo + "_" + esIndexName + ".json";

                //Migrate into ES format
                //cat solrData.json | jq -c '.response["docs"] | .[] | {"index": {"_index": "fm_history", "_type": "json"}}, .' | curl -H
                // "Content-Type: application/json" -XPOST localhost:9200/_bulk --data-binary @-
                LOGGER.info("Converting into ES format...");

                String[] cmd = { "/usr/bin/jq", "-c",
                        ".response[\"docs\"] | .[] | {\"index\": {\"_index\": \"" + esIndexName + "\", \"_type\": \"doc\"}}, .",
                        solrFile };
                LOGGER.debug(" with cmd: " + String.join(" ", cmd) + " > " + esDataFile + " : ");

                //Trying to avoid below error during conversion by waiting for 2 seconds
                //ERROR: parse error: Unfinished string at EOF at line 649432, column 25
                //				if (!monitor.isRetryPhase) {
                //					Util.sleep(sleepTime);
                //				} else {
                //					Util.sleep(sleepTime + 5);
                //				}

                int exitVal = Util.executeAndExport(cmd, esDataFile, false, true);

                if (exitVal == 0) {
                    LOGGER.debug("Conversion success: " + esDataFile);
                    monitor.enqueueEsFileForImporting(esIndexName, esDataFile);
                    new File(solrFile).delete();
                } else {
                    LOGGER.error("Conversion failed for file " + solrFile);
                    //						+ "Export the file manually for the time range and see if the file is proper.");
                    //Extract timeRange from file name and add to monitor.addFailedTimeRange() to retry export from Exporter threads.
                    addTimeRangeToFailedQueue(solrFile);
                    //Delete the 0 size output file on failure

                    new File(esDataFile).delete();
                    String target = "/tmp/migration/failure/"
                            + solrFile.substring(solrFile.lastIndexOf("/") + 1);
                    Files.move(Paths.get(solrFile), Paths.get(target), StandardCopyOption.REPLACE_EXISTING);
                }
                //Delete the SOLR format file irrespective of conversion success state to save space.
                //By running the export command for the time range in file name, same file can be generated again.

                //			new File(solrFile).delete();
                LOGGER.info("Conversion took " + ((System.currentTimeMillis() - t) / 1000 + " seconds"));
            } while (true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addTimeRangeToFailedQueue(String fileName) {
        // e.g fileName:  /tmp/migration/exported/solrData_2020-01-19T06:00:00.000Z_5.json.converting
        // e.g timeRange to be prepared: 2020-01-19T06:00:00.000Z TO 2020-01-19T06:59:59.999Z_5

        fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);
        String[] parts = fileName.split("_");
        String fromTime = parts[1];
        monitor.addFailedTimeRange(fromTime + " TO " + fromTime.replaceAll(":00:00.000Z", ":59:59.999Z")
                                           + "_" + parts[2].substring(0, parts[2].indexOf(".")));
    }

    /*private void log(String msg) {
        System.out.println(new Date().toString() + " Converter thread [" + id + "]: " + msg);
    }*/

    private static class ExportedFileNameFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".json");
        }
    }
}
