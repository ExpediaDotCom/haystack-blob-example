package com.expedia.www.haystack.dropwizard.example;

import com.expedia.haystack.dropwizard.bundle.Traceable;
import com.expedia.haystack.dropwizard.configuration.TracerFactory;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


public class CommonConfiguration extends Configuration implements Traceable {

    private BlobsConfiguration blobs;

    @Valid
    @NotNull
    private TracerFactory tracerFactory;

    private String serverEndpoint;

    @Valid
    @NotNull
    private JerseyClientConfiguration jerseyClient = new JerseyClientConfiguration();

    @JsonProperty("jerseyClient")
    public JerseyClientConfiguration getJerseyClientConfiguration() {
        return jerseyClient;
    }

    @JsonProperty
    public BlobsConfiguration getBlobs() {
        return blobs;
    }

    @JsonProperty
    public void setBlobs(BlobsConfiguration blobs) {
        this.blobs = blobs;
    }

    @JsonProperty("tracer")
    public TracerFactory getTracerFactory() {
        return tracerFactory;
    }

    @JsonProperty("tracer")
    public void setTracerFactory(TracerFactory tracerFactory) {
        this.tracerFactory = tracerFactory;
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
