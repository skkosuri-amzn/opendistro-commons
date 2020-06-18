### opendistro-commons


Collection of utilities for Elasticsearch plugin development. Currently, it has:

1. RestHelper: Contains methods to create secure REST clients to Elasticsearch. 

2. NodeHelper: Contains api's to make transport action calls across plugins. To make transport calls possible, 
the Request, Response, Action classes needs to shared. This commons library is used to host those shared classes.

3. RoleInjectorHelper: Used to inject roles for the background jobs.   



##### BUILD

```
./gradlew clean
./gradlew build 
./gradlew publishToMavenLocal
```

##### Logging
Add below to `config/log4j2.properties`
```
logger.commons.name = com.amazon.opendistroforelasticsearch.commons
logger.commons.level = debug
```

##### Backlog

1. Complete todo's.
1. Use 'org.elasticsearch.test' to test NodeHelper and RestHelper
1. Add code style.
