package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.PlanningEngineClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class PlanningEngineClientFactory {

    @Bean
    @Autowired
    public PlanningEngineClient buildPlanningEngineClient(SecuredServerConfiguration securedConfiguration, PlanningEngineClientConfiguration planningEngineClientConfiguration) {
        return new PlanningEngineClientImpl(securedConfiguration, planningEngineClientConfiguration);
    }
}
