///*
//package com.ericsson.oss.services.fm;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.ericsson.oss.services.fm.alarm.migration.concurrent.Converter;
//import com.ericsson.oss.services.fm.alarm.migration.concurrent.Importer;
//import com.ericsson.oss.services.fm.alarm.migration.concurrent.Monitor;
//import com.ericsson.oss.services.fm.alarm.migration.http.EsImporter;
//
//public class TestConvertAndImport {
//    private static final Logger LOGGER = LoggerFactory.getLogger(EsImporter.class);
//
//    private static final String TEST_MIGRATION_TEMP_DIR = "/tmp/edelpao";
//
//    public static void main(String[] args) throws Exception {
//        String exportTempDir = TEST_MIGRATION_TEMP_DIR + "/exported1";
//        String convertedTempDir = TEST_MIGRATION_TEMP_DIR + "/converted1";
//        String esHost = "localhost:9200";
//        int convertThreadsCount = 3;
//        int importThreadsCount = 2;
//        Monitor monitor = new Monitor(1, convertThreadsCount,
//                                      1, exportTempDir, convertedTempDir);
//
//        monitor.exporterExiting();
//
//        long start = System.currentTimeMillis();
//        //Converter converter = new Converter(0, exportTempDir, convertedTempDir,
//        //                                    monitor, 1, 2);
//
//        //converter.run();
//        //monitor.converterExiting();
//
//        ExecutorService convertExecutorService = Executors.newFixedThreadPool(convertThreadsCount);
//        for (int i = 0; i < convertThreadsCount; i++) {
//            convertExecutorService.execute(new Converter(0, exportTempDir, convertedTempDir,
//                                                         monitor, 1, 0));
//        }
//        //        Importer importer = new Importer(1, esHost, monitor);
//        //        importer.run();
//
//        ExecutorService importExecutorService = Executors.newFixedThreadPool(importThreadsCount);
//        for (int i = 0; i < importThreadsCount; i++) {
//            importExecutorService.execute(new Importer(importThreadsCount, esHost, monitor));
//        }
//        convertExecutorService.shutdown();
//        convertExecutorService.awaitTermination(1, TimeUnit.HOURS);
//        importExecutorService.shutdown();
//        importExecutorService.awaitTermination(1, TimeUnit.HOURS);
//        LOGGER.info("Total Migration took " + ((System.currentTimeMillis() - start) / 1000 + " seconds"));
//    }
//}
//*/
