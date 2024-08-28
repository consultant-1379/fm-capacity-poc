package com.ericsson.oss.services.fm.alarm.migration.recovery;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.services.fm.alarm.migration.Util;
import com.ericsson.oss.services.fm.alarm.migration.concurrent.Monitor;
import com.ericsson.oss.services.fm.alarm.migration.es.EsImporter;
import com.ericsson.oss.services.fm.alarm.migration.es.EsManager;
import com.ericsson.oss.services.fm.alarm.migration.solr.SolrManager;

public class LastRecovery {
    private static final Logger LOGGER = LoggerFactory.getLogger(SolrManager.class);
    private static final String exportTempDir = Util.MIGRATION_TEMP_DIR + "/exported";
    private static final String convertedTempDir = Util.MIGRATION_TEMP_DIR + "/converted";
    static String startPatternId = "\"id\":\"";
    static String endPatternId = "\"}";
    static String startPatternNumFound = "\"numFound\":";
    static String endPatternNumFound = ",\"start\"";

    public static void main(String[] args) throws IOException, InterruptedException {
        final String solrHost = "localhost:39981";
        final String solrCore = "collection1";
        String esHost = "localhost:9200";

        final SolrManager solrManager = new SolrManager(solrCore, solrHost);

        Monitor monitor = new Monitor(1, 1,
                                      1, exportTempDir, convertedTempDir);

        monitor.exporterExiting();
        final EsImporter esImporter = new EsImporter(1, exportTempDir, esHost, monitor);

        String sDir = Util.MIGRATION_TEMP_DIR + "/failure/";
        File dir = new File(sDir);
        final File[] dirList = dir.listFiles();
        long totalNumOfRecords = 0;
        for (int i = 0; i < dirList.length; i++) {
            LOGGER.info("File {}", dirList[i]);
            File file = dirList[i];
            FileReader fr = new FileReader(file);   //reads the file
            BufferedReader br = new BufferedReader(fr);  //creates a buffering character input stream
            StringBuffer sb = new StringBuffer();    //constructs a string buffer with no characters
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("numFound")) {
                    String numFound = StringUtils.substringBetween(line, startPatternNumFound, endPatternNumFound);
                    totalNumOfRecords += Long.valueOf(numFound);
                } else
                if (line.contains(startPatternId)) {
                    String id = StringUtils.substringBetween(line, startPatternId, endPatternId);
                    String basename = FilenameUtils.getBaseName(file.getName());
                    String recoverySolrFile = exportTempDir + "/" + basename + "_" + id + ".json";
                    solrManager.exportToFile(recoverySolrFile, id);
                }
            }
            fr.close();    //closes the stream and release the resources
        }
        monitor.setToMigrateRecords(totalNumOfRecords);
        enqueueEsFileForImporting(monitor);
        esImporter.run();
        EsManager esManager = new EsManager(esHost);
        boolean result = esManager.commit("fm*");
        LOGGER.info("Commit result {}", result);
        long numOfDocuments = esManager.getNumOfDocument("fm*");;
        int count = 0;
        while (numOfDocuments < monitor.getMigratedRecords() && count < 40) {
            numOfDocuments = esManager.getNumOfDocument("fm*");
            LOGGER.info("NUM of Document in ES {} OF {}", numOfDocuments, monitor.getMigratedRecords());
            Thread.sleep(30000);
            count ++;
        }
        LOGGER.info("NUM of Document in ES {} OF {}", numOfDocuments, monitor.getMigratedRecords());
        System.exit(0);
    }

    private static void enqueueEsFileForImporting(Monitor monitor) {
        File dir = new File(exportTempDir);
        String[] exportedFiles = dir.list(new ExportedFileNameFilter());
        for (String file : exportedFiles) {
            String esDataFile = file.substring(file.lastIndexOf(File.separator) + 1);
            String esIndexName = StringUtils.substringBetween(esDataFile, "solrData_", ".json");
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
