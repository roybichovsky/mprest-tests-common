package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.NetworkBatteriesConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class NetworkBatteriesClientFactory {
    @Bean
    @Autowired
    public NetworkBatteriesClient buildNetworkBatteriesClient(SecuredServerConfiguration securedServerConfiguration, NetworkBatteriesConfiguration networkBatteriesConfiguration) {
        return new NetworkBatteriesClientImpl(securedServerConfiguration, networkBatteriesConfiguration);
    }
}