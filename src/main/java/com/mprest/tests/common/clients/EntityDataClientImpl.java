package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.EntityDataClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.data.GenieEntityDataCollectionClient;
import com.mprest.tests.common.data.GenieEntityResponseClient;
import com.mprest.tests.common.utilities.BaseServiceClient;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.concurrent.Future;

import static io.restassured.RestAssured.given;

@Slf4j
public class EntityDataClientImpl extends BaseServiceClient implements EntityDataClient {

    private String entityManagerUrl;
    private String minimalTopologyRequestBody;
    private final static String ENTITY_MANAGER_URL_SUFFIX = "/entities/filter/";

    EntityDataClientImpl(SecuredServerConfiguration secured, EntityDataClientConfiguration clientConfig) {
        super(EntityDataClientImpl.class, secured, clientConfig);
        this.entityManagerUrl = clientConfig.getEntityManagerUrl();
        this.minimalTopologyRequestBody = clientConfig.getMinimalTopologyBody();
    }

    @Override
    public GenieEntityDataCollectionClient getEntities(String type) {
        Future<GenieEntityDataCollectionClient> futureEntities = getEntitiesAsync(type);
        return extractAsyncResult(futureEntities, "Could not get entities");
    }

    @Override
    public Future<GenieEntityDataCollectionClient> getEntitiesAsync(String type) {
        String url = baseUrl + "/getEntities/" + type;
        log.debug("Getting entities data from URL '{}'", url);
        return super.supplyAsync(() -> {
                    Response response =
                            given().
                                    when().
                                    get(url).
                                    then().
                                    assertThat().
                                    statusCode(HttpStatus.OK.value()).
                                    extract().
                                    response();
                    String result = response.body().asString();
                    log.debug("get entities string result: '{}'", result);
                    return response.body().as(GenieEntityResponseClient.class).getEntities();
                }
        );
    }

    public GenieEntityDataCollectionClient getMinimalTopology() {
        Future<GenieEntityDataCollectionClient> futureMinimalTopologyAsync = this.getMinimalTopologyAsync();
        return extractAsyncResult(futureMinimalTopologyAsync, "Could not get minimal topology");
    }

    @Override
    public Future<GenieEntityDataCollectionClient> getMinimalTopologyAsync() {
        String minimalTopologyRequestUrl = this.entityManagerUrl + ENTITY_MANAGER_URL_SUFFIX;
        log.debug("Getting entities data from URL '{}'", minimalTopologyRequestUrl);

        return super.supplyAsync(() -> {
                    Response response =
                            given().
                                    contentType(ContentType.JSON).
                                    body(this.minimalTopologyRequestBody).
                                    when().
                                    post(minimalTopologyRequestUrl).
                                    then().
                                    assertThat().
                                    statusCode(HttpStatus.OK.value()).
                                    extract().
                                    response();
                    String result = response.body().asString();
                    log.debug("get minimal topology string result: '{}'", result);
                    return response.body().as(GenieEntityResponseClient.class).getEntities();
                }
        );
    }

    @Override
    public Boolean deleteEntities(String type) {
        Future<Boolean> futureEntities = deleteEntitiesAsync(type);
        return extractAsyncResult(futureEntities, "Could not delete entities");
    }

    @Override
    public Future<Boolean> deleteEntitiesAsync(String type) {
        String url = baseUrl + "/deleteEntities/" + type;
        log.debug("Deleting entities data from URL '{}'", url);
        return super.supplyAsync(() -> {
                    Response response =
                            given().
                                    when().
                                    delete(url).
                                    then().
                                    assertThat().
                                    statusCode(HttpStatus.OK.value()).
                                    extract().
                                    response();
                    return response.body().as(Boolean.class);
                }
        );
    }
}
