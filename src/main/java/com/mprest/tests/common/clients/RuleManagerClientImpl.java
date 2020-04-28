package com.mprest.tests.common.clients;


import com.mprest.tests.common.configuration.RuleManagerClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.data.RuleEngine.RuleData;
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
public class RuleManagerClientImpl extends BaseServiceClient implements RuleManagerClient {

    public RuleManagerClientImpl(SecuredServerConfiguration securedConfiguration, RuleManagerClientConfiguration ruleManagerClientConfiguration) {
        super(RuleManagerClientImpl.class, securedConfiguration, ruleManagerClientConfiguration);
    }

    @Override
    public Optional<RuleData> getRule(String id) {
//        check for DemandResponseClientImpl class
        Future<Optional<RuleData>> ruleFuture = getRuleAsync(id);
        return extractOptionalAsyncResult(ruleFuture, "Failed to get rule");
    }

    @Override
    public Future<Optional<RuleData>> getRuleAsync(String id) {
        String url = baseUrl + "/{id}";
        log.debug("getting from URL '{}'", url);

        return super.supplyAsync(
                () -> {
                    Response response =
                            given()
                                    .pathParam("id", id)
                                    .when()
                                    .get(url)
                                    .then()
                                    .contentType(ContentType.JSON)
                                    .assertThat()
                                    .statusCode(HttpStatus.OK.value())
                                    .extract()
                                    .response();
                    RuleData ruleData = response.body().as(RuleData.class);
                    if (ruleData == null)
                        return Optional.empty();

                    return Optional.of(ruleData);
                }
        );
    }

    @Override
    public Optional<RuleData> createRule(RuleData rule) {
        Future<Optional<RuleData>> ruleFuture = createRuleAsync(rule);
        return extractOptionalAsyncResult(ruleFuture, "Failed to create rule");
    }

    @Override
    public Future<Optional<RuleData>> createRuleAsync(RuleData rule) {
        String url = baseUrl;
        log.debug("posting to URL '{}'", url);

        return super.supplyAsync(
                () -> {
                    Response response = given()
                                    .contentType(ContentType.JSON)
                                    .body(rule)
                            .when()
                                    .post(url)
                            .then()
                            .assertThat()
                                    .statusCode(HttpStatus.CREATED.value())
                            .extract()
                            .response();
                    ResponseBody body = response.getBody();
                    return Optional.of(body.as(RuleData.class));
                }
        );
    }

    @Override
    public Boolean deleteRule(String id) {
        Future<Boolean> booleanFuture = deleteRuleAsync(id);
        return extractAsyncResult(booleanFuture, "could not delete rule", false);
    }

    @Override
    public Future<Boolean> deleteRuleAsync(String id) {
        String url = baseUrl + "/{id}";
        log.debug("posting to URL '{}/{}'", url, id);

        return super.supplyAsync(
                () -> {
                    given()
                            .pathParam("id", id)
                            .when()
                            .delete(url)
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.NO_CONTENT.value());
                    return true;
                }
        );
    }
}
