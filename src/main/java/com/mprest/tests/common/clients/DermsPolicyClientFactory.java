package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.DermsPolicyClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Component
@Configuration
public class DermsPolicyClientFactory {

    @Bean
    @Autowired
    public DermsPolicyClient buildDermsPolicyServiceClient(SecuredServerConfiguration securedConfiguration, DermsPolicyClientConfiguration DermsPolicyClientConfiguration) {
        return new DermsPolicyClientImpl(securedConfiguration, DermsPolicyClientConfiguration);
    }
}
