package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.TopologyUploadClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class TopologyUploadClientFactory {
    @Bean
    @Autowired
    public TopologyUploadClient buildTopologyUploadClient(SecuredServerConfiguration securedConfiguration, TopologyUploadClientConfiguration topologyUploadClientConfiguration) {
        return new TopologyUploadClientImpl(securedConfiguration, topologyUploadClientConfiguration);
    }
}
