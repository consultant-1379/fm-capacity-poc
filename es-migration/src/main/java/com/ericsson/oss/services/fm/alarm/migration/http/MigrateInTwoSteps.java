package com.ericsson.oss.services.fm.alarm.migration.http;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.services.fm.alarm.migration.Util;
import com.ericsson.oss.services.fm.alarm.migration.concurrent.Monitor;
import com.ericsson.oss.services.fm.alarm.migration.es.EsImporter;
import com.ericsson.oss.services.fm.alarm.migration.es.EsManager;
import com.ericsson.oss.services.fm.alarm.migration.solr.RangesCreator;
import com.ericsson.oss.services.fm.alarm.migration.solr.SolrExporter;

public class MigrateInTwoSteps {
    private static final Logger LOGGER = LoggerFactory.getLogger(MigrateInTwoSteps.class);

    public static void main(String[] args) throws Exception {
        System.getProperty("migration.dir", Util.MIGRATION_TEMP_DIR);

        if (args.length < 5) {
            System.out.println("USAGE to migrate SOLR data to Elasticsearch\n"
                                       + ": java -jar es-migrate.jar"
                                       + " solrIp:port esIp:port exportThreadCount importThreadCount"
                                       + " DAILY_INDICES numOfRecsToExportPerIteration\n"
                                       + "e.g: java -jar es-migrate.jar"
                                       + " solr:8983 10.247.246.8:9200 8 8 DAILY_INDICES 20000 \n"
                                       + "Make sure to create /tmp/migration/exported/, /tmp/migration/converted/ "
                                       + "and /tmp/migration/failure folders manually");
            System.exit(1);
        }
        Date minInsertTime = null;
        Date maxInsertTime = null;
        if (args.length == 8) {
            DateFormat dateFormat = Util.getDateFormat();
            minInsertTime = dateFormat.parse(args[6]);
            maxInsertTime = dateFormat.parse(args[7]);
        }

        String solrHost = args[0]; //"localhost:8983";
        String esHost = args[1]; //"localhost:9200";
        int exportThreadsCount = Integer.parseInt(args[2]);
        int importThreadsCount = Integer.parseInt(args[3]);
        String solrCore = "collection1";
        int numOfRecsToExportPerIteration = Integer.parseInt(args[5]);
        int indixType = Util.DAILY_INDICES;
        if (args[4].equalsIgnoreCase("WEEKLY_INDICES")) {
            indixType = Util.WEEKLY_INDICES;
        } else if (args[4].equalsIgnoreCase("DAILY_INDICES")) {
            indixType = Util.DAILY_INDICES;
        }

        final MigrateInTwoSteps migrate = new MigrateInTwoSteps();
        migrate.migrate(solrHost, esHost, solrCore, numOfRecsToExportPerIteration, Integer.parseInt(args[2]),
                        Integer.parseInt(args[3]), indixType, minInsertTime, maxInsertTime);
    }

    private void migrate(String solrHost, String esHost, String solrCore, int numOfRecsToExportPerIteration, int exportThreadsCount,
                         int importThreadsCount, int indexType, Date minInsertTime, Date maxInsertTime) throws Exception {

        String exportTempDir = Util.MIGRATION_TEMP_DIR + "/exported";

        long start = System.currentTimeMillis();

        LOGGER.info("Migration started at {}", new Date());

        ExecutorService exportExecutorService = Executors.newFixedThreadPool(exportThreadsCount);
        ExecutorService importExecutorService = Executors.newFixedThreadPool(importThreadsCount);

        Monitor monitor = new Monitor(exportThreadsCount, 1,
                                      indexType, exportTempDir);

        RangesCreator rangesCreator = new RangesCreator(solrHost, indexType, numOfRecsToExportPerIteration, monitor, minInsertTime, maxInsertTime);

        new Thread(rangesCreator).start();
        new Thread(monitor).start();

        for (int i = 0; i < exportThreadsCount; i++) {
            exportExecutorService.execute(new SolrExporter(i, solrHost, solrCore, exportTempDir,
                                                           numOfRecsToExportPerIteration, monitor));
        }

        for (int i = 0; i < importThreadsCount; i++) {
            importExecutorService.execute(new EsImporter(i, exportTempDir, esHost, monitor));
        }

        exportExecutorService.shutdown();
        exportExecutorService.awaitTermination(24, TimeUnit.HOURS);
        importExecutorService.shutdown();
        importExecutorService.awaitTermination(24, TimeUnit.HOURS);

        // Last Recovery

        LOGGER.info("Total Migration took {} seconds: import {}  export {}", (System.currentTimeMillis() - start) / 1000, monitor.getImportTime(),
                    monitor.getExportTime());
        LOGGER.info("Expected {} Actual {} --> Lost {} SUCCESS {}%", monitor.getToMigrateRecords(), monitor.getMigratedRecords(),
                    monitor.getToMigrateRecords() - monitor.getMigratedRecords(), monitor.getMigratedPercentage());

        EsManager esManager = new EsManager(esHost);
        boolean result = esManager.commit("fm*");
        LOGGER.info("Commit result {}", result);
        long numOfDocuments = esManager.getNumOfDocument("fm*");
        int count = 0;
        while (numOfDocuments < monitor.getMigratedRecords() && count < 40) {
            numOfDocuments = esManager.getNumOfDocument("fm*");
            LOGGER.info("NUM of Document in ES {} OF {}", numOfDocuments, monitor.getMigratedRecords());
            Thread.sleep(30000);
            count++;
        }
        LOGGER.info("NUM of Document in ES {} OF {}", numOfDocuments, monitor.getMigratedRecords());
        System.exit(0);
    }
}
