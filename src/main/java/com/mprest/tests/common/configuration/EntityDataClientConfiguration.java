package com.mprest.tests.common.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "entity-data")
public class EntityDataClientConfiguration extends BaseClientConfiguration {
    private String entityManagerUrl;
    private String minimalTopologyBody;
    private String type;
}
