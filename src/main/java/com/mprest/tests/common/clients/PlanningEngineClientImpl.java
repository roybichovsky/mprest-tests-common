package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.PlanningEngineClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.data.PlanningEngineStatus;
import com.mprest.tests.common.utilities.BaseServiceClient;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.restassured.RestAssured.when;
import static org.awaitility.Awaitility.with;

@Slf4j
public class PlanningEngineClientImpl extends BaseServiceClient implements PlanningEngineClient {

    PlanningEngineClientImpl(SecuredServerConfiguration securedConfiguration, PlanningEngineClientConfiguration planningEngineConfiguration) {
        super(PlanningEngineClientImpl.class, securedConfiguration, planningEngineConfiguration);
    }

    public Boolean triggerMarketPriceClustering() {
        Future<Boolean> futureResult = triggerMarketPriceClusteringAsync();
        return extractAsyncResult(futureResult, "Failed trigger planning engine clustering");
    }

    public Future<Boolean> triggerMarketPriceClusteringAsync() {
        String url = baseUrl + "/triggermarketPriceclustering";
        log.debug("trigger market price clustering async from URL '{}'", url);

        return super.supplyAsync(() -> {
            when()
                    .get(url)
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.OK.value());
            return true;
        });
    }

    public Boolean triggerPlanner(PlanningEngineStatus.CalculationStatus calculationStatus, Duration pollingInterval, Duration timeout) {
        return triggerPlanner(calculationStatus, null, pollingInterval, timeout);
    }

    public Boolean triggerPlanner(PlanningEngineStatus.CalculationStatus calculationStatus, PlanningEngineStatus.Trigger triggeredBy, Duration pollingInterval, Duration timeout) {
        Future<Boolean> booleanFuture = triggerPlannerAsync(calculationStatus, triggeredBy, pollingInterval, timeout);
        return extractAsyncResult(booleanFuture, "Failed triggering planning engine status", timeout, false);
    }

    public Future<Boolean> triggerPlannerAsync(PlanningEngineStatus.CalculationStatus calculationStatus, Duration pollingInterval, Duration timeout) {
        return triggerPlannerAsync(calculationStatus, null, pollingInterval, timeout);
    }

    public Future<Boolean> triggerPlannerAsync(PlanningEngineStatus.CalculationStatus calculationStatus, PlanningEngineStatus.Trigger triggeredBy, Duration pollingInterval, Duration timeout) {
        return ((CompletableFuture<Boolean>) triggerPlannerAsync()).thenApply(result -> {
            if (result) {
                return waitForStatus(calculationStatus, triggeredBy, pollingInterval, timeout);
            }
            return false;
        });
    }

    public Boolean waitForStatus(PlanningEngineStatus.CalculationStatus calculationStatus, PlanningEngineStatus.Trigger triggeredBy, Duration pollingInterval, Duration timeout) {
        Future<Boolean> booleanFuture = waitForStatusAsync(calculationStatus, triggeredBy, pollingInterval, timeout);
        return extractAsyncResult(booleanFuture, "Failed waiting planning engine status", timeout, false);
    }

    public Future<Boolean> waitForStatusAsync(PlanningEngineStatus.CalculationStatus calculationStatus, PlanningEngineStatus.Trigger triggeredBy, Duration pollingInterval, Duration timeout) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        return super.supplyAsync(() -> {
            with()
                    .pollInterval(pollingInterval.getSeconds(), TimeUnit.SECONDS)
                    .await().atMost(timeout.getSeconds(), TimeUnit.SECONDS)
                    .until(() -> {
                                final Optional<PlanningEngineStatus> planningEngineStatus = getPlanningEngineStatus();
                                boolean isMatch = isPlanningEngineStatusMatch(planningEngineStatus, calculationStatus, triggeredBy);
                                atomicBoolean.set(isMatch);
                                return isMatch;
                            }
                    );
            return atomicBoolean.get();
        });
    }

    private boolean isPlanningEngineStatusMatch(Optional<PlanningEngineStatus> maybePlanningEngineStatus, PlanningEngineStatus.CalculationStatus calculationStatus, PlanningEngineStatus.Trigger triggeredBy) {
        if (!maybePlanningEngineStatus.isPresent())
            return false;

        PlanningEngineStatus planningEngineStatus = maybePlanningEngineStatus.get();
        return calculationStatus.equals(planningEngineStatus.getStatus()) && planningEngineStatus.getTriggeredBy().contains(triggeredBy);
    }

    private Future<Boolean> triggerPlannerAsync() {
        String url = baseUrl + "/triggerplanner";
        log.debug("trigger planner async from URL '{}'", url);

        return super.supplyAsync(() -> {
            when()
                    .post(url)
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.NO_CONTENT.value());
            return true;
        });
    }

    public Optional<PlanningEngineStatus> getPlanningEngineStatus() {
        Future<Optional<PlanningEngineStatus>> futureResult = getPlanningEngineStatusAsync();
        return extractOptionalAsyncResult(futureResult, "Failed getting planning engine status");
    }

    public Future<Optional<PlanningEngineStatus>> getPlanningEngineStatusAsync() {
        String url = baseUrl + "/status";
        log.debug("get planning engine status from URL '{}'", url);

        return super.supplyAsync(
                () -> {
                    Response response = when()
                            .get(url)
                            .then()
                            .extract()
                            .response();

                    log.debug("get planning engine status with result '{}' from URL '{}'", response.getStatusCode(), url);

                    if (response.getStatusCode() != HttpStatus.OK.value())
                        return Optional.of(PlanningEngineStatus.Unknown);

                    ResponseBody body = response.body();
                    if (body == null)
                        return Optional.of(PlanningEngineStatus.Unknown);

                    PlanningEngineStatus status = body.as(PlanningEngineStatus.class);
                    log.debug("got planning engine status '{}' from URL '{}'", status, url);
                    return Optional.of(status);
                }
        );
    }
}
