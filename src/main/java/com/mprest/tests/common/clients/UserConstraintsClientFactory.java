package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.configuration.UserConstraintsClientConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class UserConstraintsClientFactory {
    @Bean
    @Autowired
    public UserConstraintsClient buildUserConstraintsClient(SecuredServerConfiguration securedServerConfiguration, UserConstraintsClientConfiguration userConstraintsClientConfiguration) {
        return new UserConstraintsClientImpl(securedServerConfiguration, userConstraintsClientConfiguration);
    }
}