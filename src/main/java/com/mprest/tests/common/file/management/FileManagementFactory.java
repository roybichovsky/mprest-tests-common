package com.mprest.tests.common.file.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class FileManagementFactory {

    @Bean
    @Autowired
    public FileManagement buildFileManagement(FileManagementConfiguration fileManagementConfiguration) {
        return new FileManagement(fileManagementConfiguration);
    }
}
