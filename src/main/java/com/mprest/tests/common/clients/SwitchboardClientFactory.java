package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.configuration.SwitchboardClientConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class SwitchboardClientFactory {
    @Bean
    @Autowired
    public SwitchboardClient buildSwitchboardClient(SecuredServerConfiguration securedServerConfiguration,
                                                    SwitchboardClientConfiguration switchboardClientConfiguration) {
        return new SwitchboardClientImpl(securedServerConfiguration, switchboardClientConfiguration);
    }
}
