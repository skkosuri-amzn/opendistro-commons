package com.amazon.opendistroforelasticsearch.security.rolesinfo;

import org.elasticsearch.action.ActionType;

/**
 * Used by TransportRolesInfoAction in opendistro_security plugin.
 */
public class RolesInfoAction extends ActionType<RolesInfoResponse> {

    /*
     * todo: Use a better name for the permission.
     * something like "cluster:admin/opendistro_security/rolesinfo"
     */
    public static final String NAME = "indices:monitor";
    public static final RolesInfoAction INSTANCE = new RolesInfoAction();

    private RolesInfoAction() {
        super(NAME, RolesInfoResponse::new);
    }
}

