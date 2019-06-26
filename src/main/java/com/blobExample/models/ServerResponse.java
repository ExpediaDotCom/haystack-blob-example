package com.blobExample.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ServerResponse implements Serializable {

    private String serverName;

    private String message;

    public ServerResponse() {
        serverName = "No ServerResource";
        message = "Hi! I am default server";
    }

    public ServerResponse(String serverName, String message) {
        this.serverName = serverName;
        this.message = message;
    }

    @JsonProperty
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @JsonProperty
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty
    public String getServerName() {
        return serverName;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }
}
