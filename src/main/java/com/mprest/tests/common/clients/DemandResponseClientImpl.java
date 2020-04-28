package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.DemandResponseClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.data.DemandModel;
import com.mprest.tests.common.utilities.BaseServiceClient;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.Future;

import static io.restassured.RestAssured.given;

@Slf4j
public class DemandResponseClientImpl extends BaseServiceClient implements DemandResponseClient {

    DemandResponseClientImpl(SecuredServerConfiguration securedConfiguration, DemandResponseClientConfiguration demandResponseConfiguration) {
        super(DemandResponseClientImpl.class, securedConfiguration, demandResponseConfiguration);
    }

    @Override
    public HttpStatus saveDemandResponse(DemandModel demandModel) {
        Future<HttpStatus> httpStatusFuture = saveDemandResponseAsync(demandModel);
        return extractAsyncResult(httpStatusFuture, "could not save demand response", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public Boolean deleteDemandResponse(String demandResponseId) {
        Future<Boolean> booleanFuture = deleteDemandResponseAsync(demandResponseId);
        return extractAsyncResult(booleanFuture, "could not delete demand response", false);
    }

    @Override
    public Optional<DemandModel[]> getDemandResponseBetweenDates(Instant fromDate, Instant toDate) {
        Future<Optional<DemandModel[]>> listFuture = getDemandResponseBetweenDatesAsync(fromDate, toDate);
        return extractOptionalAsyncResult(listFuture, "Failed to get demand responses list");
    }

    @Override
    public Future<Optional<DemandModel[]>> getDemandResponseBetweenDatesAsync(Instant fromDate, Instant toDate) {
        String url = baseUrl;
        log.info("posting to URL '{}'", url);

        return super.supplyAsync(
                () -> {
                    Response response =
                            given()
                                    .queryParam("fromDate", fromDate.toString())
                                    .queryParam("toDate", toDate.toString())
                            .when()
                                    .get(url)
                            .then()
                                    .contentType(ContentType.JSON)
                            .assertThat()
                                    .statusCode(HttpStatus.OK.value())
                            .extract()
                                    .response();
                    DemandModel[] demandModels = response.body().as(DemandModel[].class);
                    log.debug("Fetching demand-response '{}' entries for dates from '{}' to '{}' (UTC)", demandModels.length, fromDate, toDate);
                    return Optional.of(demandModels);
                }
        );
    }

    @Override
    public Future<HttpStatus> saveDemandResponseAsync(DemandModel demandModel) {
        String url = baseUrl;
        log.debug("posting to URL '{}'", url);

        return super.supplyAsync(
                () -> {
                    Response response =
                            given()
                                .contentType(ContentType.JSON)
                                .body(demandModel)
                            .when()
                                .post(url);

                    return HttpStatus.valueOf(response.getStatusCode());
                }
        );
    }

    @Override
    public Future<Boolean> deleteDemandResponseAsync(String demandResponseId) {
        String url = baseUrl + "/{Id}";
        log.debug("posting to URL '{}/{}'", url, demandResponseId);

        return super.supplyAsync(
                () -> {
                    given()
                        .pathParam("Id", demandResponseId)
                    .when()
                        .delete(url)
                    .then()
                    .assertThat()
                        .statusCode(HttpStatus.OK.value());
                    return true;
                }
        );
    }
}
