package com.expedia.www.haystack.dropwizard.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ClientRequest implements Serializable {
    private String name;
    private String message;

    public ClientRequest(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public ClientRequest(){
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty
    public String getMessage() {
        return message;
    }

    @JsonProperty
    public void setMessage(String message) {
        this.message = message;
    }
}
