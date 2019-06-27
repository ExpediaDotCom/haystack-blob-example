package com.blobExample.models;

import com.blobExample.models.ServerResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ClientResponse implements Serializable {
    private String clientMessage;
    private ServerResponse serverResponse;

    public ClientResponse() {
    }

    public ClientResponse(String clientMessage, ServerResponse serverResponse) {
        this.clientMessage = clientMessage;
        this.serverResponse = serverResponse;
    }

    @JsonProperty
    public String getClientMessage() {
        return clientMessage;
    }

    @JsonProperty
    public ServerResponse getServerResponse() {
        return serverResponse;
    }
}
