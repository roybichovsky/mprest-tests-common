package com.mprest.tests.common.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "limited-user")
public class LimitedUserClientConfiguration {
    private String userName;
    private String password;
}