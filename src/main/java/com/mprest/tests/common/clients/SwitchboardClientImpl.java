package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.configuration.SwitchboardClientConfiguration;
import com.mprest.tests.common.data.SwitchingEvent;
import com.mprest.tests.common.utilities.BaseServiceClient;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

import static io.restassured.RestAssured.given;

@Slf4j
public class SwitchboardClientImpl extends BaseServiceClient implements SwitchboardClient {
    SwitchboardClientImpl(SecuredServerConfiguration securedConfiguration, SwitchboardClientConfiguration switchboardClientConfiguration) {
        super(SwitchboardClientImpl.class, securedConfiguration, switchboardClientConfiguration);
    }

    @Override
    public List<SwitchingEvent> getAllStates(List<String> switches, String match) {
        Future<List<SwitchingEvent>> listFuture = getAllStatesAsync(switches, match);
        return extractAsyncResult(listFuture, "failed to get switches list");
    }

    @Override
    public Future<List<SwitchingEvent>> getAllStatesAsync(List<String> switches, String match) {
        String url = baseUrl + "/states";
        log.info("getting switching events from URL '{}'", url);

        return super.supplyAsync(() -> {
            Response response =
                    given()
                            .queryParam("switchId", switches)
                            .queryParam("match", match)
                    .when()
                            .get(url)
                    .then()
                            .contentType(ContentType.JSON)
                    .assertThat()
                            .statusCode(HttpStatus.OK.value())
                    .extract()
                            .response();

            ResponseBody body = response.body();
            if (body == null)
                return Collections.emptyList();

            return Arrays.asList(response.body().as(SwitchingEvent[].class));
        });
    }

    @Override
    public Boolean save(List<SwitchingEvent> events) {
        Future<Boolean> listFuture = saveAsync(events);
        return extractAsyncResult(listFuture, "failed to save switching events");
    }

    @Override
    public Future<Boolean> saveAsync(List<SwitchingEvent> events) {
        String url = baseUrl + "/states";
        log.info("saving switching events to URL '{}'", url);

        return super.supplyAsync(() -> {
                    Response response =
                            given()
                                    .contentType(ContentType.JSON)
                                    .body(events)
                            .when()
                                    .post(url)
                            .then()
                            .assertThat()
                                    .statusCode(HttpStatus.OK.value())
                            .extract()
                                    .response();

                SwitchingEvent[] saved = response.body().as(SwitchingEvent[].class);
                boolean result = saved.length == events.size();
                log.debug("saving switching events result: '{}'", result);
                return result;
            }
        );
    }

    @Override
    public List<SwitchingEvent> getAllStatesSnapshot(List<String> switches, Long recordId, Instant time) {
        Future<List<SwitchingEvent>> listFuture = getAllStatesSnapshotAsync(switches, recordId, time);
        return extractAsyncResult(listFuture, "failed to get switches snapshot list");
    }

    @Override
    public Future<List<SwitchingEvent>> getAllStatesSnapshotAsync(List<String> switches, Long recordId, Instant time) {
        String url = baseUrl + "/states/snapshot";
        log.info("getting switching events snapshot from URL '{}'", url);

        return super.supplyAsync(() -> {
            Response response =
                    given()
                            .queryParam("switchId", switches)
                            .queryParam("recordId", recordId)
                            .queryParam("time", time)
                    .when()
                            .get(url)
                    .then()
                            .contentType(ContentType.JSON)
                    .assertThat()
                            .statusCode(HttpStatus.OK.value())
                    .extract()
                            .response();

            ResponseBody body = response.body();
            if (body == null)
                return Collections.emptyList();

            return Arrays.asList(response.body().as(SwitchingEvent[].class));
        });
    }
}
