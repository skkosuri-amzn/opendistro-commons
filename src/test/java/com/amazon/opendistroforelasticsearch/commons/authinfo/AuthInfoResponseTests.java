package com.amazon.opendistroforelasticsearch.commons.authinfo;

import org.junit.Test;
import org.junit.Assert;

public class AuthInfoResponseTests {


    final static String sample = "{\n" +
            "  \"user\": \"User [name=admin, backend_roles=[admin], requestedTenant=null]\",\n" +
            "  \"user_name\": \"admin\",\n" +
            "  \"user_requested_tenant\": null,\n" +
            "  \"remote_address\": \"127.0.0.1:37592\",\n" +
            "  \"backend_roles\": [\n" +
            "    \"admin\"\n" +
            "  ],\n" +
            "  \"custom_attribute_names\": [],\n" +
            "  \"roles\": [\n" +
            "    \"all_access\",\n" +
            "    \"own_index\"\n" +
            "  ],\n" +
            "  \"tenants\": {\n" +
            "    \"global_tenant\": true,\n" +
            "    \"admin_tenant\": true,\n" +
            "    \"admin\": true\n" +
            "  },\n" +
            "  \"principal\": null,\n" +
            "  \"peer_certificates\": \"0\",\n" +
            "  \"sso_logout_url\": null\n" +
            "}";
    @Test
    public void testAuthInfo(){
        AuthInfoResponse info = new AuthInfoResponse(sample);
        Assert.assertEquals("admin", info.getUserName());
        Assert.assertEquals(null, info.getUserRequestedTenant());
        Assert.assertEquals("127.0.0.1:37592", info.getRemoteAddress());
        Assert.assertEquals(1, info.getBackendRoles().size());
        Assert.assertEquals(2, info.getRoles().size());
        Assert.assertEquals("all_access,own_index", info.getRolesString());
    }
}