package com.expedia.www.haystack.dropwizard.example.server;

import com.expedia.haystack.dropwizard.bundle.HaystackTracerBundle;
import com.expedia.www.haystack.dropwizard.example.config.AppConfiguration;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ServerApplication extends Application<AppConfiguration> {

    private final HaystackTracerBundle<AppConfiguration> haystackTracerBundle = new HaystackTracerBundle<>();

    @Override
    public String getName() {
        return "server";
    }

    @Override
    public void initialize(Bootstrap<AppConfiguration> bootstrap) {
        bootstrap.addBundle(this.haystackTracerBundle);
    }

    @Override
    public void run(AppConfiguration appConfiguration, Environment environment){
        environment.jersey().register(new ServerResource());
    }
}
