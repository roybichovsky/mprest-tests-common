package com.mprest.tests.common.process.management.docker;

import com.github.dockerjava.api.DockerClient;
import com.mprest.tests.common.process.management.ProcessInstance;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class DockerizedProcess implements ProcessInstance {

    private final String name;
    private final String id;
    private final Map<String, String> properties;

    DockerizedProcess(Map<String, String> properties, String id, String name) {
        this.properties = properties;
        this.name = name;
        this.id = id;
    }

    public boolean start() {
        log.debug("Starting container '{}'", name);

        if (id == null) {
            log.error("failed to find container ID for container '{}'. Unable to start the container.", name);
            return false;
        }
        DockerClient client = DockerUtils.getDockerClient(properties);
        if (client == null) {
            log.error("Could not start container '{}' because the docker client could not be created", name);
            return false;
        }

        client.startContainerCmd(id).exec();
        log.debug("Finished starting container '{}'", name);
        return true;
    }

    public boolean stop() {
        log.debug("Stopping container '{}'", name);
        if (id == null) {
            log.error("failed to find container ID for container '{}'. Unable to stop the container.", name);
            return false;
        }
        DockerClient client = DockerUtils.getDockerClient(properties);
        if (client == null) {
            log.error("Could not stop container '{}' because the docker client could not be created", name);
            return false;
        }

        client.stopContainerCmd(id).exec();
        log.debug("Finished stopping container '{}'", name);
        return true;
    }

    public boolean isRunning() {
        log.debug("Checking if the container name '{}' is running", name);

        if (id == null) {
            log.error("failed to find container ID for container name '{}'. Unable to check if the container is running.", name);
            return false;
        }

        DockerClient client = DockerUtils.getDockerClient(properties);
        if (client == null) {
            log.error("Could not check if the container '{} is running because the docker client could not be created", name);
            return false;
        }
        String status = client.inspectContainerCmd(id)
                .exec()
                .getState()
                .getStatus();

        boolean running = status != null && status.equals("running");
        log.debug("Finished checking if the container name '{}' is running. Result: '{}'", name, running);
        return running;
    }
}