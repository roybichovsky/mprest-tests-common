package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.AggregatedSettingsClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class AggregatedSettingsClientFactory {
    @Bean
    @Autowired
    public AggregatedSettingsClient buildAggregatedSettingsClient(SecuredServerConfiguration secured, AggregatedSettingsClientConfiguration aggregatedSettingsClientConfiguration) {
        return new AggregatedSettingsClientImpl(secured, aggregatedSettingsClientConfiguration);
    }
}
