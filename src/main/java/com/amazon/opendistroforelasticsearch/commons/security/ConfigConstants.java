package com.amazon.opendistroforelasticsearch.commons.security;

class ConfigConstants {

    public static final String HTTPS = "https";
    public static final String HTTP = "http";

    public static final String HOST_DEFAULT = "localhost";
    public static final String HTTP_PORT = "http.port";
    public static final String CONTENT_TYPE = "content-type";
    public static final String CONTENT_TYPE_DEFAULT = "application/json";
    public static final String AUTHORIZATION = "Authorization";

    public static final int HTTP_PORT_DEFAULT = 9200;

    public static final String OPENDISTRO_SECURITY_SSL_HTTP_ENABLED = "opendistro_security.ssl.http.enabled";
    public static final String OPENDISTRO_SECURITY_SSL_HTTP_CLIENTAUTH_MODE = "opendistro_security.ssl.http.clientauth_mode";

    public static final String OPENDISTRO_SECURITY_SSL_HTTP_KEYSTORE_ALIAS = "opendistro_security.ssl.http.keystore_alias";
    public static final String OPENDISTRO_SECURITY_SSL_HTTP_KEYSTORE_FILEPATH = "opendistro_security.ssl.http.keystore_filepath";

    public static final String OPENDISTRO_SECURITY_SSL_HTTP_PEMKEY_FILEPATH = "opendistro_security.ssl.http.pemkey_filepath";
    public static final String OPENDISTRO_SECURITY_SSL_HTTP_PEMKEY_PASSWORD = "opendistro_security.ssl.http.pemkey_password";
    public static final String OPENDISTRO_SECURITY_SSL_HTTP_PEMCERT_FILEPATH = "opendistro_security.ssl.http.pemcert_filepath";
    public static final String OPENDISTRO_SECURITY_SSL_HTTP_PEMTRUSTEDCAS_FILEPATH = "opendistro_security.ssl.http.pemtrustedcas_filepath";

    public static final String OPENDISTRO_SECURITY_SSL_HTTP_KEYSTORE_PASSWORD = "opendistro_security.ssl.http.keystore_password";
    public static final String OPENDISTRO_SECURITY_SSL_HTTP_KEYSTORE_KEYPASSWORD = "opendistro_security.ssl.http.keystore_keypassword";
    public static final String OPENDISTRO_SECURITY_SSL_HTTP_KEYSTORE_TYPE = "opendistro_security.ssl.http.keystore_type";

    public static final String OPENDISTRO_SECURITY_SSL_HTTP_TRUSTSTORE_ALIAS = "opendistro_security.ssl.http.truststore_alias";
    public static final String OPENDISTRO_SECURITY_SSL_HTTP_TRUSTSTORE_FILEPATH = "opendistro_security.ssl.http.truststore_filepath";
    public static final String OPENDISTRO_SECURITY_SSL_HTTP_TRUSTSTORE_PASSWORD = "opendistro_security.ssl.http.truststore_password";
    public static final String OPENDISTRO_SECURITY_SSL_HTTP_TRUSTSTORE_TYPE = "opendistro_security.ssl.http.truststore_type";

    public static final String OPENDISTRO_SECURITY_IMPERSONATE_AS = "opendistro_security_impersonate_as";

}
