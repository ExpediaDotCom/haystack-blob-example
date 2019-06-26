package com.blobExample.server;


import com.blobExample.models.ClientRequest;
import com.blobExample.models.ServerResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/hi")
@Produces(MediaType.APPLICATION_JSON)
public class ServerResource {
    private final String template;
    private final String defaultName;

    public ServerResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
    }

    @POST
    @Consumes("application/json")
    public ServerResponse receiveHi(ClientRequest request){
        final String message = String.format(template, request.getName(), defaultName);
        return new ServerResponse(defaultName, message);
    }

}
