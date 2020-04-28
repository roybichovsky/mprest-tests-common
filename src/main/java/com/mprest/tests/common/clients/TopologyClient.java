package com.mprest.tests.common.clients;


import com.mprest.tests.common.data.TopologyNode;

import java.util.concurrent.Future;

public interface TopologyClient {
    TopologyNode getMinimalTopology();

    Future<TopologyNode> getMinimalTopologyAsync();
}