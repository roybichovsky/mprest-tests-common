package com.mprest.tests.common.process.management;

public interface ProcessProvider {

    ProcessInstance getProcess(AttachToProcessInstanceSettings settings);

    ProcessInstance createProcess(CreateProcessInstanceSettings settings);
}
