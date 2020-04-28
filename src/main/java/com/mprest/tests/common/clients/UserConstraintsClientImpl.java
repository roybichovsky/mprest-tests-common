package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.configuration.UserConstraintsClientConfiguration;
import com.mprest.tests.common.data.UserConstraintSaveResult;
import com.mprest.tests.common.data.UserConstraintsUI;
import com.mprest.tests.common.utilities.BaseServiceClient;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.concurrent.Future;

import static io.restassured.RestAssured.given;

@Slf4j
public class UserConstraintsClientImpl extends BaseServiceClient implements UserConstraintsClient {

    UserConstraintsClientImpl(SecuredServerConfiguration securedServerConfiguration, UserConstraintsClientConfiguration userConstraintsClientConfiguration) {
        super(UserConstraintsClientImpl.class, securedServerConfiguration, userConstraintsClientConfiguration);
    }

    @Override
    public Optional<UserConstraintsUI> getUserConstraints(String resourceId) {
        Future<Optional<UserConstraintsUI>> futureUserConstraints = getUserConstraintsAsync(resourceId);
        return extractOptionalAsyncResult(futureUserConstraints, "could not get user Constraint");
    }

    @Override
    public Boolean saveUserConstraints(UserConstraintsUI userConstraintsUI) {
        Future<Boolean> futureSaved = saveUserConstraintsAsync(userConstraintsUI);
        return extractAsyncResult(futureSaved, "could not save user constraint", false);
    }

    @Override
    public Future<Optional<UserConstraintsUI>> getUserConstraintsAsync(String resourceId) {
        String url = baseUrl + "/{assetId}";
        log.debug("getting from URL '{}'", url);

        return super.supplyAsync(() -> {
                    Response response =
                            given()
                                    .pathParam("assetId", resourceId)
                            .when()
                                    .get(url)
                            .then()
                            .extract()
                                    .response();

                    if (response.getStatusCode() != HttpStatus.OK.value())
                        return Optional.empty();

                    UserConstraintsUI userConstraintsUI = response.body().as(UserConstraintsUI.class);
                    if (!userConstraintsUI.getEnabled())
                        return Optional.empty();

                    return Optional.of(userConstraintsUI);
                }
        );
    }

    @Override
    public Future<Boolean> saveUserConstraintsAsync(UserConstraintsUI userConstraintsUI) {
        String url = baseUrl + "/save";
        log.debug("posting to URL '{}'", url);

        return super.supplyAsync(() -> {
                    Response response =
                            given()
                                    .contentType(ContentType.JSON)
                                    .body(userConstraintsUI)
                            .when()
                                    .post(url)
                            .then()
                            .assertThat()
                                    .statusCode(HttpStatus.OK.value())
                            .extract()
                                    .response();

                    UserConstraintSaveResult result = response.body().as(UserConstraintSaveResult.class);
                    log.debug("post user constraint result: '{}'", result.isSuccess());
                    return result.isSuccess();
                }
        );
    }
}
