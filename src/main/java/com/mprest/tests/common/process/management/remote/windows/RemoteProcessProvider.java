package com.mprest.tests.common.process.management.remote.windows;

import com.mprest.tests.common.process.management.*;
import com.mprest.tests.common.utilities.SSHHelper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class RemoteProcessProvider implements ProcessProvider {
    private final SSHHelper sshHelper;
    private final ProcessManagementConfiguration.RemoteServerConfiguration remoteCfg;

    public RemoteProcessProvider(ProcessManagementConfiguration.RemoteServerConfiguration remoteCfg) {
        this.sshHelper = new SSHHelper(remoteCfg.getHost(), remoteCfg.getSshUserName(), remoteCfg.getSshPassword());
        this.remoteCfg = remoteCfg;
    }

    @Override
    public ProcessInstance getProcess(AttachToProcessInstanceSettings settings) {
        List<Integer> processes = RemoteProcessHelper.getRemoteProcesses(sshHelper, settings.entryPoint);
        if (processes.size() == 1) {
            return new RemoteProcess(processes.get(0), remoteCfg.getHost(), remoteCfg.getSshUserName(), remoteCfg.getSshPassword(), settings.entryPoint);
        }

        log.info("No or multiple processes exists for '{}'", settings.entryPoint);
        return new RemoteProcess(RemoteProcess.InvalidProcessId, remoteCfg.getHost(), remoteCfg.getSshUserName(), remoteCfg.getSshPassword(), settings.entryPoint);
    }

    @Override
    public ProcessInstance createProcess(CreateProcessInstanceSettings settings) {
        return new RemoteProcess(remoteCfg.getHost(), remoteCfg.getSshUserName(), remoteCfg.getSshPassword(), settings.workingDirectory, settings.entryPoint, settings.args);
    }
}