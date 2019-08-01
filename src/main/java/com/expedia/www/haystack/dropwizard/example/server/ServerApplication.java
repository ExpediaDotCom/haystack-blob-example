package com.expedia.www.haystack.dropwizard.example.server;

import com.expedia.www.haystack.dropwizard.example.CommonConfiguration;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ServerApplication extends Application<CommonConfiguration> {
    public void start(String[] args) throws Exception{

        String[] newArgs = new String[]{"server", String.format("config-server.yaml")};

        ServerApplication serverApplication = new ServerApplication();
        serverApplication.run(newArgs);
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
    public void run(CommonConfiguration commonConfiguration, Environment environment){
        final ServerResource serverResource = new ServerResource();
        environment.jersey().register(serverResource);
    }
}
