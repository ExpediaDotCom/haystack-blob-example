package com.expedia.www.haystack.dropwizard.example.client;

import com.codahale.metrics.annotation.Timed;
import com.expedia.www.haystack.dropwizard.example.models.ClientRequest;
import com.expedia.www.haystack.dropwizard.example.models.ClientResponse;
import com.expedia.www.haystack.dropwizard.example.models.ServerResponse;
import org.eclipse.microprofile.opentracing.Traced;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

@Path("/message")
@Produces(MediaType.APPLICATION_JSON)
public class ClientResource {

    private Client jerseyClient;
    private final String serverEndpoint;

    public ClientResource(final Client jerseyClient,
                          final String serverEndpoint) {
        this.jerseyClient = jerseyClient;
        this.serverEndpoint = serverEndpoint;
    }

    @GET
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    @Traced(operationName = "message")
    public ClientResponse process() throws Exception {
        final String clientName = "Frontend";
        final String message = String.format("Hello Backend. I am %s!", clientName);
        final ClientRequest clientRequest = new ClientRequest(clientName, message);

        final ServerResponse response = jerseyClient.target(serverEndpoint)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(
                        Entity.entity(
                        clientRequest,
                        MediaType.APPLICATION_JSON_TYPE)
                )
                .readEntity(ServerResponse.class);

        return new ClientResponse(clientName + "-server", response.getMessage());
    }
}
