package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.AlertsClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.data.SingleAlert;
import com.mprest.tests.common.utilities.BaseServiceClient;

import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;

@Slf4j
public class AlertsClientImpl extends BaseServiceClient implements AlertsClient {

    public AlertsClientImpl(SecuredServerConfiguration securedConfiguration, AlertsClientConfiguration alertsConfiguration) {
        super(AlertsClientImpl.class, securedConfiguration, alertsConfiguration);
    }

    public List<SingleAlert> getAlerts(String resourceId) {
        Future<List<SingleAlert>> futureAlerts = getAlertsAsync(resourceId);
        return extractAsyncResult(futureAlerts, "could not get alerts", Collections.emptyList());
    }

    public List<SingleAlert> getAllAlerts(Instant fromDate, Instant toDate) {
        Future<List<SingleAlert>> futureAllAlerts = getAllAlertsAsync(fromDate, toDate);
        return extractAsyncResult(futureAllAlerts, "could not get alerts", Collections.emptyList());
    }

    public Future<List<SingleAlert>> getAlertsAsync(String resourceId) {
        String url = baseUrl + "/alerts/{resourceId}";
        log.debug("getting alerts from URL '{}'", url);

        return super.supplyAsync(() -> {
                    Response response =
                            given()
                                    .pathParam("resourceId", resourceId)
                            .when()
                                    .get(url)
                            .then()
                                    .contentType(ContentType.JSON)
                            .assertThat()
                                    .statusCode(HttpStatus.OK.value())
                            .extract()
                                    .response();
                    return Arrays.asList(response.body().as(SingleAlert[].class));
                }
        );
    }

    public Future<List<SingleAlert>> getAllAlertsAsync(Instant fromDate, Instant toDate) {
        String url = baseUrl + "/alerts";
        log.debug("getting alerts from URL '{}'", url);

        return super.supplyAsync(() -> {
                    Response response =
                            given()
                                    .queryParam("fromDate", fromDate)
                                    .queryParam("toDate", toDate)
                            .when()
                                    .get(url)
                            .then()
                                    .contentType(ContentType.JSON)
                            .assertThat()
                                    .statusCode(HttpStatus.OK.value())
                            .extract()
                                    .response();
                    return Arrays.asList(response.body().as(SingleAlert[].class));
                }
        );
    }
}