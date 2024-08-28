package com.ericsson.oss.services.fm.alarm.migration.solr;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.services.fm.alarm.migration.SolrCoreStats;
import com.ericsson.oss.services.fm.alarm.migration.Util;

public class SolrManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SolrManager.class);

    private final CloseableHttpClient httpClient;
    private final String solrCore;
    private final String solrHost;

    public SolrManager(final String solrCore, final String solrHost) {
        this.solrCore = solrCore;
        this.solrHost = solrHost;
        this.httpClient = HttpClients.createDefault();
    }

    public SolrCoreStats getStats() throws SolrManagerException {
        SolrCoreStats coreStats = new SolrCoreStats();
        DateFormat readFormat = Util.getDateFormat();
        try {
            URI uri = buildURIStats();
            LOGGER.debug(" with uri: " + uri);
            HttpGet request = new HttpGet(uri);

            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                LOGGER.info("Get Stats {}", result);
                JSONParser parser = new JSONParser();
                JSONObject jsonObj = (JSONObject) parser.parse(result);
                JSONObject stats = (JSONObject) jsonObj.get("stats");
                JSONObject statsFields = (JSONObject) stats.get("stats_fields");
                JSONObject insertTime = (JSONObject) statsFields.get("insertTime");
                coreStats.minInsertTime = readFormat.parse((String) insertTime.get("min"));
                coreStats.maxInsertTime = readFormat.parse((String) insertTime.get("max"));
                coreStats.totalNumOfRecords = (Long) insertTime.get("count");
                LOGGER.info("Get Stats {}", stats);
            }
        } catch (Exception e) {
            LOGGER.error("Get statistics" + e);
            throw new SolrManagerException("Get statistics ", e);
        }
        return coreStats;
    }

    public boolean exportToFile(final String solrFile, String id) throws SolrManagerException {
        boolean result = false;
        try {
            final URI uri = buildURIDoc(id);
            LOGGER.debug(" with uri: " + uri);
            HttpGet request = new HttpGet(uri);

            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                File targetFile = new File(solrFile);
                OutputStream outStream = new FileOutputStream(targetFile);
                entity.writeTo(outStream);
                result = true;
                LOGGER.info("EXPORTED for {} {}", solrFile, targetFile.length());
            } else {
                LOGGER.error("EXPORT failed for {} ", id);
            }
        } catch (Exception e) {
            LOGGER.error("EXPORT {} exception {}: {}", id, e);
            throw new SolrManagerException("EXPORT " + id, e);
        }
        return result;
    }

    public long exportToFile(final String solrFile, final int startIndex, final int numOfRecord, final String timeRange)
            throws SolrManagerException {
        long filesize = 0;
        try {
            URI uri = buildURI(startIndex, numOfRecord, timeRange);
            LOGGER.debug(" with uri: " + uri);
            HttpGet request = new HttpGet(uri);
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                File targetFile = new File(solrFile);
                OutputStream outStream = new FileOutputStream(targetFile);
                entity.writeTo(outStream);
                filesize = targetFile.length();
                LOGGER.info("EXPORTED {} from {} filesize {}", numOfRecord, timeRange, filesize);
            } else {
                LOGGER.error("EXPORT failed {} from {} ", numOfRecord, timeRange);
                throw new SolrManagerException("EXPORT failed " + numOfRecord + " from " + timeRange);
            }
        } catch (Exception e) {
            LOGGER.error("EXPORT exception {} from {}: {}", numOfRecord, timeRange, e);
            throw new SolrManagerException("EXPORT " + numOfRecord + " from " + timeRange, e);
        }
        return filesize;
    }

    public long getNumOfRecordsInTimeRange(String timeRange) throws SolrManagerException {
        Object numOfRecord = null;
        try {
            LOGGER.debug("Getting num of records for time range {} in {}", timeRange);
            URI uri = buildURI(0, 0, timeRange);
            LOGGER.debug(" with uri: {}", uri);
            HttpGet request = new HttpGet(uri);
            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                JSONParser parser = new JSONParser();
                JSONObject jsonObj = (JSONObject) parser.parse(result);
                jsonObj = (JSONObject) jsonObj.get("response");
                numOfRecord = jsonObj.get("numFound");
            }
        } catch (Exception e) {
            LOGGER.error("Get Num Of Record {} from {}: {}", numOfRecord, timeRange, e);
            throw new SolrManagerException("Get Num Of Record " + numOfRecord + " from " + timeRange, e);
        }
        if (numOfRecord == null) {
            LOGGER.error("Null num or record for {}", timeRange);
        }
        return (numOfRecord != null) ? (Long) numOfRecord : null;
    }

    public URI buildURI(final int startIndex, final int numOfRecord, final String timeRange) throws URISyntaxException {

        final String url = "http://" + solrHost + "/solr/" + solrCore + "/query";
        final URIBuilder builder;

        builder = new URIBuilder(url);

        final String records = String.valueOf(numOfRecord);
        final String start = String.valueOf(startIndex);
        final String fq = "insertTime:[" + timeRange + "]";
        builder.setParameter("q", "*:*").setParameter("start", start).setParameter("rows", records)
                .setParameter("fq", fq).setParameter("sort", "insertTime asc");
        return builder.build();
    }

    public URI buildURIStats() throws URISyntaxException {
        final String url = "http://" + solrHost + "/solr/" + solrCore + "/query";
        final URIBuilder builder;
        builder = new URIBuilder(url);
        builder.setParameter("q", "*:*").setParameter("stats", "true").setParameter("stats.field", "insertTime")
                .setParameter("rows", "0");
        return builder.build();
    }

    public URI buildURIDoc(String id) throws URISyntaxException {
        final String url = "http://" + solrHost + "/solr/" + solrCore + "/query";
        final URIBuilder builder;
        builder = new URIBuilder(url);
        builder.setParameter("q", "id:" + id);
        return builder.build();
    }
}
