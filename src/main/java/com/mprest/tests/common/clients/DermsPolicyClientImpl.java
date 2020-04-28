package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.DermsPolicyClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.data.DermsPolicy;
import com.mprest.tests.common.utilities.BaseServiceClient;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.concurrent.Future;

import static io.restassured.RestAssured.given;

@Slf4j
public class DermsPolicyClientImpl extends BaseServiceClient implements DermsPolicyClient {

    DermsPolicyClientImpl(SecuredServerConfiguration secured, DermsPolicyClientConfiguration clientConfig) {
        super(DermsPolicyClientImpl.class, secured, clientConfig);
    }

    @Override
    public DermsPolicy getPolicy() {
        Future<DermsPolicy> futureResult = getPolicyAsync();
        return extractAsyncResult(futureResult, "could not request the DERMs policy.");
    }

    @Override
    public Future<DermsPolicy> getPolicyAsync() {
        String url = baseUrl;

        return super.supplyAsync(
                () -> {
                    Response res;
                    log.debug("Requesting DERMs policy from the url: '{}'.", url);
                    res = given()
                            .when()
                            .get(url)
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.OK.value())
                            .extract()
                            .response();
                    return res.body().as(DermsPolicy.class);
                }
        );
    }

    @Override
    public boolean setPolicy(DermsPolicy policy) {
        Future<Boolean> futureResult = setPolicyAsync(policy);
        return extractAsyncResult(futureResult, "could not set the DERMs policy.", false);
    }

    @Override
    public Future<Boolean> setPolicyAsync(DermsPolicy policy) {
        String url = baseUrl;

        return super.supplyAsync(
                () -> {
                    log.debug("Setting DERMs policy with the url: '{}'.", url);
                    given()
                            .contentType(ContentType.JSON)
                            .body(policy)
                            .when()
                            .post(url)
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.OK.value());
                    return true;
                });
    }
}
