package com.blobExample.benchmarking;

import com.blobExample.client.ClientResource;
import com.blobExample.models.ClientRequest;
import com.blobExample.models.ClientResponse;
import com.blobExample.models.ServerResponse;

import java.io.File;
import java.io.IOException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.expedia.blobs.core.BlobsFactory;
import com.expedia.blobs.stores.io.FileStore;
import com.expedia.www.haystack.client.Tracer;
import com.expedia.www.haystack.client.dispatchers.Dispatcher;
import com.expedia.www.haystack.client.dispatchers.RemoteDispatcher;
import com.expedia.www.haystack.client.dispatchers.clients.GRPCAgentClient;
import com.expedia.www.haystack.client.metrics.NoopMetricsRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.mockito.Mockito;
import org.openjdk.jmh.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static org.mockito.Mockito.when;

public class BenchmarkServiceForBlobAttach {

    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkServiceForBlobAttach.class);

    @State(Scope.Benchmark)
    public static class ClientState {

        @Setup(Level.Iteration)
        public void doSetup() {
            LOGGER.info("doSetup Called");

            client = Mockito.mock(Client.class);

            blobStore = setupBlobStore();

            blobsFactory = new BlobsFactory(blobStore);

            clientRequest = new ClientRequest("ClientResource", String.format("Hello ServerResource. I am %s!", "ClientResource"));

            serverResponse = new ServerResponse("BlobServer", "Hi");

            dispatcher = new RemoteDispatcher.Builder(
                    new NoopMetricsRegistry(),
                    new GRPCAgentClient.Builder(new NoopMetricsRegistry(), "localhost", 34001).build())
                    .build();

            objectMapper = new ObjectMapper();
        }

        @TearDown(Level.Iteration)
        public void tearDown() {
            LOGGER.info("Tear down called");
            if (blobStore != null)
                blobStore.close();

            try {
                removeCurrentRunBlobs();
            } catch (IOException ex) {
                LOGGER.error("Could not delete BenchmarkingBlobs Directory");
            }
        }

        private void removeCurrentRunBlobs() throws IOException {
            String userDirectory = System.getProperty("user.dir");
            File file = new File(new String(userDirectory).concat("/BenchmarkingBlobs"));
            FileUtils.deleteDirectory(file);
        }

        private FileStore setupBlobStore() {
            File directory = setupBlobsDirectory();

            FileStore.Builder builder = new FileStore.Builder(directory)
                    .withShutdownWaitInSeconds(360)
                    .disableAutoShutdown();

            return builder.build();
        }

        private File setupBlobsDirectory() {
            String userDirectory = System.getProperty("user.dir");
            String directoryPath = new String(userDirectory).concat("/BenchmarkingBlobs");
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdir();
            }

            return directory;
        }

        public ClientRequest clientRequest;
        public ServerResponse serverResponse;
        private FileStore blobStore;
        private BlobsFactory blobsFactory;
        private Client client;
        private Dispatcher dispatcher;
        private ObjectMapper objectMapper;
    }

    @Benchmark
    @BenchmarkMode({Mode.SampleTime, Mode.AverageTime, Mode.Throughput})
    public ClientResponse blobsEnabled(ClientState state) {

        ClientResource clientResource = new ClientResource(
                state.client,
                new Tracer.Builder(
                        new NoopMetricsRegistry(),
                        "test-blob-client",
                        state.dispatcher).build(),
                state.blobsFactory,
                state.objectMapper);

        setupBehaviour(state, clientResource);

        return clientResource.getMessageFromServer();
    }

    @Benchmark
    @BenchmarkMode({Mode.SampleTime, Mode.AverageTime, Mode.Throughput})
    public ClientResponse blobsDisabled(ClientState state) {

        ClientResource clientResource = new ClientResource(
                state.client,
                new Tracer.Builder(
                        new NoopMetricsRegistry(),
                        "test-blob-client",
                        state.dispatcher).build(),
                null,
                state.objectMapper);

        setupBehaviour(state, clientResource);

        return clientResource.getMessageFromServer();
    }

    private void setupBehaviour(ClientState state, ClientResource clientResource) {
        Response mockedResponse = Mockito.mock(Response.class);
        Invocation.Builder invocationBuilder = Mockito.mock(Invocation.Builder.class);
        WebTarget webTarget = Mockito.mock(WebTarget.class);

        when(clientResource.getJerseyClient().target("http://localhost:9090/hi")).thenReturn(webTarget);

        when(webTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);

        when(invocationBuilder.post(
                Mockito.any(Entity.class)
        )).thenReturn(mockedResponse);

        when(mockedResponse.readEntity(ServerResponse.class)).thenReturn(state.serverResponse);
    }
}
