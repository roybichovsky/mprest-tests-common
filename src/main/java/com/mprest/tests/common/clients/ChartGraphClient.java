package com.mprest.tests.common.clients;

import com.mprest.tests.common.data.ChartGraph;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.Future;

public interface ChartGraphClient {
    Optional<ChartGraph> getChart(String chartGraphName, String resourceId, Instant dateTimeAnchor);
    Future<Optional<ChartGraph>> getChartAsync(String chartGraphName, String resourceId, Instant dateTimeAnchor);

    Optional<ChartGraph> getFlexibilityChart(String resourceId, Instant dateTimeAnchor);
    Future<Optional<ChartGraph>> getFlexibilityChartAsync(String resourceId, Instant dateTimeAnchor);

    Optional<ChartGraph> getMarketPriceChart(String resourceId, Instant dateTimeAnchor);
    Future<Optional<ChartGraph>> getMarketPriceChartAsync(String resourceId, Instant dateTimeAnchor);

    Optional<ChartGraph> getLoadForecastChart(String resourceId, Instant dateTimeAnchor);
    Future<Optional<ChartGraph>> getLoadForecastChartAsync(String resourceId, Instant dateTimeAnchor);

    Optional<ChartGraph> getDerPlanChart(String resourceId, Instant dateTimeAnchor);
    Future<Optional<ChartGraph>> getDerPlanChartAsync(String resourceId, Instant dateTimeAnchor);

    Optional<ChartGraph> getAvailableEnergyChart(String resourceId, Instant dateTimeAnchor);
    Future<Optional<ChartGraph>> geAvailableEnergyChartAsync(String resourceId, Instant dateTimeAnchor);

    Optional<ChartGraph> getDemandResponseChart(Instant dateTimeAnchor);
    Future<Optional<ChartGraph>> getDemandResponseChartAsync(Instant dateTimeAnchor);
}