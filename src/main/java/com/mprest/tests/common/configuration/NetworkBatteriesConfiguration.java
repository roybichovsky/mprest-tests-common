package com.mprest.tests.common.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties(prefix = "network-batteries")
public class NetworkBatteriesConfiguration extends BaseClientConfiguration  {
}