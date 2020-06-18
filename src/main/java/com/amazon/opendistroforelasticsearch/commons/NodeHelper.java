package com.amazon.opendistroforelasticsearch.commons;


import com.amazon.opendistroforelasticsearch.security.rolesinfo.RolesInfoAction;
import com.amazon.opendistroforelasticsearch.security.rolesinfo.RolesInfoRequest;
import com.amazon.opendistroforelasticsearch.security.rolesinfo.RolesInfoResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.io.stream.BytesStreamOutput;

import java.io.IOException;


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
}
