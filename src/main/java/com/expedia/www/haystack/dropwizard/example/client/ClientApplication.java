package com.expedia.www.haystack.dropwizard.example.client;

import com.expedia.blobs.stores.io.FileStore;
import com.expedia.www.haystack.dropwizard.example.BlobsConfiguration;
import com.expedia.www.haystack.dropwizard.example.CommonConfiguration;
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
import java.io.File;

public class ClientApplication extends Application<CommonConfiguration> {
    private final String FILE_STORE = "FileStore";
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
        final BlobsConfiguration blobsConfiguration = commonConfiguration.getBlobs();
        final String serverEndpoint = commonConfiguration.getServerEndpoint();
        final ClientResource clientResource = new ClientResource(
                client,
                tracer,
                serverEndpoint,
                blobsConfiguration.isEnabled() ? createBlobFactory(initializeBlobStore(blobsConfiguration)) : null,
                environment.getObjectMapper());
        environment.jersey().register(clientResource);
    }

    private BlobStore initializeBlobStore(BlobsConfiguration blobsConfiguration) {
        if (!blobsConfiguration.isEnabled()) {
            return null;
        }

        BlobsConfiguration.Store store = blobsConfiguration.getStore();
        switch (store.getName()) {
            case FILE_STORE: {
                String userDirectory = System.getProperty("user.dir");
                String directoryPath = new String(userDirectory).concat("/blobs");
                File directory = new File(directoryPath);
                if (!directory.exists()) {
                    directory.mkdir();
                }
                return new FileStore.Builder(directory).build();
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
