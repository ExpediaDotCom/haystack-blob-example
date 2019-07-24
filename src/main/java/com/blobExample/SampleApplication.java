package com.blobExample;

import com.blobExample.client.ClientApplication;
import com.blobExample.server.ServerApplication;

public class SampleApplication {
    public static void main(String[] args) throws Exception {

        if (args == null)
            throw new Exception("Arguments not provided");

        if (String.format("sampleClient").equalsIgnoreCase(args[0])) {
            new ClientApplication().start(args);
        } else if (String.format("sampleServer").equalsIgnoreCase(args[0])) {
            new ServerApplication().start(args);
        } else {
            throw new Exception(String.format("unexpected argument").concat(" ").concat(args[0]));
        }

    }
}
