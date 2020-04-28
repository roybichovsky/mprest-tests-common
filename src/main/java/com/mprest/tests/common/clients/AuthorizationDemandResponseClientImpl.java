package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.DemandResponseClientConfiguration;
import com.mprest.tests.common.configuration.LimitedUserClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.data.DemandModel;
import com.mprest.tests.common.utilities.LimitedBaseServiceClient;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.Future;

import static io.restassured.RestAssured.given;

@Slf4j
public class AuthorizationDemandResponseClientImpl extends LimitedBaseServiceClient implements AuthorizationDemandResponseClient {

    AuthorizationDemandResponseClientImpl(SecuredServerConfiguration securedConfiguration, DemandResponseClientConfiguration demandResponseConfiguration, LimitedUserClientConfiguration limitedUserClientConfiguration) {
        super(AuthorizationDemandResponseClientImpl.class, securedConfiguration, demandResponseConfiguration, limitedUserClientConfiguration);
    }

    public HttpStatus saveDemandResponse(DemandModel demandModel) {
        Future<HttpStatus> httpStatusFuture = saveDemandResponseAsync(demandModel);
        return extractAsyncResult(httpStatusFuture, "could not save demand response", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public HttpStatus validateGetForbidden(Instant fromDate, Instant toDate) {
        HttpStatus expectedHttpStatus = isSecured() ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.FORBIDDEN;
        Future<HttpStatus> listFuture = validateGetForbiddenAsync(fromDate, toDate, expectedHttpStatus);
        return extractAsyncResult(listFuture, "Failed to get demand responses list");
    }

    @Override
    public Optional<DemandModel[]> validateGetSuccess(Instant fromDate, Instant toDate) {
        Future<Optional<DemandModel[]>> successAsync = validateGetSuccessAsync(fromDate, toDate);
        return extractAsyncResult(successAsync, "Failed to get demand responses list");
    }

    public Future<HttpStatus> validateGetForbiddenAsync(Instant fromDate, Instant toDate, HttpStatus expectedHttpStatus) {
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
                            .assertThat()
                                    .statusCode(expectedHttpStatus.value())
                            .extract()
                                    .response();
                    return HttpStatus.valueOf(response.getStatusCode());
                }
        );
    }

    @Override
    public Future<Optional<DemandModel[]>> validateGetSuccessAsync(Instant fromDate, Instant toDate) {
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

    public Future<HttpStatus> saveDemandResponseAsync(DemandModel demandModel) {
        String url = baseUrl;
        log.debug("posting to URL {}", url);

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

}
