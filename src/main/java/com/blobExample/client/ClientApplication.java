package com.blobExample.client;

import com.blobExample.BlobsConfiguration;
import com.blobExample.CommonConfiguration;
import com.expedia.blobs.core.BlobContext;
import com.expedia.blobs.core.BlobStore;
import com.expedia.blobs.core.BlobsFactory;
import com.expedia.blobs.core.predicates.BlobsRateLimiter;
import com.expedia.haystack.dropwizard.bundle.HaystackTracerBundle;
import com.expedia.www.haystack.agent.blobs.client.AgentClient;
import io.opentracing.Tracer;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class ClientApplication extends Application<CommonConfiguration> {
    private final String FILE_STORE = "FileStore";
    private final String S3_STORE = "S3Store";
    private final String AGENT_STORE = "AgentStore";

    private final HaystackTracerBundle<CommonConfiguration> haystackTracerBundle = new HaystackTracerBundle<>();

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
        bootstrap.addBundle(this.haystackTracerBundle);
    }

    @Override
    public void run(CommonConfiguration commonConfiguration, Environment environment) throws Exception {
        // the following line registers ClientTracingFeature to trace all
        // outbound service calls
        final Client client = ClientBuilder.newBuilder()
                .register(this.haystackTracerBundle.clientTracingFeature(environment))
                .build();

        final Tracer tracer = environment.jersey().getProperty(Tracer.class.getName());
        final BlobsConfiguration blobsConfiguration = commonConfiguration.getBlobsConfiguration();
        final String serverEndpoint = commonConfiguration.getServerEndpoint();
        final ClientResource clientResource = new ClientResource(
                client,
                tracer,
                serverEndpoint,
                blobsConfiguration.getAreBlobsEnabled() ? createBlobFactory(initializeBlobStore(blobsConfiguration)) : null,
                environment.getObjectMapper());
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

    private BlobsFactory<BlobContext> createBlobFactory(final BlobStore blobStore) {
        BlobsRateLimiter<BlobContext> blobsRateLimiter = createBlobsRateLimiter();

        return new BlobsFactory<>(blobStore, blobsRateLimiter);
    }

    private BlobsRateLimiter<BlobContext> createBlobsRateLimiter() {
        return new BlobsRateLimiter<>(5);
    }
}
