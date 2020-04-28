package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.OperationPlanConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.data.SingleOperationPlan;
import com.mprest.tests.common.utilities.BaseServiceClient;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.concurrent.*;

import static io.restassured.RestAssured.given;

@Slf4j
public class OperationPlanClientImpl extends BaseServiceClient implements OperationPlanClient {

    OperationPlanClientImpl(SecuredServerConfiguration secured, OperationPlanConfiguration clientConfig) {
        super(OperationPlanClientImpl.class, secured, clientConfig);
    }

    @Override
    public SingleOperationPlan[] getOperationPlan() {
        Future<SingleOperationPlan[]> futureResult = getOperationPlanAsync();
        return extractAsyncResult(futureResult, "could not request the operation plan.");
    }

    @Override
    public Future<SingleOperationPlan[]> getOperationPlanAsync() {
        String url = baseUrl + "/get";

        return super.supplyAsync(
                () -> {
                        log.debug("Requesting operation plan from the url: '{}'.", url);
                        Response res = given()
                                .when()
                                .get(url)
                                .then()
                                .assertThat()
                                .statusCode(HttpStatus.OK.value())
                                .extract()
                                .response();
                        return res.body().as(SingleOperationPlan[].class);
                }
        );
    }
}
