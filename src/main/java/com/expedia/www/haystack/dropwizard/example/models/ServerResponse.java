package com.expedia.www.haystack.dropwizard.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ServerResponse implements Serializable {

    private String message;

    public ServerResponse() {
        message = "Hi! I am default server";
    }

    public ServerResponse(String message) {
        this.message = message;
    }

    @JsonProperty
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }
}
