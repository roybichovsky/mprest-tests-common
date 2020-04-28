package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.DistributionTransformersClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.data.DtxForecastChartAndAlerts;
import com.mprest.tests.common.utilities.BaseServiceClient;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.concurrent.Future;

import static io.restassured.RestAssured.given;

@Slf4j
public class DistributionTransformersClientImpl extends BaseServiceClient implements DistributionTransformersClient {

    public DistributionTransformersClientImpl(SecuredServerConfiguration secured, DistributionTransformersClientConfiguration distributionTransformersClientConfiguration) {
        super(DistributionTransformersClientImpl.class, secured, distributionTransformersClientConfiguration);
    }

    @Override
    public Optional<DtxForecastChartAndAlerts> getDtxForecastAndAlerts(String dtxId) {
        Future<Optional<DtxForecastChartAndAlerts>> futureForeCast = getDtxForecastAndAlertsAsync(dtxId);
        return extractOptionalAsyncResult(futureForeCast, "Could not get forecast Chart");
    }

    @Override
    public Future<Optional<DtxForecastChartAndAlerts>> getDtxForecastAndAlertsAsync(String dtxId) {
        String url = baseUrl + "/forecast/{dtxId}";

        return super.supplyAsync(() -> {
            Response response =
                    given()
                            .pathParam("dtxId", dtxId)
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
                return Optional.empty();

            String result = response.body().asString();
            log.debug("DtxForecastChartAndAlerts string result: '{}'", result);
            return Optional.of(body.as(DtxForecastChartAndAlerts.class));
        });
    }
}
