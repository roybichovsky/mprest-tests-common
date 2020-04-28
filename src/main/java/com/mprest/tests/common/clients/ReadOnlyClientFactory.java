package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.ReadOnlyClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class ReadOnlyClientFactory {
    @Bean
    @Autowired
    ReadOnlyClient buildReadOnlyClient(SecuredServerConfiguration securedConfiguration, ReadOnlyClientConfiguration readOnlyClientConfiguration){
        return new ReadOnlyClientImpl(securedConfiguration, readOnlyClientConfiguration);
    }
}
