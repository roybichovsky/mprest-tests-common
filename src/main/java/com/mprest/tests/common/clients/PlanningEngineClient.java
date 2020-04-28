package com.mprest.tests.common.clients;

import com.mprest.tests.common.data.PlanningEngineStatus;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Future;

public interface PlanningEngineClient {
    Boolean triggerMarketPriceClustering();
    Future<Boolean> triggerMarketPriceClusteringAsync();

    Boolean triggerPlanner(PlanningEngineStatus.CalculationStatus calculationStatus, Duration pollingInterval, Duration timeout);
    Future<Boolean> triggerPlannerAsync(PlanningEngineStatus.CalculationStatus calculationStatus, Duration pollingInterval, Duration timeout);

    Boolean triggerPlanner(PlanningEngineStatus.CalculationStatus calculationStatus, PlanningEngineStatus.Trigger triggeredBy, Duration pollingInterval, Duration timeout);
    Future<Boolean> triggerPlannerAsync(PlanningEngineStatus.CalculationStatus calculationStatus, PlanningEngineStatus.Trigger triggeredBy, Duration pollingInterval, Duration timeout);

    Optional<PlanningEngineStatus> getPlanningEngineStatus();
    Future<Optional<PlanningEngineStatus>> getPlanningEngineStatusAsync();

    Boolean waitForStatus(PlanningEngineStatus.CalculationStatus calculationStatus, PlanningEngineStatus.Trigger triggeredBy, Duration pollingInterval, Duration timeout);
    Future<Boolean> waitForStatusAsync(PlanningEngineStatus.CalculationStatus calculationStatus, PlanningEngineStatus.Trigger triggeredBy, Duration pollingInterval, Duration timeout);
}
