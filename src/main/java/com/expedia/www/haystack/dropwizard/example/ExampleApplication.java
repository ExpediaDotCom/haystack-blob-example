package com.expedia.www.haystack.dropwizard.example;

import com.expedia.www.haystack.dropwizard.example.client.ClientApplication;
import com.expedia.www.haystack.dropwizard.example.server.ServerApplication;

public class ExampleApplication {
    public static void main(String[] args) throws Exception {
        if (args == null)
            throw new Exception("Arguments not provided");

        if ("client".equalsIgnoreCase(args[0])) {
            new ClientApplication().run("server", args[1]);
        } else if ("server".equalsIgnoreCase(args[0])) {
            new ServerApplication().run("server", args[1]);
        } else {
            throw new Exception("unexpected argument".concat(" ").concat(args[0]));
        }
    }
}
