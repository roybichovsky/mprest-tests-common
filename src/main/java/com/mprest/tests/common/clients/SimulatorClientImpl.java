package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.SimulatorClientConfiguration;
import com.mprest.tests.common.process.management.AttachToProcessInstanceSettings;
import com.mprest.tests.common.process.management.CreateProcessInstanceSettings;
import com.mprest.tests.common.process.management.ProcessInstance;
import com.mprest.tests.common.process.management.ProcessManagement;
import com.mprest.tests.common.utilities.PathHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.nio.file.Path;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Slf4j
public class SimulatorClientImpl implements SimulatorClient {

    private final String SIMULATOR_DISPLAY_NAME = "Simulator";

    @Autowired
    private ProcessManagement processManagement;

    private SimulatorClientConfiguration config;

    private String simulatorWorkingDirectory;
    private String simulationFilesSubfolder;
    private Path entryPoint;

    public SimulatorClientImpl(SimulatorClientConfiguration config) {
        this.config = config;
        simulatorWorkingDirectory = config.getWorkingDirectory();
        simulationFilesSubfolder = config.getSimulationFilesSubfolder();

        entryPoint = PathHelper.combine(simulatorWorkingDirectory, config.getBatchFile());
        log.debug("Host='{}', Path='{}', Entry Point='{}'", config.getHost(), config.getWorkingDirectory(), entryPoint);
    }

    public boolean runDefault() {
        final String environmentFileName = config.getDefaultEnvironment();
        final String scenarioFileName = config.getDefaultScenario();

        return run(environmentFileName, scenarioFileName);
    }

    public boolean run(String environmentFileName, String scenarioFileName) {
        File environmentFile = new File(environmentFileName);
        File scenarioFile = new File(scenarioFileName);
        return run(environmentFile, scenarioFile);
    }

    public boolean run(File environmentFile, File scenarioFile) {
        stopSimulator();

        Path scenarioPath = PathHelper.combine(simulatorWorkingDirectory, simulationFilesSubfolder, scenarioFile.getName());
        Path environmentPath = PathHelper.combine(simulatorWorkingDirectory, simulationFilesSubfolder, environmentFile.getName());

        final String environment = environmentPath.toString();
        final String scenario = scenarioPath.toString().endsWith(".mssx") ? scenarioPath.toString() : "\"\"";
        final String[] args = {"-environment", environment, "-scenario", scenario, "-autoStart", "True"};

        log.info("Using the environment file: '{}' and scenario file: '{}' in working directory: '{}'.", environment, scenario, simulatorWorkingDirectory);

        CreateProcessInstanceSettings createSettings = new CreateProcessInstanceSettings(config.getDeploymentType(), SIMULATOR_DISPLAY_NAME, entryPoint.toString(), config.getWorkingDirectory(), args, null);
        ProcessInstance process = processManagement.createProcess(createSettings);

        return process.start();
    }

    public boolean stop() {
        log.info("Stopping the simulator and restarting it with default environment and scenario");
        return runDefault();
    }

    private void stopSimulator() {
        ProcessInstance process = getSimulatorProcess();
        if (!process.isRunning()) {
            log.warn("trying to stop the simulator but it is not in running state");
            return;
        }

        assertTrue("simulator is running after trying to stop it", process.stop());
    }

    private ProcessInstance getSimulatorProcess() {
        AttachToProcessInstanceSettings attachSettings = new AttachToProcessInstanceSettings(config.getDeploymentType(), SIMULATOR_DISPLAY_NAME, config.getExecutableFileName());
        ProcessInstance process = processManagement.getProcess(attachSettings);

        assertNotNull("simulator process instance", process);

        return process;
    }
}