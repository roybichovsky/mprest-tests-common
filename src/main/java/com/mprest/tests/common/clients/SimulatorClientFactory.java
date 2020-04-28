package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.SimulatorClientConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Configuration
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SimulatorClientFactory {
    @Bean
    @Autowired
    public SimulatorClient buildSimulatorClient(SimulatorClientConfiguration config) {
        return new SimulatorClientImpl(config);
    }
}
