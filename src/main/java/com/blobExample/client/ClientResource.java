package com.blobExample.client;

import com.blobExample.models.ClientRequest;
import com.blobExample.models.ClientResponse;
import com.blobExample.models.ServerResponse;
import com.codahale.metrics.annotation.Timed;
import com.expedia.blobs.core.*;
import com.expedia.www.haystack.client.Span;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.opentracing.Tracer;
import org.eclipse.microprofile.opentracing.Traced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;


@Path("/displayMessage")
@Produces(MediaType.APPLICATION_JSON)
public class ClientResource {

    private final Tracer tracer;
    private final BlobsFactory<BlobContext> blobFactory;
    private Client jerseyClient;
    private final ObjectMapper objectMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientResource.class);

    public ClientResource(Client jerseyClient,
                          Tracer tracer,
                          BlobsFactory<BlobContext> blobFactory,
                          ObjectMapper objectMapper) {
        this.jerseyClient = jerseyClient;
        this.tracer = tracer;
        this.blobFactory = blobFactory;
        this.objectMapper = objectMapper;
    }

    public Client getJerseyClient() {
        return jerseyClient;
    }

    @GET
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    @Traced(operationName = "/message")
    public ClientResponse getMessageFromServer() {

        final String clientName = "ClientResource";
        final String message = String.format("Hello ServerResource. I am %s!", clientName);
        final ClientRequest clientRequest = new ClientRequest(clientName, message);

        final SpanBlobContext blobContext = new SpanBlobContext((Span) this.tracer.activeSpan());

        if (blobFactory != null) {
            BlobWriter requestBlobWriter = getBlobWriter(blobContext);
            writeBlob(requestBlobWriter, clientRequest, Collections.emptyMap(), BlobType.REQUEST);
        }

        WebTarget webTarget = jerseyClient.target("http://localhost:9090/hi");
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
        Response response = invocationBuilder.post(
                Entity.entity(
                        clientRequest,
                        MediaType.APPLICATION_JSON_TYPE
                )
        );
        ServerResponse serverResponse = response.readEntity(ServerResponse.class);


        if (blobFactory != null) {
            BlobWriter responseBlobWriter = getBlobWriter(blobContext);
            writeBlob(responseBlobWriter, serverResponse, Collections.emptyMap(), BlobType.RESPONSE);
        }

        return new ClientResponse(message, serverResponse);
    }

    private void writeBlob(BlobWriter blobWriter, Object data, Map<String, String> blobMetadata, BlobType blobType) {
        blobWriter.write(blobType,
                ContentType.JSON,
                (outputStream) -> {
                    try {
                        outputStream.write(objectMapper.writeValueAsBytes(data));
                    } catch (IOException e) {
                        LOGGER.error("Exception occured while writing data to stream for preparing blob", e);
                    }
                },
                m -> blobMetadata.forEach(m::add)
        );
    }

    private BlobWriter getBlobWriter(BlobContext blobContext) {
        return this.blobFactory.create(blobContext);
    }
}
