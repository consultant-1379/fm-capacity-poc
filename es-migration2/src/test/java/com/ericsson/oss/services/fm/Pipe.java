package com.ericsson.oss.services.fm;

import java.io.IOException;

public class Pipe {

    public static void main(String[] args) throws IOException, InterruptedException {
        String cmd = "/usr/bin/curl http://localhost:39982/solr/collection1/query -d \"q=*:*&start=20000&rows=1000&fq=insertTime:[2020-07-29T03:00:00"
                + ".000Z TO 2020-07-29T03:59:59.999Z]&sort=insertTime+asc\" | jq -c '.response[\"docs\"]'";

        final long start = System.currentTimeMillis();

        Process process = Runtime.getRuntime().exec(cmd);
        int exitVal = process.waitFor();
        long delay = System.currentTimeMillis() - start;
        if (exitVal == 0) {
            System.out.println("-- Parse OK " + delay);
        } else {
            System.out.println("-- Parse NOK " + exitVal);
        }
    }
}
