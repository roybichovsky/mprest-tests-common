package com.mprest.tests.common.file.management;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties(prefix = "file")

@Setter
@Getter
public class FileManagementConfiguration {

    @NestedConfigurationProperty
    private DockerClientConfiguration docker;


    @Setter
    @Getter
    public static class DockerClientConfiguration {
        private String url;
        private String version;
        private String username;
        private String password;
        private String email;
        private String serverAddress;
        private String dockerCertPath;
        private String dockerCfgPath;
    }
}