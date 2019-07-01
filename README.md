## Span-Blob Example

This application demonstrates the use of Spans and Blobs together to be used by [Haystack-Agent](https://github.com/ExpediaDotCom/haystack-agent). This will show how the metadata for a blob stored could be saved as a tag in a span to be used later for reading them again.

In this example, the client sends a request to the server which then sends the response back to it. Both the request and response are saved as a blob at the client side and then their key is added to the span as a tag. This tag can be further used by haystack to retrieve the blob.


To understand how span tracing works please refer [haystack-dropwizard-example](https://github.com/ExpediaDotCom/haystack-dropwizard-example).
 
 ## Running this example
 
 To enable the recording of the blobs, set _`areBlobsEnabled`_ to true in _`config-client.yaml`_, run the client and server again and hit the above URL.
 
 #### Build
 
 Required:
 *  Java 1.8
 
  Build:

```mvn clean package```
 
 #### Run with Haystack server
 
 To start haystack and agent locally, one can follow the instructions at https://github.com/ExpediaDotCom/haystack-docker#to-start-haystacks-traces-trends-and-service-graph

After starting Haystack server, run this example with the following commands.

Post build use the following commands to:

 * Run the client on [localhost:9091](http://localhost:9091)

    ```java -jar target/blobExample-service-1.0-SNAPSHOT.jar sampleClient```

 * Run the server on [localhost:9090](http://localhost:9090)

    ```java -jar target/blobExample-service-1.0-SNAPSHOT.jar sampleServer```
 
 * Send a sample request:
 
    ```curl http://localhost:9091/displayMessage```
    
And then open Haystack UI at http://localhost:8080/ and search for `serviceName=test-blob-client` to see the traces.

Open the trace and look for `request-blob` and `response-blob` tags.


 
 