package com.amazon.opendistroforelasticsearch.commons.security;

import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.common.xcontent.json.JsonXContent;

import java.util.Map;

/**
 * Response of /_opendistro/_security/authinfo api.
 *
 */
public class AuthInfoResponse {
    private String user_name;
    private String user_requested_tenant;
    private String remote_address;


    public AuthInfoResponse(String json) {
        Map<String, Object> mapValue = XContentHelper.convertToMap(JsonXContent.jsonXContent, json, false);
        user_name = (String)mapValue.get("user_name");
    }

    public String getUserName(){
        return user_name;
    }
}
