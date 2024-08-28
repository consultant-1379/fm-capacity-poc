package com.ericsson.oss.services.fm.alarm.migration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Util {

    public static final String MIGRATION_TEMP_DIR = "/tmp/migration";
    public static final int RETRY_SLEEP_SECONDS = 3;
    public static final String CONVERTING_EXT = ".converting";
    //	public static final String IMPORTING_EXT = ".importing";
    public static final int WEEKLY_INDICES = 0;
    public static final int DAILY_INDICES = 1;

    public static int executeAndExport(String[] command, String outputFile, boolean outLog, boolean errorLog) {
        int exitVal = -1;
        try {
            FileOutputStream fos = new FileOutputStream(outputFile);

            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command);

            StreamConsumer errorConsumer = new StreamConsumer(
                    proc.getErrorStream(), false, errorLog);

            StreamConsumer outputConsumer = new StreamConsumer(
                    proc.getInputStream(), true, fos, outLog);

            errorConsumer.start();
            outputConsumer.start();

            exitVal = proc.waitFor();

            errorConsumer.join();
            outputConsumer.join();
            fos.flush();
            fos.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return exitVal;
    }

    public static SolrCoreStats getCoreStats(String solrHost, String solrCore) throws Exception {

        String coreStatsFile = MIGRATION_TEMP_DIR + "/coreStats.xml";

        //curl "http://localhost:8983/solr/collection1/select?q=*:*&stats=true&stats.field=insertTime&rows=0&indent=true"
        String[] cmd = { "/usr/bin/curl",
                "http://" + solrHost + "/solr/" + solrCore + "/select?q=*:*&stats=true&stats.field=insertTime&rows=0&indent=true",
                "-o", coreStatsFile };

        System.out.print(" with cmd: " + String.join(" ", cmd) + " : ");
        executeAndExport(cmd, "/tmp/coreStats.log", false, true);

        File inputFile = new File(coreStatsFile);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        NodeList dateList = doc.getElementsByTagName("date");
        NodeList longList = doc.getElementsByTagName("long");

        String val;
        String attrVal;
        Node node;
        SolrCoreStats stats = new SolrCoreStats();
        DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");
        for (int temp = 0; temp < dateList.getLength(); temp++) {
            node = dateList.item(temp);
            attrVal = node.getAttributes().getNamedItem("name").getNodeValue();
            if (attrVal.equalsIgnoreCase("min")) {
                val = node.getFirstChild().getNodeValue();
                stats.minInsertTime = readFormat.parse(val);
            } else if (attrVal.equalsIgnoreCase("max")) {
                val = node.getFirstChild().getNodeValue();
                stats.maxInsertTime = readFormat.parse(val);
            }
        }

        for (int temp = 0; temp < longList.getLength(); temp++) {
            node = longList.item(temp);
            attrVal = node.getAttributes().getNamedItem("name").getNodeValue();
            if (attrVal.equalsIgnoreCase("count")) {
                val = node.getFirstChild().getNodeValue();
                stats.totalNumOfRecords = Integer.parseInt(val);
            }
        }

        return stats;
    }

    //Get the number of records available in that time range
    public static long getNumOfRecordsInTimeRange(int id, String solrHost, String solrCore, String timeRange) {

        String outFile = "/tmp/timeRangeRecs_" + id + ".json";
        System.out.println("Getting num of records for time range");
        //curl http://localhost:8983/solr/collection1/query -d 'q=*:*&start=0&rows=0&fq=insertTime:[2020-01-19T06:00:00Z TO 2020-01-19T06:59:59.999Z]
        String[] cmd = { "/usr/bin/curl",
                "http://" + solrHost + "/solr/" + solrCore + "/query", "-d", "q=*:*&start=0&rows=0&fq=insertTime:[" + timeRange + "]",
                "-o", outFile };

        System.out.println(" with cmd: " + String.join(" ", cmd) + " : ");
        executeAndExport(cmd, "/tmp/timeRangeRecs.log", false, true);

        JSONParser jsonParser = new JSONParser();

        long numOfRecs = 0;
        try (FileReader reader = new FileReader(outFile)) {
            JSONObject jsonObj = (JSONObject) jsonParser.parse(reader);
            jsonObj = (JSONObject) jsonObj.get("response");
            numOfRecs = (Long) jsonObj.get("numFound");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return numOfRecs;
    }

    public static String getEsIndexName(int indexType, String dateTime) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");
        Calendar cal = Calendar.getInstance();
        String indexName = null;
        try {
            cal.setTime(dateFormat.parse(dateTime));
			if (indexType == Util.WEEKLY_INDICES) {
				indexName = "fm_history_" + cal.get(Calendar.YEAR) + "wk" + String.format("%02d", cal.get(Calendar.WEEK_OF_YEAR));
			} else {
				indexName = "fm_history_" + cal.get(Calendar.YEAR) + String.format("%02d", cal.get(Calendar.MONTH) + 1)
						+ String.format("%02d", cal.get(Calendar.DATE));
			}
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return indexName;
    }

    public static void sleep(int seconds) {
        if (seconds > 0) {
            try {
                Thread.sleep(seconds * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
