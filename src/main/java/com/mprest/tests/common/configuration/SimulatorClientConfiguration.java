package com.mprest.tests.common.configuration;

import com.mprest.tests.common.process.management.DeploymentType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "simulator")
public class SimulatorClientConfiguration {
    private DeploymentType deploymentType;
    private String host;
    private String workingDirectory;
    private String batchFile;
    private String defaultEnvironment;
    private String defaultScenario;
    private String executableFileName;
    private String simulationFilesSubfolder;
}
