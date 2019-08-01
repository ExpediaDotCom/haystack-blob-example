package com.expedia.www.haystack.dropwizard.example;

import com.expedia.www.haystack.dropwizard.example.client.ClientApplication;
import com.expedia.www.haystack.dropwizard.example.server.ServerApplication;

public class SampleApplication {
    public static void main(String[] args) throws Exception {

        if (args == null)
            throw new Exception("Arguments not provided");

        if (String.format("client").equalsIgnoreCase(args[0])) {
            new ClientApplication().start(args);
        } else if (String.format("server").equalsIgnoreCase(args[0])) {
            new ServerApplication().start(args);
        } else {
            throw new Exception(String.format("unexpected argument").concat(" ").concat(args[0]));
        }

    }
}
