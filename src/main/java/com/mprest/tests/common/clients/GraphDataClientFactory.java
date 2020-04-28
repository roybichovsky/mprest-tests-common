package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.GraphDataClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class GraphDataClientFactory {
    @Bean
    @Autowired
    public GraphDataClient buildGraphDataClient(SecuredServerConfiguration securedConfiguration, GraphDataClientConfiguration graphDataClientConfiguration) {
        return new GraphDataClientImpl(securedConfiguration, graphDataClientConfiguration);
    }
}
