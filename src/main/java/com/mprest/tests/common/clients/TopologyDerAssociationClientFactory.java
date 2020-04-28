package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.configuration.TopologyDerAssociationClientConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class TopologyDerAssociationClientFactory {
    @Bean
    @Autowired
    public TopologyDerAssociationClient buildTopologyDerAssociationClient(SecuredServerConfiguration securedServerConfiguration, TopologyDerAssociationClientConfiguration topologyDerAssociationClientConfiguration) {
        return new TopologyDerAssociationClientImpl(securedServerConfiguration, topologyDerAssociationClientConfiguration);
    }
}

