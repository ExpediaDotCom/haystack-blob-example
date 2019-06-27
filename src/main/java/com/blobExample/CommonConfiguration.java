package com.blobExample;

import com.expedia.haystack.dropwizard.bundle.Traceable;
import com.expedia.haystack.dropwizard.configuration.TracerFactory;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


public class CommonConfiguration extends Configuration implements Traceable {

    private BlobsConfiguration blobsConfiguration;

    @Valid
    @NotNull
    private TracerFactory tracerFactory;

    @Valid
    @NotNull
    private JerseyClientConfiguration jerseyClient = new JerseyClientConfiguration();

    @JsonProperty("jerseyClient")
    public JerseyClientConfiguration getJerseyClientConfiguration() {
        return jerseyClient;
    }

    @JsonProperty
    public BlobsConfiguration getBlobsConfiguration() {
        return blobsConfiguration;
    }

    @JsonProperty
    public void setBlobsConfiguration(BlobsConfiguration blobsConfiguration) {
        this.blobsConfiguration = blobsConfiguration;
    }

    @JsonProperty("tracer")
    public TracerFactory getTracerFactory() {
        return tracerFactory;
    }

    @JsonProperty("tracer")
    public void setTracerFactory(TracerFactory tracerFactory) {
        this.tracerFactory = tracerFactory;
    }
}
