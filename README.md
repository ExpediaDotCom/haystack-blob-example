## Haystack Blob Example

This application demonstrates the use of Spans and Blobs together to be used by [Haystack-Agent](https://github.com/ExpediaDotCom/haystack-agent). This will show how the metadata for a blob stored could be saved as a tag in a span to be used later for reading them again.

In this example, the client sends a request to the server which then sends the response back to it. Both the request and response are saved as a blob at the client side and then their key is added to the span as a tag. This tag can be further used by haystack to retrieve the blob.

## Understanding the implementation
To understand how span tracing works and is implemented please refer [haystack-dropwizard-example](https://github.com/ExpediaDotCom/haystack-dropwizard-example).
For adding the request/response blobs along with tracing, you follow the same steps as tracing example, additionally you need to provide blob configuration similar to:

```yaml

blobs:
  enabled: true
  store:
   name: file
```
And update AppConfiguration to return the [BlobFactory](https://github.com/ExpediaDotCom/haystack-blob-example/blob/master/src/main/java/com/expedia/www/haystack/dropwizard/example/config/AppConfiguration.java#L22) instance.

#### How to blob conditionally?
There may be a valid usecase where you don't want to blob request/response with every span, may be for performance reasons.
But you may want to turn on the blobs only if you see a X-DEBUG header from your upstream request. In order to do this, 
you can implement these functions and setBlobable in BlobFactory. By default, they are set to true.

```java
    boolean isServerRequestValidForBlob(ContainerRequestContext req);
    boolean isServerResponseValidForBlob(ContainerResponseContext resp);
    boolean isClientRequestValidForBlob(ClientRequestContext req);
    boolean isClientResponseValidForBlob(ClientResponseContext resp);
```

## Dependencies involved

We use [haystack-dropwizard](https://github.com/ExpediaDotCom/haystack-dropwizard) library version >= 0.3.1 that does all the heavy lifting for adding the support of blob feature.
 
 ## Running this example
  
 #### Build
 
 Required:
 *  Java 1.8
 
  Build:

```mvn clean package```
 
#### Run locally without haystack-agent (Just for debugging purpose)
 * Run the client on [localhost:9091](http://localhost:9091)

    ```java -jar target/haystack-blob-example-service-1.0-SNAPSHOT.jar client config-client-local.yaml```

 * Run the server on [localhost:9090](http://localhost:9090)

    ```java -jar target/haystack-blob-example-service-1.0-SNAPSHOT.jar server config-server-local.yaml```

The spans gets logged on the console and blobs will be written under '/blobs' directory in your working directory.

#### Run locally with haystack-agent (mostly for production)

 * Run the client on [localhost:9091](http://localhost:9091)

    ```java -jar target/haystack-blob-example-service-1.0-SNAPSHOT.jar client config-client.yaml```

 * Run the server on [localhost:9090](http://localhost:9090)

    ```java -jar target/haystack-blob-example-service-1.0-SNAPSHOT.jar server config-server.yaml```
 

#### Test Blob Feature 
  * Send a sample request:
 
    ```curl http://localhost:9091/message```
    
Look for the meta-tags `request-blob` and `response-blob` in the request and response spans respectively.
      
#### Run locally with haystack server intact 

To run the complete example properly please refer steps given in [haystack-docker](https://github.com/ExpediaDotCom/haystack-docker) for [spans-and-blobs](https://github.com/ExpediaDotCom/haystack-docker/tree/master/example). This will also start [haystack-agent](https://github.com/ExpediaDotCom/haystack-agent) at port `35001` and [haystack-ui](https://github.com/ExpediaDotCom/haystack-ui) at port `8080` locally along with the http [reverse-proxy](https://github.com/ExpediaDotCom/blobs/tree/master/haystack-blobs) at port `35002` for grpc service.

After running the docker you can test the usage by following the given steps:

 * Send a sample request:
 
    ```curl http://localhost:9091/message```
    
    One can also use the sample script we have to send more requests to the server application and see metrics such as count, duration histogram etc in Haystack UI under trends.
    ```
    ./run.sh
    ```
    
 * Open Haystack UI at http://localhost:8080/ and search for `serviceName=blob-client` to see the traces.

* Open the trace and look for `request-blob` and `response-blob` tags.