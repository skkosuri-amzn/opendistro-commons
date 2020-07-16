package com.amazon.opendistroforelasticsearch.commons;


import com.amazon.opendistroforelasticsearch.commons.authinfo.AuthInfoRequest;
import com.amazon.opendistroforelasticsearch.commons.authinfo.AuthInfoResponse;
import com.amazon.opendistroforelasticsearch.security.rolesinfo.RolesInfoAction;
import com.amazon.opendistroforelasticsearch.security.rolesinfo.RolesInfoRequest;
import com.amazon.opendistroforelasticsearch.security.rolesinfo.RolesInfoResponse;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.io.stream.BytesStreamOutput;
import org.elasticsearch.common.settings.Settings;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * Has wrapper api to make transport action calls, mainly cross plugin calls.
 *
 */
public class NodeHelper {

    private final Logger log = LogManager.getLogger(this.getClass());
    public NodeHelper() {

    }

    /**
     * Makes transport layer call to security plugin to get logged on user name and
     * associated opendistro roles.
     *
     * @param client
     * @return
     * @throws IOException
     */
    public RolesInfoResponse getRolesInfo(NodeClient client) throws IOException {
        try {
            ActionResponse action = client.admin().cluster()
                    .execute(RolesInfoAction.INSTANCE, new RolesInfoRequest()).actionGet();
            BytesStreamOutput out = new BytesStreamOutput();
            action.writeTo(out);
            return new RolesInfoResponse(out.bytes().streamInput());
        } catch (IllegalStateException ex) {
            log.warn("Unable to find action: {}, security plugin is not installed?",RolesInfoAction.INSTANCE);
            return new RolesInfoResponse();
        }
    }

    /**
     *
     * @param authInfoRequest
     * @param restClient
     * @param settings
     * @return
     * @throws IOException
     */
    public final AuthInfoResponse getAuthInfo(AuthInfoRequest authInfoRequest, RestClient restClient, Settings settings) throws IOException {
        if(!settings.getAsBoolean(ConfigConstants.OPENDISTRO_SECURITY_SSL_HTTP_ENABLED, false))
            return new AuthInfoResponse();

        Map<String,String> headers = new HashMap<>();
        headers.put(ConfigConstants.AUTHORIZATION, authInfoRequest.getAuthTokens().get(0)); //fixme:

        Request request = new Request("GET", "/_opendistro/_security/authinfo");
        request.setOptions(RequestOptions.DEFAULT.toBuilder()
                .addHeader(ConfigConstants.CONTENT_TYPE, ConfigConstants.CONTENT_TYPE_DEFAULT)
                .addHeader(ConfigConstants.AUTHORIZATION, authInfoRequest.getAuthTokens().get(0)));

        Response response = restClient.performRequest(request);
        return new AuthInfoResponse(EntityUtils.toString(response.getEntity()));
    }
}
