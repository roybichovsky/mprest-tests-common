package com.mprest.tests.common.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "read-only")
public class ReadOnlyClientConfiguration extends BaseClientConfiguration {
    private String password;
}
