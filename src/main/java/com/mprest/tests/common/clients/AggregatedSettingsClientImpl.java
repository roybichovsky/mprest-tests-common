package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.AggregatedSettingsClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.data.settings.AggregatedSettings;
import com.mprest.tests.common.utilities.BaseServiceClient;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.concurrent.Future;

import static io.restassured.RestAssured.given;

@Slf4j
public class AggregatedSettingsClientImpl extends BaseServiceClient implements AggregatedSettingsClient {
    public AggregatedSettingsClientImpl(SecuredServerConfiguration secured, AggregatedSettingsClientConfiguration clientConfiguration) {
        super(AggregatedSettingsClientImpl.class, secured, clientConfiguration);
    }

    @Override
    public AggregatedSettings getSettings() {
        Future<AggregatedSettings> futureResult = getSettingsAsync();
        return extractAsyncResult(futureResult, "Failed to get aggregated settings");
    }

    @Override
    public Future<AggregatedSettings> getSettingsAsync() {
        String url = baseUrl;
        log.debug("Get aggregated settings async from URL '{}'", url);

        return super.supplyAsync(() -> {
            Response response = given()
                    .when()
                    .get(url)
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .response();

            return response.as(AggregatedSettings.class);
        });
    }
}
