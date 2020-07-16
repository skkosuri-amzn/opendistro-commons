package com.amazon.opendistroforelasticsearch.commons;

public class ConfigConstants {

    public static final String HTTPS = "https";
    public static final String HTTP = "http";
    public static final String HOST_DEFAULT = "localhost";
    public static final String HTTP_PORT = "http.port";
    public static final int HTTP_PORT_DEFAULT = 9200;
    public static final String CONTENT_TYPE = "content-type";
    public static final String CONTENT_TYPE_DEFAULT = "application/json";
    public static final String AUTHORIZATION = "authorization";

    //These reside in security plugin.
    public static final String OPENDISTRO_SECURITY_INJECT_ROLE_ENABLED = "opendistro_security_injected_role_enabled";
    public static final String OPENDISTRO_SECURITY_INJECT_ROLE = "opendistro_security_injected_role";
    public static final String OPENDISTRO_SECURITY_USER = "_opendistro_security_user";
    public static final String OPENDISTRO_SECURITY_SSL_HTTP_ENABLED = "opendistro_security.ssl.http.enabled";
}
