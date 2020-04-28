package com.mprest.tests.common.process.management.docker;

import com.mprest.tests.common.process.management.CreateProcessInstanceSettings;
import com.mprest.tests.common.process.management.DeploymentType;

public class DockerizedProcessSettings extends CreateProcessInstanceSettings {
    public final java.io.File dockerFile;

    public DockerizedProcessSettings(String name, String entryPoint, String workingDirectory, String... args) {
        super(DeploymentType.docker, name, entryPoint, workingDirectory, args, null);
        this.dockerFile = null;
    }

    public DockerizedProcessSettings(String name, java.io.File dockerFile, String entryPoint, String workingDirectory, String[] args) {
        super(DeploymentType.docker, name, entryPoint, workingDirectory, args, null);
        this.dockerFile = dockerFile;
    }
}
