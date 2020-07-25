package com.amazon.opendistroforelasticsearch.commons.rest;

import com.amazon.opendistroforelasticsearch.commons.ConfigConstants;
import com.amazon.opendistroforelasticsearch.commons.authinfo.AuthInfoRequest;
import com.amazon.opendistroforelasticsearch.commons.authinfo.AuthInfoResponse;
import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.common.settings.Settings;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides helper methods to create low-level and high-level REST client to make calls to Elasticsearch.
 *
 * Sample usage:
 *  RestHelper restHelper = new RestHelper(settings);
 *  RestClient restClient = restHelper.createRestClientBuilder().build();
 *
 *  Request request = new Request("GET",  String.format("/%s/_search?q=ERROR", index));
 *  RequestOptions.Builder builder = restHelper.getDefaultRequestOptions().toBuilder();
 *  builder.addHeader("Authorization", "my-secret-auth-code");
 *  request.setOptions(builder);
 *
 *  Response response = restClient.performRequest(request);
 *
 */
public class RestHelper {

    private final boolean httpSSLEnabled;
    private final int port;
    private final String host;
    private final String scheme;

    public RestHelper(Settings settings) {
        host = ConfigConstants.HOST_DEFAULT;
        port = settings.getAsInt(ConfigConstants.HTTP_PORT, ConfigConstants.HTTP_PORT_DEFAULT);
        httpSSLEnabled = settings.getAsBoolean(ConfigConstants.OPENDISTRO_SECURITY_SSL_HTTP_ENABLED, false);
        scheme = httpSSLEnabled ? ConfigConstants.HTTPS : ConfigConstants.HTTP;
    }

    /**
     * Used to create default RequestOptions object. Its used to create rest request to Elasticsearch.
     * @return
     */
    public final RequestOptions getDefaultRequestOptions() {
        return createDefaultOptions().build();
    }

    /**
     * Used to create RequestOptions object with additional request headers.
     * Its used to create rest request to Elasticsearch.
     * @param headers
     * @return
     */
    public RequestOptions getRequestOptions(Map<String,String> headers) {
        RequestOptions.Builder builder = createDefaultOptions();
        headers.forEach((k, v) -> builder.addHeader(k,v));
        return builder.build();
    }

    private RequestOptions.Builder createDefaultOptions() {
        return RequestOptions.DEFAULT.toBuilder()
                .addHeader(ConfigConstants.CONTENT_TYPE, ConfigConstants.CONTENT_TYPE_DEFAULT);
    }


    /**
     * Creates a RestClientBuilder which is used to create REST client.
     * If ssl is enabled, creates RestClientBuilder using self-signed certificates as trusted.
     * Else, creates http based one.
     * Usage sample to create low-level rest client.
     *
     * RestClient restClient = RestHelper.createRestClientBuilder().build();
     *
     * @return
     * @throws IOException
     */
    public RestClientBuilder createRestClientBuilder() throws IOException {
        if(!httpSSLEnabled) {
            return org.elasticsearch.client.RestClient.builder(new HttpHost(host, port, scheme));
        } else {
            return createWithSelfSigned();
        }
    }

    /**
     * Creates a RestClientBuilder which is used to create REST client.
     * Currently uses TrustSelfSignedStrategy (trust strategy that accepts self-signed certificates as trusted).
     * todo: make the cert configurable, rather than hardcoded to self-signed.
     * @return
     * @throws IOException
     */
    private RestClientBuilder createWithSelfSigned() throws IOException {
        final SSLContext sslContext;
        try {
            SSLContextBuilder sslbuilder = new SSLContextBuilder();
            sslbuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            sslContext = sslbuilder.build();
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            throw new IOException(e);
        }

        return org.elasticsearch.client.RestClient.builder(new HttpHost(host, port, scheme))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder.setSSLContext(sslContext);
                    }
                });
    }


    /**
     *
     * @param restClient
     * @param authInfoRequest
     * @return
     * @throws IOException
     */
    public final AuthInfoResponse getAuthInfo(RestClient restClient, AuthInfoRequest authInfoRequest) throws IOException {
        if(!httpSSLEnabled)
            return new AuthInfoResponse();

        Map<String,String> headers = new HashMap<>();
        //fixme: how to handle if more than one element.
        if (authInfoRequest.getAuthTokens() == null || authInfoRequest.getAuthTokens().size() == 0) {
            throw new IOException("Authorization header is not present");
        }
        headers.put(ConfigConstants.AUTHORIZATION, authInfoRequest.getAuthTokens().get(0));

        Request request = new Request("GET", "/_opendistro/_security/authinfo");
        request.setOptions(getRequestOptions(headers));

        Response response = restClient.performRequest(request);
        return new AuthInfoResponse(EntityUtils.toString(response.getEntity()));
    }
}
