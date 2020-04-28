package com.mprest.tests.common.clients;

import com.mprest.tests.common.data.DtxForecastChartAndAlerts;

import java.util.Optional;
import java.util.concurrent.Future;

public interface DistributionTransformersClient {
    Optional<DtxForecastChartAndAlerts> getDtxForecastAndAlerts(String dtxId);
    Future<Optional<DtxForecastChartAndAlerts>> getDtxForecastAndAlertsAsync(String dtxId);
}