package com.mprest.tests.common.process.management;

public class ProcessInstanceSettings {
    public final DeploymentType deploymentType;
    public final String displayName;
    public final String entryPoint;

    public ProcessInstanceSettings(DeploymentType deploymentType, String displayName, String entryPoint) {
        this.deploymentType = deploymentType;
        this.displayName = displayName;
        this.entryPoint = entryPoint;
    }
}
