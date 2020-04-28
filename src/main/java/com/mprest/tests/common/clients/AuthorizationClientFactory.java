package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.AuthorizationClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class AuthorizationClientFactory {
    @Bean
    @Autowired
    public AuthorizationClient buildAuthorizationServiceClient(SecuredServerConfiguration securedConfiguration, AuthorizationClientConfiguration authorizationClientConfiguration) {
        return new AuthorizationClientImpl(securedConfiguration, authorizationClientConfiguration);
    }
}
