package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.DemandResponseClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class DemandResponseClientFactory {
    @Bean
    @Autowired
    public DemandResponseClient buildDemandResponseServiceClient(SecuredServerConfiguration securedConfiguration, DemandResponseClientConfiguration demandResponseConfiguration) {
        return new DemandResponseClientImpl(securedConfiguration, demandResponseConfiguration);
    }
}
