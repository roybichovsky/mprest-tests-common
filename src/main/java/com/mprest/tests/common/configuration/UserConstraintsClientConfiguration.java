package com.mprest.tests.common.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "user-constraints")
public class UserConstraintsClientConfiguration extends BaseClientConfiguration {
}
