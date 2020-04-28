package com.mprest.tests.common.process.management;

import java.util.Map;

public class CreateProcessInstanceSettings extends ProcessInstanceSettings {
    public final String workingDirectory;
    public final String[] args;
    public final Map<String, String> environment;

    public CreateProcessInstanceSettings(DeploymentType deploymentType, String displayName, String entryPoint, String workingDirectory, String[] args, Map<String, String> environment) {
        super(deploymentType, displayName, entryPoint);

        this.workingDirectory = workingDirectory;
        this.args = args == null ? new String[]{} : args;
        this.environment = environment;
    }
}