package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.configuration.SystemEventsClientConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class SystemEventsClientFactory {
    @Bean
    @Autowired
    public SystemEventsClient buildSystemEventsClient(SecuredServerConfiguration securedServerConfiguration,
                                                      SystemEventsClientConfiguration systemEventsClientConfiguration) {
        return new SystemEventsClientImpl(securedServerConfiguration, systemEventsClientConfiguration);
    }
}
