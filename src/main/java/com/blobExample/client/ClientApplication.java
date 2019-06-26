package com.blobExample.client;

import com.blobExample.BlobsConfiguration;
import com.blobExample.CommonConfiguration;
import com.expedia.blobs.core.BlobStore;
import com.expedia.blobs.stores.io.FileStore;
import com.expedia.www.haystack.agent.blobs.client.AgentClient;
import com.expedia.www.haystack.client.dispatchers.Dispatcher;
import com.expedia.www.haystack.client.dispatchers.RemoteDispatcher;
import com.expedia.www.haystack.client.dispatchers.clients.GRPCAgentClient;
import com.expedia.www.haystack.client.metrics.NoopMetricsRegistry;
import com.expedia.www.haystack.remote.clients.Client;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.io.File;

public class ClientApplication extends Application<CommonConfiguration> {
    private final String FILE_STORE = "FileStore";
    private final String S3_STORE = "S3Store";
    private final String AGENT_STORE = "AgentStore";

    public void start(String[] args) throws Exception {

        String[] newArgs = new String[]{"server", String.format("config-client.yaml")};

        ClientApplication clientApplication = new ClientApplication();
        clientApplication.run(newArgs);
    }

    @Override
    public String getName() {
        return "ClientResource Application";
    }

    @Override
    public void initialize(Bootstrap<CommonConfiguration> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(CommonConfiguration commonConfiguration, Environment environment) throws Exception {
        final javax.ws.rs.client.Client client = new JerseyClientBuilder(environment).using(commonConfiguration.getJerseyClientConfiguration())
                .build(getName() + "ClientRequest");

        Client spanClient = new GRPCAgentClient.Builder(new NoopMetricsRegistry(), "localhost", 34000).build();
        Dispatcher spanDispatcher = new RemoteDispatcher.Builder(new NoopMetricsRegistry(), spanClient).build();

        final ClientResource clientResource = new ClientResource(
                commonConfiguration.getTemplate(),
                commonConfiguration.getDefaultName(),
                client,
                initializeBlobStore(commonConfiguration.getBlobsConfiguration()),
                spanDispatcher
        );
        environment.jersey().register(clientResource);
    }

    private BlobStore initializeBlobStore(BlobsConfiguration blobsConfiguration) {
        if (!blobsConfiguration.getAreBlobsEnabled()) {
            return null;
        }

        BlobsConfiguration.Store store = blobsConfiguration.getStore();
        switch (store.getName()) {
            case FILE_STORE: {
                return null;
            }
            case S3_STORE: {
                return null;
            }
            case AGENT_STORE: {
                return new AgentClient.Builder(store.getHost(), store.getPort()).build();
            }
        }
        return null;
    }
}
