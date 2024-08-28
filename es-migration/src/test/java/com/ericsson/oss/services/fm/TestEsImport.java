package com.ericsson.oss.services.fm;

import java.io.File;
import java.io.FilenameFilter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.services.fm.alarm.migration.concurrent.Monitor;
import com.ericsson.oss.services.fm.alarm.migration.es.EsImporter;

public class TestEsImport {
    private static final Logger LOGGER = LoggerFactory.getLogger(EsImporter.class);

    private static final String TEST_MIGRATION_TEMP_DIR = "/tmp/edelpao";
    private static final String exportTempDir = TEST_MIGRATION_TEMP_DIR + "/exported1";
    private static final String convertedTempDir = TEST_MIGRATION_TEMP_DIR + "/converted";

    public static void main(String[] args) throws Exception {
        String esHost = "localhost:9200";
        int importThreadsCount = 4;
        Monitor monitor = new Monitor(1, 1,
                                      1, exportTempDir, convertedTempDir);

        monitor.exporterExiting();

        long start = System.currentTimeMillis();
        enqueueEsFileForImporting(monitor);
        ExecutorService importExecutorService = Executors.newFixedThreadPool(importThreadsCount);
        for (int i = 0; i < importThreadsCount; i++) {
            importExecutorService.execute(new EsImporter(importThreadsCount, exportTempDir, esHost, monitor));
        }
        // EsImporter importer = new EsImporter(1, exportTempDir, esHost, monitor);
        // importer.run();
        importExecutorService.shutdown();
        importExecutorService.awaitTermination(1, TimeUnit.HOURS);
        LOGGER.info("Total Migration took " + ((System.currentTimeMillis() - start) / 1000 + " seconds"));
        System.exit(0);
    }

    private static void enqueueEsFileForImporting(Monitor monitor) {
        File dir = new File(exportTempDir);
        String[] exportedFiles = dir.list(new ExportedFileNameFilter());
        for (String file : exportedFiles) {
            String esDataFile = file.substring(file.lastIndexOf(File.separator) + 1);
            int i = esDataFile.indexOf("_");
            i = esDataFile.indexOf("_", i + 1);
            i = esDataFile.indexOf("_", i + 1);
            String esIndexName = esDataFile.substring(i + 1, esDataFile.indexOf(".json"));
            monitor.enqueueEsFileForImporting(esIndexName, file);
        }
    }

    private static class ExportedFileNameFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".json");
        }
    }
}
