package com.amazon.opendistroforelasticsearch.commons.security;

import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Encapsulates RestHighLevelClient.
 * Provide wrapper methods which can impersonate and make calls to RestHighLevelClient.
 * Also, provides 'high level' api for few security plugin calls.
 *
 * Fixme:
 * Handle if user is deleted?
 * Size increase of the plugin jars
 * Acceptable errors messages,
 */
public class RestClient {

    private final Logger log = LogManager.getLogger(RestClient.class);
    private final RestHighLevelClient client;
    private final RestHelper restHelper;


    public RestClient(Settings settings) throws IOException {
        restHelper = new RestHelper(settings);
        client = new RestHighLevelClient(restHelper.createClientBuilder());
    }

    public org.elasticsearch.client.RestClient getLowLevelClient() {
        return client.getLowLevelClient();
    }


    public final SearchResponse search(SearchRequest searchRequest) throws IOException {
        return client.search(searchRequest, restHelper.getDefaultRequestOptions());
    }


    public final SearchResponse search(SearchRequest searchRequest, String impersonate) throws IOException {
        return client.search(searchRequest, restHelper.getRequestOptions(impersonate));
    }

    public final BulkResponse bulk(BulkRequest bulkRequest) throws IOException {
        return client.bulk(bulkRequest, restHelper.getDefaultRequestOptions());
    }

    public final GetResponse get(GetRequest getRequest) throws IOException {
        return client.get(getRequest, restHelper.getDefaultRequestOptions());
    }

    public final AuthInfoResponse authinfo(AuthInfoRequest authInfoRequest) throws IOException {

        Map<String,String> headers = new HashMap<>();
        headers.put(ConfigConstants.AUTHORIZATION, authInfoRequest.getAuthTokens().get(0));

        Request request = new Request("GET", "/_opendistro/_security/authinfo");
        request.setOptions(restHelper.getRequestOptions(headers));

        Response response = client.getLowLevelClient().performRequest(request);
        return new AuthInfoResponse(EntityUtils.toString(response.getEntity()));
    }

}
