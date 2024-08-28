package com.ericsson.oss.services.fm.alarm.migration.concurrent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.services.fm.alarm.migration.SolrCoreStats;
import com.ericsson.oss.services.fm.alarm.migration.Util;

public class Migrate {
    private static final Logger LOGGER = LoggerFactory.getLogger(Migrate.class);

    public static void main(String[] args) throws Exception {

        //consolOut should be the last arg and if not passed default to false.

        if (args.length < 6) {
            System.out.println("USAGE to migrate SOLR data to Elasticsearch\n"
                                       + ": java -cp EsPoC.jar com.alarm.migration.concurrent.Migrate"
                                       + " solrIp:port esIp:port exportThreadCount convertionThreadCount importThreadCount"
                                       + " DAILY_INDICES numOfRange\n"
                                       + "e.g: java -cp EsPoC.jar com.alarm.migration.concurrent.Migrate"
                                       + " solr:8983 10.247.246.8:8900 4 8 4 DAILY_INDICES 2 \n"
                                       + "Make sure to create /tmp/migration/exported/, /tmp/migration/converted/ "
                                       + "and /ericsson/postgres/solrFailedFiles folders manually");
            System.exit(1);
        }

        String solrHost = args[0]; //"localhost:8983";
        String esHost = args[1]; //"localhost:9200";
        String solrCore = "collection1";
        int numOfRecsToExportPerIteration = 20000;
        int indixType = Util.DAILY_INDICES;
		if (args[5].equalsIgnoreCase("WEEKLY_INDICES")) {
			indixType = Util.WEEKLY_INDICES;
		} else if (args[5].equalsIgnoreCase("DAILY_INDICES")) {
			indixType = Util.DAILY_INDICES;
		}

        Migrate migrate = new Migrate();
        migrate.migrate(solrHost, esHost, solrCore, numOfRecsToExportPerIteration, Integer.parseInt(args[2]),
                        Integer.parseInt(args[3]), Integer.parseInt(args[4]), indixType, Integer.parseInt(args[6]));
    }

    private void migrate(String solrHost, String esHost, String solrCore, int numOfRecsToExportPerIteration, int exportThreadsCount,
                         int convertThreadsCount, int importThreadsCount, int indexType, int numOfRange) throws Exception {

        String exportTempDir = Util.MIGRATION_TEMP_DIR + "/exported";
        String convertedTempDir = Util.MIGRATION_TEMP_DIR + "/converted";

        long start = System.currentTimeMillis();

        LOGGER.info("Migration started at " + new Date().toString());
        LOGGER.info("Getting " + solrCore + " statistics");
        SolrCoreStats stats = Util.getCoreStats(solrHost, solrCore);
        LOGGER.info("\n\nTotal number of records to migrate: " + stats.totalNumOfRecords
                            + ", Min insertTime: " + stats.minInsertTime + ", Max insertTime: " + stats.maxInsertTime);

        ExecutorService exportExecutorService = Executors.newFixedThreadPool(exportThreadsCount);
        ExecutorService convertExecutorService = Executors.newFixedThreadPool(convertThreadsCount);
        ExecutorService importExecutorService = Executors.newFixedThreadPool(importThreadsCount);

        Monitor monitor = new Monitor(exportThreadsCount, convertThreadsCount,
                                      indexType, exportTempDir, convertedTempDir);
        prepareTimeRanges(stats, indexType, numOfRange, monitor);

        new Thread(monitor).start();

        for (int i = 0; i < exportThreadsCount; i++) {
            exportExecutorService.execute(new Exporter(i, solrHost, solrCore, exportTempDir,
                                                       numOfRecsToExportPerIteration, monitor));
        }

        for (int i = 0; i < convertThreadsCount; i++) {
            convertExecutorService.execute(new Converter(i, exportTempDir, convertedTempDir,
                                                         monitor, indexType));
        }

        for (int i = 0; i < importThreadsCount; i++) {
            importExecutorService.execute(new Importer(i, esHost, monitor));
        }

        exportExecutorService.shutdown();
        convertExecutorService.shutdown();
        importExecutorService.shutdown();
        importExecutorService.awaitTermination(1, TimeUnit.HOURS);
        LOGGER.info("Total Migration took " + ((System.currentTimeMillis() - start) / 1000 + " seconds"));
        System.exit(0);
    }

    private void prepareTimeRanges(SolrCoreStats stats, int indexType, int maxRange, Monitor monitor) {

        //Form hourly time ranges and add to queue
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH");

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(stats.minInsertTime);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MILLISECOND, 0);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(stats.maxInsertTime);
        endCal.set(Calendar.MINUTE, 59);
        endCal.set(Calendar.SECOND, 59);
        endCal.set(Calendar.MILLISECOND, 999);

        String fromTime;
        String esIndexName;
        int numOfRange = 0;
        while (startCal.compareTo(endCal) < 0 && (maxRange == 0 || numOfRange < maxRange)) {

            fromTime = dateFormat.format(startCal.getTime());
            esIndexName = Util.getEsIndexName(indexType, fromTime + ":00:00.000Z");
            monitor.enqueueTimeRangeForExporting(esIndexName, fromTime + ":00:00.000Z TO " + fromTime + ":59:59.999Z");

            startCal.add(Calendar.HOUR, 1);
            numOfRange++;
        }
    }
}
