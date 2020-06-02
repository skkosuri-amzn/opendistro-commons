package com.amazon.opendistroforelasticsearch.commons.security;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.common.settings.Settings;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/***
 *  Provides helper methods to create RequestOptions and RestClientBuilder object for making Rest call.
 *
 */
public class RestHelper {

    private final Logger log = LogManager.getLogger(RestHelper.class);
    private final Settings settings;

    public RestHelper(Settings settings) {
        this.settings = settings;
    }

    public final RequestOptions getDefaultRequestOptions() {
        return createDefaultOptions().build();

    }

    public RequestOptions getRequestOptions(String impersonate) {
        return createDefaultOptions()
                .addHeader(ConfigConstants.OPENDISTRO_SECURITY_IMPERSONATE_AS, impersonate)
                .build();
    }

    public RequestOptions getRequestOptions(Map<String,String> headers) {
        RequestOptions.Builder builder = createDefaultOptions();
        headers.forEach((k, v) -> builder.addHeader(k,v));
        return builder.build();
    }


    //todo: refactor and add clientauth mechanishm.
    public RestClientBuilder createClientBuilder() throws IOException {
        if(!httpSSLEnabled()) {
            return org.elasticsearch.client.RestClient.builder(new HttpHost(getHost(), getPort(), getScheme()));
        } else {
            return createWithSelfSignedAndCreds();
        }
    }

    private String getUser() {
        return "admin";
    }

    private String getPassword() {
        return "admin";
    }

    private String getHost() {
        return ConfigConstants.HOST_DEFAULT;
    }

    private int getPort() {
        return settings.getAsInt(ConfigConstants.HTTP_PORT, ConfigConstants.HTTP_PORT_DEFAULT);
    }

    private boolean httpSSLEnabled() {
        return settings.getAsBoolean(ConfigConstants.OPENDISTRO_SECURITY_SSL_HTTP_ENABLED, false);
    }

    private String getScheme() {
        return httpSSLEnabled() ? ConfigConstants.HTTPS : ConfigConstants.HTTP;
    }

    /**
     * Create builder with self signed certs for tls and username/passwd for creds.
     * @return RestClientBuilder
     * @throws IOException
     */
    private RestClientBuilder createWithSelfSignedAndCreds() throws IOException {

        final SSLContext sslContext;
        try {
            SSLContextBuilder sslbuilder = new SSLContextBuilder();
            sslbuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            sslContext = sslbuilder.build();
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            throw new IOException(e);
        }

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(getUser(), getPassword()));

        return org.elasticsearch.client.RestClient.builder(new HttpHost(getHost(), getPort(), getScheme()))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        httpClientBuilder.setSSLContext(sslContext);
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                });
    }

    private RequestOptions.Builder createDefaultOptions() {
        return RequestOptions.DEFAULT.toBuilder()
                .addHeader(ConfigConstants.CONTENT_TYPE, ConfigConstants.CONTENT_TYPE_DEFAULT);
    }

}
