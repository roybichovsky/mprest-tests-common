package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.EntityDataClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by leonidr on 19/08/2018.
 */
@Component
@Configuration
public class EntityDataClientFactory {
    @Bean
    @Autowired
    public EntityDataClient buildEntityDataClient(SecuredServerConfiguration securedConfiguration, EntityDataClientConfiguration entityDataConfiguration) {
        return new EntityDataClientImpl(securedConfiguration, entityDataConfiguration);
    }
}
