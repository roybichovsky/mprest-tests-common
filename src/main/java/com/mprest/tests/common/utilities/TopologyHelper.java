package com.mprest.tests.common.utilities;

import com.mprest.tests.common.clients.TopologyClient;
import com.mprest.tests.common.data.TopologyNode;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Slf4j
public class TopologyHelper {

    private final TopologyClient topologyClient;

    public TopologyHelper(TopologyClient topologyClient) {
        this.topologyClient = topologyClient;
    }

    public TopologyNode getTopology() {
        final Duration TOPOLOGY_POLLING_INTERVAL = Duration.ofSeconds(10);
        final short MAX_TOPOLOGY_REQUESTS = 10;
        return getTopology(TOPOLOGY_POLLING_INTERVAL, MAX_TOPOLOGY_REQUESTS);
    }

    public TopologyNode getTopology(Duration pollingInterval, int maxRequests) {
        log.info("Trying to get a topology (polling timeout: '{}', max requests: '{}')", pollingInterval, maxRequests);
        int requestsSent = 0;
        TopologyNode receivedTopology = null;
        try {
            while (receivedTopology == null) {
                receivedTopology = topologyClient.getMinimalTopology();
                if (receivedTopology == null) {
                    log.error("Could not get the minimal topology");
                    return null;
                }
                requestsSent++;
                if (requestsSent == maxRequests) {
                    log.error("Requested a topology but did not get it");
                    return null;
                }
            }
        } catch (Exception ex) {
            String errorStr = "Could not get the topology.";
            log.error(errorStr, ex);
            return null;
        }

        log.info("Received topology");
        return receivedTopology;
    }

    public TopologyNode getUpdatedTopology() {
        final Duration TOPOLOGY_POLLING_INTERVAL = Duration.ofSeconds(10);
        final short MAX_TOPOLOGY_REQUESTS = 40;

        return getUpdatedTopology(TOPOLOGY_POLLING_INTERVAL, MAX_TOPOLOGY_REQUESTS);
    }

    public TopologyNode getUpdatedTopology(Duration pollingInterval, int maxRequests) {
        log.info("Trying to get a newer version of the topology ( polling timeout: '{}', max requests: '{}')", pollingInterval, maxRequests);
        int requestsSent = 0;
        TopologyNode receivedTopology;

        try {

            receivedTopology = topologyClient.getMinimalTopology();
            if (receivedTopology == null) {
                log.error("Could not get the minimal topology");
                return null;
            }

            requestsSent++;
            if (requestsSent == maxRequests) {
                log.error("Requested a newer version of the topology but did not get it");
                return null;
            }
        } catch (Exception ex) {
            String errorStr = "Could not get the topology";
            log.error(errorStr, ex);
            return null;
        }
        log.debug("Received topology");
        return receivedTopology;
    }

    public TopologyNode getTopologyNode(TopologyNode topology, String nodeId) {
        if (topology == null) {
            log.error("Could not get node with ID '{}' because the topology node is null", nodeId);
            return null;
        }

        log.debug("Getting node with ID '{}'", nodeId);
        try {
            TopologyNode targetNode = findChildNodeById(topology, nodeId);
            if (targetNode == null) {
                log.error("Could not get node with ID '{}'", nodeId);
                return null;
            }

            log.debug("Finished getting the node with ID '{}'", nodeId);
            return targetNode;
        } catch (Exception ex) {
            String errorStr = String.format("Could not get node with ID '%s'", nodeId);
            log.error(errorStr, ex);
            return null;
        }
    }

    public TopologyNode getTopologyNodeByName(TopologyNode topology, String nodeName) {
        if (topology == null) {
            log.error("Could not get node with ID '{}' because the topology node is null", nodeName);
            return null;
        }

        log.info("Getting node with ID '{}'", nodeName);
        try {
            TopologyNode targetNode = findChildNodeByName(topology, nodeName);
            if (targetNode == null) {
                log.error("Could not get node with ID '{}'", nodeName);
                return null;
            }

            log.info("Finished getting the node with ID '{}'", nodeName);
            return targetNode;
        } catch (Exception ex) {
            String errorStr = String.format("Could not get node with ID '%s'", nodeName);
            log.error(errorStr, ex);
            return null;
        }
    }


    private TopologyNode findChildNodeById(TopologyNode node, String nodeId) {
        String currentNodeId = node.getId();
        if (Objects.equals(currentNodeId, nodeId)) {
            return node;
        }

        List<TopologyNode> childArray = node.getChildren();
        if (childArray == null)
            return null;

        for (TopologyNode currentChild : childArray) {
            TopologyNode result = findChildNodeById(currentChild, nodeId);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private TopologyNode findChildNodeByName(TopologyNode node, String nodeName) {
        String currentNodeName = node.getName();
        if (Objects.equals(currentNodeName, nodeName)) {
            return node;
        }

        List<TopologyNode> childArray = node.getChildren();
        if (childArray == null)
            return null;

        for (TopologyNode currentChild : childArray) {
            TopologyNode result = findChildNodeByName(currentChild, nodeName);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
}