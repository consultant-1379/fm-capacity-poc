package com.ericsson.oss.services.fm;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.services.fm.alarm.migration.Util;
import com.ericsson.oss.services.fm.alarm.migration.concurrent.Monitor;
import com.ericsson.oss.services.fm.alarm.migration.solr.RangesCreator;
import com.ericsson.oss.services.fm.alarm.migration.solr.SolrExporter;

public class TestExporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SolrExporter.class);
    private static final String TEST_MIGRATION_TEMP_DIR = "/tmp/edelpao";
    private static final String solrHost = "localhost:39981";
    private static final String solrCore = "collection1";
    private static final CloseableHttpClient httpClient = HttpClients.createDefault();
    static int numOfRecsToExportPerIteration = 20000;

    public static void main(String[] args) throws Exception {
        String exportTempDir = TEST_MIGRATION_TEMP_DIR + "/exported";
        String convertedTempDir = TEST_MIGRATION_TEMP_DIR + "/converted";
        String solrCore = "collection1";

        String range = "2020-02-15T01:59:15.000Z TO 2020-02-15T01:59:15.999Z";

        int indexType = Util.DAILY_INDICES;

        //        SolrCoreStats stats = Util.getCoreStats(solrHost, solrCore);
        //        System.out.println("\n\nTotal number of records to migrate: " + stats.totalNumOfRecords
        //                                   + ", Min insertTime: " + stats.minInsertTime + ", Max insertTime: " + stats.maxInsertTime);

        Monitor monitor = new Monitor(1, 1,
                                      1, exportTempDir, convertedTempDir);

        int[] rangeSize = { 1 };
        new TestExporter().prepareTimeRanges(Util.DAILY_INDICES, monitor, range, rangeSize);
/*        new TestRanges(solrHost, numOfRecsToExportPerIteration).prepareTimeRanges(Util.DAILY_INDICES, monitor);

        int[] rangeSize = { 2 };
        new TestRanges(solrHost, numOfRecsToExportPerIteration).prepareTimeRanges(Util.DAILY_INDICES, monitor, range, rangeSize);*/

        SolrExporter exporter = new SolrExporter(0, solrHost, solrCore, exportTempDir,
                                                 numOfRecsToExportPerIteration, monitor);

        exporter.run();

        LOGGER.info("To migrate record {} migrated record {}", monitor.getToMigrateRecords(), monitor.getMigratedRecords());
    }

    public void prepareTimeRanges(int indexType, Monitor monitor, String timeRange, int[] rangeSize)
            throws java.text.ParseException, ParseException, IOException, URISyntaxException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        final String[] dateArray = timeRange.split(" TO ");
        final Date minInsertTime = dateFormat.parse(dateArray[0]);
        final Date maxInsertTime = dateFormat.parse(dateArray[1]);

        new RangesCreator(solrHost, indexType, numOfRecsToExportPerIteration, monitor).prepareTimeRanges(indexType,
                                                                                                         monitor,
                                                                                                         minInsertTime,
                                                                                                         maxInsertTime,
                                                                                                         0,
                                                                                                         rangeSize);
    }
}
