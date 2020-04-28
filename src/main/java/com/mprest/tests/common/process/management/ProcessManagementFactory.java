package com.mprest.tests.common.process.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class ProcessManagementFactory {

    @Bean
    @Autowired
    public ProcessManagement buildProcessManagement(ProcessManagementConfiguration processManagementConfiguration) {
        return new ProcessManagement(processManagementConfiguration);
    }
}
