package com.amazon.opendistroforelasticsearch.commons;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.util.concurrent.ThreadContext;

import static com.amazon.opendistroforelasticsearch.commons.ConfigConstants.OPENDISTRO_SECURITY_INJECT_ROLE;
import static com.amazon.opendistroforelasticsearch.commons.ConfigConstants.OPENDISTRO_SECURITY_INJECT_ROLE_ENABLED;

/**
 * For background jobs usage only. Roles injection can be done using transport layer only.
 * You can't inject roles using REST api.
 *
 */
public class RoleInjectorHelper implements AutoCloseable {

    private String id;
    private ThreadContext.StoredContext ctx = null;
    private ThreadContext threadContext;
    private Settings settings;
    private final Logger log = LogManager.getLogger(this.getClass());

    public RoleInjectorHelper(String id, Settings settings, ThreadContext tc) {
        this.id = id;
        this.settings = settings;
        this.threadContext = tc;

        if(isEnabled()) {
            this.ctx = tc.newStoredContext(true);
            log.debug("{}, RoleInjectorHelper constructor: {}", Thread.currentThread().getName(), id);
        }
    }

    private boolean isEnabled() {
        return "true".equals(settings.get(OPENDISTRO_SECURITY_INJECT_ROLE_ENABLED));
    }

    public void injectRoles(String injectRole) {
        if(!isEnabled()) {
            return;
        }

        if(threadContext.getTransient(OPENDISTRO_SECURITY_INJECT_ROLE) == null) {
            threadContext.putTransient(OPENDISTRO_SECURITY_INJECT_ROLE, injectRole);
            log.debug("{}, RoleInjectorHelper - inject roles: {}", Thread.currentThread().getName(), id);
        } else {
            log.error("{}, RoleInjectorHelper- most likely thread context corruption : {}",
                    Thread.currentThread().getName(), id);
        }
    }

    @Override
    public void close() {
        if(ctx !=null) {
            ctx.close();
            log.debug("{}, RoleInjectorHelper close : {}", Thread.currentThread().getName(), id);
        }
    }
}
