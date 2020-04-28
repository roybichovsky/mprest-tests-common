package com.mprest.tests.common.clients;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mprest.tests.common.configuration.AssetsClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.data.NetworkConstraint;
import com.mprest.tests.common.data.assets.Asset;
import com.mprest.tests.common.data.assets.AssetGeoCoordinate;
import com.mprest.tests.common.utilities.BaseServiceClient;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;

import static io.restassured.RestAssured.given;

@Slf4j
public class AssetsClientImpl extends BaseServiceClient implements AssetsClient {

    private final ObjectMapper mapper = new ObjectMapper();
    private final AssetsClientConfiguration assetsClientConfiguration;

    public AssetsClientImpl(SecuredServerConfiguration securedConfiguration, AssetsClientConfiguration assetsClientConfiguration) {
        super(AssetsClientImpl.class, securedConfiguration, assetsClientConfiguration);
        this.assetsClientConfiguration = assetsClientConfiguration;
    }

    @Override
    public Optional<Asset> getAsset(String assetId, Class<?> clazz) {
        return getOptionalObjectByContext(assetId, assetsClientConfiguration.getAssetContext(), clazz);
    }

    @Override
    public Future<Optional<Asset>> getAssetAsync(String assetId, Class<?> clazz) {
        return getOptionalObjectByContextAsync(assetId, assetsClientConfiguration.getAssetContext());
    }

    @Override
    public Optional<AssetGeoCoordinate> getAssetGeo(String assetId) {
        return getOptionalObjectByContext(assetId, assetsClientConfiguration.getAssetGeo(), AssetGeoCoordinate.class);
    }

    @Override
    public Future<Optional<AssetGeoCoordinate>> getAssetGeoAsync(String assetId) {
        return getOptionalObjectByContextAsync(assetId, assetsClientConfiguration.getAssetGeo());
    }

    @Override
    public Optional<NetworkConstraint> getAssetNetworkConstraint(String assetId) {
        return getOptionalObjectByContext(assetId, assetsClientConfiguration.getNetworkConstraintContext(), NetworkConstraint.class);
    }

    @Override
    public Future<Optional<NetworkConstraint>> getAssetNetworkConstraintAsync(String assetId) {
        return getOptionalObjectByContextAsync(assetId, assetsClientConfiguration.getNetworkConstraintContext());
    }

    private <T> Optional<T> getOptionalObjectByContext(String assetId, String context, Class<?> clazz) {
        Optional<Object> objectByContext = getObjectByContext(assetId, context);
        if (!objectByContext.isPresent()) {
            return Optional.empty();
        }
        try {
            JavaType javaType = mapper.getTypeFactory().constructSimpleType(clazz, null);
            T value = mapper.convertValue(objectByContext.get(), javaType);
            return Optional.of(value);
        } catch (Exception e) {
            log.error("Could not get value for asset id '{}' for context '{}'. {}", assetId, context, e);
        }
        return Optional.empty();
    }

    private <T> Optional<T> getObjectByContext(String assetId, String context) {
        Future<Optional<T>> objectByContext = getOptionalObjectByContextAsync(assetId, context);
        return extractOptionalAsyncResult(objectByContext, String.format("Could not get asset '%s' by context '%s'", assetId, context));
    }

    private <T> Future<Optional<T>> getOptionalObjectByContextAsync(String assetId, String context) {
        String url = baseUrl + "/query";
        log.debug("getting asset data by context '{}' from URL '{}'", context, url);

        return super.supplyAsync(() -> {
            Response response =
                    given()
                            .queryParam("assetId", assetId)
                            .queryParam("context", context)
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

            Map<String, Map<String, Object>> result = body.as(Map.class);
            if (result == null || result.size() == 0)
                return Optional.empty();

            Map<String, Object> contextMap = result.get(assetId);
            if (contextMap == null)
                return Optional.empty();

            T contextObject = (T) contextMap.get(context);
            if (contextObject == null) {
                return Optional.empty();
            }

            return Optional.of(contextObject);
        });
    }
}

