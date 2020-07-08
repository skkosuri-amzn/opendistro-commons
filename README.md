### opendistro-commons


#### Purpose 

The main purpose of this repository is to provide common utilities for Elasticsearch plugins.
This library include utilities:

1. To create REST clients.
1. Apply fine-grained-access-control to plugins.
1. To add shared Request/Response/Action classes used for plugin to plugin transport layer calls. 
    


#### Usage

1. To secure background jobs
    1. Save user-name and roles associated with user as part of your data types configuration.  
       For example: Roles to which logged-on user belongs to are saved as part of an AlertMonitor definition, 
       a dashboard definition, an AnomalyDetector definition, ...
       
       ```
       NodeHelper.getRolesInfo(NodeClient) : RolesInfoResponse
       ```
       This uses transport layer to get the roles. You can also use REST call.
        
    1. In background job code, enclose the code that calls Elasticsearch using RoleInjectorHelper to apply fgac 
        based on roles associated with the resource created in Step #1.
        
        ```
        try (RoleInjectorHelper roleInjectorHelper = new RoleInjectorHelper(id,
                                        settings, client.threadPool().getThreadContext())) {
            //add roles to be injected from the configuration.
            roleInjectorHelper.injectRoles("role_1,role_2");
            
            //Elasticsearch calls that needs to executed in security context.
            SearchRequestBuilder searchRequestBuilder = client.prepareSearch(monitor.indexpattern);
            SearchResponse searchResponse = searchRequestBuilder
                                      .setFrom(0).setSize(100).setExplain(true).  execute().actionGet();
         
         } catch (final ElasticsearchSecurityException ex){
            //handle the security exception
         }
        ```

1. To secure cross plugin calls: 
    1. Method 1: Use REST(recommended) calls by passing ‘authorization’ header 
    
    ```
        RestHelper restHelper = new RestHelper(settings);
        RestClient restClient = restHelper.createRestClientBuilder().build();
        
        Request request = new Request("POST",  "/alerting/monitor");
        RequestOptions.Builder builder = restHelper.getDefaultRequestOptions().toBuilder();
        builder.addHeader("Authorization", "my-secret-auth-code");
        request.setOptions(builder);
        
        Response response = restClient.performRequest(request);
    ```
    
    2. Method 2: Use transport layer call. You need a shared library and make sure ThreadContext is 
    passed correctly. You can use this library to add Action/Request/Response classes to enable cross 
    plugin transport layer calls.



##### BUILD

```
./gradlew clean
./gradlew build 

./gradlew publishToMavenLocal
```

##### Logging
To change loglevel, add below to `config/log4j2.properties` or use REST to set.
```
logger.commons.name = com.amazon.opendistroforelasticsearch.commons
logger.commons.level = debug
```

