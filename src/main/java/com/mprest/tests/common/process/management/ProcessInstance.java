package com.mprest.tests.common.process.management;

public interface ProcessInstance {
    boolean start();

    boolean stop();

    boolean isRunning();
}
