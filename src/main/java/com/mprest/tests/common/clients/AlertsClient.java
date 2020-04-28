package com.mprest.tests.common.clients;

import com.mprest.tests.common.data.SingleAlert;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.Future;

public interface AlertsClient {
    List<SingleAlert> getAlerts(String resourceId);
    Future<List<SingleAlert>> getAlertsAsync(String resourceId);

    List<SingleAlert> getAllAlerts(Instant fromDate, Instant toDate);
    Future<List<SingleAlert>> getAllAlertsAsync(Instant fromDate, Instant toDate);
}
