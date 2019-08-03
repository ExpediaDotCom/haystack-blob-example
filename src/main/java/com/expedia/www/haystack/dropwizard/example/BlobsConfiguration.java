package com.expedia.www.haystack.dropwizard.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;


public class BlobsConfiguration {

    private Store store;

    @NotEmpty
    private Boolean enabled;

    @JsonProperty
    public Boolean isEnabled() {
        return enabled;
    }

    @JsonProperty
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @JsonProperty
    public Store getStore() {
        return store;
    }

    @JsonProperty
    public void setStore(Store store) {
        this.store = store;
    }

    public BlobsConfiguration() {
    }


    public class Store {

        private String name;

        private String host;

        private int port;

        public Store() {
        }

        @JsonProperty
        public String getName() {
            return name;
        }

        @JsonProperty
        public void setName(String name) {
            this.name = name;
        }

        @JsonProperty
        public String getHost() {
            return host;
        }

        @JsonProperty
        public void setHost(String host) {
            this.host = host;
        }

        @JsonProperty
        public int getPort() {
            return port;
        }

        @JsonProperty
        public void setPort(int port) {
            this.port = port;
        }
    }
}
