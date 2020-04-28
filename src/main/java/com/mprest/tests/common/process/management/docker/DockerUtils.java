package com.mprest.tests.common.process.management.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.RemoteApiVersion;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class DockerUtils {

    public static DockerClient getDockerClient(Map<String, String> properties) {
        try {
            DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                    .withDockerHost(properties.get(DefaultDockerClientConfig.DOCKER_HOST))
                    .withRegistryUrl(properties.get(DefaultDockerClientConfig.REGISTRY_URL))
                    .withRegistryUsername(properties.get(DefaultDockerClientConfig.REGISTRY_USERNAME))
                    .withRegistryPassword(properties.get(DefaultDockerClientConfig.REGISTRY_PASSWORD))
                    .withRegistryEmail(properties.get(DefaultDockerClientConfig.REGISTRY_EMAIL))
                    .withDockerCertPath(properties.get(DefaultDockerClientConfig.DOCKER_CERT_PATH))
                    .withDockerConfig(properties.get(DefaultDockerClientConfig.DOCKER_CONFIG))
                    .withDockerTlsVerify(false)
                    .withApiVersion(RemoteApiVersion.VERSION_1_24.getVersion())
                    .build();
            log.debug("Host configured to '{}'", properties.get(DefaultDockerClientConfig.DOCKER_HOST));
            return DockerClientBuilder.getInstance(config).build();
        } catch (Exception ex) {
            log.error("Could not get docker client.", ex);
            return null;
        }
    }

    public static String getContainerId(String name, Map<String, String> properties) {
        if (name == null)
            return null;

        DockerClient client = DockerUtils.getDockerClient(properties);
        List<Container> listContainers = client.listContainersCmd()
                .withShowAll(true)
                .exec();

        Optional<Container> container = listContainers
                .stream()
                .filter(c -> name.equals(c.getNames()[0]))
                .findFirst();

        if (!container.isPresent()) {
            log.debug("container name '{}' is not present", name);
            return null;
        }

        Container containerInstance = container.get();
        String containerId = containerInstance.getId();
        log.debug("container name '{}' has container ID '{}'", name, containerId);
        return containerId;
    }
}
