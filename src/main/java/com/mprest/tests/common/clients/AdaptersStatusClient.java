package com.mprest.tests.common.clients;

import com.mprest.tests.common.data.health.Health;
import com.mprest.tests.common.data.health.HealthStatus;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Future;

public interface AdaptersStatusClient {
    Health getAdapterHealth(String adapterName);

    Future<Health> getAdapterHealthAsync(String adapterName);

    Boolean waitForAdapterHealthStatus(String adapterName, List<HealthStatus> statusList, Duration pollingInterval, Duration timeout);

    Future<Boolean> waitForAdapterHealthStatusAsync(String adapterName, List<HealthStatus> statusList, Duration pollingInterval, Duration timeout);
}
