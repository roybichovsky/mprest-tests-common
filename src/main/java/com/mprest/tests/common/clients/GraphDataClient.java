package com.mprest.tests.common.clients;

import java.util.Map;
import java.util.concurrent.Future;

public interface GraphDataClient {
    Map getGraphData(String resourceId, String graphList);

    Future<Map> getGraphDataAsync(String resourceId, String graphList);
}
