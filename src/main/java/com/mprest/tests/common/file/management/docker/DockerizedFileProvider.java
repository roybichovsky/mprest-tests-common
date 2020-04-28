package com.mprest.tests.common.file.management.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.mprest.tests.common.file.management.*;
import com.mprest.tests.common.process.management.docker.DockerUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DockerizedFileProvider implements FileProvider {

    private final Map<String, String> properties;

    public DockerizedFileProvider(FileManagementConfiguration.DockerClientConfiguration cfg) {
        this.properties = new HashMap<>();
        this.properties.put(DefaultDockerClientConfig.DOCKER_HOST, cfg.getUrl());
        this.properties.put(DefaultDockerClientConfig.API_VERSION, cfg.getVersion());
        this.properties.put(DefaultDockerClientConfig.REGISTRY_USERNAME, cfg.getUsername());
        this.properties.put(DefaultDockerClientConfig.REGISTRY_PASSWORD, cfg.getPassword());
        this.properties.put(DefaultDockerClientConfig.REGISTRY_EMAIL, cfg.getEmail());
        this.properties.put(DefaultDockerClientConfig.REGISTRY_URL, cfg.getServerAddress());
        this.properties.put(DefaultDockerClientConfig.DOCKER_CERT_PATH, cfg.getDockerCertPath());
        this.properties.put(DefaultDockerClientConfig.DOCKER_CONFIG, cfg.getDockerCfgPath());
    }

    @Override
    public boolean copyTo(FileCopySettings settings) {
        log.debug("Copying the file '{}' from container '{}' to path '{}'", settings.sourceFile, settings.sourceHost, settings.destinationFile);

        String id = DockerUtils.getContainerId(settings.destinationHost, properties);
        if (id == null) {
            log.error("failed to find container ID for container '{}'. Unable to copy the file '{}' to container '{}'", settings.destinationHost, settings.sourceFile, settings.destinationHost);
            return false;
        }

        try {
            DockerClient client = DockerUtils.getDockerClient(properties);
            if (client == null) {
                log.error("Could not copy the file '{}' to the container '{}' because the docker client could not be created", settings.sourceFile, settings.destinationHost);
                return false;
            }
            client.copyArchiveToContainerCmd(id)
                    .withHostResource(cleanPath(settings.sourceFile))
                    .withRemotePath(cleanPath(settings.destinationFile))
                    .exec();
            return true;
        } catch (Exception ex) {
            String errorStr = String.format("Could not copy file '%s' to remote path '%s' on container '%s'", settings.sourceFile, settings.destinationFile, settings.destinationHost);
            log.error(errorStr, ex);
            return false;
        }
    }

    @Override
    public boolean copyFrom(FileCopySettings settings) {
        log.debug("Copying the file '{}' from container '{}' as '{}'", settings.sourceHost, settings.sourceFile, settings.destinationFile);
        FileGetSettings getSettings = new FileGetSettings(settings.sourceHost, settings.sourceFile);
        byte[] buffer = get(getSettings);
        try {
            java.io.File targetFile = new File(cleanPath(settings.destinationFile));
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(buffer);
            outStream.close();

            log.debug("Finished copying the file '{}' from container '{}' as '{}'", settings.sourceFile, settings.sourceHost, settings.destinationFile);
            return true;
        } catch (Exception ex) {
            String errorStr = String.format("Could not copy the file '%s' from container '%s' as '%s'", settings.sourceHost, settings.sourceFile, settings.destinationFile);
            log.error(errorStr, ex);
            return false;
        }
    }

    @Override
    public boolean delete(FileDeletionSettings settings) {
        log.debug("Deleting the file '{}' from container at '{}'", settings.sourceHost, settings.sourceFile);

        String id = DockerUtils.getContainerId(settings.sourceHost, properties);
        if (id == null) {
            log.error("failed to find container ID for container name '{}'. Unable to delete the file '{}' from the container '{}'", settings.sourceHost, settings.sourceFile, settings.sourceHost);
            return false;
        }

        try {
            DockerClient client = DockerUtils.getDockerClient(properties);
            if (client == null) {
                log.error("Could not delete the file '{}' from the container '{}' because the docker client could not be created", settings.sourceFile, settings.sourceHost);
                return false;
            }

            String[] deleteCommand = {"rm", "-rf", cleanPath(settings.sourceFile)};
            ExecCreateCmdResponse cmdResponse = client.execCreateCmd(id)
                    .withAttachStdout(true)
                    .withCmd(deleteCommand)
                    .exec();

            ExecStartResultCallback callback = new ExecStartResultCallback();
            return client
                    .execStartCmd(cmdResponse.getId())
                    .exec(callback)
                    .awaitCompletion(30, TimeUnit.SECONDS);
        } catch (Exception ex) {
            String errorStr = String.format("Could not delete the file '%s' from container '%s'.", settings.sourceHost, settings.sourceFile);
            log.error(errorStr, ex);
            return false;
        }
    }

    @Override
    public byte[] get(FileGetSettings settings) {
        log.debug("Getting the file '{}' from container '{}'", settings.sourceHost, settings.sourceFile);
        String id = DockerUtils.getContainerId(settings.sourceHost, properties);
        if (id == null) {
            log.error("failed to find container ID for container name '{}'. Unable to copy the file '{}' from the container '{}'", settings.sourceHost, settings.sourceFile, settings.sourceHost);
            return null;
        }

        try {
            DockerClient client = DockerUtils.getDockerClient(properties);
            if (client == null) {
                log.error("Could not copy the file '{}' from the container '{}' because the docker client could not be created", settings.sourceFile, settings.sourceHost);
                return null;
            }

            InputStream stream = client.copyArchiveFromContainerCmd(id, cleanPath(settings.sourceFile)).exec();
            byte[] buffer = new byte[stream.available()];
            stream.read(buffer);

            log.debug("Finished getting the file '{}' from container '{}'", settings.sourceFile, settings.sourceHost);
            return buffer;
        } catch (Exception ex) {
            String errorStr = String.format("Could not get the file '%s' from container '%s'.", settings.sourceHost, settings.sourceFile);
            log.error(errorStr, ex);
            return null;
        }
    }

    private String cleanPath(String path) {
        if (path.startsWith(FileManagement.DOCKER_SCHEME)) {
            return path.substring(FileManagement.DOCKER_SCHEME.length() + 1);
        }

        return path;
    }
}
