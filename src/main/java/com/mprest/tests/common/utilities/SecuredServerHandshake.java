package com.mprest.tests.common.utilities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

class SecuredServerHandshake {
    private static final String HTTPS = "https";

    private final SecuredServerConfiguration securedServerConfiguration;
    private final String apiManagerUrl;

    public SecuredServerHandshake(SecuredServerConfiguration secured) {
        securedServerConfiguration = secured;
        apiManagerUrl = getBaseUrl(securedServerConfiguration.getApiManagerHost(), securedServerConfiguration.getApiManagerPort());
    }

    private String getBaseUrl(String host, int port) {
        return new URIBuilder().setScheme(HTTPS).setHost(host).setPort(port).toString();
    }

    String getAccessToken(String userName, String password) {
        Response response =
                given()
                        .relaxedHTTPSValidation()
                        .auth()
                            .preemptive()
                            .basic(securedServerConfiguration.getClientId(), securedServerConfiguration.getClientSecret())
                        .contentType(ContentType.URLENC)
                        .formParam("grant_type", "password")
                        .formParam("username", userName)
                        .formParam("password", password)
                .when()
                        .post(apiManagerUrl + "/oauth2/token")
                .then()
                .assertThat()
                        .statusCode(HttpStatus.OK.value())
                .extract()
                        .response();

        SecurityTokenResponse securityTokenResponse = response.body().as(SecurityTokenResponse.class);
        return securityTokenResponse.getAccessToken();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class SecurityTokenResponse {
        @JsonProperty("access_token") private String accessToken;
        @JsonProperty("refresh_token") private String refreshToken;
        @JsonProperty("scope") private String scope;
        @JsonProperty("token_type") private String tokenType;
        @JsonProperty("expires_in") private int expiresInSeconds;
    }
}
