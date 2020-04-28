package com.mprest.tests.common.clients;

import com.mprest.tests.common.data.health.Health;
import com.mprest.tests.common.data.health.HealthStatus;
import com.mprest.tests.common.configuration.AdaptersStatusClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.utilities.BaseServiceClient;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.with;
import static org.junit.Assert.*;

@Slf4j
public class AdaptersStatusClientImpl extends BaseServiceClient implements AdaptersStatusClient {

    public AdaptersStatusClientImpl(SecuredServerConfiguration securedConfiguration, AdaptersStatusClientConfiguration adaptersStatusClientConfiguration) {
        super(AdaptersStatusClientImpl.class, securedConfiguration, adaptersStatusClientConfiguration);
    }

    @Override
    public Health getAdapterHealth(String adapterName) {
        Future<Health> futureHealth = getAdapterHealthAsync(adapterName);
        return extractAsyncResult(futureHealth, "could not get adapters health");
    }

    @Override
    public Future<Health> getAdapterHealthAsync(String adapterName) {
        String url = baseUrl + "/adapters/health";
        log.debug("getting from URL '{}'", url);

        return super.supplyAsync(() -> {
                    Response response =
                            given()
                            .when()
                                    .get(url)
                            .then()
                                    .contentType(ContentType.JSON)
                            .assertThat()
                                    .statusCode(HttpStatus.OK.value())
                            .extract()
                                    .response();
                    Health[] healthData = response.body().as(Health[].class);
                    return getAdapterHealth(healthData, adapterName);
                }
        );
    }

    @Override
    public Boolean waitForAdapterHealthStatus(String adapterName, List<HealthStatus> statusList, Duration pollingInterval, Duration timeout) {
        Future<Boolean> booleanFuture = waitForAdapterHealthStatusAsync(adapterName, statusList, pollingInterval, timeout);
        return extractAsyncResult(booleanFuture, "could not wait for adapter health status", timeout, false);
    }

    @Override
    public Future<Boolean> waitForAdapterHealthStatusAsync(String adapterName, List<HealthStatus> statusList, Duration pollingInterval, Duration timeout) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        return super.supplyAsync(() -> {
            with().
                    pollInterval(pollingInterval.getSeconds(), TimeUnit.SECONDS).
                    await().atMost(timeout.getSeconds(), TimeUnit.SECONDS).
                    until(() -> {
                                final Health health = getAdapterHealth(adapterName);
                                boolean isMatch = isHealthStatusMatch(health.getStatus(), statusList);
                                atomicBoolean.set(isMatch);
                                return isMatch;
                            }
                    );
            return atomicBoolean.get();
        });
    }

    private Health getAdapterHealth(Health[] health, String adapterName) {
        assertFalse("adapterName should not be empty", StringUtils.isEmpty(adapterName));

        Health item = Arrays
                .stream(health)
                .filter(healthItem -> adapterName.equals(healthItem.getName()))
                .findAny()
                .orElse(null);

        assertNotNull("health item", item);
        log.debug("got health item '{}'", item);

        return item;
    }

    private boolean isHealthStatusMatch(HealthStatus status, List<HealthStatus> statusesToMatch) {
        if (status == null) {
            log.warn("ignoring null health status");
            return false;
        }

        if (statusesToMatch == null || statusesToMatch.isEmpty()) {
            log.warn("ignoring null or empty statuses to match list");
            return false;
        }

        return statusesToMatch.contains(status);
    }
}
