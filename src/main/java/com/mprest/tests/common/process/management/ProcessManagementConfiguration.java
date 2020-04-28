package com.mprest.tests.common.process.management;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties(prefix = "process")
public class ProcessManagementConfiguration {

    @Getter
    @Setter
    @NestedConfigurationProperty
    private DockerClientConfiguration docker;

    @Getter
    @Setter
    public static class DockerClientConfiguration {
        private String url;
        private String imageTag;
        private String version;
        private String username;
        private String password;
        private String email;
        private String serverAddress;
        private String dockerCertPath;
        private String dockerCfgPath;
    }

    @Getter
    @Setter
    @NestedConfigurationProperty
    private RemoteServerConfiguration remote;

    @Getter
    @Setter
    public static class RemoteServerConfiguration {
        private String host;
        private int port;
        private String sshUserName;
        private String sshPassword;
    }
}
