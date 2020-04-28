package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.DistributionTransformersClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class DistributionTransformersClientFactory {
    @Bean
    @Autowired
    public DistributionTransformersClient buildDistributionTransformersClient(SecuredServerConfiguration securedServerConfiguration, DistributionTransformersClientConfiguration distributionTransformersClientConfiguration) {
        return new DistributionTransformersClientImpl(securedServerConfiguration, distributionTransformersClientConfiguration);
    }
}
