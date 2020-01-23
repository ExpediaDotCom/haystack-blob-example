package com.expedia.www.haystack.dropwizard.example.config;

import com.expedia.haystack.dropwizard.bundle.Traceable;
import com.expedia.haystack.dropwizard.configuration.BlobFactory;
import com.expedia.haystack.dropwizard.configuration.TracerFactory;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


public class AppConfiguration extends Configuration implements Traceable {

    @Valid
    @NotNull
    private TracerFactory tracerFactory;

    @Valid
    @NotNull
    private BlobFactory blobFactory;

    private String serverEndpoint;

    @Valid
    @NotNull
    private JerseyClientConfiguration jerseyClient = new JerseyClientConfiguration();

    @JsonProperty("jerseyClient")
    public JerseyClientConfiguration getJerseyClientConfiguration() {
        return jerseyClient;
    }

    @JsonProperty("tracer")
    public TracerFactory getTracerFactory() {
        return tracerFactory;
    }

    @JsonProperty("tracer")
    public void setTracerFactory(TracerFactory tracerFactory) {
        this.tracerFactory = tracerFactory;
    }

    @JsonProperty("blobs")
    public BlobFactory getBlobFactory() {
        return blobFactory;
    }

    @JsonProperty("blobs")
    public void setBlobFactory() {
        this.blobFactory = blobFactory;
    }

    @JsonProperty
    public String getServerEndpoint() {
        return serverEndpoint;
    }

    @JsonProperty
    public void setServerEndpoint(String serverEndpoint) {
        this.serverEndpoint = serverEndpoint;
    }
}
