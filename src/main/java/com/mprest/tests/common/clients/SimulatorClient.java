package com.mprest.tests.common.clients;

import java.io.File;

public interface SimulatorClient {
    boolean runDefault();
    boolean run(String environmentFileName, String scenarioFileName);
    boolean run(File environmentFile, File scenarioFile);
    boolean stop();
}

