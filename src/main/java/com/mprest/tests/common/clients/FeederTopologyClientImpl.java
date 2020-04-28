package com.mprest.tests.common.clients;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mprest.tests.common.configuration.FeederTopologyClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.data.feeders.FeederNode;
import com.mprest.tests.common.data.feeders.FeederTxWeight;
import com.mprest.tests.common.utilities.BaseServiceClient;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.vavr.Tuple2;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

@Slf4j
public class FeederTopologyClientImpl extends BaseServiceClient implements FeederTopologyClient {

    FeederTopologyClientImpl(SecuredServerConfiguration securedConfiguration, FeederTopologyClientConfiguration feederTopologyClientConfiguration) {
        super(FeederTopologyClientImpl.class, securedConfiguration, feederTopologyClientConfiguration);
    }

    @Override
    public Boolean triggerTopologyCalculations() {
        Future<Boolean> resultFuture = triggerTopologyCalculationsAsync();
        return extractAsyncResult(resultFuture, "failed to trigger manual topology calculation");
    }

    @Override
    public Future<Boolean> triggerTopologyCalculationsAsync() {
        String url = baseUrl + "/trigger";
        log.debug("triggering manual topology calculation from URL '{}'", url);

        return super.supplyAsync(() -> {
            when()
                    .get(url)
            .then()
            .assertThat()
                    .statusCode(HttpStatus.OK.value());

            return true;
        });
    }

    @Override
    public List<Tuple2<String, String>> getTxToFeedersMapping() {
        Future<List<Tuple2<String, String>>> resultFuture = getTxToFeedersMappingAsync();
        return extractAsyncResult(resultFuture, "failed to get TX to feeders mapping");
    }

    @Override
    public Future<List<Tuple2<String, String>>> getTxToFeedersMappingAsync() {
        String url = baseUrl + "/txToFeeders";
        log.debug("getting TX to feeders mapping from URL '{}'", url);

        return super.supplyAsync(() -> {
            Response response =
                    when()
                            .get(url)
                    .then()
                            .contentType(ContentType.JSON)
                    .assertThat()
                            .statusCode(HttpStatus.OK.value())
                    .extract()
                            .response();

            return extractWrappedResponseBody(response, Collections.emptyList());
        });
    }

    @Override
    public List<String> getTxToFeedersMappingForTx(String txId) {
        Future<List<String>> resultFuture = getTxToFeedersMappingForTxAsync(txId);
        return extractAsyncResult(resultFuture, "failed to get TX to feeders mapping for TX");
    }

    @Override
    public Future<List<String>> getTxToFeedersMappingForTxAsync(String txId) {
        String url = baseUrl + "/txToFeeders/{txId}";
        log.debug("getting TX to feeders mapping for TX from URL '{}'", url);

        return super.supplyAsync(() -> {
            Response response =
                    given()
                            .pathParam("txId", txId)
                    .when()
                            .get(url)
                    .then()
                            .contentType(ContentType.JSON)
                    .assertThat()
                            .statusCode(HttpStatus.OK.value())
                    .extract()
                            .response();

            return extractWrappedResponseBody(response, Collections.emptyList());
        });
    }

    @Override
    public List<Tuple2<String, String>> getFeedersToTxMapping(String match) {
        Future<List<Tuple2<String, String>>> resultFuture = getFeedersToTxMappingAsync(match);
        return extractAsyncResult(resultFuture, "failed to get feeders to TX mapping");
    }

    @Override
    public Future<List<Tuple2<String, String>>> getFeedersToTxMappingAsync(String match) {
        String url = baseUrl + "/feedersToTx";
        log.debug("getting feeders to TX mapping from URL '{}'", url);

        return super.supplyAsync(() -> {
            Response response =
                    given()
                            .queryParam("match", match)
                    .when()
                            .get(url)
                    .then()
                            .contentType(ContentType.JSON)
                    .assertThat()
                            .statusCode(HttpStatus.OK.value())
                    .extract()
                            .response();

            return extractWrappedResponseBody(response, Collections.emptyList());
        });
    }

    @Override
    public List<String> getFeedersToTxMappingForFeeder(String feederId) {
        Future<List<String>> resultFuture = getFeedersToTxMappingForFeederAsync(feederId);
        return extractAsyncResult(resultFuture, "failed to get feeders to TX mapping for feeder");
    }

    @Override
    public Future<List<String>> getFeedersToTxMappingForFeederAsync(String feederId) {
        String url = baseUrl + "/feedersToTx/{feederId}";
        log.debug("getting feeders to TX mapping for feeder from URL '{}'", url);

        return super.supplyAsync(() -> {
            Response response =
                    given()
                            .pathParam("feederId", feederId)
                    .when()
                            .get(url)
                    .then()
                            .contentType(ContentType.JSON)
                    .assertThat()
                            .statusCode(HttpStatus.OK.value())
                    .extract()
                            .response();

            return extractWrappedResponseBody(response, Collections.emptyList());
        });
    }

    @Override
    public List<FeederTxWeight> getFeedersTxWithWeight(String match) {
        Future<List<FeederTxWeight>> resultFuture = getFeedersTxWithWeightAsync(match);
        return extractAsyncResult(resultFuture, "failed to get feeders TX with weight");
    }

    @Override
    public Future<List<FeederTxWeight>> getFeedersTxWithWeightAsync(String match) {
        String url = baseUrl + "/feederTxWeight";
        log.debug("getting Feeders TX with weight from URL '{}'", url);

        return super.supplyAsync(() -> {
            Response response =
                    given()
                            .queryParam("match", match)
                    .when()
                            .get(url)
                    .then()
                            .contentType(ContentType.JSON)
                    .assertThat()
                            .statusCode(HttpStatus.OK.value())
                    .extract()
                            .response();

            return extractWrappedResponseBody(response, Collections.emptyList());
        });
    }

    @Override
    public List<Tuple2<String, Set<String>>> getMultipleFedTransformers() {
        Future<List<Tuple2<String, Set<String>>>> resultFuture = getMultipleFedTransformersAsync();
        return extractAsyncResult(resultFuture, "failed to get multiple fed transformers");
    }

    @Override
    public Future<List<Tuple2<String, Set<String>>>> getMultipleFedTransformersAsync() {
        String url = baseUrl + "/multipuleFedTransformers";
        log.debug("getting multiple fed transformers from URL '{}'", url);

        return super.supplyAsync(() -> {
            Response response =
                    when()
                            .get(url)
                    .then()
                            .contentType(ContentType.JSON)
                    .assertThat()
                            .statusCode(HttpStatus.OK.value())
                    .extract()
                            .response();

            return extractWrappedResponseBody(response, Collections.emptyList());
        });
    }

    @Override
    public List<Tuple2<String, Integer>> getFeederWeight() {
        Future<List<Tuple2<String, Integer>>> resultFuture = getFeederWeightAsync();
        return extractAsyncResult(resultFuture, "failed to get feeder Weight");
    }

    @Override
    public Future<List<Tuple2<String, Integer>>> getFeederWeightAsync() {
        String url = baseUrl + "/feederWeight";
        log.debug("getting feeder weight from URL '{}'", url);

        return super.supplyAsync(() -> {
            Response response =
                    when()
                            .get(url)
                    .then()
                            .contentType(ContentType.JSON)
                    .assertThat()
                            .statusCode(HttpStatus.OK.value())
                    .extract()
                            .response();

            return extractWrappedResponseBody(response, Collections.emptyList());
        });
    }

    @Override
    public List<Tuple2<String, Integer>> getFeederWeightByFeeder(String feederId) {
        Future<List<Tuple2<String, Integer>>> resultFuture = getFeederWeightByFeederAsync(feederId);
        return extractAsyncResult(resultFuture, "failed to get feeder Weight by feeder");
    }

    @Override
    public Future<List<Tuple2<String, Integer>>> getFeederWeightByFeederAsync(String feederId) {
        String url = baseUrl + "/feederWeight/{feederId}";
        log.debug("getting feeder weight by feeder from URL '{}'", url);

        return super.supplyAsync(() -> {
            Response response =
                    given()
                            .pathParam("feederId", feederId)
                    .when()
                            .get(url)
                    .then()
                            .contentType(ContentType.JSON)
                    .assertThat()
                            .statusCode(HttpStatus.OK.value())
                    .extract()
                            .response();

            return extractWrappedResponseBody(response, Collections.emptyList());
        });
    }

    @Override
    public List<FeederNode> getEnergizedFeeders(Set<String> feeders) {
        Future<List<FeederNode>> resultFuture = getEnergizedFeedersAsync(feeders);
        return extractAsyncResult(resultFuture, "failed to get energized feeders");
    }

    @Override
    public Future<List<FeederNode>> getEnergizedFeedersAsync(Set<String> feeders) {
        String url = baseUrl + "/energizedFeeders";
        log.debug("getting energized feeders from URL '{}'", url);

        return super.supplyAsync(() -> {
            Response response =
                    given()
                            .queryParam("feederList", feeders)
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

            Map<String, Object> resultMap = body.as(Map.class);
            return (List<FeederNode>) resultMap.get("topology");
        });
    }

    private <T> T extractWrappedResponseBody(Response response, T defaultResult) {
        ResponseBody body = response.body();
        if (body == null)
            return defaultResult;

        WrappedResult<T> wrapped = body.as(WrappedResult.class);
        return wrapped.result;
    }

    @Data
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class WrappedResult<T> {
        T result;
    }
}
