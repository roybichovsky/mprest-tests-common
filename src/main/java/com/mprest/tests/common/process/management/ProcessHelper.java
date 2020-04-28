package com.mprest.tests.common.process.management;

import com.mprest.tests.common.configuration.ProcessRuntimeConfiguration;
import lombok.extern.slf4j.Slf4j;

import static org.junit.Assert.*;

@Slf4j
public class ProcessHelper {

    private final ProcessManagement processManagement;
    private final AttachToProcessInstanceSettings attachToProcessSettings;
    private final CreateProcessInstanceSettings createProcessSettings;

    public ProcessHelper(ProcessManagement processManagement,
                         AttachToProcessInstanceSettings attachToProcessSettings,
                         CreateProcessInstanceSettings createProcessSettings) {
        this.processManagement = processManagement;
        this.attachToProcessSettings = attachToProcessSettings;
        this.createProcessSettings = createProcessSettings;
    }

    public static ProcessHelper create(ProcessManagement processManagement, ProcessRuntimeConfiguration config) {
        assertNotNull(processManagement);
        assertNotNull(config);

        AttachToProcessInstanceSettings attachedProcessSettings = new AttachToProcessInstanceSettings(
                config.getDeploymentType(),
                config.getServiceName(),
                config.getEntryPointAttach());
        CreateProcessInstanceSettings createProcessSettings = new CreateProcessInstanceSettings(
                config.getDeploymentType(),
                config.getServiceName(),
                config.getEntryPointCreate(),
                config.getWorkingDirectory(),
                new String[]{config.getArguments()},
                config.getEnvironment());

        return new ProcessHelper(processManagement, attachedProcessSettings, createProcessSettings);
    }

    public void restartProcess() {
        restartProcess(null, null, null);
    }

    public void restartProcess(Runnable toRunBeforeStop, Runnable toRunAfterStop, Runnable toRunAfterStart) {
        if (toRunBeforeStop != null)
            toRunBeforeStop.run();

        stopProcess();

        if (toRunAfterStop != null)
            toRunAfterStop.run();

        startProcess();

        if (toRunAfterStart != null)
            toRunAfterStart.run();
    }

    public void startProcess() {
        ProcessInstance createdProcess = processManagement.createProcess(createProcessSettings);
        assertNotNull("created process instance should not be null", createdProcess);

        log.info("starting the process '{}'", createProcessSettings.displayName);
        createdProcess.start();

        log.info("verifying that the process '{}' is up and running again", createProcessSettings.displayName);
        assertTrue("process should be in running state after restart", createdProcess.isRunning());
    }

    public void stopProcess() {
        ProcessInstance attachedProcess = processManagement.getProcess(attachToProcessSettings);
        assertNotNull("attached process connection instance should not be null", attachedProcess);

        log.info("verifying that the process '{}' is up and running", attachToProcessSettings.displayName);
        assertTrue("process connection should be in running state", attachedProcess.isRunning());

        log.info("stopping the process connection '{}'", attachToProcessSettings.entryPoint);
        attachedProcess.stop();

        log.info("verifying that the process connection '{}' is not running", attachToProcessSettings.displayName);
        assertFalse("process should be in stopped state", attachedProcess.isRunning());
    }

    public boolean isRunning() {
        ProcessInstance attachedProcess = processManagement.getProcess(attachToProcessSettings);
        assertNotNull("attached process connection instance should not be null", attachedProcess);

        log.info("verifying if the process connection '{}' is up and running", attachToProcessSettings.displayName);
        return attachedProcess.isRunning();
    }
}
