package com.mprest.tests.common.clients;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.configuration.SystemEventsClientConfiguration;
import com.mprest.tests.common.data.SystemEvent.SystemEvent;
import com.mprest.tests.common.utilities.BaseServiceClient;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.Future;

import static io.restassured.RestAssured.given;

@Slf4j
public class SystemEventsClientImpl extends BaseServiceClient implements SystemEventsClient {

    private final ObjectMapper mapper;
    private final TypeReference<SystemEvent[]> typeReference;

    SystemEventsClientImpl(SecuredServerConfiguration securedConfiguration, SystemEventsClientConfiguration systemEventsClientConfiguration) {
        super(SystemEventsClientImpl.class, securedConfiguration, systemEventsClientConfiguration);

        this.mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        this.typeReference = new TypeReference<SystemEvent[]>() {
        };
    }

    @Override
    public Optional<SystemEvent[]> getSystemEventsBetweenDates(Instant from, Instant to) {
        Future<Optional<SystemEvent[]>> listFuture = getSystemEventsBetweenDatesAsync(from, to);
        return extractOptionalAsyncResult(listFuture, "failed to get System Events list");
    }

    @Override
    public Future<Optional<SystemEvent[]>> getSystemEventsBetweenDatesAsync(Instant from, Instant to) {
        String url = baseUrl + "/getsystemevents";
        log.debug("getting System Events from URL '{}'", url);

        return super.supplyAsync(() -> {
            Response response =
                    given()
                            .queryParam("from", from.toString())
                            .queryParam("to", to.toString())
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

            try {
                String serializedSystemEvents = body.asString();

                SystemEvent[] deserializedSystemEvents = mapper.readValue(serializedSystemEvents, typeReference);
                if (deserializedSystemEvents == null)
                    return Optional.empty();

                return Optional.of(deserializedSystemEvents);
            } catch (Exception e) {
                log.error("Error trying to parse system events\n" + body.asString(), e);
                return Optional.empty();
            }
        });
    }
}