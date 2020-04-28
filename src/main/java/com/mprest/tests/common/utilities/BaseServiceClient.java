package com.mprest.tests.common.utilities;

import com.mprest.tests.common.configuration.BaseClientConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
public abstract class BaseServiceClient {

    protected final Duration WEB_REQUEST_TIMEOUT = Duration.ofSeconds(60);

    private static final String HTTPS = "https";
    private static final String HTTP = "http";

    protected final String baseUrl;

    private final SecuredServerConfiguration securedServerConfiguration;
    private SecuredServerHandshake securedServerHandshake;

    private String accessToken;

    public BaseServiceClient(Class<?> clazz, SecuredServerConfiguration securedServerConfiguration, BaseClientConfiguration clientConfig) {
        this.securedServerConfiguration = securedServerConfiguration;

        String scheme = isSecured() ? HTTPS : HTTP;
        baseUrl = getBaseUrl(scheme, clientConfig.getHost(), clientConfig.getPort(), clientConfig.getPath(), clientConfig.getPathPrefix());

        if (isSecured()) {
            securedServerHandshake = new SecuredServerHandshake(this.securedServerConfiguration);
        }

        log.info("'{}': base remote service URL is '{}', isSecured='{}'", clazz.getSimpleName(), baseUrl, securedServerConfiguration.isEnabled());
    }

    private String getBaseUrl(String scheme, String host, int port, String path, String pathPrefix) {
        URIBuilder builder = new URIBuilder()
                .setScheme(scheme)
                .setHost(host)
                .setPort(port)
                .setPath(securedServerConfiguration.isEnabled() ? pathPrefix + path : path);
        return builder.toString();
    }

    protected boolean isSecured() {
        return securedServerConfiguration.isEnabled();
    }

    protected Map<String, String> getHeadersWithUser() {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Origin-Id", "1");
        headers.put("X-Node-Id", "1");
        headers.put("X-Tenant-Id", "mprest");
        headers.put("X-Source-Id", "1");
        headers.put("X-Username", getUserName());
        return headers;
    }

    protected String getUserName() {
        return securedServerConfiguration.getUserName();
    }

    protected String getPassword() {
        return securedServerConfiguration.getPassword();
    }

    protected <T> Future<T> supplyAsync(Supplier<T> supplier) {
        try {
            if (isSecured()) {
//                if (StringUtils.isEmpty(accessToken)) {
                    accessToken = "Bearer " + securedServerHandshake.getAccessToken(getUserName(), getPassword());
//                }

                RestAssured.useRelaxedHTTPSValidation();
                RestAssured.authentication = RestAssured.oauth2(accessToken);
            }

            RestAssured.requestSpecification = new RequestSpecBuilder()
                    .addHeaders(getHeadersWithUser())
                    .build();

            return CompletableFuture
                    .supplyAsync(supplier)
                    .thenApply(t -> {
                            RestAssured.reset();
                        return t;
                    });
        } catch (Exception ex) {
            log.error("Failed to async invoke request", ex);
            return null;
        }
    }

    protected <T> T extractAsyncResult(Future<T> futureResult, String errorMessage) {
        try {
            return futureResult.get(WEB_REQUEST_TIMEOUT.getSeconds(), TimeUnit.SECONDS);
        } catch (Throwable err) {
            log.error(errorMessage, err);
            return null;
        }
    }

    protected <T> T extractAsyncResult(Future<T> futureResult, String errorMessage, T defaultReturnValue) {
        try {
            return futureResult.get(WEB_REQUEST_TIMEOUT.getSeconds(), TimeUnit.SECONDS);
        } catch (Throwable err) {
            log.error(errorMessage, err);
            return defaultReturnValue;
        }
    }

    protected <T> T extractAsyncResult(Future<T> futureResult, String errorMessage, Duration timeout, T defaultReturnValue) {
        try {
            return futureResult.get(timeout.getSeconds(), TimeUnit.SECONDS);
        } catch (Throwable err) {
            log.error(errorMessage, err);
            return defaultReturnValue;
        }
    }

    protected <T> Optional<T> extractOptionalAsyncResult(Future<Optional<T>> futureResult, String errorMessage) {
        try {
            return futureResult.get(WEB_REQUEST_TIMEOUT.getSeconds(), TimeUnit.SECONDS);
        } catch (Throwable err) {
            log.error(errorMessage, err);
            return Optional.empty();
        }
    }

    protected <T> Optional<T> extractOptionalAsyncResult(Future<T> futureResult, String errorMessage, Duration timeout) {
        try {
            T result = futureResult.get(timeout.getSeconds(), TimeUnit.SECONDS);
            return Optional.ofNullable(result);
        } catch (Throwable err) {
            log.error(errorMessage, err);
            return Optional.empty();
        }
    }
}
