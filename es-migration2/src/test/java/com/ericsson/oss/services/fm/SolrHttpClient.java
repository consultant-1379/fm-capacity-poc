package com.ericsson.oss.services.fm;

import com.ericsson.oss.services.fm.alarm.migration.Util;

public class SolrHttpClient {
    public static void main(String[] args) throws Exception {

        final String solrHost = "localhost:39982";
        final String solrCore = "collection1";
        final String startIndex = "20000";
        final String numOfRecsToExportPerIteration = "1000";
        final String timeRange = "2020-07-29T03:00:00.000Z TO 2020-07-29T03:59:59.999Z";

        String[] cmd1 = { "/usr/bin/curl",
                "http://" + solrHost + "/solr/" + solrCore + "/query",
                "-d", "q=*:*&start=" + startIndex + "&rows=" + numOfRecsToExportPerIteration
                + "&fq=insertTime:[" + timeRange + "]&sort=insertTime+asc",
                "-o", "/tmp/zetto7" };
        StringBuilder str = new StringBuilder("");
        for (int i=0; i<cmd1.length; i++) {
            str.append(cmd1[i]);
        }
        System.out.println(str);


        final long start = System.currentTimeMillis();
        int exitVal = Util.executeAndExport(cmd1, "/tmp/zetto1", false, true);
        final long end_request = System.currentTimeMillis();
        long delay = end_request - start;
        if (exitVal == 0) {
            System.out.println("-- Response OK " + delay);
        } else {
            System.out.println("-- Response NOK " + exitVal);
        }

        // jq -c '.response["docs"]'  zetto7 > /tmp/migration/test/zetto7.json
        String[] cmd = { "/usr/bin/jq", "-c",
                ".response[\"docs\"]",
                "/tmp/zetto7" };
        // log(" with cmd: " + String.join(" ", cmd) + " > " + esDataFile + " : ");

        exitVal = Util.executeAndExport(cmd, "/tmp/zetto77", false, true);
        delay = System.currentTimeMillis() - end_request;
        if (exitVal == 0) {
            System.out.println("-- Parse OK " + delay);
        } else {
            System.out.println("-- Parse NOK " + exitVal);
        }
        // System.out.println(result);
        long total = System.currentTimeMillis() - start;
        System.out.println("-- TOTAL " + total);
    }
}

