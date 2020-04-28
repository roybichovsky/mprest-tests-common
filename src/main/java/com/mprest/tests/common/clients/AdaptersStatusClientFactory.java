package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.AdaptersStatusClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class AdaptersStatusClientFactory {
    @Bean
    @Autowired
    public AdaptersStatusClient buildAdaptersStatusClient(SecuredServerConfiguration securedConfiguration, AdaptersStatusClientConfiguration adaptersStatusClientConfiguration) {
        return new AdaptersStatusClientImpl(securedConfiguration, adaptersStatusClientConfiguration);
    }
}
