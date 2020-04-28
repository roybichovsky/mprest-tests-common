package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.AlertsClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class AlertsClientFactory {
    @Bean
    @Autowired
    public AlertsClient buildAlertsServiceClient(SecuredServerConfiguration securedConfiguration, AlertsClientConfiguration alertsConfiguration) {
        return new AlertsClientImpl(securedConfiguration, alertsConfiguration);
    }
}
