package com.mprest.tests.common.clients;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.configuration.TopologyDerAssociationClientConfiguration;
import com.mprest.tests.common.data.KeyInfo;
import com.mprest.tests.common.utilities.BaseServiceClient;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;

import static io.restassured.RestAssured.given;

@Slf4j
public class TopologyDerAssociationClientImpl extends BaseServiceClient implements TopologyDerAssociationClient {

    private ObjectMapper mapper;
    private TypeReference<Set<KeyInfo>> typeReference;

    TopologyDerAssociationClientImpl(SecuredServerConfiguration securedConfiguration, TopologyDerAssociationClientConfiguration topologyDerAssociationClientConfiguration) {
        super(TopologyDerAssociationClientImpl.class, securedConfiguration, topologyDerAssociationClientConfiguration);

        this.mapper = new ObjectMapper();
        this.typeReference = new TypeReference<Set<KeyInfo>>() {
        };
    }

    @Override
    public Optional<Set<KeyInfo>> getDerAssociation(String assetId) {
        Future<Optional<Set<KeyInfo>>> futureAsset = getDerAssociationAsync(assetId);
        return extractOptionalAsyncResult(futureAsset, "could not get Der asset");
    }

    @Override
    public Future<Optional<Set<KeyInfo>>> getDerAssociationAsync(String assetId) {
        String url = baseUrl + "/derAssociation/{assetId}";
        log.debug("getting asset from URL '{}'", url);

        return super.supplyAsync(() -> {
            Response response =
                    given()
                        .pathParam("assetId", assetId)
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
                Set<KeyInfo> keyInfoSet = mapper.readValue(body.asString(), typeReference);
                return Optional.of(keyInfoSet);
            } catch (IOException e) {
                return Optional.empty();
            }
        });
    }
}
