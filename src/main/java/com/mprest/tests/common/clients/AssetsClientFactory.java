package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.AssetsClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class AssetsClientFactory {
    @Bean
    @Autowired
    public AssetsClient buildAssetServiceClient(SecuredServerConfiguration securedServerConfiguration, AssetsClientConfiguration assetsClientConfiguration) {
        return new AssetsClientImpl(securedServerConfiguration, assetsClientConfiguration);
    }
}
