package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.configuration.TopologyClientConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class TopologyClientFactory {
    @Bean
    @Autowired
    public TopologyClient buildTopologyServiceClient(SecuredServerConfiguration securedConfiguration, TopologyClientConfiguration topologyClientConfiguration) {
        return new TopologyClientImpl(securedConfiguration, topologyClientConfiguration);
    }
}

