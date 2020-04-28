package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.RuleManagerClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class RuleManagerClientFactory {
    @Bean
    @Autowired
    public RuleManagerClient buildRuleManagerClient(SecuredServerConfiguration securedConfiguration, RuleManagerClientConfiguration ruleClientConfiguration){
        return new RuleManagerClientImpl(securedConfiguration, ruleClientConfiguration);
    }
}
