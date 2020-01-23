package com.expedia.www.haystack.dropwizard.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ClientResponse implements Serializable {
    private String name;
    private String serverResponse;

    public ClientResponse(String name, String serverResponse) {
        this.name = name;
        this.serverResponse = serverResponse;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public String getServerResponse() {
        return serverResponse;
    }
}
