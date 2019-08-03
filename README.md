## Haystack Blob Example

This application demonstrates the use of Spans and Blobs together to be used by [Haystack-Agent](https://github.com/ExpediaDotCom/haystack-agent). This will show how the metadata for a blob stored could be saved as a tag in a span to be used later for reading them again.

In this example, the client sends a request to the server which then sends the response back to it. Both the request and response are saved as a blob at the client side and then their key is added to the span as a tag. This tag can be further used by haystack to retrieve the blob.

## Understanding the implementation
To understand how span tracing works and is implemented please refer [haystack-dropwizard-example](https://github.com/ExpediaDotCom/haystack-dropwizard-example).

For blobs you need to first setup [Blob Store](/core/src/main/java/com/expedia/blobs/core/BlobStore.java) and [Blob Factory](/core/src/main/java/com/expedia/blobs/core/BlobsFactory.java).
Refer [this](https://github.com/ExpediaDotCom/haystack-blob-example/blob/master/src/main/java/com/blobExample/client/ClientApplication.java#L56) to have complete understanding of how to initialize them.

Once the blob store and factory are in place you can use them whenever you need to get a [Blob Writer](https://github.com/ExpediaDotCom/blobs/blob/master/core/src/main/java/com/expedia/blobs/core/BlobWriter.java). The type of writer returned depends on the predicate you provided(if any) during the initialization of Blob Factory. If the predicate satisfies then you will get the [BlobWriterImpl](https://github.com/ExpediaDotCom/blobs/blob/master/core/src/main/java/com/expedia/blobs/core/BlobWriterImpl.java) else you will get a [No Operation Writer](https://github.com/ExpediaDotCom/blobs/blob/master/core/src/main/java/com/expedia/blobs/core/NoOpBlobWriterImpl.java).

Writing a blob to a store is the most easy step for which you can refer [this](https://github.com/ExpediaDotCom/haystack-blob-example/blob/master/src/main/java/com/blobExample/client/ClientResource.java#L66) piece of code.

##### Point to remember: The Blob Store and Blob Factory should be initiated only once.
 
 ## Running this example
 
 To enable the recording of the blobs, set _`enabled`_ to true in _`config-client.yaml`_, run the client and server again and hit the above URL.
 
 #### Build
 
 Required:
 *  Java 1.8
 
  Build:

```mvn clean package```
 
 #### Run locally without haystack server intact (Just for debugging purpose)
 
* Please replace the following store configurations for blobs in the `config-client.yml`:
    ```
    name: AgentStore
    host: haystack-agent
    port: 34001
    ```
    with 
    ```
    name: FileStore
    ```

    You can find the blobs stored inside `/blobs` folder of your application's directory.

* Also, remove the following configurations in tracer's dispatchers:
    ```
    - type: remote
        client:
            type: agent
            host: haystack-agent
            format:
                type: protobuf
    ```

    You will be able to see your spans getting published as logs.
    
* Replace `serverEndpoint: http://haystack-blob-example-server:9090/hi` with `serverEndpoint: http://localhost:9090/hi` in the same config file.
    
* Re-build the application again

Post build use the following commands to:

 * Run the client on [localhost:9091](http://localhost:9091)

    ```java -jar target/haystack-blob-example-service-1.0-SNAPSHOT.jar client```

 * Run the server on [localhost:9090](http://localhost:9090)

    ```java -jar target/haystack-blob-example-service-1.0-SNAPSHOT.jar server```
 
 * Send a sample request:
 
    ```curl http://localhost:9091/displayMessage```
    
Look for the meta-tags `request-blob` and `response-blob` in the request and response spans respectively. 
    
    
#### Run locally with haystack server intact 

To run the complete example properly please refer steps given in [haystack-docker](https://github.com/ExpediaDotCom/haystack-docker) for [spans-and-blobs](https://github.com/ExpediaDotCom/haystack-docker/tree/master/example). This will also start [haystack-agent](https://github.com/ExpediaDotCom/haystack-agent) at port `34001` and [haystack-ui](https://github.com/ExpediaDotCom/haystack-ui) at port `8080` locally along with the http [reverse-proxy](https://github.com/ExpediaDotCom/blobs/tree/master/haystack-blobs) at port `34002` for grpc service.

After running the docker you can test the usage by following the given steps:

 * Send a sample request:
 
    ```curl http://localhost:9091/displayMessage```
    
    One can also use the sample script we have to send more requests to the server application and see metrics such as count, duration histogram etc in Haystack UI under trends.
    ```
    ./run.sh
    ```
    
 * Open Haystack UI at http://localhost:8080/ and search for `serviceName=test-blob-client` to see the traces.

* Open the trace and look for `request-blob` and `response-blob` tags.