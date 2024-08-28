package com.ericsson.oss.services.fm.alarm.migration.es;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NByteArrayEntity;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EsManager {
    final static String id = "EsImporter";
    private static final Logger LOGGER = LoggerFactory.getLogger(EsManager.class);
    int port;
    RestHighLevelClient client;
    String esHost;

    public EsManager(final String esHost) {
        this.esHost = esHost;
    }

    public boolean openConnection() {
        try {
            client = new RestHighLevelClient(
                    RestClient.builder(HttpHost.create(esHost)));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void finalize() {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkAndCreateIndex(final String indexString) throws EsManagerException {
        //        RestHighLevelClient client = new RestHighLevelClient(
        //                RestClient.builder(new HttpHost(hostname, port, "http")));
        if (client == null) {
            openConnection();
        }
        try {
            // ES 6.8.4
            GetIndexRequest requestExist = new GetIndexRequest(indexString);
            boolean exists = client.indices().exists(requestExist, RequestOptions.DEFAULT);
            //            GetIndexRequest requestExist = new GetIndexRequest();
            //            requestExist.indices(indexString);

            //            boolean exists = client.indices().exists(requestExist, RequestOptions.DEFAULT);
            if (!exists) {
                CreateIndexRequest request = new CreateIndexRequest(indexString);
                request.settings(Settings.builder()
                                         .put("index.number_of_shards", 1)
                                         .put("index.number_of_replicas", 0).put("index.refresh_interval", "-1")
                                         .put("index.translog.durability", "async").put("index.translog.sync_interval", "600s"));

                CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
                boolean acknowledged = createIndexResponse.isAcknowledged();
                boolean shardsAcknowledged = createIndexResponse.isShardsAcknowledged();
                LOGGER.info("CreateIndexResponse acknowledged: {} shardsAcknowledged:{}", acknowledged, shardsAcknowledged);
            }
        } catch (Exception e) {
            LOGGER.error("CheckAndCreateIndex {} Exception {}", indexString, e);
            throw new EsManagerException("CheckAndCreateIndex " + indexString, e);
        }
    }

    public List<String> bulkAdd(final String indexString, List array) {
        if (client == null) {
            openConnection();
        }
        ObjectMapper objectMapper = new ObjectMapper();

        List<String> listOfFailedId = new ArrayList<>();
        ByteArrayOutputStream content = new ByteArrayOutputStream();

        JSONObject jsonIndex = new JSONObject();
        JSONObject item = new JSONObject();
        item.put("_index", indexString);
        item.put("_type", "doc");
        jsonIndex.put("index", item);
        final byte[] index = jsonIndex.toString().getBytes();

        array.forEach(obj -> {
            Map jsonObject = (Map) obj;
            try {
                String json = objectMapper.writeValueAsString(jsonObject);
                final byte[] source = json.getBytes();
                content.write(index, 0, index.length);
                content.write(XContentType.JSON.xContent().streamSeparator());
                content.write(source, 0, source.length);
                content.write(XContentType.JSON.xContent().streamSeparator());

                if (content.size() > 70 * 1024 * 1024) {
                    executeAsync(content, indexString);
                    content.reset();
                }
            } catch (Exception e) {
                throw new EsManagerException("BULK ADD Failure " + indexString + "Exception", e);
            }
        });
        if (content.size() != 0) {
            executeSync(content, indexString);
        }

        return listOfFailedId;
    }

    private void executeAsync(ByteArrayOutputStream content, final String indexString) {
        Request request = new Request(HttpPost.METHOD_NAME, "/_bulk");
        request.setEntity(new NByteArrayEntity(content.toByteArray(), 0, content.size(), ContentType.APPLICATION_JSON));
        request.setOptions(RequestOptions.DEFAULT);

        RestClient lowLevelClient = client.getLowLevelClient();
        try {
            lowLevelClient.performRequestAsync(request, new ResponseListener() {
                @Override
                public void onSuccess(final Response response) {
                    LOGGER.debug("BULK ADD Success {}", response);
                }

                @Override
                public void onFailure(final Exception exception) {
                    LOGGER.error("BULK ADD Failure {} {}", indexString, exception);
                }
            });
        } catch (Exception e) {
            LOGGER.error("BULK ADD Failure {}", e);
            throw new EsManagerException("BULK ADD Failure " + indexString + "Exception", e);
        }
    }

    private void executeSync(ByteArrayOutputStream content, final String indexString) {
        Request request = new Request(HttpPost.METHOD_NAME, "/_bulk");
        request.setEntity(new NByteArrayEntity(content.toByteArray(), 0, content.size(), ContentType.APPLICATION_JSON));
        request.setOptions(RequestOptions.DEFAULT);

        RestClient lowLevelClient = client.getLowLevelClient();
        try {
            Response response = lowLevelClient.performRequest(request);
            if (response.getStatusLine().getStatusCode() != 200) {
                LOGGER.info("BULK ADD Failure {}", response);
            }
        } catch (Exception e) {
            LOGGER.error("BULK ADD Failure {}", e);
            throw new EsManagerException("BULK ADD Failure " + indexString + "Exception", e);
        }
    }

    public List<String> bulkAdd1(final String indexString, List array) {
        if (client == null) {
            openConnection();
        }
        try {
            List<String> listOfFailedId = new ArrayList<String>();
            BulkRequestList bulkRequestList = new BulkRequestList();

            array.forEach(obj -> {
                final BulkRequest current = bulkRequestList.getCurrent();
                Map jsonObject = (Map) obj;
                String jsonObjectId = (String) jsonObject.get("id");
                // IndexRequest indexRequest = new IndexRequest(indexString, "doc", jsonObjectId).source(jsonObject, XContentType.JSON);
                IndexRequest indexRequest = new IndexRequest(indexString, "doc").source(jsonObject, XContentType.JSON);
                current.add(indexRequest);
                if (current.estimatedSizeInBytes() > 70 * 1024 * 1024) {
                    LOGGER.info("BULK size {}", current.estimatedSizeInBytes());
                    bulkRequestList.newBulkRequest();
                }
            });
            LOGGER.debug("Going to BULK {}", bulkRequestList.getList().size());
            bulkRequestList.getList().forEach(bulkRequest -> {
                BulkResponse response;
                try {
                    LOGGER.debug("Going to BULK number of actions {}", bulkRequest.numberOfActions());
                    if (bulkRequest.numberOfActions() != 0) {
                        response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
                        response.forEach(elem -> {
                            if (elem.isFailed()) {
                                LOGGER.error("BULK ADD Failure {}", elem.getFailureMessage());
                                listOfFailedId.add(elem.getId());
                            } else {
                                LOGGER.debug("BULK ADD Success {}", elem.getResponse());
                            }
                        });
                    }
                } catch (Exception e) {
                    LOGGER.error("BULK ADD Failure {}", e);
                    throw new EsManagerException("BULK ADD Failure " + indexString + "Exception", e);
                }
            });
            return listOfFailedId;
        } catch (Exception e) {
            LOGGER.error("BULK ADD {} Exception {}", indexString, e);
            throw new EsManagerException("BULK ADD " + indexString + "Exception", e);
        }
    }

    public long getNumOfDocument(final String indexString) throws IOException {
        if (client == null) {
            openConnection();
        }
        try {
            //        GetIndexRequest getIndexRequest = new GetIndexRequest();
            //        getIndexRequest.indices(indexString);
            GetIndexRequest getIndexRequest = new GetIndexRequest(indexString);
            GetIndexResponse response = client.indices().get(getIndexRequest, RequestOptions.DEFAULT);
            String[] indices = response.getIndices();
            if (indices.length > 0) {
                CountRequest countRequest = new CountRequest();
                countRequest.indices(indices);

                CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);

                return countResponse.getCount();
            } else {
                return 0;
            }
        } catch (Exception e) {
            LOGGER.error("GET NUM OF DOCUMENT {} Exception {}", indexString, e);
            throw new EsManagerException("GET NUM OF DOCUMENT " + indexString + "Exception", e);
        }
    }

    public boolean commit(final String indexString) throws IOException {
        if (client == null) {
            openConnection();
        }
        GetIndexRequest getIndexRequest = new GetIndexRequest(indexString);
        GetIndexResponse response = client.indices().get(getIndexRequest, RequestOptions.DEFAULT);
        String[] indices = response.getIndices();
        if (refresh(indices)) {
            return resync(indices);
        } else {
            return false;
        }
    }

    private boolean refresh(final String[] indices) throws IOException {
        UpdateSettingsRequest request = new UpdateSettingsRequest(indices);
        Settings settings =
                Settings.builder()
                        .put("index.refresh_interval", "1s")
                        .build();
        request.settings(settings);
        final AcknowledgedResponse updateSettingsResponse =
                client.indices().putSettings(request, RequestOptions.DEFAULT);
        boolean acknowledged = updateSettingsResponse.isAcknowledged();
        LOGGER.info("refresh acknowledged: {}", acknowledged);
        return acknowledged;
    }

    private boolean resync(final String[] indices) throws IOException {
        UpdateSettingsRequest request = new UpdateSettingsRequest(indices);
        Settings settings =
                Settings.builder()
                        .put("index.translog.durability", "request")
                        .build();
        request.settings(settings);
        final AcknowledgedResponse updateSettingsResponse =
                client.indices().putSettings(request, RequestOptions.DEFAULT);
        boolean acknowledged = updateSettingsResponse.isAcknowledged();
        LOGGER.info("resync acknowledged: {}", acknowledged);
        return acknowledged;
    }

    private class BulkRequestList {
        List<BulkRequest> bulkRequestList;
        int current;

        public BulkRequestList() {
            bulkRequestList = new ArrayList<BulkRequest>();
            bulkRequestList.add(new BulkRequest());
            current = 0;
        }

        public BulkRequest getCurrent() {
            return bulkRequestList.get(current);
        }

        public List<BulkRequest> getList() {
            return bulkRequestList;
        }

        public void newBulkRequest() {
            bulkRequestList.add(new BulkRequest());
            current++;
        }
    }
}
