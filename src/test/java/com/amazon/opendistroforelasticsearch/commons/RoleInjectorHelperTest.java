package com.amazon.opendistroforelasticsearch.commons;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.util.concurrent.ThreadContext;
import org.junit.Assert;
import org.junit.Test;

import static com.amazon.opendistroforelasticsearch.commons.ConfigConstants.*;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;

public class RoleInjectorHelperTest {

    private final Logger log = LogManager.getLogger(this.getClass());
    @Test
    public void testSecurityHelperEmpty() {
        ThreadContext tc = new ThreadContext(Settings.EMPTY);
        try(RoleInjectorHelper helper = new RoleInjectorHelper("test-name", Settings.EMPTY,tc)) {
            helper.injectRoles("test-role");
                //to auto-close
        }
        Assert.assertNull(tc.getTransient(OPENDISTRO_SECURITY_INJECTED_ROLES));
    }

    @Test
    public void testSecurityHelperWithCtx() {
        Settings settings = Settings.builder().put(OPENDISTRO_SECURITY_INJECTED_ROLES_ENABLED,"true").build();
        Settings headerSettings = Settings.builder().put("request.headers.default", "1").build();
        ThreadContext threadContext = new ThreadContext(headerSettings);
        threadContext.putHeader("name", "opendistro");
        threadContext.putTransient("ctx.name", "plugin");

        assertEquals("1", threadContext.getHeader("default"));
        assertEquals("opendistro", threadContext.getHeader("name"));
        assertEquals("plugin", threadContext.getTransient("ctx.name"));

        try (RoleInjectorHelper helper = new RoleInjectorHelper("test-name", settings, threadContext)) {
            helper.injectRoles("test-role");
            assertEquals("1", threadContext.getHeader("default"));
            assertEquals("opendistro", threadContext.getHeader("name"));
            assertEquals("plugin", threadContext.getTransient("ctx.name"));
            assertNotNull(threadContext.getTransient(OPENDISTRO_SECURITY_INJECTED_ROLES));
            assertEquals("test-role", threadContext.getTransient(OPENDISTRO_SECURITY_INJECTED_ROLES));
        }
        assertEquals("1", threadContext.getHeader("default"));
        assertEquals("opendistro", threadContext.getHeader("name"));
        assertEquals("plugin", threadContext.getTransient("ctx.name"));
        assertNull(threadContext.getTransient(OPENDISTRO_SECURITY_INJECTED_ROLES));
    }
}
