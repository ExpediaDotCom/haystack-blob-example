package com.expedia.www.haystack.dropwizard.example.server;


import com.expedia.www.haystack.dropwizard.example.models.ClientRequest;
import com.expedia.www.haystack.dropwizard.example.models.ServerResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/hi")
@Produces(MediaType.APPLICATION_JSON)
public class ServerResource {

    @POST
    @Consumes("application/json")
    public ServerResponse receiveHi(ClientRequest request){
        final String message = String.format("Hi %s! I am %s!", request.getName(), "ServerResource");
        return new ServerResponse("ServerResource", message);
    }
}