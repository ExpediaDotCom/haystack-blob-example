package com.expedia.www.haystack.dropwizard.example.server;

import com.expedia.www.haystack.dropwizard.example.models.ClientRequest;
import com.expedia.www.haystack.dropwizard.example.models.ServerResponse;
import org.eclipse.microprofile.opentracing.Traced;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class ServerResource {

    @POST
    @Consumes("application/json")
    @Traced(operationName = "hello")
    public ServerResponse process(final ClientRequest request){
        final String message = String.format("Hi %s! I am %s!", request.getName(), "Backend");
        return new ServerResponse(message);
    }
}
