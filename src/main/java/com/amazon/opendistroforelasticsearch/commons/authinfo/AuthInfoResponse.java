package com.amazon.opendistroforelasticsearch.commons.authinfo;

import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.common.xcontent.json.JsonXContent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Response of /_opendistro/_security/authinfo api.
 *
 */
public class AuthInfoResponse {
    private String userName = "";
    private String userRequestedTenant = "";
    private String remoteAddress = "";
    private ArrayList<String> backendRoles = new ArrayList<>();
    private ArrayList<String> roles = new ArrayList<>();

    public AuthInfoResponse() {
        //empty
    }

    public AuthInfoResponse(String json) {
        Map<String, Object> mapValue = XContentHelper.convertToMap(JsonXContent.jsonXContent, json, false);
        userName = (String)mapValue.get("user_name");
        userRequestedTenant = (String)mapValue.get("user_requested_tenant");
        remoteAddress = (String)mapValue.get("remote_address");
        backendRoles = (ArrayList<String>)mapValue.get("backend_roles");
        roles = (ArrayList<String>)mapValue.get("roles");
    }

    public String getUserName() {
        return userName;
    }

    public String getRolesString() {
        return (roles.size() == 0) ? "" : String.join(",", roles);
    }

    public String getUserRequestedTenant() {
        return userRequestedTenant;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public List<String> getBackendRoles() {
        return backendRoles;
    }

    public List<String> getRoles() {
        return roles;
    }
}