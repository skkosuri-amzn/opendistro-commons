package com.amazon.opendistroforelasticsearch.commons.security;

import java.util.List;

/**
 * Request for /_opendistro/_security/authinfo api.
 */
public class AuthInfoRequest {

    private final List<String> auth;
    public AuthInfoRequest(List<String> authToken) {
        auth = authToken;
    }

    public List<String> getAuthTokens() {
        return auth;
    }
}
