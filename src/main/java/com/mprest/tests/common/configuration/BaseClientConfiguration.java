package com.mprest.tests.common.configuration;

import lombok.Data;

@Data
public abstract class BaseClientConfiguration {
    private String host;
    private int port;
    private String path;
    private String pathPrefix;
}
