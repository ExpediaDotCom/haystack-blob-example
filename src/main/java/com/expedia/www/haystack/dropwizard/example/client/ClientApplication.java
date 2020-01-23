package com.expedia.www.haystack.dropwizard.example.client;

import com.expedia.haystack.dropwizard.bundle.HaystackTracerBundle;
import com.expedia.www.haystack.dropwizard.example.config.AppConfiguration;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class ClientApplication extends Application<AppConfiguration> {

    private final HaystackTracerBundle<AppConfiguration> haystackTracerBundle = new HaystackTracerBundle<>();

    @Override
    public String getName() {
        return "client";
    }

    @Override
    public void initialize(Bootstrap<AppConfiguration> bootstrap) {
        bootstrap.addBundle(this.haystackTracerBundle);
    }

    @Override
    public void run(AppConfiguration appConfiguration, Environment environment) throws Exception {
        // the following line registers ClientTracingFeature to trace all
        // outbound service calls
        final Client client = ClientBuilder.newBuilder()
                .register(this.haystackTracerBundle.clientTracingFeature(environment))
                .build();

        final String serverEndpoint = appConfiguration.getServerEndpoint();
        final ClientResource clientResource = new ClientResource(client, serverEndpoint);
        environment.jersey().register(clientResource);
    }
}
