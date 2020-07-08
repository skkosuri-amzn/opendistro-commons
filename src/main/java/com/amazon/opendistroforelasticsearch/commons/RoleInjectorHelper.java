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
 * Java example Usage:
 *
 *      try (RoleInjectorHelper roleInjectorHelper = new RoleInjectorHelper(id,
 *                                      settings, client.threadPool().getThreadContext())) {
 *
 *          //add roles to be injected from the configuration.
 *          roleInjectorHelper.injectRoles("role_1,role_2");
 *
 *          //Elasticsearch calls that needs to executed in security context.
 *
 *          SearchRequestBuilder searchRequestBuilder = client.prepareSearch(monitor.indexpattern);
 *          SearchResponse searchResponse = searchRequestBuilder
 *                             .setFrom(0).setSize(100).setExplain(true).  execute().actionGet();
 *
 *      } catch (final ElasticsearchSecurityException ex){
 *            //handle the security exception
 *      }
 *
 * Kotlin usage with Coroutines:
 *
 *    //You can also use launch, based on usecase.
 *    runBlocking(RolesInjectorContextElement(monitor.id, settings, threadPool.threadContext, monitor.associatedRoles)) {
 *       //Elasticsearch calls that needs to executed in security context.
 *    }
 *
 *    class RolesInjectorContextElement(val id: String, val settings: Settings, val threadContext: ThreadContext, val roles: String)
 *     : ThreadContextElement<Unit> {
 *
 *     companion object Key : CoroutineContext.Key<RolesInjectorContextElement>
 *     override val key: CoroutineContext.Key<*>
 *         get() = Key
 *
 *     var rolesInjectorHelper = RoleInjectorHelper(id, settings, threadContext)
 *
 *     override fun updateThreadContext(context: CoroutineContext) {
 *         rolesInjectorHelper.injectRoles(roles)
 *     }
 *
 *     override fun restoreThreadContext(context: CoroutineContext, oldState: Unit) {
 *         rolesInjectorHelper.close()
 *     }
 *   }
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
