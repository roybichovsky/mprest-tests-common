package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.DemandResponseClientConfiguration;
import com.mprest.tests.common.configuration.LimitedUserClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class AuthorizationDemandResponseClientFactory {
    @Bean
    @Autowired
    public AuthorizationDemandResponseClient buildAuthorizationDemandResponseClient(SecuredServerConfiguration securedConfiguration, DemandResponseClientConfiguration demandResponseConfiguration, LimitedUserClientConfiguration limitedUserClientConfiguration) {
        return new AuthorizationDemandResponseClientImpl(securedConfiguration, demandResponseConfiguration, limitedUserClientConfiguration);
    }
}
