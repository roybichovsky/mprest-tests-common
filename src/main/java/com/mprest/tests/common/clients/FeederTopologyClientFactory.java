package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.FeederTopologyClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class FeederTopologyClientFactory {
    @Bean
    @Autowired
    public FeederTopologyClient buildFeederTopologyClient(SecuredServerConfiguration securedServerConfiguration,
                                                        FeederTopologyClientConfiguration feederTopologyClientConfiguration) {
        return new FeederTopologyClientImpl(securedServerConfiguration, feederTopologyClientConfiguration);
    }

}
