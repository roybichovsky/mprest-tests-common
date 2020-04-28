package com.mprest.tests.common.process.management.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.command.BuildImageResultCallback;
import com.mprest.tests.common.process.management.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/*
This helper class requires providing system properties by the following list:
docker.io.url
docker.io.version
docker.io.username
docker.io.password
docker.io.email
docker.io.serverAddress
docker.io.dockerCertPath
docker.io.dockerCfgPath

Note:
Not all the properties are mandatory.
See https://github.com/docker-java/docker-java for further details.
*/
@Slf4j
public class DockerizedProcessProvider implements ProcessProvider {

    private final String imageTag;
    private final Map<String, String> properties;

    public DockerizedProcessProvider(ProcessManagementConfiguration.DockerClientConfiguration cfg) {
        this.properties = new HashMap<>();
        this.properties.put(DefaultDockerClientConfig.DOCKER_HOST, cfg.getUrl());
        this.properties.put(DefaultDockerClientConfig.API_VERSION, cfg.getVersion());
        this.properties.put(DefaultDockerClientConfig.REGISTRY_USERNAME, cfg.getUsername());
        this.properties.put(DefaultDockerClientConfig.REGISTRY_PASSWORD, cfg.getPassword());
        this.properties.put(DefaultDockerClientConfig.REGISTRY_EMAIL, cfg.getEmail());
        this.properties.put(DefaultDockerClientConfig.REGISTRY_URL, cfg.getServerAddress());
        this.properties.put(DefaultDockerClientConfig.DOCKER_CERT_PATH, cfg.getDockerCertPath());
        this.properties.put(DefaultDockerClientConfig.DOCKER_CONFIG, cfg.getDockerCfgPath());

        imageTag = cfg.getImageTag();
    }

    public ProcessInstance getProcess(AttachToProcessInstanceSettings settings) {
        return getProcess(settings.entryPoint);
    }

    private ProcessInstance getProcess(String name) {
        if (!name.startsWith("/")) {
            name = "/" + name;
        }

        String id = DockerUtils.getContainerId(name, properties);
        if (id == null) {
            return null;
        }

        return new DockerizedProcess(properties, id, name);
    }


    public ProcessInstance createProcess(CreateProcessInstanceSettings settings) {
        ProcessInstance existingProcess = getProcess(settings.entryPoint);
        if (existingProcess != null)
            return existingProcess;

        if (settings instanceof DockerizedProcessSettings) {
            DockerizedProcessSettings dockCfg = (DockerizedProcessSettings) settings;
            if (dockCfg.dockerFile != null) {
                return createFromDockerFile(dockCfg);
            }
        }

        return createFromImage(settings);
    }

    private ProcessInstance createFromImage(CreateProcessInstanceSettings settings) {
        if (imageTag == null) {
            log.error("Could not create container '{}' because the required image tag is missing", settings.displayName);
            return null;
        }

        log.debug("Creating container '{}' using the image tag '{}'", settings.displayName, imageTag);
        DockerClient client = DockerUtils.getDockerClient(properties);
        if (client == null) {
            log.debug("Could not create container '{}' using the image tag '{}' because the docker client could not be created", settings.displayName, imageTag);
            return null;
        }

        CreateContainerCmd cmd = client.createContainerCmd(imageTag)
                .withName(settings.displayName);
        if (settings.workingDirectory != null) {
            cmd = cmd.withWorkingDir(settings.workingDirectory);
        }
        cmd.withEntrypoint(settings.entryPoint)
                .withCmd(settings.args)
                .exec();

        ProcessInstance container = getProcess(settings.displayName);
        if (container != null) {
            log.debug("Finished creating container '{}' using the image tag '{}'", settings.displayName, imageTag);
            return container;
        }
        log.error("Could not createProcess container '{}' using the image tag '{}'", settings.displayName, imageTag);
        return null;
    }

    private ProcessInstance createFromDockerFile(DockerizedProcessSettings config) {
        log.debug("Creating container '{}' using the docker file '{}'", config.displayName, config.dockerFile.getName());
        BuildImageResultCallback callback = new BuildImageResultCallback();
        DockerClient client = DockerUtils.getDockerClient(properties);
        if (client == null) {
            log.debug("Could not createProcess container '{}' using the docker file '{}' because the docker client could not be created", config.displayName, config.dockerFile.getName());
            return null;
        }

        BuildImageCmd cmd = client.buildImageCmd(config.dockerFile);
        String imageId = cmd.exec(callback).awaitImageId();
        client.createContainerCmd(imageId)
                .withName(config.displayName)
                .exec();

        ProcessInstance container = getProcess(config.displayName);
        if (container != null) {
            log.debug("Finished creating container '{}' using the docker file '{}'", config.displayName, config.dockerFile.getName());
            return container;
        }
        log.error("Could not createProcess container '{}' using the docker file '{}'", config.displayName, config.dockerFile.getName());
        return null;
    }
}
