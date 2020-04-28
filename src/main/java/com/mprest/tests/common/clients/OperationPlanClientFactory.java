package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.OperationPlanConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;
        import org.springframework.stereotype.Component;


@Component
@Configuration
public class OperationPlanClientFactory {

    @Bean
    @Autowired
    public OperationPlanClient buildOperationPlanServiceClient(SecuredServerConfiguration securedConfiguration, OperationPlanConfiguration operationPlanClientConfiguration) {
        return new OperationPlanClientImpl(securedConfiguration, operationPlanClientConfiguration);
    }
}
