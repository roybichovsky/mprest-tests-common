package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.ReadOnlyClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.data.ReadOnlyRequest;
import com.mprest.tests.common.utilities.BaseServiceClient;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.concurrent.Future;

import static io.restassured.RestAssured.given;

@Slf4j
public class ReadOnlyClientImpl extends BaseServiceClient implements ReadOnlyClient {

    public ReadOnlyClientImpl(SecuredServerConfiguration secured, ReadOnlyClientConfiguration clientConfig) {
        super(ReadOnlyClientImpl.class, secured, clientConfig);
    }

    @Override
    public Boolean setReadOnlyMode(ReadOnlyRequest request) {
        Future<Boolean> futureResult = setReadOnlyModeAsync(request);
        return extractAsyncResult(futureResult, "Failed to set Read-Only mode");
    }

    @Override
    public Future<Boolean> setReadOnlyModeAsync(ReadOnlyRequest requestData) {
        String url = baseUrl;
        log.debug("Set Read-Only mode async from URL '{}'", url);

        return super.supplyAsync(() -> {
            given()
                    .contentType(ContentType.JSON)
                    .body(requestData)
            .when()
                    .post(url)
            .then()
            .assertThat()
                    .statusCode(HttpStatus.OK.value());
            return true;
        });
    }
}
