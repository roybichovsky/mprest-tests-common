package com.mprest.tests.common.clients;

import com.mprest.tests.common.data.GenieEntityDataCollectionClient;

import java.util.concurrent.Future;

/**
 * Created by leonidr on 19/08/2018.
 */
public interface EntityDataClient {
    Boolean deleteEntities(String type);
    Future<Boolean> deleteEntitiesAsync(String type);
    GenieEntityDataCollectionClient getEntities(String type);
    Future<GenieEntityDataCollectionClient> getEntitiesAsync(String type);

    GenieEntityDataCollectionClient getMinimalTopology();
    Future<GenieEntityDataCollectionClient> getMinimalTopologyAsync();
}
