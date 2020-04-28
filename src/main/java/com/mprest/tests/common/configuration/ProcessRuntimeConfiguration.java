package com.mprest.tests.common.configuration;

import com.mprest.tests.common.process.management.DeploymentType;
import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@Configuration
public class ProcessRuntimeConfiguration {
    private DeploymentType deploymentType;
    private String serviceName;
    private String entryPointCreate;
    private String entryPointAttach;
    private String workingDirectory;
    private String arguments;
    private Map<String, String> environment;
}
