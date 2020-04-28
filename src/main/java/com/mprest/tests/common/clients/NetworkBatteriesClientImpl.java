package com.mprest.tests.common.clients;

import com.mprest.tests.common.configuration.NetworkBatteriesConfiguration;
import com.mprest.tests.common.configuration.SecuredServerConfiguration;
import com.mprest.tests.common.data.UploadResult;
import com.mprest.tests.common.utilities.BaseServiceClient;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.Future;

import static io.restassured.RestAssured.given;

@Slf4j
public class NetworkBatteriesClientImpl extends BaseServiceClient implements NetworkBatteriesClient {

    NetworkBatteriesClientImpl(SecuredServerConfiguration secured, NetworkBatteriesConfiguration clientConfig) {
        super(NetworkBatteriesClientImpl.class, secured, clientConfig);
    }

    @Override
    public UploadResult uploadNetworkBatteries(String fileName, byte[] bytes) {
        return uploadAsync(fileName, bytes, "ul");
    }

    private UploadResult uploadAsync(String fileName, byte[] bytes, String fileUrl) {
        Future<UploadResult> file = uploadFile(fileName, bytes, fileUrl);
        return extractAsyncResult(file, "Upload failed", Duration.ofSeconds(120), null);
    }

    private Future<UploadResult> uploadFile(String fileName, byte[] bytes, String fileUrl) {
        String url = baseUrl + "/" + fileUrl;
        log.debug("posting to URL '{}'", url);

        return super.supplyAsync(() -> {
                    Response response =
                            given()
                                    .multiPart("file", fileName, bytes)
                            .when()
                                    .post(url)
                            .then()
                            .extract()
                                    .response();

                    Map result = response.body().as(Map.class);
                    log.info("Result from upload network batteries file '{}'", result.toString());

                    boolean success = result.get("success") != null && Boolean.parseBoolean(result.get("success").toString());
                    String message = result.get("message") == null ? "" : result.get("message").toString();

                    return UploadResult.builder()
                            .success(success)
                            .message(message)
                            .build();
                }
        );
    }
}