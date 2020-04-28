package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.ChartGraphClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class ChartGraphClientFactory {
    @Bean
    @Autowired
    public ChartGraphClient buildChartGraphClient(SecuredServerConfiguration securedServerConfiguration, ChartGraphClientConfiguration chartGraphClientConfiguration) {
        return new ChartGraphClientImpl(securedServerConfiguration, chartGraphClientConfiguration);
    }
}