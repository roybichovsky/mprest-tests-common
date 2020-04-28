package com.mprest.tests.common.process.management;

import com.mprest.tests.common.process.management.docker.DockerizedProcessProvider;
import com.mprest.tests.common.process.management.local.LocalProcessProvider;
import com.mprest.tests.common.process.management.remote.windows.RemoteProcessProvider;

public class ProcessManagement {

    private final ProcessManagementConfiguration processManagementConfiguration;

    public ProcessManagement(ProcessManagementConfiguration processManagementConfiguration) {
        this.processManagementConfiguration = processManagementConfiguration;
    }

    public ProcessInstance getProcess(AttachToProcessInstanceSettings settings) {
        ProcessProvider processProvider = createProvider(settings.deploymentType);
        return processProvider.getProcess(settings);
    }

    public ProcessInstance createProcess(CreateProcessInstanceSettings settings) {
        ProcessProvider processProvider = createProvider(settings.deploymentType);
        return processProvider.createProcess(settings);
    }

    private ProcessProvider createProvider(DeploymentType deploymentType) {
        ProcessProvider processProvider;

        switch (deploymentType) {
            case docker:
                processProvider = createDockerProvider(processManagementConfiguration.getDocker());
                break;

            case remote:
                processProvider = createRemoteProvider(processManagementConfiguration.getRemote());
                break;

            default:
                processProvider = createLocalProvider();
                break;
        }
        return processProvider;
    }

    private ProcessProvider createDockerProvider(ProcessManagementConfiguration.DockerClientConfiguration dockerCfg) {
        return new DockerizedProcessProvider(dockerCfg);
    }

    private ProcessProvider createRemoteProvider(ProcessManagementConfiguration.RemoteServerConfiguration remoteCfg) {
        return new RemoteProcessProvider(remoteCfg);
    }

    private ProcessProvider createLocalProvider() {
        return new LocalProcessProvider();
    }
}
