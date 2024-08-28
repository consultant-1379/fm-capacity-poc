package com.ericsson.oss.services.fm.alarm.migration.http;

import java.text.DateFormat;
import java.util.Date;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.services.fm.alarm.migration.Util;
import com.ericsson.oss.services.fm.alarm.migration.concurrent.Monitor;
import com.ericsson.oss.services.fm.alarm.migration.solr.RangesCreator;

public class TestRanges {
    private static final Logger LOGGER = LoggerFactory.getLogger(MigrateInTwoSteps.class);

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();
    private static final String solrCore = "collection1";
    private final String solrHost;
    private final int numOfRecsToExportPerIteration;

    public TestRanges(String solrHost, int numOfRecsToExportPerIteration) {
        this.solrHost = solrHost;
        this.numOfRecsToExportPerIteration = numOfRecsToExportPerIteration;
    }

    public static void main(String[] args) throws Exception {
        String exportTempDir = Util.MIGRATION_TEMP_DIR + "/exported";
        String convertedTempDir = Util.MIGRATION_TEMP_DIR + "/converted";

        if (args.length < 2) {
            System.out.println("USAGE test SOLR data range size\n"
                                       + ": java -jar solr-test.jar"
                                       + " solrIp:port numOfRecsToExportPerIteration\n"
                                       + "e.g: java -jar solr-test.jar solr:8983 20000 \n"
                                       + "Make sure to create /tmp/migration/exported/, /tmp/migration/converted/ "
                                       + "and /tmp/migration/failure folders manually");
            System.exit(1);
        }
        Date minInsertTime = null;
        Date maxInsertTime = null;
        if (args.length == 4) {
            final DateFormat dateFormat = Util.getDateFormat();
            minInsertTime = dateFormat.parse(args[2]);
            maxInsertTime = dateFormat.parse(args[3]);
            LOGGER.info("Start {}", dateFormat.format(minInsertTime));
            LOGGER.info("End {}", dateFormat.format(minInsertTime));
        }

        Monitor monitor = new Monitor(1, 1,
                                      1, exportTempDir, convertedTempDir);

        RangesCreator rangesCreator = new RangesCreator(args[0],
                                                        Util.DAILY_INDICES,
                                                        Integer.parseInt(args[1]),
                                                        monitor,
                                                        minInsertTime,
                                                        maxInsertTime);
        rangesCreator.run();
        // new TestRanges(args[0], Integer.parseInt(args[1])).prepareTimeRanges(Util.DAILY_INDICES, monitor);
    }
}
